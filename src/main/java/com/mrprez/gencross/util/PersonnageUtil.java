package com.mrprez.gencross.util;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PropertiesList;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.value.Value;

public class PersonnageUtil {
	
	static public void setMinRecursivly(Property property){
		property.setMin();
		if(property.getSubProperties()!=null){
			for(Property subProperty : property.getSubProperties()){
				setMinRecursivly(subProperty);
			}
		}
	}
	static public void setMinRecursivly(Personnage personnage, String absoluteName){
		Property property = personnage.getProperty(absoluteName);
		if(property!=null){
			setMinRecursivly(property);
		}
	}
	
	static public void setMaxRecursivly(Property property, Value max){
		if(property.getValue()!=null){
			property.setMax(max);
		}
		if(property.getSubProperties()!=null){
			for(Property subProperty : property.getSubProperties()){
				setMaxRecursivly(subProperty, max);
			}
			if(property.getSubProperties().getDefaultProperty()!=null){
				setMaxRecursivly(property.getSubProperties().getDefaultProperty(), max);
			}
			for(Property option : property.getSubProperties().getOptions().values()){
				setMaxRecursivly(option, max);
			}
		}
	}
	static public void setMaxRecursivly(Personnage personnage, String absoluteName, Value max){
		Property property = personnage.getProperty(absoluteName);
		if(property!=null){
			setMaxRecursivly(property, max);
		}
	}
	
	static public void removeMaxRecursivly(Property property){
		property.setMax(null);
		if(property.getSubProperties()!=null){
			for(Property subProperty : property.getSubProperties()){
				removeMaxRecursivly(subProperty);
			}
			if(property.getSubProperties().getDefaultProperty()!=null){
				removeMaxRecursivly(property.getSubProperties().getDefaultProperty());
			}
			for(Property optionProperty : property.getSubProperties().getOptions().values()){
				removeMaxRecursivly(optionProperty);
			}
		}
	}
	static public void removeMaxRecursivly(Personnage personnage, String absoluteName){
		Property property = personnage.getProperty(absoluteName);
		if(property!=null){
			removeMaxRecursivly(property);
		}
	}
	static public void changePoolPoint(PropertiesList propertiesList, String poolPoint){
		for(Property property : propertiesList){
			property.getHistoryFactory().setPointPool(poolPoint);
		}
		if(propertiesList.getDefaultProperty()!=null){
			propertiesList.getDefaultProperty().getHistoryFactory().setPointPool(poolPoint);
		}
		if(propertiesList.getOptions()!=null){
			for(Property option : propertiesList.getOptions().values()){
				option.getHistoryFactory().setPointPool(poolPoint);
			}
		}
	}
	static public void changePoolPointRecursivly(PropertiesList propertiesList, String poolPoint){
		changePoolPoint(propertiesList, poolPoint);
		for(Property property : propertiesList){
			if(property.getSubProperties()!=null){
				changePoolPointRecursivly(property.getSubProperties(), poolPoint);
			}
		}
		
	}
}
