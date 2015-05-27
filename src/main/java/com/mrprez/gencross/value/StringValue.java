package com.mrprez.gencross.value;

public class StringValue extends Value {
	private String value;
	private String offset;
	
	
	public StringValue(String value) {
		super();
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public Value clone() {
		StringValue clone = new StringValue(value);
		clone.setOffset(offset);
		return clone;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!getClass().equals(obj.getClass()))
			return false;
		StringValue other = (StringValue) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		if(value==null){
			return 0;
		}
		return value.hashCode();
	}

	@Override
	public String getOffset() {
		return offset;
	}

	@Override
	public void setOffset(Object offset) {
		this.offset = (String) offset;
	}

	@Override
	public void decrease() {
		if(value.contains(offset)){
			int index = value.lastIndexOf(offset);
			value = value.substring(0, index)+value.substring(index+offset.length());
		}
	}

	@Override
	public void increase() {
		value = value+offset;
	}

	@Override
	public double getDouble() {
		return Double.parseDouble(value);
	}

	@Override
	public int getInt() {
		return Integer.parseInt(value);
	}

	@Override
	public String getString() {
		return value;
	}
	
	
	
	

}
