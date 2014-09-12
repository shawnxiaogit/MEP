package com.coco.android.ui;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.coco.android.BaseActivity;
import com.coco.android.MyApplication;
import com.coco.android.R;
import com.coco.android.bean.ConsumeCancleNode;
import com.coco.android.bean.ConsumeNode;
import com.coco.android.bean.DealNode;
import com.coco.android.bean.GetHuiZongNode;
import com.coco.android.bean.GetOperaterDetailNode;
import com.coco.android.bean.RequestUtil;
import com.coco.android.bean.ConsumeCancleNode.Node;
import com.coco.android.bean.ConsumeCancleNode2;
import com.coco.android.socket.SocketClient;
import com.coco.android.util.L;
import com.coco.android.xlist.XListView;
import com.coco.android.xlist.XListView.IXListViewListener;

/**
 * 
 * @Descriptio 消费撤销界面
 * @author Shawn
 * @Time 2013-8-28  下午4:08:56
 */
public class ConsumeCancleActivity extends BaseActivity {
//	/**
//	 * 列表视图 消费
//	 */
//	private ListView lv_consume;
	
	
	
	int pageNum = 0; // 当前页 从1开始
	int pageSize = 10; // 每页记录
	int totalPage = 0; // 记录总页数
	int totalRecords = 0; // 总记录条数
	
	private XListView mListView;
	
	
	/**
	 * 消费适配器
	 */
	private MyConSumeAdapter mConSumeAdapter;
	
	
	/**
	 * 标题栏撤销按钮
	 */
//	private Button btn_cancle;
	
	/**
	 * 标题栏返回按钮
	 */
	private Button btn_back;
	
	private int mRequestType;
	/**
	 * 卡号文本显示
	 */
	private TextView tv_card_num_value;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.consume_cancle);
		((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_4);
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new BackListener());
		
		
//		btn_cancle = (Button) findViewById(R.id.btn_sure);
//		btn_cancle.setText(R.string.btn_cancel);
//		btn_cancle.setVisibility(View.VISIBLE);
//		btn_cancle.setOnClickListener(new CancleConsumeListener());
		
		mListView = (XListView) findViewById(R.id.lv_consume);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		
		mListView.setXListViewListener(new MyXListViewListener());
		
		
		tv_card_num_value = (TextView) findViewById(R.id.tv_card_num_value);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		ConsumeCancleNode consu_cancle_node = (ConsumeCancleNode) bundle.get("data");
		ArrayList<Node> nodes = consu_cancle_node.getNodes();
		mRequestType = bundle.getInt(ConsumeDataCollectActivity2.REQUEST_TYPE);
		
		if(consu_cancle_node.getCard_num().length()==8){
			tv_card_num_value.setText(MyApplication.CARD_PREFIX+consu_cancle_node.getCard_num());
		}else{
			tv_card_num_value.setText(consu_cancle_node.getCard_num());
		}
		
		switch(mRequestType){
		case ConsumeDataCollectActivity2.CHE_XIAO:{
			((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_4);
		}break;
		case ConsumeDataCollectActivity2.CHA_XUN_MING_XI:{
			((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_2);
		}break;
		case ConsumeDataCollectActivity2.DUI_ZHANG_CHA_XUN:{
			((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_5);
		}
		}
//		if(nodes!=null&&nodes.size()>0){
//			for(int i=0;i<nodes.size();i++){
//				L.e("node["+i+"]", ""+nodes.get(i).toString());
//			}
//		}
		comparator= new MyComparator();
		mConSumeAdapter = new MyConSumeAdapter(this);
		if(nodes!=null&&nodes.size()>0){
			//对nodes进行排序
			
			Collections.sort(nodes, comparator);
			mConSumeAdapter.setConsumes(nodes);
		}
		mListView.setAdapter(mConSumeAdapter);
		
		
		//设置总的记录数量
		totalRecords = consu_cancle_node.getTotal_count();
		//设置当前页数为0
		pageNum = 0;
		
	}
	
	MyComparator comparator; 
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mConSumeAdapter.getCount()<totalRecords){
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
			pageNum = 0;
			responseHandler.postDelayed(new Runnable() {
				
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
			responseHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					L.e("mAdapter.getCount():", ""+(mConSumeAdapter.getCount()));
					L.e("totalRecords:", ""+totalRecords);;
					L.e("mAdapter.getCount()<totalRecords:", ""+(mConSumeAdapter.getCount()<totalRecords));
					if(mConSumeAdapter.getCount()<totalRecords){
						mListView.setPullLoadEnable(true);
						int requ_start = -1;
						if(pageNum == 0){
							requ_start = 0;
						}else{
							requ_start = (pageNum*pageSize);
						}
						L.e("requ_start", ""+requ_start);
						getData(requ_start,pageSize);
						pageNum++;
					}
					
				}
			}, 10);
		}
		
	}
	
	
	
	private void getData(int start,int count){
		
		
		byte[] request = ConsumeCancleNode.buildQueryList(MyApplication.CHE_XIAO_VALUE_60,
				MyApplication.CHE_XIAO_CARD_NUM,
				MyApplication.CHE_XIAO_START_TIME,
				MyApplication.CHE_XIAO_END_TIME,
				String.valueOf(start),
				String.valueOf(count));
		
		mRunningTask = new GetConsumeListTask(request);
		mRunningTask.execute();
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
//					node.setCard_num(et_card_num.getText().toString());
					if(node!=null){
						String code = getResources().getStringArray(
								R.array.return_code)[0];
						L.e("code:", code);
						if(node.getReturn_code()!=null){
							L.e("node.getReturn_code():", "" + node.getReturn_code());
							if(node.getReturn_code().equals(code)){
//								ArrayList<DealNode> nodes = node.getDealNodes();
//								Intent intent = new Intent(ConsumeDataCollectActivity2.this,ConsumeCancleActivity.class);
//								Bundle bundle = new Bundle();
//								bundle.putSerializable("data", node);
//								bundle.putInt(REQUEST_TYPE, mRequestType);
//								intent.putExtras(bundle);
//								ConsumeDataCollectActivity2.this.startActivity(intent);
								if(is_refresh == _REFRESH_DATA){
									is_refresh = _NOT_REFRESH_DATA;
									if(mConSumeAdapter!=null){
										mConSumeAdapter.removeData();
										if(node.getNodes()!=null&&node.getNodes().size()>0){
											Collections.sort(node.getNodes(),comparator);
											mConSumeAdapter.addDatas(node.getNodes());
											mConSumeAdapter.notifyDataSetChanged();
										}
									}
								}else{
									if(mConSumeAdapter!=null){
										if(node.getNodes()!=null&&node.getNodes().size()>0){
											Collections.sort(node.getNodes(),comparator);
											mConSumeAdapter.addDatas(node.getNodes());
											mConSumeAdapter.notifyDataSetChanged();
										}
									}
								}
								
								
								
								totalRecords = node.getTotal_count();
								L.e("totalRecords:", ""+totalRecords);
								L.e("mAdapter.getCount():", ""+mConSumeAdapter.getCount());
								L.e("mAdapter.getCount()<totalRecords：", ""+(mConSumeAdapter.getCount()<totalRecords));
								
								//显示总金额
//								tv_total_money.setText(getTotalMoney(mAdapter.getData())
//										+getResources().getString(R.string.yuan));
								
								
								//当前已加载的数据小于总数据条数时，显示底部更多，否则隐藏
								if(mConSumeAdapter.getCount()<totalRecords){
									mListView.setPullLoadEnable(true);
								}else{
									mListView.setPullLoadEnable(false);
									showShortToast(R.string.no_more_data);
								}
								
								onStopLoad();
								
								
							}else{
								showShortToast(R.string.get_data_faield);
							}
						}
					}
					else{
						showShortToast(R.string.get_data_faield);
					}
					if(mAlertDialog!=null&&mAlertDialog.isShowing()){
						mAlertDialog.dismiss();
					}
				}
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				showShortToast(R.string.get_data_faield);
				if(mAlertDialog!=null&&mAlertDialog.isShowing()){
					mAlertDialog.dismiss();
				}
			}break;
			}
			
		}
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
	
	
	//自定义排序
	private class MyComparator implements Comparator<Node>{

		@Override
		public int compare(Node node1, Node node2) {
			if(node1.getDeal_date().compareTo(node2.getDeal_date())<0
					){
				return 1;
			}else if(node1.getDeal_date().compareTo(node2.getDeal_date())==0){
				if(node1.getDeal_time().compareTo(node2.getDeal_time())<0){
					return 1;
				}else if(node1.getDeal_time().compareTo(node2.getDeal_time())==0){
					return 0;
				}else{
					return -1;
				}
			}else{
				return -1;
			}
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
			defaultFinish();
		}
		
	}
	
	
	
	/**
	 * 
	 * @Descriptio 消费列表适配器
	 * @author Shawn
	 * @Time 2013-8-28  下午4:29:34
	 */
	private class MyConSumeAdapter extends BaseAdapter{
		private List<Node> mConsumes;
		private Context mContext;
		private LayoutInflater mInflater;
		public MyConSumeAdapter(Context context){
			mContext = context;
			mConsumes = new ArrayList<Node>();
			mInflater = LayoutInflater.from(mContext);
		}
		
		
		public void addDatas(List<Node> list){
			
			if(list!=null&&list.size()>0){
				for(Node node:list){
					if(!mConsumes.contains(node)){
						mConsumes.add(node);
					}
				}
			}
		}
		
		public void removeData(){
			if(mConsumes!=null){
				mConsumes.clear();
			}
		}
		
		public void setConsumes(List<Node> consumes){
			mConsumes = consumes; 
		}
		
		public void removeConsume(Node consume){
			if(mConsumes!=null && mConsumes.size()>0){
				if(consume!=null){
					mConsumes.remove(consume);
				}
			}
		}
		
		
		@Override
		public int getCount() {
			return mConsumes.size();
		}

		@Override
		public Object getItem(int position) {
			return mConsumes.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Node node = mConsumes.get(position);
			ViewHolder viewHolder = null;
//			if(convertView==null){
				convertView = mInflater.inflate(R.layout.consume_item, null);
				viewHolder = new ViewHolder();
				viewHolder.btn_cancle = (Button) convertView.findViewById(R.id.btn_cancle);
				viewHolder.tv_deal_date_value = (TextView) convertView.findViewById(R.id.tv_deal_date_value);
				viewHolder.tv_deal_time_value = (TextView) convertView.findViewById(R.id.tv_deal_time_value);
//				viewHolder.tv_card_num_value = (TextView) convertView.findViewById(R.id.tv_card_num_value);
				viewHolder.tv_sum_of_consume_value = (TextView) convertView.findViewById(R.id.tv_sum_of_consume_value);
				viewHolder.tv_deal_cust_num_value = (TextView) convertView.findViewById(R.id.tv_deal_cust_num_value);
				viewHolder.tv_deal_cust_name_value = (TextView) convertView.findViewById(R.id.tv_deal_cust_name_value);
//				convertView.setTag(viewHolder);
//			}
//			else{
//				viewHolder = (ViewHolder) convertView.getTag();
//			}
			
			if(node!=null){
				if(mRequestType == ConsumeDataCollectActivity2.CHE_XIAO){
					viewHolder.btn_cancle.setVisibility(View.VISIBLE);
				}else if(mRequestType==ConsumeDataCollectActivity2.CHA_XUN_MING_XI){
					viewHolder.btn_cancle.setVisibility(View.GONE);
				}else if(mRequestType==ConsumeDataCollectActivity2.DUI_ZHANG_CHA_XUN){
					viewHolder.btn_cancle.setVisibility(View.GONE);
				}
				Date deal_date = null;
				Date deal_time = null;
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日");
				SimpleDateFormat format3 = new SimpleDateFormat("HHmmss");
				SimpleDateFormat format4 = new SimpleDateFormat("HH:mm:ss");
				try {
					if(node.getDeal_date()!=null&&node.getDeal_date().length()>0){
						deal_date=format.parse(node.getDeal_date());
					}
					if(node.getDeal_time()!=null&&node.getDeal_time().length()>0){
						deal_time = format3.parse(node.getDeal_time());
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(deal_date!=null){
					viewHolder.tv_deal_date_value.setText(format2.format(deal_date));
				}
				if(deal_time!=null){
					viewHolder.tv_deal_time_value.setText(format4.format(deal_time));
				}
//				viewHolder.tv_card_num_value.setText(node.getCard_num().subSequence(8, 16));
				if(node.getMoney()!=null&&node.getMoney().length()>0){
					viewHolder.tv_sum_of_consume_value.setText(node.getMoney()+mContext.getResources().getString(R.string.yuan));
				}
				viewHolder.tv_deal_cust_num_value.setText("");
				viewHolder.tv_deal_cust_name_value.setText("");
				if(node.getCust_num()!=null&&node.getCust_num().length()>0){
					viewHolder.tv_deal_cust_num_value.setText(node.getCust_num());
				}else{
					viewHolder.tv_deal_cust_num_value.setText("");
				}
				if(node.getCust_name()!=null&&node.getCust_name().length()>0){
					viewHolder.tv_deal_cust_name_value.setText(node.getCust_name());
				}else{
					viewHolder.tv_deal_cust_name_value.setText("");
				}
				viewHolder.btn_cancle.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						
						
						showAlertDialog(mContext.getResources().getString(R.string.warnming_prompt),
								mContext.getResources().getString(R.string.sure_to_cancle_the_consume), 
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										//组织消费撤销报文
										String val_52 = MyApplication.DEFAULT_CONSUME_KEY;
										String value_60 = MyApplication.getSixtyValue2(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
										String str_date = node.getDeal_date();
										L.e("str_date:", ""+str_date);
										String str_date2=(String) str_date.subSequence(4, 8);
										L.e("str_date2:", ""+str_date2);
										
										L.e("交易时间:", ""+node.getDeal_time());
										L.e("交易日期:", ""+str_date2);
										String val_serl_num = getSerialNum(node.getSerial_num());
										System.out.println("流水号:"+val_serl_num);
										
										byte[] request = ConsumeCancleNode2.buildConsumeCancleNode(node.getCard_num(), 
												node.getMoney(), val_serl_num, 
												node.getDeal_time(), str_date2, 
												val_52, value_60,node.getDeal_date());
										
										mRunningTask = new ConsumeCancleTask(node,request);
										mRunningTask.execute();
									}
								}, 
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								}, null);
						
						
					}
				});
			}
			
			return convertView;
		}
		
		
		class ViewHolder{
			/**
			 * 复选框取消消费
			 */
			Button btn_cancle;
			/**
			 * 文本消费日期
			 */
			TextView tv_deal_date_value;
			/**
			 * 文本消费时间
			 */
			TextView tv_deal_time_value;
//			/**
//			 * 文本消费卡号
//			 */
//			TextView tv_card_num_value;
			/**
			 * 文本客户编号
			 */
			TextView tv_deal_cust_num_value;
			/**
			 * 文本客户名称
			 */
			TextView tv_deal_cust_name_value;
			/**
			 * 文本消费金额
			 */
			TextView tv_sum_of_consume_value;
		}
	}
	
	
	/**
	 * 获取流水号
	 * @param val
	 * 流水号
	 * @return
	 */
	private String getSerialNum(String value){
		int len = value.length();
		StringBuilder sb = new StringBuilder();
		if(len == 0){
			sb.append("000000");
		}else if(len == 1){
			sb.append("00000");
		}else if(len == 2){
			sb.append("0000");
		}else if(len == 3){
			sb.append("000");
		}else if(len == 4){
			sb.append("00");
		}else if(len == 5){
			sb.append("0");
		}
		sb.append(value);
		
		return sb.toString();
	}
	
	/**
	 * 消费撤销任务
	 * @author ShawnXiao
	 *
	 */
	private class ConsumeCancleTask extends AsyncTask{
		private Node mNode;
		private byte[] mRequest;
		public ConsumeCancleTask(Node node,byte[] request){
			mNode = node;
			mRequest = request;
		};

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.consume_processing);
		}
		
		@Override
		protected Object doInBackground(Object... arg0) {
			return null;
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest, new ConsumeCancleHandler(mNode));
			client.start();
		}
		
	}
	
	/**
	 * 消费撤销处理类
	 * @author ShawnXiao
	 *
	 */
	private class ConsumeCancleHandler extends Handler{
		private Node mNode;
		public ConsumeCancleHandler(Node node){
			mNode = node;
		}
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case RequestUtil.REQUEST_SUCCESS:{
				String hex_response = (String) msg.obj;
				ConsumeCancleNode2 item = ConsumeCancleNode2.parseMsg(hex_response);
				String code = getResources().getStringArray(R.array.return_code)[0];
				L.e("code:", code);
				L.e("node.get(39):", ""+item.get(39));
				if(item.get(39).equals(code)){
					showShortToast(R.string.cancle_success);
					mConSumeAdapter.removeConsume(mNode);
					mConSumeAdapter.notifyDataSetChanged();
				}else{
					showShortToast(R.string.cancle_faield);
				}
				mAlertDialog.dismiss();
				
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				showShortToast(R.string.consume_cancle_faield);
				mAlertDialog.dismiss();
			}break;
			}
		}
	}
}
