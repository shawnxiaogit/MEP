package com.coco.android.bean;

import com.ajrd.SafeSoft;
import com.coco.android.MyApplication;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;

/**
 * ���ѳ���ʵ����
 * @author ShawnXiao
 *
 */
public class ConsumeCancleNode2 extends BaseNode{
	
	//������ѳ�������
	/**
	 * 
	 * @param val_2
	 * ����
	 * @param val_4
	 * ���
	 * @param val_11
	 * ��ˮ��
	 * @param val_12
	 * ����ʱ��
	 * @param val_13
	 * ��������
	 * @param val_52
	 * ����
	 * @param val_60
	 * 11λ�ֻ���+15λSIM����ʶ+14λ�ֻ��豸�ı�ʶ 
	 * @return
	 */
	public static byte[] buildConsumeCancleNode(String val_2,String val_4,String val_11,
			String val_12,String val_13,String val_52,String val_60,String consume_date){
		ConsumeCancleNode2 isoMsg = new ConsumeCancleNode2();
		try{
			//��Ϣ����
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.CONSUME_CANCLE_MTI);
			isoMsg.setMTI(new String(b_mti));
			//��λԪ��(��λͼ)
			String hex_bit_map = "7038040000C01015";
			byte[] b_map = MyUtil.hexStringToByte(hex_bit_map);
			isoMsg.setMainElement(b_map);
			//��2�����˺�
			isoMsg.set(2, RequestUtil.getLLVARData(val_2));
			//��3������
			isoMsg.set(3, MyApplication.CONSUME_CANCLE_HANDLE_COND);
			//��4���׽��Է�Ϊ��λ��12�ֽڳ��ȣ����㣬ǰ�油0
			isoMsg.set(4, RequestUtil.getSumOfConsume2(val_4));
			//��11����ˮ��
			isoMsg.set(11, val_11);
			//��12�򵱵ؽ���ʱ��
			isoMsg.set(12, val_12);
			//��13�򵱵ؽ�������
			isoMsg.set(13, val_13);
			//��22��POS���뷽ʽ
			isoMsg.set(22, MyApplication.POS_INPUT_TYPE);
			//��41���ն˺�
			isoMsg.set(41, MyApplication.TERM_NUM);
			//��42���̻���
			isoMsg.set(42, MyApplication.COMM_NUM);
			
			
			byte[] consume_key = SafeSoft.EncryptPin(DigitalTrans.byte2hex(MyApplication.PIN_KEY).getBytes(), val_52.getBytes(), val_2.getBytes());
			byte[] byte_pwd = MyUtil.hexStringToByte(new String(consume_key));
			isoMsg.set(52, byte_pwd);
			
//			System.out.println("PIN_KEY:"+DigitalTrans.byte2hex(MyApplication.PIN_KEY));
//			System.out.println("�������ģ�"+val_52);
//			System.out.println("����:"+val_2);
//			System.out.println("��������:"+new String(consume_key));
			
			//��60�򸽼�����
			isoMsg.set(60,val_60);
			L.e("val_60:", ""+val_60);
			
			//��62��Ʊ�ݺ�
			isoMsg.set(62, MyApplication.get62Value3(val_11,consume_date));
			L.e("MyApplication.get62Value()", ""+MyApplication.get62Value(val_11));
			
			
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
				for(int i=0;i<bit_str.length();i++){
					//�����Ƶ�ֵΪ1��AscIIֵ�����ʾ������
					if(bit_str.getBytes()[i]==49){
						//��Ϊi+1
						int field = i+1;
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
						
						
						//��62��Ʊ�ݺ�
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
						//��63��
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
		String response = "3031353202106038000002800017313639303039333230313030303030323031323041303030303439313832313432303435303231334535303030303130303130313432303134303231333134323034353032363030303030313030303033383034393137393230313430323133303332B4E6BFEECAFDBEDDBFE2B2D9D7F7B4EDCEF35BC7EBD6D8B1E0D2EBB3CCD0F25D491F56D5D7D42CA8000000";
		parseMsg(response);
	}
}
