package com.mrprez.gencross.history;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import com.mrprez.gencross.value.Value;


public class FreeHistoryFactory extends HistoryFactory{

	public FreeHistoryFactory(String pointPool) {
		super(pointPool);
	}
	
	public FreeHistoryFactory(Element element){
		super(element);
	}

	@Override
	public int getCost(Value oldValue, Value newValue, int action) {
		return 0;
	}

	@Override
	public HistoryFactory clone() {
		return new FreeHistoryFactory(pointPool);
	}

	@Override
	public Map<String, String> getArgs() {
		return new HashMap<String, String>();
	}

	@Override
	public void setArgs(Map<String, String> map) {
		;
	}
	

}
