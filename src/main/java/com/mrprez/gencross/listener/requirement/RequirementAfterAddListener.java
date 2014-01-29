package com.mrprez.gencross.listener.requirement;

import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.AfterAddPropertyListener;

public class RequirementAfterAddListener extends AfterAddPropertyListener {
	private String requiredPropertyName;
	private String requiredValue;
	private Double requiredMin;
	private Double requiredMax;
	
	
	@Override
	public void callAfterAddProperty(Property property) throws Exception {
		
		BeforeDeleteRequiredListener beforeDeleteRequiredListener = new BeforeDeleteRequiredListener();
		beforeDeleteRequiredListener.setDependantPropertyName(property.getAbsoluteName());
		beforeDeleteRequiredListener.setPattern(requiredPropertyName);
		getPersonnage().addBeforeDeletePropertyListener(beforeDeleteRequiredListener);
		
		if(requiredValue!=null || requiredMax!=null || requiredMin!=null){
			BeforeChangeRequiredListener beforeChangeRequiredListener = new BeforeChangeRequiredListener();
			beforeChangeRequiredListener.setDependantPropertyName(property.getAbsoluteName());
			beforeChangeRequiredListener.setRequiredMax(requiredMax);
			beforeChangeRequiredListener.setRequiredMin(requiredMin);
			beforeChangeRequiredListener.setRequiredValue(requiredValue);
			beforeChangeRequiredListener.setPattern(requiredPropertyName);
			getPersonnage().addBeforeChangeValueListener(beforeChangeRequiredListener);
		}
		
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
	
	public String getRequiredPropertyName(){
		return requiredPropertyName;
	}

}
