package com.coco.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.coco.android.BaseActivity;
import com.coco.android.R;
import com.coco.android.bean.Consume;
import com.coco.android.bean.ConsumeCancleNode;
import com.coco.android.bean.ConsumeCancleNode.Node;
import com.coco.android.util.L;

/**
 * 
 * @Descriptio ??????????????
 * @author Shawn
 * @Time 2013-8-28  ????4:08:56
 */
public class CheckInformationActivity extends BaseActivity {
	/**
	 * ?งา???? ????
	 */
	private ListView lv_consume;
	
	/**
	 * ??????????
	 */
	private MyConSumeAdapter mConSumeAdapter;
	
	
	/**
	 * ?????????????
	 */
	private Button btn_cancle;
	
	/**
	 * ????????????
	 */
	private Button btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.consume_cancle);
		((TextView)findViewById(R.id.tv_title)).setText(R.string.title_fun_5);
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new BackListener());
		
		
//		btn_cancle = (Button) findViewById(R.id.btn_sure);
//		btn_cancle.setText(R.string.btn_cancel);
//		btn_cancle.setVisibility(View.VISIBLE);
//		btn_cancle.setOnClickListener(new CancleConsumeListener());
		
		lv_consume = (ListView) findViewById(R.id.lv_consume);
		
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		ConsumeCancleNode consu_cancle_node = (ConsumeCancleNode) bundle.get("data");
		ArrayList<Node> nodes = consu_cancle_node.getNodes();
		
		
		mConSumeAdapter = new MyConSumeAdapter(this);
		mConSumeAdapter.setConsumes(nodes);
		
		lv_consume.setAdapter(mConSumeAdapter);
	}
	
	/**
	 * 
	 * @Descriptio ?????????????
	 * @author Shawn
	 * @Time 2013-8-23  ????3:50:43
	 */
	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			defaultFinish();
		}
		
	}
	
	
	
	/**
	 * 
	 * @Descriptio ?????????????
	 * @author Shawn
	 * @Time 2013-8-29  ????1:58:49
	 */
	private class CancleConsumeListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			L.i("CancleConsumeListener", "onClick");
		}
		
	}
	
	/**
	 * 
	 * @Descriptio ?????งา???????
	 * @author Shawn
	 * @Time 2013-8-28  ????4:29:34
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
		
		public void setConsumes(List<Node> consumes){
			mConsumes = consumes; 
		}
		
		public void removeConsume(Consume consume){
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
			Node node = mConsumes.get(position);
			ViewHolder viewHolder = null;
			if(convertView==null){
				convertView = mInflater.inflate(R.layout.consume_item, null);
				viewHolder = new ViewHolder();
				viewHolder.btn_cancle = (Button) convertView.findViewById(R.id.btn_cancle);
				viewHolder.tv_deal_date_value = (TextView) convertView.findViewById(R.id.tv_deal_date_value);
				viewHolder.tv_deal_time_value = (TextView) convertView.findViewById(R.id.tv_deal_time_value);
//				viewHolder.tv_card_num_value = (TextView) convertView.findViewById(R.id.tv_card_num_value);
				viewHolder.tv_sum_of_consume_value = (TextView) convertView.findViewById(R.id.tv_sum_of_consume_value);
				
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			if(node!=null){
				viewHolder.tv_deal_date_value.setText(node.getDeal_date());
				viewHolder.tv_deal_time_value.setText(node.getDeal_time());
//				viewHolder.tv_card_num_value.setText(node.getCard_num());
				viewHolder.tv_sum_of_consume_value.setText(node.getMoney());
				viewHolder.btn_cancle.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						//??????????????
						
					}
				});
			}
			
			return convertView;
		}
		
		
		class ViewHolder{
			/**
			 * ????????????
			 */
			Button btn_cancle;
			/**
			 * ???????????
			 */
			TextView tv_deal_date_value;
			/**
			 * ??????????
			 */
			TextView tv_deal_time_value;
			/**
			 * ??????????
			 */
//			TextView tv_card_num_value;
			/**
			 * ?????????
			 */
			TextView tv_sum_of_consume_value;
		}
	}
}
