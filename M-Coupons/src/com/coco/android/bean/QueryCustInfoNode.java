package com.coco.android.bean;


import java.io.Serializable;
import java.text.DecimalFormat;

import com.ajrd.SafeSoft;
import com.coco.android.MyApplication;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;

/**
 * 客户信息查询(按卡号)报文实体类
 * @author ShawnXiao
 *
 */
public class QueryCustInfoNode extends QueryRemainNode implements Serializable{
	
	private static final long serialVersionUID = 8053519070313729579L;
	/**
	 * 客户号
	 */
	private String mCustNum;
	/**
	 * 客户名称
	 */
	private String mCustName;
	
	/**
	 * 客户手机号
	 */
	private String mCustPhoneNum;
	
	
	
	
	public String getmCustPhoneNum() {
		return mCustPhoneNum;
	}



	public void setmCustPhoneNum(String mCustPhoneNum) {
		this.mCustPhoneNum = mCustPhoneNum;
	}



	public String getmCustNum() {
		return mCustNum;
	}

	
	private String mCustInfo;
	
	


	public String getmCustInfo() {
		return mCustInfo;
	}



	public void setmCustInfo(String mCustInfo) {
		this.mCustInfo = mCustInfo;
	}



	public void setmCustNum(String mCustNum) {
		this.mCustNum = mCustNum;
	}



	public String getmCustName() {
		return mCustName;
	}



	public void setmCustName(String mCustName) {
		this.mCustName = mCustName;
	}



	public static byte[] buildMsg(String val_2,String val_11,String val_60){
		QueryCustInfoNode isoMsg = new QueryCustInfoNode();
		try{
			//信息类型     
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.QUERY_CUST_INFO_CARD_MTI);
			isoMsg.setMTI(new String(b_mti));
			//主位元表(即位图)
			String hex_bit_map = "6038040000C00011";
			byte[] b_map=MyUtil.hexStringToByte(hex_bit_map);
//			byte[] b_map=MyUtil.hexStringToByte(MyApplication.QUERY_CUST_INFO_CARD_MAIN_ELEMENT);
			isoMsg.setMainElement(b_map);
			//第2域主账号
			isoMsg.set(2, RequestUtil.getLLVARData(val_2));
			//处理代码
			isoMsg.set(3,MyApplication.QUERY_CUST_INFO_CARD_HANDLE_CODE);
			//系统流水号
			isoMsg.set(11,val_11);
			//当地交易时间
			isoMsg.set(12,MyApplication.getCurrentTime());
			//当地交易日期
			isoMsg.set(13,MyApplication.getCurrentDate());
			//第22域POS输入方式
			isoMsg.set(22, MyApplication.POS_INPUT_TYPE);
			//终端号
			isoMsg.set(41,MyApplication.TERM_NUM);
			//商户号
			isoMsg.set(42,MyApplication.COMM_NUM);
			//附加数据，11位手机号+15位SIM卡标识+14位手机设备的标识 
			isoMsg.set(60,val_60);
			
			//持卡人手机号
//			isoMsg.set(61, val_61);
			
			
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
			L.i("reuqest", ""+RequestUtil.printRequest(isoMsg));
			
			
			return request;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static QueryCustInfoNode parseMsg(String response){
		QueryCustInfoNode node = new QueryCustInfoNode();
		try{
			if (response != null && response.length() > 0) {
				String header = response.substring(0, 8);
				node.setHeader(DigitalTrans.hexStringToString(header, 2));
				String MTI = response.substring(8, 12);
				node.setMTI(MTI);
				String mainElement = response.substring(12, 28);
				node.setMainElement(DigitalTrans.hexStringToByte(mainElement));
				String remain_data = response.substring(28);
				String bit_str = MyStringUtil.hexToBin(mainElement);
				for (int i = 0; i < bit_str.length(); i++) {
					if (((bit_str.getBytes()[i]) == 49)) {
						int field = i + 1;
						
						
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
						
						
						
						//第48域附加数据
						if(field == 48){
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
//							System.out.println("len2:"+len2);
							int len = Integer.parseInt(len2);
//							System.out.println("48-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len*2);
								node.set(field, value);
								remain_data = remain_data.substring(len * 2);
							}
						}
						
						
						
						//第54域附加金额
						if(field == 54){
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
							int len = Integer.parseInt(len2);
							System.out.println("54-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len*2);
								System.out.println("54-val_hex:"+value);
								System.out.println("54-value:"+DigitalTrans.hexStringToString(value, 2));
								
								
								if(value!=null&&value.length()>0){
									node.set(field, value);
									
									String tmp = DigitalTrans.hexStringToString(value, 2);
									String[] datas = tmp.split("C");
									for(int k=0;k<datas.length;k++){
//										System.out.println("datas["+k+"]"+datas[k]);
										if(k>=1&&k==1){
											String str = datas[k];
											Double double1 = Double.parseDouble(str); 
											Double double2 = (double1/100);
											DecimalFormat decimalFormat = new DecimalFormat("0.00");
											node.setZhangmian_yuer(decimalFormat.format(double2));
											System.out.println("node.getZhangmian_yuer():"+node.getZhangmian_yuer());
										}
									}
									
									remain_data = remain_data.substring(len * 2);
									
								}
								
								
								
							}
							
						}
						
						
						// 截取第60域(保留)的信息
						// 注意也是LLLVAR格式
						if (field == 60) {
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
//							System.out.println("len2:"+len2);
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
						// 截取61域(附加数据)的信息
						if (field == 61) {
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
//							System.out.println("len2:"+len2);
							int len = Integer.parseInt(len2);
//							System.out.println("61-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len * 2);
								node.set(field,value);
								System.out.println("61-value:"+value);
								String data = new String(DigitalTrans.hex2byte(value),"GBK");
								System.out.println("data:"+data);
								String[] str_datas = data.split("\\|");
								if(str_datas!=null&&str_datas.length>0){
									for(int k=0;k<str_datas.length;k++){
										System.out.println("str_datas["+k+"]"+str_datas[k]);
										String my_detail_data = str_datas[k];
										if(k==0){
											node.setmCustPhoneNum(my_detail_data);
										}
										if(k==1){
											node.setmCustNum(my_detail_data);
										}
										if(k==2){
											node.setmCustName(my_detail_data);
										}
									}
								}
//								String cust_num=data.substring(0, 10);
//								System.out.println("cust_num:"+cust_num);
//								node.setmCustNum(cust_num);
//								String cust_name = data.substring(10, data.length());
//								System.out.println("cust_name:"+cust_name);
//								node.setmCustName(cust_name);
								
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
				System.out.println("remain_data:" + remain_data);
			}
			System.out.println("node.getDataString():" + node.toString());
			
			return node;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	public static void main(String[] args) {
//		String response = "30303734081020380000028000193939323030303032343130373032313233303130333030303030303030303032303134323031333130333030323132333030303432373134B5169DA81BB19841";
		String response = "3031383401106038000002800419313639303039333230313030303030313032333141303031303438353133313832393133303132333030303030303130303130353543303030303030323338363430433030303030303233383634304330303030303030303030303043303030303030303030303030303030303134323031343031323331383239313330333331333831383632333432387C303530333437353539307CCDF2BCD2BBDDB3ACCAD0E4997B7CA43ED2910000000000";
		parseMsg(response);
	}
	
}
