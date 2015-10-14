package com.mrprez.gencross.listener.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.listener.AfterDeletePropertyListener;

public abstract class GroovyAfterDeletePropertyListener extends AfterDeletePropertyListener{
	
	private String script;

	@Override
	public void callAfterDeleteProperty(Property property) throws Exception {
		Binding binding = new Binding();
		binding.setProperty("property", property);
		binding.setProperty("personnage", property.getPersonnage());
		
		ImportCustomizer importCustomizer = new ImportCustomizer();
		importCustomizer.addImports("com.mrprez.gencross.*");
		importCustomizer.addImports("com.mrprez.gencross.value.*");
		CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
		compilerConfiguration.addCompilationCustomizers(importCustomizer);
		
		GroovyShell groovyShell = new GroovyShell(binding, compilerConfiguration);
		
		groovyShell.evaluate(script);
	}

	@Override
	public Map<String, String> getArgs() {
		Map<String, String> args = new HashMap<String, String>(1);
		args.put("script", script);
		return args;
	}

	@Override
	public void setArgs(Map<String, String> args) throws Exception {
		script = args.get("script");
	}
	
	
}
