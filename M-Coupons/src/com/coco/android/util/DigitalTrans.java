package com.coco.android.util;

public class DigitalTrans {

	/**
	 * �����ַ���תASCII���ַ���
	 * 
	 * @param String
	 *            �ַ���
	 * @return ASCII�ַ���
	 */
	public static String StringToAsciiString(String content) {
		String result = "";
		int max = content.length();
		for (int i = 0; i < max; i++) {
			char c = content.charAt(i);
			String b = Integer.toHexString(c);
			result = result + b;
		}
		return result;
	}
	

	/**
	 * ʮ������ת�ַ���
	 * 
	 * @param hexString
	 *            ʮ�������ַ���
	 * @param encodeType
	 *            ��������4��Unicode��2����ͨ����
	 * @return �ַ���
	 */
	public static String hexStringToString(String hexString, int encodeType) {
		String result = "";
		int max = hexString.length() / encodeType;
		for (int i = 0; i < max; i++) {
			char c = (char) DigitalTrans.hexStringToAlgorism(hexString
					.substring(i * encodeType, (i + 1) * encodeType));
			result += c;
		}
		return result;
	}

	/**
	 * ʮ�������ַ���װʮ����
	 * 
	 * @param hex
	 *            ʮ�������ַ���
	 * @return ʮ������ֵ
	 */
	public static int hexStringToAlgorism(String hex) {
		hex = hex.toUpperCase();
		int max = hex.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = hex.charAt(i - 1);
			int algorism = 0;
			if (c >= '0' && c <= '9') {
				algorism = c - '0';
			} else {
				algorism = c - 55;
			}
			result += Math.pow(16, max - i) * algorism;
		}
		return result;
	}

	/**
	 * ʮ��ת������
	 * 
	 * @param hex
	 *            ʮ�������ַ���
	 * @return �������ַ���
	 */
	public static String hexStringToBinary(String hex) {
		hex = hex.toUpperCase();
		String result = "";
		int max = hex.length();
		for (int i = 0; i < max; i++) {
			char c = hex.charAt(i);
			switch (c) {
			case '0':
				result += "0000";
				break;
			case '1':
				result += "0001";
				break;
			case '2':
				result += "0010";
				break;
			case '3':
				result += "0011";
				break;
			case '4':
				result += "0100";
				break;
			case '5':
				result += "0101";
				break;
			case '6':
				result += "0110";
				break;
			case '7':
				result += "0111";
				break;
			case '8':
				result += "1000";
				break;
			case '9':
				result += "1001";
				break;
			case 'A':
				result += "1010";
				break;
			case 'B':
				result += "1011";
				break;
			case 'C':
				result += "1100";
				break;
			case 'D':
				result += "1101";
				break;
			case 'E':
				result += "1110";
				break;
			case 'F':
				result += "1111";
				break;
			}
		}
		return result;
	}

	/**
	 * ASCII���ַ���ת�����ַ���
	 * 
	 * @param String
	 *            ASCII�ַ���
	 * @return �ַ���
	 */
	public static String AsciiStringToString(String content) {
		String result = "";
		int length = content.length() / 2;
		for (int i = 0; i < length; i++) {
			String c = content.substring(i * 2, i * 2 + 2);
			int a = hexStringToAlgorism(c);
			char b = (char) a;
			String d = String.valueOf(b);
			result += d;
		}
		return result;
	}

	/**
	 * 
	 * @Description �ַ���ת��Ϊ16�����ַ���
	 * @author Shawn
	 * @Time 2013-10-4  ����2:25:03
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static String stringToHexString(String strPart) {

		String hexString = "";

		for (int i = 0; i < strPart.length(); i++) {

			int ch = (int) strPart.charAt(i);

			String strHex = Integer.toHexString(ch);

			hexString = hexString + strHex;

		}

		return hexString;

	}

	/**
	 * ��ʮ����ת��Ϊָ�����ȵ�ʮ�������ַ���
	 * 
	 * @param algorism
	 *            int ʮ��������
	 * @param maxLength
	 *            int ת�����ʮ�������ַ�������
	 * @return String ת�����ʮ�������ַ���
	 */
	public static String algorismToHEXString(int algorism, int maxLength) {
		String result = "";
		result = Integer.toHexString(algorism);

		if (result.length() % 2 == 1) {
			result = "0" + result;
		}
		return patchHexString(result.toUpperCase(), maxLength);
	}

	/**
	 * �ֽ�����תΪ��ͨ�ַ�����ASCII��Ӧ���ַ���
	 * 
	 * @param bytearray
	 *            byte[]
	 * @return String
	 */
	public static String bytetoString(byte[] bytearray) {
		String result = "";
		char temp;

		int length = bytearray.length;
		for (int i = 0; i < length; i++) {
			temp = (char) bytearray[i];
			result += temp;
		}
		return result;
	}

	/**
	 * �������ַ���תʮ����
	 * 
	 * @param binary
	 *            �������ַ���
	 * @return ʮ������ֵ
	 */
	public static int binaryToAlgorism(String binary) {
		int max = binary.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = binary.charAt(i - 1);
			int algorism = c - '0';
			result += Math.pow(2, max - i) * algorism;
		}
		return result;
	}

	/**
	 * ʮ����ת��Ϊʮ�������ַ���
	 * 
	 * @param algorism
	 *            int ʮ���Ƶ�����
	 * @return String ��Ӧ��ʮ�������ַ���
	 */
	public static String algorismToHEXString(int algorism) {
		String result = "";
		result = Integer.toHexString(algorism);

		if (result.length() % 2 == 1) {
			result = "0" + result;

		}
		result = result.toUpperCase();

		return result;
	}

	/**
	 * HEX�ַ���ǰ��0����Ҫ���ڳ���λ�����㡣
	 * 
	 * @param str
	 *            String ��Ҫ���䳤�ȵ�ʮ�������ַ���
	 * @param maxLength
	 *            int �����ʮ�������ַ����ĳ���
	 * @return ������
	 */
	static public String patchHexString(String str, int maxLength) {
		String temp = "";
		for (int i = 0; i < maxLength - str.length(); i++) {
			temp = "0" + temp;
		}
		str = (temp + str).substring(0, maxLength);
		return str;
	}

	/**
	 * ��һ���ַ���ת��Ϊint
	 * 
	 * @param s
	 *            String Ҫת�����ַ���
	 * @param defaultInt
	 *            int ��������쳣,Ĭ�Ϸ��ص�����
	 * @param radix
	 *            int Ҫת�����ַ�����ʲô���Ƶ�,��16 8 10.
	 * @return int ת���������
	 */
	public static int parseToInt(String s, int defaultInt, int radix) {
		int i = 0;
		try {
			i = Integer.parseInt(s, radix);
		} catch (NumberFormatException ex) {
			i = defaultInt;
		}
		return i;
	}

	/**
	 * ��һ��ʮ������ʽ�������ַ���ת��Ϊint
	 * 
	 * @param s
	 *            String Ҫת�����ַ���
	 * @param defaultInt
	 *            int ��������쳣,Ĭ�Ϸ��ص�����
	 * @return int ת���������
	 */
	public static int parseToInt(String s, int defaultInt) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			i = defaultInt;
		}
		return i;
	}

	/**
	 * ʮ�������ַ���תΪByte����,ÿ����ʮ�������ַ�תΪһ��Byte
	 * 
	 * @param hex
	 *            ʮ�������ַ���
	 * @return byte ת�����
	 */
	public static byte[] hexStringToByte(String hex) {
		int max = hex.length() / 2;
		byte[] bytes = new byte[max];
		String binarys = DigitalTrans.hexStringToBinary(hex);
		for (int i = 0; i < max; i++) {
			bytes[i] = (byte) DigitalTrans.binaryToAlgorism(binarys.substring(
					i * 8 + 1, (i + 1) * 8));
			if (binarys.charAt(8 * i) == '1') {
				bytes[i] = (byte) (0 - bytes[i]);
			}
		}
		return bytes;
	}

	/**
	 * ʮ�����ƴ�ת��Ϊbyte����
	 * 
	 * @return the array of byte
	 */
	public static final byte[] hex2byte(String hex)
			throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
			String swap = "" + arr[i++] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();
		}
		return b;
	}

	/**
	 * �ֽ�����ת��Ϊʮ�������ַ���
	 * 
	 * @param b
	 *            byte[] ��Ҫת�����ֽ�����
	 * @return String ʮ�������ַ���
	 */
	public static final String byte2hex(byte b[]) {
		if (b == null) {
			throw new IllegalArgumentException(
					"Argument b ( byte array ) is null! ");
		}
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}
	
	
}