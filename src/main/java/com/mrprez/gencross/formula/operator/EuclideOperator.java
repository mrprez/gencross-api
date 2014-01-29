package com.mrprez.gencross.formula.operator;

import com.mrprez.gencross.formula.FormulaSeme;

public class EuclideOperator extends BinaryOperator {

	@Override
	public double calculate(double d1, double d2) {
		return Math.floor(d1/d2);
	}

	@Override
	public FormulaSeme clone() {
		return new EuclideOperator();
	}

	@Override
	public int getPriority() {
		return 6;
	}
	
	@Override
	public String toString(){
		return "div";
	}

}
