package com.coco.android.bean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;
import com.easier.util.StringUtil;

/**
 * 请求工具类
 * @author Shawn
 *
 */
public class RequestUtil {
	
	//请求报文成功
	public static final int REQUEST_SUCCESS = 10001;
	
	//请求报文失败
	public static final int REQUEST_FAIELD  = 10000;
	
	
	/**
	 * 获取消费金额
	 * 1、以分为单位
	 * 2、12个字节，不足前面补0
	 * @param sum
	 * 编辑框输入金额
	 * @return
	 * 以分为单位的12给字节金额
	 */
	public static String getSumOfConsume(String sum){
		Double d1 = Double.parseDouble(sum);
		Double t_sum = (d1*100);
		String str_sum = String.valueOf(t_sum);
		StringBuilder sb = new StringBuilder();
		int len = str_sum.length();
		if(len==1){
			sb.append("00000000000");
		}else if(len==2){
			sb.append("0000000000");
		}else if(len==3){
			sb.append("000000000");
		}else if(len==4){
			sb.append("00000000");
		}else if(len==5){
			sb.append("0000000");
		}else if(len==6){
			sb.append("000000");
		}else if(len==7){
			sb.append("00000");
		}else if(len==8){
			sb.append("0000");
		}else if(len==9){
			sb.append("000");
		}else if(len==10){
			sb.append("00");
		}else if(len==11){
			sb.append("00");
		}
		sb.append(str_sum);
		
		return sb.toString();
	}
	/**
	 * 获取消费金额
	 * 1、以分为单位
	 * 2、12个字节，不足前面补0
	 * @param sum
	 * 编辑框输入金额
	 * @return
	 * 以分为单位的12个字节金额
	 */
	public static String getSumOfConsume2(String sum){
		System.out.println("sum:"+sum);
		DecimalFormat format = new DecimalFormat(".00");
		BigDecimal money = ((BigDecimal.valueOf(Double.valueOf(sum))));
		System.out.println("money:"+money);
//		String str4 = format.format(money);
//		System.out.println("str4:"+str4);
		
		double price = money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		L.e("price", ""+price);
		
		String str4 = format.format(price);
		L.e("str4:",""+str4);
		String[] mPrices= str4.split("\\.");
		StringBuilder sb3 = new StringBuilder();
		for(int i=0;i<mPrices.length;i++){
			sb3.append(mPrices[i]);
		}
		System.out.println("价钱:"+sb3.toString());
		String data = sb3.toString();
		
//		int i_sum = Integer.parseInt(sum);
//		int t_sum = (i_sum*100);
//		System.out.println("sum:"+sum);
//		Double double1 = Double.valueOf(sum);
//		System.out.println("double1:"+""+double1);
//		Double double2 = (double1*100);
//		System.out.println("double2:"+""+double2);
//		String str_sum = String.valueOf(double2);
//		String str_sum =double1.toString();
//		String str_sum  = String.valueOf(money);
//		System.out.println("金额："+str_sum);
//		String[] str = str_sum.split("\\.");
//		String data = str[0];
//		System.out.println("金额:"+data);
		StringBuilder sb = new StringBuilder();
		int len = data.length();
		if(len==1){
			sb.append("00000000000");
		}else if(len==2){
			sb.append("0000000000");
		}else if(len==3){
			sb.append("000000000");
		}else if(len==4){
			sb.append("00000000");
		}else if(len==5){
			sb.append("0000000");
		}else if(len==6){
			sb.append("000000");
		}else if(len==7){
			sb.append("00000");
		}else if(len==8){
			sb.append("0000");
		}else if(len==9){
			sb.append("000");
		}else if(len==10){
			sb.append("00");
		}else if(len==11){
			sb.append("00");
		}
		sb.append(data);
		return sb.toString();
	}
	
	/**
	 * 获取请求的报文长度
	 * @param isoMsg
	 * 请求报文实体类
	 * @return
	 */
	public static String getDataLen(BaseNode isoMsg){
		StringBuilder sb2 = new StringBuilder();
		String hex_data = isoMsg.getHexString();
		int len2 = hex_data.length();
		int rea_len = (len2/2);
		if(rea_len>99&&rea_len<999){
			sb2.append("0");
		}else if(rea_len>9&&rea_len<=99){
			sb2.append("00");
		}
		sb2.append(rea_len);
		return sb2.toString();
	}
	/**
	 * 获取报文总大小
	 * @param isoMsg
	 * 请求报文实体类
	 * @return
	 */
	public static int getDataSize(BaseNode isoMsg){
		int result = 0;
//		L.i("getDataIntLen", "getDataIntLen()");
		StringBuilder sb2 = new StringBuilder();
		String hex_data = isoMsg.getHexString();
//		L.i("hex_data", ""+hex_data.length());
		int len2 = hex_data.length();
		int rea_len = (len2/2);
		if(rea_len>99&&rea_len<999){
			sb2.append("0");
		}else if(rea_len>9&&rea_len<=99){
			sb2.append("00");
		}
		sb2.append(rea_len);
		sb2.append(hex_data);
		result = sb2.toString().length();
		return result;
	}
	
	/**
	 * 获取LLVAR格式的数据
	 * @param data
	 * 原始数据
	 * @return
	 */
	public static String getLLVARData(String data){
		StringBuilder sb = new StringBuilder();
		int len = data.length();
		if(len>0&&len<=9){
			sb.append("0");
		}
//		byte[] b_len = MyUtil.hexStringToByte(String.valueOf(len));
		
//		sb.append(new String(b_len));
		sb.append(len);
		sb.append(data);
		return sb.toString();
	}
	
	/**
	 * 获取LLLVAR格式的数据
	 * @param data
	 * 原始数据
	 * @return
	 */
	public static String getLLLVARData(String data){
		StringBuilder sb = new StringBuilder();
		int len = data.length();
		if(len>9&&len<=99){
			sb.append("00");
		}else if(len>99&&len<=999){
			sb.append("0");
		}
		sb.append(len);
		sb.append(data);
		return sb.toString();
	}
	
	/**
	 * 打印请求信息
	 * @param isoMsg
	 * @return
	 */
	public static String printRequest(BaseNode isoMsg){
		StringBuilder sb = new StringBuilder();
		String str_len=DigitalTrans.stringToHexString(getDataLen(isoMsg));
		sb.append(str_len);
		sb.append(isoMsg.getHexString());
		return sb.toString();
	}
	/**
	 * 获取请求报文的字节数组
	 * @return
	 */
	public static byte[] getBytesRequest(BaseNode isoMsg){
		int dataSize = RequestUtil.getDataSize(isoMsg);
//		System.out.println("dataSize:"+dataSize);
		byte[] request = new byte[dataSize*2];
		int count =0;
		String header=RequestUtil.getDataLen(isoMsg);
		for(int i=0;i<header.getBytes().length;i++){
			request[count]=header.getBytes()[i];
			count++;
		}
		String MTI = isoMsg.getMTI();
		for(int i=0;i<MTI.getBytes().length;i++){
			request[count]=MTI.getBytes()[i];
			count++;
		}
		byte[] mainElement = isoMsg.getMainElement();
		for(int i=0;i<mainElement.length;i++){
			request[count]=mainElement[i];
			count++;
		}
		TreeMap<Integer,Object> dataElements = isoMsg.getDataElements();
		Iterator iter = dataElements.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry e = (Map.Entry)iter.next();
			int key=Integer.parseInt(e.getKey().toString());
//			if(key==52){
//				byte[] value =(byte[]) e.getValue();
//				for(int i=0;i<value.length;i++){
//					request[count]=value[i];
//					count++;
//				}
//			}else{
			if(key==64||key==52){
				byte[] mac = (byte[]) e.getValue();
				for(int i=0;i<mac.length;i++){
					request[count]=mac[i];
					count++;
				}
			}else{
				String value = e.getValue().toString();
				if(value!=null&&value.length()>0){
					for(int i=0;i<value.getBytes().length;i++){
						request[count]=value.getBytes()[i];
						count++;
					}
				}
			}
//			}
			
		}
		
		return request;
	}
	
	
	/**
	 * 获取请求报文的字节数组
	 * @return
	 */
	public static byte[] getBytesRequest2(BaseNode isoMsg){
		int dataSize = RequestUtil.getDataSize(isoMsg);
//		System.out.println("dataSize:"+dataSize);
		byte[] request = new byte[dataSize*2];
		int count =0;
		String header=RequestUtil.getDataLen(isoMsg);
		for(int i=0;i<header.getBytes().length;i++){
			request[count]=header.getBytes()[i];
			count++;
		}
		String MTI = isoMsg.getMTI();
		for(int i=0;i<MTI.getBytes().length;i++){
			request[count]=MTI.getBytes()[i];
			count++;
		}
		byte[] mainElement = isoMsg.getMainElement();
		for(int i=0;i<mainElement.length;i++){
			request[count]=mainElement[i];
			count++;
		}
		TreeMap<Integer,Object> dataElements = isoMsg.getDataElements();
		Iterator iter = dataElements.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry e = (Map.Entry)iter.next();
			int key=Integer.parseInt(e.getKey().toString());
//			if(key==52){
//				byte[] value =(byte[]) e.getValue();
//				for(int i=0;i<value.length;i++){
//					request[count]=value[i];
//					count++;
//				}
//			}else{
			if(key==64||key==52||key==61){
				byte[] mac = (byte[]) e.getValue();
				for(int i=0;i<mac.length;i++){
					request[count]=mac[i];
					count++;
				}
			}else{
				String value = e.getValue().toString();
				if(value!=null&&value.length()>0){
					for(int i=0;i<value.getBytes().length;i++){
						request[count]=value.getBytes()[i];
						count++;
					}
				}
			}
//			}
			
		}
		
		return request;
	}
	
	
	/**
	 * 获取请求报文的字节数组
	 * @return
	 */
	public static byte[] getMacBytes(BaseNode isoMsg){
		int dataSize = RequestUtil.getDataSize(isoMsg);
		System.out.println("dataSize:"+dataSize);
		byte[] request = new byte[dataSize*2];
		int count =0;
		String MTI = isoMsg.getMTI();
		for(int i=0;i<MTI.getBytes().length;i++){
			request[count]=MTI.getBytes()[i];
			count++;
		}
		byte[] mainElement = isoMsg.getMainElement();
		for(int i=0;i<mainElement.length;i++){
			request[count]=mainElement[i];
			count++;
		}
		TreeMap<Integer,Object> dataElements = isoMsg.getDataElements();
		Iterator iter = dataElements.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry e = (Map.Entry)iter.next();
			int key=Integer.parseInt(e.getKey().toString());
			if(key!=64){
				if(key==52){
					byte[] b_pwd = (byte[]) e.getValue();
					for(int i=0;i<b_pwd.length;i++){
						request[count]=b_pwd[i];
						count++;
					}
				}else{
					
				
					String value = e.getValue().toString();
					if(value!=null&&value.length()>0){
						for(int i=0;i<value.getBytes().length;i++){
							request[count]=value.getBytes()[i];
							count++;
						}
					}
				}
			}
			
		}
		
		return request;
	}
	
	/**
	 * 获取请求报文的字节数组
	 * @return
	 */
	public static int getMacLen(BaseNode isoMsg){
		int count =0;
		String MTI = isoMsg.getMTI();
		if(MTI!=null&&MTI.length()>0){
			for(int i=0;i<MTI.getBytes().length;i++){
				count++;
			}
		}
		byte[] mainElement = isoMsg.getMainElement();
		if(mainElement!=null&&mainElement.length>0){
			for(int i=0;i<mainElement.length;i++){
				count++;
			}
		}
		TreeMap<Integer,Object> dataElements = isoMsg.getDataElements();
		Iterator iter = dataElements.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry e = (Map.Entry)iter.next();
			int key=Integer.parseInt(e.getKey().toString());
			if(key!=64){
				if(key==52){
					byte[] b_pwd = (byte[]) e.getValue();
					for(int i=0;i<b_pwd.length;i++){
						count++;
					}
				}else{
					String value = e.getValue().toString();
					if(value!=null&&value.length()>0){
						for(int i=0;i<value.getBytes().length;i++){
							count++;
						}
					}
				}
			}
			
		}
		
		return count;
	}
	
	/**
	 * 获取加入密文长度的报文
	 * @param datas
	 * 密文
	 * @return
	 */
	public static byte[] getMiWenData4(int len,byte[] datas){
		L.e("getMiWenData4", "getMiWenData4");
		L.e("len:", ""+len);
		byte[] one_zeor= "0".getBytes();
		byte[] two_zeros = "00".getBytes();
//		StringBuilder sb = new StringBuilder();
	
		if(len>0&&len<=99){
			L.e("len>0&&len<=99", ""+(len>0&&len<=99));
			byte[] mLen = MyUtil.byteMerger(two_zeros, String.valueOf(len).getBytes());
			L.e("mLen:", ""+new String(mLen));
			return MyUtil.byteMerger(mLen, datas);
		}else if(len>99&&len<=999){
			L.e("len>99&&len<=999", ""+(len>99&&len<=999));
			byte[] mLen = MyUtil.byteMerger(one_zeor, String.valueOf(len).getBytes());
			L.e("mLen:", ""+new String(mLen));
			return MyUtil.byteMerger(mLen, datas);
		}else if(len>999){
			L.e("len>999:", ""+(len>999));
			return MyUtil.byteMerger(String.valueOf(len).getBytes(), datas);
		}
		
		return null;
		
	}
	/**
	 * 获取加入密文长度的报文
	 * @param datas
	 * 密文
	 * @return
	 */
	public static byte[] getMiWenData(byte[] datas){
		L.e("getMiWenData", "getMiWenData");
		int len = datas.length;
		String str_len = String.valueOf(datas.length);
		L.e("len:", ""+len);
		byte[] one_zeor= "0".getBytes();
		byte[] two_zeros = "00".getBytes();
		if(len>0&&len<=99){
			byte[] mLen = MyUtil.byteMerger(two_zeros, str_len.getBytes());
			L.e("mLen:", ""+new String(mLen));
			return MyUtil.byteMerger(mLen, datas);
		}else if(len>99&&len<=999){
			byte[] mLen = MyUtil.byteMerger(one_zeor, str_len.getBytes());
			L.e("mLen:", ""+new String(mLen));
			return MyUtil.byteMerger(mLen, datas);
		}else{
			byte[] mLen = str_len.getBytes();
			L.e("mLen:", ""+new String(mLen));
			return MyUtil.byteMerger(mLen, datas);
		}
		
	}
	
	//这才是最地道的 
	public static boolean isNumeric(String str){ 
		try { 
			Integer.parseInt(str); 
			return true; 
		} catch (NumberFormatException e) { 
			return false; 
		} 
	}
}
