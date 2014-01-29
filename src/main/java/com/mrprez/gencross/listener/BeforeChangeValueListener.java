package com.mrprez.gencross.listener;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.value.Value;

public abstract class BeforeChangeValueListener extends PropertyListener {

	public abstract boolean callBeforeChangeValue(Property property, Value newValue) throws Exception;
}
