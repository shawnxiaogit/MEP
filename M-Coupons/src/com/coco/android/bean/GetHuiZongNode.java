package com.coco.android.bean;


import java.io.Serializable;
import java.util.ArrayList;

import com.coco.android.MyApplication;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.MyUtil;

/**
 * 获取汇总信息报文实体类
 * @author ShawnXiao
 *
 */
public class GetHuiZongNode extends BaseNode implements Serializable{

	private static final long serialVersionUID = -5688367131222576058L;
	
	
	/**
	 * 手机相关信息 11位手机号+15位SIM卡标识+14位手机设备的标识
	 */
	private String phoneInfo;
	/**
	 * 当前返回记录位置
	 */
	private String current_return_falg;
	/**
	 * 本次返回记录数
	 */
	private String current_return_count;
	
	/**
	 * 开始时间
	 */
	private String start_time;
	/**
	 * 结束时间
	 */
	private String end_time;
	
	/**
	 * 查询标识：1-自己 2-主管

	 */
	private String query_flag;
	/**
	 * 查询类别:C-充值明细D-消费明细
	 */
	private String query_type;
	
	
	
	/**
	 * 卡号
	 */
	private String card_num;
	/**
	 * 客户编号
	 */
	private String cust_num;
	
	/**
	 * 手机号
	 */
	private String phone_num;
	
	/**
	 * 线路号
	 */
	private String xianluhao;
	
	
	
	/**
	 * 开始分钟
	 */
	private String start_min;
	/**
	 * 结束分钟
	 */
	private String end_min;
	
	
	public String getCard_num() {
		return card_num;
	}




	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}




	public String getCust_num() {
		return cust_num;
	}




	public void setCust_num(String cust_num) {
		this.cust_num = cust_num;
	}




	public String getPhone_num() {
		return phone_num;
	}




	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}




	public String getXianluhao() {
		return xianluhao;
	}




	public String getStart_min() {
		return start_min;
	}




	public void setStart_min(String start_min) {
		this.start_min = start_min;
	}




	public String getEnd_min() {
		return end_min;
	}




	public void setEnd_min(String end_min) {
		this.end_min = end_min;
	}




	public void setXianluhao(String xianluhao) {
		this.xianluhao = xianluhao;
	}

	public ArrayList<DealNode> dealNodes;
	
	public GetHuiZongNode(){
		dealNodes = new ArrayList<DealNode>();
	}
	
	
	
	
	public ArrayList<DealNode> getDealNodes() {
		return dealNodes;
	}




	public void setDealNodes(ArrayList<DealNode> dealNodes) {
		this.dealNodes = dealNodes;
	}




	public String getQuery_flag() {
		return query_flag;
	}

	public void setQuery_flag(String query_flag) {
		this.query_flag = query_flag;
	}

	public String getQuery_type() {
		return query_type;
	}

	public void setQuery_type(String query_type) {
		this.query_type = query_type;
	}

	public static byte[] buildMsg(String phone_info,
			String start,
			String count,
			String start_time,
			String end_time,
			String start_min,
			String end_min,
			String cust_num){
		GetHuiZongNode node = new GetHuiZongNode();
		try{
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.QUERY_HUI_ZONG_MTI);
			node.setMTI(new String(b_mti));
			node.setPhoneInfo(phone_info);
			
			//这里以后要做翻页，目前的做法是限定只返回10条
			//当前返回记录位置 ,0表示第1行。
			node.setCurrent_return_falg(start);
			//本次返回记录数 
			node.setCurrent_return_count(count);
			node.setStart_time(start_time);
			node.setEnd_time(end_time);
			node.setCust_num(cust_num);
			
			node.setStart_min(start_min);
			node.setEnd_min(end_min);
//			if(node.getCust_num()!=null&&node.getCust_num().length()>0
//					&&!node.getCust_num().equals("")){
//				node.setCust_num(cust_num);
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return getQueryListRequest(node);
	}

	public static byte[] getQueryListRequest(GetHuiZongNode node){
		
		StringBuilder sb = new StringBuilder();
		if(node.getMTI()!=null&&node.getMTI().length()>0){
			sb.append(node.getMTI());
		}
		if(node.getPhoneInfo()!=null&&node.getPhoneInfo().length()>0){
			sb.append(node.getPhoneInfo());
			sb.append("|");
		}
		if(node.getCurrent_return_falg()!=null&&node.getCurrent_return_falg().length()>0){
			sb.append(node.getCurrent_return_falg());
			sb.append("|");
		}
		if(node.getCurrent_return_count()!=null&&node.getCurrent_return_count().length()>0){
			sb.append(node.getCurrent_return_count());
			sb.append("|");
		}
		
		if(node.getStart_time()!=null&&node.getStart_time().length()>0){
			sb.append(node.getStart_time());
			sb.append("|");
		}
		if(node.getEnd_time()!=null&&node.getEnd_time().length()>0){
			sb.append(node.getEnd_time());
			sb.append("|");
		}
		
		if(node.getStart_min()!=null&&node.getStart_min().length()>0){
			sb.append(node.getStart_min());
			sb.append("|");
		}else{
			sb.append("|");
		}
		if(node.getEnd_min()!=null&&node.getEnd_min().length()>0){
			sb.append(node.getEnd_min());
			sb.append("|");
		}else{
			sb.append("|");
		}
		
		if(node.getCust_num()!=null&&node.getCust_num().length()>0&&!node.getCust_num().equals("")){
			sb.append(node.getCust_num());
			sb.append("|");
		}else{
			sb.append("|");
		}
		
		String data = sb.toString();
		StringBuilder sb2 = new StringBuilder();
		int rea_len = data.length();
		if(rea_len>99&&rea_len<999){
			sb2.append("0");
		}else if(rea_len>9&&rea_len<=99){
			sb2.append("00");
		}
		sb2.append(rea_len);
		sb2.append(data);


		System.out.println("request:"+printRequest(node,rea_len));
		return sb2.toString().getBytes();
	}
	
	private static final String SPLITE_FLAG = "|";
	
	private static String printRequest(GetHuiZongNode node,int rea_len){
		StringBuilder sb2 = new StringBuilder();
		if(rea_len>99&&rea_len<999){
			sb2.append("0");
		}else if(rea_len>9&&rea_len<=99){
			sb2.append("00");
		}
		sb2.append(rea_len);
		
		StringBuilder sb = new StringBuilder();
		sb.append(DigitalTrans.stringToHexString(sb2.toString()));
		if(node.getMTI()!=null&&node.getMTI().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getMTI().getBytes()));
		}
		if(node.getPhoneInfo()!=null&&node.getPhoneInfo().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getPhoneInfo().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		
		
		if(node.getCurrent_return_falg()!=null&&node.getCurrent_return_falg().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getCurrent_return_falg().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getCurrent_return_count()!=null&&node.getCurrent_return_count().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getCurrent_return_count().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		
		if(node.getStart_time()!=null&&node.getStart_time().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getStart_time().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		
		
		if(node.getEnd_time()!=null&&node.getEnd_time().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getEnd_time().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		
		if(node.getStart_min()!=null&&node.getStart_min().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getStart_min().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		
		if(node.getEnd_min()!=null&&node.getEnd_min().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getEnd_min().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		
		if(node.getCust_num()!=null&&node.getCust_num().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getCust_num().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		
		return sb.toString();
	}
	
	
	public String getStart_time() {
		return start_time;
	}


	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}


	public String getEnd_time() {
		return end_time;
	}


	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}


	public String getPhoneInfo() {
		return phoneInfo;
	}


	public void setPhoneInfo(String phoneInfo) {
		this.phoneInfo = phoneInfo;
	}


	public String getCurrent_return_falg() {
		return current_return_falg;
	}


	public void setCurrent_return_falg(String current_return_falg) {
		this.current_return_falg = current_return_falg;
	}


	public String getCurrent_return_count() {
		return current_return_count;
	}


	public void setCurrent_return_count(String current_return_count) {
		this.current_return_count = current_return_count;
	}
	public String getTotal_count() {
		return total_count;
	}


	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}


	public String getRecord_count() {
		return record_count;
	}


	public void setRecord_count(String record_count) {
		this.record_count = record_count;
	}
	
	/**
	 * 返回码
	 */
	private String return_code;
	
	public String getReturn_code() {
		return return_code;
	}
	

	/**
	 * 总记录数
	 */
	private String total_count;
	/**
	 * 记录个数
	 */
	private String record_count;

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	
	public static GetHuiZongNode parseMsg(String response){
		GetHuiZongNode node  = new GetHuiZongNode();
		try{
			if(response!=null&&response.length()>0){
				String remain_data = response;
				//报文头，及长度
				if(remain_data.length()>8){
					String header = remain_data.substring(0, 8);
					node.setHeader(DigitalTrans.hexStringToString(header, 2));
					remain_data = remain_data.substring(8);
				}
				if(remain_data.length()>4){
					//消息类型
					String MTI = remain_data.substring(0, 4);
					node.setMTI(MTI);
					remain_data = remain_data.substring(4);
				}
				if(remain_data.length()>6){
					//返回码
					String value = remain_data.substring(0, 6);
					String real_value = value.substring(0,4);
					node.setReturn_code(DigitalTrans.hexStringToString(real_value, 2));
//					node.set(39, DigitalTrans.hexStringToString(real_value, 2));
					remain_data = remain_data.substring(6);
				}
				
				byte[] b_data = DigitalTrans.hex2byte(remain_data);
				String str_data = new String(b_data,"GBK");
				System.out.println("str_data:"+str_data);
				
				String[] datas = str_data.split("\\|");
				for(int i=0;i<datas.length;i++){
//					System.out.println("datas["+i+"] : "+datas[i]);
					if(datas!=null&&datas.length>0){
						if(i==0){
							node.setTotal_count(datas[0]);
						}
						if(datas.length>1){
							if(i==1){
								node.setRecord_count(datas[1]);
							}
						}
					}
				}
				String[] datas2 = str_data.split("\\}");
				for(int i=0;i<datas2.length;i++){
//					System.out.println("datas2["+i+"] : "+datas2[i]);
					if(datas2!=null&&datas2.length>0){
						if(i==1){
							String[] details = datas2[i].split("\\|");
							for(int j=0;j<details.length;j++){
								System.out.println("details["+j+"] : "+ details[j]);
								if(j>=1){
									if(details!=null&&details.length>0){
										if(details[j]!=null&&details[j].length()>0
												&&!details[j].equals("")&&!details[j].equalsIgnoreCase("null")
												&&details[j].startsWith("\"")){
												String[] details2 = details[j].split("~");
												if(details[j]!=null&&details[j].length()>0
														&&!details[j].equals("")&&!details[j].equalsIgnoreCase("null")){
													DealNode dealNode = new DealNode();
													for(int n=0;n<details2.length;n++){
														String mdata = details2[n];
														System.out
																.println("details2["+n+"]:"+mdata);
														if(mdata!=null&&mdata.length()>0&&!mdata.equals("")&&!mdata.equalsIgnoreCase("null")){
															String str1 = null;
															String str2 = null;
															if(n==0){
																dealNode.setMep_account(mdata.replace("\"", ""));
															}
															if(n==1){
																str1 = mdata.replace(" ", "");
																dealNode.setMoney(str1);
															}
															if(n==2){
																dealNode.setCount(mdata.replace("\"", ""));
															}
															if(n==3){
																str2 = mdata.replace(" ", "");
																dealNode.setChexiaoMoney(str2);
															}
															if(n==4){
																dealNode.setChexiaoCount(mdata.replace("\"", ""));
															}
//															if(str1!=null||str2!=null){
//																dealNode.setTotalMoney(str1,str2);
//															}
														}
													}
												node.dealNodes.add(dealNode);
											}
										}
									}
								}
							}
						}
					}
				}
				
				
			}
		}catch(Exception e){
			
		}
		printMsg(node);
		return node;
	}
	public static void printMsg(GetHuiZongNode node){
		StringBuilder sb = new StringBuilder();
		if(node.getHeader()!=null&&node.getHeader().length()>0){
			sb.append("node.getHeader():"+node.getHeader());
			sb.append("\n");
		}
		if(node.getMTI()!=null&&node.getMTI().length()>0){
			sb.append("node.getMTI():"+node.getMTI());
			sb.append("\n");
		}
		if(node.getReturn_code()!=null&&node.getReturn_code().length()>0){
			sb.append("node.getReturn_code():"+node.getReturn_code());
			sb.append("\n");   
		}
		
		if(node.getTotal_count()!=null&&node.getTotal_count().length()>0){
			sb.append("node.getTotal_count():"+node.getTotal_count());
			sb.append("\n");
		}
		if(node.getRecord_count()!=null&&node.getRecord_count().length()>0){
			sb.append("node.getRecord_count():"+node.getRecord_count());
			sb.append("\n");
		}
		
		if(node.getDealNodes()!=null&&node.getDealNodes().size()>0){
			for(int i=0;i<node.getDealNodes().size();i++){
				DealNode item = node.getDealNodes().get(i);
				if(item!=null){
					sb.append(item.toString());
				}
			}
		}
		
		System.out.println(sb.toString());
		
	}
	public static void main(String[] args) {
		String response = "30343136100430307C327C317C7B7C7469746C653DD4F6BCD3B2E9D1AF4D4550BBE3D7DCD0C5CFA22020202020202020202020202020202020202020207C7472616E636F64653D2020202020207C666C643D4E414D457ED6D5B6CBBAC57E307E32317E327E4E554C4C7C666C643D5452414E414D54317ED7DCBDBBD2D7BDF0B6EE7E307E32317E327E4E554C4C7C666C643D4E554D317ED7DCBDBBD2D7B1CACAFD7E317E31307E327E4E554C4C7C666C643D5452414E414D54327ED7DCB3B7CFFABDF0B6EE7E307E32317E327E4E554C4C7C666C643D4E554D327ED7DCB3B7CFFAB1CACAFD7E317E31307E327E4E554C4C7C7D7C223133313632363437393132227E202020202020202020202020202020202020202020202020202020342E38337E32317E20202020202020202020202020202020202020202020202020202D332E31307E327C223133333031373937383636227E202020202020202020202020202020202020202020202020202036382E38347E36387E202020202020202020202020202020202020202020202020202D31342E32387E31377C7C7C7C7C0000000000";
		parseMsg(response);
	}
	
}
