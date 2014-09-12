package com.coco.android.bean;

import java.io.UnsupportedEncodingException;

import com.coco.android.MyApplication;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;
import com.easier.util.StringUtil;

/**
 * 初始化报文实体类
 * @author ShawnXiao
 *
 */
public class InitNode extends BaseNode{
	
	
	public static byte[] buildMsg(String val_11,String val_60,String val_61){
		InitNode isoMsg = new InitNode();
		try{
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.INIT_MTI);
			isoMsg.setMTI(new String(b_mti));
			//主位元表(即位图)
			String hex_bit_map = "2038000000000019";
//			byte[] b_map=MyUtil.hexStringToByte(MyApplication.INIT_MTI);
			byte[] b_map=MyUtil.hexStringToByte(hex_bit_map);
			isoMsg.setMainElement(b_map);
			//处理代码
			isoMsg.set(3,MyApplication.INIT_HANDLE_CODE);
			//系统流水号
			isoMsg.set(11,val_11);
			//当地交易时间w
			isoMsg.set(12,MyApplication.getCurrentTime());
			//当地交易日期
			isoMsg.set(13,MyApplication.getCurrentDate());
			//终端号
//			isoMsg.set(41,MyApplication.TERM_NUM);
//			//商户号
//			isoMsg.set(42,MyApplication.COMM_NUM);
			//附加数据
			isoMsg.set(60,val_60);
			//61域验证码
			isoMsg.set(61, val_61);
			
			byte[] byte_mac = MyApplication.getMsgCheckCode(isoMsg, MyApplication.DEFAULT_MAC_KEY);
			String mac = DigitalTrans.byte2hex(byte_mac);
			L.e("mac:", ""+mac);
			byte[] byte_mac2=MyUtil.hexStringToByte(new String(byte_mac));
			isoMsg.set(64,byte_mac2);
			
			byte[] request = RequestUtil.getBytesRequest(isoMsg);
			L.i("reuqest", ""+RequestUtil.printRequest(isoMsg));
			
			return request;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 解析初始化报文
	 * @param response
	 * 返回报文
	 * @return
	 */
	public static InitNode parseResponse(String response){
		InitNode node = new InitNode();
		if (response != null && response.length() > 0) {
			String header = response.substring(0, 8);
			node.setHeader(DigitalTrans.hexStringToString(header, 2));
			String MTI = response.substring(8, 12);
			node.setMTI(MTI);
			String mainElement = response.substring(12, 28);
			node.setMainElement(DigitalTrans.hexStringToByte(mainElement));
			String remain_data = response.substring(28);
			// 将位图转化为2进制
			String bit_str = MyStringUtil.hexToBin(mainElement);
			for (int i = 0; i < bit_str.length(); i++) {
				if (((bit_str.getBytes()[i]) == 49)) {
					int field = i + 1;
					// 截取第3域(处理代码)的信息
					if (field == 3) {
						String value = remain_data.substring(0, 12);
						node.set(field,
								DigitalTrans.hexStringToString(value, 2));
						remain_data = remain_data.substring(12);
					}
					// 截取第11域(系统流水号)的信息
					if (field == 11) {
						String value = remain_data.substring(0, 12);
						node.set(field,
								DigitalTrans.hexStringToString(value, 2));
						remain_data = remain_data.substring(12);
					}
					// 截取第12域(当地交易时间HHMMSS)的信息
					if (field == 12) {
						String value = remain_data.substring(0, 12);
						node.set(field,
								DigitalTrans.hexStringToString(value, 2));
						remain_data = remain_data.substring(12);
					}
					// 截取第13域(当地交易日期MMDD)的信息
					if (field == 13) {
						String value = remain_data.substring(0, 8);
						node.set(field,
								DigitalTrans.hexStringToString(value, 2));
						remain_data = remain_data.substring(8);
					}
					// 截取第39域(返回码)的信息
					if (field == 39) {
						String value = remain_data.substring(0, 4);
						node.set(field,
								DigitalTrans.hexStringToString(value, 2));
						remain_data = remain_data.substring(4);
					}
					// 截取第41域(终端号)的信息
					if (field == 41) {
						String value = remain_data.substring(0, 16);
						node.set(field,
								DigitalTrans.hexStringToString(value, 2));
						remain_data = remain_data.substring(16);
					}
					// 截取第42域(商户号)的信息
					if (field == 42) {
						String value = remain_data.substring(0, 30);
						System.out.println("value-42:"+value);
						node.set(field,
								DigitalTrans.hexStringToString(value, 2));
						remain_data = remain_data.substring(30);
					}
					// 截取第60域(保留)的信息
					// 注意也是LLLVAR格式
					if (field == 60) {
						String len1=remain_data.substring(0, 6);
						String len2=DigitalTrans.hexStringToString(len1, 2);
//						System.out.println("len2:"+len2);
						int len = Integer.parseInt(len2);
//						System.out.println("60-len:" + len);
						remain_data = remain_data.substring(6);
						if(len>0){
							String value = remain_data.substring(0, len * 2);
//							System.out.println("60-value:"+value);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(len * 2);
						}
					}
					
					//第63域
					if(field == 63){
						String len1=remain_data.substring(0, 6);
						String len2=DigitalTrans.hexStringToString(len1, 2);
						int len = Integer.parseInt(len2);
//						System.out.println("63-len:" + len);
						remain_data = remain_data.substring(6);
						if(len>0){
							String value = remain_data.substring(0, len * 2);
							byte[] data = DigitalTrans.hex2byte(value);
//							node.set(field,
//									DigitalTrans.hexStringToString(value, 2));
							try {
								node.set(field, new String(data,"GBK"));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							remain_data = remain_data.substring(len * 2);
						}
					}
					
					// 截取64域(信息验证码)BIN码
					if (field == 64) {
						String value = remain_data.substring(0, 16);
						node.set(field, value);
						remain_data = remain_data.substring(16);
					}
				}
			}
		}
		System.out.println("node.getDataString():" + node.toString());
		return node;
	}
	public static void main(String[] args) {
		String response = "30303734081020380000028000193939323030313033303734353039353135353131303530303030303030303032303134323031333131303530393531353530303431313737D67004D0891F3765";
		parseResponse(response);
	}
}
