package com.coco.android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyUtil {
	/***
	 * ��16�����ַ���ת�����ֽ�����
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/** */
	/**
	 * ���ֽ�����ת����16�����ַ���
	 * 
	 * @param bArray
	 * @return
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/** */
	/**
	 * ���ֽ�����ת��Ϊ����
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static final Object bytesToObject(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectInputStream oi = new ObjectInputStream(in);
		Object o = oi.readObject();
		oi.close();
		return o;
	}

	/** */
	/**
	 * �ѿ����л�����ת�����ֽ�����
	 * 
	 * @param s
	 * @return
	 * @throws IOException
	 */
	public static final byte[] objectToBytes(Serializable s) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream ot = new ObjectOutputStream(out);
		ot.writeObject(s);
		ot.flush();
		ot.close();
		return out.toByteArray();
	}

	public static final String objectToHexString(Serializable s)
			throws IOException {
		return bytesToHexString(objectToBytes(s));
	}

	public static final Object hexStringToObject(String hex)
			throws IOException, ClassNotFoundException {
		return bytesToObject(hexStringToByte(hex));
	}

	/** */
	/**
	 * @��������: BCD��תΪ10���ƴ�(����������)
	 * @�������: BCD��
	 * @������: 10���ƴ�
	 */
	public static String bcd2Str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
				.toString().substring(1) : temp.toString();
	}

	/** */
	/**
	 * @��������: 10���ƴ�תΪBCD��
	 * @�������: 10���ƴ�
	 * @������: BCD��
	 */
	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}

		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;

		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}

			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}

			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	/** */
	/**
	 * @��������: BCD��תASC��
	 * @�������: BCD��
	 * @������: ASC��
	 */
	public static String BCD2ASC(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			int h = ((bytes[i] & 0xf0) >>> 4);
			int l = (bytes[i] & 0x0f);
			temp.append(bytes[h]).append(bytes[l]);
		}
		return temp.toString();
	}

	/** */
	/**
	 * MD5�����ַ��������ؼ��ܺ��16�����ַ���
	 * 
	 * @param origin
	 * @return
	 */
	public static String MD5EncodeToHex(String origin) {
		return bytesToHexString(MD5Encode(origin));
	}

	/** */
	/**
	 * MD5�����ַ��������ؼ��ܺ���ֽ�����
	 * 
	 * @param origin
	 * @return
	 */
	public static byte[] MD5Encode(String origin) {
		return MD5Encode(origin.getBytes());
	}

	/** */
	/**
	 * MD5�����ֽ����飬���ؼ��ܺ���ֽ�����
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] MD5Encode(byte[] bytes) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			return md.digest(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}

	}
	
	 /**
     * ��ȡָ���ֽ�����
     * @param src    Դ�ֽ�����
     * @param begin    ��ʼ�±�
     * @param count    ��ȡ�ֽڸ���
     * @return    �µ��ֽ�����
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
        return bs;
    }
    
    /**
     * ��ȡ�������ݳ���
     * @param len
     * ���ݳ���
     * @return
     */
    public static int getMyDataLen(int len){
		int myLen = 0;
		if(len+2<=8){
			return 8;
		}
		if(len+2>8){
			int count = ((len+2)/8);
			int remain = (len+2)%8;
//			System.out.println("count:"+count);
			if(remain==0){
				myLen = (count)*8;
			}else{
				myLen = (count+1)*8;
			}
			
			
		}
		
		return myLen;
	}
    
	/**
	 * ��ȡbyte����
	 * @param b	��byte����
	 * @param j	�Ǵ�С
	 * @return
	 */
	public static byte[] cutOutByte(byte[] b,int j){
		if(b.length==0 || j==0){
			return null;
		}
		byte[] bjq = new byte[j];
		for(int i = 0; i<j;i++){
			bjq[i]=b[i];
		}
		return bjq;
	}
	

	
    /**
     * �ϲ�����byte����
     * @param byte_1
     * ��һ������
     * @param byte_2
     * �ڶ�������
     * @return
     */
  	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
  		byte[] byte_3 = new byte[byte_1.length+byte_2.length];
  		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
  		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
  		return byte_3;
  	}
}
