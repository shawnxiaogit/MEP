package com.coco.android.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Message;

import com.ajrd.SafeSoft;
import com.coco.android.MyApplication;
import com.coco.android.bean.RequestUtil;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyUtil;

/**
 * Socket通讯类
 * @author Shawn
 *
 */
public class SocketClient extends Thread{
	private String mIp;
	private int mPort;
	private Socket client;
	private InputStream is;
	private OutputStream os;
	private byte[] mRequest;
	private String hexResponse;
	private Handler mHandler;
	public SocketClient(String ip,int port,byte[] request,Handler handler){
		mIp=ip;
		mPort=port;
//		mRequest = request;
		//==============这里对全报文，进行加密==============

		byte[] datas = MyUtil.subBytes(request,4,request.length-4);
		int len=MyUtil.getMyDataLen(Integer.parseInt(new String(MyUtil.cutOutByte(request,4))));
		byte[] len2 = MyUtil.subBytes(request,0,4);
		int len3 = Integer.parseInt(new String(len2));
		
		byte[] mydatas = SafeSoft.EncryptMsg(datas,len3);
		mRequest = RequestUtil.getMiWenData4(len,mydatas);
		
		
		//==============这里对全报文，进行加密==============
		mHandler = handler;
		client = new Socket();
		L.e("mRequest密文:", DigitalTrans.byte2hex(mRequest));
	}
	
	public SocketClient(byte[] request,Handler handler){
		mIp= MyApplication.IP;
		mPort = MyApplication.PORT;
//		mRequest = request;
		//==============这里对全报文，进行加密==============

		byte[] datas = MyUtil.subBytes(request,4,request.length-4);
		int len=MyUtil.getMyDataLen(Integer.parseInt(new String(MyUtil.cutOutByte(request,4))));
		byte[] len2 = MyUtil.subBytes(request,0,4);
		int len3 = Integer.parseInt(new String(len2));
		System.out.println("request:"+DigitalTrans.byte2hex(request));
		System.out.println("datas:"+DigitalTrans.byte2hex(datas));
		System.out.println("len3:"+len3);
		byte[] mydatas = SafeSoft.EncryptMsg(datas,len3);
		System.out.println("mydatas:"+DigitalTrans.byte2hex(mydatas));
		mRequest = RequestUtil.getMiWenData4(len,mydatas);
		
		
		//==============这里对全报文，进行加密==============
		mHandler = handler;
		client = new Socket();
		L.e("mRequest密文:", DigitalTrans.byte2hex(mRequest));
		
	}
	
	@Override
	public void run() {
		try {
			System.out.println("SocketClient-----run()");
			InetSocketAddress addr = new InetSocketAddress(mIp, mPort);
//			client = new Socket(mIp, mPort);
			client.connect(addr, 60000);
			if(client.isConnected()&&!client.isInputShutdown()){
				os = client.getOutputStream();
				is = client.getInputStream();
				os.write(mRequest);
				os.flush();
				Thread.sleep(2000);
				//---------这里需要改，如果没有返回的时候，会无限循环的---
				int len=0;
				while(len==0){
					len= is.available();
				}
				byte[] bytes=new byte[len];
				is.read(bytes);
				//======================这里进行全报文解密=============================
				System.out.println("response密文："+DigitalTrans.byte2hex(bytes));
				//密文长度
				byte[] b_ret_miwen_len = MyUtil.subBytes(bytes,0,4);
				//密文长度，整数
				int int_ret_miwen_len = Integer.parseInt(new String(b_ret_miwen_len));
				L.e("int_ret_miwen_len:", ""+int_ret_miwen_len);
				//返回密文数据，不包含长度
				byte[] b_ret_miwen_datas = MyUtil.subBytes(bytes,4,bytes.length-4);
				//解密后的返回报文，不包含长度
				byte[] m_ret_minwen_datas = SafeSoft.DecryptMsg(b_ret_miwen_datas, int_ret_miwen_len);
				
				//解密后的返回报文，加上报文长度
				byte[] m_ret_minwen_datas_append_header_len = RequestUtil.getMiWenData(m_ret_minwen_datas);
				//--------------------
				String hex_response = DigitalTrans.byte2hex(m_ret_minwen_datas_append_header_len);
				System.out.println("response明文:"+hex_response);
				//======================这里进行全报文解密=============================
				//如果成功返回，将返回至传至界面处理
				if(hex_response!=null&&hex_response.length()>0){
					Message message = new Message();
					message.what = RequestUtil.REQUEST_SUCCESS;
					message.obj = hex_response;
					mHandler.sendMessage(message);
				}else{
					//没有返回这，发送获取数据失败，到界面处理
					Message message = new Message();
					message.what = RequestUtil.REQUEST_FAIELD;
					mHandler.sendMessage(message);
				}
			
			}
		}catch(SocketTimeoutException e){
			e.printStackTrace();
			Message message = new Message();
			message.what = RequestUtil.REQUEST_FAIELD;
			message.obj = "连接超时";
			mHandler.sendMessage(message);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Message message = new Message();
			message.what = RequestUtil.REQUEST_FAIELD;
			message.obj = "服务器为开启，或则其他原因连不上服务器";
			mHandler.sendMessage(message);
			
		} catch (IOException e) {
			e.printStackTrace();
			Message message = new Message();
			message.what = RequestUtil.REQUEST_FAIELD;
			message.obj = "连接异常";
			mHandler.sendMessage(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Message message = new Message();
			message.what = RequestUtil.REQUEST_FAIELD;
			message.obj = e.getMessage();
			mHandler.sendMessage(message);
		} 
		finally{
			try{
				if(client!=null){
					os.close();
					is.close();
					client.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void setResponse(String hexString){
		hexResponse = hexString;
	}
	public String getResponse(){
		return hexResponse;
	}
}
