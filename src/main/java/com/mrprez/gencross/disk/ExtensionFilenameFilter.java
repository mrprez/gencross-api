package com.mrprez.gencross.disk;

import java.io.File;
import java.io.FilenameFilter;

public class ExtensionFilenameFilter implements FilenameFilter {
	private String extension;
	
	
	public ExtensionFilenameFilter(String extension) {
		super();
		this.extension = extension;
	}

	@Override
	public boolean accept(File dir, String name) {
		return name.endsWith("."+extension);
	}

}
