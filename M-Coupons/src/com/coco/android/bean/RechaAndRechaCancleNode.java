package com.coco.android.bean;


/**
 * ��ֵ�ͳ�ֵ�������ݱ���ʵ����
 * @author ShawnXiao
 *
 */
public class RechaAndRechaCancleNode extends BaseNode{
	/**
	 * ��ֵ����
	 */
	public static final int TYPE_RECHARGE = 111;
	/**
	 * ��ֵ��������
	 */
	public static final int TYPE_RECHARGE_CANCLE = 112;
	
	/**
	 * ��ֵ���ǳ�ֵ����
	 */
	private int type;
	/**
	 * ����
	 */
	private String mCardNum;
	/**
	 * ����
	 */
	private String mDate;
	/**
	 * ���
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
