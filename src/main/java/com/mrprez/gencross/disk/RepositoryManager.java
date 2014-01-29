package com.mrprez.gencross.disk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

public class RepositoryManager {
	private File repository;
	private RepositoryClassLoader repositoryClassLoader;
	
	
	public RepositoryManager(File repository) throws IOException{
		super();
		if(repository.isDirectory()){
			this.repository = repository;
		}else{
			this.repository = repository.getParentFile();
		}
		repositoryClassLoader = new RepositoryClassLoader(repository);
	}
	
	public String archivePlugin(String name) throws IOException, URISyntaxException {
		Enumeration<URL> urls = repositoryClassLoader.getResources(PluginDescriptor.PLUGIN_DESC_FILE_NAME);
		while(urls.hasMoreElements()){
			URL url = urls.nextElement();
			PluginDescriptor pd = new PluginDescriptor(url.openStream());
			if(pd.getName().equals(name)){
				String archiveName = name+"_"+new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
				File archiveDir = new File(repository, archiveName);
				archiveDir.mkdir();
				String oldJarPath = url.getPath().substring(0, url.getPath().indexOf("!"));
				File oldJarFile = new File(oldJarPath);
				oldJarFile.renameTo(new File(archiveDir, oldJarFile.getName()));
				return archiveName;
			}
		}
		return null;
	}
	
	public void addPlugin(File pluginFile) throws IOException {
		File destFile = new File(repository, pluginFile.getName());
		InputStream is = new BufferedInputStream(new FileInputStream(pluginFile));
		try{
			OutputStream os = new BufferedOutputStream(new FileOutputStream(destFile));
			try{
				int b;
				while((b=is.read())>=0){
					os.write(b);
				}
			}finally{
				os.close();
			}
		}finally{
			is.close();
		}
	}
	
	public File getRepository(){
		return repository;
	}

	public RepositoryClassLoader getRepositoryClassLoader() {
		return repositoryClassLoader;
	}
	

}
