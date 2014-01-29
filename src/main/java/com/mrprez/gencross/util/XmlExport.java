package com.mrprez.gencross.util;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PoolPoint;
import com.mrprez.gencross.Property;

public class XmlExport {
	
	public Document buildXmlToExport(Personnage personnage){
		Document document = DocumentHelper.createDocument();
		Element personnageEl = document.addElement("personnage");
		for(PoolPoint poolPoint : personnage.getPointPools().values()){
			Element poolPointEl = personnageEl.addElement("pointPool");
			poolPointEl.addAttribute("name", poolPoint.getName());
			poolPointEl.addElement("remaining").setText(""+poolPoint.getRemaining());
			poolPointEl.addElement("total").setText(""+poolPoint.getTotal());
		}
		Iterator<Property> it = personnage.iterator();
		while(it.hasNext()){
			addPropertyToElement(it.next(), personnageEl);
		}
		return document;
	}
	
	
	private Element addPropertyToElement(Property property, Element element){
		Element propertyEl = element.addElement("property");
		propertyEl.addAttribute("name", property.getRenderer().displayName(property));
		propertyEl.addElement("name").setText(property.getRenderer().displayName(property));
		if(property.getValue()!=null){
			propertyEl.addElement("Value").setText(property.getRenderer().displayValue(property));
		}
		if(property.getSubProperties()!=null){
			Iterator<Property> it = property.iterator();
			int index = 0;
			while(it.hasNext()){
				index++;
				Element subPropertyEl = addPropertyToElement(it.next(), propertyEl);
				subPropertyEl.addAttribute("index", ""+index);
			}
		}
		return propertyEl;
	}
	
}
