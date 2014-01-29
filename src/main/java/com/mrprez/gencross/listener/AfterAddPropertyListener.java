package com.mrprez.gencross.listener;

import com.mrprez.gencross.Property;

public abstract class AfterAddPropertyListener extends PropertyListener{
	
	public abstract void callAfterAddProperty(Property property) throws Exception;
	
	
}
