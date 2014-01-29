package com.mrprez.gencross.history;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import com.mrprez.gencross.value.IntValue;
import com.mrprez.gencross.value.Value;

public class TabHistoryFactory extends HistoryFactory{
	private Integer[] table;
	
	
	public TabHistoryFactory(Integer[] table, String poolPoint) {
		super(poolPoint);
		this.table = table.clone();
	}
	
	public TabHistoryFactory(Element element) {
		super(element);
	}


	@Override
	public HistoryFactory clone() {
		return  new TabHistoryFactory(table, pointPool);
	}


	@Override
	public int getCost(Value oldValue, Value newValue, int action) {
		int oldCost;
		int newCost;
		if(oldValue==null){
			oldCost = 0;
		}else{
			oldCost = table[((IntValue)oldValue).getValue()];
		}
		if(newValue==null){
			newCost = 0;
		}else{
			newCost = table[((IntValue)newValue).getValue()];
		}
		return newCost-oldCost;
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String,String> map = new HashMap<String, String>();
		for(int i=0; i<table.length; i++){
			map.put(""+i, ""+table[i]);
		}
		return map;
	}

	@Override
	public void setArgs(Map<String, String> map) {
		table = new Integer[map.size()];
		Arrays.fill(table, 0);
		for(String key : map.keySet()){
			table[Integer.parseInt(key)] = Integer.parseInt(map.get(key));
		}
	}
	
	
}
