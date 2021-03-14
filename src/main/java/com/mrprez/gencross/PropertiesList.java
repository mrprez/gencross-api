package com.mrprez.gencross;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.mrprez.gencross.value.IntValue;
import com.mrprez.gencross.value.Value;

public class PropertiesList implements Collection<Property> {
	private boolean fixe = true;
	private boolean open = false;
	private Boolean canRemoveElement;
	private LinkedHashMap<String, Property> properties = new LinkedHashMap<String, Property>();
	private LinkedHashMap<String, Property> options = new LinkedHashMap<String, Property>();
	private Property owner;
	private Property defaultProperty;
	
	public PropertiesList(Property owner) {
		super();
		this.owner = owner;
	}
	
	public PropertiesList(boolean fixe, boolean open, Property owner) {
		super();
		this.fixe = fixe;
		this.open = open;
		this.owner = owner;
	}
	
	public PropertiesList(String[] nameTab, Value defaultValue, boolean fixe, boolean open, Property owner){
		super();
		this.fixe = fixe;
		this.open = open;
		this.owner = owner;
		for(int i=0; i<nameTab.length; i++){
			add(new Property(nameTab[i], defaultValue.clone(), owner));
		}
	}
	
	public PropertiesList(Element element, Property owner) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException{
		super();
		this.owner = owner;
		fixe = element.attributeValue("fixe").equals("true");
		open = element.attributeValue("open").equals("true");
		if(element.attributeValue("canRemoveElement")!=null){
			canRemoveElement = element.attributeValue("canRemoveElement").equals("true");
		}
		Element defaultPropertyEl = element.element("defaultProperty");
		if(defaultPropertyEl!=null){
			defaultProperty = new Property(defaultPropertyEl.element("property"), owner);
		}
		if(element.element("propertyChoice")!=null){
			Iterator<?> it = element.element("propertyChoice").elementIterator("property");
			while(it.hasNext()){
				Property option = new Property((Element)it.next(),owner);
				options.put(option.getFullName(), option);
			}
		}
		Iterator<?> it = element.element("properties").elementIterator("property");
		while(it.hasNext()){
			Property property = new Property((Element)it.next(),owner);
			add(property);
		}
	}
	
	public boolean canRemoveElement(){
		if(canRemoveElement!=null){
			return canRemoveElement.booleanValue();
		}else{
			return !fixe;
		}
	}
	
	
	public Boolean getCanRemoveElement() {
		return canRemoveElement;
	}
	public void setCanRemoveElement(Boolean canRemoveElement) {
		this.canRemoveElement = canRemoveElement;
	}
	public boolean isFixe() {
		return fixe;
	}
	public boolean getFixe() {
		return fixe;
	}
	public void setFixe(boolean fixe) {
		this.fixe = fixe;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open, Property defaultProperty) {
		this.open = open;
		this.defaultProperty = defaultProperty;
		this.defaultProperty.setOwner(owner);
	}
	public void setOpen(boolean open){
		setOpen(open, new Property("",new IntValue(0),owner));
	}
	public Property getDefaultProperty() {
		return defaultProperty;
	}
	public void setDefaultProperty(Property defaultProperty) {
		this.defaultProperty = defaultProperty;
	}
	public Map<String, Property> getProperties() {
		return properties;
	}
	public void setProperties(LinkedHashMap<String, Property> properties) {
		this.properties = properties;
	}
	public Map<String, Property> getOptions() {
		return options;
	}
	public void setOptions(LinkedHashMap<String, Property> options) {
		this.options = options;
	}
	public void setOptions(String[] optionTab, Property defaultProperty) {
		options = new LinkedHashMap<String, Property>();
		for(int i=0; i<optionTab.length; i++){
			Property newProperty = defaultProperty.clone();
			newProperty.setName(optionTab[i]);
			newProperty.setOwner(owner);
			options.put(optionTab[i], newProperty);
		}
	}
	public void addOptionProperty(Property property){
		options.put(property.getFullName(), property);
		property.setOwner(this.owner);
	}
	public void setOptions(String[] optionTab, Value defaultValue) {
		setOptions(optionTab, new Property("",defaultValue, owner));
	}
	
	public Property getOwner() {
		return owner;
	}
	public void setOwner(Property owner) {
		this.owner = owner;
	}
	
	public boolean add(Property property){
		properties.put(property.getFullName(),property);
		property.setOwner(owner);
		return true;
	}
	
	public Property remove(Property property){
		return properties.remove(property.getFullName());
	}

	public int size(){
		return properties.size();
	}
	public Property get(String name){
		return properties.get(name);
	}
	public Property get(int index){
		Iterator<Property> it = properties.values().iterator();
		int i = 0;
		while(it.hasNext()){
			Property prop = it.next();
			if(i==index){
				return prop;
			}
			i++;
		}
		return null;
	}
	public Set<String> keySet(){
		return properties.keySet();
	}

	@Override
	public Iterator<Property> iterator() {
		return properties.values().iterator();
	}
	
	public void setEditableRecursivly(boolean editable){
		for(Property property : properties.values()){
			property.setEditableRecursivly(editable);
		}
		if(defaultProperty!=null){
			defaultProperty.setEditable(editable);
		}
		for(Property property : options.values()){
			property.setEditableRecursivly(editable);
		}
	}
	
	public void setMin(Value min){
		for(Property property : properties.values()){
			property.setMin(min);
		}
		if(options!=null){
			for(Property option : options.values()){
				option.setMin(min);
			}
		}
		if(defaultProperty!=null){
			defaultProperty.setMin(min);
		}
	}
	public void setMax(Value max){
		for(Property property : properties.values()){
			property.setMax(max);
		}
		if(options!=null){
			for(Property option : options.values()){
				option.setMax(max);
			}
		}
		if(defaultProperty!=null){
			defaultProperty.setMax(max);
		}
	}
	public void setMin(){
		for(Property property : properties.values()){
			property.setMin(property.getValue().clone());
		}
		if(options!=null){
			for(Property option : options.values()){
				option.setMin();
			}
		}
		if(defaultProperty!=null){
			defaultProperty.setMin();
		}
	}
	public void setMax(){
		for(Property property : properties.values()){
			property.setMax();
		}
		if(options!=null){
			for(Property option : options.values()){
				option.setMax();
			}
		}
		if(defaultProperty!=null){
			defaultProperty.setMax();
		}
	}
	
	public Element getXML(){
		DefaultElement element = new DefaultElement("propertiesList");
		element.addAttribute("fixe",fixe?"true":"false");
		element.addAttribute("open",open?"true":"false");
		if(canRemoveElement!=null){
			element.addAttribute("canRemoveElement",canRemoveElement?"true":"false");
		}
		if(defaultProperty!=null){
			element.addElement("defaultProperty").add(defaultProperty.getXML());
		}
		Element optionsEl = element.addElement("propertyChoice");
		for(Property option : options.values()){
			optionsEl.add(option.getXML());
		}
		Element propertiesEl = element.addElement("properties");
		for(Property property : properties.values()){
			Element propertyEl = property.getXML();
			propertiesEl.add(propertyEl);
		}
		return element;
	}

	@Override
	public PropertiesList clone(){
		PropertiesList clone = new PropertiesList(fixe,open,owner);
		if(canRemoveElement!=null){
			clone.canRemoveElement = Boolean.valueOf(canRemoveElement);
		}
		if(defaultProperty!=null){
			clone.defaultProperty = defaultProperty.clone();
		}
		for(String optionName : options.keySet()){
			clone.options.put(""+optionName, options.get(optionName).clone());
		}
		for(String propertyName : properties.keySet()){
			clone.properties.put(""+propertyName, properties.get(propertyName).clone());
		}
		return clone;
	}
	
	public void bindTo(Property property){
		setOwner(property);
		if(defaultProperty!=null){
			defaultProperty.setOwner(property);
		}
		for(String optionName : options.keySet()){
			options.get(optionName).setOwner(property);
		}
		for(String propertyName : properties.keySet()){
			properties.get(propertyName).setOwner(property);
		}
	}

	@Override
	public boolean addAll(Collection<? extends Property> arg0) {
		for(Property p : arg0){
			add(p);
			p.setOwner(owner);
		}
		return true;
	}

	@Override
	public void clear() {
		properties.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return properties.containsValue(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return properties.values().containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		return properties.isEmpty();
	}

	@Override
	public boolean remove(Object arg0) {
		return properties.remove(arg0)!=null;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		properties.clear();
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> coll) {
		boolean result = false;
		for(Object object : coll){
			result = result || properties.remove(object)!=null;
		}
		return result;
	}

	@Override
	public Object[] toArray() {
		Property array[] = new Property[properties.size()];
		int i = 0;
		for(Property property : properties.values()){
			array[i] = property;
			i++;
		}
		return array;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] arg0) {
		Class<?> clazz = arg0.getClass().getComponentType();
		T array[] = (T[]) Array.newInstance(clazz, properties.size());
		int i = 0;
		for(Property property : properties.values()){
			array[i] = (T) property;
			i++;
		}
		return array;
	}
	
	
	
}
