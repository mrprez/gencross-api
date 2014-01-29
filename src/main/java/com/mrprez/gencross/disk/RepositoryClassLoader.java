package com.mrprez.gencross.disk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class RepositoryClassLoader extends ClassLoader {
	private File repository;
	private Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
	private Map<String, Set<URL>> ressources = new HashMap<String, Set<URL>>();
	
	private FilenameFilter jarNameFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".jar");
		}
	};
	
	
	public RepositoryClassLoader(File repository) throws IOException {
		super();
		this.repository = repository;
		parseRepository();
	}
	
	public synchronized void reinit() throws IOException{
		classes.clear();
		ressources.clear();
		parseRepository();
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if(classes.containsKey(name)){
			return classes.get(name);
		}
		return super.findClass(name);
	}

	@Override
	protected URL findResource(String name) {
		name = name.replace('\\', File.separatorChar).replace('/', File.separatorChar);
		if(ressources.containsKey(name)){
			Set<URL> urls = ressources.get(name);
			return urls.iterator().next();
		}
		return super.findResource(name);
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		name = name.replace('\\', File.separatorChar).replace('/', File.separatorChar);
		if(ressources.containsKey(name)){
			return Collections.enumeration(ressources.get(name));
		}
		return super.findResources(name);
	}
	
	private synchronized void parseRepository() throws IOException{
		File jarTab[] = repository.listFiles(jarNameFilter);
		for(int i=0; i<jarTab.length; i++){
			JarInputStream jis = new JarInputStream(new FileInputStream(jarTab[i]));
			try{
				JarEntry jarEntry;
				while((jarEntry=jis.getNextJarEntry()) != null){
					if(!jarEntry.isDirectory()){
						File file = new File(jarEntry.getName());
						if(file.getName().endsWith(".class")){
							try{
								Class<?> clazz = loadClass(jis);
								classes.put(clazz.getName(), clazz);
							}catch(LinkageError e){
								System.err.println("WARNING: "+e.getMessage());
								e.printStackTrace();
							}
						}else{
							String name = file.getPath().replace('\\', File.separatorChar).replace('/', File.separatorChar);
							if(!ressources.containsKey(name)){
								ressources.put(name, new HashSet<URL>());
							}
							ressources.get(name).add(new URL("jar:"+jarTab[i].toURI().toURL()+"!/"+jarEntry.getName()));
						}
					}
				}
			}finally{
				jis.close();
			}
		}
	}
	
	private Class<?> loadClass(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try{
			int i;
			while((i=is.read())>=0){
				baos.write(i);
			}
			return super.defineClass(null, baos.toByteArray(), 0, baos.toByteArray().length);
		}finally{
			baos.close();
		}
	}
	
	
}
