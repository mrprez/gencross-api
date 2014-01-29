package com.mrprez.gencross.listener;

import com.mrprez.gencross.Property;

public abstract class BeforeDeletePropertyListener extends PropertyListener {

	public abstract boolean callBeforeDeleteProperty(Property property) throws Exception;
}
