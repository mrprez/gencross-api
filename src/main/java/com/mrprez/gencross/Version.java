package com.mrprez.gencross;

import java.util.Arrays;

import org.dom4j.Attribute;

public class Version implements Comparable<Version> {
	public final static Version DEFAULT_VERSION = new Version(new int[]{0});
	private static final String VERSION_SEPARATOR = ".";
	
	private int numbers[];
	
	
	protected Version() {
		super();
	}
	
	public Version(int... numbers) {
		super();
		this.numbers = numbers;
	}

	public Version(String numberString) {
		super();
		String tab[] = numberString.split("["+VERSION_SEPARATOR+"]");
		numbers = new int[tab.length];
		for(int i=0; i<tab.length; i++){
			numbers[i] = Integer.parseInt(tab[i]);
		}
	}
	
	public Version(Attribute attribute) {
		this(attribute.getStringValue());
	}


	@Override
	public int compareTo(Version other) {
		for(int i=0; i<other.numbers.length && i<this.numbers.length; i++){
			if(other.numbers[i]!=this.numbers[i]){
				return this.numbers[i]-other.numbers[i];
			}
		}
		return this.numbers.length-other.numbers.length;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<numbers.length; i++){
			sb.append(numbers[i]);
			sb.append(VERSION_SEPARATOR);
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof Version){
			return Arrays.equals(((Version)arg0).numbers, numbers);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		for(int i=0; i<numbers.length; i++){
			hashCode = hashCode+numbers[i];
		}
		return hashCode;
	}
	
	public int[] getNumbers(){
		return Arrays.copyOf(numbers, numbers.length);
	}
	
	

}
