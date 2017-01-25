package com.mrprez.gencross;

import java.util.Iterator;

import com.mrprez.gencross.history.HistoryFactory;

public interface PropertyOwner {

	public abstract Iterator<Property> iterator();
	public abstract HistoryFactory getHistoryFactory();

	public abstract Property getProperty(String currentName);
}
