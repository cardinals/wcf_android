package com.mushapi.wcf;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONStringer;

import android.R.string;
import android.util.Log;

public class WebClient
{

	private String encode;
	private HttpClient httpClient;
	private HttpParams httpParams;
	private int timeout;
	private int bufferSize;
	private static final String TAG = "WebClient";

	public WebClient()
	{
		timeout = 20 * 1000;
		bufferSize = 8192;
		encode = "UTF-8";
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
		HttpConnectionParams.setSoTimeout(httpParams, timeout);
		HttpConnectionParams.setSocketBufferSize(httpParams, bufferSize);
		HttpClientParams.setRedirecting(httpParams, true);
		httpClient = new DefaultHttpClient(httpParams);
	}

	public String doGet(String url) throws Exception
	{
		return doGet(url, null);
	}

	public String doPost(String url) throws Exception
	{
		return doPost(url, null);
	}

	public String doGet(String url, Map<String, String> params)
			throws Exception
	{
		// ���QueryString
		String paramStr = "";
		if (params != null)
		{
			Iterator<Entry<String, String>> iter = params.entrySet().iterator();
			while (iter.hasNext())
			{
				Entry<String, String> entry = (Entry<String, String>) iter
						.next();
				paramStr += "&" + entry.getKey() + "="
						+ URLEncoder.encode(entry.getValue(), encode);
			}
			if (paramStr.length() > 0)
				paramStr.replaceFirst("&", "?");
			url += paramStr;
		}
		// ����HttpGet����
		HttpGet get = new HttpGet(url);
		Log.v(TAG, "HttpGet URL" + url);
		try
		{
			String strResp = "";
			// ��������
			Log.v(TAG, "doGet:" + url);
			HttpResponse resp = httpClient.execute(get);
			if (resp.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK)
				strResp = EntityUtils.toString(resp.getEntity());
			else
				// ������ص�StatusCode����OK�����쳣
				throw new Exception("Error Response:"
						+ resp.getStatusLine().toString());
			return strResp;
		} finally
		{
			get.abort();
		}
	}

	public String doPost(String url, Map<String, String> params)
			throws Exception
	{
		// POST������װ
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		if (params != null)
		{
			Iterator<Entry<String, String>> iter = params.entrySet().iterator();
			while (iter.hasNext())
			{
				Entry<String, String> entry = (Entry<String, String>) iter
						.next();
				data.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
		}
		HttpPost post = new HttpPost(url);
		try
		{
			// �������������������
			if (params != null)
				post.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));
			// ��������
			Log.v(TAG, "doPost: " + url);
			Log.v(TAG, "HttpPost: " + post);
			Log.v(TAG, "data: " + data.toString());
			HttpResponse resp = httpClient.execute(post);
			String strResp = "";
			if (resp.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK)
			{
				strResp = EntityUtils.toString(resp.getEntity());
				Log.v(TAG, "strResp: " + strResp);
			}

			else
				// ������ص�StatusCode����OK�����쳣
				throw new Exception("Error Response:"
						+ resp.getStatusLine().toString());
			return strResp;
		} finally
		{
			post.abort();
		}
	}

	/**
	 * @param url
	 *            - ��Ҫ���ʵ�address
	 * @param data
	 *            - Request�������ַ���
	 * @param contentType
	 *            - Request��ContentType
	 * @return Response���ַ���
	 * @throws Exception
	 */
	public String doPost(String url, String data, String contentType)
			throws Exception
	{
		HttpPost post = new HttpPost(url);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");
		try
		{
			JSONStringer jstring = new JSONStringer().object().key("sql")
					.value("���ү").endObject();

			// �������������������
			StringEntity se = new StringEntity(jstring.toString(), HTTP.UTF_8);
			se.setContentType(contentType);
			post.setEntity(se);
			// ��������
			Log.v(TAG, "doPost: " + url);
			Log.v(TAG, "HttpPost: " + post);
			Log.v(TAG + "  data: ", data.toString());
			HttpResponse resp = httpClient.execute(post);
			String strResp = "";
			if (resp.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK)
				strResp = EntityUtils.toString(resp.getEntity());
			else
				// ������ص�StatusCode����OK�����쳣
				throw new Exception("Error Response:"
						+ resp.getStatusLine().toString());
			return strResp;
		} finally
		{
			post.abort();
		}
	}

	/**
	 * @param url
	 *            �����������ַ
	 * @param data
	 *            ���ݶ���
	 * @param contentType
	 *            Request��ContentType
	 * @return ���ص�json�ַ���
	 */
	public String doPost(String url, Data data, String contentType)
	{

		String strResp = "";
		HttpPost request = new HttpPost(url);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");

		// ��֯json
		JSONStringer vehicle;
		try
		{
			vehicle = new JSONStringer().object().key("data").object()
					.key("Name").value(data.getName()).key("age")
					.value(data.getAge()).key("array").value(data.getArray())
					.key("msg").value(data.getMsg()).endObject().endObject();
			StringEntity entity = new StringEntity(vehicle.toString());

			request.setEntity(entity);

			// ��WCF����������
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);
			// �ж��Ƿ�ɹ�
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK)
				strResp = EntityUtils.toString(response.getEntity());

			Log.d("WebInvoke", "Saving : "
					+ response.getStatusLine().getStatusCode());
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			request.abort();
		}

		// Reload plate numbers

		return strResp;

	}
}