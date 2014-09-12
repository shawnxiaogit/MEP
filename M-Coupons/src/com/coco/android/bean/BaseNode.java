package com.coco.android.bean;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.coco.android.util.DigitalTrans;
import com.coco.android.util.MyUtil;
import com.easier.util.StringUtil;

/**
 * 
 * @Descriptio 8583报文基础节点
 * @author Shawn
 * @Time 2013-9-22 上午10:01:31
 */
public class BaseNode {

	/**
	 * 报文包长度
	 */
	private String header;

	// 内容 码值 格式 属性 请求 备注
	/**
	 * 信息类型 BCD n 4 0800
	 */
	private String MTI;
	/**
	 * 主位元表 BIN b 64 M
	 * 
	 */
	private byte[] mainElement;
	/**
	 * 数据元素
	 */
	private TreeMap<Integer, Object> dataElements = new TreeMap<Integer, Object>();

	/**
	 * 密码加密秘钥
	 */
	private byte[] pin_key;
	/**
	 * mac加密秘钥
	 */
	private byte[] mac_key;

	/**
	 * 本笔批次号
	 */
	private String picihao;
	/**
	 * 本笔票据号
	 */
	private String piaojuhao;

	/**
	 * 查询号
	 */
	private String chaxunhao;
	/**
	 * 日期
	 */
	private String date;

	public byte[] getPin_key() {
		return pin_key;
	}

	public void setPin_key(byte[] pin_key) {
		this.pin_key = pin_key;
	}

	public byte[] getMac_key() {
		return mac_key;
	}

	public void setMac_key(byte[] mac_key) {
		this.mac_key = mac_key;
	}

	public String getPicihao() {
		return picihao;
	}

	public void setPicihao(String picihao) {
		this.picihao = picihao;
	}

	public String getPiaojuhao() {
		return piaojuhao;
	}

	public void setPiaojuhao(String piaojuhao) {
		this.piaojuhao = piaojuhao;
	}

	public String getChaxunhao() {
		return chaxunhao;
	}

	public void setChaxunhao(String chaxunhao) {
		this.chaxunhao = chaxunhao;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setHeader(String len) {
		header = len;
	}

	public String getHeader() {
		return header;
	}

	public void setPinKey(byte[] key) {
		pin_key = key;
	}

	public byte[] getPinKey() {
		return pin_key;
	}

	public void setMacKey(byte[] key) {
		mac_key = key;
	}

	public byte[] getMacKey() {
		return mac_key;
	}

	/**
	 * 设置相应域的值
	 * 
	 * @param field
	 *            相应的域
	 * @param value
	 *            域的值
	 */
	public void set(int field, Object value) {
		dataElements.put(field, value);
	}

	/**
	 * 根据域名获取域的值
	 * 
	 * @param field
	 *            域名
	 * @return 域值
	 */
	public Object get(int field) {
		return dataElements.get(field);
	}

	public String getMTI() {
		return MTI;
	}

	public void setMTI(String mTI) {
		MTI = mTI;
	}

	public byte[] getMainElement() {
		return mainElement;
	}

	public void setMainElement(byte[] mainElement) {
		this.mainElement = mainElement;
	}

	public TreeMap<Integer, Object> getDataElements() {
		return dataElements;
	}

	public void setDataElements(TreeMap<Integer, Object> dataElements) {
		this.dataElements = dataElements;
	}

	public String getDataString() {
		StringBuilder sb = new StringBuilder();
		if (MTI != null && MTI.length() > 0) {
			sb.append(MTI);
		}
		if (mainElement != null && mainElement.length > 0) {
			sb.append(mainElement);
		}

		Iterator iter = dataElements.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry e = (Map.Entry) iter.next();
			if (e.getValue() != null) {
				sb.append(e.getValue());
			}
		}

		return sb.toString();
	}

	public String getMacDataString() {
		StringBuilder sb = new StringBuilder();
		if (MTI != null && MTI.length() > 0) {
			sb.append(MTI);
		}
		if (mainElement != null && mainElement.length > 0) {
			sb.append(new String(mainElement));
		}

		Iterator iter = dataElements.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry e = (Map.Entry) iter.next();
			int key = (Integer) e.getKey();
			if (key != 64) {
				if (e.getValue() != null) {
					sb.append(e.getValue());
				}
			}
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		if (header != null && header.length() > 0) {
			sb.append("header:");
			sb.append(header);
			sb.append("\n");
		}
		if (MTI != null && MTI.length() > 0) {
			sb.append("MTI:");
			sb.append(MTI);
			sb.append("\n");
		}
		if (mainElement != null && mainElement.length > 0) {
			sb.append("mainElement:");
			sb.append(DigitalTrans.byte2hex(mainElement));
			sb.append("\n");
		}

		Iterator iter = dataElements.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry e = (Map.Entry) iter.next();
			int key = Integer.parseInt(e.getKey().toString());
			String value = e.getValue().toString();
			if (value != null && value.length() > 0) {
				sb.append("[" + key + "]");
				sb.append(value);
			}
		}

		return sb.toString();
	}

	public String getHexString() {
		StringBuilder sb = new StringBuilder();

		if (MTI != null && MTI.length() > 0) {
			sb.append(MyUtil.bytesToHexString(MTI.getBytes()));
		}
		if (mainElement != null && mainElement.length > 0) {
			sb.append(MyUtil.bytesToHexString(mainElement));
		}

		Iterator iter = dataElements.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry e = (Map.Entry) iter.next();
			int key = Integer.parseInt(e.getKey().toString());
			if (key == 64 || key == 52) {
				byte[] mac = (byte[]) e.getValue();
				sb.append(MyUtil.bytesToHexString(mac));
			} else {

				String value = e.getValue().toString();
				if (value != null && value.length() > 0) {
					// sb.append("["+key+"]");

					// if(key==64){
					// sb.append(DigitalTrans.hexStringToString(StringUtil.bcd2Str(value.getBytes()),2));
					// }else
					if (key == 2) {
						// 查询余额接口中的，第二域
						String len = value.substring(0, 2);
						String data = value.substring(2, value.length());
						sb.append(MyUtil.bytesToHexString(len.getBytes()));
						sb.append(MyUtil.bytesToHexString(data.getBytes()));
					}
					// else if(key==62){
					// String len =value.substring(0,4);
					// String data = value.substring(4,value.length());
					// sb.append(len);
					// sb.append(MyUtil.bytesToHexString(data.getBytes()));
					// }
					else {
						sb.append(MyUtil.bytesToHexString(value.getBytes()));
					}
				}
			}
		}

		return sb.toString();
	}

	public String getMacHexString() {
		StringBuilder sb = new StringBuilder();

		if (MTI != null && MTI.length() > 0) {
			sb.append(MyUtil.bytesToHexString(MTI.getBytes()));
		}
		if (mainElement != null && mainElement.length > 0) {
			sb.append(MyUtil.bytesToHexString(mainElement));
		}

		Iterator iter = dataElements.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry e = (Map.Entry) iter.next();
			int key = Integer.parseInt(e.getKey().toString());
			if (key == 64 || key == 52) {
				byte[] mac = (byte[]) e.getValue();
				sb.append(MyUtil.bytesToHexString(mac));
			} else {
				String value = e.getValue().toString();
				if (value != null && value.length() > 0) {
					// sb.append("["+key+"]");

					// if(key==64){
					// sb.append(DigitalTrans.hexStringToString(StringUtil.bcd2Str(value.getBytes()),2));
					// }else
					if (key != 64) {
						if (key == 2) {
							// 查询余额接口中的，第二域
							String len = value.substring(0, 2);
							String data = value.substring(2, value.length());
							sb.append(MyUtil.bytesToHexString(len.getBytes()));
							sb.append(MyUtil.bytesToHexString(data.getBytes()));
						} else if (key == 62) {
							String len = value.substring(0, 4);
							String data = value.substring(4, value.length());
							sb.append(len);
							sb.append(MyUtil.bytesToHexString(data.getBytes()));
						} else {
							sb.append(MyUtil.bytesToHexString(value.getBytes()));
						}
					}
				}
			}
		}

		return sb.toString();
	}

}
