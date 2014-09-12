package com.coco.android.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.coco.android.MyApplication;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.MyUtil;

/**
 * 获取消费列表报文实体类
 * @author Shawn
 *
 */
public class ConsumeCancleNode extends BaseNode implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	public static byte[] buildConsumeCancleNode(){
		ConsumeCancleNode isoMsg = new ConsumeCancleNode();
		try{
			isoMsg.setMTI("1001");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 手机信息
	 * 11位手机号+15位SIM卡标识+14位手机设备的标识
	 */
	private String phone_info;
	/**
	 * 当前返回记录位置
	 * 0表示第1行
	 */
	private String current_return_flag;
	/**
	 * 本次返回记录数
	 */
	private String current_return_count;
	/**
	 * 交易种类
	 * 1 – 消费
	 * 2 – 充值
	 */
	private String deal_type;
	/**
	 * 卡号
	 */
	private String card_num;
	
	/**
	 * 开始时间
	 */
	private String start_time;
	
	/**
	 * 结束时间
	 */
	private String end_time;
	
	
	private ArrayList<Node> nodes;
	
	
	
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}



	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public class Node implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2L;
		/**
		 * 交易日期
		 */
		private String deal_date;
		/**
		 * 交易时间
		 */
		private String deal_time;
		/**
		 * 流水号
		 */
		private String serial_num;
		/**
		 * 卡号
		 */
		private String card_num;
		/**
		 * 金额
		 */
		private String money;
		/**
		 * 备注
		 */
		private String mark;
		
		/**
		 * 客户编号
		 */
		private String cust_num;
		/**
		 * 客户名称
		 */
		private String cust_name;
		
		
		public String getCust_num() {
			return cust_num;
		}



		public void setCust_num(String cust_num) {
			this.cust_num = cust_num;
		}



		public String getCust_name() {
			return cust_name;
		}
		
		public void setCust_name(String cust_name) {
			this.cust_name = cust_name;
		}
		
		public String getDeal_date() {
			return deal_date;
		}
		public void setDeal_date(String deal_date) {
			this.deal_date = deal_date;
		}
		public String getDeal_time() {
			return deal_time;
		}
		public void setDeal_time(String deal_time) {
			this.deal_time = deal_time;
		}
		public String getSerial_num() {
			return serial_num;
		}
		public void setSerial_num(String serial_num) {
			this.serial_num = serial_num;
		}
		public String getCard_num() {
			return card_num;
		}
		public void setCard_num(String card_num) {
			this.card_num = card_num;
		}
		public String getMoney() {
			return money;
		}
		public void setMoney(String money) {
			this.money = money;
		}
		public String getMark() {
			return mark;
		}
		public void setMark(String mark) {
			this.mark = mark;
		}
		@Override
		public String toString() {
			return "Node [deal_date=" + deal_date + ", deal_time=" + deal_time
					+ ", serial_num=" + serial_num + ", card_num=" + card_num
					+ ", money=" + money + ", mark=" + mark + "]";
		}



		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((card_num == null) ? 0 : card_num.hashCode());
			result = prime * result
					+ ((cust_name == null) ? 0 : cust_name.hashCode());
			result = prime * result
					+ ((cust_num == null) ? 0 : cust_num.hashCode());
			result = prime * result
					+ ((deal_date == null) ? 0 : deal_date.hashCode());
			result = prime * result
					+ ((deal_time == null) ? 0 : deal_time.hashCode());
			result = prime * result + ((mark == null) ? 0 : mark.hashCode());
			result = prime * result + ((money == null) ? 0 : money.hashCode());
			result = prime * result
					+ ((serial_num == null) ? 0 : serial_num.hashCode());
			return result;
		}



		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (card_num == null) {
				if (other.card_num != null)
					return false;
			} else if (!card_num.equals(other.card_num))
				return false;
			if (cust_name == null) {
				if (other.cust_name != null)
					return false;
			} else if (!cust_name.equals(other.cust_name))
				return false;
			if (cust_num == null) {
				if (other.cust_num != null)
					return false;
			} else if (!cust_num.equals(other.cust_num))
				return false;
			if (deal_date == null) {
				if (other.deal_date != null)
					return false;
			} else if (!deal_date.equals(other.deal_date))
				return false;
			if (deal_time == null) {
				if (other.deal_time != null)
					return false;
			} else if (!deal_time.equals(other.deal_time))
				return false;
			if (mark == null) {
				if (other.mark != null)
					return false;
			} else if (!mark.equals(other.mark))
				return false;
			if (money == null) {
				if (other.money != null)
					return false;
			} else if (!money.equals(other.money))
				return false;
			if (serial_num == null) {
				if (other.serial_num != null)
					return false;
			} else if (!serial_num.equals(other.serial_num))
				return false;
			return true;
		}



		private ConsumeCancleNode getOuterType() {
			return ConsumeCancleNode.this;
		}
		
		
		
		
	}
	
	
	
	public String getCard_num() {
		return card_num;
	}



	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}



	public String getPhone_info() {
		return phone_info;
	}



	public void setPhone_info(String phone_info) {
		this.phone_info = phone_info;
	}



	public String getCurrent_return_flag() {
		return current_return_flag;
	}



	public void setCurrent_return_flag(String current_return_flag) {
		this.current_return_flag = current_return_flag;
	}



	public String getCurrent_return_count() {
		return current_return_count;
	}



	public void setCurrent_return_count(String current_return_count) {
		this.current_return_count = current_return_count;
	}



	public String getDeal_type() {
		return deal_type;
	}



	public void setDeal_type(String deal_type) {
		this.deal_type = deal_type;
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



	/**
	 * 获取消费列表
	 * @param val_60
	 * 11位手机号+15位SIM卡标识+14位手机设备的标识
	 * @param card_num
	 * 卡号
	 * @param start_time
	 * 开始时间   
	 * @param end_time
	 * 结束时间
	 * @return
	 */
	public static byte[] buildQueryList(String val_60,String card_num,String start_time,String end_time,String start,String count){
		ConsumeCancleNode node = new ConsumeCancleNode();
		try{
			byte[] b_mti = MyUtil.hexStringToByte(MyApplication.DEAL_CANCLE_QUERY_MTI);
			node.setMTI(new String(b_mti));
			node.setPhone_info(val_60);
			if(start!=null&&start.length()>0){
				node.setCurrent_return_flag(start);
			}else{
				node.setCurrent_return_flag(MyApplication.DEAL_CANCLE_CURRENT_RETURN_FLAG2);
			}
			if(count!=null&&count.length()>0){
				node.setCurrent_return_count(count);
			}else{
				node.setCurrent_return_count(MyApplication.DEAL_CANCLE_CURRENT_RETURN_COUNT);
			}
			
			node.setDeal_type(MyApplication.DEAL_TYPE_CONSUME);
			node.setCard_num(card_num);
			node.setStart_time(start_time);
			node.setEnd_time(end_time);
			
			
		}catch(Exception e){
			
		}
		return getQreryListRequest(node);
	}
	
	public static byte[] getQreryListRequest(ConsumeCancleNode node){
		StringBuilder sb = new StringBuilder();
		if(node.getMTI()!=null&&node.getMTI().length()>0){
			sb.append(node.getMTI());
		}
		if(node.getPhone_info()!=null&&node.getPhone_info().length()>0){
			sb.append(node.getPhone_info());
			sb.append("|");
		}
		if(node.getCurrent_return_flag()!=null&&node.getCurrent_return_flag().length()>0){
			sb.append(node.getCurrent_return_flag());
			sb.append("|");
		}
		if(node.getCurrent_return_count()!=null&&node.getCurrent_return_count().length()>0){
			sb.append(node.getCurrent_return_count());
			sb.append("|");
		}
		if(node.getDeal_type()!=null&&node.getDeal_type().length()>0){
			sb.append(node.getDeal_type());
			sb.append("|");
		}
		if(node.getCard_num()!=null&&node.getCard_num().length()>0){
			sb.append(node.getCard_num());
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
	private static String printRequest(ConsumeCancleNode node,int rea_len){
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
		if(node.getPhone_info()!=null&&node.getPhone_info().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getPhone_info().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getCurrent_return_flag()!=null&&node.getCurrent_return_flag().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getCurrent_return_flag().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getCurrent_return_count()!=null&&node.getCurrent_return_count().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getCurrent_return_count().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getDeal_type()!=null&&node.getDeal_type().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getDeal_type().getBytes()));
			sb.append(MyUtil.bytesToHexString(SPLITE_FLAG.getBytes()));
		}
		if(node.getCard_num()!=null&&node.getCard_num().length()>0){
			sb.append(MyUtil.bytesToHexString(node.getCard_num().getBytes()));
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
		return sb.toString();
	}
	
	/**
	 * 返回码
	 */
	private String return_code;
	
	/**
	 * 总记录数
	 */
	private int total_count;
	
	/**
	 * 记录个数，本次记录个数
	 */
	private int recod_count;
	
	
	
	public int getTotal_count() {
		return total_count;
	}



	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}



	public int getRecod_count() {
		return recod_count;
	}



	public void setRecod_count(int recod_count) {
		this.recod_count = recod_count;
	}



	public String getReturn_code() {
		return return_code;
	}



	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}



	/**
	 * 解析获取列表报文
	 * @param response
	 * 返回报文
	 * @return
	 */
	public static ConsumeCancleNode parseMsg(String response){
		ConsumeCancleNode node = new ConsumeCancleNode();
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
					remain_data = remain_data.substring(6);
				}
//				System.out.println("remain_data:"+remain_data);
				String mData = DigitalTrans.hexStringToString(remain_data, 2);
				System.out.println("mData:"+mData);
				String[] mDatas = mData.split("#");
				if(mData!=null&&mData.length()>0){
					if(mDatas!=null&&mDatas.length>0){
						for(int i=0;i<mDatas.length;i++){
//							System.out.println("mDatas["+i+"]"+mDatas[i]);
							String detail_data = mDatas[i];
							if(i==0){
								String[] len_datas = detail_data.split("\\|");
								for(int k=0;k<len_datas.length;k++){
//									System.out.println("len_datas["+k+"]"+len_datas[k]);
									String len_data = len_datas[k];
									if(k==0){
										node.setTotal_count(Integer.parseInt(len_data));
									}
									if(k==1){
										node.setRecod_count(Integer.parseInt(len_data));
									}
								}
							}
							
							if(i==1){
								if(detail_data.length()>10){
								String[] real_datas = detail_data.split("&");
								ArrayList<Node> nodes = new ArrayList<Node>();
								for(int m=0;m<real_datas.length;m++){
									System.out.println("real_datas["+m+"]"+real_datas[m]);
									if(real_datas[m]!=null&&real_datas[m].length()>6
											&&!real_datas[m].equals("")
											&&!real_datas[m].equalsIgnoreCase("null")
											){
									String[] detail_item_datas = real_datas[m].split("\\|");
									if(detail_item_datas!=null&&detail_item_datas.length>1
											&&!detail_item_datas.equals("")){
										Node item =node.new Node();
										for(int n=0;n<detail_item_datas.length;n++){
//											System.out.println("detail_item_datas["+n+"]"+detail_item_datas[n]);
											String item_data = detail_item_datas[n];
											if(item_data!=null&&item_data.length()>0
													&&!item_data.equals("")
													&&!item_data.equalsIgnoreCase("null")){
											if(n==0){
												item.setDeal_date(item_data);
											}
											if(n==1){
												item.setDeal_time(item_data);
											}
											if(n==2){
												item.setSerial_num(item_data);
											}
											if(n==3){
												item.setCard_num(item_data);
											}
											if(n==4){
												item.setMoney(item_data);
											}
											
											if(n==5){
												item.setCust_num(item_data);
											}
											if(n==6){
												item.setCust_name(new String(DigitalTrans.hex2byte(DigitalTrans.stringToHexString(item_data)),"GBK"));
											}
											if(n==7){
												item.setMark(item_data);
											}
											}
										}
										nodes.add(item);
										node.setNodes(nodes);
									}
									
								}
								
								}
							}
							
							}
						}
					}
				}
				
				
				
//				int count=0;
//				
//				if(remain_data.length()>4){
//					//总记录数
//					String value = remain_data.substring(0, 4);
//					String real_value = value.substring(0,2);
//					count = Integer.parseInt(DigitalTrans.hexStringToString(real_value, 2));
//					node.setTotal_count(count);
//					remain_data = remain_data.substring(4);
//				}
//				
//				if(remain_data.length()>4){
//					//记录个数，本次记录个数
//					String value = remain_data.substring(0, 4);
//					String real_value = value.substring(0,2);
//					node.setRecod_count(Integer.parseInt(DigitalTrans.hexStringToString(real_value, 2)));
//					remain_data = remain_data.substring(4);
//				}
//				
////				System.out.println("remain_data:"+remain_data);
//				if(remain_data!=null&&remain_data.length()>40){
//					String[] datas = remain_data.split("26");
//					ArrayList<Node> nodes = new ArrayList<Node>();
//					for(int i=0;i<datas.length;i++){
////						System.out.println("data["+i+"]"+datas[i]);
//						if(datas[i]!=null&&datas[i].length()>0){
//							String data = datas[i];
//							String[] items = data.split("7C");
//							Node item =node.new Node();
//							for(int j=0;j<items.length;j++){
////								System.out.println("items["+j+"]"+items[j]);
//								if(j>=0){
//									if(items[0]!=null&&items[0].length()>0){
//										String value = items[0];
//										item.setDeal_date(DigitalTrans.hexStringToString(value, 2));
//									}
//								}
//								if(j>=1){
//									if(items[1]!=null&&items[1].length()>0){
//										String value = items[1];
//										item.setDeal_time(DigitalTrans.hexStringToString(value, 2));
//									}
//								}
//								if(j>=2){
//									if(items[2]!=null&&items[2].length()>0){
//										String value = items[2];
//										item.setSerial_num(DigitalTrans.hexStringToString(value, 2));
//									}
//								}
//								if(j>=3){
//									if(items[3]!=null&&items[3].length()>0){
//										String value = items[3];
//										item.setCard_num(DigitalTrans.hexStringToString(value, 2));
//									}
//								}
//								if(j>=4){
//									if(items[4]!=null&&items[4].length()>0){
//										String value = items[4];
//										item.setMoney(DigitalTrans.hexStringToString(value, 2));
//									}
//								}
//								if(j>=5){
//									if(items[5]!=null&&items[5].length()>0){
//										String value = items[5];
//										item.setMark(DigitalTrans.hexStringToString(value, 2));
//									}
//								}
//								
//								
//							}
////							System.out.println(item.toString());
//							nodes.add(item);
//						}
//						
//					}
//					
//					node.setNodes(nodes);
//				}
				
//				System.out.println("remain_data:"+remain_data);
				
				
				
				printConsumeCancleNode(node);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return node;
	}
	
	
	private static void printConsumeCancleNode(ConsumeCancleNode node){
		StringBuilder sb = new StringBuilder();
		if(node.getHeader()!=null&&node.getHeader().length()>0){
			sb.append("node.getHeader():");
			sb.append(node.getHeader());
			sb.append("\n");
		}
		if(node.getMTI()!=null&&node.getMTI().length()>0){
			sb.append("node.getMTI():");
			sb.append(node.getMTI());
			sb.append("\n");
		}
		if(node.getReturn_code()!=null&&node.getReturn_code().length()>0){
			sb.append("node.getReturn_code():");
			sb.append(node.getReturn_code());
			sb.append("\n");
		}
		if(node.getTotal_count()!=0){
			sb.append("node.getTotal_count():");
			sb.append(node.getTotal_count());
			sb.append("\n");
		}
		if(node.getRecod_count()!=0){
			sb.append("node.getRecod_count():");
			sb.append(node.getRecod_count());
			sb.append("\n");
		}
		if(node.getNodes()!=null&&node.getNodes().size()>0){
			for(int i=0;i<node.getNodes().size();i++){
				Node item = node.getNodes().get(i);
				if(item!=null){
					sb.append("item["+i+"]");
					if(item.getDeal_date()!=null&&item.getDeal_date().length()>0){
						sb.append("item.getDeal_date():"+item.getDeal_date());
					}
					if(item.getDeal_time()!=null&&item.getDeal_time().length()>0){
						sb.append("item.getDeal_time():"+item.getDeal_time());
					}
					if(item.getSerial_num()!=null&&item.getSerial_num().length()>0){
						sb.append("item.getSerial_num():"+item.getSerial_num());
					}
					if(item.getCard_num()!=null&&item.getCard_num().length()>0){
						sb.append("item.getCard_num():"+item.getCard_num());
					}
					if(item.getMoney()!=null&&item.getMoney().length()>0){
						sb.append("item.getMoney():"+item.getMoney());
					}
					if(item.getCust_num()!=null&&item.getCust_num().length()>0){
						sb.append("item.getCust_num():"+item.getCust_num());
					}
					if(item.getCust_name()!=null&&item.getCust_name().length()>0){
						sb.append("item.getCust_name():"+item.getCust_name());
					}
					
					if(item.getMark()!=null&&item.getMark().length()>0){
						sb.append("item.getMark():"+item.getMark());
					}
					sb.append("\n");
				}
			}
		}
		
		System.out.println(sb.toString());
	}
	
	
	
	
	
	
	public static void main(String[] args) {
//		String response="30303830100130307C317C312332303134303130387C3135343634397C34383132367C393030393332303130303030303130327C312E30307C303530333437353539307CCDF2BCD2BBDDB3ACCAD07C7C26000000";
		
		String response = "30353230100130307C32357C31302332303134303332367C3135313235307C35313833387C393030393332303130303030303031317C302E31307C303530333434333832337CBFAAD0C4B1E3C0FBB5EA7C7C2632303134303332317C3130343735307C35303939327C393030393332303130303030303031317C302E31307C7C7C7C2632303134303332317C3130343835377C35303939347C393030393332303130303030303031317C302E31307C7C7C7C2632303134303332317C3130343935397C35303939367C393030393332303130303030303031317C302E31307C7C7C7C2632303134303332317C3130353230317C35303939387C393030393332303130303030303031317C302E31307C7C7C7C2632303134303332317C3130353330337C35313030307C393030393332303130303030303031317C302E31307C7C7C7C2632303134303332317C3130353430397C35313030327C393030393332303130303030303031317C302E31307C7C7C7C2632303134303332317C3130353534377C35313030347C393030393332303130303030303031317C302E31307C7C7C7C2632303134303332317C3130353634387C35313030367C393030393332303130303030303031317C302E31307C7C7C7C2632303134303332317C3130353734397C35313030387C393030393332303130303030303031317C302E31307C7C7C7C26000000000000000000";
		parseMsg(response);
	}
}
