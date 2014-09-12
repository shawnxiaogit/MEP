package com.coco.android.bean;

import java.io.UnsupportedEncodingException;

import com.coco.android.MyApplication;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;
import com.easier.util.StringUtil;

/**
 * ��ʼ������ʵ����
 * @author ShawnXiao
 *
 */
public class InitNode extends BaseNode{
	
	
	public static byte[] buildMsg(String val_11,String val_60,String val_61){
		InitNode isoMsg = new InitNode();
		try{
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.INIT_MTI);
			isoMsg.setMTI(new String(b_mti));
			//��λԪ��(��λͼ)
			String hex_bit_map = "2038000000000019";
//			byte[] b_map=MyUtil.hexStringToByte(MyApplication.INIT_MTI);
			byte[] b_map=MyUtil.hexStringToByte(hex_bit_map);
			isoMsg.setMainElement(b_map);
			//�������
			isoMsg.set(3,MyApplication.INIT_HANDLE_CODE);
			//ϵͳ��ˮ��
			isoMsg.set(11,val_11);
			//���ؽ���ʱ��w
			isoMsg.set(12,MyApplication.getCurrentTime());
			//���ؽ�������
			isoMsg.set(13,MyApplication.getCurrentDate());
			//�ն˺�
//			isoMsg.set(41,MyApplication.TERM_NUM);
//			//�̻���
//			isoMsg.set(42,MyApplication.COMM_NUM);
			//��������
			isoMsg.set(60,val_60);
			//61����֤��
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
	 * ������ʼ������
	 * @param response
	 * ���ر���
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
			// ��λͼת��Ϊ2����
			String bit_str = MyStringUtil.hexToBin(mainElement);
			for (int i = 0; i < bit_str.length(); i++) {
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
					// ��ȡ��42��(�̻���)����Ϣ
					if (field == 42) {
						String value = remain_data.substring(0, 30);
						System.out.println("value-42:"+value);
						node.set(field,
								DigitalTrans.hexStringToString(value, 2));
						remain_data = remain_data.substring(30);
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
		}
		System.out.println("node.getDataString():" + node.toString());
		return node;
	}
	public static void main(String[] args) {
		String response = "30303734081020380000028000193939323030313033303734353039353135353131303530303030303030303032303134323031333131303530393531353530303431313737D67004D0891F3765";
		parseResponse(response);
	}
}
