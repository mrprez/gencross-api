package com.mrprez.gencross;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.mrprez.gencross.history.HistoryFactory;
import com.mrprez.gencross.renderer.Renderer;
import com.mrprez.gencross.value.IntValue;
import com.mrprez.gencross.value.StringValue;
import com.mrprez.gencross.value.Value;

public class Property implements PropertyOwner {
	private String name;
	private String specification = null;
	private Value value;
	private Value min;
	private Value max;
	private boolean editable = true;
	private boolean removable = true;
	private PropertiesList subProperties;
	private List<Value> options;
	private PropertyOwner owner;
	private HistoryFactory historyFactory;
	private Renderer renderer;
	private String comment;
	public static final String SPECIFICATION_SEPARATOR = " - ";
	
	
	public Property(String name, PropertyOwner owner){
		this.name = name;
		this.owner = owner;
	}
	
	public Property(String name, Value value, PropertyOwner owner) {
		super();
		this.name = name;
		this.value = value;
		this.owner = owner;
	}
	
	public Property(String name, Value value, PropertyOwner owner, boolean editable) {
		super();
		this.name = name;
		this.value = value;
		this.owner = owner;
		this.editable = editable;
	}
	
	public Property(String name, Value value, PropertyOwner owner, boolean editable, Value min, Value max) {
		super();
		this.name = name;
		this.value = value;
		this.owner = owner;
		this.editable = editable;
		this.min = min;
		this.max = max;
	}
	
	public Property(String name, String value, PropertyOwner owner) {
		super();
		this.name = name;
		this.value = new StringValue(value);
		this.owner = owner;
	}
	public Property(String name, int value, PropertyOwner owner) {
		super();
		this.name = name;
		this.value = new IntValue(value);
		this.owner = owner;
	}
	

	public Property(Element element, PropertyOwner owner) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		super();
		this.owner = owner;
		name = element.attribute("name").getValue();
		value = Value.createValue(element.element("Value"));
		editable = element.attribute("editable").getValue().equals("true");
		if(element.attribute("canBeRemoved")!=null){
			removable = element.attribute("canBeRemoved").getValue().equals("true");
		}
		if(element.attribute("specification")!=null){
			specification = element.attribute("specification").getValue();
		}
		Element minEl = element.element("min");
		if (minEl != null) {
			min = Value.createValue(minEl.element("Value"));
		}
		Element maxEl = element.element("max");
		if (maxEl != null) {
			max = Value.createValue(maxEl.element("Value"));
		}
		Element optionsEl = element.element("valueChoice");
		if (optionsEl != null) {
			options = new ArrayList<Value>();
			Iterator<?> it = optionsEl.elementIterator();
			while (it.hasNext()) {
				options.add(Value.createValue((Element) it.next()));
			}
		}
		Element rendererEl = element.element("renderer");
		if(rendererEl!=null){
			renderer = getPersonnage().getRenderer(rendererEl.attributeValue("class"));
		}
		Element propertiesListEl = element.element("propertiesList");
		if(propertiesListEl!=null){
			subProperties = new PropertiesList(propertiesListEl, this);
		}
		Element historyFactoryEl = element.element("historyFactory");
		if(historyFactoryEl!=null){
			Class<? extends HistoryFactory> historyFactoryClass = getPersonnage().getHistoryFactoryClass(historyFactoryEl.attributeValue("class"));
			historyFactory = historyFactoryClass.getConstructor(Element.class).newInstance(historyFactoryEl);
		}
		Element commentEl = element.element("comment");
		if(commentEl!=null){
			comment = commentEl.getText().startsWith("\n")?commentEl.getText().replaceFirst("\n", ""):commentEl.getText();
		}
	}
	
	public PropertiesList addSubPropertiesList(boolean fixe, boolean open) {
		subProperties = new PropertiesList(fixe, open, this);
		return subProperties;
	}
	public Property getSubProperty(String name){
		return subProperties.get(name);
	}

	public PropertiesList getSubProperties() {
		return subProperties;
	}
	public void setSubProperties(PropertiesList subProperties) {
		subProperties.setOwner(this);
		this.subProperties = subProperties;
	}
	public void removeSubProperties(){
		subProperties = null;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String getFullName(){
		if(specification==null){
			return name;
		}
		return name+SPECIFICATION_SEPARATOR+specification;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public boolean isRemovable() {
		return removable;
	}
	public void setRemovable(boolean removable) {
		this.removable = removable;
	}
	public PropertyOwner getOwner() {
		return owner;
	}
	public void setOwner(PropertyOwner owner) {
		this.owner = owner;
	}
	public Renderer getRenderer() {
		if(renderer==null){
			return Renderer.DEFAULT_RENDERER;
		}
		return renderer;
	}
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	public void setEditableRecursivly(boolean editable){
		this.editable = editable;
		if(subProperties!=null){
			subProperties.setEditableRecursivly(editable);
		}
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Value getMin() {
		return min;
	}
	public void setMin(Value min) {
		this.min = min;
	}
	public Value getMax() {
		return max;
	}
	public void setMax(Value max) {
		this.max = max;
	}
	public HistoryFactory getHistoryFactory() {
		if(historyFactory==null){
			return owner.getHistoryFactory();
		}
		return historyFactory;
	}
	public HistoryFactory getActualHistoryFactory() {
		return historyFactory;
	}
	public void setHistoryFactory(HistoryFactory historyFactory) {
		this.historyFactory = historyFactory;
	}
	public List<Value> getOptions() {
		return options;
	}
	public void setOptions(List<Value> options) {
		this.options = options;
	}
	public void setOptions(Value valueTab[]){
		options = new ArrayList<Value>();
		for(int i=0; i<valueTab.length; i++){
			options.add(valueTab[i]);
		}
	}
	public void setOptions(String valueTab[]){
		options = new ArrayList<Value>();
		for(int i=0; i<valueTab.length; i++){
			options.add(new StringValue(valueTab[i]));
		}
	}
	public void setOptions(int valueTab[]){
		options = new ArrayList<Value>();
		for(int i=0; i<valueTab.length; i++){
			options.add(new IntValue(valueTab[i]));
		}
	}
	
	public Personnage getPersonnage(){
		if(owner instanceof Personnage){
			return (Personnage) owner;
		}
		return ((Property)owner).getPersonnage();
	}
	
	public String getAbsoluteName(){
		if(owner instanceof Property){
			return ((Property)owner).getAbsoluteName()+"#"+getFullName();
		}
		return getFullName();
	}

	@Override
	public Iterator<Property> iterator() {
		if(subProperties==null){
			return null;
		}
		return subProperties.iterator();
	}
	
	public void setMin() {
		if(value!=null){
			min = value.clone();
		}
	}
	public void setMax() {
		if(value!=null){
			max = value.clone();
		}
	}

	public String getText() {
		if(value!=null){
			return getRenderer().displayName(this)+" : "+getRenderer().displayValue(this);
		}
		return name;
	}
	
	public Property clone(){
		Property clone = new Property(""+name,owner);
		clone.setEditable(isEditable());
		clone.setRemovable(isRemovable());
		clone.setSpecification(specification==null?null:""+specification);
		clone.setComment(getComment()==null?null:""+comment);
		if(getValue()!=null){
			clone.setValue(getValue().clone());
		}
		if(getMin()!=null){
			clone.setMin(getMin().clone());
		}
		if(getMax()!=null){
			clone.setMax(getMax().clone());
		}
		if(renderer!=null){
			clone.setRenderer(getRenderer().copy());
		}
		if(getSubProperties()!=null){
			clone.setSubProperties(getSubProperties().clone());
			clone.getSubProperties().bindTo(clone);
		}
		if(getOptions()!=null){
			List<Value> cloneOptions = new ArrayList<Value>();
			for(Value value : getOptions()){
				cloneOptions.add(value.clone());
			}
			clone.setOptions(getOptions());
		}
		if(historyFactory!=null){
			clone.setHistoryFactory(historyFactory.clone());
		}
		return clone;
	}
	
	public Element getXML(){
		DefaultElement element = new DefaultElement("property");
		element.addAttribute("name", name);
		element.addAttribute("editable",editable?"true":"false");
		element.addAttribute("canBeRemoved",removable?"true":"false");
		if(specification!=null){
			element.addAttribute("specification",specification);
		}
		if(value!=null){
			element.add(value.getXML());
		}
		if(min!=null){
			Element minEl = element.addElement("min");
			minEl.add(min.getXML());
		}
		if(max!=null){
			Element maxEl = element.addElement("max");
			maxEl.add(max.getXML());
		}
		if(options!=null){
			Element optionsEl = element.addElement("valueChoice");
			for(Value optionValue : options){
				optionsEl.add(optionValue.getXML());
			}
		}
		if(renderer!=null){
			Element rendererEl = element.addElement("renderer");
			rendererEl.addAttribute("class", renderer.getName());
		}
		if(subProperties!=null){
			element.add(subProperties.getXML());
		}
		if(historyFactory!=null){
			element.add(historyFactory.getXML());
		}
		if(comment!=null){
			element.addElement("comment").setText(comment);
		}
		return element;
	}
	
	
	
}
