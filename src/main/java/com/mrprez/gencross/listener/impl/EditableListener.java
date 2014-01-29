package com.mrprez.gencross.listener.impl;

import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.PassToNextPhaseListener;

public class EditableListener extends PassToNextPhaseListener {
	private String propertyName;
	private String pattern;
	private boolean editable;

	@Override
	public void callPassToNextPhase() throws Exception {
		if(propertyName != null){
			getPersonnage().getProperty(propertyName).setEditable(editable);
		}
		if(pattern != null){
			for(Property property : getPersonnage().getProperties()){
				treatProperty(property);
			}
		}
	}
	
	private void treatProperty(Property property){
		if(property.getAbsoluteName().matches(pattern)){
			property.setEditable(editable);
		}
		if(property.getSubProperties()!=null){
			for(Property subProperty : property.getSubProperties()){
				treatProperty(subProperty);
			}
			if(property.getSubProperties().getDefaultProperty()!=null){
				treatProperty(property.getSubProperties().getDefaultProperty());
			}
			if(property.getSubProperties().getOptions()!=null){
				for(Property option : property.getSubProperties().getOptions().values()){
					treatProperty(option);
				}
			}
		}
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String, String> args = new HashMap<String, String>();
		if(propertyName != null){
			args.put("propertyName", propertyName);
		}
		if(pattern != null){
			args.put("pattern", pattern);
		}
		args.put("editable", editable?"true":"false");
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		propertyName = args.get("propertyName");
		pattern = args.get("pattern");
		editable = "true".equals(args.get("editable"));
	}

}
