package com.mrprez.gencross.migration;


/**
 * Classe responsable de la migration d'un personnage d'un version antérieur vers une version plus nouvelle.
 * Lors de la migration d'un personnage, plusieurs Migrator pourront être appelé afin d'obtenir la version voulue.
 *
 */
public interface Migrator {
	
	/**
	 * Part d'un MigrationPersonnage, c'est à dire le personnage avec des "dummy" listeners à la place des listeners, pour construire le personnage migré.
	 * @param migrationPersonnage
	 * @return le personnage migré. Celui-ci doit contenir le PluginDescriptor avec la version du personnage migré.
	 * @throws Exception
	 */
	public MigrationPersonnage migrate(MigrationPersonnage migrationPersonnage) throws Exception;
	

}
