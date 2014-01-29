package com.mrprez.gencross.history;

import java.util.Date;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.mrprez.gencross.value.Value;


public final class HistoryItem {
	public static final int CREATION = 1;
	public static final int UPDATE = 2;
	public static final int DELETION = 3;
	
	
	private Date date = new Date();
	private Value oldValue;
	private Value newValue;
	private String absoluteName;
	private String pointPool;
	private String phase;
	private int action;
	private int cost;
	
	
	public HistoryItem() {
		super();
	}
	
	public HistoryItem(Element element){
		super();
		action = Integer.parseInt(element.attributeValue("action"));
		absoluteName = element.element("absoluteName").getText();
		if(element.element("oldValue")!=null){
			oldValue = Value.createValue(element.element("oldValue").element("Value"));
		}
		if(element.element("newValue")!=null){
			newValue = Value.createValue(element.element("newValue").element("Value"));
		}
		date = new Date(Long.parseLong(element.element("date").getText()));
		pointPool = element.element("pointPool").getText();
		phase = element.element("phase").getText();
		cost = Integer.parseInt(element.element("cost").getText());
	}
	
	
	
	public HistoryItem clone(){
		HistoryItem clone = new HistoryItem();
		clone.date = new Date(date.getTime());
		if(oldValue!=null){
			clone.setOldValue(oldValue.clone());
		}
		if(newValue!=null){
			clone.setNewValue(newValue.clone());
		}
		clone.setAbsoluteName(new String(absoluteName));
		clone.setPhase(new String(phase));
		clone.setCost(cost);
		clone.setPointPool(new String(pointPool));
		clone.setAction(action);
		return clone;
	}
	
	public Element getXML(){
		Element element = new DefaultElement("historyItem");
		element.addAttribute("action", ""+action);
		if(absoluteName!=null){
			element.addElement("absoluteName").setText(absoluteName);
		}
		if(oldValue!=null){
			element.addElement("oldValue").add(oldValue.getXML());
		}
		if(newValue!=null){
			element.addElement("newValue").add(newValue.getXML());
		}
		element.addElement("date").setText(""+date.getTime());
		if(phase!=null){
			element.addElement("phase").setText(phase);
		}
		if(pointPool!=null){
			element.addElement("pointPool").setText(pointPool);
		}
		element.addElement("cost").setText(""+cost);
		return element;
	}

	public Value getOldValue() {
		return oldValue;
	}
	public void setOldValue(Value oldValue) {
		this.oldValue = oldValue;
	}
	public Value getNewValue() {
		return newValue;
	}
	public void setNewValue(Value newValue) {
		this.newValue = newValue;
	}
	public String getAbsoluteName() {
		return absoluteName;
	}
	public void setAbsoluteName(String absoluteName) {
		this.absoluteName = absoluteName;
	}
	public Date getDate() {
		return date;
	}
	public String getPointPool() {
		return pointPool;
	}
	public void setPointPool(String pointPool) {
		this.pointPool = pointPool;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getShortName(){
		String shortName = absoluteName.substring(absoluteName.lastIndexOf("#")+1);
		if(shortName.length()>20){
			return shortName.substring(0, 20);
		}
		return shortName;
	}
	
}
