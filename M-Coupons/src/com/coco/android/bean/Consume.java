package com.coco.android.bean;

/**
 * 
 * @Descriptio 消费Bean
 * @author Shawn
 * @Time 2013-8-28  下午5:29:14
 */
public class Consume {
	/**
	 * 是否撤销
	 */
	private boolean isCancle;
	/**
	 * 消费时间
	 */
	private String time;
	/**
	 * 消费卡号
	 */
	private String card_num;
	
	/**
	 * 消费手机号码
	 */
	private String phone_num;
	/**
	 * 消费金额
	 */
	private String sum_of_consume;
	
	/**
	 * 
	 * @Description 初始化数据
	 * @author Shawn
	 * @Time 2013-8-29  下午1:47:26
	 * @param param
	 * @return void
	 * @exception exception
	 */
	public void init(boolean isCancle,String time,String card_num,String phone_num,String sum_of_consume){
		clear();
		this.isCancle = isCancle;
		this.time = time;
		this.card_num = card_num;
		this.phone_num = phone_num;
		this.sum_of_consume = sum_of_consume+"元";
	}
	
	private void clear(){
		this.isCancle = false;
		this.time = "";
		this.card_num = "";
		this.phone_num ="";
		this.sum_of_consume ="";
	}
	
	public boolean isCancle() {
		return isCancle;
	}
	public void setCancle(boolean isCancle) {
		this.isCancle = isCancle;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getPhone_num() {
		return phone_num;
	}
	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}
	public String getSum_of_consume() {
		return sum_of_consume;
	}
	public void setSum_of_consume(String sum_of_consume) {
		this.sum_of_consume = sum_of_consume;
	}
	
	
	
}
