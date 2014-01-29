package com.mrprez.gencross.formula.operator;




public class AdditionOperator extends BinaryOperator {

	

	@Override
	public AdditionOperator clone(){
		return new AdditionOperator();
	}

	@Override
	public double calculate(double d1, double d2) {
		return d1+d2;
	}

	@Override
	public int getPriority() {
		return 8;
	}
	
	@Override
	public String toString(){
		return "+";
	}

}
