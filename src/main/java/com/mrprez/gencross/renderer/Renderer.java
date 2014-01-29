package com.mrprez.gencross.renderer;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.value.Value;

public class Renderer {
	public static final Renderer DEFAULT_RENDERER = new Renderer();
	
	
	public String displayValue(Value value){
		return value.toString();
	}
	
	public String displayName(String propertyName){
		return propertyName;
	}

	public String displayValue(Property property){
		if(property.getValue()==null){
			return "";
		}
		return property.getValue().toString();
	}
	
	public String displayName(Property property){
		return property.getFullName();
	}
	
	public Renderer copy(){
		try {
			return getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getName(){
		return getClass().getName();
	}
	
}
