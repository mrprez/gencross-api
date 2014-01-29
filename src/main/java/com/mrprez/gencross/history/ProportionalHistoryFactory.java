package com.mrprez.gencross.history;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import com.mrprez.gencross.value.Value;

public class ProportionalHistoryFactory extends HistoryFactory {
	private double factor;
	private double startValue;
	
	
	public ProportionalHistoryFactory(String pointPool) {
		super(pointPool);
		this.factor = 1.0;
		this.startValue = 0.0;
	}
	
	public ProportionalHistoryFactory(String pointPool, double factor) {
		super(pointPool);
		this.factor = factor;
		this.startValue = 0.0;
	}
	
	public ProportionalHistoryFactory(String pointPool, double factor, double startValue) {
		super(pointPool);
		this.factor = factor;
		this.startValue = startValue;
	}

	public ProportionalHistoryFactory(Element element) {
		super(element);
	}
	
	@Override
	public HistoryFactory clone() {
		return new ProportionalHistoryFactory(pointPool, factor, startValue);
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("factor", Double.toString(factor));
		map.put("startValue", Double.toString(startValue));
		return map;
	}

	@Override
	public int getCost(Value oldValue, Value newValue, int action) {
		double oldCost;
		double newCost;
		
		if(oldValue!=null){
			oldCost = oldValue.getDouble() / ((Number)oldValue.getOffset()).doubleValue();
		}else{
			oldCost = startValue / ((Number)newValue.getOffset()).doubleValue();
		}
		if(newValue!=null){
			newCost = newValue.getDouble() / ((Number)newValue.getOffset()).doubleValue();
		}else{
			newCost = startValue / ((Number)oldValue.getOffset()).doubleValue();
		}
		return (int) ((newCost-oldCost)*factor);
	}

	@Override
	public void setArgs(Map<String, String> map) {
		if(map.containsKey("factor")){
			factor = Double.parseDouble(map.get("factor"));
		}else{
			factor = 1.0;
		}
		if(map.containsKey("startValue")){
			startValue = Double.parseDouble(map.get("startValue"));
		}else{
			startValue = 0.0;
		}
	}

}
