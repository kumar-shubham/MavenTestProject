package com.pisight.pimoney1.beans;

import java.io.File;
import java.util.concurrent.Callable;

public class CallableService implements Callable<String> {

	private String fileName;
	
	public CallableService(String fileName){
		this.fileName=fileName;
	}

	public String call() throws Exception {
		
		File file = new File(fileName);
		ImageUtility.rotateImage(file);
		file = new File(fileName);
		String result = GoogleOCR.getHTML(file);
        
		return result;
	}

}
