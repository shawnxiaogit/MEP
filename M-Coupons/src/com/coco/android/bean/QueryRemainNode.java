package com.coco.android.bean;



import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import com.ajrd.SafeSoft;
import com.coco.android.MyApplication;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;
import com.easier.util.StringUtil;

/**
 * ��ѯ����ʵ����
 * @author Shawn
 *
 */
public class QueryRemainNode extends BaseNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4839979581041519581L;




	//��װ��ѯ����
	public static byte[] buildMsg(String val_2,String val_11,String val_52,String val_60){
		QueryRemainNode isoMsg = new QueryRemainNode();
		try{
			//��Ϣ����
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.QUERY_MTI);
			isoMsg.setMTI(new String(b_mti));
			//��λԪ��(��λͼ)
			String hex_bit_map = "6038040000C01011";
			byte[] b_map=MyUtil.hexStringToByte(hex_bit_map);
			isoMsg.setMainElement(b_map);
			//���ʺ�
			isoMsg.set(2, RequestUtil.getLLVARData(val_2));
			//�������
			isoMsg.set(3, MyApplication.QUERY_HANDLE_CODE);
			//ϵͳ��ˮ��
			isoMsg.set(11, val_11);
			//���ؽ���ʱ��
			isoMsg.set(12,MyApplication.getCurrentTime());
			//���ؽ�������
			isoMsg.set(13,MyApplication.getCurrentDate());
			//����Ч��--------��ʱ��Ҫ
//			isoMsg.set(14, value);
			//POS���뷽ʽ
			isoMsg.set(22, MyApplication.POS_INPUT_TYPE);
			//2�ŵ�����
//			isoMsg.set(35, RequestUtil.getLLVARData(val_2));
			//3�ŵ�����
//			isoMsg.set(36, value)
			//�ն˺�
			isoMsg.set(41, MyApplication.TERM_NUM);
			//�̻���
			isoMsg.set(42, MyApplication.COMM_NUM);
			
			
			
			byte[] consume_key = SafeSoft.EncryptPin(DigitalTrans.byte2hex(MyApplication.PIN_KEY).getBytes(), val_52.getBytes(), val_2.getBytes());
			byte[] byte_pwd = MyUtil.hexStringToByte(new String(consume_key));
			isoMsg.set(52, byte_pwd);
			
//			System.out.println("PIN_KEY:"+DigitalTrans.byte2hex(MyApplication.PIN_KEY));
//			System.out.println("�������ģ�"+val_52);
//			System.out.println("����:"+val_2);
//			System.out.println("��������:"+new String(consume_key));
			
			
			//��������
			isoMsg.set(60,val_60);
//			L.e("val_60:", ""+val_60);
			
			
			
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
	
	
	/**
	 * ������ѯ����
	 * @param response
	 * @return
	 */
	public static QueryRemainNode parseQueryRemainNode(String response){
		QueryRemainNode node = new QueryRemainNode();
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
						//��2�����ʺ�
						if(field == 2){
							//ǰ4λΪ����
							String len1=remain_data.substring(0, 4);
							String len2 = DigitalTrans.hexStringToString(len1, 2);
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
						//��48�򸽼�����
						if(field == 48){
							
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
							int len = Integer.parseInt(len2);
							System.out.println("48-len:" + len);
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
							int len = Integer.parseInt(len2);
							System.out.println("54-len:" + len);
							remain_data = remain_data.substring(6);
							if(len>0){
								String value = remain_data.substring(0, len * 2);
								System.out.println("60-value:"+value);
								node.set(field,
										DigitalTrans.hexStringToString(value, 2));
								remain_data = remain_data.substring(len * 2);
							}
						}
						
						//��63��
						if(field == 63){
							String len1=remain_data.substring(0, 6);
							String len2=DigitalTrans.hexStringToString(len1, 2);
							if(RequestUtil.isNumeric(len2)){
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
			
			System.out.println("node.toString():"+node.toString());
			System.out.println("printYuErData:"+QueryRemainNode.printYuErData(node));
			return node;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	/**
	 * �������
	 */
	private String zhangmian_yuer;
	/**
	 * �������
	 */
	private String keyong_yuer;
	/**
	 * �����ȡ���
	 */
	private String dangtian_kequ_yuer;
	/**
	 * ��Ϣ
	 */
	private String lixi;
	/**
	 * δ���۱���
	 */
	private String weidengzhe_bishu;
	
	
	
	
	public String getZhangmian_yuer() {
		return zhangmian_yuer;
	}


	public void setZhangmian_yuer(String zhangmian_yuer) {
		this.zhangmian_yuer = zhangmian_yuer;
	}


	public String getKeyong_yuer() {
		return keyong_yuer;
	}


	public void setKeyong_yuer(String keyong_yuer) {
		this.keyong_yuer = keyong_yuer;
	}


	public String getDangtian_kequ_yuer() {
		return dangtian_kequ_yuer;
	}


	public void setDangtian_kequ_yuer(String dangtian_kequ_yuer) {
		this.dangtian_kequ_yuer = dangtian_kequ_yuer;
	}


	public String getLixi() {
		return lixi;
	}


	public void setLixi(String lixi) {
		this.lixi = lixi;
	}


	public String getWeidengzhe_bishu() {
		return weidengzhe_bishu;
	}


	public void setWeidengzhe_bishu(String weidengzhe_bishu) {
		this.weidengzhe_bishu = weidengzhe_bishu;
	}
	
	public static String printYuErData(QueryRemainNode node){
		StringBuilder sb = new StringBuilder();
		if(node.zhangmian_yuer!=null&&node.zhangmian_yuer.length()>0){
			sb.append("node.zhangmian_yue:"+node.zhangmian_yuer);
		}
		if(node.keyong_yuer!=null&&node.keyong_yuer.length()>0){
			sb.append("node.keyong_yuer:"+node.keyong_yuer);
		}
		if(node.dangtian_kequ_yuer!=null&&node.dangtian_kequ_yuer.length()>0){
			sb.append("node.dangtian_kequ_yuer:"+node.dangtian_kequ_yuer);
		}
		if(node.lixi!=null&&node.lixi.length()>0){
			sb.append("node.lixi:"+node.lixi);
		}
		if(node.weidengzhe_bishu!=null&&node.weidengzhe_bishu.length()>0){
			sb.append("node.weidengzhe_bishu:"+node.weidengzhe_bishu);
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
//		String response = "30313432011060380000028104111639303039333230313030303131313636333141303030303232363139313734303331313032383030303030303030303200000055433030303030303139373830304330303030303031393738303043303030303030303030303030433030303030303030303030303030300014323031333130323831373430333175280B7DB15D797B";
//		String response = "30303837011060380000028104110033314130303030323238313731393433303231303238413030303030303030320016D79C3A2AA34F7017E3A497021664D359000000143230313331303238313934333032126FE76A46ADFF12";
		String response = "303133360110603800000280001331363930303933323031303030303031303233314130303030353930353830303033353730353230453530303030313030353031343230313430353230303030333537303432BFA85B393030393332303130303030303130325DD2D1B1BBB6B3BDE1A3ACC7EBC1AACFB5D2B5CEF1D4B12E95911D96B0DC55000000000000";
		parseQueryRemainNode(response);
	}
}
