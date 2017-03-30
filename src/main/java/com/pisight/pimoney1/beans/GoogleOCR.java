package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GoogleOCR {

	private static final String LINE_SEPARATOR			=	"</td></tr><tr><td>";
	
	private static final String API_KEY = "AIzaSyDhSGVyNV5u86n7CTOvDhgC59TJFYTvVSI";
	
	private static Logger LOGGER = Logger.getLogger(GoogleOCR.class.getName());


	public static String getHTML(File file){
		
		LOGGER.info("preparing request object for OCR request to google");
		LOGGER.info(" @@@@@@@@@@@@@@@@@@@   file Name ::: " + file.getName() + "  @@@@@@@@@@@@@@@@@@@@@@@@");
		String ocrURL = "https://vision.googleapis.com/v1/images:annotate?key="+ API_KEY;

		String result = "";
		String resultText = "";
		HttpClient httpClient = new DefaultHttpClient();
		Scanner in = null;
		HttpResponse response = null;
		File originalFile = file;
		String encodedBase64 = null;
		try {
			FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
			byte[] bytes = new byte[(int)originalFile.length()];
			fileInputStreamReader.read(bytes);
			encodedBase64 = new String(Base64.encodeBase64(bytes));
			fileInputStreamReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String json = "{\"requests\":[{\"image\":{\"content\":\"" + encodedBase64 + "\"},\"features\":[{\"type\":\"TEXT_DETECTION\"}]}]}";
		try {
			HttpPost request = new HttpPost(ocrURL);
			StringEntity params = new StringEntity(json);
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			response = httpClient.execute(request);
			LOGGER.info("OCR response recieved");
			HttpEntity entity =  response.getEntity();

			in = new Scanner(entity.getContent());
			while (in.hasNext())
			{
				result += " " + in.next();

			}

			System.out.println("result -> " + result);
			JsonObject jsonObject = new JsonObject(result);
			JsonArray array = (JsonArray) jsonObject.get("responses");
			JSONArray array1 = (JSONArray) array.getJSONObject(0).getJSONArray("textAnnotations");
			
			
			int py1 = 0;
			int py2 = 0;
			TreeMap<Integer, String> map = new TreeMap<Integer, String>();
			for(int i = 1; i<array1.length();i++){
				
				JSONObject object = array1.getJSONObject(i);
				String text = object.getString("description");
				JSONArray vertices = object.getJSONObject("boundingPoly").getJSONArray("vertices");
				
				int y1 = (Integer) vertices.getJSONObject(0).get("y");
				int y2 = (Integer) vertices.getJSONObject(2).get("y");
				int x = (Integer) vertices.getJSONObject(0).get("x");
				
				if((y1 >= py1 && y1 <= py2) || (y2 >= py1 && y2 <= py2) || (py1 >= y1 && py1 <= y2) || (py2 >= y1 && py2 <= y2)){
					if(y1>=py1 && y1 <= py2){
						int d1 = y2-y1;
						int d2 = py2-y1;
						int percent = d2*100/d1;
						if(percent < 50){
							Set<Integer> keys = map.keySet();
							for(Integer key:keys){
								resultText += " " + map.get(key);
							}
							resultText += LINE_SEPARATOR;
							map.clear();
							map.put(x, text);
							py1 = y1;
							py2 = y2;
							continue;
						}
					}
					map.put(x, text);
				}
				else{
					Set<Integer> keys = map.keySet();
					for(Integer key:keys){
						resultText += " " + map.get(key);
					}
					resultText += LINE_SEPARATOR;
					map.clear();
					map.put(x, text);
				}
				py1 = y1;
				py2 = y2;
				
			}

		}catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			httpClient.getConnectionManager().shutdown(); //Deprecated

		}
		LOGGER.info("response parsed");
		return resultText;
	}

}
