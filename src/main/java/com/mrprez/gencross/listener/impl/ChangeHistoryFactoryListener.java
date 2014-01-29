package com.mrprez.gencross.listener.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.history.HistoryFactory;
import com.mrprez.gencross.listener.PassToNextPhaseListener;

public class ChangeHistoryFactoryListener extends PassToNextPhaseListener {
	private String propertyName;
	private String className;
	private String pointPool;
	private Map<String, String> historyArgs;
	private HistoryFactory historyFactory;
	

	@Override
	public void callPassToNextPhase() throws Exception {
		Property property = getPersonnage().getProperty(propertyName);
		property.setHistoryFactory(historyFactory);
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String, String> args = new HashMap<String, String>();
		args.putAll(historyArgs);
		args.put("class", className);
		args.put("pointPool", pointPool);
		args.put("property", propertyName);
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		className = args.get("class");
		propertyName = args.get("property");
		pointPool = args.get("pointPool");
		historyArgs = args;
		historyArgs.remove("property");
		historyArgs.remove("pointPool");
		historyArgs.remove("class");
		historyFactory = createHistoryFactory();
	}
	
	private HistoryFactory createHistoryFactory() throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException{
		DefaultElement element = new DefaultElement("historyFactory");
		element.addAttribute("class", className);
		element.addElement("pointPool").setText(pointPool);
		Element argsEl = element.addElement("args");
		for(String key : historyArgs.keySet()){
			Element argEl = argsEl.addElement("arg");
			argEl.addAttribute("key", key);
			argEl.setText(historyArgs.get(key));
		}
		Class<? extends HistoryFactory> historyFactoryClass = getPersonnage().getHistoryFactoryClass(className);
		return historyFactoryClass.getConstructor(Element.class).newInstance(element);
	}

}
