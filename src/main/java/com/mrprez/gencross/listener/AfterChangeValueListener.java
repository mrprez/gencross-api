package com.mrprez.gencross.listener;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.value.Value;

public abstract class AfterChangeValueListener extends PropertyListener {
	
	public abstract void callAfterChangeValue(Property property, Value oldValue) throws Exception;
}
