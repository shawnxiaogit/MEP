package com.coco.android.ui;


import java.util.Timer;
import java.util.TimerTask;

import com.coco.android.BaseActivity;
import com.coco.android.MyApplication;
import com.coco.android.R;
import com.coco.android.bean.GetSyntCodeNode;
import com.coco.android.bean.InitNode;
import com.coco.android.bean.RequestUtil;
import com.coco.android.https.HttpUtils;
import com.coco.android.socket.SocketClient;
import com.coco.android.util.L;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



/**
 * 
 * @Descriptio 初始化界面
 * @author Shawn
 * @Time 2013-8-23  
 */
public class InitActivity extends BaseActivity {
	/**
	 * 操作员号
	 */
//	private EditText et_oper_num;
	/**
	 * 手机号码编辑框
	 */
	private EditText et_phone_num;
	
	/**
	 * 验证码编辑框
	 */
	private EditText et_regi_pwd;
	/**
	 * 初始化按钮
	 */
	private Button btn_register;
	/**
	 * 获取动态验证码按钮
	 */
	private Button btn_get_code;
	
	/**
	 * 返回按钮
	 */
	private Button btn_back;
	
	/**
	 * 账号
	 */
	private String account;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_init);
		
		//初始化控件
		initView();
		//为控件设置监听器
		setUpViewListener();
	}
	
	
	private int timeInt;
	private Timer timer;
	/**
	 * @Title: doSuccessGet
	 * @Description: 成功获取注册码后做界面刷新
	 */
	protected void doSuccessGet() {
		timer = new Timer();
		timeInt = 120;
		String timeStr = getString(R.string.limit_time, timeInt);
		btn_get_code.setText(timeStr);
		btn_get_code.setClickable(false);
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				if (timeInt == 1) {
					msg.what = 20;
				} else {
					msg.what = 10;
					timeInt--;
				}
				btn_timer_handler.sendMessage(msg);
			}
		};
		timer.schedule(timerTask, 1000, 1000);
	}
	
	
	private Handler btn_timer_handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 10:{
				String timeStr = getResources().getString(
						R.string.limit_time, timeInt);
				
				btn_get_code.setText(timeStr);
				btn_get_code.setClickable(false);
			}break;
			case 20:{
				String get_code = getResources().getString(
						R.string.txt_get_code_again);
				btn_get_code.setText(get_code);
				btn_get_code.setClickable(true);
				timer.cancel();
			}break;
			}
		}
		
	};
	
	/**
	 * 
	 * @Description 为控件设置监听器
	 * @author Shawn
	 * @Time 2013-8-26  
	 */
	private void setUpViewListener(){
		btn_register.setOnClickListener(new RegisterListener());
		
		btn_get_code.setOnClickListener(new GetSytaCodeListener());
		btn_back.setOnClickListener(new BackListener());
		
	}
	
	/**
	 * 获取动态注册码监听器
	 * @author Shawn
	 *
	 */
	private class GetSytaCodeListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
//			String term_num = "00000002";
//			String comm_num = "999073154110001";
//			String mac_key = "3133363739363035";
			
//			String value_60 = MyApplication.getSixtyValue(comm_num, term_num);
			
			String phone_num = et_phone_num.getText().toString();
			
			//1、新增判断网络是否可用
			if(!HttpUtils.isNetworkAvailable(InitActivity.this)){
				showShortToast(R.string.no_available_net_work);
				return;
			}
			//2、判断手机号码是否为空
			if(TextUtils.isEmpty(phone_num)){
				showShortToast(R.string.toast_phone_num_not_null);
				return;
			}
			
			
			String value_11 =MyApplication.getLineNum(getApplicationContext());
			String value_60 = MyApplication.getSixtyValue2(phone_num,MyApplication.IMSI,MyApplication.IEMI);
			
			//组装获取动态码报文
			byte[] request = GetSyntCodeNode.buildMsg(null,GetSyntCodeNode.GET_INIT_SYNTAX_CODE,value_11,value_60,null);
			
			
			mRunningTask = new GetSyntaxTask(request);
			mRunningTask.execute(MyApplication.LOGIN_URL);
			
		}
		
	}
	
	/**
	 * 获取验证码任务
	 * @author Shawn
	 *
	 */
	private class GetSyntaxTask extends AsyncTask{
		private byte[] mRequest;
		public GetSyntaxTask(byte[] request){
			mRequest = request;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.get_syta_code);
		}
		@Override
		protected Object doInBackground(Object... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest,responseHandler);
			client.start();
		}
	}
	
	Handler responseHandler= new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case RequestUtil.REQUEST_SUCCESS:{
				String hex_response = (String) msg.obj;
				if(hex_response!=null&&hex_response.length()>0){
					//解析返回报文
					GetSyntCodeNode node = GetSyntCodeNode.parseGetSyntCodeNode(hex_response);
					String code = getResources().getStringArray(R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39):", ""+node.get(39));
					if(node.get(39).equals(code)){
						showShortToast(R.string.get_synt_code_succ);
						String synt_code = (String) node.get(61);
						if(synt_code!=null&&synt_code.length()>0){
							if(L.INIT_DEBUG){
								et_regi_pwd.setText(synt_code);
							}
							doSuccessGet();
						}
					}else{
						
						String err_msg = (String) node.get(63);
						if(err_msg!=null&&err_msg.length()>0){
							showShortToast(err_msg);
						}else{
							showShortToast(R.string.get_synt_code_faield);
						}
					}
					
				}else{
					showShortToast(R.string.get_synt_code_faield);
				}
				mAlertDialog.dismiss();
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				if(msg.obj!=null){
					showShortToast(msg.obj.toString());
				}else{
					showShortToast(R.string.get_synt_code_faield);
				}
				
				mAlertDialog.dismiss();
			}break;
			}
//			mAlertDialog.dismiss();
		}
	};
	
	/**
	 * 
	 * @Descriptio 返回按钮监听器
	 * @author Shawn
	 * @Time 2013-8-23 
	 */
	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			openActivity(LoginActivity.class);
			defaultFinish();
			
		}
		
	}
	
	/**
	 * 
	 * @Descriptio 初始化按钮监听器
	 * @author Shawn
	 * @Time 2013-8-26  
	 */
	private class RegisterListener implements OnClickListener{

		@Override
		public void onClick(View v) {
//			String operNum = et_oper_num.getText().toString();
			String phoneNum = et_phone_num.getText().toString();
			String regiPwd = et_regi_pwd.getText().toString();
			account = phoneNum;
			
			//1、新增判断网络是否可用
			if(!HttpUtils.isNetworkAvailable(InitActivity.this)){
				showShortToast(R.string.no_available_net_work);
				return;
			}
			if(!dataIsNull(phoneNum,regiPwd)){
				//组装初始化报文
				String value_11 =MyApplication.getLineNum(getApplicationContext());
				String value_60 = MyApplication.getSixtyValue2(phoneNum,MyApplication.IMSI,MyApplication.IEMI);
				String val_61 = getSixtyOneValue(regiPwd);
				byte[] request = InitNode.buildMsg(value_11, value_60,val_61);
				
				mRunningTask = new RegisterTask(phoneNum,regiPwd,request);
				mRunningTask.execute(MyApplication.REGISTER_URL);
			}
		}
		
	}
	
	
	private String getSixtyOneValue(String value){
		int len=value.length();
		StringBuilder sb = new StringBuilder();
		if (len >= 0 && len <= 9) {
			sb.append("00");
		} else if (len > 9 && len <= 99) {
			sb.append("0");
		}
		sb.append(len);
		sb.append(value);
		return sb.toString();
	}
	
	
	/**
	 * 
	 * @Descriptio 初始化任务
	 * @author Shawn
	 * @Time 2013-8-26 
	 */
	private class RegisterTask extends AsyncTask{
//		private String mOperNum;
		private String mPhoneNum;
		private String mRegiPwd;
		private byte[] mRequest;
		
		public RegisterTask(String phoneNum,
				String regiPwd,byte[] request){
//			mOperNum = operNum;
			mPhoneNum = phoneNum;
			mRegiPwd = regiPwd;
			mRequest = request;
		}
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.handing_process);
			
		}


		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			
			SocketClient client = new SocketClient(mRequest,initHandler);
			client.start();
		}
		
	}
	
	
	Handler initHandler = new Handler(){
		public void handleMessage(Message msg) {
//			mAlertDialog.dismiss();
			switch (msg.what) {
			case RequestUtil.REQUEST_SUCCESS: {
				String hex_response = (String) msg.obj;
				if (hex_response != null && hex_response.length() > 0) {
					InitNode node = InitNode.parseResponse(hex_response);
					String code = getResources().getStringArray(
							R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39):", "" + node.get(39));
					if (node.get(39).equals(code)) {
						//这里保存初始化的商户号和终端号
						String ter_num = (String) node.get(41);
						L.e("ter_num:", ""+ter_num);
						String comm_num = (String) node.get(42);
						L.e("comm_num:", ""+comm_num);
						
//						String comm_num_value = comm_num.substring(0, 10);
//						L.e("comm_num_value", ""+comm_num_value);
						MyApplication.TERM_NUM = ter_num;
						MyApplication.COMM_NUM = comm_num;
								
						
						SharedPreferences sp = getSharedPreferences(MyApplication.SP_TERM_COMM,Context.MODE_PRIVATE);
						sp.edit().putString(MyApplication.SP_TERM_NUM_KEY, ter_num)
						.putString(MyApplication.SP_COMM_NUM_KEY, comm_num).commit();
						
						
						StringBuilder sb = new StringBuilder();
						sb.append(MyApplication.IEMI);
						sb.append(MyApplication.IMSI);
						
						SharedPreferences sp_phone_info = getSharedPreferences(MyApplication.SP_PHONE_INFO, Context.MODE_PRIVATE);
						sp_phone_info.edit().putString(MyApplication.SP_PHONE_INFO_KEY, sb.toString()).commit();
						
						
						SharedPreferences sp_first_use_app = getSharedPreferences(MyApplication.SP_IS_USE_APP_FIRST, Context.MODE_PRIVATE);
						sp_first_use_app.edit().putBoolean(MyApplication.SP_IS_USE_APP_FIRST_KEY, false).commit();
						
						//保存初始化的线路号
						SharedPreferences sp_account = getSharedPreferences(MyApplication.SP_ACCOUNT, Context.MODE_PRIVATE);
						sp_account.edit().putString(MyApplication.SP_ACCOUNT_KEY, account).commit();
						
						
						
						showShortToast(R.string.init_success);
						openActivity(LoginActivity.class);
						defaultFinish();
					}else{
						String err_msg = (String)node.get(63);
						if(err_msg!=null&&err_msg.length()>0){
							showShortToast(err_msg);
						}else{
							showShortToast(R.string.init_faield);
						}
					}
				}else{
					showShortToast(R.string.init_faield);
				}
				mAlertDialog.dismiss();
			}break;
			case RequestUtil.REQUEST_FAIELD: {
				showShortToast(R.string.init_faield);
				mAlertDialog.dismiss();
			}break;
			}
		};
	};
	
	/**
	 * 
	 * @Description 数据是否为空
	 * @author Shawn
	 * @Time 2013-8-26  
	 * @param phonNum 手机号码
	 * @param regiPwd 动态码
	 * @return boolean 
	 * 
	 */
	private boolean dataIsNull(String phonNum,
			String regiPwd){
		boolean result = true;
		
		if(TextUtils.isEmpty(phonNum)){
			result = true;
			showShortToast(R.string.toast_phone_num_not_null);
		}else{
			if(TextUtils.isEmpty(regiPwd)){
				result = true;
				showShortToast(R.string.yanzheng_code);
			}else{
				result = false;
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @Description 初始化控件
	 * @author Shawn
	 * @Time 2013-8-26  
	 */
	private void initView(){
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.tv_title)).setText(R.string.title_init);
		
		et_phone_num = (EditText) findViewById(R.id.et_phone_num);
		et_regi_pwd = (EditText) findViewById(R.id.et_regi_pwd);
		
		
		btn_get_code = (Button) findViewById(R.id.btn_get_code);
		btn_register = (Button) findViewById(R.id.btn_register);
		
		if(L.GetSyntaCodeDebug){
			et_phone_num.setText(MyApplication.PHONE_NUM);
		}
		
		timer = new Timer();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
	}
}
