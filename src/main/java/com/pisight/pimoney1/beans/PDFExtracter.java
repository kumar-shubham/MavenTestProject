package com.pisight.pimoney1.beans;

import java.io.File;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFExtracter extends PDFTextStripper {


	public static final String TABLE_ID					=	"PDF_TO_HTML";

	private static final String PAGE_SEPARATOR 		= 	"</td></tr><tr><td>* Shubham - END OF PAGE *</td></tr><tr><td>";	

	private static final String LINE_SEPARATOR			=	"</td></tr><tr><td>";

	private static final String TABLE_OPENING			=	"<table id=\""+TABLE_ID+"\"><tbody><tr><td>";

	private static final String TABLE_CLOSING			=	"</td></tr></tbody></table>";

	private PDDocument pdDocument;


	public PDFExtracter(File file) throws Exception{
	
		 if(null == file || !file.exists()){
			 
	
			 throw new Exception("File doesn't exist");
	
		 }
	
		 this.pdDocument = 	PDDocument.load(file);
	}
	
	public PDFExtracter(File file, String password) throws Exception{
		
		 if(null == file || !file.exists()){
			 
	
			 throw new Exception("File doesn't exist");
	
		 }
		 
	
		 this.pdDocument = 	PDDocument.load(file);
		 
		 pdDocument.setAllSecurityToBeRemoved(true);
		 
		 if(pdDocument.isEncrypted()){
			 System.out.println("pdf encrypted");
			 StandardDecryptionMaterial sdm = new StandardDecryptionMaterial(password);
			 pdDocument.openProtection(sdm);
		 }
	}
	

	public int getNumberOfPages() throws Exception{

		if(null == pdDocument){

			throw new Exception("PDDocument is null");

		}

		return pdDocument.getNumberOfPages();

	}

	public String convertPDFToHTML(String wordSeparator) throws Exception{

		long methodStartTime = System.currentTimeMillis();
		long methodEndTime = 0l;

		String htmlCode = TABLE_OPENING + convertPDFToTextOrHTML(wordSeparator) + TABLE_CLOSING;
	    
		
		System.out.println("################################################################");
		System.out.println(htmlCode);
		System.out.println("################################################################");
		String escapedJavaScripthtmlCode= StringEscapeUtils.escapeEcmaScript(htmlCode);
		StringBuilder sb = new StringBuilder();
		sb.append("var newHTML         = document.createElement ('table');");
		sb.append("newHTML.setAttribute('id','"+ TABLE_ID + "');");
		sb.append("var strVar=").append("\"").append(escapedJavaScripthtmlCode).append("\";");
		sb.append("newHTML.innerHTML=strVar;");
		sb.append("document.body.appendChild(newHTML);");
		methodEndTime = System.currentTimeMillis();

		System.out.println("4^Latency for extracting data FromPDF() method in millis =" + (methodEndTime - methodStartTime));
		
		return sb.toString();
	}
	


	private String convertPDFToTextOrHTML(String wordSeparator) throws Exception{


		System.out.println("2^^ Total number of pages : "+pdDocument.getNumberOfPages());

		PDFTextStripper textStripper = new PDFTextStripper();

		textStripper.setSortByPosition(true);

		textStripper.setWordSeparator(wordSeparator);

		textStripper.setLineSeparator(LINE_SEPARATOR);

		textStripper.setPageSeparator(PAGE_SEPARATOR);

		if(null == this.pdDocument){

			throw new Exception("PDDocument is null");

		}

		return textStripper.getText(pdDocument);

	}




}
