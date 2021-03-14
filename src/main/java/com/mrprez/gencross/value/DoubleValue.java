package com.mrprez.gencross.value;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;

public class DoubleValue extends Value {
	private Double value;
	private Double offset = 1.0;
	private static DecimalFormat format = buildFormat();
	
	private static DecimalFormat buildFormat(){
		DecimalFormat format = new DecimalFormat("#.#");
		DecimalFormatSymbols decimalFormatSymbols = format.getDecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		format.setDecimalFormatSymbols(decimalFormatSymbols);
		return format;
	}
	
	public DoubleValue(Double value) {
		super();
		this.value = value;
	}

	@Override
	public Value clone() {
		DoubleValue clone = new DoubleValue(Double.valueOf(value.doubleValue()));
		clone.setOffset(Double.valueOf(offset));
		return clone;
	}

	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof DoubleValue){
			return ((DoubleValue)arg0).value.equals(value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public void setValue(String value) {
		this.value = Double.valueOf(value);
	}

	@Override
	public String toString() {
		return format.format(value);
	}

	@Override
	public Double getOffset() {
		return offset;
	}

	@Override
	public void setOffset(Object offset) {
		this.offset = (Double) offset;
		int i=0;
		while(Math.floor(this.offset.doubleValue()*Math.pow(10, i))!=this.offset.doubleValue()*Math.pow(10, i)){
			i++;
		}
		char formatCharTab[] = new char[i];
		Arrays.fill(formatCharTab, '#');
		format.applyPattern("#."+new String(formatCharTab)+"#");
	}

	@Override
	public void decrease() {
		value = value - offset;
	}

	@Override
	public void increase() {
		value = value + offset;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public double getDouble() {
		return value;
	}

	@Override
	public int getInt() {
		return value.intValue();
	}

	@Override
	public String getString() {
		return format.format(value);
	}

	
	

}
