package com.mrprez.gencross.formula;

public class Constant extends FormulaSeme {
	private double value;

	public Constant(double value) {
		super();
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	@Override
	public Constant clone() {
		return new Constant(value);
	}
	
	@Override
	public String toString(){
		return ""+value;
	}

	@Override
	public boolean equals(Object object) {
		if(object instanceof Constant){
			return ((Constant)object).value == value;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return (int)value;
	}

}
