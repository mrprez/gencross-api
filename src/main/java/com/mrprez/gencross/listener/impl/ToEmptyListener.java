package com.mrprez.gencross.listener.impl;

import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.listener.PassToNextPhaseListener;

/**
 * Ce listener passe la propriété toEmpty d'un pool de point à true ou false. 
 * 
 * @author MrPrez
 *
 */
public class ToEmptyListener extends PassToNextPhaseListener {
	private String pointPool;
	private boolean toEmpty = true;
	
	@Override
	public void callPassToNextPhase() throws Exception {
		super.getPersonnage().getPointPools().get(pointPool).setToEmpty(toEmpty);
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String, String> args = new HashMap<String, String>();
		args.put("pointPool", pointPool);
		args.put("toEmpty", toEmpty?"true":"false");
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		pointPool = args.get("pointPool");
		if(args.containsKey("toEmpty")){
			toEmpty = args.get("toEmpty").equals("true");
		}
	}

}
