package com.mrprez.gencross.exception;

import com.mrprez.gencross.Version;

public class PersonnageVersionException extends Exception {
	private static final long serialVersionUID = 1L;
	
	
	private Version fileVersion;
	private Version pluginVersion;
	
	public PersonnageVersionException() {
		super();
	}

	public PersonnageVersionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	public PersonnageVersionException(Version fileVersion, Version pluginVersion, String message) {
		super(message);
		this.fileVersion = fileVersion;
		this.pluginVersion = pluginVersion;
	}

	public PersonnageVersionException(String arg0) {
		super(arg0);
	}

	public PersonnageVersionException(Throwable arg0) {
		super(arg0);
	}

	public Version getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(Version fileVersion) {
		this.fileVersion = fileVersion;
	}

	public Version getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(Version pluginVersion) {
		this.pluginVersion = pluginVersion;
	}
	
	

}
