package com.anibug.smsmanager.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


public class WebClient {
	private HttpClient client = new DefaultHttpClient();
	
	private final String protocol = "http";
	
	@SuppressWarnings("unused")
	private final String host;
	@SuppressWarnings("unused")
	private final int port;
	
	private final String url_save;
	private final String url_list;
	
	public WebClient(String host, int port) {
		URL url = null;
		try {
			url = new URL(protocol, host, port, "/messages");
		} catch (MalformedURLException e) {
			// FIXME: Add to report.
		}
		this.host = host;
		this.port = port;
		if (url == null)
			url_list = "";
		else
			url_list = url.toString();
		url_save = url_list;
	}

	public List<Message> getList() {
		HttpGet req = new HttpGet(url_list);
		
		//TODO: response content -> json -> list<message>. 
		return null;
	}
	
	public boolean save(Message message) {
		HttpPost post = new HttpPost(url_save);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("message[content]", message.getContent()));
		params.add(new BasicNameValuePair("message[phone_number]", message.getPhoneNumber()));
		params.add(new BasicNameValuePair("message[created_at]", message.getDateCreated().toGMTString()));
		params.add(new BasicNameValuePair("message[hash]", ""));

		try {
			post.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e) {
			// TODO: Add to report.
			return false;
		}

		try {
			client.execute(post);
		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		return true;
	}
}
