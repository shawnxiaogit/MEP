package com.coco.android.ui;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.coco.android.BaseActivity;
import com.coco.android.MyApplication;
import com.coco.android.R;
import com.coco.android.bean.DealNode;
import com.coco.android.bean.GetHuiZongNode;
import com.coco.android.bean.GetOperaterDetailNode;
import com.coco.android.bean.RechaAndRechaCancleNode;
import com.coco.android.bean.RequestUtil;
import com.coco.android.socket.SocketClient;
import com.coco.android.util.DigitalTrans;
import com.coco.android.util.L;
import com.coco.android.xlist.XListView;
import com.coco.android.xlist.XListView.IXListViewListener;


/**
 * Mep汇总信息情界面
 * @author ShawnXiao
 *
 */
public class MepHuiZongActivity extends BaseActivity{
	
	/**
	 * 上个界面传过来的数据键
	 */
	public static final String KEY_QUERY_DATAS = "KEY_QUERY_DATAS";
	
//	 /**
//	  * 功能列表
//	 */
//	private ListView mListView;
	
	
	int pageNum = 1; // 当前页 从1开始
	int pageSize = 10; // 每页记录
	int totalPage = 0; // 记录总页数
	int totalRecords = 0; // 总记录条数
	
	private XListView mListView;
	
	/**
	 * 数据适配器
	 */
	private MyDataAdapter mAdapter;
	
	
	private ArrayList<RechaAndRechaCancleNode> mDatas;
	
	/**
	 * 标题栏返回按钮
	 */
	private Button btn_back;
	
	/**
	 * 上个界面传过来的操作员明细实体类
	 */
	private GetHuiZongNode rechargeNode;
	
	/**
	 * 界面数据汇总
	 */
	private TextView tv_total_money;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.e("RecharAndRecharCancleDetailActivity", "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rechar_and_rechar_cancle_detail);
		
		mListView = (XListView) findViewById(R.id.lv_rechar_data);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		
		mListView.setXListViewListener(new MyXListViewListener());
		
		((TextView)findViewById(R.id.tv_title)).setText(getResources().getStringArray(R.array.main_menu)[4]);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new BackListener());
		
	
		Intent intent = getIntent();
		rechargeNode = (GetHuiZongNode) intent.getExtras().getSerializable(KEY_QUERY_DATAS);
//		GetOperaterDetailNode.printMsg(rechargeNode);
		//初始化数据，后面要修改为网络获取
//		initData(rechargeNode);
//		comparator = new MyComparator();
//		Collections.sort(rechargeNode.getDealNodes(), comparator);
		mAdapter = new MyDataAdapter(this,rechargeNode.getDealNodes());
		mListView.setAdapter(mAdapter);
		
		tv_total_money = (TextView) findViewById(R.id.tv_total_money);
		tv_total_money.setText(getTotalMoney(rechargeNode.getDealNodes())
				+getResources().getString(R.string.yuan));
		
		//设置总的记录数量
		totalRecords = Integer.parseInt(rechargeNode.getTotal_count());
		//设置当前页数为1
		pageNum = 1;
	}
	
//	MyComparator comparator;
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mAdapter.getCount()<totalRecords){
			mListView.setPullLoadEnable(true);
		}else{
			mListView.setPullLoadEnable(false);
		}
	}
	
	
	
	//刷新获取数据
	private static final int _REFRESH_DATA = 222;
	//不是刷新获取数据
	private static final int _NOT_REFRESH_DATA = 111;
	
	private static int is_refresh = -1;
	
	private class MyXListViewListener implements IXListViewListener{
		// 下拉更新
		@Override
		public void onRefresh() {
			
			is_refresh = _REFRESH_DATA;
			pageNum = 1;
			getOperaterDetailHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					getData(pageNum,pageSize);
				}
			}, 10);
			
		}
		// 上滑更多
		@Override
		public void onLoadMore() {
			
			is_refresh = _NOT_REFRESH_DATA;
			
			L.e("onLoadMore", "onLoadMore");
			getOperaterDetailHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					L.e("mAdapter.getCount():", ""+(mAdapter.getCount()));
					L.e("totalRecords:", ""+totalRecords);;
					L.e("mAdapter.getCount()<totalRecords:", ""+(mAdapter.getCount()<totalRecords));
					if(mAdapter.getCount()<totalRecords){
						mListView.setPullLoadEnable(true);
						int requ_start = ((pageNum*pageSize)+1);
						L.e("requ_start", ""+requ_start);
						getData(requ_start,pageSize);
						pageNum++;
					}
					
				}
			}, 10);
		}
		
	}
	
	private void getData(int start,int count){
		L.e("getData:", "getData");
		String phone_info = MyApplication.getSixtyValue3(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
////		byte[] request = GetOperaterDetailNode.getOpraterDetailMsg(MyApplication.PHONE_INFO2,
//		byte[] request = GetOperaterDetailNode.getOpraterDetailMsg(phone_info,
//				MyApplication.QUERY_START_TIME,
//				MyApplication.QUERY_END_TIME,
//				MyApplication.EMPLOYEE,
//				MyApplication.GET_OPERATER_DETAIL_QUERY_TYPE,
//				null,
//				null,
//				null,
//				String.valueOf(start),
//				String.valueOf(count));
////				byte[] request = GetOperaterDetailNode.getOpraterDetailMsg(phone_info, start_time2, end_time2,
////						"1","C");
//		mRunningTask = new GetOperaterDetailTask(request);
//		mRunningTask.execute();
		
		
		
		byte[] request = GetHuiZongNode.buildMsg(phone_info, 
				String.valueOf(start), 
				String.valueOf(count), 
				MyApplication.QUERY_START_TIME, 
				MyApplication.QUERY_END_TIME,
				MyApplication.QUERY_START_MIN,
				MyApplication.QUERY_END_MIN,
				MyApplication.COMM_NUM);
				
//				byte[] request = GetOperaterDetailNode.getOpraterDetailMsg(phone_info, start_time2, end_time2,
//						"1","C");
		mRunningTask = new GetHuiZongTask(request);
		mRunningTask.execute();
	}
	
	
	/**
	 * 获取操作员明细任务
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
	 * 获取操作员明细处理助手
	 */
	Handler getHuiZongHandler = new Handler(){
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
				case RequestUtil.REQUEST_SUCCESS: {
					String hex_response = (String) msg.obj;
					L.e("hex_response:", DigitalTrans.bytetoString(hex_response.getBytes()));
					if (hex_response != null && hex_response.length() > 0) {
						//解析报文
						GetHuiZongNode node = GetHuiZongNode.parseMsg(hex_response);
						String code = getResources().getStringArray(
								R.array.return_code)[0];
						L.e("code:", code);
						L.e("node.getReturn_code():", "" + node.getReturn_code());
						if (node.getReturn_code().equals(code)) {
//							Intent intent  = new Intent(QueryRechargeDateSelectActivity.this,
//									MepHuiZongActivity.class);
//							Bundle bundle = new Bundle();
//							bundle.putSerializable(MepHuiZongActivity.KEY_QUERY_DATAS, node);
//							intent.putExtras(bundle);
//							QueryRechargeDateSelectActivity.this.startActivity(intent);
//							overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
							
							
							
							if(mAdapter!=null){
								mAdapter.addDatas(node.getDealNodes());
								mAdapter.notifyDataSetChanged();
							}
							totalRecords = Integer.parseInt(node.getTotal_count());
							L.e("totalRecords:", ""+totalRecords);
							L.e("mAdapter.getCount():", ""+mAdapter.getCount());
							L.e("mAdapter.getCount()<totalRecords：", ""+(mAdapter.getCount()<totalRecords));
							
							//显示总金额
							tv_total_money.setText(getTotalMoney(mAdapter.getData())
									+getResources().getString(R.string.yuan));
							
							
							//当前已加载的数据小于总数据条数时，显示底部更多，否则隐藏
							if(mAdapter.getCount()<totalRecords){
								mListView.setPullLoadEnable(true);
							}else{
								mListView.setPullLoadEnable(false);
								showShortToast(R.string.no_more_data);
							}
							
							onStopLoad();
							
							
						}else{
							showShortToast(R.string.query_faield);
						}
					}else{
						showShortToast(R.string.query_faield);
					}
					if(mAlertDialog!=null&&mAlertDialog.isShowing()){
						mAlertDialog.dismiss();
					}
				}break;
				case RequestUtil.REQUEST_FAIELD: {
					showShortToast(R.string.query_faield);
					if(mAlertDialog!=null&&mAlertDialog.isShowing()){
						mAlertDialog.dismiss();
					}
				}break;
			}
		};
	}; 
	
	
	
	// 停止加载框
	private void onStopLoad() {
		L.e("onStopLoad", "onStopLoad");
		mListView.stopRefresh();
		mListView.stopLoadMore();
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		mListView.setRefreshTime("" + hour + ":" + (minute < 10 ? "0" : "")
				+ minute);
	}
	
	
	/**
	 * 获取操作员明细任务
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
	 * 获取操作员明细处理助手
	 */
	Handler getOperaterDetailHandler = new Handler(){
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
				case RequestUtil.REQUEST_SUCCESS: {
					String hex_response = (String) msg.obj;
					if (hex_response != null && hex_response.length() > 0) {
						//解析报文
						GetOperaterDetailNode node = GetOperaterDetailNode.parseMsg(hex_response);
						String code = getResources().getStringArray(
								R.array.return_code)[0];
						L.e("code:", code);
						L.e("node.getReturn_code():", "" + node.getReturn_code());
						if (node.getReturn_code().equals(code)) {
							if(is_refresh == _REFRESH_DATA){
								is_refresh = _NOT_REFRESH_DATA;
								if(mAdapter!=null){
									mAdapter.removeData();
									mAdapter.addDatas(node.getDealNodes());
								}
							}else{
							
								if(mAdapter!=null){
									mAdapter.addDatas(node.getDealNodes());
									
								}
								
							}
							
//							Collections.sort(mAdapter.getData(), comparator);
							mAdapter.notifyDataSetChanged();
							
							totalRecords = Integer.parseInt(node.getTotal_count());
							L.e("totalRecords:", ""+totalRecords);
							L.e("mAdapter.getCount():", ""+mAdapter.getCount());
							L.e("mAdapter.getCount()<totalRecords：", ""+(mAdapter.getCount()<totalRecords));
							
							//显示总金额
							tv_total_money.setText(getTotalMoney(mAdapter.getData())
									+getResources().getString(R.string.yuan));
							
							
							//当前已加载的数据小于总数据条数时，显示底部更多，否则隐藏
							if(mAdapter.getCount()<totalRecords){
								mListView.setPullLoadEnable(true);
							}else{
								mListView.setPullLoadEnable(false);
							}
							
							onStopLoad();
							
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
	
	
	//自定义排序工具
//	private class MyComparator implements Comparator<DealNode>{
//
//		@Override
//		public int compare(DealNode node1, DealNode node2) {
//			if(node1.getDeal_time().compareTo(node2.getDeal_time())<0){
//				return 1;
//			}else if(node1.getDeal_time().compareTo(node2.getDeal_time())==0){
//				return 0;
//			}else{
//				return -1;
//			}
//		}
//		
//	}
	
	
	/**
	 * 获取操作明细总金额
	 * @param nodes
	 * @return
	 */
	private String getTotalMoney(ArrayList<DealNode> nodes){
		Double result = new Double("0");
		for(DealNode node:nodes){
			if(node.getMoney()!=null&&node.getMoney().length()>0){
//				if(node.getDeal_type().equals(DealNode.TYPE_D)){
					result = result + new Double(node.getMoney())+new Double(node.getChexiaoMoney());
//				}else if(node.getDeal_type().equals(DealNode.TYPE_C)){
//					result = result - new Double(node.getDeal_money());
//				}
			}
		}
		
		BigDecimal bg = new BigDecimal(result);
		L.e("BigDecimal", ""+bg);
		double d2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		L.e("d2:", ""+d2);
//		String mTotal = String.valueOf(d2);
//		L.e("mTotal:", ""+mTotal);
		DecimalFormat format = new DecimalFormat(".00");
			
//		L.e("getTotalMoney:", ""+format.format(mTotal));
		return format.format(d2);
	}
	private void initData(GetOperaterDetailNode rechargeNode){
		mDatas = new ArrayList<RechaAndRechaCancleNode>();
		RechaAndRechaCancleNode node = new RechaAndRechaCancleNode();
		node.setType(RechaAndRechaCancleNode.TYPE_RECHARGE);
		node.setmCardNum("9009320100000011");
		node.setmDate("2013-11-15 17:33");
		node.setmMoney("8.50");
		mDatas.add(node);
		
		RechaAndRechaCancleNode node2 = new RechaAndRechaCancleNode();
		node2.setType(RechaAndRechaCancleNode.TYPE_RECHARGE_CANCLE);
		node2.setmCardNum("9009320100000011");
		node2.setmDate("2013-11-15 17:33");
		node2.setmMoney("8.50");
		mDatas.add(node2);
		
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
			MepHuiZongActivity.this.finish();
		}
		
	}
	
	/**
	 * 数据适配器
	 * @author ShawnXiao
	 *
	 */
	private class MyDataAdapter extends BaseAdapter{
		private ArrayList<DealNode> mDatas;
		private Context mContext;
		private LayoutInflater mInflater;
		public MyDataAdapter(Context context,ArrayList<DealNode> datas){
			mDatas = datas;
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}
		
		public ArrayList<DealNode> getData(){
			return mDatas;
		}
		
		public void addData(DealNode node){
			if(node!=null){
				if(!mDatas.contains(node)){
					mDatas.add(node);
				}
			}
			
		}
		
		public void removeData(){
			if(mDatas!=null){
				mDatas.clear();
			}
		}
		
		public void addDatas(ArrayList<DealNode> list){
			if(list!=null&&list.size()>0){
				for(DealNode node:list){
					if(!mDatas.contains(node)){
						mDatas.add(node);
					}
				}
			}
		}

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			DealNode node = mDatas.get(position);
			ViewHolder holder = null;
//			if(convertView == null){
				convertView = mInflater.inflate(R.layout.mep_hui_zong_item, null);
				holder = new ViewHolder();
				holder.tv_mep_account_value = (TextView) convertView.findViewById(R.id.tv_mep_account_value);
				holder.tv_sum_of_money_value = (TextView) convertView.findViewById(R.id.tv_sum_of_money_value);
				holder.tv_sum_of_count_value = (TextView) convertView.findViewById(R.id.tv_sum_of_count_value);
				holder.tv_sum_of_chexiao_money_value = (TextView) convertView.findViewById(R.id.tv_sum_of_chexiao_money_value);
				holder.tv_sum_of_chexiao_count_value = (TextView) convertView.findViewById(R.id.tv_sum_of_chexiao_count_value);
				holder.tv_sum_of_real_money_value = (TextView) convertView.findViewById(R.id.tv_sum_of_real_money_value);
//				convertView.setTag(holder);
//			}else{
//				holder = (ViewHolder) convertView.getTag();
//			}
			
			if(node!=null){
				if(node.getMep_account()!=null
						&&node.getMep_account().length()>0){
					holder.tv_mep_account_value.setText(node.getMep_account());
				}
				if(node.getMoney()!=null
						&&node.getMoney().length()>0){
					holder.tv_sum_of_money_value.setText(node.getMoney()+mContext.getResources().getString(R.string.yuan));
				}
				if(node.getCount()!=null
						&&node.getCount().length()>0){
					holder.tv_sum_of_count_value.setText(node.getCount()+mContext.getResources().getString(R.string.bi));
				}
				if(node.getChexiaoMoney()!=null&&node.getChexiaoMoney().length()>0){
					holder.tv_sum_of_chexiao_money_value.setText(node.getChexiaoMoney()+mContext.getResources().getString(R.string.yuan));
				}
				if(node.getChexiaoCount()!=null&&node.getChexiaoCount().length()>0){
					holder.tv_sum_of_chexiao_count_value.setText(node.getChexiaoCount()+mContext.getResources().getString(R.string.bi));
				}
				if((node.getMoney()!=null&&node.getMoney().length()>0)||(
						node.getChexiaoMoney()!=null&&node.getChexiaoMoney().length()>0)){
					double d1 = Double.valueOf(node.getMoney());
					double d2 = Double.valueOf(node.getChexiaoMoney());
					BigDecimal bd1 = new BigDecimal(d1);
					BigDecimal bd2 = new BigDecimal(d2);
					double d3 = bd1.add(bd2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					DecimalFormat format = new DecimalFormat(".00");
					L.e("d1", ""+d1);
					L.e("d2:", ""+d2);
					L.e("bd1:", ""+bd1);
					L.e("bd2:", ""+bd2);
					L.e("d3:",""+ d3);
					holder.tv_sum_of_real_money_value.setText(format.format(d3)+mContext.getResources().getString(R.string.yuan));
				}
			}
			
			
			return convertView;
		}
		
		class ViewHolder{
			/**
			 * MEP账号
			 */
			TextView tv_mep_account_value;
			/**
			 * 总交易金额
			 */
			TextView tv_sum_of_money_value;
			/**
			 * 总交易笔数
			 */
			TextView tv_sum_of_count_value;
			/**
			 * 总撤销金额
			 */
			TextView tv_sum_of_chexiao_money_value;
			/**
			 * 总撤销金额
			 */
			TextView tv_sum_of_chexiao_count_value;
			/**
			 * 实际交易金额
			 */
			TextView tv_sum_of_real_money_value;
		}
		
	}
}
