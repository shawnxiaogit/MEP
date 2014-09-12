package com.coco.android.bean;


import java.io.UnsupportedEncodingException;

import com.coco.android.MyApplication;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;
import com.easier.util.StringUtil;

/**
 * ��ȡ��̬��֤�뱨��ʵ����
 * @author Shawn
 *
 */
public class GetSyntCodeNode extends BaseNode{
	
	/**
	 * ��ȡ��ʼ����̬��
	 */
	public static final int GET_INIT_SYNTAX_CODE = 1111;
	/**
	 * ��ȡ���Ѷ�̬��
	 */
	public static final int GET_CONSUME_SYNTAX_CODE = 1112;
	
	/**
	 * ��װ��ȡ��̬��֤��
	 * @param val_11
	 * ϵͳ��ˮ��
	 * @param val_60
	 * 3λ����(ASCII)+11λ�ֻ���+15λSIM����ʶ+14λ�ֻ��豸�ı�ʶ
	 * @return
	 */
	public static byte[] buildMsg(String card_num,int type,String val_11,String val_60,String sum_of_consume){
		GetSyntCodeNode isoMsg = new GetSyntCodeNode();
		try{
			//��Ϣ����
			byte[] b_mti = null;
			if(type == GET_INIT_SYNTAX_CODE){
				b_mti=MyUtil.hexStringToByte(MyApplication.GET_SYTA_MTI);
			}else if(type==GET_CONSUME_SYNTAX_CODE){
				b_mti=MyUtil.hexStringToByte(MyApplication.GET_SYTA_CONSUME_CODE_MTI);
			}
			
			isoMsg.setMTI(new String(b_mti));
			//��λԪ��(��λͼ)
			String hex_bit_map = "";
			if(type == GET_INIT_SYNTAX_CODE){
				hex_bit_map= "2038000000000011";
			}else if(type==GET_CONSUME_SYNTAX_CODE){
				hex_bit_map= "7038000000000011";
			}
//			byte[] b_map=MyUtil.hexStringToByte(MyApplication.LOGIN_MAIN_ELEMENT);
			byte[] b_map=MyUtil.hexStringToByte(hex_bit_map);
			isoMsg.setMainElement(b_map);
			
			
			//��2�����˺�
			if(type==GET_CONSUME_SYNTAX_CODE){
				isoMsg.set(2, RequestUtil.getLLVARData(card_num));
			}
			//3�������
			if(type == GET_INIT_SYNTAX_CODE){
				isoMsg.set(3,MyApplication.GET_SYTA_HANDLE_CODE);
			}else if(type==GET_CONSUME_SYNTAX_CODE){
				isoMsg.set(3,MyApplication.GET_SYTA_CONSUME_CODE_HANDLE_CODE);
			}
			//4���׽��
			if(sum_of_consume!=null&&sum_of_consume.length()>0){
				isoMsg.set(4, RequestUtil.getSumOfConsume2(sum_of_consume));
			}
			
			//11ϵͳ��ˮ��
			isoMsg.set(11,val_11);
			//���ؽ���ʱ��
			isoMsg.set(12,MyApplication.getCurrentTime());
			//���ؽ�������
			isoMsg.set(13,MyApplication.getCurrentDate());
			//�ն˺�
//			isoMsg.set(41,MyApplication.TERM_NUM);
//			//�̻���
//			isoMsg.set(42,MyApplication.COMM_NUM);
			//��������
			isoMsg.set(60,val_60);
			
			
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
	 * ������ȡ��̬��֤�뷵�ر���
	 * @param response
	 * @return
	 */
	public static GetSyntCodeNode parseGetSyntCodeNode(String response){
		GetSyntCodeNode node = new GetSyntCodeNode();
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
				
				System.out.println("remain_data:" + remain_data);
			}
			System.out.println("node.getDataString():" + node.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return node;
	}
	
	public static void main(String[] args) {
		String response = "3030373408102038000002800019393932303030303233383130323233343130313032393030303030303030303230313432303133313032393232333431303030343137343911808AFC89632817";
		parseGetSyntCodeNode(response);
	}
	
}
