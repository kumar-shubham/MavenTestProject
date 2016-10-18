package com.pisight.pimoney1.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.pisight.pimoney1.beans.CardAccount;
import com.pisight.pimoney1.beans.DocumentRequest;
import com.pisight.pimoney1.beans.ParserFactory;
import com.pisight.pimoney1.parsers.Parser;


@RestController
public class ParserController {

	@RequestMapping(value = "/parse/{name}/{container}/{locale}/{type}", method = RequestMethod.GET, headers = "Accept=application/json")
	public CardAccount ParserHandler(@PathVariable String name, @PathVariable String container, @PathVariable String locale, @PathVariable String type) throws IOException{

		File file = getFile(name, type);
		String docByte = encodeFileToBase64Binary(file);
		
		DocumentRequest doc = new DocumentRequest();
		doc.setName(name);
		doc.setContainer(container);
		doc.setLocale(locale);
		doc.setType(type);
		doc.setDocByte(docByte);
		
		System.out.println("TTTTTTTTTTTTTTTTTTTT::::::::::: " + doc.getName());
		RestTemplate restTemplate = new RestTemplate();
		CardAccount ca = restTemplate.postForObject("http://localhost:8080/MyWebServices/secretParse", doc, CardAccount.class);
		System.out.println("LLLLLLLLLLLLLLLLLLLLLLL:::::::::::: " + ca.getAccountHolder());

		
		return ca;
	}

	@RequestMapping(value = "/secretParse", method = RequestMethod.POST, headers = "Accept=application/json")
	public CardAccount ParseStatement(@RequestBody DocumentRequest doc) throws Exception {

		System.out.println("PPPPPPPPPPPPPPPPPPPPPPPP:::::::::: " + doc.getDocByte());
		WebDriver driver = getDriver();
		
		String docByte = doc.getDocByte();
		byte[] decodeByte = Base64.decodeBase64(docByte);
		File file  = new File("temp.pdf");
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(decodeByte);
		
		String name = doc.getName();
		String container = doc.getContainer();
		String locale = doc.getLocale();
		String type = doc.getType();
		
		ParserFactory pf = new ParserFactory();

		Parser p = pf.getParser(name, container, locale, type);

		CardAccount ca = p.parse(driver, file);
		if(ca != null){
			ca.setUserId(doc.getUserId());
		}
		fos.close();

		System.out.println("KKKKKKKKKKKKKKKKK  ::: " + ca.getAccountName());
		return ca;
	}

	private File getFile(String name, String type) {
		// TODO Auto-generated method stub

		String fileName = name + "/" + name + "." + type.toLowerCase();
		System.out.println("FFFFFFFFFFFFFFFFFFFFFF  ::: " + fileName);

		Path p = Paths.get(System.getProperty("user.home"), "data/statements", fileName);

		return p.toFile();
	}

	private WebDriver getDriver() {
		// TODO Auto-generated method stub
		Path p1 = Paths.get(System.getProperty("user.home"), "drivers", "phantomjs");

		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);  
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, p1.toString());

		WebDriver driver = new PhantomJSDriver(caps);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		return driver;
	}
	
	private String encodeFileToBase64Binary(File file)
			throws IOException {

		byte[] bytes = loadFile(file);
		String encodedString = Base64.encodeBase64String(bytes);
		return encodedString;
	}

	private static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int)length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}

		is.close();
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+file.getName());
		}

		return bytes;
	}

}
