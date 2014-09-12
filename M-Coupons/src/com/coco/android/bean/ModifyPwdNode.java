package com.coco.android.bean;


import java.io.UnsupportedEncodingException;

import com.ajrd.SafeSoft;
import com.coco.android.MyApplication;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;

/**
 * �޸����뱨��ʵ����
 * @author ShawnXiao
 *
 */
public class ModifyPwdNode extends BaseNode{
	
	public static byte[] buildMsg(String val_11,String val_52,String val_60,String val_61){
		ModifyPwdNode isoMsg = new ModifyPwdNode();
		try{
			//��Ϣ����
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.MODIFY_PWD_MTI);
			isoMsg.setMTI(new String(b_mti));
			//��λԪ��(��λͼ)
			String hex_bit_map = "2038000000C01019";
			byte[] b_map=MyUtil.hexStringToByte(hex_bit_map);
//			byte[] b_map=MyUtil.hexStringToByte(MyApplication.MODIFY_PWD_MAIN_ELEMENT);
			isoMsg.setMainElement(b_map);
			//������
			isoMsg.set(3, MyApplication.MODIFY_PWD_HANDLE_CODE);
			//��11����ˮ��
			isoMsg.set(11, val_11);
			//��12�򵱵ؽ���ʱ��
			isoMsg.set(12, MyApplication.getCurrentTime());
			//��13�򵱵ؽ�������
			isoMsg.set(13, MyApplication.getCurrentDate());
			//��41���ն˺�
			isoMsg.set(41, MyApplication.TERM_NUM);
			//��42���̻���
			isoMsg.set(42, MyApplication.COMM_NUM);
			
			String card_num = MyApplication.PHONE_NUM+"00000";
//			byte[] consume_key = SafeSoft.EncryptPin(DigitalTrans.byte2hex(MyApplication.PIN_KEY).getBytes(),
//					val_52.getBytes(), card_num.getBytes());
//			byte[] byte_pwd = MyUtil.hexStringToByte(new String(consume_key));
//			isoMsg.set(52, byte_pwd);
//			
//			System.out.println("PIN_KEY:"+DigitalTrans.byte2hex(MyApplication.PIN_KEY));
//			System.out.println("�������ģ�"+val_52);
//			System.out.println("����:"+card_num);
//			System.out.println("��������:"+new String(consume_key));
			
//			byte[] consume_key = SafeSoft.EncryptPin(MyApplication.MAIN_KEY2.getBytes(), val_52.getBytes(), card_num.getBytes());
			byte[] consume_key = SafeSoft.EncryptTlr(val_52.getBytes());
			byte[] byte_pwd = MyUtil.hexStringToByte(new String(consume_key));
			isoMsg.set(52, byte_pwd);
			
			System.out.println("MAIN_KEY:"+new String(MyApplication.MAIN_KEY2));
			System.out.println("�������ģ�"+val_52);
			System.out.println("����:"+card_num);
			System.out.println("��������:"+new String(consume_key));
			
			
			//��������
			isoMsg.set(60,val_60);
			
//			byte[] consume_key2 = SafeSoft.EncryptPin(MyApplication.MAIN_KEY2.getBytes(), val_61.getBytes(), card_num.getBytes());
			byte[] consume_key2 = SafeSoft.EncryptTlr(val_61.getBytes());
//			byte[] consume_key2 = SafeSoft.EncryptPin(DigitalTrans.byte2hex(MyApplication.PIN_KEY).getBytes(),
//					val_61.getBytes(), MyApplication.PHONE_NUM.getBytes());
//			byte[] byte_pwd2 = MyUtil.hexStringToByte(new String(consume_key2));
			isoMsg.set(61, getSixtyOneValue(new String(consume_key2)));
			
			
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
	
	private static String getSixtyOneValue(String val_61){
		
		int len = val_61.length();
		
		StringBuilder sb = new StringBuilder();
		if (len >= 0 && len <= 9) {
			sb.append("00");
		} else if (len > 9 && len <= 99) {
			sb.append("0");
		}
		sb.append(len);
		sb.append(val_61);
		return sb.toString();
	}
	
	
	public static ModifyPwdNode parseMsg(String response){
		ModifyPwdNode node  = new ModifyPwdNode();
		try{
			if(response!=null&&response.length()>0){
				//����ͷ��������
				String header = response.substring(0, 8);
				node.setHeader(DigitalTrans.hexStringToString(header, 2));
				//��Ϣ����
				String MTI = response.substring(8, 12);
				node.setMTI(MTI);
				//��λԪ��(��λͼ)
				String mainElement = response.substring(12, 28);
				node.setMainElement(DigitalTrans.hex2byte(mainElement));
				String remain_data = response.substring(28);
				System.out.println("remain_data:"+remain_data);
				
				//��λͼת��Ϊ2����
				String bit_str = MyStringUtil.hexToBin(mainElement);
//				System.out.println("bit_str:"+bit_str);
				for(int i=0;i<bit_str.length();i++){
					//�����Ƶ�ֵΪ1��AscIIֵ�����ʾ������
					if(bit_str.getBytes()[i]==49){
						//��Ϊi+1
						int field = i+1;
						//��3�������
						if(field == 3){
							String value = remain_data.substring(0, 12);
							node.set(field, DigitalTrans.hexStringToString(value, 2));
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
						//��39�򷵻���
						if(field == 39){
							String value = remain_data.substring(0, 4);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(4);
						}
						//��41���ն˺�
						if(field==41){
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
						// ��ȡ��60��(����)����Ϣ
						// ע��Ҳ��LLLVAR��ʽ
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
						//��61����
						if(field == 61){
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
							int len = Integer.parseInt(len2);
//							System.out.println("61-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len * 2);
								
								byte[] b_data_61 = DigitalTrans.hex2byte(value);
//								node.set(field,
//										new String(b_data_61,"GBK"));
								remain_data = remain_data.substring(len * 2);
							}
						}
						
						//��63��
						if(field == 63){
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
							int len = Integer.parseInt(len2);
//							System.out.println("63-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len * 2);
								byte[] data = DigitalTrans.hex2byte(value);
//								node.set(field,
//										DigitalTrans.hexStringToString(value, 2));
								try {
									node.set(field, new String(data,"GBK"));
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
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
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("node.toString():"+node.toString());
		return node;
	}
	public static void main(String[] args) {
		String response = "3030383608102038000002800019393933303030303339373037313035303033313132313030303030303030303330313432303133313132313130353030333031363137443231434543304436413132323628524D8D0001A7B1";
		parseMsg(response);
	}
}
