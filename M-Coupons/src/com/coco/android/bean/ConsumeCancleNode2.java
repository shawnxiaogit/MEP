package com.coco.android.bean;

import com.ajrd.SafeSoft;
import com.coco.android.MyApplication;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;

/**
 * 消费撤销实体类
 * @author ShawnXiao
 *
 */
public class ConsumeCancleNode2 extends BaseNode{
	
	//组件消费撤销报文
	/**
	 * 
	 * @param val_2
	 * 卡号
	 * @param val_4
	 * 金额
	 * @param val_11
	 * 流水号
	 * @param val_12
	 * 交易时间
	 * @param val_13
	 * 交易日期
	 * @param val_52
	 * 密码
	 * @param val_60
	 * 11位手机号+15位SIM卡标识+14位手机设备的标识 
	 * @return
	 */
	public static byte[] buildConsumeCancleNode(String val_2,String val_4,String val_11,
			String val_12,String val_13,String val_52,String val_60,String consume_date){
		ConsumeCancleNode2 isoMsg = new ConsumeCancleNode2();
		try{
			//信息类型
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.CONSUME_CANCLE_MTI);
			isoMsg.setMTI(new String(b_mti));
			//主位元素(即位图)
			String hex_bit_map = "7038040000C01015";
			byte[] b_map = MyUtil.hexStringToByte(hex_bit_map);
			isoMsg.setMainElement(b_map);
			//第2域主账号
			isoMsg.set(2, RequestUtil.getLLVARData(val_2));
			//第3域处理码
			isoMsg.set(3, MyApplication.CONSUME_CANCLE_HANDLE_COND);
			//第4域交易金额，以分为单位，12字节长度，不足，前面补0
			isoMsg.set(4, RequestUtil.getSumOfConsume2(val_4));
			//第11域流水号
			isoMsg.set(11, val_11);
			//第12域当地交易时间
			isoMsg.set(12, val_12);
			//第13域当地交易日期
			isoMsg.set(13, val_13);
			//第22域POS输入方式
			isoMsg.set(22, MyApplication.POS_INPUT_TYPE);
			//第41域终端号
			isoMsg.set(41, MyApplication.TERM_NUM);
			//第42域商户号
			isoMsg.set(42, MyApplication.COMM_NUM);
			
			
			byte[] consume_key = SafeSoft.EncryptPin(DigitalTrans.byte2hex(MyApplication.PIN_KEY).getBytes(), val_52.getBytes(), val_2.getBytes());
			byte[] byte_pwd = MyUtil.hexStringToByte(new String(consume_key));
			isoMsg.set(52, byte_pwd);
			
//			System.out.println("PIN_KEY:"+DigitalTrans.byte2hex(MyApplication.PIN_KEY));
//			System.out.println("密码明文："+val_52);
//			System.out.println("卡号:"+val_2);
//			System.out.println("密码密文:"+new String(consume_key));
			
			//第60域附加数据
			isoMsg.set(60,val_60);
			L.e("val_60:", ""+val_60);
			
			//第62域票据号
			isoMsg.set(62, MyApplication.get62Value3(val_11,consume_date));
			L.e("MyApplication.get62Value()", ""+MyApplication.get62Value(val_11));
			
			
			byte[] data = RequestUtil.getMacBytes(isoMsg);
			int len = RequestUtil.getMacLen(isoMsg);
			byte[] mac =  SafeSoft.GenerateMac(DigitalTrans.byte2hex(MyApplication.MAC_KEY).getBytes(), len, data);
			byte[] byte_mac =MyUtil.hexStringToByte(new String(mac));
			//第64域信息验证码
			isoMsg.set(64,byte_mac);
			
//			System.out.println("MAC_KEY:"+DigitalTrans.byte2hex(MyApplication.MAC_KEY));
//			System.out.println("长度:"+len);
//			System.out.println("数据:"+isoMsg.getMacHexString());
//			System.out.println("MAC:"+new String(mac));
			byte[] request = RequestUtil.getBytesRequest(isoMsg);
//			L.i("request:", isoMsg.getDataString());
			L.i("reuqest_hex", ""+RequestUtil.printRequest(isoMsg));
			
			return request;
			
		}catch(Exception e){
			
		}
		
		return null;
	}
	
	
	public static ConsumeCancleNode2 parseMsg(String response){
		ConsumeCancleNode2 node = new ConsumeCancleNode2();
		try{
			if(response!=null&&response.length()>0){
				//报文头，及长度
				String header = response.substring(0, 8);
				node.setHeader(DigitalTrans.hexStringToString(header, 2));
				//消息类型
				String MTI = response.substring(8, 12);
				node.setMTI(MTI);
				//主位元素(即位图)
				String mainElement = response.substring(12, 28);
				node.setMainElement(DigitalTrans.hex2byte(mainElement));
				String remain_data = response.substring(28);
				System.out.println("remain_data:"+remain_data);
				//将位图转化为2进制
				String bit_str = MyStringUtil.hexToBin(mainElement);
				for(int i=0;i<bit_str.length();i++){
					//二进制的值为1的AscII值，则表示该域有
					if(bit_str.getBytes()[i]==49){
						//域为i+1
						int field = i+1;
						//第2域主帐号
						if(field == 2){
							//前4位为长度
							String len1=remain_data.substring(0, 4);
//							System.out.println("len1:"+len1);
							String len2 = DigitalTrans.hexStringToString(len1, 2);
//							System.out.println("len2:"+len2);
							int len = Integer.parseInt(len2);
							remain_data = remain_data.substring(4);
							if(len>0){
								String value = remain_data.substring(0, len*2);
								node.set(field, DigitalTrans.hexStringToString(value, 2));
								remain_data = remain_data.substring(len*2);
							}
						}
						//第3域处理代码
						if(field == 3){
							String value = remain_data.substring(0, 12);
							node.set(field, DigitalTrans.hexStringToString(value, 2));
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
						//第39域返回码
						if(field == 39){
							String value = remain_data.substring(0, 4);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(4);
						}
						//第41域终端号
						if(field==41){
							String value = remain_data.substring(0, 16);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(16);
						}
						// 截取第60域(保留)的信息
						// 注意也是LLLVAR格式
						if (field == 60) {
							
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
							int len = Integer.parseInt(len2);
//							System.out.println("60-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len * 2);
//								System.out.println("60-value:"+value);
								node.set(field,
										DigitalTrans.hexStringToString(value, 2));
								remain_data = remain_data.substring(len * 2);
							}
						}
						
						
						//第62域票据号
						if(field == 62){
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
							int len = Integer.parseInt(len2);
//							System.out.println("62-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len * 2);
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
//							System.out.println("63-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len * 2);
								node.set(field,
										new String(DigitalTrans.hexStringToByte(value),"GBK"));
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
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("node.toString():"+node.toString());
		return node;
	}
	
	public static void main(String[] args) {
		String response = "3031353202106038000002800017313639303039333230313030303030323031323041303030303439313832313432303435303231334535303030303130303130313432303134303231333134323034353032363030303030313030303033383034393137393230313430323133303332B4E6BFEECAFDBEDDBFE2B2D9D7F7B4EDCEF35BC7EBD6D8B1E0D2EBB3CCD0F25D491F56D5D7D42CA8000000";
		parseMsg(response);
	}
}
