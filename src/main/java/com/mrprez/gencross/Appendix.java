package com.mrprez.gencross;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Appendix extends Properties {
	private static final long serialVersionUID = 1L;
	
	
	public Appendix() {
		super();
	}
	
	public Appendix(InputStream is) throws IOException {
		super();
		this.load(new InputStreamReader(is, "UTF-8"));
	}
	
	public Map<String,String> getSubMap(String key){
		Map<String,String> result = new HashMap<String, String>();
		for(Object k : this.keySet()){
			if(((String)k).startsWith(key.replace(' ', '_'))){
				result.put((String)k, (String)this.get(k));
			}
		}
		return result;
	}
	
	public Map<String,String> getSubMapFromRegex(String regex){
		Map<String,String> result = new HashMap<String, String>();
		for(Object key : this.keySet()){
			if(((String)key).matches(regex)){
				result.put((String)key, (String)this.get(key));
			}
		}
		return result;
	}

	@Override
	public String getProperty(String key) {
		return super.getProperty(key.replace(' ', '_'));
	}

	
	@Override
	public synchronized boolean containsKey(Object key) {
		return super.containsKey(key.toString().replace(' ', '_'));
	}
	
	
	
	
	
	

}
