package com.mrprez.gencross.listener.custom;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.BeforeChangeValueListener;
import com.mrprez.gencross.value.Value;

public class CustomBeforeChangeValueListener extends BeforeChangeValueListener {
	private Method method;
	
	
	public CustomBeforeChangeValueListener(){
		super();
	}
	
	public CustomBeforeChangeValueListener(Personnage personnage, String methodName, String pattern) throws SecurityException, NoSuchMethodException{
		super();
		setPersonnage(personnage);
		method = personnage.getClass().getMethod(methodName, Property.class, Value.class);
		setPattern(pattern);
	}
	
	@Override
	public boolean callBeforeChangeValue(Property property, Value newValue) throws Exception {
		return ((Boolean)method.invoke(getPersonnage(), property, newValue)).booleanValue();
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String, String> args = new HashMap<String, String>();
		args.put("method", method.getName());
		return args;
	}


	@Override
	public void setArgs(Map<String, String> args) throws SecurityException, NoSuchMethodException {
		method = getPersonnage().getClass().getMethod(args.get("method"), Property.class, Value.class);
	}

}
