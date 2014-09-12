package com.coco.android.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import android.text.TextUtils;


/**
 * �ı������ࡣ
 * 
 * @author super
 * 
 */
public final class StringUtil {

//    private static final Pattern PHONE_NO = Pattern.compile("^[1]\\d{10}$");
    
    
    private static final Pattern PHONE_NO = Pattern.compile("^13[0-9]{9}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}$");
    
    
    public static boolean isPhoneValid(final String phone){
    	if (TextUtils.isEmpty(phone)) {
			return false;
		}
    	final Matcher matcher = PHONE_NO.matcher(phone);
    	return matcher.matches();
    }
    
    /**
	 * The price into a string with a decimal point Only used in the display
	 * above !!!
	 * 
	 * @param price
	 *            (fen)
	 * @return converted price
	 */
	public static String convertMoneyForDisplay(String price) {
		String fenStr = null;
		boolean isNegative = false;
		long longPrice = Long.parseLong(price);
		if (longPrice < 0) {
			isNegative = true;
			longPrice = -longPrice;
		}
		int fen = (int) (longPrice % 100);

		fenStr = Integer.toString(fen);

		if (longPrice % 100 < 10) {
			fenStr = "0" + fenStr;
		} else {

		}
		if (isNegative) {
			return "��-" + Long.toString(longPrice / 100) + "." + fenStr;
		} else {
			return "��" + Long.toString(longPrice / 100) + "." + fenStr;
		}
	}
	
    /**
	 * The price into a string with a decimal point Only used in the display
	 * above !!!
	 * 
	 * @param price
	 *            (fen)
	 * @return converted price
	 */
	public static String convertMoneyForDisplay2(String price) {
		String fenStr = null;
		boolean isNegative = false;
		long longPrice = Long.parseLong(price);
		if (longPrice < 0) {
			isNegative = true;
			longPrice = -longPrice;
		}
		int fen = (int) (longPrice % 100);

		fenStr = Integer.toString(fen);

		if (longPrice % 100 < 10) {
			fenStr = "0" + fenStr;
		} else {

		}
		
		return Long.toString(longPrice / 100) + "." + fenStr;
	}
	
	public static boolean gbk(String str) {
//		char[] chars = str.toCharArray();
//		boolean isGB2312 = false;
//		for (int i = 0; i < chars.length; i++) {
//			byte[] bytes = ("" + chars[i]).getBytes();
//			if (bytes.length == 2) {
//				int[] ints = new int[2];
//				ints[0] = bytes[0] & 0xff;
//				ints[1] = bytes[1] & 0xff;
//				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
//						&& ints[1] <= 0xFE) {
//					isGB2312 = true;
//					break;
//				}
//			}
//		}
//		return isGB2312;
		
		 String regEx = "[\\u4e00-\\u9fa5]";      
         Pattern p = Pattern.compile(regEx);      
         Matcher m = p.matcher(str);      
         if (m.find()) {      
        	 return true;
         }
         return false;
	}

	/**
	 * ��->Ԫת�� 10000��->100Ԫ
	 * @param num 
	 * @return
	 */
	public static String longToIntString(final long num) {
		return num/100+"";
//		return s.substring(0, s.length() - 2);
	}

	/**
	 * ��->Ԫת��  10000��->100.00Ԫ
	 * @param num
	 * @return
	 */
	public static String longToDoubleString(final long num) {
		return (double)num/100+"";
//		String p = String.valueOf(num);
//		return p.substring(0, p.length() - 2) + "." + p.substring(p.length() - 2);
	}
	
	/**
	 * �ָ��ַ�������
	 * @param str
	 * @param length
	 * @return
	 */
    public static String splitString(String str,int length){
    	StringBuilder ret = new StringBuilder();
    	if(length>=str.length()){
    		ret.append(str);
    		ret.append(" ");
    	}else{
    		int strLength = str.length();
    		for(int i = 0;i<strLength ;i+=length){
    			if(i+length<strLength){
    				ret.append(str.substring(i,i+length)); 
    				ret.append(" ");
    			}else{
    				ret.append(str.substring(i,strLength)); 
    			}
    		}
    	}
    	return ret.toString();
    }
    
    /**
	 * ���׽���ʽ��
	 */
    public static String myMoneyFormat(String money) {
		return (Double.parseDouble(money) / 100.00) + "";
	}
    
    /**
     * ƽ̨����ʽ����2λС���ַ���
     */
    public static String backDataFomat(String data){
    	if(data == null || "".equals(data)){
    		return "0.00";
    	}
    	
    	try{
    		return new java.text.DecimalFormat("0.00").format(Double.parseDouble(data));
    	}catch (Exception e) {
    		e.printStackTrace();
    		return "0.00";
		}
    }
    
    /**
	 * �����ַ�����ʽ��
	 */
	public static String myDateFormat(String date) {
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8) + " " + date.substring(8, 10) + ":"
				+ date.substring(10, 12) + ":" + date.substring(12, 14);
	}
	
	/**
	 * ���п����ַ�����ʽ��
	 */
	public static String myCardNumFormat(String num) {
		StringBuilder sb = new StringBuilder("");
		sb.append(num.substring(0, 4));
		for(int i=0;i<num.length()-8;i++){
			sb.append("*");
		}
		sb.append(num.substring(num.length()-4));
		return splitString(sb.toString(), 4);
	}
    

	/**
	 * �ֻ������б�
	 * @param array
	 * @return
	 */
    private static List<String> array2list(String array[]) {
        int len = array.length;
        List<String> list = new ArrayList<String>();
        
        for(int index = len-1; index >= 0; index--) {
        	list.add(array[index]);
        }
        return list;
    }
    
    //CharMode����  
  	//����ĳ���ַ���������һ��.  
  	private static int CharMode(char iN) {
  		if (iN >= 48 && iN <= 57) //����  
  			return 1;
  		if (iN >= 65 && iN <= 90) //��д��ĸ  
  			return 2;
  		if (iN >= 97 && iN <= 122) //Сд  
  			return 4;
  		else
  			return 8; //�����ַ�  
  	}
  	
  	//bitTotal����  
  	//�������ǰ���뵱��һ���ж�����ģʽ  
  	private static int bitTotal(int num) {
  		int modes = 0;
  		for (int i = 0; i < 4; i++) {
  			if ((num & 1) > 0)
  				modes++;
  			num >>>= 1;
  		}
  		return modes;
  	}
    
    public static int checkStrong(String pwdStr){
    	if (pwdStr.length() < 6)
			return 0; //����̫��  
		int Modes = 0;
		for (int i = 0; i < pwdStr.length(); i++) {
			//����ÿһ���ַ������ͳ��һ���ж�����ģʽ.  
			Modes |= CharMode(pwdStr.charAt(i));
		}
		return bitTotal(Modes);
    }
}
