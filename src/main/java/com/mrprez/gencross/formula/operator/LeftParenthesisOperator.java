package com.mrprez.gencross.formula.operator;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.formula.Constant;
import com.mrprez.gencross.formula.Formula;
import com.mrprez.gencross.formula.FormulaSeme;
import com.mrprez.gencross.formula.MalformedFormulaException;

public class LeftParenthesisOperator extends Operator {

	@Override
	public void calculate(Personnage personnage) throws MalformedFormulaException {
		Formula subFormula = new Formula();
		FormulaSeme seme;
		for(seme = getNext();!(seme instanceof RightParenthesisOperator) && seme!=null; seme=seme.getNext()){
			if(seme instanceof LeftParenthesisOperator){
				((LeftParenthesisOperator)seme).calculate(personnage);
				seme = seme.getPrevious().getNext();
			}
			subFormula.addSeme(seme.clone());
		}
		if(seme==null){
			throw new MalformedFormulaException("Missing right parenthesis");
		}
		RightParenthesisOperator closingParenthesis = (RightParenthesisOperator)seme;
		double evaluation = subFormula.destructiveEvaluation(personnage);
		FormulaSeme result = new Constant(evaluation);
		result.setPrevious(getPrevious());
		result.setNext(closingParenthesis.getNext());
	}
	

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public FormulaSeme clone() {
		return new LeftParenthesisOperator();
	}
	
	@Override
	public String toString(){
		return "(";
	}

}
