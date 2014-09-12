package com.coco.android.bean;


import com.ajrd.SafeSoft;
import com.coco.android.MyApplication;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.coco.android.util.MyUtil;
import com.easier.util.StringUtil;

/**
 * 获取消费动态验证码报文实体类
 * @author Shawn
 *
 */
public class GetSyntConsumeCodeNode extends BaseNode{
	
	/**
	 * 组装获取动态验证码
	 * @param val_11
	 * 系统流水号
	 * @param val_60
	 * 3位长度(ASCII)+11位手机号+15位SIM卡标识+14位手机设备的标识
	 * @return
	 */
	public static byte[] buildMsg(String val_11,String val_60){
		GetSyntConsumeCodeNode isoMsg = new GetSyntConsumeCodeNode();
		try{
			//信息类型
			//信息类型     
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.GET_SYTA_CONSUME_CODE_MTI);
			isoMsg.setMTI(new String(b_mti));
			//主位元表(即位图)
			String hex_bit_map = "2038000000C00011";
//			byte[] b_map=MyUtil.hexStringToByte(MyApplication.GET_SYTA_CONSUME_CODE_MAIN_ELEMENT);
			byte[] b_map=MyUtil.hexStringToByte(hex_bit_map);
			isoMsg.setMainElement(b_map);
			//处理代码
			isoMsg.set(3,MyApplication.GET_SYTA_CONSUME_CODE_HANDLE_CODE);
			//系统流水号
			isoMsg.set(11,val_11);
			//当地交易时间
			isoMsg.set(12,MyApplication.getCurrentTime());
			//当地交易日期
			isoMsg.set(13,MyApplication.getCurrentDate());
			//终端号
			isoMsg.set(41,MyApplication.TERM_NUM);
//			//商户号
			isoMsg.set(42,MyApplication.COMM_NUM);
			//附加数据
			isoMsg.set(60,val_60);
			
			
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
	 * 解析获取动态验证码返回报文
	 * @param response
	 * @return
	 */
	public static GetSyntConsumeCodeNode parseGetSyntCodeNode(String response){
		GetSyntConsumeCodeNode node = new GetSyntConsumeCodeNode();
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
								node.set(field,
										DigitalTrans.hexStringToString(value, 2));
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
