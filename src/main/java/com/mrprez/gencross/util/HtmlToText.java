package com.mrprez.gencross.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

public class HtmlToText extends ParserCallback {
	private StringBuilder stringBuilder;
	
	public void parse(Reader reader) throws IOException{
		ParserDelegator parser = new ParserDelegator();
		stringBuilder = new StringBuilder();
		parser.parse(reader, this, true);
	}
	
	public void parse(String string) throws IOException{
		parse(new StringReader(string));
	}

	@Override
	public void handleText(char[] arg0, int arg1) {
		stringBuilder.append(arg0);
	}
	
	public String getString(){
		return stringBuilder.toString();
	}
	
	

}
