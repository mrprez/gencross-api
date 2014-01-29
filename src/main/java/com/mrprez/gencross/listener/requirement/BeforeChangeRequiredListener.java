package com.mrprez.gencross.listener.requirement;

import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.BeforeChangeValueListener;
import com.mrprez.gencross.value.Value;

public class BeforeChangeRequiredListener extends BeforeChangeValueListener {
	private String dependantPropertyName;
	private String requiredValue;
	private Double requiredMin;
	private Double requiredMax;
	

	@Override
	public boolean callBeforeChangeValue(Property property, Value newValue) throws Exception {
		if(requiredValue!=null){
			if(!newValue.getString().equals(requiredValue)){
				getPersonnage().setActionMessage("La propriété "+dependantPropertyName+" nécessite de garder cette valeur");
				return false;
			}
		}
		if(requiredMax!=null){
			if(newValue.getDouble()>requiredMax.doubleValue()){
				getPersonnage().setActionMessage("La propriété "+dependantPropertyName+" nécessite de garder cette propriété inférieure (ou égale) à "+requiredMax);
				return false;
			}
		}
		if(requiredMin!=null){
			if(newValue.getDouble()<requiredMin.doubleValue()){
				getPersonnage().setActionMessage("La propriété "+dependantPropertyName+" nécessite de garder cette propriété supérieure (ou égale) à "+requiredMin);
				return false;
			}
		}
		return true;
	}

	@Override
	public Map<String, String> getArgs() {
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("dependantPropertyName", dependantPropertyName);
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
		dependantPropertyName = args.get("dependantPropertyName");
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

	public String getDependantPropertyName() {
		return dependantPropertyName;
	}

	public void setDependantPropertyName(String dependantPropertyName) {
		this.dependantPropertyName = dependantPropertyName;
	}

	public String getRequiredValue() {
		return requiredValue;
	}

	public void setRequiredValue(String requiredValue) {
		this.requiredValue = requiredValue;
	}

	public Double getRequiredMin() {
		return requiredMin;
	}

	public void setRequiredMin(Double requiredMin) {
		this.requiredMin = requiredMin;
	}

	public Double getRequiredMax() {
		return requiredMax;
	}

	public void setRequiredMax(Double requiredMax) {
		this.requiredMax = requiredMax;
	}
	
	

}
