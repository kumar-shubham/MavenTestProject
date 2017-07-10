package com.selenium.test;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author kumar
 *
 */

@Component("ACADriver")
@Scope("prototype")
public class ACADriver {

	private WebDriver driver = null;
	
	private String downloadFileLocation = null;
	
	private int downloadTimeOutInSec = 60;

	/**
	 * @return
	 * @throws MalformedURLException 
	 */
	public WebDriver getDriver() throws MalformedURLException {
		if(driver == null){
			Path p = Paths.get(System.getProperty("user.home"), "drivers", "chromedriver");

			System.out.println(":::::::::::::::: " + p);
			System.setProperty("webdriver.chrome.driver",p.toString() );
			System.out.println("::::::::::::::::sssssssssss :::::::: " + System.getProperty("webdriver.chrome.driver"));
			
			String downloadFilepath = System.getProperty("user.home") + "/public/download/";
			String randomString = RandomStringUtils.randomAlphanumeric(8);
			downloadFilepath += randomString + "/";
			downloadFileLocation = downloadFilepath;
			
			File directory = new File(downloadFilepath	);
			if(!directory.exists()){
				directory.mkdir();
			}
			Map<String, Object> preferences = new Hashtable<String, Object>();
			preferences.put("profile.default_content_settings.popups", 0);
			preferences.put("download.prompt_for_download", "false");
			preferences.put("download.default_directory", downloadFilepath);
			preferences.put("plugins.always_open_pdf_externally", true);

			// disable flash and the PDF viewer
			preferences.put("plugins.plugins_disabled", new String[]{
			    "Adobe Flash Player", "Chrome PDF Viewer"});

			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", preferences);

			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(capabilities);

//			driver = new ChromeDriver();

			driver.manage().window().maximize();
			setDriverTimeout(10);
		}
		return driver;
	}
	
	public String getDownloadFileLocation(){
		return downloadFileLocation;
	}
	
	public String downloadFile(WebElement element) throws Exception{
		
		String downloadFileLocation = this.getDownloadFileLocation();

		int downloadTimeOutInSec = this.downloadTimeOutInSec;
		
		String tempFileLocation = System.getProperty("user.home") + "/public/tmp/";
		String randomString = RandomStringUtils.randomAlphanumeric(8);
		tempFileLocation += randomString + "/";

		File directory = new File(downloadFileLocation	);
		if(!directory.exists()){
			directory.mkdirs();
		}
		
		File tempDirectory = new File(tempFileLocation);
		if(!tempDirectory.exists()){
			tempDirectory.mkdirs();
		}

		if(element == null){
			throw new Exception("Download element is null");
		}

		System.out.println("going to download file with extension -> pdf");

		element.click();

		long startTime = new Date().getTime();

		File folder = null;

		File[] files = null;

		long diff = (new Date().getTime() - startTime)/1000;

		System.out.println("downloadFileLocation -> " + downloadFileLocation);
		System.out.println("tempFileLocation -> " + tempFileLocation);
		while(diff < downloadTimeOutInSec){
			Thread.sleep(2000);
			System.out.println("diff -> " + diff);
			folder = new File(downloadFileLocation);
			files = folder.listFiles();
			if(files.length > 0){
				String mimeType = Files.probeContentType(files[0].toPath());
				System.out.println(mimeType);
				String fileName = files[0].getName();
				if(mimeType != null && mimeType.toLowerCase().contains("application/pdf") && fileName.toLowerCase().matches(".*\\.pdf")){
					System.out.println("new filename -> " + tempFileLocation + fileName);
					files[0].renameTo(new File(tempFileLocation + fileName));
					directory.delete();
					return (tempFileLocation + fileName);
				}
			}
			diff = (new Date().getTime() - startTime)/1000;
		}
		directory.delete();
		return null;
		
	}
	

	/**
	 * @return the downloadTimeOutInSec
	 */
	public int getDownloadTimeOutInSec() {
		return downloadTimeOutInSec;
	}

	/**
	 * @param downloadTimeOutInSec the downloadTimeOutInSec to set
	 */
	public void setDownloadTimeOutInSec(int downloadTimeOutInSec) {
		this.downloadTimeOutInSec = downloadTimeOutInSec;
	}

	/**
	 * @param seconds
	 */
	public void setDriverTimeout(int seconds){
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}

}
