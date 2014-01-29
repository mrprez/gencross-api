package com.mrprez.gencross.history;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import com.mrprez.gencross.value.Value;


public class ConstantHistoryFactory extends HistoryFactory{
	private int cost;
	
	
	public ConstantHistoryFactory(String pointPool, int cost) {
		super(pointPool);
		this.cost = cost;
	}

	public ConstantHistoryFactory(Element element) {
		super(element);
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("cost", ""+cost);
		return map;
	}

	@Override
	public void setArgs(Map<String, String> map) {
		cost = Integer.parseInt(map.get("cost"));
	}

	@Override
	public int getCost(Value oldValue, Value newValue, int action) throws Exception {
		switch (action) {
		case HistoryItem.CREATION:
			return cost;
		case HistoryItem.DELETION:
			return -cost;
		default:
			return 0;
		}
	}

	@Override
	public HistoryFactory clone() {
		return new ConstantHistoryFactory(new String(pointPool), cost);
	}

}
