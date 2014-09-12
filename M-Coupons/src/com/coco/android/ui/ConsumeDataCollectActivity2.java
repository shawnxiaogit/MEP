package com.coco.android.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.coco.android.BaseActivity;
import com.coco.android.MyApplication;
import com.coco.android.R;
import com.coco.android.bean.ConsumeCancleNode;
import com.coco.android.bean.DealNode;
import com.coco.android.bean.GetOperaterDetailNode;
import com.coco.android.bean.ConsumeCancleNode.Node;
import com.coco.android.bean.RequestUtil;
import com.coco.android.socket.SocketClient;
import com.coco.android.util.L;
import com.coco.android.wheelview.ScreenInfo;
import com.coco.android.wheelview.WheelMain;
import com.easier.ui.CalendarActivity;
import com.google.zxing.CaptureActivity;

/**
 * 
 * @Descriptio 查询消费明细，信息收集界面
 * @author Shawn
 * @Time 2013-8-22  下午2:14:58
 */
public class ConsumeDataCollectActivity2 extends BaseActivity {
	
	/**
	 * 请求类型
	 */
	public static final String REQUEST_TYPE = "REQUEST_TYPE";
	
	
	/**
	 * 默认请求类型值
	 */
	public static final int DEFAULT_REQUEST = -1;
	
	/**
	 * 撤销充值
	 */
	public static final int CHE_XIAO = 111;
	/**
	 * 查询交易明细
	 */
	public static final int CHA_XUN_MING_XI = 112;
	/**
	 * 查询对账信息
	 */
	public static final int DUI_ZHANG_CHA_XUN = 113;
	/**
	 * 查询汇总信息
	 */
	public static final int CHA_XUN_HUIZONG = 114;
	/**
	 * 界面请求类型
	 */
	private int mRequestType;
	
	/**
	 * 编辑框卡号
	 */
	private EditText et_card_num;
	/**
	 * 编辑框结束时间
	 */
	private EditText et_start_date;
	/**
	 * 编辑康结束时间
	 */
	private EditText et_end_date;
	
	
	/**
	 * 按钮 扫一扫
	 */
	private Button btn_scan;
	/**
	 * 按钮 查询
	 */
	private Button btn_query;
	
	/**
	 * 获取选择的开始日期 
	 */
	private static final int GET_START_SELECT_DATE = 222;
	/**
	 * 获取选择的结束日期
	 */
	private static final int GET_END_SELECT_DATE = 224;
	
	/**
	 * 获取扫描的二维码 request_code
	 */
	private static final int GET_CARD_NUM    = 223;
	
	/**
	 * 标题栏返回按钮
	 */
	private Button btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.consume_data_collect2);
		
		//初始化控件
		initView();
		//设置控件监听器
		setUpViewListener();
		
	}
	
	/**
	 * 
	 * @Description 为控件设置监听器
	 * @author Shawn
	 * @Time 2013-8-22  下午5:49:34
	 * @param param
	 * @return void
	 * @exception exception
	 */
	private void setUpViewListener(){
		btn_scan.setOnClickListener(new ScanListener());
		
//		et_start_date.setOnClickListener(new SelectDateListener(GET_START_SELECT_DATE));
//		et_end_date.setOnClickListener(new SelectDateListener(GET_END_SELECT_DATE));
		
		et_start_date.setOnClickListener(new TimeSelectListener(TYPE_DATE,START_TYPE));
		et_end_date.setOnClickListener(new TimeSelectListener(TYPE_DATE,END_TYPE));
		
		btn_query.setOnClickListener(new QueryListener());
		
		btn_back.setOnClickListener(new BackListener());
		
	}
	
	private class TimeSelectListener implements OnClickListener{
		private int mDateType;
		private int mStartEndType;
		public TimeSelectListener(int dateType,int startEndType){
			mDateType = dateType;
			mStartEndType = startEndType;
		}
		
		@Override
		public void onClick(View arg0) {
			showTimeSelect(mDateType,mStartEndType);
		}
		
	}
	
	
	//日期类型
	//日期
	private static final int TYPE_DATE = 1111;
	//分钟
	private static final int TYPE_MINU = 1112;
	
	//开始
	public static final int START_TYPE = 2221;
	//结束
	public static final int END_TYPE = 2222;
	
	
	@SuppressLint("NewApi") public void showTimeSelect(final int timeType,final int startEndType) {
		LayoutInflater inflater = LayoutInflater.from(ConsumeDataCollectActivity2.this);
		View timepickerview = inflater.inflate(R.layout.selectbirthday, null);
		ScreenInfo screenInfo = new ScreenInfo(ConsumeDataCollectActivity2.this);
		final WheelMain wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		Calendar calendar = Calendar.getInstance();
		
		//设置日历类型
		if(timeType == TYPE_DATE){
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			wheelMain.setTime(year, month, day);
		}else if(timeType == TYPE_MINU){
			wheelMain.showHours(calendar.get(Calendar.HOUR),
					calendar.get(Calendar.MINUTE));
		}
		final Dialog dialog1 = new AlertDialog.Builder(this,R.style.calendar_dialog_style).setView(
				timepickerview).show();
		Window window = dialog1.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.mystyle); // 添加动画
		Button ig_lunar = (Button) timepickerview.findViewById(R.id.ig_lunar);
		
		Button ig_solar = (Button) timepickerview.findViewById(R.id.ig_solar);
//		if(timeType == TYPE_DATE){
//			ig_lunar.setVisibility(View.VISIBLE);
//			ig_solar.setVisibility(View.VISIBLE);
//		}else if(timeType == TYPE_MINU){
			ig_lunar.setVisibility(View.GONE);
			ig_solar.setVisibility(View.GONE);
//		}

		
		Button btn = (Button) timepickerview
				.findViewById(R.id.btn_datetime_sure);
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				if(timeType == TYPE_DATE){
					String info = wheelMain.getTime();
					if(startEndType == START_TYPE){
						et_start_date.setText(info);
					}else if(startEndType == END_TYPE){
						et_end_date.setText(info);
					}
				}else if(timeType == TYPE_MINU){
					String info = wheelMain.getHourMin();
					if(startEndType == START_TYPE){
//						et_start_time.setText(info);
					}else if(startEndType == END_TYPE){
//						et_end_time.setText(info);
					}
				}
				
				dialog1.dismiss();
			
			}
		});
	}
	
	/**
	 * 查询按钮监听器
	 * @author ShawnXiao
	 *
	 */
	private class QueryListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			String card_num = et_card_num.getText().toString();
			String start_time = et_start_date.getText().toString();
			String end_time = et_end_date.getText().toString();
			
			
			
			if(isDataEmpty(start_time,end_time)){
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
			//判断结束时间是否小于开始时间
			if(MyApplication.isEndTimeSmallThanStartTime(start_time,end_time)){
				showShortToast(R.string.end_time_can_not_small_than_start_time);
				return;
			}
			
			Date  start_date=null;
			Date end_date=null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
			try {
				start_date  = format.parse(start_time);
				end_date = format.parse(end_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			String start_time2 = format2.format(start_date);
			String end_time2 =  format2.format(end_date);
			L.e("start_time2:", ""+start_time2);
			L.e("end_time2", end_time2);
			
			String value_60 = MyApplication.getSixtyValue3(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
			
			MyApplication.CHE_XIAO_VALUE_60 = value_60;
			MyApplication.CHE_XIAO_CARD_NUM = sb.toString();
			MyApplication.CHE_XIAO_START_TIME = start_time2;
			MyApplication.CHE_XIAO_END_TIME = end_time2;
			
			
			byte[] request = ConsumeCancleNode.buildQueryList(value_60,sb.toString(),start_time2,end_time2,null,null);
			
			mRunningTask = new GetConsumeListTask(request);
			mRunningTask.execute();
		}
		
	}
	
	
	/**
	 * 获取消费列表任务
	 * @author ShawnXiao
	 *
	 */
	private class GetConsumeListTask extends AsyncTask{
		private byte[] mRequest;
		public GetConsumeListTask(byte[] request){
			mRequest = request;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.handing_process);
		}
		

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
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
				if (hex_response != null && hex_response.length() > 0) {
					
//					GetOperaterDetailNode node = GetOperaterDetailNode.parseMsg(hex_response);
					//解析报文
					ConsumeCancleNode node = ConsumeCancleNode.parseMsg(hex_response);
					node.setCard_num(et_card_num.getText().toString());
					if(node!=null){
						String code = getResources().getStringArray(
								R.array.return_code)[0];
						L.e("code:", code);
						if(node.getReturn_code()!=null){
							L.e("node.getReturn_code():", "" + node.getReturn_code());
							if(node.getReturn_code().equals(code)){
//								ArrayList<DealNode> nodes = node.getDealNodes();
								Intent intent = new Intent(ConsumeDataCollectActivity2.this,ConsumeCancleActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("data", node);
								bundle.putInt(REQUEST_TYPE, mRequestType);
								intent.putExtras(bundle);
								ConsumeDataCollectActivity2.this.startActivity(intent);
								
							}else{
								showShortToast(R.string.get_data_faield);
							}
							mAlertDialog.dismiss();
						}
					}
					else{
						showShortToast(R.string.get_data_faield);
					}
					mAlertDialog.dismiss();
				}
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				showShortToast(R.string.get_data_faield);
				mAlertDialog.dismiss();
			}break;
			}
			
		}
	};
	
	private boolean isDataEmpty(String start_time,String end_time){
		boolean result = true;
//		if(TextUtils.isEmpty(card_num)){
//			showShortToast(R.string.consume_card_num_can_not_null);
//			result = true;
//		}else{
			if(TextUtils.isEmpty(start_time)){
				showShortToast(R.string.start_time_can_not_null);
				result = true;
			}else{
				if(TextUtils.isEmpty(end_time)){
					showShortToast(R.string.end_time_can_not_null);
				}else{
					result = false;
				}
			}
//		}
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
			Intent intent_query = new Intent(ConsumeDataCollectActivity2.this,CaptureActivity.class);
			startActivityForResult(intent_query,GET_CARD_NUM);
		}
		
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
			ConsumeDataCollectActivity2.this.finish();
		}
		
	}
	
	
	/**
	 * 
	 * @Descriptio 选择日期监听器
	 * @author Shawn
	 * @Time 2013-8-22  下午5:50:36
	 */
	private class SelectDateListener implements OnClickListener{
	
		private int mType;
		public SelectDateListener(int type){
			mType = type;
		}

		@Override
		public void onClick(View v) {
			switch(mType){
			case GET_START_SELECT_DATE:{
				Intent intent_select_date = new Intent(ConsumeDataCollectActivity2.this,
						CalendarActivity.class);
				ConsumeDataCollectActivity2.this.startActivityForResult(intent_select_date, 
						GET_START_SELECT_DATE);
			}break;
			case GET_END_SELECT_DATE:{
				Intent intent_select_date = new Intent(ConsumeDataCollectActivity2.this,
						CalendarActivity.class);
				ConsumeDataCollectActivity2.this.startActivityForResult(intent_select_date, 
						GET_END_SELECT_DATE);
			}break;
			}
			
			
		}
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch(requestCode){
			case GET_START_SELECT_DATE:{
				String select_date = data.getExtras().getString("result");
				L.e("select_date:", ""+select_date);
				if(select_date!=null&&select_date.length()>0){
					et_start_date.setText(select_date);
				}
			}break;
			case GET_END_SELECT_DATE:{
				String select_date = data.getExtras().getString("result");
				L.e("select_date:", ""+select_date);
				if(select_date!=null&&select_date.length()>0){
					et_end_date.setText(select_date);
				}
			}break;
			case GET_CARD_NUM:{
				String result = data.getExtras().getString("result");
				L.e("result", ""+result);
				if(TextUtils.isEmpty(result)){
					showShortToast(R.string.scan_retry);
					return;
				}
				et_card_num.setText(result);
			}break;
			default:
				break;
			}
		}
	}
	
	
	/**
	 * 
	 * @Description 初始化控件
	 * @author Shawn
	 * @Time 2013-8-22  下午5:43:27
	 * @return void
	 */
	private void initView(){
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.tv_title)).setText(getResources().getStringArray(R.array.main_menu)[1]);
		
		et_card_num = (EditText) findViewById(R.id.et_card_num);
		et_start_date = (EditText) findViewById(R.id.et_start_date);
		et_end_date = (EditText) findViewById(R.id.et_end_date);
		
		
		btn_scan = (Button) findViewById(R.id.btn_scan);
		btn_query = (Button) findViewById(R.id.btn_query);
		
		Intent intent = getIntent();
		mRequestType = intent.getExtras().getInt(REQUEST_TYPE);
		switch(mRequestType){
		case CHE_XIAO:{
			((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_4);
		}break;
		case CHA_XUN_MING_XI:{
			((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_2);
		}break;
		case DUI_ZHANG_CHA_XUN:{
			((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_5);
		}break;
		}
		
		if(L.GET_DEAL_LIST_DEBUG){
			et_card_num.setText("9009320100011158");
			et_start_date.setText("2013年11月01日");
			et_end_date.setText("2013年11月04日");
		}
	}
}
