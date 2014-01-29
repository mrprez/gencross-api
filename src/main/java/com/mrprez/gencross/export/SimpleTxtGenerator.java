package com.mrprez.gencross.export;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PoolPoint;
import com.mrprez.gencross.Property;

public class SimpleTxtGenerator extends FileGenerator {

	@Override
	public void write(Personnage personnage, OutputStream os) throws FileNotFoundException, IOException {
		for(PoolPoint poolPoint : personnage.getPointPools().values()){
			os.write(poolPoint.getName().getBytes());
			os.write(": ".getBytes());
			os.write((""+poolPoint.getRemaining()+"/"+poolPoint.getTotal()).getBytes());
			os.write("\n".getBytes());
		}
		os.write("\n".getBytes());
		Iterator<Property> it = personnage.iterator();
		while(it.hasNext()){
			writeProperty(it.next(), os, 0);
		}
	}
	
	private void writeProperty(Property property, OutputStream os, int depth) throws IOException{
		for(int i=0; i<depth; i++){
			os.write("  ".getBytes());
		}
		os.write(property.getRenderer().displayName(property).getBytes());
		if(property.getValue()!=null){
			os.write((": "+property.getRenderer().displayValue(property)).getBytes());
		}
		os.write("\n".getBytes());
		if(property.getSubProperties()!=null){
			for(Property subProperty : property.getSubProperties().getProperties().values()){
				writeProperty(subProperty, os, depth+1);
			}
		}
	}

	@Override
	public String getOutputExtension() {
		return "txt";
	}

}
