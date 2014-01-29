package com.mrprez.gencross.disk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import com.mrprez.gencross.Version;

public class PluginDescriptor {
	public final static String PLUGIN_DESC_FILE_NAME = "genCrossPlugin.xml";
	
	private final static String PREFIX = "genCross.plugin.";
	private final static String NAME = PREFIX+"name";
	private final static String CLASS_NAME = PREFIX+"className";
	private final static String GCR_FILE_NAME = PREFIX+"gcrFileName";
	private final static String APPENDIX_FILE_NAME = PREFIX+"appendixFileName";
	private final static String VERSION = PREFIX+"version";
	private final static String HELP_FILE_NAME = PREFIX+"helpFileName";
	private final static String HELP_FILE_SIZE = PREFIX+"helpFileSize";
	private final static String MIGRATION_PREFIX = PREFIX+"migration.";
	
	private String name;
	private String className;
	private String gcrFileName;
	private String appendixFileName;
	private Version version;
	private String helpFileName;
	private Long helpFileSize;
	private Map<String, String> migrators = new HashMap<String, String>();
	
	
	public PluginDescriptor() {
		super();
	}
	
	public PluginDescriptor(InputStream is) throws InvalidPropertiesFormatException, IOException{
		super();
		load(is);
	}
	
	public PluginDescriptor(File file) throws InvalidPropertiesFormatException, FileNotFoundException, IOException{
		super();
		FileInputStream fis = new FileInputStream(file);
		try{
			load(fis);
		}finally{
			fis.close();
		}
	}
	
	public PluginDescriptor(Properties properties){
		super();
		load(properties);
	}
	
	private void load(InputStream is) throws InvalidPropertiesFormatException, IOException{
		Properties properties = new Properties();
		properties.loadFromXML(is);
		load(properties);
	}
	
	private void load(Properties properties) {
		name = properties.getProperty(NAME);
		className = properties.getProperty(CLASS_NAME);
		gcrFileName = properties.getProperty(GCR_FILE_NAME);
		appendixFileName = properties.getProperty(APPENDIX_FILE_NAME);
		version = properties.containsKey(VERSION) ? new Version(properties.getProperty(VERSION)) : Version.DEFAULT_VERSION;
		helpFileName = properties.getProperty(HELP_FILE_NAME);
		if(properties.containsKey(HELP_FILE_SIZE)){
			helpFileSize = Long.valueOf(properties.getProperty(HELP_FILE_SIZE));
		}
		for(String key : properties.stringPropertyNames()){
			if(key.startsWith(MIGRATION_PREFIX)){
				migrators.put(key.substring(MIGRATION_PREFIX.length()), properties.getProperty(key));
			}
		}
	}
	
	public void save(OutputStream os) throws IOException{
		Properties properties = new Properties();
		properties.put(NAME, name);
		if(className != null){
			properties.put(CLASS_NAME, className);
		}
		properties.put(GCR_FILE_NAME, gcrFileName);
		if(appendixFileName != null){
			properties.put(APPENDIX_FILE_NAME, appendixFileName);
		}
		properties.put(VERSION, version.toString());
		if(helpFileName != null){
			properties.put(HELP_FILE_NAME, helpFileName);
			properties.put(HELP_FILE_SIZE, helpFileSize.toString());
		}
		properties.store(os, "");
	}
	
	public String getName() {
		return name;
	}

	public String getClassName() {
		return className;
	}

	public String getGcrFileName() {
		return gcrFileName;
	}

	public String getAppendixFileName() {
		return appendixFileName;
	}

	public Version getVersion() {
		return version;
	}

	public String getHelpFileName() {
		return helpFileName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setGcrFileName(String gcrFileName) {
		this.gcrFileName = gcrFileName;
	}

	public void setAppendixFileName(String appendixFileName) {
		this.appendixFileName = appendixFileName;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public void setHelpFileName(String helpFileName) {
		this.helpFileName = helpFileName;
	}
	
	public Long getHelpFileSize() {
		return helpFileSize;
	}

	public void setHelpFileSize(Long helpFileSize) {
		this.helpFileSize = helpFileSize;
	}
	
	public String getMigratorClassName(Version version){
		return migrators.get(version.toString());
	}

	@Override
	public String toString(){
		return name;
	}
	
	public static class PluginComparator implements Comparator<PluginDescriptor> {
		@Override
		public int compare(PluginDescriptor pd1, PluginDescriptor pd2) {
			if(pd1.getName()==null && pd2.getName()!=null){
				return -1;
			}
			if(pd1.getName()!=null && pd2.getName()==null){
				return 1;
			}
			if(pd1.getName()!=null && pd2.getName()!=null){
				if(!pd1.getName().equals(pd2.getName())){
					return pd1.getName().compareTo(pd2.getName());
				}
			}
			if(pd1.getVersion()==null && pd2.getVersion()!=null){
				return -1;
			}
			if(pd1.getVersion()!=null && pd2.getVersion()==null){
				return 1;
			}
			if(pd1.getVersion()!=null && pd2.getVersion()!=null){
				return pd1.getVersion().compareTo(pd2.getVersion());
			}
			return 0;
		}
	}
	
	public static PluginDescriptor getDefaultPluginDescriptor(){
		PluginDescriptor defaultPluginDescriptor = new PluginDescriptor();
		defaultPluginDescriptor.setName("default");
		defaultPluginDescriptor.setClassName("com.mrprez.gencross.Personnage");
		return defaultPluginDescriptor;
	}

}
