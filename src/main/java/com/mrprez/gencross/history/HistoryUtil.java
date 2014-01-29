package com.mrprez.gencross.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mrprez.gencross.Property;


public class HistoryUtil {
	
	public static int sumHistory(List<HistoryItem> history, String regex, String pointPool){
		int sum = 0;
		for(HistoryItem item : history){
			if(item.getAbsoluteName().matches(regex) && item.getPointPool().equals(pointPool)){
				sum = sum + item.getCost();
			}
		}
		return sum;
	}
	
	public static Map<String,Integer> sumHistoryOfSubTree(List<HistoryItem> history, Property property){
		Map<String,Integer> result = new HashMap<String, Integer>();
		for(HistoryItem historyItem : history){
			if(historyItem.getAbsoluteName().startsWith(property.getAbsoluteName())){
				if(result.containsKey(historyItem.getPointPool())){
					result.put(historyItem.getPointPool(), result.get(historyItem.getPointPool())+historyItem.getCost());
				}else{
					result.put(historyItem.getPointPool(), historyItem.getCost());
				}
			}
		}
		return result;
	}
	
	public static int sumHistoryOfSubTree(List<HistoryItem> history, Property property, String pointPool){
		int result = 0;
		for(HistoryItem historyItem : history){
			if(historyItem.getAbsoluteName().startsWith(property.getAbsoluteName())){
				if(pointPool.equals(historyItem.getPointPool())){
					result = result + historyItem.getCost();
				}
			}
		}
		return result;
	}
	
	public static List<HistoryItem> getSubHistory(List<HistoryItem> history, Property property){
		List<HistoryItem> result = new ArrayList<HistoryItem>();
		for(HistoryItem historyItem : history){
			if(historyItem.getAbsoluteName().startsWith(property.getAbsoluteName())){
				result.add(historyItem);
			}
		}
		return result;
	}
	
	public static int sumHistoryOfSpecifiableProperty(List<HistoryItem> history, String motherPropertyAbsoluteName, String propertySimpleName, String pointPool){
		String comparName = motherPropertyAbsoluteName+"#"+propertySimpleName;
		int result = 0;
		for(HistoryItem historyItem : history){
			if(historyItem.getAbsoluteName().startsWith(comparName)){
				if(historyItem.getPointPool().equals(pointPool)){
					result = result + historyItem.getCost();
				}
			}
		}
		return result;
	}
	

}
