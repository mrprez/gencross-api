package com.mrprez.gencross.formula.operator;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.formula.FormulaSeme;
import com.mrprez.gencross.formula.MalformedFormulaException;

public class RightParenthesisOperator extends Operator {

	@Override
	public void calculate(Personnage personnage) throws MalformedFormulaException {
		;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public FormulaSeme clone() {
		return new RightParenthesisOperator();
	}
	
	@Override
	public String toString(){
		return ")";
	}

}
