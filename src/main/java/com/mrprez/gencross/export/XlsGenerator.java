package com.mrprez.gencross.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import com.mrprez.gencross.Personnage;

public class XlsGenerator extends TemplatedFileGenerator {
	private HSSFWorkbook workbook;
	private File templateFile;
	
	
	@Override
	public void setTemplate(File templateFile) throws FileNotFoundException, IOException {
		this.templateFile = templateFile;
		reloadTemplate();
	}
	
	private void reloadTemplate() throws IOException{
		FileInputStream fis = new FileInputStream(templateFile);
		try{
			this.workbook = new HSSFWorkbook(fis);
		}finally{
			fis.close();
		}
	}

	@Override
	public void write(Personnage personnage, OutputStream os) throws FileNotFoundException, IOException {
		for(int s=0; s<workbook.getNumberOfSheets(); s++){
			HSSFSheet sheet = workbook.getSheetAt(s);
			for(int r=0; r<sheet.getLastRowNum(); r++){
				HSSFRow row = sheet.getRow(r);
				if(row!=null){
					for(int c=0; c<row.getLastCellNum(); c++){
						HSSFCell cell = row.getCell(c);
						if(cell!=null && cell.getCellType()==Cell.CELL_TYPE_STRING){
							HSSFRichTextString richTextString = cell.getRichStringCellValue();
							String string = richTextString.getString();
							cell.setCellValue(super.replace(string, personnage));
						}
					}
				}
			}
		}
		workbook.write(os);
		reloadTemplate();
	}

	@Override
	public String getOutputExtension() {
		return "xls";
	}

	@Override
	public String getTemlpateFileExtensionDescription() {
		return "Fichiers Excel";
	}

	@Override
	public String getTemplateFileExtension() {
		return "xls";
	}

}
