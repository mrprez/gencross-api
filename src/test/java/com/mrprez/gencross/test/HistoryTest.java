package com.mrprez.gencross.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PersonnageSaver;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.history.HistoryItem;

public abstract class HistoryTest {
	private String fileName;
	private Personnage personnageRef;
	private Personnage personnage;
	private List<HistoryItem> history;
	private PersonnageFactory personnageFactory;
	
	
	public HistoryTest(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	@Before
	public void setUp() throws Exception{
		this.personnageFactory = new PersonnageFactory(true);
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
		personnageRef = personnageFactory.loadPersonnage(is);
	}
	
	
	@Test
	public void test() throws Exception {
		PluginDescriptor pluginDescriptor = personnageRef.getPluginDescriptor();
		personnage = personnageFactory.buildNewPersonnage(pluginDescriptor);
		history = personnageRef.getHistory();
		
		for(HistoryItem historyItem : history) {
			reachPhase(historyItem.getPhase());
			if(historyItem.getAction()==HistoryItem.CREATION){
				creation(historyItem);
			} else if(historyItem.getAction()==HistoryItem.UPDATE){
				update(historyItem);
			} else if(historyItem.getAction()==HistoryItem.DELETION){
				delete(historyItem);
			}
		}
		
		reachPhase(personnageRef.getPhase());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteArrayOutputStream baosRef = new ByteArrayOutputStream();
		
		PersonnageSaver.savePersonnage(personnage, baos);
		PersonnageSaver.savePersonnage(personnageRef, baosRef);
		
		String xml = new String(baos.toByteArray(), "UTF-8");
		String xmlRef = new String(baosRef.toByteArray(), "UTF-8");
		xml = xml.replaceAll("<date>[0-9]*</date>", "<date>0000000000000</date>");
		xmlRef = xmlRef.replaceAll("<date>[0-9]*</date>", "<date>0000000000000</date>");
		
		assertEquals(xmlRef, xml);
		
	}
	
	private void reachPhase(String phase) throws Exception{
		while(! personnage.getPhase().equals(phase)){
			String oldPhase = personnage.getPhase();
			if(!personnage.phaseFinished()){
				Assert.fail("Cannot pass to phase: "+personnage.nextPhase()+", phase "+oldPhase+" is not finished");
			}
			personnage.passToNextPhase();
			System.out.println("Pass to phase: "+personnage.getPhase());
			if(personnage.getPhase().equals(oldPhase)){
				Assert.fail("Pass to next phase from "+oldPhase+" failed");
			}
		}
	}
	
	private void creation(HistoryItem historyItem) throws Exception{
		String motherPropertyAbsoluteName = historyItem.getAbsoluteName().substring(0, historyItem.getAbsoluteName().lastIndexOf("#"));
		Property motherProperty = personnage.getProperty(motherPropertyAbsoluteName);
		String propertyName = historyItem.getAbsoluteName().substring(historyItem.getAbsoluteName().lastIndexOf("#")+1);
		String specification = null;
		if(propertyName.contains(Property.SPECIFICATION_SEPARATOR)){
			specification = propertyName.substring(propertyName.indexOf(Property.SPECIFICATION_SEPARATOR) + 3);
			propertyName = propertyName.substring(0, propertyName.indexOf(Property.SPECIFICATION_SEPARATOR));
		}
		if(motherProperty.getSubProperties().isFixe()){
			fail("Mother property "+motherProperty.getAbsoluteName()+" is fixe");
		}
		String propertyKey = specification!=null ? propertyName+Property.SPECIFICATION_SEPARATOR : propertyName;
		if(motherProperty.getSubProperties().getOptions() != null && motherProperty.getSubProperties().getOptions().containsKey(propertyKey)){
			Property newProperty = motherProperty.getSubProperties().getOptions().get(propertyKey).clone();
			if(newProperty.getSpecification() != null){
				newProperty.setSpecification(specification);
			}
			boolean success = personnage.addPropertyToMotherProperty(newProperty);
			assertTrue("Cannot add property "+historyItem.getAbsoluteName()+" "+personnage.getActionMessage(), success);
		} else if(motherProperty.getSubProperties().isOpen()){
			Property newProperty = motherProperty.getSubProperties().getDefaultProperty().clone();
			newProperty.setName(propertyName);
			newProperty.setSpecification(specification);
			boolean success = personnage.addPropertyToMotherProperty(newProperty);
			assertTrue("Cannot add property "+historyItem.getAbsoluteName()+" "+personnage.getActionMessage(), success);
		} else {
			fail("Cannot add property "+historyItem.getAbsoluteName()+", property list is not opened");
		}
		HistoryItem newHistoryItem = personnage.getHistory().get(personnage.getHistory().size()-1);
		System.out.println("Add    property "+historyItem.getAbsoluteName()+" : "+historyItem.getNewValue()+" ("+newHistoryItem.getCost()+" "+newHistoryItem.getPointPool()+")");
	}
	
	private void update(HistoryItem historyItem) throws Exception{
		Property property = personnage.getProperty(historyItem.getAbsoluteName());
		assertEquals("Property "+property.getAbsoluteName()+" has the wrong value",historyItem.getOldValue(), property.getValue());
		boolean success = personnage.setNewValue(property, historyItem.getNewValue().clone());
		assertTrue("Cannot update property "+historyItem.getAbsoluteName()+" "+personnage.getActionMessage(), success);
		HistoryItem newHistoryItem = personnage.getHistory().get(personnage.getHistory().size()-1);
		System.out.println("Update property "+historyItem.getAbsoluteName()+" : "+historyItem.getNewValue()+" ("+newHistoryItem.getCost()+" "+newHistoryItem.getPointPool()+")");
	}
	
	private void delete(HistoryItem historyItem) throws Exception{
		Property property = personnage.getProperty(historyItem.getAbsoluteName());
		assertEquals("Property "+property.getAbsoluteName()+" has the wrong value",historyItem.getOldValue(), property.getValue());
		boolean success = personnage.removePropertyFromMotherProperty(property);
		assertTrue("Cannot remove property "+historyItem.getAbsoluteName()+" "+personnage.getActionMessage(), success);
		HistoryItem newHistoryItem = personnage.getHistory().get(personnage.getHistory().size()-1);
		System.out.println("Remove property "+historyItem.getAbsoluteName()+" : "+historyItem.getOldValue()+" ("+newHistoryItem.getCost()+" "+newHistoryItem.getPointPool()+")");
	}

}
