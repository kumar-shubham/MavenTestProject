package com.pisight.pimoney1.beans;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.impl.io.IdentityOutputStream;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pisight.pimoney1.beans.CardAccount;
import com.pisight.pimoney1.beans.CardTransaction;
import com.pisight.pimoney1.beans.ParserUtility;

public class PDFTest {


	public static final String TABLE_ID					=	"PDF_TO_HTML";

	private static final String TABLE_OPENING			=	"<table id=\""+TABLE_ID+"\"><tbody><tr><td>";

	private static final String TABLE_CLOSING			=	"</td></tr></tbody></table>";

	private static final String LINE_SEPARATOR			=	"</td></tr><tr><td>";
	
	private static File file1 = null;
	private static String output = null;


	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		//		Path p = Paths.get(System.getProperty("user.home"), ".m2", "chromedriver.exe");

		//		sSystem.setProperty("webdriver.chrome.driver",p.toString() );

		final long startTime = System.currentTimeMillis();
		WebDriver driver = getDriver();


		JavascriptExecutor js = (JavascriptExecutor) driver;

		//		String page = getHTMLFromPDF();

		String page = getHTMLFromImage();
		
		FileOutputStream fos = new FileOutputStream(new File(file1.getParent() + "/html/"+file1.getName()+".html"));
		fos.write(output.getBytes());
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~``");
		//		System.out.println(page);

//		js.executeScript(page);
		//		scrapeStatement(driver);
		System.out.println("closing driver");

		driver.quit();
		
		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime) );

	}

	private static String getHTMLFromPDF() throws Exception{

		PDFExtracter pdfExtractor = null;
		try{
			pdfExtractor = new PDFExtracter(getFile("HDFC", "52913630_1475827655982", "pdf"), "KUMA1932");
		}catch(CryptographyException e){
			if(e.getMessage().contains("The supplied password does not match")){
				System.out.println("The supplied password does not match");
			}
			throw e;
		}

		BoxTest boxTest = null;
		try{
			boxTest = new BoxTest(getFile("AMEX", "Statement_Oct 2015", "pdf"), "");
		}catch(CryptographyException e){
			if(e.getMessage().contains("The supplied password does not match")){
				System.out.println("The supplied password does not match");
			}
			throw e;
		}

		String page = boxTest.convertPDFToHTML(" ");
		return page;
	}

	private static List<String> convertPDFtoImage(File file) throws IOException{

		PDDocument document = PDDocument.loadNonSeq(file, null);
		List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
		String path = file.getParent();
		System.out.println("path -> " + path);
		List<String> imageFiles = new ArrayList<String>();
		int page = 0;
		for (PDPage pdPage : pdPages)
		{ 
			++page;
			String filename = path + "/temp/temp" + "-" + page + ".png";
			BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
			ImageIOUtil.writeImage(bim, filename, 300);
			imageFiles.add(filename);
		}
		document.close();

		return imageFiles;

	}

	private static String getHTMLFromImage() throws Exception{

		File pdfFile = getFile("insurance/stmts3", "AXA LivingEnhancer", "pdf");

		//		File pdfFile = getFile("CITI", "CITIMain", "pdf");

		List<String> imageFiles = convertPDFtoImage(pdfFile);

		String htmlCode = "";

		List<FutureTask<String>> results = new ArrayList<FutureTask<String>>();
		ExecutorService executor = Executors.newFixedThreadPool(imageFiles.size());
		for(String fileName: imageFiles){
			CallableService cs = new CallableService(fileName);
			FutureTask<String> ft = new FutureTask<String>(cs);
			results.add(ft);
			executor.execute(ft);
		}
		while(true){
			int count = 0;
			for(FutureTask<String> ft: results){
				if(ft.isDone()){
					count++;
				}
			}
			if(count == results.size()){
				executor.shutdown();
				break;
			}
		}
		
		for(FutureTask<String> ft: results){
			htmlCode +=  LINE_SEPARATOR + ft.get();
		}

		htmlCode = TABLE_OPENING + htmlCode + TABLE_CLOSING;

		System.out.println("page -> " + htmlCode);
		output = htmlCode;

		String escapedJavaScripthtmlCode= StringEscapeUtils.escapeEcmaScript(htmlCode);
		StringBuilder sb = new StringBuilder();
		sb.append("var newHTML         = document.createElement ('table');");
		sb.append("newHTML.setAttribute('id','"+ TABLE_ID + "');");
		sb.append("var strVar=").append("\"").append(escapedJavaScripthtmlCode).append("\";");
		sb.append("newHTML.innerHTML=strVar;");
		sb.append("document.body.appendChild(newHTML);");
		return sb.toString();
	}
	

	private static File getFile(String dir, String name, String type) {
		// TODO Auto-generated method stub

		String fileName = dir + "/" + name + "." + type.toLowerCase();
		System.out.println("FFFFFFFFFFFFFFFFFFFFFF  ::: " + fileName);

		Path p = Paths.get(System.getProperty("user.home"), "Downloads/statements/statements", fileName);

		System.out.println("AAAAAAAAAAAA :: " + p.toString());

		file1 = p.toFile();
		return p.toFile();
	}

	private static WebDriver getDriver() {
		// TODO Auto-generated method stub
		Path p1 = Paths.get(System.getProperty("user.home"), "drivers", "phantomjs");

		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);  
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, p1.toString());

		WebDriver driver = new PhantomJSDriver(caps);

		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

		return driver;
	}

	public static void scrapeStatement(WebDriver driver) throws Exception{


		

		TakesScreenshot tss = (TakesScreenshot)driver;
		tss.getScreenshotAs(OutputType.BASE64);
		WebElement ele = driver.findElement(By.id("PDF_TO_HTML"));

		ObjectMapper mapper = new ObjectMapper();
		Path p3 = Paths.get(System.getProperty("user.home"), "Documents", "bankStmt.json");
		try {
			mapper.writeValue(new File(p3.toString()), null);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




	}
	


}

