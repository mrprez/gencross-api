package com.mrprez.gencross.formula;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.formula.operator.Operator;
import com.mrprez.gencross.value.DoubleValue;
import com.mrprez.gencross.value.IntValue;

public class Formula implements Cloneable {
	private String destinationPropertyName;
	private FormulaSeme firstSeme;
	private FormulaSeme lastSeme;
	private int index;
	public final static String shortOperatorsRegexp = "[-+:*/()]";
	public final static List<String> operators = Arrays.asList("+","-","*","/","div","min","max","(",")");
	private Set<String> variableNames = new HashSet<String>();
	
	
	public Formula(){
		super();
	}
	
	public Formula(String string) throws MalformedFormulaException{
		super();
		destinationPropertyName = string.substring(0, string.indexOf("=")).trim();
		string = string.substring(string.indexOf("=")+1)+" ";
		index = 0;
		while(index<string.length()){
			if(string.substring(index,index+1).matches("\\s")){
				index++;
			}else if(string.charAt(index)=='#'){
				Variable variable = extractVariable(string);
				variableNames.add(variable.getPropertyAbsoluteName());
				addSeme(variable);
			}else if(string.substring(index,index+1).matches("[0-9]")){
				addSeme(extractConstant(string));
			}else{
				addSeme(extractOperator(string));
			}
		}
	}
	
	private Variable extractVariable(String string){
		StringBuilder variableName = new StringBuilder();
		index++;
		while(index<string.length() && !string.substring(index,index+1).matches(shortOperatorsRegexp) && string.charAt(index)!=' '){
			if(variableName.length()>0){
				variableName.append("#");
			}
			variableName.append(string.substring(index, string.indexOf("#",index)));
			index = string.indexOf("#",index) + 1;
		}
		return new Variable(variableName.toString());
	}
	
	private Operator extractOperator(String string) throws MalformedFormulaException{
		if(operators.contains(string.substring(index,index+1))){
			Operator operator = Operator.buildOperator(string.substring(index,index+1));
			index++;
			return operator;
		}
		if(operators.contains(string.substring(index,index+3))){
			Operator operator = Operator.buildOperator(string.substring(index,index+3));
			index = index+3;
			return operator;
		}
		throw new MalformedFormulaException("unknown operator: "+string.substring(index));
	}
	
	private Constant extractConstant(String string){
		StringBuilder constantName = new StringBuilder();
		while(string.substring(index, index+1).matches("[0-9.]")){
			constantName.append(string.substring(index, index+1));
			index++;
		}
		return new Constant(Double.parseDouble(constantName.toString()));
	}
	
	public Formula clone(){
		Formula clone = new Formula();
		clone.destinationPropertyName = ""+destinationPropertyName;
		for(FormulaSeme originSeme=firstSeme; originSeme!=null; originSeme=originSeme.getNext()){
			clone.addSeme(originSeme.clone());
		}
		return clone;
	}
	
	public void process(Personnage personnage) throws MalformedFormulaException{
		Property destinationProperty = personnage.getProperty(destinationPropertyName);
		if(destinationProperty.getValue() instanceof DoubleValue){
			((DoubleValue)destinationProperty.getValue()).setValue(evaluation(personnage));
		}else if(destinationProperty.getValue() instanceof IntValue){
			int newint = (int) evaluation(personnage);
			((IntValue)destinationProperty.getValue()).setValue(newint);
		}else{
			destinationProperty.getValue().setValue(""+evaluation(personnage));
		}
	}
	
	public double evaluation(Personnage personnage) throws MalformedFormulaException{
		return this.clone().destructiveEvaluation(personnage);
	}
	
	public double destructiveEvaluation(Personnage personnage) throws MalformedFormulaException{
		for(int priority = Operator.MIN_PRIORITY; priority<=Operator.MAX_PRIORITY; priority++){
			for(FormulaSeme seme = firstSeme; seme!=null; seme = seme.getNext()){
				if(seme instanceof Operator){
					Operator operator = ((Operator)seme);
					if(operator.getPriority()==priority){
						operator.calculate(personnage);
					}
				}
			}
		}
		if(firstSeme!=lastSeme){
			throw new MalformedFormulaException("Syntax Error: "+this);
		}
		if(firstSeme instanceof Constant){
			return ((Constant)firstSeme).getValue();
		}
		if(firstSeme instanceof Variable){
			return ((Variable)firstSeme).getValue(personnage);
		}
		throw new MalformedFormulaException("Syntax Error: "+this);
	}

	public String getDestinationPropertyName() {
		return destinationPropertyName;
	}
	public void setDestinationPropertyName(String destinationPropertyName) {
		this.destinationPropertyName = destinationPropertyName;
	}
	public FormulaSeme getFirstSeme() {
		return firstSeme;
	}
	public FormulaSeme getLastSeme() {
		return lastSeme;
	}
	public void setFirstSeme(FormulaSeme firstSeme) {
		this.firstSeme = firstSeme;
	}
	public void setLastSeme(FormulaSeme lastSeme) {
		this.lastSeme = lastSeme;
	}
	public Set<String> getVariableNames() {
		return variableNames;
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		if(destinationPropertyName!=null){
			sb.append(destinationPropertyName);
			sb.append("=");
		}
		for(FormulaSeme seme = firstSeme; seme!=null; seme = seme.getNext()){
			sb.append(seme.toString());
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Formula)){
			return false;
		}
		Formula otherFormula = (Formula)object;
		if(!otherFormula.destinationPropertyName.equals(this.destinationPropertyName)){
			return false;
		}
		FormulaSeme seme = this.firstSeme;
		FormulaSeme otherSeme = otherFormula.firstSeme;
		while(seme!=null && otherSeme!=null){
			if(!seme.equals(otherSeme)){
				return false;
			}
			seme = seme.getNext();
			otherSeme = otherSeme.getNext();
		}
		if(seme!=null || otherSeme!=null){
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode(){
		int result = 0;
		for(FormulaSeme seme = firstSeme; seme!=null; seme = seme.getNext()){
			result = result + seme.hashCode();
		}
		return result;
	}
	
	public void addSeme(FormulaSeme seme){
		seme.formula = this;
		if(firstSeme==null){
			firstSeme = seme;
			lastSeme = seme;
		}else{
			lastSeme.setNext(seme);
			lastSeme = seme;
		}
	}
	
	

}
