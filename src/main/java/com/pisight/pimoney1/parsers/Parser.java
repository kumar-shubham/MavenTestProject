package com.pisight.pimoney1.parsers;

import java.io.File;

import org.openqa.selenium.WebDriver;

import com.pisight.pimoney1.beans.CardAccount;



public interface Parser {

	public CardAccount parse(WebDriver driver, File file) throws Exception;

}
