package com.mrprez.gencross.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Properties;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PoolPoint;
import com.mrprez.gencross.Property;

public abstract class TemplatedFileGenerator extends FileGenerator {
	protected Properties properties = new Properties();
	private char openVariableChar = '{';
	private char closeVariableChar = '}';
	public static final String CLOSE_VARIABLE_PROPERTY_NAME = "variable.close";
	public static final String OPEN_VARIABLE_PROPERTY_NAME = "variable.open";
	public static final String VALUE = "value";
	public static final String NAME = "name";
	public static final String BRUT_VALUE = "brutValue";
	public static final String BRUT_NAME = "brutName";
	public static final String TEXT = "text";
	public static final String COMMENT = "comment";
	public static final String POINT_TOTAL = "pointsTotal";
	public static final String REMAINING_POINTS = "remainingPoints";
	public static final String SPEND_POINTS = "spendPoints";
	
	public abstract void setTemplate(File template) throws FileNotFoundException, IOException;
	
	public String getTemplateFileExtension(){
		return null;
	}
	
	public String getTemlpateFileExtensionDescription(){
		return null;
	}
	
	public void loadProperties(InputStream is) throws IOException{
		properties.load(is);
		if(properties.containsKey(OPEN_VARIABLE_PROPERTY_NAME)){
			if(properties.getProperty(OPEN_VARIABLE_PROPERTY_NAME).length()>0){
				openVariableChar = properties.getProperty(OPEN_VARIABLE_PROPERTY_NAME).charAt(0);
			}
		}
		if(properties.containsKey(CLOSE_VARIABLE_PROPERTY_NAME)){
			if(properties.getProperty(CLOSE_VARIABLE_PROPERTY_NAME).length()>0){
				closeVariableChar = properties.getProperty(CLOSE_VARIABLE_PROPERTY_NAME).charAt(0);
			}
		}
	}
	
	protected void replace(Reader reader, Writer writer, Personnage personnage) throws IOException{
		BufferedReader bufReader = new BufferedReader(reader);
		BufferedWriter bufWriter = new BufferedWriter(writer);
		char c;
		int i;
		while((i=bufReader.read())>=0){
			c = (char) i;
			if(c==openVariableChar){
				String variableName = getVariableName(bufReader);
				variableName = properties.getProperty(variableName, variableName);
				bufWriter.write(getText(variableName, personnage));
			}else{
				bufWriter.write(i);
			}
		}
		bufWriter.flush();
	}
	
	private String getVariableName(Reader reader) throws IOException{
		StringBuilder stringBuilder = new StringBuilder();
		char c;
		while((c=(char) reader.read())>=0){
			if(c==closeVariableChar){
				return stringBuilder.toString();
			}else{
				stringBuilder.append(c);
			}
		}
		throw new IOException("EOF found, '}' expected");
	}
	
	protected String replace(String string, Personnage personnage){
		String result = ""+string;
		while(result.contains(""+openVariableChar)){
			int start = result.indexOf(""+openVariableChar);
			int end = result.indexOf(""+closeVariableChar,start);
			if(end<0){
				end = result.length();
			}
			String variableName = result.substring(start+1,end).trim();
			variableName = properties.getProperty(variableName,variableName);
			result = result.replace(""+openVariableChar+variableName+closeVariableChar, getText(variableName, personnage));
		}
		return result;
	}
	
	protected String getText(String name, Personnage personnage){
		int lastSharpIndex = name.lastIndexOf('#');
		if(lastSharpIndex<0){
			lastSharpIndex = 0;
		}
		Property property;
		PoolPoint poolPoint;
		String type = VALUE;
		if(name.substring(lastSharpIndex).contains("->")){
			type = name.substring(name.indexOf("->", lastSharpIndex)+2);
		}
		if(type.equals(POINT_TOTAL) || type.equals(REMAINING_POINTS) || type.equals(SPEND_POINTS)){
			poolPoint = personnage.getPointPools().get(name.substring(0, name.indexOf("->", lastSharpIndex)));
			property = null;
		}else{
			if(name.substring(lastSharpIndex).contains("->")){
				property = findProperty(name.substring(0, name.indexOf("->", lastSharpIndex)), personnage);
			}else{
				property = findProperty(name, personnage);
			}
			poolPoint = null;
		}
		
		if(property==null && poolPoint==null){
			return "";
		}
		
		if(type.equals(VALUE)){
			if(property.getValue()!=null){
				return property.getRenderer().displayValue(property);
			}else{
				return "";
			}
		}else if(type.equals(BRUT_VALUE)){
			if(property.getValue()!=null){
				return property.getValue().getString();
			}else{
				return "";
			}
		}else if(type.equals(NAME)){
			return property.getRenderer().displayName(property);
		}else if(type.equals(BRUT_NAME)){
			return property.getName();
		}else if(type.equals(TEXT)){
			if(property.getValue()!=null){
				return property.getRenderer().displayName(property)+" : "+property.getRenderer().displayValue(property);
			}else{
				return "";
			}
		}else if(type.equals(COMMENT)){
			if(property.getComment()!=null){
				return property.getComment();
			}else{
				return "";
			}
		}else if(type.equals(POINT_TOTAL)){
			return ""+poolPoint.getTotal();
		}else if(type.equals(REMAINING_POINTS)){
			return ""+poolPoint.getRemaining();
		}else if(type.equals(SPEND_POINTS)){
			return ""+(poolPoint.getTotal()-poolPoint.getRemaining());
		}
		return "";
	}
	
	private Property findProperty(String name, Personnage personnage){
		String namesTab[] = name.split("#");
		Property result = personnage.getProperty(namesTab[0]);
		for(int i=1; i<namesTab.length; i++){
			if(result==null){
				return null;
			}
			result = getNextProperty(namesTab[i], result);
		}
		return result;
	}
	
	private Property getNextProperty(String name, Property property){
		if(property.getSubProperties()==null){
			return null;
		}
		if(name.startsWith("@")){
			Property result = null;
			int index = Integer.parseInt(name.replaceAll("@", ""));
			Iterator<Property> it = property.getSubProperties().iterator();
			int count = 0;
			while(it.hasNext() && count<index){
				result = it.next();
				count++;
			}
			if(count!=index){
				return null;
			}
			return result;
		}else{
			if(property.getSubProperty(name)==null){
				return null;
			}
			return property.getSubProperty(name);
		}
	}
	
	
}
