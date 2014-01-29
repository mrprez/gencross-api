package com.mrprez.gencross.formula.operator;

import com.mrprez.gencross.formula.FormulaSeme;


public class DivisionOperator extends BinaryOperator {

	@Override
	public FormulaSeme clone() {
		return new DivisionOperator();
	}

	@Override
	public double calculate(double d1, double d2) {
		return d1/d2;
	}

	@Override
	public int getPriority() {
		return 6;
	}
	
	@Override
	public String toString(){
		return "/";
	}

}
