package com.mrprez.gencross.history;

import java.util.List;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;

public class HistoryPlayer {
	private List<HistoryItem> history;

	
	public HistoryPlayer(List<HistoryItem> history) {
		super();
		this.history = history;
	}
	
	public Personnage playHistory( Personnage personnage ) throws Exception{
		
		for(HistoryItem historyItem : history) {
			reachPhase(historyItem.getPhase(), personnage);
			if(historyItem.getAction()==HistoryItem.CREATION){
				creation(historyItem, personnage);
			} else if(historyItem.getAction()==HistoryItem.UPDATE){
				update(historyItem, personnage);
			} else if(historyItem.getAction()==HistoryItem.DELETION){
				delete(historyItem, personnage);
			}
		}
		
		reachPhase(personnage.getPhase(), personnage);
		
		return personnage;
	}
	
	private void reachPhase(String phase, Personnage personnage) throws Exception{
		while(! personnage.getPhase().equals(phase)){
			String oldPhase = personnage.getPhase();
			if(!personnage.phaseFinished()){
				throw new Exception("Cannot pass to phase: "+personnage.nextPhase()+", phase "+oldPhase+" is not finished");
			}
			personnage.passToNextPhase();
			System.out.println("Pass to phase: "+personnage.getPhase());
			if(personnage.getPhase().equals(oldPhase)){
				throw new Exception("Pass to next phase from "+oldPhase+" failed");
			}
		}
	}
	
	private void creation(HistoryItem historyItem, Personnage personnage) throws Exception{
		String motherPropertyAbsoluteName = historyItem.getAbsoluteName().substring(0, historyItem.getAbsoluteName().lastIndexOf("#"));
		Property motherProperty = personnage.getProperty(motherPropertyAbsoluteName);
		String propertyName = historyItem.getAbsoluteName().substring(historyItem.getAbsoluteName().lastIndexOf("#")+1);
		String specification = null;
		if(propertyName.contains(Property.SPECIFICATION_SEPARATOR)){
			specification = propertyName.substring(propertyName.indexOf(Property.SPECIFICATION_SEPARATOR) + 3);
			propertyName = propertyName.substring(0, propertyName.indexOf(Property.SPECIFICATION_SEPARATOR));
		}
		if(motherProperty.getSubProperties().isFixe()){
			throw new Exception("Mother property "+motherProperty.getAbsoluteName()+" is fixe");
		}
		String propertyKey = specification!=null ? propertyName+Property.SPECIFICATION_SEPARATOR : propertyName;
		if(motherProperty.getSubProperties().getOptions() != null && motherProperty.getSubProperties().getOptions().containsKey(propertyKey)){
			Property newProperty = motherProperty.getSubProperties().getOptions().get(propertyKey).clone();
			if(newProperty.getSpecification() != null){
				newProperty.setSpecification(specification);
			}
			boolean success = personnage.addPropertyToMotherProperty(newProperty);
			if(!success){
				throw new Exception("Cannot add property "+historyItem.getAbsoluteName()+" "+personnage.getActionMessage());
			}
		} else if(motherProperty.getSubProperties().isOpen()){
			Property newProperty = motherProperty.getSubProperties().getDefaultProperty().clone();
			newProperty.setName(propertyName);
			newProperty.setSpecification(specification);
			boolean success = personnage.addPropertyToMotherProperty(newProperty);
			if(!success){
				throw new Exception("Cannot add property "+historyItem.getAbsoluteName()+" "+personnage.getActionMessage());
			}
		} else {
			throw new Exception("Cannot add property "+historyItem.getAbsoluteName()+", property list is not opened");
		}
		HistoryItem newHistoryItem = personnage.getHistory().get(personnage.getHistory().size()-1);
		System.out.println("Add    property "+historyItem.getAbsoluteName()+" : "+historyItem.getNewValue()+" ("+newHistoryItem.getCost()+" "+newHistoryItem.getPointPool()+")");
	}
	
	private void update(HistoryItem historyItem, Personnage personnage) throws Exception{
		Property property = personnage.getProperty(historyItem.getAbsoluteName());
		if(!historyItem.getOldValue().equals(property.getValue())){
			throw new Exception("Property "+property.getAbsoluteName()+" has the wrong value");
		}
		boolean success = personnage.setNewValue(property, historyItem.getNewValue().clone());
		if(!success){
			throw new Exception("Cannot update property "+historyItem.getAbsoluteName()+" "+personnage.getActionMessage());
		}
		HistoryItem newHistoryItem = personnage.getHistory().get(personnage.getHistory().size()-1);
		System.out.println("Update property "+historyItem.getAbsoluteName()+" : "+historyItem.getNewValue()+" ("+newHistoryItem.getCost()+" "+newHistoryItem.getPointPool()+")");
	}
	
	private void delete(HistoryItem historyItem, Personnage personnage) throws Exception{
		Property property = personnage.getProperty(historyItem.getAbsoluteName());
		if(!historyItem.getOldValue().equals(property.getValue())){
			throw new Exception("Property "+property.getAbsoluteName()+" has the wrong value");
		}
		boolean success = personnage.removePropertyFromMotherProperty(property);
		if(!success){
			throw new Exception("Cannot remove property "+historyItem.getAbsoluteName()+" "+personnage.getActionMessage());
		}
		HistoryItem newHistoryItem = personnage.getHistory().get(personnage.getHistory().size()-1);
		System.out.println("Remove property "+historyItem.getAbsoluteName()+" : "+historyItem.getOldValue()+" ("+newHistoryItem.getCost()+" "+newHistoryItem.getPointPool()+")");
	}

}
