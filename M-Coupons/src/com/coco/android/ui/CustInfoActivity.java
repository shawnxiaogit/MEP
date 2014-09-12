package com.coco.android.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.coco.android.BaseActivity;
import com.coco.android.R;
import com.coco.android.bean.CustomerInfoListNode;
import com.coco.android.bean.QueryCustInfoNode;
import com.coco.android.util.L;

/**
 * �ͻ���Ϣ����
 * @author ShawnXiao
 *
 */
public class CustInfoActivity extends BaseActivity{
	
	/**
	 * �ı��ͻ����
	 */
	private TextView tv_cust_num_value;
	/**
	 * �ı��ͻ�����
	 */
	private TextView tv_cust_name_value;

	/**
	 * �ı����
	 */
	private TextView tv_cust_yuer_value;
	
	
	/**
	 * ȷ����ť
	 */
	private Button btn_cust_info_sure;
	
	public static final String DATA_KEY = "DATA_KEY";
	
	
	/**
	 * �ͻ��ֻ���
	 */
	private TextView tv_cust_phone_num_value;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cust_info);
		
		initView();
		
		setUpViewListener();
	}
	/**
	 * �����Ϣ
	 */
	private String mYuer;
	//��ʼ���ؼ�
	private void initView(){
		((TextView)findViewById(R.id.tv_title)).setText(R.string.cust_info_data_title);
		
		tv_cust_num_value = (TextView) findViewById(R.id.tv_cust_num_value);
		tv_cust_name_value = (TextView) findViewById(R.id.tv_cust_name_value);
		tv_cust_phone_num_value = (TextView) findViewById(R.id.tv_cust_phone_num_value);
		tv_cust_yuer_value = (TextView) findViewById(R.id.tv_cust_yuer_value);
		
		btn_cust_info_sure = (Button) findViewById(R.id.btn_cust_info_sure);
		
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		QueryCustInfoNode node = (QueryCustInfoNode) bundle.get(DATA_KEY);
		
		if(node.getmCustNum()!=null&&node.getmCustNum().length()>0){
			tv_cust_num_value.setText(node.getmCustNum());
		}
		if(node.getmCustName()!=null&&node.getmCustName().length()>0){
			tv_cust_name_value.setText(node.getmCustName());
		}
		if(node.getmCustPhoneNum()!=null&&node.getmCustPhoneNum().length()>0){
			tv_cust_phone_num_value.setText(node.getmCustPhoneNum());
		}
		L.e("node.getZhangmian_yuer():", ""+node.getZhangmian_yuer());
		if(node.getZhangmian_yuer()!=null&&node.getZhangmian_yuer().length()>0){
			tv_cust_yuer_value.setText(node.getZhangmian_yuer()+getResources().getString(R.string.yuan));
			mYuer = node.getZhangmian_yuer();
		}
		
		
		
	}
	
	private void setUpViewListener(){
		btn_cust_info_sure.setOnClickListener(new BtnOnClickListener());
	}
	
	/**
	 * ȷ�ϰ�ť������
	 * @author ShawnXiao
	 *
	 */
	private class BtnOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			L.e("BtnOnClickListener", "BtnOnClickListener");
			if(mYuer!=null&&mYuer.length()>0){
				L.e("mYuer:", ""+mYuer);
				//�������Ϣ�����ϸ�����
				Intent intent = getIntent();
				intent.putExtra("result", mYuer);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
		
	}
	/**
	 * ����
	 */
	public static final int KEY_YUER = 1113;
}	
