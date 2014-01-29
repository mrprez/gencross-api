package com.mrprez.gencross.listener.requirement;

import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.AfterAddPropertyListener;
import com.mrprez.gencross.listener.AfterDeletePropertyListener;
import com.mrprez.gencross.listener.BeforeAddPropertyListener;

public class RequirementBeforeAddListener extends BeforeAddPropertyListener {
	private String requiredPropertyName;
	private String requiredValue;
	private Double requiredMin;
	private Double requiredMax;
	

	@Override
	public boolean callBeforeAddProperty(Property property) throws Exception {
		Property requiredProperty = super.getPersonnage().getProperty(requiredPropertyName);
		if(requiredProperty==null){
			getPersonnage().setActionMessage("La propriété "+requiredPropertyName+" est requise");
			return false;
		}
		if(requiredValue!=null){
			if(requiredProperty.getValue()==null){
				getPersonnage().setActionMessage("La propriété "+requiredPropertyName+" doit être égale à "+requiredValue);
				return false;
			}
			if(!requiredValue.equals(requiredProperty.getValue().getString())){
				getPersonnage().setActionMessage("La propriété "+requiredPropertyName+" doit être égale à "+requiredValue);
				return false;
			}
		}
		if(requiredMax!=null){
			if(requiredProperty.getValue()==null){
				getPersonnage().setActionMessage("La propriété "+requiredPropertyName+" doit être inférieure (ou égale) à "+requiredMax);
				return false;
			}
			if(requiredProperty.getValue().getDouble()>requiredMax){
				getPersonnage().setActionMessage("La propriété "+requiredPropertyName+" doit être inférieure (ou égale) à "+requiredMax);
				return false;
			}
		}
		if(requiredMin!=null){
			if(requiredProperty.getValue()==null){
				getPersonnage().setActionMessage("La propriété "+requiredPropertyName+" doit être supérieure (ou égale) à "+requiredMin);
				return false;
			}
			if(requiredProperty.getValue().getDouble()<requiredMin){
				getPersonnage().setActionMessage("La propriété "+requiredPropertyName+" doit être supérieure (ou égale) à "+requiredMin);
				return false;
			}
		}
		
		if(findRequirementAfterAddListener()==null){
			RequirementAfterAddListener afterAddListener = new RequirementAfterAddListener();
			afterAddListener.setArgs(getArgs());
			afterAddListener.setPattern(property.getAbsoluteName());
			getPersonnage().addAfterAddPropertyListener(afterAddListener);
		}
		
		if(findDeleteDependantPropertyListener()==null){
			DeleteDependantPropertyListener deleteListener = new DeleteDependantPropertyListener();
			deleteListener.setRequiredPropertyName(requiredPropertyName);
			deleteListener.setPattern(property.getAbsoluteName());
			getPersonnage().addAfterDeletePropertyListener(deleteListener);
		}
		
		return true;
	}
	
	
	private RequirementAfterAddListener findRequirementAfterAddListener(){
		for(AfterAddPropertyListener listener : getPersonnage().getAfterAddPropertyListeners()){
			if(listener instanceof RequirementAfterAddListener){
				RequirementAfterAddListener requirementListener = (RequirementAfterAddListener)listener;
				if(requiredPropertyName.equals(requirementListener.getRequiredPropertyName())){
					return requirementListener;
				}
			}
		}
		return null;
	}
	
	private DeleteDependantPropertyListener findDeleteDependantPropertyListener(){
		for(AfterDeletePropertyListener listener : getPersonnage().getAfterDeletePropertyListeners()){
			if(listener instanceof DeleteDependantPropertyListener){
				DeleteDependantPropertyListener requirementListener = (DeleteDependantPropertyListener)listener;
				if(requiredPropertyName.equals(requirementListener.getRequiredPropertyName())){
					return requirementListener;
				}
			}
		}
		return null;
	}
	
	
	
	

	@Override
	public Map<String, String> getArgs() {
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("requiredPropertyName", requiredPropertyName);
		if(requiredValue!=null){
			args.put("requiredValue", requiredValue);
		}
		if(requiredMax!=null){
			args.put("requiredMax", requiredMax.toString());
		}
		if(requiredMin!=null){
			args.put("requiredMin", requiredMin.toString());
		}
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		requiredPropertyName = args.get("requiredPropertyName");
		if(args.containsKey("requiredValue")){
			requiredValue = args.get("requiredValue");
		}
		if(args.containsKey("requiredMax")){
			requiredMax = Double.parseDouble(args.get("requiredMax"));
		}
		if(args.containsKey("requiredMin")){
			requiredMin = Double.parseDouble(args.get("requiredMin"));
		}
	}

}
