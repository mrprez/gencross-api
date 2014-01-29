package com.mrprez.gencross.listener.custom;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.BeforeDeletePropertyListener;

public class CustomBeforeDeletePropertyListener extends BeforeDeletePropertyListener {
	private Method method;
	
	
	public CustomBeforeDeletePropertyListener(){
		super();
	}
	
	public CustomBeforeDeletePropertyListener(Personnage personnage, String methodName, String pattern) throws SecurityException, NoSuchMethodException{
		super();
		setPersonnage(personnage);
		method = personnage.getClass().getMethod(methodName, Property.class);
		setPattern(pattern);
	}

	@Override
	public boolean callBeforeDeleteProperty(Property property) throws Exception {
		Boolean result = (Boolean) method.invoke(getPersonnage(), property);
		return result.booleanValue();
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String, String> args = new HashMap<String, String>();
		args.put("method", method.getName());
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		method = getPersonnage().getClass().getMethod(args.get("method"), Property.class);
	}
	
	

}
