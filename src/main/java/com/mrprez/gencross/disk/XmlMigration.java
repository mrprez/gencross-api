package com.mrprez.gencross.disk;

import java.util.Iterator;

import org.dom4j.Element;

import com.mrprez.gencross.history.HistoryFactory;
import com.mrprez.gencross.value.Value;

public class XmlMigration {
	
	
	public static Element treatXml1_8(Element root) throws Exception{
		if(root.attribute("gencrossVersion") == null){
			Element propertiesEl = root.element("properties");
			Iterator<?> propertyElIt = propertiesEl.elementIterator("property");
			while(propertyElIt.hasNext()){
				Element propertyEl = (Element) propertyElIt.next();
				treatPropertyXml(propertyEl);
			}
			
			for(Object historyItemEl : root.element("history").elements()){
				treatHistoryItemEl((Element) historyItemEl);
			}
		}
		
		root.addAttribute("gencrossVersion", "1.9");
		
		return root;
	}
	
	private static Element treatPropertyXml(Element propertyEl){
		if(propertyEl.element("defaultHistory") != null){
			treatDefaultHistoryXml(propertyEl.element("defaultHistory"));
		}
		
		if(propertyEl.element("propertiesList") != null){
			for(Object subPropertyEl : propertyEl.element("propertiesList").element("properties").elements("property")){
				treatPropertyXml((Element) subPropertyEl);
			}
			if(propertyEl.element("propertiesList").element("defaultProperty") != null){
				treatPropertyXml(propertyEl.element("propertiesList").element("defaultProperty").element("property"));
			}
			if(propertyEl.element("propertyChoice") != null){
				for(Object propertyChoiceEl : propertyEl.element("propertyChoice").elements("property")){
					treatPropertyXml((Element) propertyChoiceEl);
				}
			}
		}
		
		return propertyEl;
	}
	
	private static Element treatDefaultHistoryXml(Element defaultHistoryEl){
		Element historyEl = defaultHistoryEl.element("historyItem");
		defaultHistoryEl.clearContent();
		defaultHistoryEl.add(historyEl.element("pointPool").createCopy());
		defaultHistoryEl.add(historyEl.element("args").createCopy());
		String className = treatClassName(historyEl.attributeValue("class"));
		defaultHistoryEl.addAttribute("class", className);
		defaultHistoryEl.setName("historyFactory");
		
		return defaultHistoryEl;
	}
	
	private static String treatClassName(String className){
		className = className.replace("Item", "Factory");
		if(className.equals("com.mrprez.gencross.history.OneForOneHistoryFactory")){
			return "com.mrprez.gencross.history.ProportionalHistoryFactory";
		}
		if(className.equals("com.mrprez.gencross.history.NForOneHistoryFactory")){
			return "com.mrprez.gencross.history.ProportionalHistoryFactory";
		}
		if(className.equals("com.mrprez.gencross.history.FixedHistoryFactory")){
			return "com.mrprez.gencross.history.ConstantHistoryFactory";
		}
		return className;
	}
	
	private static Element treatHistoryItemEl(Element historyItemEl) throws Exception{
		String className = treatClassName(historyItemEl.attributeValue("class"));
		@SuppressWarnings("unchecked")
		Class<? extends HistoryFactory> hisoryFactoryClass = (Class<? extends HistoryFactory>) Class.forName(className);
		HistoryFactory hisoryFactory = hisoryFactoryClass.getConstructor(Element.class).newInstance(historyItemEl);
		
		historyItemEl.remove(historyItemEl.attribute("class"));
		historyItemEl.remove(historyItemEl.element("args"));
		
		Value oldValue = null;
		if(historyItemEl.element("oldValue")!=null){
			oldValue = Value.createValue(historyItemEl.element("oldValue").element("Value"));
		}
		Value newValue = null;
		if(historyItemEl.element("newValue")!=null){
			newValue = Value.createValue(historyItemEl.element("newValue").element("Value"));
		}
		int action = Integer.parseInt(historyItemEl.attributeValue("action"));
		historyItemEl.addElement("cost").setText(""+hisoryFactory.getCost(oldValue, newValue, action));
		
		if(historyItemEl.element("phase") == null){
			int index = historyItemEl.getParent().elements().indexOf(historyItemEl);
			Element phaseEl = ((Element)historyItemEl.getParent().elements().get(index-1)).element("phase");
			historyItemEl.addElement("phase").setText(phaseEl.getText());
		}
		
		return historyItemEl;
	}

	public static void treatXml1_9(Element rootElement) {
		rootElement.addAttribute("gencrossVersion", "1.10");
		
	}
	
	public static void treatXml1_10(Element rootElement) {
		rootElement.addAttribute("gencrossVersion", "1.11");
		
	}
	
	
	

}
