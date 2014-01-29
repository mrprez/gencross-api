package com.mrprez.gencross.value;

public class IntValue extends Value {
	private Integer value;
	private Integer offset = 1;
	
	
	public IntValue(Integer value) {
		super();
		this.value = value;
	}



	@Override
	public String toString() {
		if(value==null){
			return null;
		}
		return value.toString();
	}

	@Override
	public Value clone() {
		IntValue clone;
		if(value==null){
			clone = new IntValue(null);
		}else{
			clone = new IntValue(new Integer(value));
		}
		clone.setOffset(new Integer(offset));
		return clone;
	}



	@Override
	public void setValue(String value) {
		this.value = new Integer(value);
	}
	public Integer getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}



	@Override
	public boolean equals(Object arg0) {
		if(!(arg0 instanceof IntValue)){
			return false;
		}
		return ((IntValue)arg0).value.equals(value);
	}



	@Override
	public int hashCode() {
		return value.hashCode();
	}



	@Override
	public Integer getOffset() {
		return offset;
	}



	@Override
	public void setOffset(Object offset) {
		this.offset = (Integer)offset;
	}



	@Override
	public void decrease() {
		value = value-offset;
	}



	@Override
	public void increase() {
		value = value+offset;
	}

	@Override
	public double getDouble() {
		return value;
	}

	@Override
	public int getInt() {
		return value;
	}

	@Override
	public String getString() {
		return value.toString();
	}



	
	

}
