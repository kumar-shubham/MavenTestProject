package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.http.client.ClientProtocolException;

public class App {


	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		//		Ocr.main(args);
		Path filepath = Paths.get(System.getProperty("user.home"), "public", "jb.pdf");
		File file = new File(filepath.toString());
		String path = file.getAbsolutePath();
		//		String dateStr = new Date().toString().replace(" ", "");
		String dateStr = "test";
		Path filepath1 = Paths.get(System.getProperty("user.home"), "public", "temp2-" + dateStr + ".pdf");
		Path filepath2 = Paths.get(System.getProperty("user.home"), "public", "temp3-" + dateStr + ".pdf");
		Path filepath3 = Paths.get(System.getProperty("user.home"), "public", "temp4-" + dateStr + ".pdf");
		String cmd1 = "pdftk " + path + " output " + filepath1.toString() + " uncompress";
		String cmd2 = "sed -e 's/EBanking/ /' " + filepath1.toString() +" > " + filepath2.toString();
		String cmd3 = "pdftk " + filepath2.toString() +" output " + filepath3.toString() + " compress";
		String [] cmd = new String[] {
				"bash", "-c", cmd2 
		};
		try{
			Process p1 = Runtime.getRuntime().exec(cmd1);
			p1.waitFor();
			Process p2 = Runtime.getRuntime().exec(cmd);
			p2.waitFor();
			Process p3 = Runtime.getRuntime().exec(cmd3);
			p3.waitFor();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		

//		System.out.println(filepath1.toString());
//		System.out.println(filepath2.toString());
//		System.out.println(filepath3.toString());

	}



}
