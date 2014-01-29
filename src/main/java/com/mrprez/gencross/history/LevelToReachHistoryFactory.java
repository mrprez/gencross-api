package com.mrprez.gencross.history;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import com.mrprez.gencross.value.Value;

public class LevelToReachHistoryFactory extends HistoryFactory {
	private double factor;
	private double startValue;
	
	
	public LevelToReachHistoryFactory(Element element) {
		super(element);
	}
	
	public LevelToReachHistoryFactory(String poolPoint) {
		super(poolPoint);
		this.factor = 1.0;
		this.startValue = 0.0;
	}
	
	public LevelToReachHistoryFactory(double factor, String poolPoint) {
		super(poolPoint);
		this.factor = factor;
		this.startValue = 0.0;
	}
	
	public LevelToReachHistoryFactory(double factor, double startValue, String poolPoint) {
		super(poolPoint);
		this.factor = factor;
		this.startValue = startValue;
	}

	@Override
	public HistoryFactory clone() {
		return new LevelToReachHistoryFactory(factor, startValue, pointPool);
	}

	@Override
	public Map<String, String> getArgs() {
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("factor", Double.toString(factor));
		args.put("startValue", Double.toString(startValue));
		return args;
	}

	@Override
	public int getCost(Value oldValue, Value newValue, int action) {
		double newDouble = newValue==null ? startValue : newValue.getDouble();
		double oldDouble = oldValue==null ? startValue : oldValue.getDouble();
		double offset;
		if(newValue!=null){
			offset = ((Number)newValue.getOffset()).doubleValue();
		} else {
			offset = ((Number)oldValue.getOffset()).doubleValue();
		}
		
		double cost = (newDouble - oldDouble) * (newDouble + oldDouble + offset) / (2 * offset) * factor;
		return (int) cost;
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
