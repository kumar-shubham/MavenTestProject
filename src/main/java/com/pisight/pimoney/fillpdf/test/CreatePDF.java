package com.pisight.pimoney.fillpdf.test;

import java.io.IOException;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

public class CreatePDF {

	public static void main(String args[]) throws IOException {

		String pdfpath = "/home/kumar/kumar/barclays/Forms/Generalli/test1.pdf";

		PDDocument pdDocument = new PDDocument();
		PDPage page = new PDPage(PDRectangle.A4);
		pdDocument.addPage(page);

		PDAcroForm form = new PDAcroForm(pdDocument);
		pdDocument.getDocumentCatalog().setAcroForm(form);

		PDFont font = PDType1Font.HELVETICA;
		PDResources resources = new PDResources();
		resources.put(COSName.getPDFName("Helv"), font);
		form.setDefaultResources(resources);

		PDTextField textField = new PDTextField(form);
		textField.setPartialName("SampleField");
		
		String defaultAppearance = "/Helv 12 Tf 0 0 1 rg";
		textField.setDefaultAppearance(defaultAppearance);
		
		form.getFields().add(textField);
		
		PDAnnotationWidget widget = textField.getWidgets().get(0);
		PDRectangle rect = new PDRectangle(50, 750, 200, 50);
		widget.setRectangle(rect);
		widget.setPage(page);

		PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
		fieldAppearance.setBorderColour(new PDColor(new float[]{0,1,0}, PDDeviceRGB.INSTANCE));
		fieldAppearance.setBackground(new PDColor(new float[]{1,1,0}, PDDeviceRGB.INSTANCE));
		widget.setAppearanceCharacteristics(fieldAppearance);
		
		widget.setPrinted(true);
		page.getAnnotations().add(widget);
		textField.setValue("Sample Field");

		//Save the PDF to a location.
		pdDocument.save(pdfpath);
		pdDocument.close();

	}

}
