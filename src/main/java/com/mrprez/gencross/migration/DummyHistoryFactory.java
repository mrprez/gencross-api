package com.mrprez.gencross.migration;

import java.util.Map;

import org.dom4j.Element;

import com.mrprez.gencross.history.HistoryFactory;
import com.mrprez.gencross.value.Value;

public class DummyHistoryFactory extends HistoryFactory {
	private Map<String, String> args;
	private String className;
	

	public DummyHistoryFactory(Element element) {
		super(element);
		className = element.attributeValue("class");
	}

	@Override
	public int getCost(Value oldValue, Value newValue, int action) {
		return 0;
	}

	@Override
	public DummyHistoryFactory clone() {
		throw new RuntimeException("DummyHistoryItem cannot be used");
	}

	@Override
	public Map<String, String> getArgs() {
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) {
		this.args = args;
	}
	
	@Override
	public Element getXML(){
		Element element = super.getXML();
		element.attribute("class").setValue(className);
		return element;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	

}
