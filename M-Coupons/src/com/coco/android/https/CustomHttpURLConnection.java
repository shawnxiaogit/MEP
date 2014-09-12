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
 * @Descriptio 通过HttpURLConection的Get方式来请求网络工具类
 * @author Shawn
 * @Time 2013-8-16  下午2:44:16
 */
public class CustomHttpURLConnection {
	private static String TAG = "CustomHttpURLConnection";
	
	private static HttpURLConnection conn;
	
	private static final String CHARSET_UTF8 = HTTP.UTF_8;
	private static final String CHARSET_GB2312 = "GB2312";
	
	/**
	 * 
	 * @Description 通过HttpURLConnection的Get方法访问网络
	 * @author Shawn
	 * @Time 2013-8-16  下午3:00:28
	 * @param url
	 * @param NameValuePairs 参数键值对
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
	 * @Description 通过HttpURLConnnection的Post方式来访问网络
	 * @author Shawn
	 * @Time 2013-8-16  下午3:32:46
	 * @param strUrl 请求地址
	 * @param nameValuePairs 请求参数键值对
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
			//设置是否从HttpURLConnection读取，默认为true
			conn.setDoInput(true);
			//设置是否向HttpURLConnection输出，因为这是Post请求,
			//参数要放在http正文里,默认为false
			conn.setDoOutput(true);
			//设置请求的方法为POST，默认为GET
			conn.setRequestMethod("POST");
			//Post请求不能使用缓存
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			
			//设置传送内容为可序列化的java对象
			//(如果不设置此项，在传送序列化对象时，当WEB服务器默认不是这种类型时,可能会报java.io.EOFException)
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
	 * @Description 将键值对参数组装成字符串形式
	 * @author Shawn
	 * @Time 2013-8-16  下午6:40:00
	 * @param param 键值对参数
	 * @return String 字符串返回值
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
