package com.pisight.pimoney1.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String connectionTestCommand = "pwd | tee -a /home/kumar/Desktop/a.out";
		System.out.println("Connection Test Command = " + connectionTestCommand);
		
		ProcessBuilder builder = new ProcessBuilder("sh", "/home/kumar/Desktop/test.sh");
		builder.redirectOutput(new File("/home/kumar/Desktop/a.out"));
		builder.redirectError(new File("/home/kumar/Desktop/a.out"));
		Process p1 = builder.start(); // may throw IOException
		
		p1.waitFor();
		
		FileInputStream fis = new FileInputStream("/home/kumar/Desktop/a.out");
		fis.getChannel().position(0);
        BufferedReader br=new BufferedReader(new InputStreamReader(fis));    
        int i;    
        String output2 = "";
        while((i=br.read())!=-1){  
        output2 += (char) i;
        }  
        br.close();
        fis.close();
		
		System.out.println( "output =>  " + output2);
	}

}
