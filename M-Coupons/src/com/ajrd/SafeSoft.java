package com.ajrd;

public class SafeSoft {
	/**
	 * 加密
	 */
	public static final int ENCRYPT = 0;
	/**
	 * 解密
	 */
	public static final int DECRYPT = 1;
	
	static {

		System.loadLibrary("AjrdSafeSoft");
	}

	/**
	 * 获取MAC信息鉴别码
	 * 
	 * @param mac_key
	 *            Mac校验密码
	 * @param data_len
	 *            要生成Mac鉴别码的数据长度
	 * @param data
	 *            实际的数据字节 要生成Mac鉴别码的数据
	 * @return Mac鉴别码
	 */
	public native static byte[] GenerateMac(byte[] mac_key, int data_len,
			byte[] data);

	/**
	 * 密码加密
	 * 
	 * @param key
	 *            秘钥字节数组
	 * @param pwd
	 *            密码字节数组
	 * @param card_num
	 *            卡号字节数组
	 * @return 加密后的密码字节数组(16进制数字)
	 */
	public native static byte[] EncryptPin(byte key[], byte pwd[],
			byte card_num[]);

	public native static byte[] Byte2Hex(byte s1[], int i1);

	public native static byte[] Hex2Byte(byte s1[], int i1);

	/**
	 * Des加解密 数据和密钥是十六进制表示的，16个字节。
	 * 
	 * @param data
	 *            需要加密或解密的数据
	 * @param key
	 *            密钥
	 * @param type
	 *            0 - 加密 1-解密
	 * @return
	 */
	public native static byte[] Des(byte data[], byte key[], int type);
	
	/**
	 * 
	 * @param pwd
	 * @return
	 */
	public native static byte[] EncryptTlr( byte pwd[] );

	public static void main(String[] args) {
		String a1, a2, a3, a4, a5, a6, a7;
		int len1, len2, len3, len4, len5;
		;
		byte b1[], b2[], b3[], b4[], b5[], b6[], b7[];
		;
		/*
		 * byte b5[] = new byte[16];
		 */

		a1 = "1234567890123456";
		a2 = "1234567890";
		a3 = "223456";
		a4 = "6225100010009999";
		a5 = "abcdefgh";
		a6 = "3132333435363738";
		a7 = "0123450123401230";

		b1 = a1.getBytes();
		b2 = a2.getBytes();
		b3 = a3.getBytes();
		b4 = a4.getBytes();
		b5 = a5.getBytes();
		b6 = a6.getBytes();
		b7 = a7.getBytes();

		len1 = 10;
		len2 = 8;
		len3 = 16;
		len4 = 0;
		len5 = 1;

		/*
		 * b5 = new byte();
		 */

		System.out.println(new String(SafeSoft.EncryptPin(b1, b3, b4)));
		System.out.println(new String(SafeSoft.GenerateMac(b1, len1, b2)));
		System.out.println(new String(SafeSoft.Byte2Hex(b5, len2)));
		System.out.println(new String(SafeSoft.Hex2Byte(b6, len3)));

		System.out.println(new String(SafeSoft.Des(b7, b7, len4)));
		System.out.println(new String(SafeSoft.Des(b7, b7, len5)));
	}

	// MAC：7789AFAC888BA28B
	// 加密后密码：4F6EEA3427EAB667
	// 4F6EEA3427EAB667
	/**
	 * 全报文加密
	 * @param datas
	 * 明文，不包含长度
	 * @param len
	 * 长度：特别注意计算方式如下
	 * 数组的长度计算方法如下：
		大于len+2的最小8的倍数，如len=6，则返回长度为8；如len=7，则返回长度为16。
	 * @return
	 * 密文，传送到服务器还需要加上4字节的长度
	 */
	public native static byte[] EncryptMsg(byte datas[], int len);

	/**
	 * 全报文解密
	 * @param datas
	 * 密文
	 * @param len
	 * 长度：特别注意计算方式如下
	 * 如传入的长度参数为len（len一定是8的倍数），数组的长度即为len即可，不过实际的长度可能比len小，
		但是只要根据报文解析完成即可，多余的报文不再解析。
	 * @return
	 * 
	 */
	public native static byte[] DecryptMsg(byte datas[], int len);

}
