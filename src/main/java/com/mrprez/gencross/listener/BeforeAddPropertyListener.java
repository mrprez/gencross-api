package com.mrprez.gencross.listener;

import com.mrprez.gencross.Property;

public abstract class BeforeAddPropertyListener extends PropertyListener {
	
	public abstract boolean callBeforeAddProperty(Property property) throws Exception;
}
