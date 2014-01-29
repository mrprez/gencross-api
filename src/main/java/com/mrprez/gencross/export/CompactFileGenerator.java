package com.mrprez.gencross.export;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;

public class CompactFileGenerator extends FileGenerator {
	private static int MAX_LINE_SIZE = 100;
	private String tabulation = ""; 

	@Override
	public void write(Personnage personnage, OutputStream outputStream) throws FileNotFoundException, IOException {
		for(Property property : personnage.getProperties()){
			outputStream.write(getPropertyString(property).getBytes("UTF-8"));
			outputStream.write("\n".getBytes("UTF-8"));
		}
	}

	@Override
	public String getOutputExtension() {
		return "txt";
	}
	
	private String getPropertyString(Property property){
		StringBuilder sb = new StringBuilder(property.getText());
		if(property.getSubProperties() != null && !property.getSubProperties().isEmpty()){
			sb.append(" (");
			for(Property subProperty : property.getSubProperties()){
				String subPropertyString = getPropertyString(subProperty);
				if(subPropertyString.contains("\n")){
					return getPropertyExpandedString(property);
				}
				sb.append(subPropertyString);
				if(sb.length() > MAX_LINE_SIZE){
					return getPropertyExpandedString(property);
				}
				sb.append(", ");
			}
			sb.delete(sb.length()-2, sb.length());
			sb.append(")");
		}
		return sb.toString();
	}
	
	private String getPropertyExpandedString(Property property){
		StringBuilder sb = new StringBuilder(tabulation);
		sb.append(property.getText());
		if(property.getSubProperties() != null){
			tabulation = tabulation + "\t";
			for(Property subProperty : property.getSubProperties()){
				sb.append("\n");
				sb.append(tabulation);
				sb.append(getPropertyString(subProperty));
			}
			tabulation = tabulation.substring(1);
		}
		return sb.toString();
	}

}
