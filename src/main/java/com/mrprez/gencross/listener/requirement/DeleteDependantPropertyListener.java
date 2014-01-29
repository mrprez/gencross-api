package com.mrprez.gencross.listener.requirement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.AfterDeletePropertyListener;
import com.mrprez.gencross.listener.BeforeChangeValueListener;
import com.mrprez.gencross.listener.BeforeDeletePropertyListener;

public class DeleteDependantPropertyListener extends AfterDeletePropertyListener {
	private String requiredPropertyName;
	
	
	@Override
	public void callAfterDeleteProperty(Property property) throws Exception {
		Iterator<BeforeChangeValueListener> changeListenerIt = getPersonnage().getBeforeChangeValueListeners().iterator();
		while(changeListenerIt.hasNext()){
			BeforeChangeValueListener listener = changeListenerIt.next();
			if(listener instanceof BeforeChangeRequiredListener){
				BeforeChangeRequiredListener changeRequiredListener = (BeforeChangeRequiredListener) listener;
				if(changeRequiredListener.getPattern().equals(requiredPropertyName)){
					changeListenerIt.remove();
				}
			}
		}
		
		Iterator<BeforeDeletePropertyListener> deleteListenerIt = getPersonnage().getBeforeDeletePropertyListeners().iterator();
		while(deleteListenerIt.hasNext()){
			BeforeDeletePropertyListener listener = deleteListenerIt.next();
			if(listener instanceof BeforeDeletePropertyListener){
				BeforeDeletePropertyListener deleteRequiredListener = (BeforeDeletePropertyListener) listener;
				if(deleteRequiredListener.getPattern().equals(requiredPropertyName)){
					deleteListenerIt.remove();
				}
			}
		}
		
		
	}

	@Override
	public Map<String, String> getArgs() {
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("requiredPropertyName", requiredPropertyName);
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		requiredPropertyName = args.get("requiredPropertyName");
	}

	public String getRequiredPropertyName() {
		return requiredPropertyName;
	}

	public void setRequiredPropertyName(String requiredPropertyName) {
		this.requiredPropertyName = requiredPropertyName;
	}
	
}
