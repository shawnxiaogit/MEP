package com.coco.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.coco.android.MyApplication;
import com.coco.android.R;


/**
 * 
 * @Descriptio ��������ҳ
 * @author Shawn
 * @Time 2013-9-29  ����11:09:40
 */
public class GuidActivity extends Activity implements OnPageChangeListener,OnClickListener{
	private ViewPager viewPager;
	private List<View> views;
	private GuideAdapter adapter;
	/**
	 * ��ť����
	 */
	private Button btn_tiaoguo ;
	/**
	 * ������ͼƬ��Դ
	 */
	private static final int[] pics = new int[]{
		R.drawable.fun_01,R.drawable.fun_02,
		R.drawable.fun_03,R.drawable.fun_04,
		R.drawable.fun_05,R.drawable.fun_06
	};
	
	/**
	 * �ײ�С��
	 */
	private ImageView[] dots;
	
	/**
	 * ��ס��ǰλ��
	 */
	private int current;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		//��ʼ��
		init();
		//Ϊ�ؼ����ü�����
		setUpViewListener();
	}
	//Ϊ�ؼ����ü�����
	private void setUpViewListener(){
		viewPager.setOnPageChangeListener(this);
		btn_tiaoguo.setOnClickListener(new TiaoGuoListener());
	}
	/**
	 * 
	 * @Descriptio ����������
	 * @author Shawn
	 * @Time 2013-9-29  ����4:37:15
	 */
	private class TiaoGuoListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			SharedPreferences sp = getSharedPreferences(MyApplication.SP_IS_READ_FUN_GUID, 
					Context.MODE_PRIVATE);
			SharedPreferences sp2 = getSharedPreferences(MyApplication.SP_JUM_FOM_CON, Context.MODE_PRIVATE);
			boolean is_read = sp.getBoolean(MyApplication.SP_KEY_READ_FUN_GUID, false);
			boolean is_jump_form_conf = sp2.getBoolean(MyApplication.SP_JUM_FOM_CON_KEY, false);
			//1���Ķ�������ת��½����
			if(is_read==true){
				//�ж��Ƿ��Ǵ�����ҳ������  
				//������ת����ҳ��
				if(is_jump_form_conf){
					finish();
				}else{
					//������ת��½ҳ��
					Intent intent = new Intent(GuidActivity.this,LoginActivity.class);
					GuidActivity.this.startActivity(intent);
					overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
					finish();
				}
			}else{
				//2��û���Ķ�����ת��ʼ������
				Intent intent = new Intent(GuidActivity.this,InitActivity.class);
				GuidActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				finish();
			}
			//3������Ϊ���Ķ���������ҳ
			sp.edit().putBoolean(MyApplication.SP_KEY_READ_FUN_GUID, true).commit();
			
		}
		
	}
	//��ʼ��
	private void init(){
		views = new ArrayList<View>();
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		for(int i = 0; i < pics.length ; i++){
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(params);
			iv.setImageResource(pics[i]);
			views.add(iv);
		}
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		adapter = new GuideAdapter(views);
		viewPager.setAdapter(adapter);
		btn_tiaoguo = (Button) findViewById(R.id.button_tiaoguo);
		setUpJiZhi();
	}
	//����Viewpager�Ļ���
	private void setUpJiZhi(){
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
		dots = new ImageView[pics.length];
		for(int i=0;i<pics.length;i++){
			dots[i] = (ImageView) ll.getChildAt(i);//����
			dots[i].setEnabled(true);//�����û�ɫ
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);
		}
		current = 0;
		dots[current].setEnabled(false);//����ѡ��״̬����ɫ
		
	}
	
	@Override
	public void onClick(View v) {
		//��ȡλ��
		int position = (Integer)v.getTag();
		//���õ�ǰ����Ϊ���ҳ��
		setViews(position);
		//���õ�Ϊѡ�е�
		setDots(position);
		
	}
	//���õ�ǰС��
	private void setDots(int position){
		if(position<0||position>pics.length-1||current==position){
			return ;
		}
		dots[position].setEnabled(false);
		dots[current].setEnabled(true);
		current = position;
	}
	//���õ�ǰ����ҳ
	private void setViews(int position){
		if(position<0||position>=pics.length){
			return;
		}
		viewPager.setCurrentItem(position);
	}
	//��ҳ�滬��״̬�ı����
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	//��ҳ�滬����ʱ�����
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if(arg0==pics.length-1){
			btn_tiaoguo.setEnabled(true);
			btn_tiaoguo.setVisibility(View.VISIBLE);
		}else{
			btn_tiaoguo.setEnabled(false);
			btn_tiaoguo.setVisibility(View.GONE);
		}
	}
	//��ҳ��ѡ�е�ʱ�����
	@Override
	public void onPageSelected(int arg0) {
		setDots(arg0);
	}
	
	
	/**
	 * 
	 * @Descriptio ViewPager��������
	 * @author Shawn
	 * @Time 2013-9-29  ����1:23:46
	 */
	private class GuideAdapter extends PagerAdapter{
		private List<View> mViews;
		public GuideAdapter(List<View> views){
			mViews = views;
		}

		
		//����positionλ��
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(mViews.get(position));
		}
		
		//��ʼ��positionλ�õĽ���
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(mViews.get(position),0);
			return mViews.get(position);
		}
		
		//��ȡ������
		@Override
		public int getCount() {
			if(mViews!=null){
				return mViews.size();
			}
			return 0;
		}

		//�ж��Ƿ��ɽ������ɶ���
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0==arg1);
		}
		
	}
	
}
