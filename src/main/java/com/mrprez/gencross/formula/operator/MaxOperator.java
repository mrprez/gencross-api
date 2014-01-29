package com.mrprez.gencross.formula.operator;

import com.mrprez.gencross.formula.FormulaSeme;

public class MaxOperator extends BinaryOperator {

	@Override
	public double calculate(double d1, double d2) {
		return Math.max(d1, d2);
	}

	@Override
	public FormulaSeme clone() {
		return new MaxOperator();
	}

	@Override
	public int getPriority() {
		return 2;
	}
	
	@Override
	public String toString(){
		return "max";
	}

}
