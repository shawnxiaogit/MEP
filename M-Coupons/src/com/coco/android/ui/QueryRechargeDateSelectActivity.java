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
import android.content.SharedPreferences;
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
import com.coco.android.bean.ConsumeCancleNode.Node;
import com.coco.android.bean.GetHuiZongNode;
import com.coco.android.bean.GetOperaterDetailNode;
import com.coco.android.bean.GetOperaterDetailNode3;
import com.coco.android.bean.RequestUtil;
import com.coco.android.socket.SocketClient;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.wheelview.ScreenInfo;
import com.coco.android.wheelview.WheelMain;
import com.easier.ui.CalendarActivity;

/**
 * 
 * @Descriptio ��ѯ����Ա������ϸ������ѡ�����
 * @author Shawn
 * @Time 2013-8-22  ����2:14:58
 */
public class QueryRechargeDateSelectActivity extends BaseActivity {
	
	
	
	/**
	 * �ı�����ͷ
	 */
	private TextView title_bar_title;
	/**
	 * ��������෵�ذ�ť
	 */
	private Button title_bar_left_button;
	
	
	
	/**
	 * �༭��ʼ����
	 */
	private EditText et_start_date;
	
	/**
	 * �༭��ʼʱ��
	 */
	private EditText et_start_time;
	
	/**
	 * �༭���������
	 */
	private EditText et_end_date;
	
	/**
	 * �༭�����ʱ��
	 */
	private EditText et_end_time;
	
	/**
	 * ��ť ��ѯ
	 */
	private Button btn_query;
	
	/**
	 * ��ȡѡ��Ŀ�ʼ���� 
	 */
	private static final int GET_START_SELECT_DATE = 222;
	/**
	 * ��ȡѡ��Ľ�������
	 */
	private static final int GET_END_SELECT_DATE = 224;
	
	
	/**
	 * ���������ذ�ť
	 */
	private Button btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.query_recharge_date_select);
		
		//��ʼ���ؼ�
		initView();
		//���ÿؼ�������
		setUpViewListener();
		
	}
	
	/**
	 * 
	 * @Description Ϊ�ؼ����ü�����
	 * @author Shawn
	 * @Time 2013-8-22  ����5:49:34
	 * @param param
	 * @return void
	 * @exception exception
	 */
	private void setUpViewListener(){
		
		et_start_date.setOnClickListener(new TimeSelectListener(TYPE_DATE,START_TYPE));
		et_start_time.setOnClickListener(new TimeSelectListener(TYPE_MINU,START_TYPE));
		
		et_end_date.setOnClickListener(new TimeSelectListener(TYPE_DATE,END_TYPE));
		et_end_time.setOnClickListener(new TimeSelectListener(TYPE_MINU,END_TYPE));
		
		
		
		
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
	
	
	
	
	
	
	/**
	 * ��ȡ����Ա��ϸ����
	 * @author ShawnXiao
	 *
	 */
	private class GetHuiZongTask extends AsyncTask{
		private byte[] mRequest;
		public GetHuiZongTask(byte[] request){
			mRequest = request;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.handing_process);
		}
		@Override
		protected Object doInBackground(Object... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest,getHuiZongHandler);
			client.start();
		}
		
	}
	/**
	 * ��ȡ����Ա��ϸ��������
	 */
	Handler getHuiZongHandler = new Handler(){
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
				case RequestUtil.REQUEST_SUCCESS: {
					String hex_response = (String) msg.obj;
					L.e("hex_response:", DigitalTrans.bytetoString(hex_response.getBytes()));
					if (hex_response != null && hex_response.length() > 0) {
						//��������
						GetHuiZongNode node = GetHuiZongNode.parseMsg(hex_response);
						String code = getResources().getStringArray(
								R.array.return_code)[0];
						L.e("code:", code);
						L.e("node.getReturn_code():", "" + node.getReturn_code());
						if (node.getReturn_code().equals(code)) {
							Intent intent  = new Intent(QueryRechargeDateSelectActivity.this,
									MepHuiZongActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable(MepHuiZongActivity.KEY_QUERY_DATAS, node);
							intent.putExtras(bundle);
							QueryRechargeDateSelectActivity.this.startActivity(intent);
							overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
						}else{
							showShortToast(R.string.query_faield);
						}
					}else{
						showShortToast(R.string.query_faield);
					}
					mAlertDialog.dismiss();
				}break;
				case RequestUtil.REQUEST_FAIELD: {
					showShortToast(R.string.query_faield);
					mAlertDialog.dismiss();
				}break;
			}
		};
	};  

	
	
	
	/**
	 * ��ȡ����Ա��ϸ����
	 * @author ShawnXiao
	 *
	 */
	private class GetOperaterDetailTask extends AsyncTask{
		private byte[] mRequest;
		public GetOperaterDetailTask(byte[] request){
			mRequest = request;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.handing_process);
		}
		@Override
		protected Object doInBackground(Object... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest,getOperaterDetailHandler);
			client.start();
		}
		
	}
	/**
	 * ��ȡ����Ա��ϸ��������
	 */
	Handler getOperaterDetailHandler = new Handler(){
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
				case RequestUtil.REQUEST_SUCCESS: {
					String hex_response = (String) msg.obj;
					if (hex_response != null && hex_response.length() > 0) {
						
						L.e("hex_response", ""+hex_response);
						//��������
						GetOperaterDetailNode3 node = GetOperaterDetailNode3.parseMsg(hex_response);
						String code = getResources().getStringArray(
								R.array.return_code)[0];
						L.e("code:", code);
						L.e("node.getReturn_code():", "" + node.getReturn_code());
						if (node.getReturn_code().equals(code)) {
							Intent intent  = new Intent(QueryRechargeDateSelectActivity.this,
									RecharAndRecharCancleDetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable(RecharAndRecharCancleDetailActivity.KEY_QUERY_DATAS, node);
							intent.putExtras(bundle);
							QueryRechargeDateSelectActivity.this.startActivity(intent);
							overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
						}else{
							showShortToast(R.string.query_faield);
						}
					}else{
						showShortToast(R.string.query_faield);
					}
					mAlertDialog.dismiss();
				}break;
				case RequestUtil.REQUEST_FAIELD: {
					showShortToast(R.string.query_faield);
					mAlertDialog.dismiss();
				}break;
			}
		};
	};  
	/**
	 * ��ѯ��ť������
	 * @author ShawnXiao
	 *
	 */
	private class QueryListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			String start_time = et_start_date.getText().toString();
			String end_time = et_end_date.getText().toString();
			
			String start_min = et_start_time.getText().toString();
			String end_min = et_end_time.getText().toString();
			
			//��ʼ���ںͽ������ڲ���Ϊ��
			if(TextUtils.isEmpty(start_time)){
				showShortToast(R.string.start_date_can_not_null);
				return;
			}
			if(TextUtils.isEmpty(start_min)){
				showShortToast(R.string.start_time_can_not_null);
				return;
			}
			
			if(TextUtils.isEmpty(end_time)){
				showShortToast(R.string.end_date_can_not_null);
				return;
			}
			
			if(TextUtils.isEmpty(end_min)){
				showShortToast(R.string.end_time_can_not_null);
				return;
			}
			
			if(L.NOT_RECHARGE_DEBUG){
				Intent intent  = new Intent(QueryRechargeDateSelectActivity.this,
						RecharAndRecharCancleDetailActivity.class);
				QueryRechargeDateSelectActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				
			}else{
				Date  start_date=null;
				Date end_date=null;
				SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd��");
				SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
				
				SimpleDateFormat format3  = new SimpleDateFormat("HH:mm");
				
				SimpleDateFormat format4  = new SimpleDateFormat("HH:mm:ss");
				
				Date start_min2 = null;
				Date end_min2 = null;
						
				try {
					
					start_date  = format.parse(start_time);
					end_date = format.parse(end_time);
				
					start_min2 = format3.parse(start_min);
					end_min2 = format3.parse(end_min);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
				if(MyApplication.isEndTimeSmallThanStartTime2(start_date,end_date,start_min2,end_min2)){
					showShortToast(R.string.end_time_can_not_small_than_start_time);
					return;
				}
				String start_time2 = format2.format(start_date);
				String end_time2 =  format2.format(end_date);
				
				start_min = format4.format(start_min2);
				end_min = format4.format(end_min2);
				
				L.e("start_time2:", ""+start_time2);
				L.e("end_time2:", ""+end_time2);
				
				L.e("start_min3:", ""+start_min);
				L.e("end_min3:", ""+start_min);
				
				// ��ѯ��־ 1-�Լ�2-����
				
				String phone_info = MyApplication.getSixtyValue3(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
//				String phone_info = MyApplication.getSixtyValue3("AD103      ","460023007363048","86591901436736");
				
				//�����ѯ���ֻ���Ϣ
				MyApplication.PHONE_INFO2 = phone_info;
				//�����ѯ��ʼʱ��
				MyApplication.QUERY_START_TIME = start_time2;
				//�����ѯ����ʱ��
				MyApplication.QUERY_END_TIME = end_time2;
				//�����ѯ��ʼ����
				MyApplication.QUERY_START_MIN = start_min;
				//�����ѯ��������
				MyApplication.QUERY_END_MIN = end_min;
				
				L.e("MyApplication.COMM_NUM:", ""+MyApplication.COMM_NUM);
				L.e("mRequestType:", ""+mRequestType);
				L.e("MyApplication.PHONE_NUM", ""+MyApplication.PHONE_NUM);
				
				L.e("MyApplication.myMepAuority == MyApplication.MEP_BOOSS_AUTHORITY", ""+(MyApplication.myMepAuority == MyApplication.MEP_BOOSS_AUTHORITY));
				//��ѯ������ϸ
				if(mRequestType == ConsumeDataCollectActivity2.CHA_XUN_MING_XI||
						mRequestType == ConsumeDataCollectActivity2.CHE_XIAO){
					//���û��Ȩ�ޣ����ѯ���Ǹ�Mep�Ľ�����ϸ
					if(MyApplication.myMepAuority == MyApplication.MEP_BOOSS_AUTHORITY){
						byte[] request = GetOperaterDetailNode3.getOpraterDetailMsg(
								phone_info,
								MyApplication.QUERY_MEP_DEAL_CURRENT_RETURN_POSI,
								MyApplication.QUERY_MEP_DEAL_CURRENT_RETURN_COUNT,
								MyApplication.COMM_NUM,
								start_time2,
								end_time2,
								start_min,
								end_min,
								MyApplication.QUERY_MEP_DEAL_QUERY_TYPE_D
								);
						mRunningTask = new GetOperaterDetailTask(request);
						mRunningTask.execute();
						
					}else{
						//�����Ȩ�ޣ����ѯ�����̻��µĽ�����ϸ
						byte[] request = GetOperaterDetailNode3.getOpraterDetailMsg(
								phone_info,
								MyApplication.QUERY_MEP_DEAL_CURRENT_RETURN_POSI,
								MyApplication.QUERY_MEP_DEAL_CURRENT_RETURN_COUNT,
								null,
								start_time2,
								end_time2,
								start_min,
								end_min,
								MyApplication.QUERY_MEP_DEAL_QUERY_TYPE_D
								);
						mRunningTask = new GetOperaterDetailTask(request);
						mRunningTask.execute();
					}
					
					
					
//					byte[] request = GetOperaterDetailNode.getOpraterDetailMsg(
//							phone_info,
//							start_time2,
//							end_time2,
//							MyApplication.EMPLOYEE,
//							MyApplication.GET_OPERATER_DETAIL_QUERY_TYPE,
//							null,
//							null,
//							null,
//							MyApplication.QUERY_DEAL_DETAIL_CURRENT_RETURN_FLAG,
//							MyApplication.QUERY_DEAL_DETAIL_QUERY_CURRENT_RETURN_COUNT,
//							null
//							);
//					mRunningTask = new GetOperaterDetailTask(request);
//					mRunningTask.execute();
				}else if(mRequestType == ConsumeDataCollectActivity2.CHA_XUN_HUIZONG){
				
					byte[] request = GetHuiZongNode.buildMsg(phone_info, "1", "10", start_time2, end_time2,start_min,end_min, MyApplication.COMM_NUM);
							
					mRunningTask = new GetHuiZongTask(request);
					mRunningTask.execute();
				}
				
			}
		}
		
	}
	
	
	/**
	 * ��ȡ�����б�����
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
					//��������
					ConsumeCancleNode node = ConsumeCancleNode.parseMsg(hex_response);
//					node.setCard_num(et_card_num.getText().toString());
					if(node!=null){
						String code = getResources().getStringArray(
								R.array.return_code)[0];
						L.e("code:", code);
						if(node.getReturn_code()!=null){
							L.e("node.getReturn_code():", "" + node.getReturn_code());
							if(node.getReturn_code().equals(code)){
								ArrayList<Node> nodes = node.getNodes();
								if(nodes!=null&&nodes.size()>0){
									Intent intent = new Intent(QueryRechargeDateSelectActivity.this,ConsumeCancleActivity.class);
									Bundle bundle = new Bundle();
									bundle.putSerializable("data", node);
//									bundle.putInt(REQUEST_TYPE, mRequestType);
									intent.putExtras(bundle);
									QueryRechargeDateSelectActivity.this.startActivity(intent);
								}else{
									showShortToast(R.string.no_data);
								}
								
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
	
	private boolean isDataEmpty(String card_num,String start_time,String end_time){
		boolean result = true;
		if(TextUtils.isEmpty(card_num)){
			showShortToast(R.string.consume_card_num_can_not_null);
			result = true;
		}else{
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
		}
		return result;
	}
	
	
	/**
	 * 
	 * @Descriptio ������һ������
	 * @author Shawn
	 * @Time 2013-8-23  ����3:50:43
	 */
	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			QueryRechargeDateSelectActivity.this.finish();
		}
		
	}
	
	//��������
	//����
	private static final int TYPE_DATE = 1111;
	//����
	private static final int TYPE_MINU = 1112;
	
	//��ʼ
	public static final int START_TYPE = 2221;
	//����
	public static final int END_TYPE = 2222;
	
	@SuppressLint("NewApi") public void showTimeSelect(final int timeType,final int startEndType) {
		LayoutInflater inflater = LayoutInflater.from(QueryRechargeDateSelectActivity.this);
		View timepickerview = inflater.inflate(R.layout.selectbirthday, null);
		ScreenInfo screenInfo = new ScreenInfo(QueryRechargeDateSelectActivity.this);
		final WheelMain wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		Calendar calendar = Calendar.getInstance();
		
		//������������
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
		window.setGravity(Gravity.BOTTOM); // �˴���������dialog��ʾ��λ��
		window.setWindowAnimations(R.style.mystyle); // ��Ӷ���
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
						et_start_time.setText(info);
					}else if(startEndType == END_TYPE){
						et_end_time.setText(info);
					}
				}
				
				dialog1.dismiss();
			
			}
		});
	}
	
	
	/**
	 * 
	 * @Descriptio ѡ�����ڼ�����
	 * @author Shawn
	 * @Time 2013-8-22  ����5:50:36
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
				Intent intent_select_date = new Intent(QueryRechargeDateSelectActivity.this,
						CalendarActivity.class);
				QueryRechargeDateSelectActivity.this.startActivityForResult(intent_select_date, 
						GET_START_SELECT_DATE);
			}break;
			case GET_END_SELECT_DATE:{
				Intent intent_select_date = new Intent(QueryRechargeDateSelectActivity.this,
						CalendarActivity.class);
				QueryRechargeDateSelectActivity.this.startActivityForResult(intent_select_date, 
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
			default:
				break;
			}
		}
	}
	
	/**
	 * TextView ����ͷ
	 */
	private TextView tv_title;
	
	/**
	 * 
	 * @Description ��ʼ���ؼ�
	 * @author Shawn
	 * @Time 2013-8-22  ����5:43:27
	 * @return void
	 */
	private void initView(){
		tv_title = ((TextView)findViewById(R.id.tv_title));
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		
		
		et_start_date = (EditText) findViewById(R.id.et_start_date);
		et_start_time = (EditText) findViewById(R.id.et_start_time);
		
		et_end_date = (EditText) findViewById(R.id.et_end_date);
		et_end_time = (EditText) findViewById(R.id.et_end_time);
		
		
		btn_query = (Button) findViewById(R.id.btn_query);
		
		
		if(L.GET_DEAL_LIST_DEBUG){
			et_start_date.setText("2013��10��25��");
			et_end_date.setText("2013��11��08��");
		}
		
		
		Intent intent = getIntent();
		mRequestType = intent.getIntExtra(ConsumeDataCollectActivity2.REQUEST_TYPE,
				ConsumeDataCollectActivity2.DEFAULT_REQUEST );
		
		if(mRequestType == ConsumeDataCollectActivity2.CHA_XUN_MING_XI){
			tv_title.setText(getResources().getStringArray(R.array.main_menu)[3]);
		}else if(mRequestType == ConsumeDataCollectActivity2.CHE_XIAO){
			tv_title.setText(getResources().getStringArray(R.array.main_menu)[1]);
		}else if(mRequestType == ConsumeDataCollectActivity2.CHA_XUN_HUIZONG){
			tv_title.setText(getResources().getStringArray(R.array.main_menu)[4]);
		}
	}
	/**
	 * �ϸ����洫�ݹ�������������
	 */
	private int mRequestType = ConsumeDataCollectActivity2.DEFAULT_REQUEST;
}
