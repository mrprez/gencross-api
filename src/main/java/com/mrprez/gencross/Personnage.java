package com.mrprez.gencross;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.formula.FormulaManager;
import com.mrprez.gencross.formula.MalformedFormulaException;
import com.mrprez.gencross.history.HistoryFactory;
import com.mrprez.gencross.history.HistoryItem;
import com.mrprez.gencross.listener.AfterAddPropertyListener;
import com.mrprez.gencross.listener.AfterChangeValueListener;
import com.mrprez.gencross.listener.AfterDeletePropertyListener;
import com.mrprez.gencross.listener.BeforeAddPropertyListener;
import com.mrprez.gencross.listener.BeforeChangeValueListener;
import com.mrprez.gencross.listener.BeforeDeletePropertyListener;
import com.mrprez.gencross.listener.PassToNextPhaseListener;
import com.mrprez.gencross.listener.PropertyListener;
import com.mrprez.gencross.renderer.Renderer;
import com.mrprez.gencross.util.ReversedListIterator;
import com.mrprez.gencross.util.ValueComparator;
import com.mrprez.gencross.value.IntValue;
import com.mrprez.gencross.value.StringValue;
import com.mrprez.gencross.value.Value;

public class Personnage implements PropertyOwner {
	public final static Version gencrossVersion = new Version(1, 11);
	protected List<String> phaseList = new ArrayList<String>();
	protected String phase = "Start";
	protected LinkedHashMap<String, PoolPoint> pointPools = new LinkedHashMap<String, PoolPoint>();
	protected LinkedHashMap<String, Property> properties = new LinkedHashMap<String, Property>();
	protected List<String> errors = new ArrayList<String>();
	protected List<HistoryItem> history = new ArrayList<HistoryItem>();
	protected HistoryFactory historyFactory = HistoryFactory.FREE_HISTORY_FACTORY;
	protected List<BeforeChangeValueListener> beforeChangeValueListeners = new ArrayList<BeforeChangeValueListener>();
	protected List<AfterChangeValueListener> afterChangeValueListeners = new ArrayList<AfterChangeValueListener>();
	protected List<BeforeAddPropertyListener> beforeAddPropertyListeners = new ArrayList<BeforeAddPropertyListener>();
	protected List<AfterAddPropertyListener> afterAddPropertyListeners = new ArrayList<AfterAddPropertyListener>();
	protected List<BeforeDeletePropertyListener> beforeDeletePropertyListeners = new ArrayList<BeforeDeletePropertyListener>();
	protected List<AfterDeletePropertyListener> afterDeletePropertyListeners = new ArrayList<AfterDeletePropertyListener>();
	protected List<PassToNextPhaseListener> passToNextPhaseListeners = new ArrayList<PassToNextPhaseListener>();
	protected Appendix appendix = new Appendix();
	protected String actionMessage;
	private String password;
	protected FormulaManager formulaManager = new FormulaManager();
	private PluginDescriptor pluginDescriptor;
	
	
	public Personnage(){
		super();
	}
	
	public String nextPhase(){
		if(phaseList.indexOf(phase)+1<phaseList.size()){
			return phaseList.get(phaseList.indexOf(phase)+1);
		}
		return null;
	}
	
	public void passToNextPhase() throws Exception{
		if(!phaseFinished()){
			return;
		}
		if(this.nextPhase()!=null){
			callPassToNextPhaseListener();
			phase = this.nextPhase();
			calculate();
		}
	}
	public void calculate(){
		errors.clear();
		
		// Vérifie les pointPools
		for(PoolPoint poolPoint : pointPools.values()){
			if(poolPoint.isToEmpty()){
				if(poolPoint.getRemaining()>0){
					errors.add("Il reste des "+poolPoint.getName()+" à dépenser");
				}else if(poolPoint.getRemaining()<0){
					errors.add("Vous avez dépensez trop de "+poolPoint.getName());
				}
			}
		}
	}
	
	public boolean isInInterval(Property property, Value value){
		if(property == null){
			return false;
		}
		ValueComparator comparator = new ValueComparator();
		if(property.getMin()!=null){
			if(comparator.compare(value,property.getMin())>0){
				return false;
			}
		}
		if(property.getMax()!=null){
			if(comparator.compare(property.getMax(),value)>0){
				return false;
			}
		}
		return true;
	}
	
	public boolean phaseFinished(){
		calculate();
		if(errors.size()==0){
			return true;
		}
		return false;
	}
	
	
	public void addProperty(Property property){
		properties.put(property.getFullName(), property);
		property.setOwner(this);
	}
	
	public boolean addPropertyToMotherProperty(Property property) throws Exception{
		Property mother = (Property) property.getOwner();
		if(property.getFullName().contains("#")){
			actionMessage = "Le caractère \"#\" est interdit";
			return false;
		}
		if(mother.getSubProperty(property.getFullName())!=null){
			actionMessage = "Cette propriété existe déjà";
			return false;
		}
		if(!callBeforeAddPropertyListeners(property)){
			return false;
		}
		mother.getSubProperties().add(property);
		HistoryFactory historyFactory = property.getHistoryFactory();
		HistoryItem newHistoryItem = historyFactory.buildHistoryItem(property.getAbsoluteName(), null, property.getValue(), HistoryItem.CREATION, phase);
		history.add(newHistoryItem);
		if(pointPools.containsKey(newHistoryItem.getPointPool())){
			pointPools.get(newHistoryItem.getPointPool()).spend(newHistoryItem.getCost());
		}
		callAfterAddPropertyListeners(property);
		formulaManager.impactModificationFor(property.getAbsoluteName(), this);
		calculate();
		return true;
	}
	
	public boolean removePropertyFromMotherProperty(Property property) throws Exception{
		if(!property.isRemovable()){
			return false;
		}
		if(property.getOwner() instanceof Personnage){
			return false;
		}
		if(!((Property)property.getOwner()).getSubProperties().canRemoveElement()){
			return false;
		}
		if(!callBeforeDeletePropertyListener(property)){
			return false;
		}
		Property mother = (Property) property.getOwner();
		mother.getSubProperties().remove(property);
		HistoryFactory historyFactory = property.getHistoryFactory();
		HistoryItem newHistoryItem = historyFactory.buildHistoryItem(property.getAbsoluteName(), property.getValue(), null, HistoryItem.DELETION, phase);
		history.add(newHistoryItem);
		if(pointPools.containsKey(newHistoryItem.getPointPool())){
			pointPools.get(newHistoryItem.getPointPool()).spend(newHistoryItem.getCost());
		}
		callAfterDeletePropertyListener(property);
		formulaManager.impactModificationFor(property.getAbsoluteName(), this);
		calculate();
		return true;
	}
	
	public Property getProperty(String absoluteName){
		String[] nameTab = absoluteName.split("[#]");
		Property property = properties.get(nameTab[0]);
		for(int i=1; i<nameTab.length; i++){
			if(property==null){
				return null;
			}
			if(property.getSubProperties()==null){
				return null;
			}
			property = property.getSubProperty(nameTab[i]);
		}
		return property;
	}
	
	public int size(){
		return properties.size();
	}
	public Set<String> keySet(){
		return properties.keySet();
	}
	public List<String> getErrors() {
		return errors;
	}
	public String getPhase() {
		return phase;
	}
	public List<String> getPhaseList() {
		return phaseList;
	}
	public List<HistoryItem> getHistory() {
		return history;
	}
	public ListIterator<HistoryItem> getReversedIteratorHistory(){
		return new ReversedListIterator<HistoryItem>(history);
	}
	public HistoryFactory getHistoryFactory(){
		return historyFactory;
	}
	public Map<String, PoolPoint> getPointPools() {
		return pointPools;
	}
	public void putPoolPoint(String name, int total){
		pointPools.put(name, new PoolPoint(name,total));
	}
	public Collection<Property> getProperties(){
		return properties.values();
	}
	public Collection<String> getPropertyNames(){
		return properties.keySet();
	}
	public Appendix getAppendix(){
		return appendix;
	}
	public void setAppendix(Appendix appendix){
		this.appendix = appendix;
	}
	public InputStream getHelpFileInputStream(){
		if(hasHelpFile()){
			return getClass().getResourceAsStream(pluginDescriptor.getHelpFileName());
		}
		return null;
	}
	public boolean hasHelpFile(){
		return pluginDescriptor.getHelpFileName() != null;
	}
	public FormulaManager getFormulaManager(){
		return formulaManager;
	}
	public String getActionMessage() {
		return actionMessage;
	}
	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}
	public void clearActionMessage(){
		actionMessage = null;
	}
	public final String getPassword() {
		return password;
	}
	public final void setPassword(String password) {
		this.password = password;
	}
	
	
	

	public boolean setNewValue(Property property, Value newValue) throws Exception{
		if(!isInInterval(property, newValue)){
			return false;
		}
		if(!callBeforeChangeValueListeners(property, newValue)){
			return false;
		}
		HistoryFactory historyFactory = property.getHistoryFactory();
		HistoryItem newHistoryItem = historyFactory.buildHistoryItem(property.getAbsoluteName(), property.getValue(), newValue, HistoryItem.UPDATE, phase);
		
		history.add(newHistoryItem);
		if(pointPools.containsKey(newHistoryItem.getPointPool())){
			pointPools.get(newHistoryItem.getPointPool()).spend(newHistoryItem.getCost());
		}
		property.setValue(newValue);
		callAfterChangeValueListeners(property, newHistoryItem.getOldValue());
		formulaManager.impactModificationFor(property.getAbsoluteName(), this);
		calculate();
		return true;
	}
	
	private boolean callBeforeChangeValueListeners(Property property, Value newValue){
		for(BeforeChangeValueListener listener : beforeChangeValueListeners){
			if(listener.getPhases()==null || listener.getPhases().contains(phase)){
				if(Pattern.matches(listener.getPattern(), property.getAbsoluteName())){
					try {
						if(listener.callBeforeChangeValue(property, newValue)==false){
							return false;
						}
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void callAfterChangeValueListeners(Property property, Value oldValue) throws Exception{
		for(AfterChangeValueListener listener : afterChangeValueListeners){
			if(listener.getPhases()==null || listener.getPhases().contains(phase)){
				if(Pattern.matches(listener.getPattern(), property.getAbsoluteName())){
					listener.callAfterChangeValue(property, oldValue);
				}
			}
		}
	}
	
	private boolean callBeforeAddPropertyListeners(Property newProperty){
		for(BeforeAddPropertyListener listener : beforeAddPropertyListeners){
			if(listener.getPhases()==null || listener.getPhases().contains(phase)){
				if(Pattern.matches(listener.getPattern(), newProperty.getAbsoluteName())){
					try {
						if(listener.callBeforeAddProperty(newProperty)==false){
							return false;
						}
					} catch (Exception e) {
						e.printStackTrace();
						actionMessage = "Erreur technique: "+e.getMessage();
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void callAfterAddPropertyListeners(Property newProperty) throws Exception{
		for(AfterAddPropertyListener listener : afterAddPropertyListeners){
			if(listener.getPhases()==null || listener.getPhases().contains(phase)){
				if(Pattern.matches(listener.getPattern(), newProperty.getAbsoluteName())){
					listener.callAfterAddProperty(newProperty);
				}
			}
		}
	}
	
	private boolean callBeforeDeletePropertyListener(Property oldProperty){
		for(BeforeDeletePropertyListener listener : beforeDeletePropertyListeners){
			if(listener.getPhases()==null || listener.getPhases().contains(phase)){
				if(Pattern.matches(listener.getPattern(), oldProperty.getAbsoluteName())){
					try {
						if(listener.callBeforeDeleteProperty(oldProperty)==false){
							return false;
						}
					}catch(Exception e){
						e.printStackTrace();
						actionMessage = "Erreur technique: "+e.getMessage();
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void callAfterDeletePropertyListener(Property oldProperty) throws Exception{
		for(AfterDeletePropertyListener listener : afterDeletePropertyListeners){
			if(listener.getPhases()==null || listener.getPhases().contains(phase)){
				if(Pattern.matches(listener.getPattern(), oldProperty.getAbsoluteName())){
					listener.callAfterDeleteProperty(oldProperty);
				}
			}
		}
	}
	
	private void callPassToNextPhaseListener() throws Exception{
		for(PassToNextPhaseListener listener : passToNextPhaseListeners){
			if(listener.getPhase().equals(phase)){
				listener.callPassToNextPhase();
			}
		}
	}
	
	public void addBeforeChangeValueListener(BeforeChangeValueListener listener){
		beforeChangeValueListeners.add(listener);
		listener.setPersonnage(this);
	}
	public void addAfterChangeValueListener(AfterChangeValueListener listener){
		afterChangeValueListeners.add(listener);
		listener.setPersonnage(this);
	}
	public void addBeforeAddPropertyListener(BeforeAddPropertyListener listener){
		beforeAddPropertyListeners.add(listener);
		listener.setPersonnage(this);
	}
	public void addAfterAddPropertyListener(AfterAddPropertyListener listener){
		afterAddPropertyListeners.add(listener);
		listener.setPersonnage(this);
	}
	public void addBeforeDeletePropertyListener(BeforeDeletePropertyListener listener){
		beforeDeletePropertyListeners.add(listener);
		listener.setPersonnage(this);
	}
	public void addAfterDeletePropertyListener(AfterDeletePropertyListener listener){
		afterDeletePropertyListeners.add(listener);
		listener.setPersonnage(this);
	}
	public void addPassToNextPhaseListener(PassToNextPhaseListener listener){
		passToNextPhaseListeners.add(listener);
		listener.setPersonnage(this);
	}
	
	public List<BeforeChangeValueListener> getBeforeChangeValueListeners() {
		return beforeChangeValueListeners;
	}
	public List<AfterChangeValueListener> getAfterChangeValueListeners() {
		return afterChangeValueListeners;
	}
	public List<BeforeAddPropertyListener> getBeforeAddPropertyListeners() {
		return beforeAddPropertyListeners;
	}
	public List<AfterAddPropertyListener> getAfterAddPropertyListeners() {
		return afterAddPropertyListeners;
	}
	public List<BeforeDeletePropertyListener> getBeforeDeletePropertyListeners() {
		return beforeDeletePropertyListeners;
	}
	public List<AfterDeletePropertyListener> getAfterDeletePropertyListeners() {
		return afterDeletePropertyListeners;
	}
	public List<PassToNextPhaseListener> getPassToNextPhaseListeners() {
		return passToNextPhaseListeners;
	}
	
	public void setNewValue(Property property, String newValue) throws Exception{
		setNewValue(property, new StringValue(newValue));
	}
	public void setNewValue(Property property, int newValue) throws Exception{
		setNewValue(property, new IntValue(newValue));
	}
	public void setNewValue(String absoluteName, Value newValue) throws Exception{
		setNewValue(getProperty(absoluteName), newValue);
	}
	public void setNewValue(String absoluteName, String newValue) throws Exception{
		setNewValue(getProperty(absoluteName), newValue);
	}
	public void setNewValue(String absoluteName, int newValue) throws Exception{
		setNewValue(getProperty(absoluteName), newValue);
	}
	
	public Iterator<Property> iterator(){
		return properties.values().iterator();
	}
	
	
	public Document getXML(){
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("personnage");
		root.addAttribute("name", getPluginDescriptor().getName());
		root.addAttribute("version", getPluginDescriptor().getVersion().toString());
		root.addAttribute("gencrossVersion", gencrossVersion.toString());
		if(password!=null){
			root.addAttribute("password", password);
		}
		root.addElement("phase").addAttribute("name", phase);
		Element phaseListEl = root.addElement("phaseList");
		for(int i=0; i<phaseList.size();i++){
			Element phaseEl = phaseListEl.addElement("phase");
			phaseEl.addAttribute("name", phaseList.get(i));
		}
		Element pointPoolEl = root.addElement("pointPools");
		for(PoolPoint pool : pointPools.values()){
			pointPoolEl.add(pool.getXml());
		}
		Element propertiesEl = root.addElement("properties");
		for(Property property : properties.values()){
			propertiesEl.add(property.getXML());
		}
		Element beforeChangeValueListenersEl = root.addElement("beforeChangeValueListeners");
		for(BeforeChangeValueListener listener : beforeChangeValueListeners){
			beforeChangeValueListenersEl.add(listener.getXml());
		}
		Element afterChangeValueListenersEl = root.addElement("afterChangeValueListeners");
		for(AfterChangeValueListener listener : afterChangeValueListeners){
			afterChangeValueListenersEl.add(listener.getXml());
		}
		Element beforeAddPropertyListenersEl = root.addElement("beforeAddPropertyListeners");
		for(BeforeAddPropertyListener listener : beforeAddPropertyListeners){
			beforeAddPropertyListenersEl.add(listener.getXml());
		}
		Element afterAddPropertyListenersEl = root.addElement("afterAddPropertyListeners");
		for(AfterAddPropertyListener listener : afterAddPropertyListeners){
			afterAddPropertyListenersEl.add(listener.getXml());
		}
		Element beforeDeletePropertyListenersEl = root.addElement("beforeDeletePropertyListeners");
		for(BeforeDeletePropertyListener listener : beforeDeletePropertyListeners){
			beforeDeletePropertyListenersEl.add(listener.getXml());
		}
		Element afterDeletePropertyListenersEl = root.addElement("afterDeletePropertyListeners");
		for(AfterDeletePropertyListener listener : afterDeletePropertyListeners){
			afterDeletePropertyListenersEl.add(listener.getXml());
		}
		Element passToNextPhaseListenersEl = root.addElement("passToNextPhaseListeners");
		for(PassToNextPhaseListener listener : passToNextPhaseListeners){
			passToNextPhaseListenersEl.add(listener.getXml());
		}
		root.add(formulaManager.getXml());
		Element historyEl = root.addElement("history");
		for(HistoryItem historyItem : history){
			historyEl.add(historyItem.getXML());
		}
		return document;
	}
	
	public void setXML(Element root) throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedFormulaException, IllegalArgumentException, InvocationTargetException{
		setXMLWithoutListener(root);
		try {
			setXMLListeners(root);
		} catch (Exception e) {
			InstantiationException ie = new InstantiationException();
			ie.initCause(e);
			throw ie;
		}
		calculate();
	}
	
	
	public void setXMLWithoutListener(Element root) throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedFormulaException, IllegalArgumentException, InvocationTargetException{
		phase = root.element("phase").attributeValue("name");
		password = root.attribute("password")!=null?root.attributeValue("password"):null;
		Iterator<?> it = root.element("phaseList").elementIterator("phase");
		phaseList.clear();
		while(it.hasNext()){
			Element phaseEl = (Element) it.next();
			phaseList.add(phaseEl.attributeValue("name"));
		}
		pointPools.clear();
		it = root.element("pointPools").elementIterator("pool");
		while(it.hasNext()){
			PoolPoint poolPoint = new PoolPoint((Element) it.next());
			pointPools.put(poolPoint.getName(), poolPoint);
		}
		properties.clear();
		it = root.element("properties").elementIterator("property");
		while(it.hasNext()){
			addProperty(new Property((Element)it.next(), this));
		}
		if(root.element("formulas")!=null){
			formulaManager.setXml(root.element("formulas"));
		}
		history.clear();
		it = root.element("history").elementIterator("historyItem");
		while(it.hasNext()){
			Element historyItemEl = (Element)it.next();
			history.add(new HistoryItem(historyItemEl));
		}
		calculate();
	}
	
	public void setXMLListeners(Element root) throws Exception{
		beforeChangeValueListeners.clear();
		Iterator<?> it = root.element("beforeChangeValueListeners").elementIterator("listener");
		while(it.hasNext()){
			Element listenerEl = (Element)it.next();
			beforeChangeValueListeners.add((BeforeChangeValueListener) PropertyListener.buildListener(listenerEl, this));
		}
		afterChangeValueListeners.clear();
		it = root.element("afterChangeValueListeners").elementIterator("listener");
		while(it.hasNext()){
			Element listenerEl = (Element)it.next();
			afterChangeValueListeners.add((AfterChangeValueListener) PropertyListener.buildListener(listenerEl, this));
		}
		beforeAddPropertyListeners.clear();
		it = root.element("beforeAddPropertyListeners").elementIterator("listener");
		while(it.hasNext()){
			Element listenerEl = (Element)it.next();
			beforeAddPropertyListeners.add((BeforeAddPropertyListener) PropertyListener.buildListener(listenerEl, this));
		}
		afterAddPropertyListeners.clear();
		it = root.element("afterAddPropertyListeners").elementIterator("listener");
		while(it.hasNext()){
			Element listenerEl = (Element)it.next();
			afterAddPropertyListeners.add((AfterAddPropertyListener) PropertyListener.buildListener(listenerEl, this));
		}
		beforeDeletePropertyListeners.clear();
		it = root.element("beforeDeletePropertyListeners").elementIterator("listener");
		while(it.hasNext()){
			Element listenerEl = (Element)it.next();
			beforeDeletePropertyListeners.add((BeforeDeletePropertyListener) PropertyListener.buildListener(listenerEl, this));
		}
		afterDeletePropertyListeners.clear();
		it = root.element("afterDeletePropertyListeners").elementIterator("listener");
		while(it.hasNext()){
			Element listenerEl = (Element)it.next();
			afterDeletePropertyListeners.add((AfterDeletePropertyListener) PropertyListener.buildListener(listenerEl, this));
		}
		passToNextPhaseListeners.clear();
		if(root.element("passToNextPhaseListeners")!=null){
			it = root.element("passToNextPhaseListeners").elementIterator("listener");
			while(it.hasNext()){
				Element listenerEl = (Element)it.next();
				passToNextPhaseListeners.add(PassToNextPhaseListener.buildListener(listenerEl, this));
			}
		}
	}
	
	
	public Personnage clone() throws CloneNotSupportedException{
		try {
			Personnage clone = this.getClass().newInstance();
			clone.pluginDescriptor = pluginDescriptor;
			for(Property property : properties.values()){
				clone.addProperty(property.clone());
			}
			for(String key : pointPools.keySet()){
				clone.pointPools.put(""+key, pointPools.get(key).clone());
			}
			for(HistoryItem historyItem : history){
				clone.history.add(historyItem.clone());
			}
			for(AfterAddPropertyListener listener : afterAddPropertyListeners){
				clone.addAfterAddPropertyListener((AfterAddPropertyListener) listener.clone());
			}
			for(BeforeAddPropertyListener listener : beforeAddPropertyListeners){
				clone.addBeforeAddPropertyListener((BeforeAddPropertyListener) listener.clone());
			}
			for(AfterChangeValueListener listener : afterChangeValueListeners){
				clone.addAfterChangeValueListener((AfterChangeValueListener) listener.clone());
			}
			for(BeforeChangeValueListener listener : beforeChangeValueListeners){
				clone.addBeforeChangeValueListener((BeforeChangeValueListener) listener.clone());
			}
			for(AfterDeletePropertyListener listener : afterDeletePropertyListeners){
				clone.addAfterDeletePropertyListener((AfterDeletePropertyListener) listener.clone());
			}
			for(BeforeDeletePropertyListener listener : beforeDeletePropertyListeners){
				clone.addBeforeDeletePropertyListener((BeforeDeletePropertyListener) listener.clone());
			}
			for(PassToNextPhaseListener listener : passToNextPhaseListeners){
				clone.addPassToNextPhaseListener((PassToNextPhaseListener) listener.clone());
			}
			clone.errors.clear();
			for(String error : errors){
				clone.errors.add(new String(error));
			}
			clone.phaseList.clear();
			for(String phase : phaseList){
				clone.phaseList.add(new String(phase));
			}
			clone.historyFactory = historyFactory.clone();
			clone.phase = new String(phase);
			clone.password = password;
			clone.appendix = appendix;
			clone.formulaManager = formulaManager.clone();
			
			return clone;
		} catch (InstantiationException e) {
			CloneNotSupportedException newEx = new CloneNotSupportedException();
			newEx.initCause(e);
			throw newEx;
		} catch (IllegalAccessException e) {
			CloneNotSupportedException newEx = new CloneNotSupportedException();
			newEx.initCause(e);
			throw newEx;
		}
	}
	
	/**
	 * Cette methode permet d'obtenir une instance de renderer à partir de son nom de classe.
	 * Sa principale utilité est de remonter la résolution des renderers dans le personnage plutot que dans les Property.
	 * @param name
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public Renderer getRenderer(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (Renderer) getClass().getClassLoader().loadClass(className).newInstance();
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends HistoryFactory> getHistoryFactoryClass(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (Class<? extends HistoryFactory>) getClass().getClassLoader().loadClass(className);
	}
	
	public PluginDescriptor getPluginDescriptor(){
		return pluginDescriptor;
	}
	public void setPluginDescriptor(PluginDescriptor pluginDescriptor){
		this.pluginDescriptor = pluginDescriptor;
	}

}
