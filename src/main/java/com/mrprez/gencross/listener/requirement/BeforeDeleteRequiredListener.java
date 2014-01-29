package com.mrprez.gencross.listener.requirement;

import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.BeforeDeletePropertyListener;

public class BeforeDeleteRequiredListener extends BeforeDeletePropertyListener {
	private String dependantPropertyName;
	
	
	@Override
	public boolean callBeforeDeleteProperty(Property property) throws Exception {
		getPersonnage().setActionMessage("La propriété "+dependantPropertyName+" nécessite de garder cette propriété");
		return false;
	}

	@Override
	public Map<String, String> getArgs() {
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("dependantPropertyName", dependantPropertyName);
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		dependantPropertyName = args.get("dependantPropertyName");
	}

	public String getDependantPropertyName() {
		return dependantPropertyName;
	}

	public void setDependantPropertyName(String dependantPropertyName) {
		this.dependantPropertyName = dependantPropertyName;
	}
	
	

}
