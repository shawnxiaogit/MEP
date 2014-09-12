package com.coco.android.ui;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coco.android.BaseActivity;
import com.coco.android.MyApplication;
import com.coco.android.R;
import com.coco.android.bean.QueryRemainNode;
import com.coco.android.bean.RequestUtil;
import com.coco.android.socket.SocketClient;
import com.coco.android.util.L;
import com.google.zxing.CaptureActivity;

/**
 * 
 * @Descriptio 查询余额界面
 * @author Shawn
 * @Time 2013-8-20  下午5:10:46
 */
public class QueryRemainActivity extends BaseActivity {
	private static final String TAG = "QueryRemainActivity";
	
	/**
	 * 编辑框卡号
	 */
	private EditText et_card_num;
	
	/**
	 * 按钮扫一扫
	 */
	private Button btn_scan;
	/**
	 * 按钮查询
	 */
	private Button btn_query;
	
	/**
	 * 容器查询结果
	 */
	private RelativeLayout panel_query_result;
	
	/**
	 * 文本查询结果
	 */
	private TextView tv_remain_money;
	
	/**
	 * 获取二维码
	 */
	private static final int REQUEST_GET_CONDE = 0;
	
	/**
	 * 标题栏返回按钮
	 */
	private Button btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.query_remain);
		
		initView();
		
		setUpViewListener();
		
	}
	
	/**
	 * 
	 * @Description 为控件设置监听器
	 * @author Shawn
	 * @Time 2013-8-20  下午6:08:01
	 * @param param
	 * @return void
	 * @exception exception
	 */
	private void setUpViewListener(){
		btn_scan.setOnClickListener(new ScanListener());
		btn_query.setOnClickListener(new QueryListener());
		
		btn_back.setOnClickListener(new BackListener());
	}
	
	/**
	 * 
	 * @Descriptio 返回上一个界面
	 * @author Shawn
	 * @Time 2013-8-23  下午3:50:43
	 */
	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			defaultFinish();
		}
		
	}
	
	
	
	/**
	 * 
	 * @Descriptio 查询监听器
	 * @author Shawn
	 * @Time 2013-8-20  下午6:13:29
	 */
	private class QueryListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			panel_query_result.setVisibility(View.INVISIBLE);
			tv_remain_money.setText("");
			
//			//检查卡号和密码是否为空
			String card_num = et_card_num.getText().toString();
			
			
			if(TextUtils.isEmpty(card_num)){
				showShortToast(R.string.card_num_can_not_null);
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
			L.e("card_num:", sb.toString());
				
			String val_11 = MyApplication.getLineNum(getApplicationContext());
			String val_52 = MyApplication.DEFAULT_CONSUME_KEY;
			String value_60 = MyApplication.getSixtyValue2(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
			byte[] isoMsg = QueryRemainNode.buildMsg(sb.toString(),val_11,val_52,value_60);
			
			//请求网络，获取余额
			mRunningTask = new GetRemainMoneyTask(sb.toString(),isoMsg);
			mRunningTask.execute(MyApplication.QUERY_REMAIN_MONEY_URL);
		}
		
	}
	
	/**
	 * 
	 * @Descriptio 查询余额网络请求任务
	 * @author Shawn
	 * @Time 2013-8-21  上午10:30:52
	 */
	private class GetRemainMoneyTask extends AsyncTask<Object, Integer, String>{
		private String mCardNum;
		private byte[] mRequest;
		public GetRemainMoneyTask(String cardNum,byte[] request){
			mCardNum = cardNum;
			mRequest = request;
		}
		
		@Override
		protected void onPreExecute() {
			showProgressDialog(R.string.query_process);
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			SocketClient client = new SocketClient(mRequest,responseHandler);
			client.start();
		}

		@Override
		protected String doInBackground(Object... arg0) {
			
			
			return null;
		}
		
	}
	
	Handler responseHandler= new Handler(){
		public void handleMessage(Message msg) {
//			mAlertDialog.dismiss();
			switch(msg.what){
			case RequestUtil.REQUEST_SUCCESS:{
				String hex_response = (String) msg.obj;
				if(hex_response!=null&&hex_response.length()>0){
					//解析报文
					QueryRemainNode node = QueryRemainNode.parseQueryRemainNode(hex_response);
					String code = getResources().getStringArray(R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39):", ""+node.get(39));
					if(node.get(39).equals(code)){
						String remain = node.getZhangmian_yuer();
						L.e("remain:", ""+remain);
						tv_remain_money.setText(remain);
						panel_query_result.setVisibility(View.VISIBLE);
					}else{
						String err_msg = (String)node.get(63);
						if(err_msg!=null&&err_msg.length()>0){
							showShortToast(err_msg);
						}else{
							showShortToast(R.string.card_not_exist_or_opend);
						}
						
					}
				}else{
					showShortToast(R.string.query_remain_faield);
				}
				mAlertDialog.dismiss();
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				showShortToast(R.string.query_remain_faield);
				mAlertDialog.dismiss();
			}break;
			}
		}
	};
	
	/**
	 * 
	 * @Description 检查卡号和密码为空与否
	 * @author Shawn
	 * @Time 2013-8-20  下午6:21:04
	 * @param param
	 * @return boolean
	 * @exception exception
	 */
	private boolean checkCardNumAndPwdIsNull(String cardNum,String pwd,String phone_num){
		boolean result = true;
		if(TextUtils.isEmpty(cardNum)){
			result = true;
			showShortToast(R.string.card_num_can_not_null);
		}else{
			if(TextUtils.isEmpty(phone_num)){
				result = true;
				showShortToast(R.string.toast_phone_num_not_null);
			}else{
				if(TextUtils.isEmpty(pwd)){
					result = true;
					showShortToast(R.string.query_pwd_can_not_null);
				}else{
					if(!TextUtils.isEmpty(cardNum)&&!TextUtils.isEmpty(pwd)){
						result = false;
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @Descriptio 扫一扫监听器
	 * @author Shawn
	 * @Time 2013-8-20  下午6:09:27
	 */
	private class ScanListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			panel_query_result.setVisibility(View.GONE);
			Intent intent_query = new Intent(QueryRemainActivity.this,CaptureActivity.class);
			startActivityForResult(intent_query,REQUEST_GET_CONDE);
		}
		
	}
	
	/**
	 * 
	 * @Description 初始化组件
	 * @author Shawn
	 * @Time 2013-8-20  下午6:05:01
	 * @param param
	 * @return void
	 * @exception exception
	 */
	private void initView(){
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.tv_title)).setText(getResources().getStringArray(R.array.main_menu)[2]);
		
		et_card_num = (EditText) findViewById(R.id.et_card_num);
//		et_card_num.setText("90088801000002");
		
		if(L.QUERY_DEBUG){
			et_card_num.setText("9009320100011224");
		}
		
		btn_scan = (Button) findViewById(R.id.btn_scan);
		btn_query = (Button) findViewById(R.id.btn_query);
		
		panel_query_result = (RelativeLayout) findViewById(R.id.panel_query_result);
		
		tv_remain_money = (TextView) findViewById(R.id.tv_remain_money);
		
		panel_query_result.setVisibility(View.GONE);
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.e(TAG, "onActivityResult()");
		L.e("requestCode", ""+requestCode);
		L.e("REQUEST_GET_CONDE", ""+REQUEST_GET_CONDE);
		L.e("resultCode", ""+resultCode);
		L.e("RESULT_OK", ""+RESULT_OK);
		
		if(requestCode == REQUEST_GET_CONDE && resultCode == RESULT_OK){
			String result = data.getExtras().getString("result");
			L.e("result", ""+result);
			if(TextUtils.isEmpty(result)){
				showShortToast(R.string.scan_retry);
				return;
			}
			if(L.QUERY_DEBUG){
				et_card_num.setText(MyApplication.CARD_NUM);
			}else{
				et_card_num.setText(result);
			}
		}
	}
}
