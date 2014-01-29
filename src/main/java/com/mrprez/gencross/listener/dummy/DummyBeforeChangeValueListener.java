package com.mrprez.gencross.listener.dummy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.BeforeChangeValueListener;
import com.mrprez.gencross.listener.custom.CustomBeforeChangeValueListener;
import com.mrprez.gencross.value.Value;

public class DummyBeforeChangeValueListener extends BeforeChangeValueListener {
	private Map<String, String> args = new HashMap<String, String>();
	public String className;
	
	
	public DummyBeforeChangeValueListener(Element listenerEl, Personnage personnage){
		super();
		setPersonnage(personnage);
		setName(listenerEl.attributeValue("name"));
		setPattern(listenerEl.element("pattern").getText());
		
		className = listenerEl.attributeValue("class");
		if(className != null){
			Iterator<?> it = listenerEl.elementIterator("arg");
			while(it.hasNext()){
				Element argEl = (Element) it.next();
				args.put(argEl.attributeValue("name"), argEl.getText());
			}
		} else {
			className = CustomBeforeChangeValueListener.class.getName();
			args.put("method", listenerEl.element("method").getTextTrim());
		}
		
		if(listenerEl.element("phase")!=null){
			phases = new HashSet<String>();
			Iterator<?> it = listenerEl.elementIterator("phase");
			while(it.hasNext()){
				phases.add(((Element)it.next()).getText());
			}
		}
	}
	
	@Override
	public boolean callBeforeChangeValue(Property property, Value newValue) throws Exception {
		return true;
	}

	@Override
	public Map<String, String> getArgs() {
		return args;
	}


	@Override
	public void setArgs(Map<String, String> args) throws SecurityException, NoSuchMethodException {
		this.args = args;
	}
	
	@Override
	public Element getXml() {
		Element element = super.getXml();
		element.addAttribute("class", className);
		return element;
	}

}
