package com.mrprez.gencross.listener;

import com.mrprez.gencross.Property;

public abstract class AfterDeletePropertyListener extends PropertyListener {

	public abstract void callAfterDeleteProperty(Property property) throws Exception;
}
