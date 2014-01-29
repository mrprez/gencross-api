package com.mrprez.gencross.listener;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.listener.custom.CustomAfterAddPropertyListener;
import com.mrprez.gencross.listener.custom.CustomAfterChangeValueListener;
import com.mrprez.gencross.listener.custom.CustomAfterDeletePropertyListener;
import com.mrprez.gencross.listener.custom.CustomBeforeAddPropertyListener;
import com.mrprez.gencross.listener.custom.CustomBeforeChangeValueListener;
import com.mrprez.gencross.listener.custom.CustomBeforeDeletePropertyListener;


public abstract class PropertyListener extends Listener {
	protected Set<String> phases;
	private String pattern;
	

	
	@SuppressWarnings("unchecked")
	public static Listener buildListener(Element element, Personnage personnage) throws Exception{
		PropertyListener listener = null;
		Map<String, String> args = new HashMap<String, String>();
		String className = element.attributeValue("class");
		if(className!=null){
			Class<? extends PropertyListener> clazz = (Class<? extends PropertyListener>) Listener.class.getClassLoader().loadClass(className);
			listener = clazz.newInstance();
			Iterator<?> it = element.elementIterator("arg");
			while(it.hasNext()){
				Element argEl = (Element) it.next();
				args.put(argEl.attributeValue("name"), argEl.getText());
			}
		}else{
			String listenerGroup = element.getParent().getName();
			if(listenerGroup.equals("beforeChangeValueListeners")){
				listener = new CustomBeforeChangeValueListener();
			}else if(listenerGroup.equals("afterChangeValueListeners")){
				listener = new CustomAfterChangeValueListener();
			}else if(listenerGroup.equals("beforeAddPropertyListeners")){
				listener = new CustomBeforeAddPropertyListener();
			}else if(listenerGroup.equals("afterAddPropertyListeners")){
				listener = new CustomAfterAddPropertyListener();
			}else if(listenerGroup.equals("beforeDeletePropertyListeners")){
				listener = new CustomBeforeDeletePropertyListener();
			}else if(listenerGroup.equals("afterDeletePropertyListeners")){
				listener = new CustomAfterDeletePropertyListener();
			}
			args.put("method", element.element("method").getTextTrim());
		}
		listener.setPersonnage(personnage);
		listener.setArgs(args);
		listener.setName(element.attributeValue("name"));
		listener.setPattern(element.element("pattern").getText());
		if(element.element("phase")!=null){
			listener.phases = new HashSet<String>();
			Iterator<?> it = element.elementIterator("phase");
			while(it.hasNext()){
				listener.phases.add(((Element)it.next()).getText());
			}
		}
		
		return listener;
	}
	
	@Override
	public PropertyListener clone() throws CloneNotSupportedException {
		PropertyListener clone;
		try {
			clone = this.getClass().newInstance();
			clone.setPersonnage(this.getPersonnage());
			clone.setPattern(pattern);
			clone.setArgs(this.getArgs());
			if(getPhases()!=null){
				clone.setPhases(new HashSet<String>());
				for(String phase : getPhases()){
					clone.getPhases().add(new String(phase));
				}
			}
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
		element.addElement("pattern").setText(getPattern());
		Map<String, String> args = getArgs();
		for(String key : args.keySet()){
			Element argEl = element.addElement("arg");
			argEl.addAttribute("name", key);
			argEl.setText(args.get(key));
		}
		if(phases!=null){
			for(String phase : phases){
				element.addElement("phase").setText(phase);
			}
		}
		return element;
	}
	
	public Collection<String> getPhases(){
		return phases;
	}
	protected void setPhases(Set<String> phases) {
		this.phases = phases;
	}
	
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}
