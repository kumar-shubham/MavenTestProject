package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFManager {
    
	   private PDFParser parser;
	   private PDFTextStripper pdfStripper;
	   private PDDocument pdDoc ;
	   private COSDocument cosDoc ;
	   
	   private String Text ;
	   private String filePath = "/home/kumar/Downloads/statements/statements/investments/823512 EUR.pdf";
	   private File file;

	    public PDFManager() {
	        
	    }
	   public String ToText() throws IOException
	   {
	       this.pdfStripper = null;
	       this.pdDoc = null;
	       this.cosDoc = null;
	       
	       file = new File(filePath);
	       parser = new PDFParser(new RandomAccessFile(file,"r")); // update for PDFBox V 2.0
	       
	       parser.parse();
	       cosDoc = parser.getDocument();
	       pdfStripper = new PDFTextStripper();
	       pdDoc = new PDDocument(cosDoc);
	       pdDoc.getNumberOfPages();
	       pdfStripper.setStartPage(1);
	       pdfStripper.setEndPage(10);
	       
	       // reading text from page 1 to 10
	       // if you want to get text from full pdf file use this code
	       // pdfStripper.setEndPage(pdDoc.getNumberOfPages());
	       
	       Text = pdfStripper.getText(pdDoc);
	       return Text;
	   }

	    public void setFilePath(String filePath) {
	        this.filePath = filePath;
	    }
	   
	}