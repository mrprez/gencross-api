package com.mrprez.gencross.formula;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;

public class Variable extends FormulaSeme{
	private String propertyAbsoluteName;
	
	
	public Variable(String propertyAbsoluteName) {
		super();
		this.propertyAbsoluteName = propertyAbsoluteName;
	}
	
	public double getValue(Personnage personnage){
		Property property = personnage.getProperty(propertyAbsoluteName);
		if(property==null){
			return 0.0;
		}
		if(property.getValue()==null){
			return 1.0;
		}
		return property.getValue().getDouble();
	}

	@Override
	public Variable clone() {
		return new Variable(propertyAbsoluteName);
	}

	public String getPropertyAbsoluteName() {
		return propertyAbsoluteName;
	}
	public void setPropertyAbsoluteName(String propertyAbsoluteName) {
		this.propertyAbsoluteName = propertyAbsoluteName;
	}
	
	@Override
	public String toString(){
		return " #"+propertyAbsoluteName+"# ";
	}

	@Override
	public boolean equals(Object object) {
		if(object instanceof Variable){
			return propertyAbsoluteName.equals(((Variable)object).propertyAbsoluteName);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return propertyAbsoluteName!=null ? propertyAbsoluteName.hashCode() : 0;
	}

}
