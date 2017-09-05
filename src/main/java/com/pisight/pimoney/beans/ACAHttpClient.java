package com.pisight.pimoney.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

public class ACAHttpClient{
	
	private String url = null;
	
	private String requestType = null;
	
	private JSONObject jsonObj = new JSONObject();
	
	private String contentType = null;
	
	private String stringData = null;
	
	public static final String REQUEST_TYPE_POST = "post";
	
	public static final String REQUEST_TYPE_GET = "get";
	
	public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
	
	List<Header> headers = new ArrayList<Header>();
	
	public static Logger logger = Logger.getLogger(ACAHttpClient.class);
	
	
	public ACAHttpClient(String url, String requestType) throws Exception{
		
		if(StringUtils.isEmpty(url)){
			throw new Exception("url is null");
		}
		
		if(!(requestType.equals(REQUEST_TYPE_GET) || requestType.equals(REQUEST_TYPE_POST))){
			throw new Exception("request type is not proper");
		}
		
		this.url = url;
		this.requestType = requestType;
	}
	
	
	@SuppressWarnings("unchecked")
	public void setDataField(String key, Object value){
		jsonObj.put(key, value);
	}
	
	public void setStringEntity(String data){
		stringData = data;
	}
	
	public void addHeader(String name, String value){
		Header header = new BasicHeader(name, value);
		headers.add(header);
	}
	
	
	public void setContentType(String contentType){
		this.contentType = contentType;
	}
	
	
	public String getResponse() throws Exception{
		
		if(requestType.equals(REQUEST_TYPE_POST)){
			return getResponseForPostRequest();
		}
		else if(requestType.equals(REQUEST_TYPE_GET)){
			return getResponseForGetRequest();
		}
		
		return null;
	}
	
	
	public String getResponseForGetRequest() throws ClientProtocolException, IOException{
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		HttpGet request = new HttpGet(url);
		
		for(Header header: headers){
			request.setHeader(header);
		}
		
		HttpResponse response = httpClient.execute(request);
		HttpEntity entity =  response.getEntity();
		
		Scanner in = new Scanner(entity.getContent());
		String result = "";
		while (in.hasNext())
		{
			result += " " + in.next();

		}
		if(in != null){
			in.close();
		}
		
		return result;
	}
	
	
	public String getResponseForPostRequest() throws Exception{
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(url);
		
		for(Header header: headers){
			request.setHeader(header);
		}
		
		if(stringData == null){
			stringData = jsonObj.toJSONString();
		}
//		ScriptLogger.writeInfo("string DATA ::: " + stringData);
		StringEntity params = new StringEntity(stringData);
		
		if(contentType == null){
			throw new Exception("content type is not set");
		}
		params.setContentType(contentType);
		request.setEntity(params);
		
		HttpResponse response = httpClient.execute(request);
		HttpEntity entity =  response.getEntity();
		
		Scanner in = new Scanner(entity.getContent());
		String result = "";
		while (in.hasNext())
		{
			result += " " + in.next();

		}
		if(in != null){
			in.close();
		}
		
		return result;
	}
	
	

}
