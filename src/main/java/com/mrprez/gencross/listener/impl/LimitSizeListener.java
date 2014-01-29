package com.mrprez.gencross.listener.impl;

import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.BeforeAddPropertyListener;

/**
 * Ce listener limite la taille  d'une PropertiesList.
 * Il bloque l'ajout d'un nouvel élément si la taille max est atteinte.
 * 
 * @author MrPrez
 *
 */
public class LimitSizeListener extends BeforeAddPropertyListener {
	private int size = 1;
	
	@Override
	public boolean callBeforeAddProperty(Property property) throws Exception {
		Property mother = (Property) property.getOwner();
		if(mother.getSubProperties().size()>=size){
			super.getPersonnage().setActionMessage("Taille de la liste limitée à "+size);
			return false;
		}
		return true;
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("size", ""+size);
		return map;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		if(args.containsKey("size")){
			size = Integer.parseInt(args.get("size"));
		}
	}

}
