package com.mrprez.gencross.formula.operator;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.formula.Constant;
import com.mrprez.gencross.formula.MalformedFormulaException;
import com.mrprez.gencross.formula.Variable;

public abstract class BinaryOperator extends Operator {
	
	

	@Override
	public void calculate(Personnage personnage)
			throws MalformedFormulaException {
		double d1;
		double d2;
		if(getPrevious() instanceof Variable){
			d1 = ((Variable)getPrevious()).getValue(personnage);
		}else if(getPrevious() instanceof Constant){
			d1 = ((Constant)getPrevious()).getValue();
		}else{
			throw new MalformedFormulaException("left terme of addition is invalid");
		}
		if(getNext() instanceof Variable){
			d2 = ((Variable)getNext()).getValue(personnage);
		}else if(getNext() instanceof Constant){
			d2 = ((Constant)getNext()).getValue();
		}else{
			throw new MalformedFormulaException("right terme of addition is invalid");
		}
		Constant result = new Constant(calculate(d1,d2));
		result.setFormula(formula);
		if(getPrevious().getPrevious()==null){
			formula.setFirstSeme(result);
		}else{
			getPrevious().getPrevious().setNext(result);
		}
		if(getNext().getNext()==null){
			formula.setLastSeme(result);
		}else{
			getNext().getNext().setPrevious(result);
		}
	}
	
	public abstract double calculate(double d1, double d2);

}
