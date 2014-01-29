package com.mrprez.gencross.listener.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.AfterChangeValueListener;
import com.mrprez.gencross.value.DoubleValue;
import com.mrprez.gencross.value.IntValue;
import com.mrprez.gencross.value.Value;

/**
 * Ce listener permet de calculer une valeur d'une propriété à partir d'une autre.
 * Les valeurs doivent être des DoubleValue ou IntValue.
 * Pour son calcul, ce listener se base sur un tableau associatif. Il va chercher la clé correspondant à la propriété modifiée et modifier la Value de la propriété à modifier.
 * S'il ne trouve pas la valeur de la propriété modifiée et qu'elle est plus petite que toutes les clés qu'il a dans son tableau, il prend la plus petite clé.
 * S'il ne trouve pas la valeur de la propriété modifiée et qu'elle est plus grande que toutes les clés qu'il a dans son tableau, il prend la plus grande clé.
 * 
 * @author MrPrez
 *
 */

public class TabFormulaListener extends AfterChangeValueListener {
	private String dependantPropertyName;
	private SortedMap<Double, Double> tab = new TreeMap<Double, Double>();
	
	@Override
	public void callAfterChangeValue(Property property, Value oldValue) throws Exception {
		Property dependantProperty = property.getPersonnage().getProperty(dependantPropertyName);
		Double newDoubleValue = tab.get(property.getValue().getDouble());
		if(newDoubleValue==null){
			Double minKey = tab.firstKey();
			Double maxKey = tab.lastKey();
			if(property.getValue().getDouble()<minKey){
				newDoubleValue = tab.get(minKey);
			}
			if(property.getValue().getDouble()>maxKey){
				newDoubleValue = tab.get(maxKey);
			}
		}
		Value dependantValue = dependantProperty.getValue();
		if(dependantValue instanceof DoubleValue){
			((DoubleValue)dependantValue).setValue(newDoubleValue);
		}else if(dependantValue instanceof IntValue){
			((IntValue)dependantValue).setValue(newDoubleValue.intValue());
		}

	}

	@Override
	public Map<String, String> getArgs() {
		Map<String, String> args = new HashMap<String, String>();
		args.put("dependantPropertyName", dependantPropertyName);
		int i=0;
		for(Double key : tab.keySet()){
			args.put("key"+i, key.toString());
			args.put("value"+i, tab.get(key).toString());
			i++;
		}
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		dependantPropertyName = args.get("dependantPropertyName");
		int i=0;
		while(args.containsKey("key"+i)){
			tab.put(Double.parseDouble(args.get("key"+i)), Double.parseDouble(args.get("value"+i)));
			i++;
		}
	}

}
