package com.anibug.smsmanager.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebClient {
	private final HttpClient client = new DefaultHttpClient();
	private final MessageManager manager;
	private final String protocol = "http";

	@SuppressWarnings("unused")
	private final String host;
	@SuppressWarnings("unused")
	private final int port;

	private final String url_save;
	private final String url_list;

	public WebClient(String host, int port, MessageManager manager) {
		this.manager = manager;
		URL url = null;
		try {
			url = new URL(protocol, host, port, "/messages.json");
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
		BufferedReader in;
		try {
			HttpGet request = new HttpGet(url_list);
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		List<Message> messages = new ArrayList<Message>();
		String line;
		try {
			while ((line = in.readLine()) != null) {
				JSONArray ja = new JSONArray(line.replace("\\r", ""));

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					JSONObject postJson = jo.getJSONObject("message");
					messages.add(toMessage(postJson));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return messages;
	}

	private Message toMessage(JSONObject postJson) throws JSONException {
		Message message = new Message(
				postJson.getString("phone_number"),
				new Date(postJson.getString("created_at")),
				postJson.getString("content"),
				postJson.getInt("status"),
				postJson.getInt("online_id"));
		return message;
	}

	public boolean save(Message message) {
		HttpPost post = new HttpPost(url_save);

		List<NameValuePair> params = toParameters(message);

		try {
			post.setEntity(new UrlEncodedFormEntity(params));
            post.setHeader("Accept", "application/json");
		} catch (UnsupportedEncodingException e) {
			// TODO: Add to report.
			return false;
		}

		try {
			HttpResponse response = client.execute(post);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			
			String line = in.readLine();
            if (line == null)
                return false;

            JSONObject jo = new JSONObject(line.replace("\\r", ""));
            JSONObject messageJson = jo.getJSONObject("message");

            // "online_id" in json is the server unique id for this message.
            message.setOnlineId(messageJson.getInt("online_id"));
            manager.save(message);

        } catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private List<NameValuePair> toParameters(Message message) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("content", message.getContent()));
		params.add(new BasicNameValuePair("phone_number", message.getPhoneNumber()));
		params.add(new BasicNameValuePair("date_created", message.getDateCreated().toGMTString()));
        params.add(new BasicNameValuePair("status", String.valueOf(message.getStatus())));
        return params;
	}
}
