package com.mrprez.gencross.listener.impl;

import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.AfterChangeValueListener;
import com.mrprez.gencross.value.Value;

public class AlimentationPropertyListener extends AfterChangeValueListener {
	private String pointPool;
	private Integer factor;
	
	@Override
	public void callAfterChangeValue(Property property, Value oldValue) throws Exception {
		int delta = property.getValue().getInt() - oldValue.getInt();
		if(factor!=null){
			delta = delta * factor;
		}
		getPersonnage().getPointPools().get(pointPool).add(delta);
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String, String> args = new HashMap<String, String>();
		args.put("pointPool", pointPool);
		if(factor!=null){
			args.put("factor", ""+factor);
		}
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		pointPool = args.get("pointPool");
		if(args.containsKey("factor")){
			factor = Integer.valueOf(args.get("factor"));
		}else{
			factor = null;
		}
	}

}
