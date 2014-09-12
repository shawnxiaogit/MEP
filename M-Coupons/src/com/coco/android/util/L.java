package com.coco.android.util;

public class L {
	public static boolean isDebug = false;
	
	public static boolean LoginDebug = false;

	public static boolean ConsumeDebug = false;
	
	//获取验证码调试
	public static boolean GetSyntaCodeDebug = false;
	/**
	 * 查询调试
	 */
	public static boolean QUERY_DEBUG = false;
	/**
	 * 获取消费、充值列表调试
	 */
	public static boolean GET_DEAL_LIST_DEBUG  = false;
	
	
	/**
	 * 如果是MEP老板的话，可以测试查看所有员工的交易汇总
	 */
	public static final boolean MEP_BOSS_TEST = false;
	
	/**
	 * 没有网络调试
	 */
	public static boolean NO_NET_DEBUG = false;
	
	/**
	 * 没有充值和充值撤销数据调试
	 */
	public static boolean NOT_RECHARGE_DEBUG = false;
	
	/**
	 * 没卡调试
	 */
	public static boolean NO_CARD_DEBUG = false;
	
	/**
	 * 没有手机
	 */
	public static boolean NO_PHONE = false;
	
	
	/**
	 * 短信网关不可用
	 */
	public static boolean NO_DUAN_XIN_WANG_GUAN = false;
	
	
	//初始化获取验证码
	public static boolean INIT_DEBUG = false;
	
	/**
	 * 消费的时候是否保存验证码信息
	 */
	public static boolean IS_SAVE_CHECK_CODE = false;
	
	/**
	 * IP切换调试
	 */
	public static boolean IP_SWITCH_DEBUG = false;
	
	public static void v(String tag, String msg) {
		if (isDebug)
			android.util.Log.v(tag, msg);
	}

	public static void v(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.v(tag, msg, t);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			android.util.Log.d(tag, msg);
	}

	public static void d(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.d(tag, msg, t);
	}

	public static void i(String tag, String msg) {
		if (isDebug)
			android.util.Log.i(tag, msg);
	}

	public static void i(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.i(tag, msg, t);
	}

	public static void w(String tag, String msg) {
		if (isDebug)
			android.util.Log.w(tag, msg);
	}

	public static void w(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.w(tag, msg, t);
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			android.util.Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.e(tag, msg, t);
	}
}
