package com.mrprez.gencross;

import com.mrprez.gencross.history.HistoryFactory;

public interface PropertyOwner extends Iterable<Property> {

	public HistoryFactory getHistoryFactory();

	public Property getProperty(String currentName);
	
}
