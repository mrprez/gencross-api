package com.mrprez.gencross.migration;

import com.mrprez.gencross.renderer.Renderer;

public class DummyRenderer extends Renderer {
	public String className;

	
	public DummyRenderer(String className) {
		super();
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	@Override
	public String getName(){
		return className;
	}
	

}
