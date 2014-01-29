package com.mrprez.gencross.history;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.mrprez.gencross.value.Value;

public abstract class HistoryFactory {
	public static final HistoryFactory FREE_HISTORY_FACTORY = new FreeHistoryFactory("");
	protected String pointPool;
	
	
	public HistoryFactory(String pointPool){
		super();
		this.pointPool = pointPool;
	}
	
	public HistoryFactory(Element element){
		super();
		pointPool = element.element("pointPool").getText();
		if(element.element("args") != null){
			setArgsXML(element.element("args"));
		}
	}
	
	
	public abstract int getCost(Value oldValue, Value newValue, int action) throws Exception;
	public abstract Map<String, String> getArgs();
	public abstract void setArgs(Map<String, String> args);
	public abstract HistoryFactory clone();
	
	public HistoryItem buildHistoryItem(String absoluteName, Value oldValue, Value newValue, int action, String phase) throws Exception{
		HistoryItem historyItem = new HistoryItem();
		historyItem.setAbsoluteName(absoluteName);
		historyItem.setAction(action);
		historyItem.setCost(getCost(oldValue, newValue, action));
		if(newValue != null){
			historyItem.setNewValue(newValue.clone());
		}
		if(oldValue != null){
			historyItem.setOldValue(oldValue.clone());
		}
		historyItem.setPhase(phase);
		historyItem.setPointPool(pointPool);
		
		return historyItem;
	}
	
	protected Element getArgsXML(){
		Element element = new DefaultElement("args");
		Map<String,String> map = getArgs();
		for(String key : map.keySet()){
			Element valueEl = element.addElement("arg");
			valueEl.addAttribute("key", key);
			valueEl.setText(map.get(key));
		}
		return element;
	}
	
	protected void setArgsXML(Element element){
		Iterator<?> it = element.elementIterator("arg");
		Map<String, String> map = new HashMap<String, String>();
		while(it.hasNext()){
			Element valueEl = (Element) it.next();
			map.put(valueEl.attributeValue("key"),valueEl.getText());
		}
		setArgs(map);
	}
	
	public Element getXML(){
		Element element = new DefaultElement("historyFactory");
		element.addAttribute("class", getClass().getCanonicalName());
		element.addElement("pointPool").setText(pointPool);
		element.add(getArgsXML());
		return element;
	}

	public String getPointPool() {
		return pointPool;
	}
	public void setPointPool(String pointPool) {
		this.pointPool = pointPool;
	}
	
	

}
