package com.mrprez.gencross.export;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.util.HtmlToText;

public class DrawerGenerator extends TemplatedFileGenerator {
	public static final String BACKGROUND_IMAGE_NAME = "background.jpg";
	public static final String XML_NAME = "descriptor.xml";
	
	private Document document;
	private BufferedImage image;
	private Map<String, Font> fonts = new HashMap<String, Font>();
	
	@Override
	public void setTemplate(File template) throws FileNotFoundException,IOException {
		try {
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(template)));
			try{
				ZipEntry entry;
				while((entry = zis.getNextEntry()) != null){
					if(entry.getName().equals(XML_NAME)){
						document = loadXml(zis);
					}else if(entry.getName().equals(BACKGROUND_IMAGE_NAME)){
						image = ImageIO.read(zis);
					}else if(entry.getName().endsWith(".ttf")){
						Font font = Font.createFont(Font.TRUETYPE_FONT, zis);
						fonts.put(font.getFontName(), font);
					}
				}
			}finally{
				zis.close();
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public void write(Personnage personnage, OutputStream outputStream) throws FileNotFoundException, IOException {
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = result.createGraphics();
		graphics.drawImage(image, 0, 0,image.getWidth(),image.getHeight(), null);
		Iterator<?> it = document.getRootElement().elementIterator();
		while(it.hasNext()){
			Element element = (Element)it.next();
			int x = Integer.parseInt(element.attributeValue("x"));
			int y = Integer.parseInt(element.attributeValue("y"));
			float fontSize = Float.parseFloat(element.attributeValue("fontSize"));
			String fontName = element.attributeValue("fontName");
			int fontStyle = Integer.parseInt(element.attributeValue("fontStyle"));
			Font font  = fonts.get(fontName);
			font = font.deriveFont(fontSize);
			font = font.deriveFont(fontStyle);
			double angle = element.attributeValue("angle")!=null?Double.parseDouble(element.attributeValue("angle")):0.0;
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.rotate(angle);
			font = font.deriveFont(affineTransform);
			graphics.setFont(font);
			graphics.setColor(buildColor(element.attributeValue("color")));
			String text = getText(element.getText(), personnage);
			if(text.startsWith("<html>")){
				HtmlToText htmlToText = new HtmlToText();
				htmlToText.parse(text);
				text = htmlToText.getString();
			}
			if(element.attribute("substring")!=null){
				int substring = Integer.parseInt(element.attributeValue("substring"));
				if(substring<text.length()){
					text = text.substring(0, substring);
				}
			}
			graphics.drawString(text, x, y);
		}
		graphics.dispose();
		ImageIO.write(result, "jpeg", outputStream);
	}
	
	public static Document loadXml(InputStream inputStream) throws IOException, DocumentException{
		InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
		StringBuilder buffer = new StringBuilder();
		int i;
		while((i=reader.read())>=0){
			buffer.append((char)i);
		}
		Document document = DocumentHelper.parseText(buffer.toString());
		return document;
	}
	
	private Color buildColor(String colorString){
		String tab[] = colorString.split(",");
		int r = Integer.parseInt(tab[0]);
		int g = Integer.parseInt(tab[1]);
		int b = Integer.parseInt(tab[2]);
		return new Color(r,g,b);
	}

	@Override
	public String getOutputExtension() {
		return "jpg";
	}

	@Override
	public String getTemlpateFileExtensionDescription() {
		return "GenCross Drawer";
	}

	@Override
	public String getTemplateFileExtension() {
		return "gcd";
	}

}
