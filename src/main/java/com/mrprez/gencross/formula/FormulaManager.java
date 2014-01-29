package com.mrprez.gencross.formula;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.mrprez.gencross.Personnage;

public class FormulaManager implements Iterable<Formula>{
	private Map<String, Formula> formulaMap = new HashMap<String, Formula>();
	private Map<String, Set<Formula>> formulaDependencies = new HashMap<String, Set<Formula>>();
	
	
	public FormulaManager clone(){
		FormulaManager clone = new FormulaManager();
		for(Formula formula : formulaMap.values()){
			clone.addFormula(formula.clone());
		}
		return clone;
	}
	
	public void addFormula(Formula formula){
		removeFormula(formula.getDestinationPropertyName());
		formulaMap.put(formula.getDestinationPropertyName(), formula);
		
		for(FormulaSeme seme = formula.getFirstSeme(); seme!=null; seme = seme.getNext()){
			if(seme instanceof Variable){
				String propertyName = ((Variable)seme).getPropertyAbsoluteName();
				if(formulaDependencies.containsKey(propertyName)){
					formulaDependencies.get(propertyName).add(formula);
				}else{
					formulaDependencies.put(propertyName, new HashSet<Formula>());
					formulaDependencies.get(propertyName).add(formula);
				}
			}
		}
	}
	
	public void removeFormula(String destinationPropertyName){
		for(Entry<String, Set<Formula>> dependencyEntry : formulaDependencies.entrySet()){
			Iterator<Formula> formulaIt = dependencyEntry.getValue().iterator();
			while(formulaIt.hasNext()){
				if(formulaIt.next().getDestinationPropertyName().equals(destinationPropertyName)){
					formulaIt.remove();
				}
			}
		}
		formulaMap.remove(destinationPropertyName);
	}
	
	public void impactModificationFor(String propertyName, Personnage personnage) throws MalformedFormulaException{
		Set<Formula> set = formulaDependencies.get(propertyName);
		if(set!=null){
			for(Formula formula : set){
				formula.process(personnage);
				impactModificationFor(formula.getDestinationPropertyName(), personnage);
			}
		}
		
	}
	
	public void setXml(Element element) throws MalformedFormulaException{
		Iterator<?> it = element.elementIterator("formula");
		while(it.hasNext()){
			addFormula(new Formula(((Element)it.next()).getText()));
		}
	}
	
	public Element getXml(){
		DefaultElement element = new DefaultElement("formulas");
		for(Formula formula : formulaMap.values()){
			element.addElement("formula").setText(formula.toString());
		}
		return element;
	}
	
	public boolean isImpacted(String propertyName){
		return formulaMap.containsKey(propertyName);
	}
	
	public int size(){
		return formulaMap.size();
	}

	@Override
	public Iterator<Formula> iterator() {
		return formulaMap.values().iterator();
	}
	
	
	

}
