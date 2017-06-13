package com.selenium.test;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SelTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		ACADriver dr = new ACADriver();

		WebDriver driver = dr.getDriver();

		driver.get("https://pagalworld.me/filedownload/12111/107233/01%20Ik%20Vaari%20Aa%20-%20Raabta%20(Arijit%20Singh)%20320Kbps.html");
		WebElement downloadLink = driver.findElement(By.xpath("/html/body/div[2]/div[5]/a"));

		String downloadedFile = dr.downloadFile(downloadLink);
		
		System.out.println(downloadedFile);
		
		File file = new File(downloadedFile);
		File folder = file.getParentFile();
		file.delete();
		folder.delete();

		



	}

}
