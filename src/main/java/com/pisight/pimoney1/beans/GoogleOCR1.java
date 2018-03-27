package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author kumar
 * This class tests the vision api
 *
 */
public class GoogleOCR1 {

	public static final String TABLE_ID					=	"PDF_TO_HTML";

	private static final String TABLE_OPENING			=	"<table id=\""+TABLE_ID+"\"><tbody><tr><td>";

	private static final String TABLE_CLOSING			=	"</td></tr></tbody></table>";

	private static final String LINE_SEPARATOR			=	"</td></tr><tr><td>";

	public static void main(String[] args) {
		// TODO Auto-generated method stub


		String result = getHTML(getFile("images","India-Travel-policy-Bajaj-Allianz-3", "jpg"));
		
		result = TABLE_OPENING + result + TABLE_CLOSING;
		
		System.out.println("page ->>> " + result);



	}

	public static String getHTML(File file){
		String ocrURL = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyC3K1d02FWbOeIMPYa_I7sFkOAsZmsbU0w";

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

		String json = "{\"requests\":[{\"image\":{\"content\":\"" + encodedBase64 + "\"},\"imageContext\":{\"languageHints\":[\"en\", \"mr\"]},\"features\":[{\"type\":\"TEXT_DETECTION\"}]}]}";
		try {
			HttpPost request = new HttpPost(ocrURL);
			StringEntity params = new StringEntity(json);
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			response = httpClient.execute(request);

			HttpEntity entity =  response.getEntity();

			in = new Scanner(entity.getContent());
			while (in.hasNext())
			{
				result += " " + in.next();

			}
			System.out.println("-->> " + result);

			
			JSONObject jsonObject = new JSONObject(result);
			JSONArray array = (JSONArray) jsonObject.get("responses");
			System.out.println("####################");
			JSONArray array1 = (JSONArray) array.getJSONObject(0).getJSONArray("textAnnotations");
			
			
			int py1 = 0;
			int py2 = 0;
			System.out.println("array length -> " + array1.length());
			TreeMap<Integer, String> map = new TreeMap<Integer, String>();
			for(int i = 1; i<array1.length();i++){
				
				JSONObject object = array1.getJSONObject(i);
				String text = object.getString("description");
				JSONArray vertices = object.getJSONObject("boundingPoly").getJSONArray("vertices");
				
				int y1 = (Integer) vertices.getJSONObject(0).get("y");
				int y2 = (Integer) vertices.getJSONObject(2).get("y");
				int x = (Integer) vertices.getJSONObject(0).get("x");
				
				
				
//				System.out.println("py1 :: " + py1 + "   py2 :: " + py2);
//				System.out.println("y1  :: " + y1 +  "    y2 :: " + y2);
				if((y1 >= py1 && y1 <= py2) || (y2 >= py1 && y2 <= py2) || (py1 >= y1 && py1 <= y2) || (py2 >= y1 && py2 <= y2)){
//					System.out.println("************************************");
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
//					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					
					Set<Integer> keys = map.keySet();
					for(Integer key:keys){
						resultText += " " + map.get(key);
					}
					resultText += LINE_SEPARATOR;
//					System.out.println("map -> " + map);
					map.clear();
					map.put(x, text);
				}
				py1 = y1;
				py2 = y2;
				
			}
			
			
//			System.out.println("result Text -> " + resultText);
			JSONObject desc = (JSONObject) array1.getJSONObject(0);
			result  = (String) desc.get("description");

			result  = result.replaceAll("\\n", LINE_SEPARATOR);
//			result  = TABLE_OPENING + result + TABLE_CLOSING;

//			System.out.println(result);

			EntityUtils.consume(entity);

			// handle response here...
		}catch (Exception ex) {
			// handle exception here
			System.out.println(ex.getMessage());
		} finally {
			httpClient.getConnectionManager().shutdown(); //Deprecated

		}

		return resultText;
	}

	private static File getFile(String dir, String name, String type) {
		// TODO Auto-generated method stub

		String fileName = dir + "/" + name + "." + type;
		System.out.println("FFFFFFFFFFFFFFFFFFFFFF  ::: " + fileName);

		Path p = Paths.get(System.getProperty("user.home"), "kumar", fileName);

		System.out.println("AAAAAAAAAAAA :: " + p.toString());

		return p.toFile();
	}

}
