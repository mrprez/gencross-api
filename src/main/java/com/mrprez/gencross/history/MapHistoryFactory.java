package com.mrprez.gencross.history;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import com.mrprez.gencross.value.DoubleValue;
import com.mrprez.gencross.value.IntValue;
import com.mrprez.gencross.value.StringValue;
import com.mrprez.gencross.value.Value;

public class MapHistoryFactory extends HistoryFactory {
	private Map<Value,Integer> map;
	
	
	public MapHistoryFactory(Map<Value, Integer> map, String poolPoint) {
		super(poolPoint);
		this.map = map;
	}
	
	public MapHistoryFactory(Element element) {
		super(element);
	}
	
	@Override
	public HistoryFactory clone() {
		return new MapHistoryFactory(new HashMap<Value, Integer>(map), pointPool);
	}

	@Override
	public int getCost(Value oldValue, Value newValue, int action) {
		if(oldValue==null){
			return map.get(newValue);
		}
		if(newValue==null){
			return -map.get(oldValue);
		}
		return map.get(newValue)-map.get(oldValue);
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String,String> result = new HashMap<String, String>();
		for(Value value : map.keySet()){
			result.put(value.toString(), map.get(value).toString());
		}
		result.put("class",map.keySet().iterator().next().getClass().getName());
		return result;
	}

	@Override
	public void setArgs(Map<String, String> argsMap) {
		map = new HashMap<Value, Integer>();
		if(argsMap.get("class").equals(IntValue.class.getName())){
			for(String key : argsMap.keySet()){
				if(!key.equals("class")){
					map.put(new IntValue(new Integer(key)), Integer.parseInt(argsMap.get(key)));
				}
			}
		}else if(argsMap.get("class").equals(DoubleValue.class.getName())){
			for(String key : argsMap.keySet()){
				if(!key.equals("class")){
					map.put(new DoubleValue(new Double(key)), Integer.parseInt(argsMap.get(key)));
				}
			}
		}else{
			for(String key : argsMap.keySet()){
				if(!key.equals("class")){
					map.put(new StringValue(key), Integer.parseInt(argsMap.get(key)));
				}
			}
		}
	}



	

}
