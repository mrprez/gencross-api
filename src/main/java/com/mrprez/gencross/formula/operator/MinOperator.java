package com.mrprez.gencross.formula.operator;

import com.mrprez.gencross.formula.FormulaSeme;


public class MinOperator extends BinaryOperator {

	@Override
	public FormulaSeme clone() {
		return new MinOperator();
	}

	@Override
	public double calculate(double d1, double d2) {
		return Math.min(d1, d2);
	}

	@Override
	public int getPriority() {
		return 4;
	}
	
	@Override
	public String toString(){
		return "min";
	}

}
