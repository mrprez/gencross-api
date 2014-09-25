package com.mrprez.gencross.export;

public class PptxGenerator extends MOXGenerator {

	@Override
	public String getOutputExtension() {
		return "pptx";
	}

	@Override
	public String getTemplateFileExtension() {
		return "pptx";
	}

	@Override
	public String getTemlpateFileExtensionDescription() {
		return "Power Point";
	}

}
