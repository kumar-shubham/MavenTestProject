package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
	
	private float fontSizeThreshold = -1;

	private PDDocument pdDocument;

	public BoxTest(File file) throws Exception{

		if(null == file || !file.exists()){


			throw new Exception("File doesn't exist");

		}

		this.pdDocument = 	PDDocument.load(file);
	}
	
	public BoxTest(String file) throws Exception{

		if(StringUtils.isEmpty(file)){


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
	

	/**
	 * Parses PDF and returns string
	 * @param wordSeparator -> Mandatory Field. Separator for words
	 * @param regex -> Regex to match word for to identify a line
	 * @param startText -> Header start text
	 * @param endText -> Header End text :: Based on the start and end text of a line the endposition is decided
	 * and for the following lines if the text matches regex then markertext is inserted at that endposition to identify the line.
	 * @param markerText -> The text that need to be inserted in the line for identification of that line(ex - (DR) for debit transaction)
	 * @param haltText -> After encountering this text the regex match will stop
	 * @param prevText -> This text should appear before the startText. Used for better matching. If null then only startText will be considered.
	 * @param fontSizeThreshold -> ALL font size greater than this threshold size will be removed from the parsed string. This is generally used to remove Big font watermarks or unwanted headers
	 * @return parsed String
	 * @throws Exception
	 */
	public String convertPDFToHTML(String wordSeparator, String regex, String startText, String endText, String markerText, String haltText, String prevText, float fontSizeThreshold) throws Exception{

		long methodStartTime = System.currentTimeMillis();
		long methodEndTime = 0l;

		String htmlCode = TABLE_OPENING + convertPDFToTextOrHTML(wordSeparator, regex, startText, endText, markerText, haltText, prevText, fontSizeThreshold) + TABLE_CLOSING;


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
	
	public String convertPDFToHTML(String wordSeparator, String regex, String startText, String endText, String markerText, String haltText, String prevText) throws Exception{

		return convertPDFToHTML(wordSeparator, regex, startText, endText, markerText, haltText, prevText, -1);
	}
	
	public String convertPDFToHTML(String wordSeparator, float fontSizeThreshold) throws Exception{

		return convertPDFToHTML(wordSeparator, null, null, null, null, null, null, fontSizeThreshold);
	}

	public String convertPDFToHTML(String wordSeparator) throws Exception{

		return convertPDFToHTML(wordSeparator, null, null, null, null, null, null);
	}


	/**
	 * Parses PDF and returns string
	 * @param wordSeparator -> Mandatory Field. Separator for words
	 * @param regex -> Regex to match word for to identify a line
	 * @param startText -> Header start text
	 * @param endText -> Header End text :: Based on the start and end text of a line the endposition is decided
	 * and for the following lines if the text matches regex then markertext is inserted at that endposition to identify the line.
	 * @param markerText -> The text that need to be inserted in the line for identification of that line(ex - (DR) for debit transaction)
	 * @param haltText -> After encountering this text the regex match will stop
	 * @param prevText -> This text should appear before the startText. Used for better matching. If null then only startText will be considered.
	 * @param fontSizeThreshold -> ALL font size greater than this threshold size will be removed from the parsed string. This is generally used to remove Big font watermarks or unwanted headers
	 * @return parsed String
	 * @throws Exception
	 */
	private String convertPDFToTextOrHTML(String wordSeparator, String regex, String startText, String endText, String markerText, String haltText, String prevText, float fontSizeThreshold) throws Exception{

		System.out.println("2^^ Total number of pages box test : "+pdDocument.getNumberOfPages());

		this.regex = regex;
		this.startText = startText;
		this.endText = endText;
		this.markerText = markerText;
		this.haltText = haltText;
		this.prevText = prevText;
		this.fontSizeThreshold = fontSizeThreshold;

		PDFTextStripper textStripper = new PDFTextStripper(){

			float endPosition = 0;
			String regex = BoxTest.this.regex;
			String startText = BoxTest.this.startText;
			String endText = BoxTest.this.endText;
			String markerText = BoxTest.this.markerText;
			String haltText = BoxTest.this.haltText;
			String prevText = BoxTest.this.prevText;
			float fontSizeThreshold = BoxTest.this.fontSizeThreshold;
			String actualPrevText = null;
			float previousY = -1;
			@Override
			protected void writeString(String text, List<TextPosition> textPositions) throws IOException
			{

				// Use tree map and sort by x position.
				// user some logic to write text once for a line by passing text and textPositions
				System.out.println();
				System.out.println("text 222- > " + text );
				
				
				String newText = "";
				List<Integer> positions = new ArrayList<Integer>();
				if(fontSizeThreshold > 0){
//					System.out.println("Font size threshold is :: " + fontSizeThreshold);
					for(int i = 0; i<textPositions.size(); i++){
						float fontSize = textPositions.get(i).getFontSize();
						
						
						System.out.println(" text is :: " + textPositions.get(i));
//						System.out.println(" x position is :: " + textPositions.get(i).getX());
						System.out.println(" y position is :: " + textPositions.get(i).getY());
						
						if(fontSize < fontSizeThreshold){
							newText += text.charAt(i);
							
						}
						else{
							positions.add(i);
						}
						
					}
				}
				
				if(StringUtils.isNotEmpty(newText)){
					text = newText;
					newText = "";
				}
				
//				System.out.println("text position size before :: " + textPositions.size());
				
				for(int i = 0; i< positions.size(); i++){
					textPositions.remove(positions.get(i)-i);
				}
				
//				System.out.println("text position size after :: " + textPositions.size());
				for(int i = 0; i< textPositions.size(); i++){
					
					float currentY = textPositions.get(i).getY();
					
					if(previousY > 0 && currentY - previousY > 10){
						newText += "</td></tr><tr><td>";
					}
					newText += text.charAt(i);
					previousY = currentY;
				}
				
				if(StringUtils.isNotEmpty(newText)){
					text = newText;
					newText = "";
				}

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

//		textStripper.setLineSeparator(LINE_SEPARATOR);
		


		if(null == this.pdDocument){

			throw new Exception("PDDocument is null");

		}

		return textStripper.getText(pdDocument);
	}


}
