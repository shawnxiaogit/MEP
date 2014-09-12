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
 * 查询报文实体类
 * @author Shawn
 *
 */
public class QueryRemainNode extends BaseNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4839979581041519581L;




	//组装查询报文
	public static byte[] buildMsg(String val_2,String val_11,String val_52,String val_60){
		QueryRemainNode isoMsg = new QueryRemainNode();
		try{
			//信息类型
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.QUERY_MTI);
			isoMsg.setMTI(new String(b_mti));
			//主位元素(即位图)
			String hex_bit_map = "6038040000C01011";
			byte[] b_map=MyUtil.hexStringToByte(hex_bit_map);
			isoMsg.setMainElement(b_map);
			//主帐号
			isoMsg.set(2, RequestUtil.getLLVARData(val_2));
			//处理代码
			isoMsg.set(3, MyApplication.QUERY_HANDLE_CODE);
			//系统流水号
			isoMsg.set(11, val_11);
			//当地交易时间
			isoMsg.set(12,MyApplication.getCurrentTime());
			//当地交易日期
			isoMsg.set(13,MyApplication.getCurrentDate());
			//卡有效期--------暂时不要
//			isoMsg.set(14, value);
			//POS输入方式
			isoMsg.set(22, MyApplication.POS_INPUT_TYPE);
			//2磁道内容
//			isoMsg.set(35, RequestUtil.getLLVARData(val_2));
			//3磁道内容
//			isoMsg.set(36, value)
			//终端号
			isoMsg.set(41, MyApplication.TERM_NUM);
			//商户号
			isoMsg.set(42, MyApplication.COMM_NUM);
			
			
			
			byte[] consume_key = SafeSoft.EncryptPin(DigitalTrans.byte2hex(MyApplication.PIN_KEY).getBytes(), val_52.getBytes(), val_2.getBytes());
			byte[] byte_pwd = MyUtil.hexStringToByte(new String(consume_key));
			isoMsg.set(52, byte_pwd);
			
//			System.out.println("PIN_KEY:"+DigitalTrans.byte2hex(MyApplication.PIN_KEY));
//			System.out.println("密码明文："+val_52);
//			System.out.println("卡号:"+val_2);
//			System.out.println("密码密文:"+new String(consume_key));
			
			
			//附加数据
			isoMsg.set(60,val_60);
//			L.e("val_60:", ""+val_60);
			
			
			
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
	
	
	/**
	 * 解析查询报文
	 * @param response
	 * @return
	 */
	public static QueryRemainNode parseQueryRemainNode(String response){
		QueryRemainNode node = new QueryRemainNode();
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
//				System.out.println("bit_str:"+bit_str);
				
				for(int i=0;i<bit_str.length();i++){
					//二进制的值为1的AscII值，则表示该域有
					if(bit_str.getBytes()[i]==49){
						//域为i+1
						int field = i+1;
						//第2域主帐号
						if(field == 2){
							//前4位为长度
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
						//第48域附加数据
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
						
						//第63域
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
						// 截取64域(信息验证码)BIN码
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
	 * 账面余额
	 */
	private String zhangmian_yuer;
	/**
	 * 可用余额
	 */
	private String keyong_yuer;
	/**
	 * 当天可取余额
	 */
	private String dangtian_kequ_yuer;
	/**
	 * 利息
	 */
	private String lixi;
	/**
	 * 未登折笔数
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
