package com.mrprez.gencross.formula.operator;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.formula.FormulaSeme;
import com.mrprez.gencross.formula.MalformedFormulaException;

public abstract class Operator extends FormulaSeme{
	public static final int MAX_PRIORITY = 10;
	public static final int MIN_PRIORITY = 0;
	
	
	public abstract void calculate(Personnage personnage) throws MalformedFormulaException;
	public abstract int getPriority();
	public abstract String toString();
	
	
	public static Operator buildOperator(String string) throws MalformedFormulaException{
		if("+".equals(string)){
			return new AdditionOperator();
		}
		if("-".equals(string)){
			return new SubstractionOperator();
		}
		if("*".equals(string)){
			return new MultiplicationOperator();
		}
		if("/".equals(string)){
			return new DivisionOperator();
		}
		if("div".equals(string)){
			return new EuclideOperator();
		}
		if("max".equals(string)){
			return new MaxOperator();
		}
		if("min".equals(string)){
			return new MinOperator();
		}
		if("(".equals(string)){
			return new LeftParenthesisOperator();
		}
		if(")".equals(string)){
			return new RightParenthesisOperator();
		}
		throw new MalformedFormulaException("unknown operator: "+string);
	}
	
	@Override
	public boolean equals(Object object) {
		return getClass().isInstance(object);
	}
	
	@Override
	public int hashCode(){
		return toString().hashCode();
	}
	
	
}
