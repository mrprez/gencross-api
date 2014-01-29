package com.mrprez.gencross.disk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.NoSuchPaddingException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.mrprez.gencross.Appendix;
import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Version;
import com.mrprez.gencross.exception.PersonnageVersionException;
import com.mrprez.gencross.migration.MigrationPersonnage;
import com.mrprez.gencross.migration.Migrator;

public class PersonnageFactory {
	private ClassLoader classLoader;
	private boolean acceptMigration = false;
	private Map<String, PluginDescriptor> pluginDescriptors = new TreeMap<String, PluginDescriptor>();
	
	
	public PersonnageFactory() throws IOException{
		super();
		classLoader = getClass().getClassLoader();
		loadPlugins();
	}
	
	public PersonnageFactory(boolean acceptMigration) throws IOException{
		this.acceptMigration = acceptMigration;
		classLoader = getClass().getClassLoader();
		loadPlugins();
	}
	
	public PersonnageFactory(ClassLoader classLoader) throws IOException {
		super();
		this.classLoader = classLoader;
		loadPlugins();
	}
	
	public PersonnageFactory(ClassLoader classLoader, boolean acceptMigration) throws IOException {
		super();
		this.classLoader = classLoader;
		this.acceptMigration = acceptMigration;
		loadPlugins();
	}
	
	private void loadPlugins() throws IOException{
		Enumeration<URL> urls = classLoader.getResources(PluginDescriptor.PLUGIN_DESC_FILE_NAME);
		while(urls.hasMoreElements()){
			PluginDescriptor pd = new PluginDescriptor(urls.nextElement().openStream());
			pluginDescriptors.put(pd.getName(), pd);
		}
	}
	
	public Personnage buildNewPersonnage(String pluginName) throws Exception{
		return buildNewPersonnage(getPluginDescriptor(pluginName));
	}
	
	public Personnage buildNewPersonnage(PluginDescriptor pluginDescriptor) throws Exception {
		InputStream encryptedIS = classLoader.getResourceAsStream(pluginDescriptor.getGcrFileName());
		Document document = loadXml(DecryptInputStream.buildDecryptInputStream(encryptedIS));
		Element root = document.getRootElement();
		
		// Virgin personnage
		Personnage personnage = (Personnage) classLoader.loadClass(pluginDescriptor.getClassName()).newInstance();
		personnage.setPluginDescriptor(pluginDescriptor);
		
		// Appendix
		if(pluginDescriptor.getAppendixFileName() != null){
			Appendix appendix = new Appendix(classLoader.getResourceAsStream(pluginDescriptor.getAppendixFileName()));
			personnage.setAppendix(appendix);
		}
		
		personnage.setXML(root);
		
		return personnage;
	}
	
	public Personnage loadPersonnage(InputStream is) throws Exception {
		Document document = loadXml(is);
		return loadPersonnage(document);
	}
	
	public Personnage loadPersonnage(Document document) throws Exception {
		Element root = document.getRootElement();
		Version gencrossVersion = new Version(1,8);
		if(root.attribute("gencrossVersion") != null){
			gencrossVersion = new Version(root.attributeValue("gencrossVersion"));
		}
		if( ! gencrossVersion.equals(Personnage.gencrossVersion)) {
			if(!acceptMigration){
				throw new PersonnageVersionException("Version de l'API gencross antérieur à 1.10");
			}
			if(gencrossVersion.compareTo(new Version(1,9)) < 0){
				XmlMigration.treatXml1_8(document.getRootElement());
			}
			if(gencrossVersion.compareTo(new Version(1,10)) < 0){
				XmlMigration.treatXml1_9(document.getRootElement());
			}
			if(gencrossVersion.compareTo(new Version(1,11)) < 0){
				XmlMigration.treatXml1_10(document.getRootElement());
			}
		}
		
		// PluginDescriptor
		PluginDescriptor pluginDescriptor;
		if(root.attribute("name") != null) {
			pluginDescriptor = pluginDescriptors.get(root.attributeValue("name"));
			if(pluginDescriptor == null){
				throw new PersonnageVersionException("Cannot find plugin descriptor "+root.attributeValue("name"));
			}
		}else if(root.attribute("class") != null){
			pluginDescriptor = findPluginDescriptorByClass(root.attributeValue("class"));
			if(pluginDescriptor == null){
				throw new PersonnageVersionException("Cannot find plugin descriptor for class "+root.attributeValue("class"));
			}
		}else{
			pluginDescriptor = PluginDescriptor.getDefaultPluginDescriptor();
		}
		
		// Virgin personnage
		Personnage personnage = (Personnage) classLoader.loadClass(pluginDescriptor.getClassName()).newInstance();
		personnage.setPluginDescriptor(pluginDescriptor);
		
		// Appendix
		if(pluginDescriptor.getAppendixFileName() != null){
			Appendix appendix = new Appendix(classLoader.getResourceAsStream(pluginDescriptor.getAppendixFileName()));
			personnage.setAppendix(appendix);
		}
		
		// Version
		Version version = root.attribute("version")!=null?new Version(root.attribute("version")):Version.DEFAULT_VERSION;
		if(version.compareTo(pluginDescriptor.getVersion())>0){
			throw new PersonnageVersionException(version, pluginDescriptor.getVersion(),"Votre version du plugin personnage ("+pluginDescriptor.getName()+") : "+pluginDescriptor.getVersion()+" n'est pas ï¿½ jour, la version "+version+" est requise");
		}else if(version.compareTo(pluginDescriptor.getVersion())<0){
			if(!acceptMigration){
				throw new PersonnageVersionException(version, pluginDescriptor.getVersion(), "Votre personnage est dans une version ("+version+") plus vieille que votre plugin :"+pluginDescriptor.getVersion());
			}else{
				personnage = migrate(document, pluginDescriptor);
			}
		}else{
			personnage.setXML(root);
		}
		return personnage;
	}
	
	public Personnage loadPersonnageFromXml(File xmlFile) throws Exception{
		return loadPersonnage(new FileInputStream(xmlFile));
	}
	
	public Personnage loadPersonnageFromGcr(File gcrFile) throws Exception{
		InputStream is = DecryptInputStream.buildDecryptInputStream(new FileInputStream(gcrFile));
    	return loadPersonnage(is);
	}
	
	public static Document loadGcr(InputStream inputStream) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IOException, DocumentException{
		return loadXml(DecryptInputStream.buildDecryptInputStream(inputStream));
	}
	
	public static Document loadXml(InputStream inputStream) throws IOException, DocumentException{
		InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
		StringBuilder buffer = new StringBuilder();
		try{
			int i;
			while((i=reader.read())>=0){
				buffer.append((char)i);
			}
		}finally{
			inputStream.close();
		}
		Document document = DocumentHelper.parseText(buffer.toString());
		return document;
	}
	
	private Personnage migrate(Document document, PluginDescriptor targetPluginDescriptor) throws Exception{
		// PluginDescriptor avec la version du personnage obsolète
		PluginDescriptor originPluginDescriptor = new PluginDescriptor();
		originPluginDescriptor.setName(targetPluginDescriptor.getName());
		Element root = document.getRootElement();
		Version version = root.attribute("version")!=null?new Version(root.attribute("version")):Version.DEFAULT_VERSION;
		originPluginDescriptor.setVersion(version);
		
		// MigrationPersonnage : personnage avec des dummyListeners. C'est lui qui va être migré.
		MigrationPersonnage migrationPersonnage = new MigrationPersonnage(document.getRootElement(), originPluginDescriptor);
		
		// Migration
		while(! migrationPersonnage.getPluginDescriptor().getVersion().equals(targetPluginDescriptor.getVersion())){
			Version originVersion = migrationPersonnage.getPluginDescriptor().getVersion();
			String migratorClassName = targetPluginDescriptor.getMigratorClassName(originVersion);
			if(migratorClassName==null){
				throw new PersonnageVersionException("No migrator for plugin "+targetPluginDescriptor.getName()+" v"+originVersion);
			}
			Migrator migrator = (Migrator) classLoader.loadClass(migratorClassName).newInstance();
			migrationPersonnage = migrator.migrate(migrationPersonnage);
			if(migrationPersonnage.getPluginDescriptor().getVersion().equals(originVersion)){
				throw new PersonnageVersionException(migratorClassName + " didn't work. Version is still "+originVersion);
			}
		}
		
		return loadPersonnage(migrationPersonnage.getXML());
	}

	public boolean isAcceptMigration() {
		return acceptMigration;
	}
	public void setAcceptMigration(boolean acceptMigration) {
		this.acceptMigration = acceptMigration;
	}
	
	public PluginDescriptor findPluginDescriptorByClass(String className){
		for(PluginDescriptor pd : pluginDescriptors.values()){
			if(pd.getClassName() != null && pd.getClassName().equals(className)){
				return pd;
			}
		}
		String shortClassName = className.substring(className.lastIndexOf("."));
		for(PluginDescriptor pd : pluginDescriptors.values()){
			if(pd.getClassName() != null){
				String shortDescClassName = pd.getClassName().substring(pd.getClassName().lastIndexOf("."));
				if(shortDescClassName.equals(shortClassName)){
					return pd;
				}
			}
		}
		return null;
	}
	
	public PluginDescriptor getPluginDescriptor(String name){
		return pluginDescriptors.get(name);
	}
	
	public Collection<PluginDescriptor> getPluginList(){
		return pluginDescriptors.values();
	}
	
}
