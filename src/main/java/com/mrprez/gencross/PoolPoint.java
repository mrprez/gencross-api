package com.mrprez.gencross;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

public class PoolPoint {
	private String name;
	private int total;
	private int remaining;
	private boolean toEmpty = false;
	
	
	public PoolPoint(String name, int total) {
		super();
		this.name = name;
		this.total = total;
		this.remaining = total;
	}
	
	public PoolPoint(Element element){
		super();
		name = element.attributeValue("name");
		remaining = Integer.parseInt(element.attributeValue("remaining"));
		total = Integer.parseInt(element.attributeValue("total"));
		if(element.attribute("toEmpty")!=null){
			toEmpty = "true".equals(element.attributeValue("toEmpty"));
		}
	}
	
	public void spend(int cost){
		remaining = remaining - cost;
	}
	
	public void add(int nb){
		total = total + nb;
		remaining = remaining + nb;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getRemaining() {
		return remaining;
	}
	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}
	public boolean isToEmpty() {
		return toEmpty;
	}
	public void setToEmpty(boolean toEmpty) {
		this.toEmpty = toEmpty;
	}
	

	public String toString(){
		return ""+remaining+"/"+total;
	}
	public PoolPoint clone(){
		PoolPoint clone = new PoolPoint(name, total);
		clone.remaining = remaining;
		clone.toEmpty = toEmpty;
		return clone;
	}
	public Element getXml(){
		DefaultElement element = new DefaultElement("pool");
		element.addAttribute("name", name);
		element.addAttribute("remaining",""+remaining);
		element.addAttribute("total",""+total);
		element.addAttribute("toEmpty", toEmpty?"true":"false");
		return element;
	}
	

}
