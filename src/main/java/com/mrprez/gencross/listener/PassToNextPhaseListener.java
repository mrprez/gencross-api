package com.mrprez.gencross.listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.listener.custom.CustomPassToNextPhaseListener;


public abstract class PassToNextPhaseListener extends Listener {
	private String phase;
	
	
	public abstract void callPassToNextPhase() throws Exception;


	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}

	@SuppressWarnings("unchecked")
	public static PassToNextPhaseListener buildListener(Element element, Personnage personnage) throws Exception{
		PassToNextPhaseListener listener = null;
		Map<String, String> args = new HashMap<String, String>();
		String className = element.attributeValue("class");
		if(className!=null){
			Class<? extends PassToNextPhaseListener> clazz = (Class<? extends PassToNextPhaseListener>) Listener.class.getClassLoader().loadClass(className);
			listener = clazz.newInstance();
			Iterator<?> it = element.elementIterator("arg");
			while(it.hasNext()){
				Element argEl = (Element) it.next();
				args.put(argEl.attributeValue("name"), argEl.getText());
			}
		}else{
			listener = new CustomPassToNextPhaseListener();
			args.put("method", element.element("method").getText());
		}
		if(element.element("phase")==null && element.element("pattern")!=null){
			element.addElement("phase").setText(element.element("pattern").getText());
		}
		listener.setPersonnage(personnage);
		listener.setName(element.attributeValue("name"));
		listener.setArgs(args);
		listener.setPhase(element.element("phase").getText());
		return listener;
	}
	
	
	@Override
	public PassToNextPhaseListener clone() throws CloneNotSupportedException {
		PassToNextPhaseListener clone;
		try {
			clone = this.getClass().newInstance();
			clone.setPersonnage(this.getPersonnage());
			clone.setPhase(new String(phase));
			clone.setArgs(this.getArgs());
			if(getName()!=null){
				clone.setName(new String(getName()));
			}
		} catch (Exception e) {
			CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(e);
			throw cnse;
		}
		return clone;
	}
	
	public Element getXml(){
		DefaultElement element = new DefaultElement("listener");
		element.addAttribute("class", this.getClass().getCanonicalName());
		element.addElement("phase").setText(getPhase());
		Map<String, String> args = getArgs();
		for(String key : args.keySet()){
			Element argEl = element.addElement("arg");
			argEl.addAttribute("name", key);
			argEl.setText(args.get(key));
		}
		return element;
	}
	
	
	

}
