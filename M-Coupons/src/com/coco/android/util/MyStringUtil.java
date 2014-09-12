package com.coco.android.util;


import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * @Descriptio TODO
 * @author Shawn
 * @Time 2013-9-24  下午4:55:58
 */
public class MyStringUtil {
	/**
	 * 
	 * @Description 去掉字符串中的空格
	 * @author Shawn
	 * @Time 2013-9-18  下午2:42:34
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
	 * @Description 替换字符串前后空格
	 * @author Shawn
	 * @Time 2013-9-18  下午2:41:08
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static String trim(String s) {
		  int i = s.length();// 字符串最后一个字符的位置
		  int j = 0;// 字符串第一个字符
		  int k = 0;// 中间变量
		  char[] arrayOfChar = s.toCharArray();// 将字符串转换成字符数组
		  while ((j < i) && (arrayOfChar[(k + j)] <= ' '))
		   ++j;// 确定字符串前面的空格数
		  while ((j < i) && (arrayOfChar[(k + i - 1)] <= ' '))
		   --i;// 确定字符串后面的空格数
		  return (((j > 0) || (i < s.length())) ? s.substring(j, i) : s);// 返回去除空格后的字符串
	 }
	
	/**
	 * 
	 * @Description 获取没有空格的字符串
	 * @author Shawn
	 * @Time 2013-9-24  下午4:58:08
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
	 * @Description 16进制转化为二进制
	 * @author Shawn
	 * @Time 2013-9-18  上午10:36:33
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
	
	public static String string2ASCII(String s) {// 字符串转换为ASCII码
		StringBuilder sb = new StringBuilder();
		// String s="990000";//字符串

		char[] chars = s.toCharArray(); // 把字符中转换为字符数组
//		System.out.println("\n\n汉字 ASCII\n----------------------");
		for (int i = 0; i < chars.length; i++) {// 输出结果
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
