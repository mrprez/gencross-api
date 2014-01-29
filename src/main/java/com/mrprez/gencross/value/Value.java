package com.mrprez.gencross.value;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

public abstract class Value {
	
	public abstract String toString();
	public abstract Value clone();
	public abstract void setValue(String value);
	public abstract Object getOffset();
	public abstract void setOffset(Object offset);
	public abstract void increase();
	public abstract void decrease();
	public abstract int getInt();
	public abstract String getString();
	public abstract double getDouble();
	@Override
	public abstract boolean equals(Object arg0);
	@Override
	public abstract int hashCode();
	
	public Element getXML(){
		Element element = new DefaultElement("Value");
		element.addAttribute("class", getClass().getName());
		element.setText(toString());
		if(getOffset()!=null){
			element.addAttribute("offset", getOffset().toString());
		}
		return element;
	}
	
	public static Value createValue(Element element){
		if(element==null){
			return null;
		}
		String className = element.attribute("class").getText();
		Value result = null;
		if(className.equals(IntValue.class.getName())){
			result = new IntValue(Integer.parseInt(element.getText()));
			if(element.attributeValue("offset")!=null){
				result.setOffset(Integer.parseInt(element.attributeValue("offset")));
			}
		}else if(className.equals(StringValue.class.getName())){
			result =  new StringValue(element.getText());
			if(element.attributeValue("offset")!=null){
				result.setOffset(element.attributeValue("offset"));
			}
		}else if(className.equals(DoubleValue.class.getName())){
			result = new DoubleValue(Double.parseDouble(element.getText()));
			if(element.attributeValue("offset")!=null){
				result.setOffset(Double.parseDouble(element.attributeValue("offset")));
			}
		}
		return result;
	}
	
	public static StringValue createValue(String string){
		return new StringValue(string);
	}
	
	public static IntValue createValue(Integer integer){
		return new IntValue(integer);
	}
	
	public static DoubleValue createValue(Double d){
		return new DoubleValue(d);
	}
	public static DoubleValue createValue(Double d, Double offset){
		DoubleValue result = new DoubleValue(d);
		result.setOffset(offset);
		return result;
	}
	

}
