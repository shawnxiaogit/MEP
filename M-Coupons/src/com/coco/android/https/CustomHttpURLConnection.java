package com.coco.android.https;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;

/**
 * 
 * @Descriptio ͨ��HttpURLConection��Get��ʽ���������繤����
 * @author Shawn
 * @Time 2013-8-16  ����2:44:16
 */
public class CustomHttpURLConnection {
	private static String TAG = "CustomHttpURLConnection";
	
	private static HttpURLConnection conn;
	
	private static final String CHARSET_UTF8 = HTTP.UTF_8;
	private static final String CHARSET_GB2312 = "GB2312";
	
	/**
	 * 
	 * @Description ͨ��HttpURLConnection��Get������������
	 * @author Shawn
	 * @Time 2013-8-16  ����3:00:28
	 * @param url
	 * @param NameValuePairs ������ֵ��
	 * @return String
	 * @exception exception
	 */
	public static String getFromWebByHttpUrlConnection(String strUrl,
			NameValuePair... NameValuePairs){
		String result = "";
		try{
			URL url = new URL(strUrl);
			
			conn = (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(4000);
			conn.setRequestProperty("accept", "*/*");
			conn.connect();
			
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while((line = br.readLine())!=null){
				result += line;
			}
			br.close();
			isr.close();
			is.close();
			conn.disconnect();
			
			return result;
		}catch(MalformedURLException e){
			Log.e(TAG, "getFromWebByHttpUrlConnection:"+e.getMessage());
			e.printStackTrace();
			return null;
		}catch(IOException e){
			Log.e(TAG, "getFromWebByHttpUrlConnection:"+e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 
	 * @Description ͨ��HttpURLConnnection��Post��ʽ����������
	 * @author Shawn
	 * @Time 2013-8-16  ����3:32:46
	 * @param strUrl �����ַ
	 * @param nameValuePairs ���������ֵ��
	 * @return String
	 * @exception exception
	 */
	public static String postFromWebByHttpURLConnection(String strUrl,
			NameValuePair... nameValuePairs){
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(nameValuePairs!=null&&nameValuePairs.length>0){
			for(int i=0;i<nameValuePairs.length;i++){
				params.add(new BasicNameValuePair(nameValuePairs[i].getName(),
						nameValuePairs[i].getValue()));
			}
		}
		
		String result = "";
		try{
			URL url = new URL(strUrl);
			conn = (HttpURLConnection)url.openConnection();
			//�����Ƿ��HttpURLConnection��ȡ��Ĭ��Ϊtrue
			conn.setDoInput(true);
			//�����Ƿ���HttpURLConnection�������Ϊ����Post����,
			//����Ҫ����http������,Ĭ��Ϊfalse
			conn.setDoOutput(true);
			//��������ķ���ΪPOST��Ĭ��ΪGET
			conn.setRequestMethod("POST");
			//Post������ʹ�û���
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			
			//���ô�������Ϊ�����л���java����
			//(��������ô���ڴ������л�����ʱ����WEB������Ĭ�ϲ�����������ʱ,���ܻᱨjava.io.EOFException)
			conn.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded");
			conn.connect();
			
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			String param = getParams(params);
			bw.write(param);
			bw.close();
			osw.close();
			os.close();
			
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @Description ����ֵ�Բ�����װ���ַ�����ʽ
	 * @author Shawn
	 * @Time 2013-8-16  ����6:40:00
	 * @param param ��ֵ�Բ���
	 * @return String �ַ�������ֵ
	 * @exception exception
	 */
	private static String getParams(List<NameValuePair> params) throws UnsupportedEncodingException{
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		if(params!=null && params.size()>0){
			for(NameValuePair nameValuePair:params){
				if(first)
					first = false;
				else
					sb.append("&");
				sb.append(URLEncoder.encode(nameValuePair.getName(), CHARSET_UTF8));
				sb.append("=");
				sb.append(URLEncoder.encode(nameValuePair.getValue(), CHARSET_UTF8));
			}
		}
		return sb.toString();
	}
}
