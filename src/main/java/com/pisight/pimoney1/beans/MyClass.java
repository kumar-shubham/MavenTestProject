package com.pisight.pimoney1.beans;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pisight.pimoney.beans.ACAHttpClient;
import com.pisight.pimoney.dto.ParserResponse;
import com.pisight.pimoney.models.InvestmentAccount;

public class MyClass {

	// private static Logger logger = Logger.getLogger(MyClass.class);

	public static void main(String[] args) throws Exception {

		long start = System.currentTimeMillis();
		WebDriver driver = getDriver();

		JavascriptExecutor js = (JavascriptExecutor) driver;

		PDFExtracter boxTest = null;

		String folder = "investments/new";
		String filename = "COUTTS_SG_INV";

		try {
			boxTest = new PDFExtracter(getFile(folder, filename, "pdf"), "");
		} catch (Exception e) {
			if (e.getMessage().contains("Cannot decrypt PDF, the password is incorrect")) {
				System.out.println("Cannot decrypt PDF, the password is incorrect");
			}
			throw e;
		}

		String page = boxTest.convertPDFToHTML(" ");

		Path htmlfilepath = Paths.get(System.getProperty("user.home"), "Documents/html/", filename + ".html");

		String fileName = folder + "/" + filename + ".pdf";

		Path p = Paths.get(System.getProperty("user.home"), "kumar/statements/statements", fileName);

		js.executeScript(page);

		writeFile(htmlfilepath.toString(), driver.getPageSource());

		try {
			// scrapeStatement(driver);
			ParserResponse response = callParser("SG", "Coutts", p.toString());
			System.out.println();
			System.out.println();
			System.out.println("asset xml");
			System.out.println(response.getXmlAsset());
			System.out.println();
			System.out.println();
			System.out.println("transaction xml");
			System.out.println(response.getXmlTrans());
			System.out.println("generic size => " + response.getGenericContainers().size());
		} catch (Exception e) {
			throw e;
		} finally {

			driver.quit();
			System.out.println("Total Time Taken -> " + (System.currentTimeMillis() - start) + " ms");
		}
	}

	public static void writeFile(String filepath, String output) throws FileNotFoundException, IOException {
		FileWriter ofstream = new FileWriter(filepath);
		try (BufferedWriter out = new BufferedWriter(ofstream)) {
			out.write(output);
		}
	}

	private static File getFile(String dir, String name, String type) throws IOException {

		String fileName = dir + "/" + name + "." + type.toLowerCase();

		Path p = Paths.get(System.getProperty("user.home"), "kumar/statements/statements", fileName);

		System.out.println(p.toString());
		System.out.println(Files.probeContentType(p));
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

	private static ParserResponse callParser(String locale, String parser, String filepath) throws Exception {

		String docByte = getBase64(filepath);

		String url = "http://192.168.0.15:8085/parserengine/parse";

		ACAHttpClient client = new ACAHttpClient(url, ACAHttpClient.REQUEST_TYPE_POST);

		client.setContentType(ACAHttpClient.CONTENT_TYPE_JSON);

		client.setDataField("locale", locale);
		client.setDataField("name", parser);
		client.setDataField("docByte", docByte);

		String result = client.getResponseForPostRequest();

		ObjectMapper mapper = new ObjectMapper();

		ParserResponse pr = mapper.readValue(result, ParserResponse.class);
		
		System.out.println("object from => " + pr.isFromObject());
		System.out.println("generic size => " + pr.getGenericContainers().size());

		if (pr.isFromObject()) {
			String path = "/home/kumar/public/object.ser";
			
			byte[] decodeByte = Base64.decodeBase64(pr.getObjectByte());
			writeFile(path, decodeByte);
			
			FileInputStream fis = null;
			ObjectInputStream in = null;
			try {
				fis = new FileInputStream(path);
				in = new ObjectInputStream(fis);
				pr = (ParserResponse) in.readObject();
				in.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return pr;

	}

	private static void writeFile(String path, byte[] decodeByte) throws IOException {
		
		FileOutputStream fos = new FileOutputStream(new File(path));
		fos.write(decodeByte);
		
	}

	private static String getBase64(String filepath) throws IOException {
		File originalFile = new File(filepath);
		String encodedBase64 = null;
		FileInputStream fileInputStreamReader = null;
		try {
			fileInputStreamReader = new FileInputStream(originalFile);
			byte[] bytes = new byte[(int) originalFile.length()];
			fileInputStreamReader.read(bytes);
			encodedBase64 = Base64.encodeBase64String(bytes);
		} catch (FileNotFoundException e) {
			System.out.println("File not found while parsing statement");
		} catch (IOException e) {
			System.out.println("IO Exception ");
		} finally {
			if (fileInputStreamReader != null) {
				fileInputStreamReader.close();
			}
		}

		/*
		 * if(originalFile != null){ File directory =
		 * originalFile.getParentFile(); originalFile.delete();
		 * directory.delete(); }
		 */

		return encodedBase64;
	}

	public static void scrapeStatement(WebDriver driver) throws Exception {
		List<InvestmentAccount> accounts = null;

		ObjectMapper mapper = new ObjectMapper();
		Path path = Paths.get(System.getProperty("user.home"), "Documents", "bankStmt.json");
		try {
			mapper.writeValue(new File(path.toString()), accounts);
			// String x = mapper.writeValueAsString(accounts);
			// JSONObject json = mapper.readValue(new File(p.toString()),
			// JSONObject.class);
			// String xml = XML.toString(json);
			// //System.out.println(xml);
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
