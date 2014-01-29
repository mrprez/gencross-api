package com.mrprez.gencross.test.migration;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Version;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.migration.MigrationPersonnage;
import com.mrprez.gencross.migration.Migrator;

public class MigratorTest implements Migrator {

	@Override
	public MigrationPersonnage migrate(MigrationPersonnage migrationPersonnage) throws Exception {
		PluginDescriptor pluginDescriptor = migrationPersonnage.getPluginDescriptor();
		pluginDescriptor.setVersion(new Version(1,0));
		
		return new MigrationPersonnage(new Personnage().getXML().getRootElement(), pluginDescriptor);
	}

}
