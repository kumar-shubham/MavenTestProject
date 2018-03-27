package com.pisight.pimoney.fillpdf.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.pisight.pimoney1.beans.PDFExtracter;

public class FillPDF {

	private static PDDocument _pdfDocument;

	public static void main(String[] args) throws Exception {

		String folder = "Generalli";
		String filename = "test1";
		
		saveHTMLFile(folder, filename);

		File file = getFile(folder, filename, "pdf");

		File targetFile = getFile(folder, filename + "-output", "pdf");

		populateAndCopy(file, targetFile);

	}

	private static void populateAndCopy(File originalPdf, File targetPdf) throws IOException {

		_pdfDocument = PDDocument.load(originalPdf);

		System.out.println(_pdfDocument.getNumberOfPages());

		printFields();

		_pdfDocument.save(targetPdf);
		_pdfDocument.close();

	}

	@SuppressWarnings("rawtypes")
	public static void printFields() throws IOException {
		PDDocumentCatalog docCatalog = _pdfDocument.getDocumentCatalog();
		PDAcroForm acroForm = docCatalog.getAcroForm();
		if (acroForm == null) {
			System.out.println("Acro Form is null. No Forms Found in the pdf");
			return;
		}
		List<PDField> fields = acroForm.getFields();
		Iterator fieldsIter = fields.iterator();

		System.out.println(new Integer(fields.size()).toString() + " top-level fields were found on the form");

		while (fieldsIter.hasNext()) {
			PDField field = (PDField) fieldsIter.next();
			processField(field, "|--", field.getPartialName());
		}
	}

	@SuppressWarnings("rawtypes")
	private static void processField(PDField field, String sLevel, String sParent) throws IOException {
		List<PDField> kids = null;
		if (field instanceof PDNonTerminalField) {
			PDNonTerminalField nonTerminalField = (PDNonTerminalField) field;
			kids = nonTerminalField.getChildren();
		}

		if (kids != null) {
			Iterator kidsIter = kids.iterator();
			if (!sParent.equals(field.getPartialName())) {
				sParent = sParent + "." + field.getPartialName();
			}

			System.out.println(sLevel + sParent);

			while (kidsIter.hasNext()) {
				Object pdfObj = kidsIter.next();
				if (pdfObj instanceof PDField) {
					PDField kid = (PDField) pdfObj;
					processField(kid, "|  " + sLevel, sParent);
				}
			}
		} else {
			String outputString = sLevel + sParent + "." + field.getPartialName() + ",  type="
					+ field.getClass().getName();
			System.out.println(outputString);
		}
	}

	private static void saveHTMLFile(String folder, String filename) throws Exception {
		
		WebDriver driver = getDriver();

		JavascriptExecutor js = (JavascriptExecutor) driver;

		PDFExtracter boxTest = null;
		try {
			boxTest = new PDFExtracter(getFile(folder, filename, "pdf"), "");
		} catch (Exception e) {
			if (e.getMessage().contains("Cannot decrypt PDF, the password is incorrect")) {
				System.out.println("Cannot decrypt PDF, the password is incorrect");
			}
			if(driver != null) {
				System.out.println("closing driver");
				driver.quit();
			}
			throw e;
		}

		String page = boxTest.convertPDFToHTML(" ");

		Path htmlfilepath = Paths.get(System.getProperty("user.home"), "Documents/html/", filename + ".html");
		
		System.out.println("HTML File => " + htmlfilepath.toString());

		js.executeScript(page);

		writeFile(htmlfilepath.toString(), driver.getPageSource());
		
		if(driver != null) {
			driver.close();
		}
	}

	private static File getFile(String dir, String name, String type) throws IOException {

		String fileName = dir + "/" + name + "." + type.toLowerCase();

		Path p = Paths.get(System.getProperty("user.home"), "kumar/barclays/Forms", fileName);

		System.out.println(p.toString());
		System.out.println(Files.probeContentType(p));
		return p.toFile();
	}
	
	public static void writeFile(String filepath, String output) throws FileNotFoundException, IOException {
		FileWriter ofstream = new FileWriter(filepath);
		try (BufferedWriter out = new BufferedWriter(ofstream)) {
			out.write(output);
		}
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

}
