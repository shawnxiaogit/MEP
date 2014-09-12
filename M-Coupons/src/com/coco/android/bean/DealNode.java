package com.coco.android.bean;


import java.io.Serializable;

/**
 * 交易实体类
 * @author ShawnXiao
 *
 */
public class DealNode implements Serializable{
	private static final long serialVersionUID = -8980165543211462880L;
	
	/**
	 * 开卡描述
	 */
	public static final String STRING_OPEN_CARD = "开卡";
	
	/**
	 * 资金进出<C>转入
	 */
	public static final String TYPE_C = "C";
	/**
	 * 资金进出<D>转出
	 */
	public static final String TYPE_D = "D";
	
	
	/**
	 * MEP账号(即手机号码)
	 */
	private String mep_account;
	
	/**
	 *总交易金额
	 */
	private String money;
	
	/**
	 * 总交易笔数
	 */
	private String count;
	
	/**
	 * 总撤销金额
	 */
	private String chexiaoMoney;
	/**
	 * 总撤销笔数
	 */
	private String chexiaoCount;
	
	/**
	 * 实际交易金额
	 */
	private String totalMoney;
	
	
	
	/**
	 * 线路号
	 */
	private String xianluhao;
	
	/**
	 * 卡号
	 */
	private String cardNum;
	
	
	
	/**
	 * 商户号
	 */
	private String commNum;
	
	
	public String getCommNum() {
		return commNum;
	}
	public void setCommNum(String commNum) {
		this.commNum = commNum;
	}
	public String getXianluhao() {
		return xianluhao;
	}
	public void setXianluhao(String xianluhao) {
		this.xianluhao = xianluhao;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getChexiaoMoney() {
		return chexiaoMoney;
	}
	public void setChexiaoMoney(String chexiaoMoney) {
		this.chexiaoMoney = chexiaoMoney;
	}
	public String getChexiaoCount() {
		return chexiaoCount;
	}
	public void setChexiaoCount(String chexiaoCount) {
		this.chexiaoCount = chexiaoCount;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String money,String chexiaoMoney) {
		double d1 = Double.valueOf(money);
		double d2 = Double.valueOf(chexiaoMoney);
		double d3 = d1+d2;
		this.totalMoney = String.valueOf(d3);
	}
	/**
	 * 类型是充值的文字描述
	 */
	public static final String STRING_CANCLE_CONSUME = "消费撤销";
	public static final String STRING_CANCLE_CONSUME2 = "交易撤销";
	/**
	 * 类型是充值撤销的文字描述
	 */
	public static final String STRING_CONSUME = "消费";
	public static final String STRING_CONSUME2 = "交易";
	
	
	public static final String SUBCRIB_CANCLE = "取消";
	public static final String SUBCRIB_CONSUME = "消费";
	
	
	/**
	 * 交易时间
	 */
	private String deal_time;
	/**
	 * 交易金额
	 */
	private String deal_money;
	/**
	 * 资金进出<C>转入<D>转出
	 */
	private String deal_type;
	/**
	 * 余额
	 */
	private String reamin;
	/**
	 * 交易机构
	 */
	private String deal_org;
	/**
	 * 摘要
	 */
	private String deal_summry;
	
	/**
	 * 原交易流水号
	 */
	private String orinal_line;
	/**
	 * 子交易流水号
	 */
	private String sub_line;
	
	/**
	 * 客户编号
	 */
	private String cust_num;
	/**
	 * 客户名称
	 */
	private String cust_name;
	
	
	
	
	public String getMep_account() {
		return mep_account;
	}
	public void setMep_account(String mep_account) {
		this.mep_account = mep_account;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
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
	public String getDeal_time() {
		return deal_time;
	}
	public void setDeal_time(String deal_time) {
		this.deal_time = deal_time;
	}
	public String getDeal_money() {
		return deal_money;
	}
	public void setDeal_money(String deal_money) {
		this.deal_money = deal_money;
	}
	public String getDeal_type() {
		return deal_type;
	}
	public void setDeal_type(String deal_type) {
		this.deal_type = deal_type;
	}
	public String getReamin() {
		return reamin;
	}
	public void setReamin(String reamin) {
		this.reamin = reamin;
	}
	public String getDeal_org() {
		return deal_org;
	}
	public void setDeal_org(String deal_org) {
		this.deal_org = deal_org;
	}
	public String getDeal_summry() {
		return deal_summry;
	}
	public void setDeal_summry(String deal_summry) {
		this.deal_summry = deal_summry;
	}
	public String getOrinal_line() {
		return orinal_line;
	}
	public void setOrinal_line(String orinal_line) {
		this.orinal_line = orinal_line;
	}
	public String getSub_line() {
		return sub_line;
	}
	public void setSub_line(String sub_line) {
		this.sub_line = sub_line;
	}

	
	
	
	

	@Override
	public String toString() {
		return "DealNode [mep_account=" + mep_account + ", money=" + money
				+ ", count=" + count + ", chexiaoMoney=" + chexiaoMoney
				+ ", chexiaoCount=" + chexiaoCount + ", totalMoney="
				+ totalMoney + ", deal_time=" + deal_time + ", deal_money="
				+ deal_money + ", deal_type=" + deal_type + ", reamin="
				+ reamin + ", deal_org=" + deal_org + ", deal_summry="
				+ deal_summry + ", orinal_line=" + orinal_line + ", sub_line="
				+ sub_line + ", cust_num=" + cust_num + ", cust_name="
				+ cust_name + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cust_name == null) ? 0 : cust_name.hashCode());
		result = prime * result
				+ ((cust_num == null) ? 0 : cust_num.hashCode());
		result = prime * result
				+ ((deal_money == null) ? 0 : deal_money.hashCode());
		result = prime * result
				+ ((deal_org == null) ? 0 : deal_org.hashCode());
		result = prime * result
				+ ((deal_summry == null) ? 0 : deal_summry.hashCode());
		result = prime * result
				+ ((deal_time == null) ? 0 : deal_time.hashCode());
		result = prime * result
				+ ((deal_type == null) ? 0 : deal_type.hashCode());
		result = prime * result
				+ ((orinal_line == null) ? 0 : orinal_line.hashCode());
		result = prime * result + ((reamin == null) ? 0 : reamin.hashCode());
		result = prime * result
				+ ((sub_line == null) ? 0 : sub_line.hashCode());
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
		DealNode other = (DealNode) obj;
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
		if (deal_money == null) {
			if (other.deal_money != null)
				return false;
		} else if (!deal_money.equals(other.deal_money))
			return false;
		if (deal_org == null) {
			if (other.deal_org != null)
				return false;
		} else if (!deal_org.equals(other.deal_org))
			return false;
		if (deal_summry == null) {
			if (other.deal_summry != null)
				return false;
		} else if (!deal_summry.equals(other.deal_summry))
			return false;
		if (deal_time == null) {
			if (other.deal_time != null)
				return false;
		} else if (!deal_time.equals(other.deal_time))
			return false;
		if (deal_type == null) {
			if (other.deal_type != null)
				return false;
		} else if (!deal_type.equals(other.deal_type))
			return false;
		if (orinal_line == null) {
			if (other.orinal_line != null)
				return false;
		} else if (!orinal_line.equals(other.orinal_line))
			return false;
		if (reamin == null) {
			if (other.reamin != null)
				return false;
		} else if (!reamin.equals(other.reamin))
			return false;
		if (sub_line == null) {
			if (other.sub_line != null)
				return false;
		} else if (!sub_line.equals(other.sub_line))
			return false;
		return true;
	}
	
	
}