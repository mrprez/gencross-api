package com.mrprez.gencross.formula.operator;

import com.mrprez.gencross.formula.FormulaSeme;


public class SubstractionOperator extends BinaryOperator {

	@Override
	public FormulaSeme clone() {
		return new SubstractionOperator();
	}

	@Override
	public double calculate(double d1, double d2) {
		return d1-d2;
	}

	@Override
	public int getPriority() {
		return 8;
	}
	
	@Override
	public String toString(){
		return "-";
	}

}
