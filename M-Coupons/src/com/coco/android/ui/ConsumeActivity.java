package com.coco.android.ui;


import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.coco.android.BaseActivity;
import com.coco.android.MyApplication;
import com.coco.android.R;
import com.coco.android.bean.ConsumeNode;
import com.coco.android.bean.GetSyntCodeNode;
import com.coco.android.bean.QueryCustInfoNode;
import com.coco.android.bean.RequestUtil;
import com.coco.android.socket.SocketClient;
import com.coco.android.util.L;
import com.coco.android.util.MyStringUtil;
import com.google.zxing.CaptureActivity;


/**
 * 
 * @Descriptio  ���ѽ���
 * @author Shawn
 * @Time 2013-8-22  ����2:11:21
 */
public class ConsumeActivity extends BaseActivity {
	private static final String TAG = "ConsumeActivity";
	/**
	 * �༭�򿨺�
	 */
	private EditText et_consume_card_num;
	/**
	 * �༭�����ѽ��
	 */
	private EditText et_sum_of_consume;
	/**
	 * ��ťɨ��
	 */
	private Button btn_scan;
	
	/**
	 * ��ťȷ������
	 */
	private Button btn_consume_sure;
	
	/**
	 * ��ȡ��ά��
	 */
	private static final int REQUEST_GET_CONDE = 0;
	private static final int REQUEST_YUER = 2223;
	
	/**
	 * ���������ذ�ť
	 */
	private Button btn_back;
	
	
	/**
	 * ��ȡ��֤�밴ť
	 */
	private Button btn_get_syntax_code;
	
	
	private EditText et_syntax_code;
	
	//����������Ϣ
	private void enableInputData(){
		et_consume_card_num.setEnabled(true);
		et_sum_of_consume.setEnabled(true);
		btn_scan.setEnabled(true);
	}
	//������������Ϣ
	private void disableInputData(){
		et_consume_card_num.setEnabled(false);
		et_sum_of_consume.setEnabled(false);
		btn_scan.setEnabled(false);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.consume);
		//��ʼ���ؼ�
		initView();
		//Ϊ�ؼ����ü�����
		setUpViewListener();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		enableInputData();
	}
	
	/**
	 * 
	 * @Description Ϊ�ؼ����ü�����
	 * @author Shawn
	 * @Time 2013-8-26  ����2:01:44
	 */
	private void setUpViewListener(){
		btn_back.setOnClickListener(new BackListener());
		btn_scan.setOnClickListener(new ScannerListener());
//		btn_get_dyna_code.setOnClickListener(new GetDynamicCodeListener());
		
		btn_consume_sure.setOnClickListener(new ConsumeSureListener());
		
		btn_get_syntax_code.setOnClickListener(new GetDynamicCodeListener());
		
//		et_sum_of_consume.setOnEditorActionListener(new OnEditorActionListener() {
//			
//			@Override
//			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		});
	}
	

	
	/**
	 * ��ȡ��֤�밴ť������
	 * @author ShawnXiao
	 *
	 */
	private class GetDynamicCodeListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			//1�����Ų���Ϊ��
			String card_num = et_consume_card_num.getText().toString();
			//2�����ѽ���Ϊ��
			String consume_num = et_sum_of_consume.getText().toString();
			if(TextUtils.isEmpty(card_num)){
				showShortToast(R.string.card_num_can_not_null);
				return;
			}
			
			if(TextUtils.isEmpty(consume_num)){
				showShortToast(R.string.sum_of_consume_can_not_null);
				return;
			}
			
			StringBuilder sb = new StringBuilder();
			if(card_num==null||card_num.length()==0){
				showShortToast(R.string.card_num_error);
				return ;
			}else if(card_num.length()==8){
				sb.append(MyApplication.CARD_PREFIX);
				sb.append(card_num);
			}else if(card_num.length()==16){
				sb.append(card_num);
			}else{
				showShortToast(R.string.card_num_error);
				return ;
			}
			
			String sum_of_consume = et_sum_of_consume.getText().toString();
			L.e("card_num:", sb.toString());
			Double my_cousume = Double.valueOf(sum_of_consume);
			L.e("my_cousume:", ""+my_cousume);
			L.e("ZERO_MONEY:", ""+ZERO_MONEY);
			L.e("my_cousume.equals(ZERO_MONEY)", ""+(my_cousume.equals(ZERO_MONEY)));
			if(my_cousume.equals(ZERO_MONEY)){
				showShortToast(R.string.consume_of_sume_can_not_zero);
				return;
			}
			
			
			
			Double double_consume = Double.valueOf(sum_of_consume);
			L.e("double_consume:", ""+double_consume);
			L.e("double_yuer:", ""+double_yuer);
			if(double_yuer!=null){
				if(double_consume>double_yuer){
					showShortToast(R.string.consume_of_sum_can_not_large_than_remain);
					return;
				}
			}
			
			//2����֯��ȡ��֤�뱨��
			String value_11 =MyApplication.getLineNum(getApplicationContext());
			String value_60 = MyApplication.getSixtyValue2(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
			
			//��װ��ȡ��̬�뱨��
			byte[] request = GetSyntCodeNode.buildMsg(sb.toString(),GetSyntCodeNode.GET_CONSUME_SYNTAX_CODE,value_11,value_60,sum_of_consume);
			
			
			mRunningTask = new GetSyntaxTask(request);
			mRunningTask.execute();
		}
		
	}
	
	
	/**
	 * ��ȡ��֤������
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
			showProgressDialog(R.string.get_syta_code2);
		}
		@Override
		protected Object doInBackground(Object... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest,getSyntaxCodeHandler);
			client.start();
		}
	}
	
	Handler getSyntaxCodeHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case RequestUtil.REQUEST_SUCCESS:{
				String hex_response = (String) msg.obj;
				if(hex_response!=null&&hex_response.length()>0){
					//�������ر���
					GetSyntCodeNode node = GetSyntCodeNode.parseGetSyntCodeNode(hex_response);
					String code = getResources().getStringArray(R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39):", ""+node.get(39));
					if(node.get(39).equals(code)){
						showShortToast(R.string.get_synt_code_succ2);
						disableInputData();
						String synt_code = (String) node.get(61);
						if(synt_code!=null&&synt_code.length()>0){
							if(L.NO_DUAN_XIN_WANG_GUAN){
								et_syntax_code.setText(synt_code);
							}
						}
						
						doSuccessGet();
						
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
				showShortToast(R.string.get_synt_code_faield);
				mAlertDialog.dismiss();
			}break;
			}
//			mAlertDialog.dismiss();
		}
	};
	
	
	
	private int timeInt;
	private Timer timer;
	/**
	 * @Title: doSuccessGet
	 * @Description: �ɹ���ȡע�����������ˢ��
	 */
	protected void doSuccessGet() {
		timer = new Timer();
		timeInt = 120;
		String timeStr = getString(R.string.limit_time, timeInt);
		btn_get_syntax_code.setText(timeStr);
		btn_get_syntax_code.setClickable(false);
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
				
				btn_get_syntax_code.setText(timeStr);
				btn_get_syntax_code.setClickable(false);
			}break;
			case 20:{
				String get_code = getResources().getString(
						R.string.txt_get_code_again);
				btn_get_syntax_code.setText(get_code);
				btn_get_syntax_code.setClickable(true);
				timer.cancel();
			}break;
			}
		}
		
	};
	
	
	
	/**
	 * 
	 * @Descriptio ������һ������
	 * @author Shawn
	 * @Time 2013-8-23  ����3:50:43
	 */
	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			defaultFinish();
		}
		
	}
	
	/**
	 * 
	 * @Descriptio ɨ�������
	 * @author Shawn
	 * @Time 2013-8-26  ����2:03:49
	 */
	private class ScannerListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			et_sum_of_consume.setText("");
			et_syntax_code.setText("");
			
			//�����������ȡ��ά��
			Intent intent_query = new Intent(ConsumeActivity.this,CaptureActivity.class);
			if(L.IS_SAVE_CHECK_CODE){
				String check_code = et_syntax_code.getText().toString();
				if(check_code!=null&&check_code.length()>0){
					intent_query.putExtra("code", check_code);
				}
			}
			startActivityForResult(intent_query,REQUEST_GET_CONDE);
		}
		
	}
	
	
	/**
	 * 
	 * @Descriptio ��ȡ��̬��֤��
	 * @author Shawn
	 * @Time 2013-8-26  ����3:22:02
	 */
	private class GetDynamicCodeTask extends AsyncTask<String,Integer,String>{
		private String mCardNum;
		private String mSumOfConsume;
		private String mPhoneNum;
		public GetDynamicCodeTask(String card_num,String sum_of_consume,String phone_num){
			mCardNum = card_num;
			mSumOfConsume = sum_of_consume;
			mPhoneNum = phone_num;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.getting_dynamic_code);
		}
		
		@Override
		protected String doInBackground(String... params) {
			String result = "";
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mAlertDialog.dismiss();
		}
		
	}
	
	
	/**
	 * 
	 * @Description ��鿨�ź����ѽ���Ƿ�Ϊ��
	 * @author Shawn
	 * @Time 2013-8-26  ����2:43:43
	 * @param card_num ����
	 * @param sum_of_consume ���ѽ��
	 * @param phone_num �ֻ�����
	 * @return boolean
	 */
	private boolean numAndConsumeIsNull(String card_num,String sum_of_consume,String syntax_code){
		boolean result = true;
		if(TextUtils.isEmpty(card_num)){
			result = true;
			showShortToast(R.string.consume_card_num_can_not_null);
		}else{
			if(TextUtils.isEmpty(sum_of_consume)){
				result = true;
				showShortToast(R.string.sum_of_consume_can_not_null);
			}else{
				if(TextUtils.isEmpty(syntax_code)){
					showShortToast(R.string.syntax_code_not_null);
					result = true;
				}else{
					result = false;
				}
			}
		}
		return result;
	}
	
	
	/**
	 * ���ѽ��Ϊ0Ա
	 */
	private static final Double ZERO_MONEY = 0.0;
	
	/**
	 * 
	 * @Descriptio ȷ�������������󣬲���ʾ�������ѽ������
	 * @author Shawn
	 * @Time 2013-8-26  ����2:12:50
	 */
	private class ConsumeSureListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			String card_num = et_consume_card_num.getText().toString();
			String sum_of_consume = et_sum_of_consume.getText().toString();
			String dynamic_code = et_syntax_code.getText().toString();
			
			//&&!codeAndPwdIsNull(dynamic_code,consume_pwd)
			
			
			if(!numAndConsumeIsNull(card_num,sum_of_consume,dynamic_code)){
//				Integer my_cousume = Integer.parseInt(sum_of_consume);
				Double my_cousume = Double.valueOf(sum_of_consume);
				L.e("my_cousume:", ""+my_cousume);
				L.e("ZERO_MONEY:", ""+ZERO_MONEY);
				L.e("my_cousume.equals(ZERO_MONEY)", ""+(my_cousume.equals(ZERO_MONEY)));
				if(my_cousume.equals(ZERO_MONEY)){
					showShortToast(R.string.consume_of_sume_can_not_zero);
					return;
				}
				
				
				
				Double double_consume = Double.valueOf(sum_of_consume);
				L.e("double_consume:", ""+double_consume);
				L.e("double_yuer:", ""+double_yuer);
				if(double_yuer!=null){
					if(double_consume>double_yuer){
						showShortToast(R.string.consume_of_sum_can_not_large_than_remain);
						return;
					}
				}
				StringBuilder sb = new StringBuilder();
				if(card_num==null||card_num.length()==0){
					showShortToast(R.string.card_num_error);
					return ;
				}else if(card_num.length()==8){
					sb.append(MyApplication.CARD_PREFIX);
					sb.append(card_num);
				}else if(card_num.length()==16){
					sb.append(card_num);
				}else{
					showShortToast(R.string.card_num_error);
					return ;
				}
				L.e("card_num:", sb.toString());
				
				String val_11 = MyApplication.getLineNum(getApplicationContext());
//				String val_60 = MyApplication.getSixtyValue(MyApplication.COMM_NUM, MyApplication.TERM_NUM);
				String value_60 = MyApplication.getSixtyValue2(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
				//��װ���ѱ���
				String val_52 = MyApplication.DEFAULT_CONSUME_KEY;
				String val_61 = getSixtyOneValue(dynamic_code);
				
				byte[] consume_request=ConsumeNode.buildConsumeNode(sb.toString(),sum_of_consume,val_11,val_52,value_60,val_61);
//				if(L.ConsumeDebug){
//					consume_request = ConsumeNode.buildConsumeNode(card_num,sum_of_consume,val_11,value_60);
//				}
				
				mRunningTask = new ConsumeSureTask(sb.toString(),sum_of_consume,
						consume_request);
				mRunningTask.execute(MyApplication.CONSUME_URL);
			}
			
		}
		
	}
	
	
	private class ConsumeSureTask extends AsyncTask{
		private String mCardNum;
		private String mSumOfConsume;
		private byte[] mRequest;
		public ConsumeSureTask(String card_num,String sum_of_consume,
				byte[] request){
			mCardNum = card_num;
			mSumOfConsume = sum_of_consume;
			mRequest = request;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.consume_processing);
		}
		
		@Override
		protected String doInBackground(Object... params) {
			
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest, responseHandler);
			client.start();
		}
		
	}
	
	
	
	
	Handler responseHandler= new Handler(){
		public void handleMessage(Message msg) {
//			mAlertDialog.dismiss();
			switch(msg.what){
			case RequestUtil.REQUEST_SUCCESS:{
				mAlertDialog.dismiss();
				String hex_response = (String) msg.obj;
				ConsumeNode node = ConsumeNode.parseConsumeNode(hex_response);
				String code = getResources().getStringArray(R.array.return_code)[0];
				L.e("code:", code);
				L.e("node.get(39):", ""+node.get(39));
				if(node.get(39).equals(code)){
					showShortToast(R.string.consume_success);
					openActivity(MainMenuActivity.class);
					defaultFinish();
				}
				else if(node.get(39).equals(getResources().getStringArray(R.array.return_code)[1])){
					showShortToast(R.string.dynamic_code_error);
				}
				else{
					String err_msg = (String)node.get(63);
					if(err_msg!=null&&err_msg.length()>0){
						showShortToast(err_msg);
					}else{
						showShortToast(R.string.consume_faield);
					}
					
				}
				
				
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				showShortToast(R.string.consume_faield);
				mAlertDialog.dismiss();
			}break;
			}
		}
	};
	
	
	/**
	 * 
	 * @Description ��̬��֤�������Ϊ�����
	 * @author Shawn
	 * @Time 2013-8-26  ����3:41:42
	 * @param dynamic_code ��̬��֤��
	 * @return boolean
	 * @exception exception
	 */
	private boolean codeAndPwdIsNull(String dynamic_code,String consume_pwd){
		boolean result = true;
		if(TextUtils.isEmpty(dynamic_code)){
			result = true;
			showShortToast(R.string.dynamic_code_can_not_null);
		}else{
			if(TextUtils.isEmpty(consume_pwd)){
				result = true;
				showShortToast(R.string.consume_pwd_can_not_null);
			}else{
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * ��ʼ���ؼ�
	 * @Description TODO
	 * @author Shawn
	 * @Time 2013-8-26  ����1:56:24
	 */
	private void initView(){
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.tv_title)).setText(getResources().getStringArray(R.array.main_menu)[0]);
		
		et_consume_card_num = (EditText) findViewById(R.id.et_consume_card_num);
		et_sum_of_consume = (EditText) findViewById(R.id.et_sum_of_consume);
		
		et_syntax_code = (EditText) findViewById(R.id.et_syntax_code);
		
		
		btn_scan= (Button) findViewById(R.id.btn_scan);
		
		
		
		btn_get_syntax_code = (Button) findViewById(R.id.btn_get_syntax_code);
		btn_consume_sure = (Button) findViewById(R.id.btn_consume_sure);
		
		
		//����Ĭ����Ϣ
		if(L.ConsumeDebug){
			et_consume_card_num.setText("9009320100011224");
			et_sum_of_consume.setText("1");
		}
		enableInputData();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.e(TAG, "onActivityResult");
		L.e("requestCode:", ""+requestCode);
		L.e("resultCode:", ""+resultCode);
		if(requestCode == REQUEST_GET_CONDE && resultCode == RESULT_OK){
			String result = data.getExtras().getString("result");
			L.e("result", ""+result);
			if(TextUtils.isEmpty(result)){
				showShortToast(R.string.scan_retry);
				return;
			}
			
			if(L.NO_CARD_DEBUG){
				et_consume_card_num.setText("9009320100001290");
			}else{
				et_consume_card_num.setText(result);
			}
			
			
			if(L.IS_SAVE_CHECK_CODE){
				String code = data.getExtras().getString("code");
				if(code!=null&&code.length()>0){
					et_syntax_code.setText(code);
				}
			}
			
			String val_11 = MyApplication.getLineNum(getApplicationContext());
//			String val_60 = MyApplication.getSixtyValue(MyApplication.COMM_NUM, MyApplication.TERM_NUM);
			String value_60 = MyApplication.getSixtyValue2(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
			
			//��֯���ĸ��ݿ��Ų�ѯ�ͻ����ơ��ͻ���š����
			byte[] request  =  QueryCustInfoNode.buildMsg(et_consume_card_num.getText().toString(), val_11, value_60);
			
			mRunningTask = new QueryCustInfoTask(request);
			mRunningTask.execute();
		}else if(requestCode == REQUEST_YUER && resultCode == RESULT_OK ){
			String result = data.getExtras().getString("result");
			mYuEr = Double.parseDouble(result);
			L.e("mYuEr:", ""+mYuEr);
			
			
		}
	}
	
	/**
	 * ��ѯ�������
	 */
	private Double mYuEr;
	
	/**
	 * ��ѯ�ͻ���Ϣ(������)����
	 * @author ShawnXiao
	 *
	 */
	private class QueryCustInfoTask extends AsyncTask{
		private byte[] mRequest;
		public QueryCustInfoTask(byte[] request){
			mRequest = request;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.consume_processing);
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest, queryCustInfoHandler);
			client.start();
		}
	}
	/**
	 * ���
	 */
	private Double double_yuer;
	
	Handler queryCustInfoHandler= new Handler(){
		public void handleMessage(Message msg) {
//			mAlertDialog.dismiss();
			switch(msg.what){
			case RequestUtil.REQUEST_SUCCESS:{
				String hex_response = (String) msg.obj;
				QueryCustInfoNode node = QueryCustInfoNode.parseMsg(hex_response);
				String code = getResources().getStringArray(R.array.return_code)[0];
				L.e("code:", code);
				L.e("node.get(39):", ""+node.get(39));
				if(node.get(39).equals(code)){
//					showShortToast(R.string.consume_success);
					//�����Ի�����ʾ��Ϣ
					//�����ɹ���չʾ��ȡ�Ŀͻ���Ϣ
					Intent intent = new Intent(ConsumeActivity.this,CustInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(CustInfoActivity.DATA_KEY,node);
					
					intent.putExtras(bundle);
					L.e("queryCustInfoHandler", "handleMessage");
					L.e("node.getZhangmian_yuer()", node.getZhangmian_yuer());
					double_yuer = Double.parseDouble(node.getZhangmian_yuer());
					
					ConsumeActivity.this.startActivityForResult(intent,REQUEST_YUER);
				}else{
					showShortToast(R.string.get_data_faield);
				}
				mAlertDialog.dismiss();
//				openActivity(MainMenuActivity.class);
//				defaultFinish();
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				showShortToast(R.string.get_data_faield);
				mAlertDialog.dismiss();
			}break;
			}
		}
	};
	/**
	 * ��ȡ61��̬��֤��
	 * @param value
	 * @return
	 */
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
}
