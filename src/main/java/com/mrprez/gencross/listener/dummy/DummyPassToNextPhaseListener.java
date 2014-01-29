package com.mrprez.gencross.listener.dummy;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.listener.PassToNextPhaseListener;
import com.mrprez.gencross.listener.custom.CustomBeforeChangeValueListener;

public class DummyPassToNextPhaseListener extends PassToNextPhaseListener {
	private Map<String, String> args = new HashMap<String, String>();
	private String className;
	
	
	public DummyPassToNextPhaseListener(){
		super();
	}
	
	public DummyPassToNextPhaseListener(Element listenerEl, Personnage personnage) {
		super();
		setPersonnage(personnage);
		setName(listenerEl.attributeValue("name"));
		
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
		
		if(listenerEl.element("phase")==null && listenerEl.element("pattern")!=null){
			listenerEl.addElement("phase").setText(listenerEl.element("pattern").getText());
		}
		setPhase(listenerEl.element("phase").getText());
	}
	
	@Override
	public void callPassToNextPhase() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		;
	}

	@Override
	public Map<String, String> getArgs() {
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		this.args = args;
	}
	
	@Override
	public Element getXml() {
		Element element = super.getXml();
		element.addAttribute("class", className);
		return element;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	

}
