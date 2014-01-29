package com.mrprez.gencross.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PoolPoint;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.formula.Formula;
import com.mrprez.gencross.history.HistoryFactory;
import com.mrprez.gencross.listener.Listener;
import com.mrprez.gencross.listener.PassToNextPhaseListener;
import com.mrprez.gencross.listener.PropertyListener;
import com.mrprez.gencross.value.Value;

public class PersonnageSimilarity {
	private Personnage personnage1;
	private Personnage personnage2;
	
	
	public PersonnageSimilarity(Personnage personnage1, Personnage personnage2) {
		super();
		this.personnage1 = personnage1;
		this.personnage2 = personnage2;
	}

	public double getSimilarity(){
		double similarity = 0.2*getListenerAndFormulaSimilarity()
				+ 0.1*getPhasesSimilarity()
				+ 0.1*getPointPoolsSimilarity()
				+ 0.6*getPropertiesSimilarity();
		return similarity;
	}
	
	private double getListenerAndFormulaSimilarity(){
		SortedSet<Listener> listeners1 = new TreeSet<Listener>(listenerComparator);
		listeners1.addAll(personnage1.getAfterAddPropertyListeners());
		listeners1.addAll(personnage1.getAfterChangeValueListeners());
		listeners1.addAll(personnage1.getAfterDeletePropertyListeners());
		listeners1.addAll(personnage1.getBeforeAddPropertyListeners());
		listeners1.addAll(personnage1.getBeforeChangeValueListeners());
		listeners1.addAll(personnage1.getBeforeDeletePropertyListeners());
		listeners1.addAll(personnage1.getPassToNextPhaseListeners());
		SortedSet<Listener> listeners2 = new TreeSet<Listener>(listenerComparator);
		listeners2.addAll(personnage2.getAfterAddPropertyListeners());
		listeners2.addAll(personnage2.getAfterChangeValueListeners());
		listeners2.addAll(personnage2.getAfterDeletePropertyListeners());
		listeners2.addAll(personnage2.getBeforeAddPropertyListeners());
		listeners2.addAll(personnage2.getBeforeChangeValueListeners());
		listeners2.addAll(personnage2.getBeforeDeletePropertyListeners());
		listeners2.addAll(personnage2.getPassToNextPhaseListeners());
		double count = 0;
		for(Listener listener1 : listeners1){
			if(listeners2.contains(listener1)){
				count++;
			}
		}
		
		for(Formula formula1 : personnage1.getFormulaManager()){
			for(Formula formula2 : personnage2.getFormulaManager()){
				if(formula1.equals(formula2)){
					count++;
					break;
				}
			}
		}
		
		return count / (double)(Math.max(listeners1.size(), listeners2.size())+Math.max(personnage1.getFormulaManager().size(), personnage2.getFormulaManager().size()));
	}
	
	private double getPhasesSimilarity(){
		int tab[][] = new int[personnage1.getPhaseList().size()][personnage2.getPhaseList().size()];
		for(int i=0; i<personnage1.getPhaseList().size(); i++){
			for(int j=0; j<personnage2.getPhaseList().size(); j++){
				int c = personnage1.getPhaseList().get(i).equals(personnage2.getPhaseList().get(j))?0:1;
				int base;
				if(i==0 && j==0){
					base = 0;
				}else if(i==0){
					base = tab[i][j-1];
				}else if(j==0){
					base = tab[i-1][j];
				}else{
					base = Math.min(Math.min(tab[i][j-1], tab[i-1][j]),tab[i-1][j-1]);
				}
				tab[i][j] = base + c;
			}
		}
		int difference = tab[personnage1.getPhaseList().size()-1][personnage2.getPhaseList().size()-1];
		if(!personnage1.getPhase().equals(personnage2.getPhase())){
			difference++;
		}
		double similarity = 1.0 - ((double)difference) / ((double)Math.max(personnage1.getPhaseList().size(), personnage2.getPhaseList().size())+1);
		Logger.getLogger("PersonnageSimilarity").debug("Phases Similarity="+similarity);
		return similarity;
	}
	
	private double getPointPoolsSimilarity(){
		int count = 0;
		for(PoolPoint poolPoint1 : personnage1.getPointPools().values()){
			PoolPoint poolPoint2 = personnage2.getPointPools().get(poolPoint1.getName());
			if(poolPoint2!=null){
				if(poolPoint1.getRemaining()==poolPoint2.getRemaining() && poolPoint1.getTotal()==poolPoint2.getTotal() && poolPoint1.isToEmpty()==poolPoint2.isToEmpty()){
					count++;
				}
			}
		}
		double similarity = (double)count / (double)Math.max(personnage1.getPointPools().size(), personnage2.getPointPools().size());
		Logger.getLogger("PersonnageSimilarity").debug("Point Pools Similarity="+similarity);
		return similarity;
	}
	
	private double getPropertiesSimilarity(){
		double similarity = 0.0;
		for(Property property1 : personnage1.getProperties()){
			if(personnage2.getPropertyNames().contains(property1.getFullName())){
				similarity = similarity + similarity(property1, personnage2.getProperty(property1.getFullName()));
			}
		}
		double similarityResult = similarity / (double) (Math.max(personnage1.size(), personnage2.size()));
		Logger.getLogger("PersonnageSimilarity").debug("Properties Similarity="+similarityResult);
		return similarityResult;
	}
	
	private double similarity(Property property1, Property property2){
		int similarity = 0;
		int criteriaNumber = 9;
		if(property1.isEditable()==property2.isEditable()){
			similarity++;
		}
		if(property1.isRemovable()==property2.isRemovable()){
			similarity++;
		}
		if(compare(property1.getActualHistoryFactory(), property2.getActualHistoryFactory())){
			similarity++;
		}
		if(compare(property1.getValue(), property2.getValue())){
			similarity++;
		}
		if(compare(property1.getMax(), property2.getMax())){
			similarity++;
		}
		if(compare(property1.getMin(), property2.getMin())){
			similarity++;
		}
		if(property1.getComment()!=null && property1.getComment().equals(property2.getComment())){
			similarity++;
		}
		if(property1.getComment()==null && property2.getComment()==null){
			similarity++;
		}
		if(property1.getSpecification()!=null && property1.getSpecification().equals(property2.getSpecification())){
			similarity++;
		}
		if(property1.getSpecification()==null && property2.getSpecification()==null){
			similarity++;
		}
		if(property1.getOptions()==null && property2.getOptions()==null){
			similarity++;
		}
		if(property1.getOptions()!=null && property1.getOptions().equals(property2.getOptions())){
			similarity++;
		}
		if(property1.getSubProperties()!=null || property2.getSubProperties()!=null){
			criteriaNumber = criteriaNumber + 4;
			if(property1.getSubProperties()!=null && property2.getSubProperties()!=null){
				if(property1.getSubProperties().isFixe()==property2.getSubProperties().isFixe()){
					similarity++;
				}
				if(property1.getSubProperties().isOpen()==property2.getSubProperties().isOpen()){
					similarity++;
				}
				if(property1.getSubProperties().canRemoveElement()==property2.getSubProperties().canRemoveElement()){
					similarity++;
				}
				if(property1.getSubProperties().getDefaultProperty()==null && property2.getSubProperties().getDefaultProperty()==null){
					similarity++;
				}else{
					if(property1.getSubProperties().getDefaultProperty()!=null && property2.getSubProperties().getDefaultProperty()!=null && similarity(property1.getSubProperties().getDefaultProperty(), property2.getSubProperties().getDefaultProperty())==1.0){
						similarity++;
					}
				}
				double optionSimilarity = 0.0;
				for(Property option1 : property1.getSubProperties().getOptions().values()){
					if(property2.getSubProperties().getOptions().containsKey(option1.getFullName())){
						optionSimilarity = optionSimilarity + similarity(option1, property2.getSubProperties().getOptions().get(option1.getFullName()));
					}
				}
				double subPropertiesSimilarity = 0.0;
				for(Property subProperty1 : property1.getSubProperties()){
					if(property2.getSubProperties().get(subProperty1.getFullName())!=null){
						subPropertiesSimilarity = subPropertiesSimilarity + similarity(subProperty1, property2.getSubProperties().get(subProperty1.getFullName()));
					}
				}
				double result = 0.5*(optionSimilarity+subPropertiesSimilarity+0.1) / (0.1+Math.max(property1.getSubProperties().getOptions().size(), property2.getSubProperties().getOptions().size())+Math.max(property1.getSubProperties().size(), property2.getSubProperties().size()))
						+ 0.5*(double)similarity/(double)criteriaNumber;
				Logger.getLogger("PersonnageSimilarity").debug("Property Similarity ("+property1.getAbsoluteName()+")="+result);
				return result;
			}
		}
		Logger.getLogger("PersonnageSimilarity").debug("Property Similarity ("+property1.getAbsoluteName()+")="+((double)similarity/(double)criteriaNumber));
		return (double)similarity/(double)criteriaNumber;
	}
	
	private boolean compare(Value value1, Value value2){
		if(value1==null && value2==null){
			return true;
		}
		if(value1==null || value2==null){
			return false;
		}
		if(!value1.equals(value2)){
			return false;
		}
		if(value1.getOffset()!=null && !value1.getOffset().equals(value2.getOffset())){
			return false;
		}
		return true;
	}
	
	private boolean compare(HistoryFactory historyFactory1, HistoryFactory historyFactory2){
		if(historyFactory1==null && historyFactory2==null){
			return true;
		}
		if(historyFactory1==null || historyFactory2==null){
			return false;
		}
		if(!historyFactory1.getClass().equals(historyFactory2.getClass())){
			return false;
		}
		if(!historyFactory1.getPointPool().equals(historyFactory2.getPointPool())){
			return false;
		}
		if(!historyFactory1.getArgs().equals(historyFactory2.getArgs())){
			return false;
		}
		return true;
	}
	
	
	private Comparator<Listener> listenerComparator = new Comparator<Listener>() {
		@Override
		public int compare(Listener listener1, Listener listener2) {
			if(!listener1.getClass().getName().equals(listener2.getClass().getName())){
				return listener1.getClass().getName().compareTo(listener2.getClass().getName());
			}
			if(listener1.getName()==null && listener2.getName()!=null){
				return -1;
			}
			if(listener1.getName()!=null && listener2.getName()==null){
				return 1;
			}
			if(listener1.getName()!=null && !listener1.getName().equals(listener2.getName())){
				return listener1.getName().compareTo(listener2.getName());
			}
			if(listener1 instanceof PropertyListener){
				PropertyListener propListener1 = (PropertyListener)listener1;
				PropertyListener propListener2 = (PropertyListener)listener2;
				if(!propListener1.getPattern().equals(propListener2.getPattern())){
					return propListener1.getPattern().compareTo(propListener2.getPattern());
				}
				if(propListener1.getPhases()==null && propListener2.getPhases()!=null){
					return -1;
				}
				if(propListener1.getPhases()!=null && propListener2.getPhases()==null){
					return 1;
				}
				if(propListener1.getPhases()!=null){
					SortedSet<String> sortedPhase1 = new TreeSet<String>(propListener1.getPhases());
					SortedSet<String> sortedPhase2 = new TreeSet<String>(propListener2.getPhases());
					Iterator<String> it1 = sortedPhase1.iterator();
					Iterator<String> it2 = sortedPhase2.iterator();
					while(it1.hasNext() && it2.hasNext()){
						String phase1 = it1.next();
						String phase2 = it2.next();
						if(!phase1.equals(phase2)){
							return phase1.compareTo(phase2);
						}
					}
					if(it1.hasNext()){
						return 1;
					}
					if(it2.hasNext()){
						return -1;
					}
				}
			}else if(listener1 instanceof PassToNextPhaseListener){
				PassToNextPhaseListener phaseListener1 = (PassToNextPhaseListener) listener1;
				PassToNextPhaseListener phaseListener2 = (PassToNextPhaseListener) listener2;
				if(!phaseListener1.getPhase().equals(phaseListener2.getPhase())){
					return phaseListener1.getPhase().compareTo(phaseListener2.getPhase());
				}
			}
			
			SortedMap<String, String> args1 = new TreeMap<String, String>(listener1.getArgs());
			SortedMap<String, String> args2 = new TreeMap<String, String>(listener2.getArgs());
			Iterator<String> it1 = args1.keySet().iterator();
			Iterator<String> it2 = args2.keySet().iterator();
			while(it1.hasNext() && it2.hasNext()){
				String key1 = it1.next();
				String key2 = it2.next();
				if(!key1.equals(key2)){
					return key1.compareTo(key2);
				}
				if(!args1.get(key1).equals(args2.get(key2))){
					return args1.get(key1).compareTo(args2.get(key2));
				}
			}
			if(it1.hasNext()){
				return 1;
			}
			if(it2.hasNext()){
				return -1;
			}
			return 0;
		}
	};

}
