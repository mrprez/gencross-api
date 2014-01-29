package com.mrprez.gencross.listener.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.listener.PassToNextPhaseListener;

public class CustomPassToNextPhaseListener extends PassToNextPhaseListener {
	private Method method;
	
	
	public CustomPassToNextPhaseListener(){
		super();
	}
	
	public CustomPassToNextPhaseListener(Personnage personnage, String methodName, String phase) throws SecurityException, NoSuchMethodException{
		super();
		setPersonnage(personnage);
		method = personnage.getClass().getMethod(methodName);
		setPhase(phase);
	}
	
	@Override
	public void callPassToNextPhase() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		method.invoke(getPersonnage());
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String, String> args = new HashMap<String, String>();
		args.put("method", method.getName());
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		method = getPersonnage().getClass().getMethod(args.get("method"));
	}

}
