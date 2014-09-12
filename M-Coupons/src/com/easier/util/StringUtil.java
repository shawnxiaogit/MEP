package com.easier.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: zhouxin@easier.cn
 * 字符串的处理类
 * Date: 12-11-22
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {
    /**
     * 判断是否为null或空值
     *
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断str1和str2是否相同
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == str2 || str1 != null && str1.equals(str2);
    }

    /**
     * 判断str1和str2是否相同(不区分大小写)
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串str1是否包含字符串str2
     *
     * @param str1 源字符串
     * @param str2 指定字符串
     * @return true源字符串包含指定字符串，false源字符串不包含指定字符串
     */
    public static boolean contains(String str1, String str2) {
        return str1 != null && str1.contains(str2);
    }

    /**
     * 判断字符串是否为空，为空则返回一个空值，不为空则返回原字符串
     *
     * @param str 待判断字符串
     * @return 判断后的字符串
     */
    public static String getString(String str) {
        return str == null ? "" : str;
    }
    
    
    //java二进制,字节数组,字符,十六进制,BCD编码转换2007-06-07 00:17/** *//** 
    /** * 把16进制字符串转换成字节数组 
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

 /** *//** 
     * 把字节数组转换成16进制字符串 
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

 /** *//** 
     * 把字节数组转换为对象 
     * @param bytes 
     * @return 
     * @throws IOException 
     * @throws ClassNotFoundException 
     */ 
 public static final Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException { 
     ByteArrayInputStream in = new ByteArrayInputStream(bytes); 
     ObjectInputStream oi = new ObjectInputStream(in); 
     Object o = oi.readObject(); 
     oi.close(); 
     return o; 
 }

 /** *//** 
     * 把可序列化对象转换成字节数组 
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

 public static final String objectToHexString(Serializable s) throws IOException{ 
     return bytesToHexString(objectToBytes(s)); 
 }

 public static final Object hexStringToObject(String hex) throws IOException, ClassNotFoundException{ 
     return bytesToObject(hexStringToByte(hex)); 
 }

 /** *//** 
     * @函数功能: BCD码转为10进制串(阿拉伯数据) 
     * @输入参数: BCD码 
     * @输出结果: 10进制串 
     */ 
 public static String bcd2Str(byte[] bytes){ 
     StringBuffer temp=new StringBuffer(bytes.length*2);

     for(int i=0;i<bytes.length;i++){ 
      temp.append((byte)((bytes[i]& 0xf0)>>>4)); 
      temp.append((byte)(bytes[i]& 0x0f)); 
     } 
     return temp.toString().substring(0,1).equalsIgnoreCase("0")?temp.toString().substring(1):temp.toString(); 
 }

 /** *//** 
     * @函数功能: 10进制串转为BCD码 
     * @输入参数: 10进制串 
     * @输出结果: BCD码 
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

     for (int p = 0; p < asc.length()/2; p++) { 
      if ( (abt[2 * p] >= '0') && (abt[2 * p] <= '9')) { 
       j = abt[2 * p] - '0'; 
      } else if ( (abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) { 
       j = abt[2 * p] - 'a' + 0x0a; 
      } else { 
       j = abt[2 * p] - 'A' + 0x0a; 
      }

      if ( (abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) { 
       k = abt[2 * p + 1] - '0'; 
      } else if ( (abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) { 
       k = abt[2 * p + 1] - 'a' + 0x0a; 
      }else { 
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
	 * @函数功能: BCD码转ASC码
	 * @输入参数: BCD串
	 * @输出结果: ASC码
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

 /** *//** 
     * MD5加密字符串，返回加密后的16进制字符串 
     * @param origin 
     * @return 
     */ 
 public static String MD5EncodeToHex(String origin) { 
        return bytesToHexString(MD5Encode(origin)); 
      }

 /** *//** 
     * MD5加密字符串，返回加密后的字节数组 
     * @param origin 
     * @return 
     */ 
 public static byte[] MD5Encode(String origin){ 
     return MD5Encode(origin.getBytes()); 
 }

 /** *//** 
     * MD5加密字节数组，返回加密后的字节数组 
     * @param bytes 
     * @return 
     */ 
 public static byte[] MD5Encode(byte[] bytes){ 
     MessageDigest md=null; 
     try { 
      md = MessageDigest.getInstance("MD5"); 
      return md.digest(bytes); 
     } catch (NoSuchAlgorithmException e) { 
      e.printStackTrace(); 
      return new byte[0]; 
     } 

  
 }
 
 /// <summary>
 /// BCD码转为10进制串(阿拉伯数据) 
 /// </summary>
 /// <param name="bytes">BCD码 </param>
 /// <returns>10进制串 </returns>
 public String bcd2Str2(byte[] bytes)
 {
     StringBuilder temp = new StringBuilder(bytes.length * 2);

     for (int i = 0; i < bytes.length; i++)
     {
         temp.append((byte)((bytes[i] & 0xf0) >> 4));
         temp.append((byte)(bytes[i] & 0x0f));
     }
     return temp.toString().substring(0, 1).equals("0") ? temp.toString().substring(1) : temp.toString();
 }
 
 /// <summary>
 /// 10进制串转为BCD码 
 /// </summary>
 /// <param name="asc">10进制串 </param>
 /// <returns>BCD码 </returns>
 public byte[] str2Bcd2(String asc)
 {
     int len = asc.length();
     int mod = len % 2;

     if (mod != 0)
     {
         asc = "0" + asc;
         len = asc.length();
     }

     byte[] abt = new byte[len];
     if (len >= 2)
     {
         len = len / 2;
     }

     byte[] bbt = new byte[len];
//     abt = System.Text.Encoding.ASCII.GetBytes(asc);
     int j, k;

     for (int p = 0; p < asc.length() / 2; p++)
     {
         if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9'))
         {
             j = abt[2 * p] - '0';
         }
         else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z'))
         {
             j = abt[2 * p] - 'a' + 0x0a;
         }
         else
         {
             j = abt[2 * p] - 'A' + 0x0a;
         }

         if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9'))
         {
             k = abt[2 * p + 1] - '0';
         }
         else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z'))
         {
             k = abt[2 * p + 1] - 'a' + 0x0a;
         }
         else
         {
             k = abt[2 * p + 1] - 'A' + 0x0a;
         }

         int a = (j << 4) + k;
         byte b = (byte)a;
         bbt[p] = b;
     }
     return bbt;
 }
}

