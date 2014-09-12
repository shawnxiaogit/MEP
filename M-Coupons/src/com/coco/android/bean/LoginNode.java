package com.coco.android.bean;

import java.io.UnsupportedEncodingException;

import com.ajrd.SafeSoft;
import com.coco.android.MyApplication;
import com.coco.android.util.DesUtil;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;
import com.easier.util.StringUtil;

/**
 * 
 * @Descriptio ǩ������
 * @author Shawn
 * @Time 2013-9-24  ����4:39:20
 */
public class LoginNode extends BaseNode {
	public LoginNode(){
		
	}
	/**
	 * ��װǩ������
	 * @param term_num
	 * �ն˺�
	 * @param comm_num
	 * �̻���
	 * @param mac_key
	 * mac��Կ
	 * @param value_11
	 * ϵͳ��ˮ��
	 * @param value_60
	 * 3λ����(ASCII)+11λ�ֻ���+15λSIM����ʶ+14λ�ֻ��豸�ı�ʶ
	 * @param val_61
	 * ����
	 * @return
	 */
	public static byte[] buildLoginMsg(String card_num,String term_num,String comm_num,String mac_key,
			String value_11,String val_52,String value_60){
		LoginNode isoMsg = new LoginNode();
//		String term_num = "00000002";
//		String comm_num = "999073154110001";
//		String mac_key = "3133363739363035";
		try {
			//��Ϣ����     
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.LOGIN_MTI);
			isoMsg.setMTI(new String(b_mti));
			//��λԪ��(��λͼ)
//			String hex_bit_map = "2038000000C00000";
			byte[] b_map=MyUtil.hexStringToByte(MyApplication.LOGIN_MAIN_ELEMENT);
			isoMsg.setMainElement(b_map);
			//�������
			isoMsg.set(3,MyApplication.LOGIN_HANDLE_CODE);
			//ϵͳ��ˮ��
			isoMsg.set(11,value_11);
			//���ؽ���ʱ��
			isoMsg.set(12,MyApplication.getCurrentTime());
			//���ؽ�������
			isoMsg.set(13,MyApplication.getCurrentDate());
			//�ն˺�
			isoMsg.set(41,term_num);
			//�̻���
			isoMsg.set(42,comm_num);
			L.e("value_60:", ""+value_60);
			
			
//			byte[] consume_key = SafeSoft.EncryptPin(MyApplication.MAIN_KEY2.getBytes(), val_52.getBytes(), card_num.getBytes());
			byte[] consume_key = SafeSoft.EncryptTlr(val_52.getBytes());
			byte[] byte_pwd = MyUtil.hexStringToByte(new String(consume_key));
			isoMsg.set(52, byte_pwd);
			
			System.out.println("MAIN_KEY:"+new String(MyApplication.MAIN_KEY2));
			System.out.println("�������ģ�"+val_52);
			System.out.println("����:"+card_num);
			System.out.println("��������:"+new String(consume_key));
			
//			//��������
			isoMsg.set(60,value_60);
			
			String key = "AB85D30EBE4CB720";
			String data = "123456789012345678";
			byte[] mac =  SafeSoft.GenerateMac(key.getBytes(), data.getBytes().length, data.getBytes());
			byte[] byte_mac =MyUtil.hexStringToByte(new String(mac));
			//��64����Ϣ��֤��
			isoMsg.set(64,byte_mac);
			
//			System.out.println("��Կ:"+key);
//			System.out.println("����:"+data.getBytes().length);
//			System.out.println("����:"+data);
//			System.out.println("MAC:"+new String(mac));
			
			byte[] request = RequestUtil.getBytesRequest(isoMsg);
			L.i("reuqest", ""+RequestUtil.printRequest(isoMsg));
			return request;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	

	/**
	 * �������ص�ǩ������
	 * 
	 * @param response
	 * ����ʵ����
	 * @return
	 */
	public static LoginNode paseResponse(String response) {
		// 012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		// 1 2 3 4 5 6 7 8 9 1 2 3 4 5 6 7 8 9
//		String response = "0810203800000281001D393930303030303137343032303034333136303931393030303030303030303200160A8A7C55F8316B8144FC2A6765881BDB003832303133303931393030343331363130313939393037333135343131303030317C616473327C000000263030303030313030303034302020202020203230313330393139AA4D7A884A6F0613";
		LoginNode node = new LoginNode();
		if (response != null && response.length() > 0) {
			String header = response.substring(0, 8);
			node.setHeader(DigitalTrans.hexStringToString(header, 2));
//			response = response.substring(6);
			
			String MTI = response.substring(8, 12);
			node.setMTI(MTI);
			String mainElement = response.substring(12, 28);
			node.setMainElement(DigitalTrans.hexStringToByte(mainElement));
			String remain_data = response.substring(28);
//			System.out.println("remain_data:" + remain_data);
			// ��λͼת��Ϊ2����
			String bit_str = MyStringUtil.hexToBin(mainElement);
//			System.out.println("bit_str:" + bit_str);

			for (int i = 0; i < bit_str.length(); i++) {
				// System.out.println("["+i+"]"+(((bit_str.getBytes()[i])==49)?1:0));
				if (((bit_str.getBytes()[i]) == 49)) {
					int field = i + 1;
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
					// ��ȡ��48��(��������)����Ϣ ǰ8�ֽ�Ϊ��������㷨����Կ����8�ֽ�Ϊmac�㷨��Կ��������Կ��
					// ע���ʽ��LLLVAR��ʽ
					if (field == 48) {
						// ǰ��λΪ����
						String len1=remain_data.substring(0, 6);
						String len2=DigitalTrans.hexStringToString(len1, 2);
//						System.out.println("len2:"+len2);
						int len = Integer.parseInt(len2);
//						System.out.println("48-len:" + len);
						remain_data = remain_data.substring(6);
						if(len>0){
							String value = remain_data.substring(0, len * 2);
//							System.out.println("48-value:"+value);
							node.set(field, value);
							remain_data = remain_data.substring(len * 2);
							
							
							String pin_key = value.substring(0, 16);
							byte[] b_pin_key = DigitalTrans.hex2byte(pin_key);
							node.setPinKey(b_pin_key);
							
//							System.out.println("PIN_KEY����:"+pin_key);
							
							String mac_key = value.substring(16, 32);
							byte[] b_mac_key = DigitalTrans.hex2byte(mac_key);
							node.setMacKey(b_mac_key);
							
//							System.out.println("MAC_KEY����:"+mac_key);
							
						}
					}
					// ��ȡ��60��(����)����Ϣ
					// ע��Ҳ��LLLVAR��ʽ
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
					// ��ȡ61��(��������)����Ϣ
					if (field == 61) {
						String len1=remain_data.substring(0, 6);
						String len2=DigitalTrans.hexStringToString(len1, 2);
//						System.out.println("len2:"+len2);
						int len = Integer.parseInt(len2);
//						System.out.println("61-len:" + len);
						remain_data = remain_data.substring(6);
						if(len>0){
							String value = remain_data.substring(0, len * 2);
							node.set(field,
									DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(len * 2);
						}
					}
					// ��ȡ62��(Ʊ�ݺ�)����Ϣ
					if (field == 62) {
						String len1=remain_data.substring(0, 6);
						String len2=DigitalTrans.hexStringToString(len1, 2);
//						System.out.println("len2:"+len2);
						int len = Integer.parseInt(len2);
//						System.out.println("62-len:" + len);
						remain_data = remain_data.substring(6);
						if(len>0){
							String value = remain_data.substring(0, len * 2);
//							System.out.println("62-value:"+value);
							node.set(field, DigitalTrans.hexStringToString(value, 2));
							remain_data = remain_data.substring(len * 2);
							
							
							if(value.length()>=12){
								String data = value.substring(0, 12);
//								System.out.println("data:"+data);
								String data_val = DigitalTrans.hexStringToString(data, 2);
								System.out.println("data_val1:"+data_val);
								node.setPicihao(data_val);
								value = value.substring(12);
							}
							if(value.length()>=12){
								String data = value.substring(0, 12);
//								System.out.println("data:"+data);
								String data_val = DigitalTrans.hexStringToString(data, 2);
								System.out.println("data_val2:"+data_val);
								node.setPiaojuhao(data_val);
								value = value.substring(12);
							}
							if(value.length()>=12){
								String data = value.substring(0, 12);
//								System.out.println("data:"+data);
								String data_val = DigitalTrans.hexStringToString(data, 2);
								System.out.println("data_val3:"+data_val);
								node.setChaxunhao(data_val);
								value = value.substring(12);
							}
							if(value.length()>=16){
								String data = value.substring(0, 16);
//								System.out.println("data:"+data);
								String data_val = DigitalTrans.hexStringToString(data, 2);
								System.out.println("data_val4:"+data_val);
								node.setChaxunhao(data_val);
								value = value.substring(16);
							}
						}
					}
					
					//��63��
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
					// ��ȡ64��(��Ϣ��֤��)BIN��
					if (field == 64) {
						String value = remain_data.substring(0, 16);
						node.set(field, value);
						remain_data = remain_data.substring(16);
					}

				}
			}
//			System.out.println("remain_data:" + remain_data);

		}
		System.out.println("node.getDataString():" + node.toString());
		return node;
	}
	
	
	public static void main(String[] args) {
		String response = "30313230081020380000028000133939303030303035383130323231343734323035313545353030303031303035303434323031343035313532313437343231313035303333333635313720202020207C31333136363236343739317C303138D6D5B6CBD2D1D7F7B7CFB2BBC4DCB5C7C2BCA15BE2BD3141CFED0000";
		System.out.println(response.length()/2);
		paseResponse(response);
	}
}
