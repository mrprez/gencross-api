package com.mrprez.gencross.listener.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.AfterChangeValueListener;
import com.mrprez.gencross.value.DoubleValue;
import com.mrprez.gencross.value.IntValue;
import com.mrprez.gencross.value.Value;

/**
 * Ce listener est utilisé dans le cas où une propriété sert à déterminer la base d'une ou plusieurs autres.
 * Chaque fois que cette propriété est modifié, la nouvelle base est calculée. cette base devient le minimum de proriétés dépendantes et la valeur de ces propriétés est recalculé.
 * Ex: Admettons un système où les compétences dépendent chacune d'un attribut. La valeur de base de chaque compétence est égale à la moitiée de l'attribut correspondant.
 * Ce listener recalculera le minimum et la valeur de chaque compétence lorsqu'on modifie un attribut.
 * 
 * @author gagneretg
 *
 */
public class BaseValueListener extends AfterChangeValueListener {
	private Set<String> properties = new HashSet<String>();
	private double factor = 1.0;
	
	@Override
	public Map<String, String> getArgs() {
		Map<String, String> args = new HashMap<String, String>();
		int i = 1;
		for(String propertyName : properties){
			args.put(""+i, propertyName);
			i++;
		}
		args.put("factor", ""+factor);
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		int i = 1;
		while(args.containsKey(""+i)){
			properties.add(args.get(""+i));
			i++;
		}
		if(args.containsKey("factor")){
			factor = Double.parseDouble(args.get("factor"));
		}
	}

	@Override
	public void callAfterChangeValue(Property property, Value oldValue) throws Exception {
		Personnage personnage = property.getPersonnage();
		double oldBase = oldValue.getDouble()*factor;
		double newBase = property.getValue().getDouble()*factor;
		for(String propertyName : properties){
			Property deriveProperty = personnage.getProperty(propertyName);
			if(deriveProperty!=null){
				double newValue = deriveProperty.getValue().getDouble()-oldBase+newBase;
				if(deriveProperty.getValue() instanceof IntValue){
					((IntValue)deriveProperty.getValue()).setValue((int)newValue);
					deriveProperty.setMin(new IntValue((int)newValue));
				}else if(deriveProperty.getValue() instanceof DoubleValue){
					((DoubleValue)deriveProperty.getValue()).setValue(newValue);
					deriveProperty.setMin(new DoubleValue(newValue));
				}
			}
		}
	}

}
