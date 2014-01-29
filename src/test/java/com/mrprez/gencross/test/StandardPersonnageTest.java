package com.mrprez.gencross.test;

import org.junit.Assert;
import org.junit.Test;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PersonnageFactory;

public abstract class StandardPersonnageTest {
	private String pluginName;
	
	
	public StandardPersonnageTest(String pluginName){
		super();
		this.pluginName = pluginName;
	}
	
	@Test
	public void test() throws Exception{
		PersonnageFactory personnageFactory = new PersonnageFactory();
		Personnage personnage = personnageFactory.buildNewPersonnage(pluginName);
		
		Assert.assertFalse(personnage.getProperties().isEmpty());
		
		
	}

}
