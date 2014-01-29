package com.mrprez.gencross.listener;

import java.util.Map;

import com.mrprez.gencross.Personnage;

public abstract class Listener {
	private Personnage personnage;
	private String name;
	

	public void setPersonnage(Personnage personnage) {
		this.personnage = personnage;
	}

	public Personnage getPersonnage() {
		return personnage;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract Map<String, String> getArgs();
	
	public abstract void setArgs(Map<String, String> args) throws Exception;

	public abstract Listener clone() throws CloneNotSupportedException;
	


}
