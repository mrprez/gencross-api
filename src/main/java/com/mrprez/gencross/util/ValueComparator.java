package com.mrprez.gencross.util;

import java.util.Comparator;

import com.mrprez.gencross.value.DoubleValue;
import com.mrprez.gencross.value.IntValue;
import com.mrprez.gencross.value.Value;

public class ValueComparator implements Comparator<Value> {

	public int compare(Value arg0, Value arg1) {
		if(arg0 instanceof IntValue && arg1 instanceof IntValue){
			return ((IntValue)arg1).getValue()-((IntValue)arg0).getValue();
		}
		if(arg0 instanceof DoubleValue && arg1 instanceof DoubleValue){
			return (int) (((DoubleValue)arg1).getValue()-((DoubleValue)arg0).getValue());
		}
		return arg0.toString().compareTo(arg1.toString());
	}

}
