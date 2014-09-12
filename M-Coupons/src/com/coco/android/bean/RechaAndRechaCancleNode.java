package com.coco.android.bean;


/**
 * 充值和充值撤销数据报文实体类
 * @author ShawnXiao
 *
 */
public class RechaAndRechaCancleNode extends BaseNode{
	/**
	 * 充值类型
	 */
	public static final int TYPE_RECHARGE = 111;
	/**
	 * 充值撤销类型
	 */
	public static final int TYPE_RECHARGE_CANCLE = 112;
	
	/**
	 * 充值还是充值撤销
	 */
	private int type;
	/**
	 * 卡号
	 */
	private String mCardNum;
	/**
	 * 日期
	 */
	private String mDate;
	/**
	 * 金额
	 */
	private String mMoney;
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getmCardNum() {
		return mCardNum;
	}
	public void setmCardNum(String mCardNum) {
		this.mCardNum = mCardNum;
	}
	public String getmDate() {
		return mDate;
	}
	public void setmDate(String mDate) {
		this.mDate = mDate;
	}
	public String getmMoney() {
		return mMoney;
	}
	public void setmMoney(String mMoney) {
		this.mMoney = mMoney;
	}
	
}
