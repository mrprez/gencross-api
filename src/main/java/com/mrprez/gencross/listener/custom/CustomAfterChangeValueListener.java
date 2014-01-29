package com.mrprez.gencross.listener.custom;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.AfterChangeValueListener;
import com.mrprez.gencross.value.Value;

public class CustomAfterChangeValueListener extends AfterChangeValueListener {
	private Method method;
	
	
	public CustomAfterChangeValueListener(){
		super();
	}
	
	public CustomAfterChangeValueListener(Personnage personnage, String methodName, String pattern) throws SecurityException, NoSuchMethodException{
		super();
		setPersonnage(personnage);
		method = personnage.getClass().getMethod(methodName, Property.class, Value.class);
		setPattern(pattern);
	}
	
	@Override
	public void callAfterChangeValue(Property property, Value oldValue) throws Exception {
		method.invoke(getPersonnage(), property, oldValue);
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
