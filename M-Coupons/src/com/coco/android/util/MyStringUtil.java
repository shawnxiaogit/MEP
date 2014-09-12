package com.coco.android.util;


import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ������
 * @Descriptio TODO
 * @author Shawn
 * @Time 2013-9-24  ����4:55:58
 */
public class MyStringUtil {
	/**
	 * 
	 * @Description ȥ���ַ����еĿո�
	 * @author Shawn
	 * @Time 2013-9-18  ����2:42:34
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static String replaceBlank(String str) {
		  String dest = "";
		  if (str!=null) {
		   Pattern p = Pattern.compile("\\s*|t|r|n");
		   Matcher m = p.matcher(str);
		   dest = m.replaceAll("");
		  }
		  return dest;
	 }
	
	/**
	 * 
	 * @Description �滻�ַ���ǰ��ո�
	 * @author Shawn
	 * @Time 2013-9-18  ����2:41:08
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static String trim(String s) {
		  int i = s.length();// �ַ������һ���ַ���λ��
		  int j = 0;// �ַ�����һ���ַ�
		  int k = 0;// �м����
		  char[] arrayOfChar = s.toCharArray();// ���ַ���ת�����ַ�����
		  while ((j < i) && (arrayOfChar[(k + j)] <= ' '))
		   ++j;// ȷ���ַ���ǰ��Ŀո���
		  while ((j < i) && (arrayOfChar[(k + i - 1)] <= ' '))
		   --i;// ȷ���ַ�������Ŀո���
		  return (((j > 0) || (i < s.length())) ? s.substring(j, i) : s);// ����ȥ���ո����ַ���
	 }
	
	/**
	 * 
	 * @Description ��ȡû�пո���ַ���
	 * @author Shawn
	 * @Time 2013-9-24  ����4:58:08
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static String getNoBlankString(String s){
		String result = null;
		String s1=replaceBlank(s);
		result = trim(s1);
		return result;
	} 
	
	/**
	 * 
	 * @Description 16����ת��Ϊ������
	 * @author Shawn
	 * @Time 2013-9-18  ����10:36:33
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static String hexToBin(String hex){
	    String bin = "";
	    String binFragment = "";
	    int iHex;
	    hex = hex.trim();
	    hex = hex.replaceFirst("0x", "");

	    for(int i = 0; i < hex.length(); i++){
	        iHex = Integer.parseInt(""+hex.charAt(i),16);
	        binFragment = Integer.toBinaryString(iHex);

	        while(binFragment.length() < 4){
	            binFragment = "0" + binFragment;
	        }
	        bin += binFragment;
	    }
	    return bin;
	}
	
	public static String string2ASCII(String s) {// �ַ���ת��ΪASCII��
		StringBuilder sb = new StringBuilder();
		// String s="990000";//�ַ���

		char[] chars = s.toCharArray(); // ���ַ���ת��Ϊ�ַ�����
//		System.out.println("\n\n���� ASCII\n----------------------");
		for (int i = 0; i < chars.length; i++) {// ������
			sb.append((int)chars[i]);
//			System.out.println(" " + chars[i] + " " + (int) chars[i]);
		}
//		System.out.println(sb.toString());
		return sb.toString();
	}
	
	
	
	
	public static byte[] str2cbcd(String s) {
		if (s.length() % 2 != 0) {
			s = "0" + s;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		char[] cs = s.toCharArray();
		for (int i = 0; i < cs.length; i += 2) {
			int high = cs[i] - 48;
			int low = cs[i + 1] - 48;
			baos.write(high << 4 | low);
		}
		return baos.toByteArray();
	}

	public static String cbcd2string(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int h = ((b[i] & 0xff) >> 4) + 48;
			sb.append((char) h);
			int l = (b[i] & 0x0f) + 48;
			sb.append((char) l);
		}
		return sb.toString();
	}
}
