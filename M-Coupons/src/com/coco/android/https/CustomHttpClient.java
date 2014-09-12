package com.coco.android.https;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.coco.android.R;
import com.coco.android.util.CommonLog;
import com.coco.android.util.LogFactory;

import android.content.Context;
import android.util.Log;

/**
 * 
 * @Descriptio Http请求客户端
 * @author Shawn
 * @Time 2013-8-15  下午4:01:12
 */
public class CustomHttpClient {
	
	private static String TAG = "CustomHttpClient";
	
	private static HttpClient customerHttpClient;
	
	private static final String CHARSET_UTF_8 = HTTP.UTF_8;
	
	private static final String CHARSET_GB2312 = "GB2312";
	
	private static CommonLog log = LogFactory.createLog();
	
	/**
	 * 
	 * @Description 创建HttpClient实例
	 * @author Shawn
	 * @Time 2013-8-15  下午5:16:42
	 * @param context 上下文
	 * @return HttpClient
	 * @exception exception
	 */
	private static synchronized HttpClient getHttpClient(Context context){
		if(null == customerHttpClient){
			//设置一些基本参数
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET_UTF_8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams.setUserAgent(params,
					"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
									+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			
			//超时设置
			/* 从连接池中去连接的超时时间*/
			ConnManagerParams.setTimeout(params, 1000);
			
			/* 连接超时 */
			int ConnectionTimeOut = 3000;
			if(!HttpUtils.isWifiDataEnable(context)){
				ConnectionTimeOut = 10000;
			}
			HttpConnectionParams.setConnectionTimeout(params, ConnectionTimeOut);
			
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(params, 4000);
			
			//设置HttpClient支持HTTP和HTTPS两种方式
			SchemeRegistry scheRegi = new SchemeRegistry();
			scheRegi.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			scheRegi.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			
			
			//使用线程安全的连接来管理HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, scheRegi);
			
			customerHttpClient = new DefaultHttpClient(conMgr,params);
			
		}
		return customerHttpClient;
	}
	
	/**
	 * 
	 * @Description 通过HttpClient的Get方式获取返回值
	 * @author Shawn
	 * @Time 2013-8-15  下午5:19:59
	 * @param context 上下文
	 * @param url 链接URL
	 * @param nameValuePair 携带参数键值对
	 * @return String 
	 * @exception exception
	 */
	public static String getFromWebByHttpClient(Context context,String url,
			NameValuePair... nameValuePairs) throws Exception{
		log.d("getFromWebByHttpClient  url="+url);
		
		try{
			StringBuilder sb = new StringBuilder();
			sb.append(url);
			if(nameValuePairs!=null&&nameValuePairs.length>0){
				sb.append("?");
				for(int i=0;i<nameValuePairs.length;i++){
					if(i>0){
						sb.append("&");
					}
					sb.append(String.format("%s=%s",
							nameValuePairs[i].getName(),
							nameValuePairs[i].getValue()));
				}
			}
			
			//HttpGet连接对象
			HttpGet httpRequest = new HttpGet(sb.toString());
			//取得HttpClient对象
			HttpClient httpClient = getHttpClient(context);
			//请求HttpClient，获取HttpResponse
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			//请求不成功抛出RunTimeException
			if(httpResponse.getStatusLine().getStatusCode()!=HttpStatus.SC_OK){
				throw new RuntimeException(context.getResources().
						getString(R.string.httpError));
			}
			return EntityUtils.toString(httpResponse.getEntity());
			
		}catch(ParseException e){
			throw new RuntimeException(context.getResources().
					getString(R.string.httpError));
		}catch(IOException e){
			log.e("IOException");
			e.printStackTrace();
			throw new RuntimeException(context.getResources().
					getString(R.string.httpError));
		}
		
	}
	
	/**
	 * 
	 * @Description HttpClient的Post方法
	 * @author Shawn
	 * @Time 2013-8-16  上午10:47:09
	 * @param context 上下文
	 * @param url 链接
	 * @param nameValuePairs 请求参数键值对
	 * @return String
	 * @exception exception
	 */
	public static String postFromWebByHttpClient(Context context,String url,
			NameValuePair... nameValuePairs){
		try{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if(nameValuePairs != null && nameValuePairs.length > 0){
				for(int i = 0;i < nameValuePairs.length ; i++){
					params.add(nameValuePairs[i]);
				}
			}
			UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params,
					CHARSET_UTF_8);
			
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(urlEncoded);
			HttpClient httpClient = getHttpClient(context);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
				throw new RuntimeException(context.getResources().
						getString(R.string.httpError));
			}
			HttpEntity httpEntity = httpResponse.getEntity();
			
			return (httpEntity==null) ? null : EntityUtils.toString(
					httpEntity,CHARSET_UTF_8);
		}catch(UnsupportedEncodingException e){
			Log.w(TAG, e.getMessage());
			return null;
		}catch(ClientProtocolException e){
			Log.w(TAG, e.getMessage());
			return null;
		}catch(IOException e){
			throw new RuntimeException(context.getResources().
					getString(R.string.httpError),e);
		}
	}
	
}
