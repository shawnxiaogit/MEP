package com.coco.android.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.coco.android.AppManager;
import com.coco.android.BaseActivity;
import com.coco.android.MyApplication;
import com.coco.android.R;
import com.coco.android.bean.LoginNode;
import com.coco.android.bean.RequestUtil;
import com.coco.android.https.HttpUtils;
import com.coco.android.socket.SocketClient;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;

import android.widget.EditText;
import android.widget.Button;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * @Descriptio ��½����
 * @author Shawn
 * @Time 2013-8-14 ����1:17:51
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	/**
	 * �˺ű༭��
	 */
	private EditText login_account_edit;
	/**
	 * ����༭��
	 */
	private EditText login_password_edit;
	/**
	 * ��½��ť
	 */
	private Button login_btn;

	/**
	 * ע�ᰴť
	 */
	// private Button regi_btn;
	// /**
	// * ��ס�û�������
	// */
	// private CheckBox login_rem_account_chkbox;
	// /**
	// * ��ס�������
	// */
	// private CheckBox login_rem_password_chkbox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		initView();

		setUpViewListener();
	}

	/**
	 * 
	 * @Description Ϊ�ؼ����ü�����
	 * @author Shawn
	 * @Time 2013-8-14 ����1:27:47
	 * @param param
	 * @return void
	 * @exception exception
	 */
	private void setUpViewListener() {
		login_btn.setOnClickListener(this);
		// regi_btn.setOnClickListener(this);

	}

	/**
	 * 
	 * @Description ��ʼ���ؼ�
	 * @author Shawn
	 * @Time 2013-8-14 ����1:21:17
	 * @param param
	 * @return void
	 * @exception exception
	 */
	private void initView() {
		login_account_edit = (EditText) findViewById(R.id.login_account_edit);
		login_password_edit = (EditText) findViewById(R.id.login_password_edit);

//		if (L.isDebug) {
//			login_account_edit.setText("13301797863");
//			login_password_edit.setText("111111");
//		}

		login_btn = (Button) findViewById(R.id.login_btn);
		// regi_btn = (Button) findViewById(R.id.regi_btn);

		// login_rem_account_chkbox = (CheckBox)
		// findViewById(R.id.login_rem_account_chkbox);
		// login_rem_password_chkbox = (CheckBox)
		// findViewById(R.id.login_rem_password_chkbox);

		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		//�����ʼ������·��
		SharedPreferences sp_account = getSharedPreferences(MyApplication.SP_ACCOUNT, Context.MODE_PRIVATE);
		String account = sp_account.getString(MyApplication.SP_ACCOUNT_KEY, "");
		
		if(account!=null&&account.length()>0){
			login_account_edit.setText(account);
		}else{
			login_account_edit.setText("");
		}
	}
	
	String account = "";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ��½
		case R.id.login_btn: {

			if (L.NO_NET_DEBUG) {
				openActivity(MainMenuActivity.class);
				defaultFinish();
			} else {
				
				
				String phone_num = login_account_edit.getText().toString();
				account = phone_num;
				
				String pwd = login_password_edit.getText().toString();
				if (!checkNamepwdIsNull(phone_num, pwd)) {
					//����˺Ų���11λ���油0
					if(phone_num.length()<11){
						int len = phone_num.length();
						int len2 = 11-len;
						StringBuilder sb = new StringBuilder();
						for(int i=0;i<len2;i++){
							sb.append("0");
						}
						phone_num = phone_num+sb.toString();
					}
					L.e("phone_num:", phone_num);
					
					//�����ж������Ƿ����
					if(!HttpUtils.isNetworkAvailable(LoginActivity.this)){
						showShortToast(R.string.no_available_net_work);
						return;
					}
					
					// 1�����������˺�ƥ��
//					if (accountAndPwdCorrect(phone_num, pwd)) {
						// �����û���Ϊһ����ʱȫ�ֱ���
//						MyApplication.TMP_USER_NAME = userName;
						L.e("MyApplication.TMP_USER_NAME", ""
								+ MyApplication.TMP_USER_NAME);
						// ��ȡ�ֻ�imsi
						MyApplication.PHONE_NUM = phone_num;
						Log.e("IMSI:", "" + MyApplication.IMSI);
						Log.e("IEMI:", "" + MyApplication.IEMI);
//						String term_num = "00000002";
//						String comm_num = "999073154110001";
//						String mac_key = "3133363739363035";
						String value_11 = MyApplication
								.getLineNum(getApplicationContext());
						L.e("value_11:", "" + value_11);
						String value_60 = getSixtyValue2(
								MyApplication.PHONE_NUM, MyApplication.IMSI, MyApplication.IEMI);
						
						String card_num = MyApplication.PHONE_NUM+"00000";
						
						L.e("MyApplication.TERM_NUM:", ""+MyApplication.TERM_NUM);
						L.e("MyApplication.COMM_NUM:", ""+MyApplication.COMM_NUM);
						//MyApplication.COMM_NUM:0503338173    
						//MyApplication.TERM_NUM:00001001
						// ��װǩ�����ױ���
						byte[] isoMsg = LoginNode.buildLoginMsg(card_num,MyApplication.TERM_NUM,
								MyApplication.COMM_NUM, MyApplication.DEFAULT_MAC_KEY, value_11, pwd,value_60);
						mRunningTask = new LoginTask(phone_num, pwd, MyApplication.IMSI, MyApplication.IEMI,
								isoMsg);
						mRunningTask.execute(MyApplication.LOGIN_URL);
					} 
			}
		}
			break;
		}
	}

	/**
	 * ��ȡ������60�������
	 * 
	 * @param phoneNum
	 *            �ֻ���
	 * @param IMSI
	 *            15λSIM����ʶ
	 * @param IEMI
	 *            14λ�ֻ��豸��ʶ
	 * @return
	 */
	private String getSixtyValue2(String phoneNum, String IMSI, String IEMI) {
		StringBuilder sb = new StringBuilder();
		if (phoneNum != null && phoneNum.length() > 0) {
			sb.append(phoneNum);
		}
		if (IMSI != null && IMSI.length() > 0) {
			sb.append(IMSI);
		}
		if (IEMI != null && IEMI.length() > 0) {
			sb.append(IEMI);
		}
		String data = sb.toString();
		int len = data.length();
		StringBuilder sb2 = new StringBuilder();
		if (len >= 0 && len <= 9) {
			sb2.append("00");
		} else if (len > 9 && len <= 99) {
			sb2.append("0");
		}
		sb2.append(len);
		sb2.append(data);
		return sb2.toString();
	}

	Handler responseHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RequestUtil.REQUEST_SUCCESS: {

				String hex_response = (String) msg.obj;
				// L.i("hex_response:", hex_response);
				if (hex_response != null && hex_response.length() > 0) {
					// ��������
					LoginNode node = LoginNode.paseResponse(hex_response);
					String code = getResources().getStringArray(
							R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39):", "" + node.get(39));
					if (node.get(39).equals(code)) {
						// ����mac������Կ
						MyApplication.PIN_KEY = node.getPinKey();
						// L.e("MyApplication.PIN_KEY:",
						// ""+DigitalTrans.byte2hex(MyApplication.PIN_KEY));
						L.e("MyApplication.PIN_KEY:", DigitalTrans.byte2hex(MyApplication.PIN_KEY));
						// �������������Կ
						MyApplication.MAC_KEY = node.getMacKey();
						// L.e("MyApplication.MAC_KEY:",
						// ""+DigitalTrans.byte2hex(MyApplication.MAC_KEY));
						L.e("MyApplication.MAC_KEY:", ""
								+ DigitalTrans.byte2hex(MyApplication.MAC_KEY));
						MyApplication.setLineNum(getApplicationContext());

						// ���ñ������κ�
						MyApplication.CURRENT_PICHI_HAO = node.getPicihao();
						 L.i("MyApplication.CURRENT_PICHI_HAO:",
						 ""+MyApplication.CURRENT_PICHI_HAO);
						// ���ñ���Ʊ�ݺ�
						MyApplication.CURRENT_PIAOJU_HAO = node.getPiaojuhao();
						 L.i("MyApplication.CURRENT_PIAOJU_HAO:",
						 ""+MyApplication.CURRENT_PIAOJU_HAO);
						// ���ò�ѯ��
						MyApplication.CURRENT_CHAXUN_HAO = node.getChaxunhao();
						 L.i("MyApplication.CURRENT_CHAXUN_HAO",
						 ""+MyApplication.CURRENT_CHAXUN_HAO);
						MyApplication.CURRENT_DATE = node.getDate();
						 L.i("MyApplication.CURRENT_DATE:",
						 ""+MyApplication.CURRENT_DATE);
						//���淵�صĲ���ԱȨ��
						String authority = (String) node.get(61);
						
						if(authority!=null&&authority.length()>0){
							String authority1 ="";
							String authority2 ="";
							if(authority.length()>=2){
								authority1= authority.substring(0, 1);
								authority2 = authority.substring(1, 2);
							}
							
							MyApplication.myAuthority = Integer.parseInt(authority1);
							MyApplication.myMepAuority= Integer.parseInt(authority2);
							L.e("MyApplication.myAuthority:", ""+MyApplication.myAuthority);
							L.e("MyApplication.myMepAuority", ""+MyApplication.myMepAuority);
						}
						//
						// String val_62 = MyApplication.get62Value();
						// L.e("val_62:", ""+val_62);
						// L.e("val_62",
						// ""+DigitalTrans.stringToHexString(val_62));
						//����һ��ʹ��APP��ֵ����Ϊfasle
						SharedPreferences sp = getSharedPreferences(MyApplication.SP_IS_USE_APP_FIRST, 
								Context.MODE_PRIVATE);
						sp.edit().putBoolean(MyApplication.SP_IS_USE_APP_FIRST_KEY, false).commit();
						
						showShortToast(R.string.login_success);
						
						//�����ʼ������·��
						SharedPreferences sp_account = getSharedPreferences(MyApplication.SP_ACCOUNT, Context.MODE_PRIVATE);
						sp_account.edit().putString(MyApplication.SP_ACCOUNT_KEY, account).commit();
						
						openActivity(MainMenuActivity.class);
						
						//�����¼������
						MyApplication.LOGIN_PWD = login_password_edit.getText().toString();
						defaultFinish();
					} else {
						String err_msg = (String) node.get(63);
						if(err_msg!=null&&err_msg.length()>0){
							showShortToast(err_msg);
						}else{
							showShortToast(R.string.account_or_pwd_not_corr);
						}
						
					}
					mAlertDialog.dismiss();

				}
			}
				break;
			case RequestUtil.REQUEST_FAIELD: {
				if(msg.obj!=null){
					showShortToast(msg.obj.toString());
				}else{
					showShortToast(R.string.login_faield);
				}
				
				mAlertDialog.dismiss();
			}
				break;
			}
		};
	};

	/**
	 * 
	 * @Description �ж��û����������Ԥ����Ƿ�һ��
	 * @author Shawn
	 * @Time 2013-10-9 ����1:25:31
	 * @param userName
	 *            �û���
	 * @param pwd
	 *            ����
	 * @return boolean �û����������Ƿ�ƥ��
	 * @exception exception
	 */
	private boolean accountAndPwdCorrect(String userName, String pwd) {
		SharedPreferences sp = getSharedPreferences(
				MyApplication.SP_USER_NAM_PWD, Context.MODE_PRIVATE);

		for (int i = 0; i < MyApplication.USER_NAME_LIST.length; i++) {
			String defaName = MyApplication.USER_NAME_LIST[i];
			if (userName.equals(defaName)) {
				String defaPwd = sp.getString(userName,
						MyApplication.PWD_DEFAULT);
				if (pwd.equals(defaPwd)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @Descriptio ��½����
	 * @author Shawn
	 * @Time 2013-8-15 ����3:00:23
	 */
	private class LoginTask extends AsyncTask {

		private String mUserName;
		private String mPwd;
		private String IMSI;
		private String IEMI;
		private byte[] mRequest;

		public LoginTask() {

		}

		public LoginTask(String userName, String pwd, String imsi, String iemi,
				byte[] request) {
			mUserName = userName;
			mPwd = pwd;
			IMSI = imsi;
			IEMI = iemi;
			mRequest = request;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.loging);
		}

		@Override
		protected Object doInBackground(Object... params) {
			String url = (String) params[0];

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);

			// mAlertDialog.dismiss();
			SocketClient client = new SocketClient(mRequest, responseHandler);
			client.start();
			// openActivity(MainMenuActivity.class);
			// defaultFinish();
		}

	}

	TelephonyManager telephonyManager;

	private String getImsi() {
		return telephonyManager.getSubscriberId().substring(0, 15);
	}

	private String getIemi() {
		return telephonyManager.getDeviceId().substring(0, 14);
	}

	/**
	 * 
	 * @Description ����û���������Ϊ�����
	 * @author Shawn
	 * @Time 2013-8-15 ����1:59:22
	 * @param userName
	 *            �û���
	 * @param pwd
	 *            ����
	 * @return boolean
	 * @exception exception
	 */
	private boolean checkNamepwdIsNull(String userName, String pwd) {
		boolean result = true;
		if (TextUtils.isEmpty(userName)) {
			showShortToast(R.string.username_can_not_null2);
			result = true;
			
		} else {
			if (TextUtils.isEmpty(pwd)) {
				result = true;
				showShortToast(R.string.pwd_can_not_null);
			} else {
				if (!TextUtils.isEmpty(userName)
						&& !TextUtils.isEmpty(userName)) {
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @Description ��ȡ60������
	 * @author Shawn
	 * @Time 2013-9-24 ����5:33:42
	 * @param param
	 * @return String
	 * @exception exception
	 */
	private String getSixtyValue(String comm_num, String term_num) {
		StringBuilder sb = new StringBuilder();
		sb.append(MyApplication.getCurrentCompleteDate());
		sb.append(MyApplication.getCurrentCompleteTime());
		sb.append("3");// POS�豸����
		sb.append("1");// Ԥ��Ȩ��ʶ
		sb.append("1");// λ�ұ�ʶ��
		sb.append(comm_num);
		sb.append("|");
		sb.append(term_num);
		sb.append("|");

		String value = sb.toString();
		StringBuilder sb2 = new StringBuilder();
		int len = value.length();

		if (len > 99) {
			sb2.append("0");
		} else if (len > 9 && len <= 99) {
			sb2.append("00");
		}
		sb2.append(len);
		sb2.append(value);
		// String strLen=new String(StringUtil.str2Bcd(sb2.toString()));
		// StringBuilder data_six= new StringBuilder();
		// data_six.append(strLen);
		// data_six.append(value);
		L.i("getSixTeenValue(comm_num, term_num)", sb2.toString());

		return sb2.toString();
	}

	// ��д���ذ�ť�¼�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showAlertDialog(getString(R.string.warnming_prompt),
					getString(R.string.is_exit_app),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							AppManager.getAppManager().AppExit(getApplicationContext());

						}
					}, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					}, null);
		}
		return super.onKeyDown(keyCode, event);
	}
}
