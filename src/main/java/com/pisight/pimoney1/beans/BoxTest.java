package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

public class BoxTest extends PDFTextStripper {


	public static final String TABLE_ID					=	"PDF_TO_HTML";

	private static final String PAGE_SEPARATOR 		= 	"</td></tr><tr><td>* Shubham - END OF PAGE *</td></tr><tr><td>";

	private static final String LINE_SEPARATOR			=	"</td></tr><tr><td>";

	private static final String TABLE_OPENING			=	"<table id=\""+TABLE_ID+"\"><tbody><tr><td>";

	private static final String TABLE_CLOSING			=	"</td></tr></tbody></table>";

	private String regex = null;

	private String startText = null;

	private String endText = null;

	private String markerText = "";

	private String haltText = null;

	private String prevText = null;

	private PDDocument pdDocument;

	public BoxTest(File file) throws Exception{

		if(null == file || !file.exists()){


			throw new Exception("File doesn't exist");

		}

		this.pdDocument = 	PDDocument.load(file);
	}

	public BoxTest(File file, String password) throws Exception{

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

	public String convertPDFToHTML(String wordSeparator, String regex, String startText, String endText, String markerText, String haltText, String prevText) throws Exception{

		long methodStartTime = System.currentTimeMillis();
		long methodEndTime = 0l;

		String htmlCode = TABLE_OPENING + convertPDFToTextOrHTML(wordSeparator, regex, startText, endText, markerText, haltText, prevText) + TABLE_CLOSING;


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

	public String convertPDFToHTML(String wordSeparator) throws Exception{

		return convertPDFToHTML(wordSeparator, null, null, null, null, null, null);
	}


	private String convertPDFToTextOrHTML(String wordSeparator, String regex, String startText, String endText, String markerText, String haltText, String prevText) throws Exception{

		System.out.println("2^^ Total number of pages box test : "+pdDocument.getNumberOfPages());

		this.regex = regex;
		this.startText = startText;
		this.endText = endText;
		this.markerText = markerText;
		this.haltText = haltText;
		this.prevText = prevText;

		PDFTextStripper textStripper = new PDFTextStripper(){

			float endPosition = 0;
			String regex = BoxTest.this.regex;
			String startText = BoxTest.this.startText;
			String endText = BoxTest.this.endText;
			String markerText = BoxTest.this.markerText;
			String haltText = BoxTest.this.haltText;
			String prevText = BoxTest.this.prevText;
			String actualPrevText = null;
			@Override
			protected void writeString(String text, List<TextPosition> textPositions) throws IOException
			{

				System.out.println("text 222- > " + text);



				int endIndex = 0;
				if(regex != null){
					if(endText == null){
						endIndex = startText.length()-1;
					}
					if(regex != null){
						if(haltText != null && text.toUpperCase().contains(haltText.toUpperCase())){
							endPosition = 0;
						}
						Pattern p = Pattern.compile(regex);
						Matcher m = p.matcher(text);
						if(m.matches()){
							int index = text.indexOf(m.group(1));
							TextPosition firstProsition = textPositions.get(index);
							float pos = firstProsition.getXDirAdj();
							String value = "";
							System.out.println("inside matcher ->>>>" + pos + " <<<<- " + endPosition);
							if(pos < endPosition){
								value = markerText;
							}
							writeString(value);
						}
						if(text.contains(startText) && (prevText == null || prevText.equals(actualPrevText))){
							System.out.println("text - > " + text);
							int index = 0;
							if(endText != null){
								index = text.indexOf(endText);
							}else{
								index = text.indexOf(startText)+endIndex;
							}
							System.out.println("index of end of "  + startText + " -> " + index);
							TextPosition pos = textPositions.get(index);
							endPosition = pos.getXDirAdj();
							System.out.println("position of withdrawal -> " + pos.getXDirAdj());
						}
					}
				}
				super.writeString(text, textPositions);
				actualPrevText = text;
			}
		};

		textStripper.setSortByPosition(true);

		textStripper.setWordSeparator(wordSeparator);

		textStripper.setLineSeparator(LINE_SEPARATOR);


		if(null == this.pdDocument){

			throw new Exception("PDDocument is null");

		}

		return textStripper.getText(pdDocument);
	}


}
