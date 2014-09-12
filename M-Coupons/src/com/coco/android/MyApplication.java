package com.coco.android;

import java.text.SimpleDateFormat;
import java.util.Date;


import com.ajrd.SafeSoft;
import com.coco.android.bean.BaseNode;
import com.coco.android.util.L;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class MyApplication extends Application {
	
	public static final String BASE_URL = "http://www.shenmei.com/";
	
	
	//���ܴ�����־������
	public static String RECIVE_LOG_EMAIL = "562779246@qq.com";
	public static String TMP_USER_NAME = "";
	
	
//	public static final String MAIN_KEY = "3836353132353235";
	
	//Ĭ��IMSI��
	public static final String DEFAULT_IMSI = "000000000000000";
	
	public static final String MAIN_KEY = "2013110220131103";
	
	public static final String MAIN_KEY2 = "0000000000000000";
	
	//Ĭ��Mac��������
	public static final String DEFAULT_MAC_KEY = "1234567890123456";
	
	//�����¼������
	public static String LOGIN_PWD = "";
	
	//mac������Կ
	public static byte[] MAC_KEY = null;
	//���������Կ
	public static byte[] PIN_KEY = null;
	
	
	//===========������ѯ���=============
	public static String CHE_XIAO_VALUE_60 = "";
	public static String CHE_XIAO_CARD_NUM = "";
	public static String CHE_XIAO_START_TIME = "";
	public static String CHE_XIAO_END_TIME = "";
	//===========������ѯ���=============
	
	//��ѯ������ϸ���
	/**
	 * ��ѯ������ϸ ��ǰ���ؼ�¼λ�� ,0��ʾ��1�С�
	 */
	public static String QUERY_DEAL_DETAIL_CURRENT_RETURN_FLAG = "1"; 
	/**
	 * ��ѯ������ϸ ���η��ؼ�¼�� 
	 */
	public static String QUERY_DEAL_DETAIL_QUERY_CURRENT_RETURN_COUNT = "10";
	
	/**
	 * ��ѯ������ϸ ��Ϣ����
	 */
	public static final String QUERY_DEAL_DETAIL_MTI = "1003";
	
	/**
	 * ��ѯ������Ϣ ��Ϣ����
	 */
	public static final String QUERY_HUI_ZONG_MTI = "1004";
	
	/**
	 * ���׳�����ѯ ��Ϣ����
	 */
	public static final String DEAL_CANCLE_QUERY_MTI = "1001";
	
	/**
	 * ���ѳ�����ѯ ��ǰ���ؼ�¼λ�� ,0��ʾ��1�С�
	 */
	public static String DEAL_CANCLE_CURRENT_RETURN_FLAG = "1";
	public static String DEAL_CANCLE_CURRENT_RETURN_FLAG2 = "0";
	/**
	 * ���ѳ�����ѯ ���η��ؼ�¼�� 
	 */
	public static String DEAL_CANCLE_CURRENT_RETURN_COUNT = "10";
	//��������  1 �C ���� 	2 �C ��ֵ

	/**
	 * ��������	 1 �C ���� 	2 �C ��ֵ
	 */
	public static String DEAL_TYPE_CONSUME = "1";
	
	/**
	 * ����ǰ׺
	 */
	public static final String CARD_PREFIX = "90093201";
	/**
	 * ����ԱȨ��
	 */
	public static final int AUTHORITY_MANAGER = 9;
	/**
	 * ����Ա
	 */
	public static final String MANAGER = "2";
	/**
	 * ��ͨ����ԱȨ��
	 */
	public static final int AUTHORITY_EMPLOYEE = 8;
	/**
	 * ��ͨԱ��
	 */
	public static final String EMPLOYEE = "1";
	
	/**
	 * ��ѯ���ֻ���Ϣ
	 */
	public static String PHONE_INFO2 = "";
	
	/**
	 * ��ѯ��ʼʱ��
	 */
	public static String QUERY_START_TIME = "";
	/**
	 * ��ѯ����ʱ��
	 */
	public static String QUERY_END_TIME = "";
	
	/**
	 * ��ѯ��ʼ����
	 */
	public static String QUERY_START_MIN = "";
	
	/**
	 * ��ѯ��������
	 */
	public static String QUERY_END_MIN = "";
	
	
	/**
	 * ǩ�����صĲ���ԱȨ��
	 */
	public static int myAuthority = 0;
	
	public static int myMepAuority = -1;
	
	/*��2λ����ѯ����Ȩ��
		0-��Ȩ��
		1-��Ȩ��
	*/
	public static final int MEP_EMPLOYEE_AUTHORITY = 0;
	public static final int MEP_BOOSS_AUTHORITY = 1;
	
	
	//��ѯ����Ա������ϸ����ѯ��ʶ:1-�Լ�2-����   ��Ҫǩ�����������ʶ

	public static final String GET_OPERATER_DETAIL_QUERY_FLAG = "1";
	
	//��ѯ����Ա������ϸ����ѯ���:C-��ֵ��ϸD-������ϸ
	public static final String GET_OPERATER_DETAIL_QUERY_TYPE = "D";
	
	//================4.2.19. ��ѯMEP��ϸ��Ϣ===================
	public static final String QUERY_MEP_DEAL_MTI = "1005";
	
	//��ǰ���ؼ�¼λ��
	public static final String QUERY_MEP_DEAL_CURRENT_RETURN_POSI = "1";
	//���η��ؼ�¼��
	public static final String QUERY_MEP_DEAL_CURRENT_RETURN_COUNT ="10";
	
	//���  d-�跽 c-����
	
	public static final String QUERY_MEP_DEAL_QUERY_TYPE_C = "c";
	
	public static final String QUERY_MEP_DEAL_QUERY_TYPE_D = "d";
	
	//================4.2.19. ��ѯMEP��ϸ��Ϣ===================
	/**
	 * �����ն˺ţ��̻���SP������
	 */
	public static final String SP_TERM_COMM = "SP_TERM_COMM";
	/**
	 * SP�ն˺�key
	 */
	public static final String SP_TERM_NUM_KEY = "SP_TERM_NUM_KEY";
	/**
	 * SP�̻���key
	 */
	public static final String SP_COMM_NUM_KEY = "SP_COMM_NUM_KEY";
	
	
	//�����˺�
	public static final String SP_ACCOUNT = "SP_ACCOUNT";
	//�����˺ŵ�key
	public static final String SP_ACCOUNT_KEY = "SP_ACCOUNT_KEY";
	
	
	//�����û��Ƿ��״�ʹ��APP SP������
	public static final String SP_IS_USE_APP_FIRST       = "SP_IS_USE_APP_FIRST";
	//�����û��Ƿ��״�ʹ��APP��key
	public static final String SP_IS_USE_APP_FIRST_KEY   = "SP_IS_USE_APP_FIRST_KEY";
	
	
	
	//�����û�ʹ�õ��ֻ��ż��ֻ����豸��Ϣ
	public static final String SP_PHONE_INFO  			 = "SP_PHONE_INFO";
	
	
	//�����û�ʹ�õ��ֻ��ż��ֻ����豸��Ϣ key
	public static final String SP_PHONE_INFO_KEY  		 = "SP_PHONE_INFO_KEY";
	
	
	
	//�����Ƿ��޸�������
	public static final String SP_HAS_MODIFY_LOGIN_PWD = "SP_HAS_MODIFY_LOGIN_PWD";
	//�����Ƿ��޸�������key
	public static final String SP_HAS_MODIFY_LOGIN_PWD_KEY = "SP_HAS_MODIFY_LOGIN_PWD_KEY";
	
	//�ֻ���Ϣ
	public static String PHONE_INFO = "";
	//Ĭ���ֻ���Ϣ
	public static final String DEFAULT_PHONE_INFO = "00000000000000000000000000000";
	
	
	
	
	//��˾�Ĳ��Ե�ַ
//	public static String IP = "211.147.70.11";
	
//	public static final String IP_AJRD = "211.147.70.11";
	
	//���ֵĵ�ַ
	public static final String IP = "221.181.95.106";
	
	//�ɿڿ���IP��ַ����
//	public static final String IP = "116.228.232.11";
	
	//������ʽ�����˿�
	public static final int PORT = 7926;
	
	//���ֲ��Զ˿�
//	public static final int PORT = 7926;
	
	//�����ն˺�
	public static String TERM_NUM = null;
	public static final String DEFAULT_TERM_NUM = "00000002";
	//�����̻���
	public static String COMM_NUM = null;
	public static final String DEFAULT_COMM_NUM ="999073154110001";
	
	
	//���Կ���
	public static final String CARD_NUM = "9009320100000011";
	//��������
	public static final String CARD_PWD ="877729";
	
	//���Կ���2
	public static final String CARD_NUM2 = "9009320100011166";
	//��������2
	public static final String CARD_PWD2 ="111111";
	
//	public static final String PHONE_NUM				 = "13301797863";
	public static String PHONE_NUM				 = "";
	
	
	//Ĭ����������
	public static final String DEFAULT_CONSUME_KEY = "123456";
	
	//Ĭ���˺�
	public static final String[] USER_NAME_LIST = new String[]{
		"000000","000001"
	};
	//Ĭ������
	public static final String PWD_DEFAULT = "111111";
	
	//�����Ƿ��Ǵ�����Ҳ����Ĺ�������ҳ
	public static final String SP_JUM_FOM_CON = "SP_JUM_FOM_CON";
	//���������ҳ����빦������ҳ�ļ�
	public static final String SP_JUM_FOM_CON_KEY = "SP_JUM_FOM_CON_KEY";
	
	//�����û����������SP����
	public static final String SP_USER_NAM_PWD = "SP_USER_NAM_PWD";
	
	//�����Ƿ��Ķ���������SP����
	public static final String SP_IS_READ_FUN_GUID = "SP_IS_READ_FUN_GUID";
	
	//�����Ƿ��Ķ��������ļ�
	public static final String SP_KEY_READ_FUN_GUID = "SP_KEY_READ_FUN_GUID";
	
	/**
	 * ��½�ӿڵ�ַ
	 */
	public static final String LOGIN_URL          = BASE_URL + "login";
	
	/**
	 * ע��ӿڵ�ַ
	 */
	public static final String REGISTER_URL       = BASE_URL + "register";
	
	/**
	 * ��ѯ���ӿڵ�ַ
	 */
	public static final String QUERY_REMAIN_MONEY_URL = BASE_URL + "query_remain_money";
	
	/**
	 * ��ȡ��̬��֤��ӿڵ�ַ
	 */
	public static final String GET_DYNAMIC_CODE_URL    = BASE_URL + "get_dynamic_code";
	/**
	 * ���ѽӿڵ�ַ
	 */
	public static final String CONSUME_URL        = BASE_URL + "consume";
	
	
	
	//=====================ǩ���������====================
	/**
	 * ǩ����Ϣ����
	 */
	public static final String LOGIN_MTI = "0800";
	/**
	 * ǩ����λԪ��
	 */
	public static final String LOGIN_MAIN_ELEMENT = "2038000000C01011";
	/**
	 * ǩ���������
	 */
	public static final String LOGIN_HANDLE_CODE = "990000";
	
	//=====================ǩ���������====================
	
	//===================�޸����뱨�����==============
	
	public static final String MODIFY_PWD_MTI = "0800";
	public static final String MODIFY_PWD_MAIN_ELEMENT = "";
	public static final String MODIFY_PWD_HANDLE_CODE = "993000";
	//===================�޸����뱨�����==============
	
	
	//=====================��ѯ�������====================
	/**
	 * ��ѯ�����Ϣ����
	 */
	public static final String QUERY_MTI = "0100";
	/**
	 * ��ѯ�����λԪ��
	 */
	public static final String QUERY_MAIN_ELEMENT = "603C060030C01001";
	//31A000-����ѯ,31A080-���ֲ�ѯ
	/**
	 * ��ѯ������
	 */
	public static final String QUERY_HANDLE_CODE = "31A000";
	
	/**
	 * POS���뷽ʽ
	 */
	public static final String POS_INPUT_TYPE = "011";
	
	//=====================��ѯ�������====================
	
	
	//=====================���ѱ������====================
	/**
	 * ������Ϣ����
	 */
	public static final String CONSUME_MTI = "0200";
	//���� �������  00A000-��ͨ����
	public static final String CONSUME_HAND_CODE1 = "00A000";
	//���� �������  00A080-��������
	public static final String CONSUME_HAND_CODE2 = "00A080";
	
	//����Ч��
	public static String CONSUME_CARD_DEAD_TIME = "9999";
	
	//POS���뷽ʽ   01  - �ֹ�
	public static String POS_INPUT_TYPE1	 = "001";
	//POS���뷽ʽ   02  - ����
	public static String POS_INPUT_TYPE2	 = "002";
	//POS���뷽ʽ   05  - ���ɵ�·��������Ϣ�ɿ�
	public static String POS_INPUT_TYPE3	 = "005";
	//POS���뷽ʽ  95  - ���ɵ�·��������Ϣ���ɿ�
	public static String POS_INPUT_TYPE4	 = "095";

	//��˳���
	public static String CARD_ORDER_NUM      = "123";
	
	//=====================���ѱ������====================
	
	
	//=====================���ѳ����������====================
	
	/**
	 * ���ѳ�����Ϣ����
	 */
	public static final String CONSUME_CANCLE_MTI = "0200";
	/**
	 * ���ѳ�����λԪ��
	 */
	public static final String CONSUME_CANCLE_MAIN_ELEMENT = "";
	/**
	 * ���ѳ���������
	 */
	public static final String CONSUME_CANCLE_HANDLE_COND = "20A000";
	
	
	//=====================���ѳ����������====================
	
	
	//=====================��ȡ��̬��֤�뱨�����====================
	
	/**
	 * ��ȡ��̬��֤����Ϣ����
	 */
	public static final String GET_SYTA_MTI 			= "0800";
	/**
	 * ��ȡ��֤����λԪ��
	 */
	public static final String GET_SYTA_MAIN_ELEMENT 	= "2038000000C00011";
	/**
	 * ��ȡ��֤�봦����
	 */
	public static final String GET_SYTA_HANDLE_CODE     = "992000";
	
	//=====================��ȡ��̬��֤�뱨�����====================
	
	
	//=====================��ȡ���Ѷ�̬��֤�뱨�����====================
	
	/**
	 * ��ȡ���Ѷ�̬��֤����Ϣ����
	 */
	public static final String GET_SYTA_CONSUME_CODE_MTI = "0800";
	/**
	 * ��ȡ���Ѷ�̬��֤����λԪ��
	 */
	public static final String GET_SYTA_CONSUME_CODE_MAIN_ELEMENT = "2038000000C00011";
	/**
	 * ��ȡ���Ѷ�̬��֤�봦����
	 */
	public static final String GET_SYTA_CONSUME_CODE_HANDLE_CODE = "992100";
	
	
	//=====================��ȡ���Ѷ�̬��֤�뱨�����====================
	
	
	
	//=====================��ȡ�ͻ���Ϣ�б������====================
		/**
		 * ��ѯ�ͻ���Ϣ��Ϣ����
		 */
		public static final String CUSTOMER_INFO_LIST_MTI = "0800";
		/**
		 * ��ѯ�ͻ���Ϣ��λԪ��
		 */
		public static final String CUSTOMER_INFO_LIST_MAIN_ELEMENT = "2038000000C00019";
		/**
		 * ��ѯ�ͻ���Ϣ������
		 */
		public static final String CUSTOMER_INFO_LIST_HANDLE_CODE = "31A000";
		//=====================��ȡ�ͻ���Ϣ�б������====================
	
	//=====================��ʼ���������====================
	/**
	 * ��ʼ����Ϣ����
	 */
	public static final String INIT_MTI 				= "0800";
	/**
	 * ��ʼ����λԪ��
	 */
	public static final String INIT_MAIN_ELEMENT		= "";
	/**
	 * ��ʼ��������
	 */
	public static final String INIT_HANDLE_CODE			= "992001";
	
	//=====================��ʼ���������====================
	
	
	
	//=====================�ͻ���Ϣ��ѯ(������)�������====================
	/**
	 * �ͻ���Ϣ��ѯ(������)��Ϣ����
	 */
	public static final String QUERY_CUST_INFO_CARD_MTI = "0100";
	/**
	 * �ͻ���Ϣ��ѯ(������)��λԪ��
	 */
	public static final String QUERY_CUST_INFO_CARD_MAIN_ELEMENT = "";
	/**
	 * �ͻ���Ϣ��ѯ(������)������
	 */
	public static final String QUERY_CUST_INFO_CARD_HANDLE_CODE = "31A001";
	
	//=====================�ͻ���Ϣ��ѯ(������)�������====================
	
	public static final String OPEN_CARD_TEST_CARD_NUM = "9009320100011166";
	
	
	
	/**
	 * ��ֵ������ѯ  ��Ϣ����
	 */
	public static final String RECHARGE_CANCLE_QUERY_MTI = "1003";
	
	
	//��������
	public static final int REQUEST_TYPE_CHE_XIAO = 777;
	//��ѯ����
	public static final int REQUEST_TYPE_CHA_XUN = 888;
	
	
	/**
	 * ��ֵ������ѯ   ��ǰ���ؼ�¼λ��
	 */
	public static String RECHARGE_CANCLE_QUERY_CURRENT_RETURN_FLAG = "1";
	public static String RECHARGE_CANCLE_QUERY_CURRENT_RETURN_FLAG2 = "0";
	/**
	 * ��ֵ������ѯ   ���η��ؼ�¼��
	 */
	public static String RECHARGE_CANCLE_QUERY_CURRENT_RETURN_COUNT = "10";
	
	/**
	 * ��ѯָ��Ա���Ĳ�����ϸ
	 */
	public static final int TYPE_QUERY_SOME_EMPLOYEE = 1113;
	
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		
	    //ע��App�쳣����������
//		Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
      
		SharedPreferences sp = getSharedPreferences(MyApplication.SP_JUM_FOM_CON, Context.MODE_PRIVATE);
		sp.edit().putBoolean(MyApplication.SP_JUM_FOM_CON_KEY, false).commit();
		

	}
	
	/**
	 * 
	 * @Description ��ȡ��ǰʱ��
	 * @author Shawn
	 * @Time 2013-9-24  ����5:25:21
	 * @param param
	 * @return String
	 * @exception exception
	 */
	//eg:121942
	public static String getCurrentTime(){
		SimpleDateFormat format = new SimpleDateFormat("HHmmss");
		return format.format(new Date());
	}
	
	/**
	 * 
	 * @Description ��ȡ��ǰ����
	 * @author Shawn
	 * @Time 2013-9-24  ����5:25:21
	 * @param param
	 * @return String
	 * @exception exception
	 */
	//eg:1014
	public static String getCurrentDate(){
		SimpleDateFormat format = new SimpleDateFormat("MMdd");
		return format.format(new Date());
	}
	
	/**
	 * ��ȡ��ǰ����������
	 * @return
	 */
	
	public static String getCurrentCompleteDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(new Date());
	}
	
	/**
	 * ��ȡ��ǰ������ʱ��
	 * @return
	 */
	public static String getCurrentCompleteTime(){
		SimpleDateFormat format = new SimpleDateFormat("HHmmss");
		return format.format(new Date());
	}
	
	/**
	 * ��ȡMacУ����
	 * @param isoMsg
	 * ǩ����Ϣʵ����
	 * @param key
	 * mac��Կ
	 * @return
	 */
	public static byte[] getMsgCheckCode(BaseNode isoMsg,String key){
		L.i("getMsgCheckCode", "getMsgCheckCode()");
		try {
//			L.e("data:", new String(isoMsg.pack()));
//			byte[] data = isoMsg.pack();
			byte[] data = isoMsg.getDataString().getBytes();
//			byte[] data = RequestUtil.getBytesRequest(isoMsg);
			if(isoMsg.getMacKey()!=null&&isoMsg.getMacKey().length>0){
				return SafeSoft.GenerateMac(
						isoMsg.getMacKey(), data.length, data);
			}else{
				return SafeSoft.GenerateMac(
						MyApplication.DEFAULT_MAC_KEY.getBytes(), data.length, data);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * ��ȡMacУ����
	 * @param isoMsg
	 * ǩ����Ϣʵ����
	 * @param key
	 * mac��Կ
	 * @return
	 */
	public static byte[] getMsgCheckCode(BaseNode isoMsg,byte[] key){
		L.i("getMsgCheckCode", "getMsgCheckCode()");
		try {
//			L.e("data:", new String(isoMsg.pack()));
//			byte[] data = isoMsg.pack();
			byte[] data = isoMsg.getDataString().getBytes();
//			byte[] data = RequestUtil.getBytesRequest(isoMsg);
			if(isoMsg.getMacKey()!=null&&isoMsg.getMacKey().length>0){
				return SafeSoft.GenerateMac(
						isoMsg.getMacKey(), data.length, data);
			}else{
				return SafeSoft.GenerateMac(
						MyApplication.DEFAULT_MAC_KEY.getBytes(), data.length, data);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	//����Mac��֤
	private void macTest(){
		String str_mac_key = "1234567890123456";
		byte[] mak_key = str_mac_key.getBytes();
		String str_data = "1234567890";
		byte[] data = str_data.getBytes();
		int data_len = data.length;
		byte[] mac_test = SafeSoft.GenerateMac(mak_key, data_len, data);
		L.e("mac_test", new String(mac_test));
		//���ӦΪ��7789AFAC888BA28B����ȷ
	}
	
	
	
	
	/**
	 * ��ӿ�ݷ�ʽ
	 * ע��AndroidManifest.xml�����Ȩ��:"com.android.permission.launcher.INSTALL_SHORTCUT"
	 * @param activity
	 * ����Activity
	 */
	public static void addShortCut(Activity activity){
		Intent shor_cut_intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		//��ݷ�ʽ������
		shor_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, activity.getString(R.string.app_name));
		//�������ظ�����
		shor_cut_intent.putExtra("duplicate", false);
		//�ƶ���ǰ��ActivityΪ��ݷ�ʽ����������
		//ע�⣺ComponentName�ĵڶ�������������ϵ��(.)�������ݷ�ʽ�޷�������Ӧ����
		ComponentName componentName = new ComponentName(activity.getPackageName(), "."+activity.getLocalClassName());
		shor_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(componentName));
		//��ݷ�ʽ��ͼ��
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(activity, R.drawable.mep_app_icon);
		shor_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		activity.sendBroadcast(shor_cut_intent);
		
//		Intent intent = new Intent();
//		intent.setAction(Intent.ACTION_MAIN);
//		intent.setClassName(activity.getPackageName(), "."+activity.getLocalClassName());
//		intent.addCategory(Intent.CATEGORY_LAUNCHER);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//		
//		Intent short_cut_intent = new Intent();
//		short_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
//		//���ÿ�ݷ�ʽ����
//		short_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, activity.getString(R.string.app_name));
//		//Ӧ�ó���ͼ��
//		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(activity, R.drawable.mep_app_icon);
//		//���Ӧ�ó���ͼ��
//		short_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
//		//�������ظ�����
//		short_cut_intent.putExtra("duplicate", false);
//		
//		short_cut_intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//		activity.sendBroadcast(short_cut_intent);
		
	}
//	/**
//	 * �жϿ�ݷ�ʽ�Ƿ����
//	 * ע�⻹����AndroidManifest.xml�����Ȩ��:"com.android.launcher.permission.READ_SETTINGS"
//	 * @param activity
//	 * @return
//	 */
//	public static boolean isShortCutExist(Activity activity){
//		boolean result = false;
//		String title="";
//		
//		try{
//			final PackageManager pm = activity.getPackageManager();
//			//��ȡӦ�ó��������
//			title = pm.getApplicationLabel(pm.getApplicationInfo(
//					activity.getPackageName(), PackageManager.GET_META_DATA)).toString();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		final String uriStr;
//		if(android.os.Build.VERSION.SDK_INT<8){
//			uriStr = "content://com.android.launcher.settings.favorites?notify=true";
//		}else{
//			uriStr = "content://com.android.launcher2.settings.favorites?notify=true";
//		}
//		
//		final Uri CONTENT_URI =Uri.parse(uriStr);
//		final Cursor cursor = activity.getContentResolver().query(CONTENT_URI, null
//				, "title=?", new String[]{title}, null);
//		if(cursor!=null&&cursor.getCount()>0){
//			result = true;
//		}
//		
//		return result;
//	}
	//�����Ƿ񴴽��˿�ݷ�ʽ��sp����
	public static final String SP_IS_SHORTCUT_EXIST = "SP_IS_SHORTCUT_EXIST";
	//�����Ƿ񴴽��˿�ݷ�ʽ��key
	public static final String KEY_SP_SHORTCUT = "KEY_SP_SHORTCUT";
	/**
	 * �Ƿ񴴽��˿�ݷ�ʽ
	 * @param activity
	 * @return
	 */
	public static boolean isShortCutExist(Activity activity){
		SharedPreferences sp =activity.getSharedPreferences(SP_IS_SHORTCUT_EXIST, Activity.MODE_PRIVATE);
		return sp.getBoolean(KEY_SP_SHORTCUT, false);
	}
	/**
	 * ���ô����˿�ݷ�ʽ
	 * @param activity
	 * @param isExist
	 */
	public static void setShorCut(Activity activity,boolean isExist){
		SharedPreferences sp = activity.getSharedPreferences(SP_IS_SHORTCUT_EXIST, Activity.MODE_PRIVATE);
		sp.edit().putBoolean(SP_IS_SHORTCUT_EXIST, isExist).commit();
	}
	
	/**
	 * 
	 * @Description ���ϵͳ��ˮ��
	 * @author Shawn
	 * @Time 2013-9-24  ����5:12:49
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static String getLineNum(Context context){
		SharedPreferences sp = context.getSharedPreferences("num", Context.MODE_PRIVATE);
		
		return sp.getString("line_num", "000001"); 
	}
	/**
	 * 
	 * @Description ���ϵͳ��ˮ��
	 * @author Shawn
	 * @Time 2013-9-24  ����5:12:49
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static void setLineNum(Context context){
		SharedPreferences sp = context.getSharedPreferences("num", Context.MODE_PRIVATE);
		String num=sp.getString("line_num", "000001");
		int i_num = Integer.parseInt(num);
		i_num++;
		if(i_num==999999){
			i_num = 0;
		}
		String str_num=String.valueOf(i_num);
		if(str_num.length()==1){
			str_num = "00000"+str_num;
		}else if(str_num.length()==2){
			str_num = "0000"+str_num;
		}else if(str_num.length()==3){
			str_num = "000"+str_num;
		}else if(str_num.length()==4){
			str_num = "00"+str_num;
		}else if(str_num.length()==5){
			str_num = "0"+str_num;
		}
		
		
		sp.edit().putString("line_num", str_num).commit();
	}
	
	
	
	/**
	 * 
	 * @Description ��ȡ60������
	 * @author Shawn
	 * @Time 2013-9-24  ����5:33:42
	 * @param param
	 * @return String
	 * @exception exception
	 */
	public static String getSixtyValue(String comm_num,String term_num){
		StringBuilder sb = new StringBuilder();
		sb.append(MyApplication.getCurrentCompleteDate());
		sb.append(MyApplication.getCurrentCompleteTime());
		sb.append("3");//POS�豸����
		sb.append("1");//Ԥ��Ȩ��ʶ
		sb.append("1");//λ�ұ�ʶ��
		sb.append(comm_num);
		sb.append("|");
		sb.append(term_num);
		sb.append("|");
		
		String value = sb.toString();
		StringBuilder sb2= new StringBuilder();
		int len= value.length();
		
		if(len>99){
			sb2.append("0");
		}else if(len>9&&len<=99){
			sb2.append("00");
		}
		sb2.append(len);
		sb2.append(value);
//		String strLen=new String(StringUtil.str2Bcd(sb2.toString()));
//		StringBuilder data_six=  new StringBuilder();
//		data_six.append(strLen);
//		data_six.append(value);
		L.i("getSixTeenValue(comm_num, term_num)", sb2.toString());
		
		return sb2.toString();
	}
	
	/**
	 * �������κ�
	 */
	public static String CURRENT_PICHI_HAO = "";
	/**
	 * ����Ʊ�ݺ�
	 */
	public static String CURRENT_PIAOJU_HAO = "";
	
	/**
	 * ��ѯ��
	 */
	public static String CURRENT_CHAXUN_HAO = "";
	
	/**
	 * ����
	 */
	public static String CURRENT_DATE = "";
	
	
	public static String get62Value(){
		StringBuilder sb = new StringBuilder();
		sb.append("026");
		sb.append(MyApplication.CURRENT_PICHI_HAO);
		sb.append(MyApplication.CURRENT_PIAOJU_HAO);
		sb.append(MyApplication.CURRENT_CHAXUN_HAO);
		sb.append(MyApplication.getCurrentCompleteDate());
		return sb.toString();
		
	}
	
	/**
	 * ��ȡ������60�������
	 * @param phoneNum
	 * �ֻ���
	 * @param IMSI
	 * 15λSIM����ʶ
	 * @param IEMI
	 * 14λ�ֻ��豸��ʶ
	 * @return
	 */
	public static String getSixtyValue2(String phoneNum,String IMSI,String IEMI){
		StringBuilder sb = new StringBuilder();
		if(phoneNum!=null&&phoneNum.length()>0){
			sb.append(phoneNum);
		}
		if(IMSI!=null&&IMSI.length()>0){
			sb.append(IMSI);
		}
		if(IEMI!=null&&IEMI.length()>0){
			sb.append(IEMI);
		}
		String data = sb.toString();
		int len = data.length();
		StringBuilder sb2 = new StringBuilder();
		if(len>=0&&len<=9){
			sb2.append("00");
		}else if(len>9&&len<=99){
			sb2.append("0");
		}
		sb2.append(len);
		sb2.append(data);
		return sb2.toString();
	}	
	
	
	public static String getSixtyValue3(String phoneNum,String IMSI,String IEMI){
		StringBuilder sb = new StringBuilder();
		if(phoneNum!=null&&phoneNum.length()>0){
			sb.append(phoneNum);
		}
		if(IMSI!=null&&IMSI.length()>0){
			sb.append(IMSI);
		}
		if(IEMI!=null&&IEMI.length()>0){
			sb.append(IEMI);
		}
//		String data = sb.toString();
//		int len = data.length();
//		StringBuilder sb2 = new StringBuilder();
//		if(len>=0&&len<=9){
//			sb2.append("00");
//		}else if(len>9&&len<=99){
//			sb2.append("0");
//		}
//		sb2.append(len);
//		sb2.append(data);
		return sb.toString();
	}
	
	
	/**
	 * SIM��Ψһ��ʶ
	 */
	public static String IMSI = "";
	/**
	 * �ֻ��豸Ψһ��ʶ
	 */
	public static String IEMI = "";
	
	
	public static final String LOGIN_PWD_DEFAULT_DES_KEY = "000000000000000";
	
	/**
	 * ��ȡǩ��61���ֵ
	 * @param pwd
	 * @param mac
	 * @return
	 */
	public static String getLoginSixtyOneValue(String pwd,String mac){
		
		byte[] de_pin_key = SafeSoft.Des(pwd.getBytes(), mac.getBytes(), SafeSoft.ENCRYPT);
		
		return new String(de_pin_key);
	}
	
	
	public static String get62Value(String serial_num){
		StringBuilder sb = new StringBuilder();
		
		sb.append(MyApplication.CURRENT_PICHI_HAO);
		sb.append(MyApplication.CURRENT_PIAOJU_HAO);
//		sb.append(MyApplication.CURRENT_CHAXUN_HAO);
		sb.append(serial_num);
		sb.append(MyApplication.getCurrentCompleteDate());
		String data = sb.toString();
		int len = data.length();
		StringBuilder sb2 = new StringBuilder();
		if(len>=0&&len<=9){
			sb2.append("00");
		}else if(len>9&&len<=99){
			sb2.append("0");
		}
		sb2.append(len);
		sb2.append(data);
		return sb2.toString();
		
	}
	public static String get62Value3(String serial_num,String date){
		StringBuilder sb = new StringBuilder();
		
		if(MyApplication.CURRENT_PICHI_HAO!=null&&MyApplication.CURRENT_PICHI_HAO.length()>0){
			sb.append(MyApplication.CURRENT_PICHI_HAO);
		}else{
			sb.append("000000");
		}
		
		if(MyApplication.CURRENT_PIAOJU_HAO!=null&&MyApplication.CURRENT_PIAOJU_HAO.length()>0){
			sb.append(MyApplication.CURRENT_PIAOJU_HAO);
		}else{
			sb.append("000000");
		}
		
//		sb.append(MyApplication.CURRENT_CHAXUN_HAO);
		sb.append(serial_num);
		sb.append(date);
		String data = sb.toString();
		int len = data.length();
		StringBuilder sb2 = new StringBuilder();
		if(len>=0&&len<=9){
			sb2.append("00");
		}else if(len>9&&len<=99){
			sb2.append("0");
		}
		sb2.append(len);
		sb2.append(data);
		return sb2.toString();
		
	}
	
	
	/**
	 * �жϽ���ʱ���Ƿ�С�ڿ�ʼʱ��
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean isEndTimeSmallThanStartTime(String startTime,String endTime){
		boolean result = false;
		Date date_start=null;
		Date date_end=null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd��");
		try{
			date_start = format.parse(startTime);
			date_end = format.parse(endTime);
			if(date_end.before(date_start)){
				result = true;
			}
		}catch(Exception e){
			
		}
		return result;
	}
	
	/**
	 * �жϽ���ʱ���Ƿ�С�ڿ�ʼʱ��
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean isEndTimeSmallThanStartTime2(Date startDay,Date endDay,
			Date startTime,Date endTime){
		boolean result = false;
		
		if(endDay.equals(startDay)){
			if(endTime.before(startTime)){
				result = true;
			}
		}else if(endDay.before(startDay)){
			result = true;
		}
		
		return result;
	}
	
	/**
	 * ��ȡApp��װ����Ϣ
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try { 
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
		if(info == null) info = new PackageInfo();
		return info;
	}
	
	
	
}

