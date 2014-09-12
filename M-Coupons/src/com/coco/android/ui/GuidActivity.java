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
 * @Descriptio 功能引导页
 * @author Shawn
 * @Time 2013-9-29  上午11:09:40
 */
public class GuidActivity extends Activity implements OnPageChangeListener,OnClickListener{
	private ViewPager viewPager;
	private List<View> views;
	private GuideAdapter adapter;
	/**
	 * 按钮跳过
	 */
	private Button btn_tiaoguo ;
	/**
	 * 引导屏图片资源
	 */
	private static final int[] pics = new int[]{
		R.drawable.fun_01,R.drawable.fun_02,
		R.drawable.fun_03,R.drawable.fun_04,
		R.drawable.fun_05,R.drawable.fun_06
	};
	
	/**
	 * 底部小点
	 */
	private ImageView[] dots;
	
	/**
	 * 记住当前位置
	 */
	private int current;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		//初始化
		init();
		//为控件设置监听器
		setUpViewListener();
	}
	//为控件设置监听器
	private void setUpViewListener(){
		viewPager.setOnPageChangeListener(this);
		btn_tiaoguo.setOnClickListener(new TiaoGuoListener());
	}
	/**
	 * 
	 * @Descriptio 跳过监听器
	 * @author Shawn
	 * @Time 2013-9-29  下午4:37:15
	 */
	private class TiaoGuoListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			SharedPreferences sp = getSharedPreferences(MyApplication.SP_IS_READ_FUN_GUID, 
					Context.MODE_PRIVATE);
			SharedPreferences sp2 = getSharedPreferences(MyApplication.SP_JUM_FOM_CON, Context.MODE_PRIVATE);
			boolean is_read = sp.getBoolean(MyApplication.SP_KEY_READ_FUN_GUID, false);
			boolean is_jump_form_conf = sp2.getBoolean(MyApplication.SP_JUM_FOM_CON_KEY, false);
			//1、阅读过则跳转登陆界面
			if(is_read==true){
				//判断是否是从配置页面进入的  
				//是则跳转配置页面
				if(is_jump_form_conf){
					finish();
				}else{
					//否则跳转登陆页面
					Intent intent = new Intent(GuidActivity.this,LoginActivity.class);
					GuidActivity.this.startActivity(intent);
					overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
					finish();
				}
			}else{
				//2、没有阅读则跳转初始化界面
				Intent intent = new Intent(GuidActivity.this,InitActivity.class);
				GuidActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				finish();
			}
			//3、保存为已阅读功能引导页
			sp.edit().putBoolean(MyApplication.SP_KEY_READ_FUN_GUID, true).commit();
			
		}
		
	}
	//初始化
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
	//建立Viewpager的机制
	private void setUpJiZhi(){
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
		dots = new ImageView[pics.length];
		for(int i=0;i<pics.length;i++){
			dots[i] = (ImageView) ll.getChildAt(i);//索引
			dots[i].setEnabled(true);//都设置灰色
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);
		}
		current = 0;
		dots[current].setEnabled(false);//设置选中状态，白色
		
	}
	
	@Override
	public void onClick(View v) {
		//获取位置
		int position = (Integer)v.getTag();
		//设置当前界面为点击页面
		setViews(position);
		//设置点为选中点
		setDots(position);
		
	}
	//设置当前小点
	private void setDots(int position){
		if(position<0||position>pics.length-1||current==position){
			return ;
		}
		dots[position].setEnabled(false);
		dots[current].setEnabled(true);
		current = position;
	}
	//设置当前引导页
	private void setViews(int position){
		if(position<0||position>=pics.length){
			return;
		}
		viewPager.setCurrentItem(position);
	}
	//当页面滑动状态改变调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	//当页面滑动的时候调用
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
	//当页面选中的时候调用
	@Override
	public void onPageSelected(int arg0) {
		setDots(arg0);
	}
	
	
	/**
	 * 
	 * @Descriptio ViewPager的适配器
	 * @author Shawn
	 * @Time 2013-9-29  下午1:23:46
	 */
	private class GuideAdapter extends PagerAdapter{
		private List<View> mViews;
		public GuideAdapter(List<View> views){
			mViews = views;
		}

		
		//销毁position位置
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(mViews.get(position));
		}
		
		//初始化position位置的界面
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(mViews.get(position),0);
			return mViews.get(position);
		}
		
		//获取界面数
		@Override
		public int getCount() {
			if(mViews!=null){
				return mViews.size();
			}
			return 0;
		}

		//判断是否由界面生成对象
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0==arg1);
		}
		
	}
	
}
