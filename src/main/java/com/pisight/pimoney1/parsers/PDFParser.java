package com.pisight.pimoney1.parsers;

import java.io.File;

import org.openqa.selenium.WebDriver;

import com.pisight.pimoney1.beans.CardAccount;
import com.pisight.pimoney1.beans.PDFExtracter;




public abstract class PDFParser implements Parser {
	
	protected String parsePDFToHTML(File file) throws Exception{
		
		PDFExtracter pdfExtractor = new PDFExtracter(file);
		
		String page = pdfExtractor.convertPDFToHTML(" ");
		
		return page;
		
	}

	public CardAccount parse(WebDriver driver, File file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
