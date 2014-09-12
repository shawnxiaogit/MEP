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
 * �ͻ���Ϣ��ѯ(������)����ʵ����
 * @author ShawnXiao
 *
 */
public class QueryCustInfoNode extends QueryRemainNode implements Serializable{
	
	private static final long serialVersionUID = 8053519070313729579L;
	/**
	 * �ͻ���
	 */
	private String mCustNum;
	/**
	 * �ͻ�����
	 */
	private String mCustName;
	
	/**
	 * �ͻ��ֻ���
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
			//��Ϣ����     
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.QUERY_CUST_INFO_CARD_MTI);
			isoMsg.setMTI(new String(b_mti));
			//��λԪ��(��λͼ)
			String hex_bit_map = "6038040000C00011";
			byte[] b_map=MyUtil.hexStringToByte(hex_bit_map);
//			byte[] b_map=MyUtil.hexStringToByte(MyApplication.QUERY_CUST_INFO_CARD_MAIN_ELEMENT);
			isoMsg.setMainElement(b_map);
			//��2�����˺�
			isoMsg.set(2, RequestUtil.getLLVARData(val_2));
			//�������
			isoMsg.set(3,MyApplication.QUERY_CUST_INFO_CARD_HANDLE_CODE);
			//ϵͳ��ˮ��
			isoMsg.set(11,val_11);
			//���ؽ���ʱ��
			isoMsg.set(12,MyApplication.getCurrentTime());
			//���ؽ�������
			isoMsg.set(13,MyApplication.getCurrentDate());
			//��22��POS���뷽ʽ
			isoMsg.set(22, MyApplication.POS_INPUT_TYPE);
			//�ն˺�
			isoMsg.set(41,MyApplication.TERM_NUM);
			//�̻���
			isoMsg.set(42,MyApplication.COMM_NUM);
			//�������ݣ�11λ�ֻ���+15λSIM����ʶ+14λ�ֻ��豸�ı�ʶ 
			isoMsg.set(60,val_60);
			
			//�ֿ����ֻ���
//			isoMsg.set(61, val_61);
			
			
			byte[] data = RequestUtil.getMacBytes(isoMsg);
			int len = RequestUtil.getMacLen(isoMsg);
			byte[] mac =  SafeSoft.GenerateMac(DigitalTrans.byte2hex(MyApplication.MAC_KEY).getBytes(), len, data);
			byte[] byte_mac =MyUtil.hexStringToByte(new String(mac));
			//��64����Ϣ��֤��
			isoMsg.set(64,byte_mac);
			
//			System.out.println("MAC_KEY:"+DigitalTrans.byte2hex(MyApplication.MAC_KEY));
//			System.out.println("����:"+len);
//			System.out.println("����:"+isoMsg.getMacHexString());
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
						
						
						//��2�����ʺ�
						if(field == 2){
							//ǰ4λΪ����
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
						// ��ȡ��3��(�������)����Ϣ
						if (field == 3) {
							String value = remain_data.substring(0, 12);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(12);
						}
						
						// ��ȡ��11��(ϵͳ��ˮ��)����Ϣ
						if (field == 11) {
							String value = remain_data.substring(0, 12);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(12);
						}
						// ��ȡ��12��(���ؽ���ʱ��HHMMSS)����Ϣ
						if (field == 12) {
							String value = remain_data.substring(0, 12);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(12);
						}
						// ��ȡ��13��(���ؽ�������MMDD)����Ϣ
						if (field == 13) {
							String value = remain_data.substring(0, 8);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(8);
						}
						
						// ��ȡ��39��(������)����Ϣ
						if (field == 39) {
							String value = remain_data.substring(0, 4);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(4);
						}
						// ��ȡ��41��(�ն˺�)����Ϣ
						if (field == 41) {
							String value = remain_data.substring(0, 16);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(16);
						}
						
						
						
						//��48�򸽼�����
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
						
						
						
						//��54�򸽼ӽ��
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
						
						
						// ��ȡ��60��(����)����Ϣ
						// ע��Ҳ��LLLVAR��ʽ
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
						// ��ȡ61��(��������)����Ϣ
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
						// ��ȡ64��(��Ϣ��֤��)BIN��
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
