package com.mrprez.gencross.test.util;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.disk.PersonnageFactory;

public class PersonnageBuilder {
	private Personnage personnage;

	public PersonnageBuilder(Personnage personnage) {
		super();
		this.personnage = personnage;
	}


	public static PersonnageBuilder newPersonnage(String pluginName) throws Exception {
		PersonnageFactory personnageFactory = new PersonnageFactory();
		return new PersonnageBuilder(personnageFactory.buildNewPersonnage(pluginName));
	}

	public PersonnageBuilder withValue(String propertyName, int value) throws Exception {
		personnage.setNewValue(propertyName, value);
		return this;
	}

	public PersonnageBuilder withValue(String propertyName, String value) throws Exception {
		personnage.setNewValue(propertyName, value);
		return this;
	}

	public PersonnageBuilder withNewProperty(String propertyName) throws Exception {
		String motherName = propertyName.substring(0, propertyName.lastIndexOf('#'));
		String newName = propertyName.substring(propertyName.lastIndexOf('#') + 1);
		Property motherProperty = personnage.getProperty(motherName);
		Property newProperty;
		if (motherProperty.getSubProperties().getOptions() != null && motherProperty.getSubProperties().getOptions().containsKey(newName)) {
			newProperty = motherProperty.getSubProperties().getOptions().get(newName);
		} else {
			newProperty = motherProperty.getSubProperties().getDefaultProperty().clone();
			newProperty.setName(newName);
		}
		personnage.addPropertyToMotherProperty(newProperty);
		return this;
	}

	public PersonnageBuilder withoutProperty(String propertyName) throws Exception {
		personnage.removePropertyFromMotherProperty(personnage.getProperty(propertyName));
		return this;
	}

	public PersonnageBuilder withNextPhase() {
		personnage.nextPhase();
		return this;
	}

	public Personnage get() {
		return personnage;
	}


}
