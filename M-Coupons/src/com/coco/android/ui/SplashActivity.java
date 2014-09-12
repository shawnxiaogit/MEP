package com.coco.android.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;

import com.coco.android.AppManager;
import com.coco.android.BaseActivity;
import com.coco.android.MyApplication;
import com.coco.android.R;
import com.coco.android.util.L;

import android.view.View;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.os.Handler;

/**
 * 
 * @Descriptio Ӧ�ó�����������
 * @author Shawn
 * @Time 2013-8-14 ����3:13:07
 */
public class SplashActivity extends BaseActivity {

	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View view = LayoutInflater.from(this).inflate(R.layout.splash, null);
		setContentView(view);
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		MyApplication.IEMI = getIemi();
		MyApplication.IMSI = getImsi();

		L.e("MyApplication.IEMI:", "" + MyApplication.IEMI);
		L.e("MyApplication.IMSI:", "" + MyApplication.IMSI);
		if (L.NO_PHONE) {
			// �ֻ���:13301797863������:111111
			MyApplication.IEMI = "A000004338B7F5";
			MyApplication.IMSI = "460031273888043";
		}
		// ��ȡ������ն˺��̻���
		SharedPreferences sp2 = getSharedPreferences(
				MyApplication.SP_TERM_COMM, Context.MODE_PRIVATE);

		MyApplication.TERM_NUM = sp2.getString(MyApplication.SP_TERM_NUM_KEY,
				MyApplication.DEFAULT_TERM_NUM);
		MyApplication.COMM_NUM = sp2.getString(MyApplication.SP_COMM_NUM_KEY,
				MyApplication.DEFAULT_COMM_NUM);

		
		//����ʹ��APP���ֻ���Ϣ
		StringBuilder sb = new StringBuilder();
		sb.append(MyApplication.IEMI);
		sb.append(MyApplication.IMSI);
		MyApplication.PHONE_INFO = sb.toString();
		L.e("MyApplication.PHONE_INFO:", MyApplication.PHONE_INFO);
		
		
//		 SharedPreferences sp =
//		 getSharedPreferences(MyApplication.SP_IS_SHORTCUT_EXIST,
//		 Activity.MODE_PRIVATE);
//		 boolean isShortCustExist =
//		 sp.getBoolean(MyApplication.KEY_SP_SHORTCUT, false);
//		 L.e("isShortCustExist:", ""+(isShortCustExist));
//		 // L.e("isShortCustExist2",""+(MyApplication.hasShortcut(SplashActivity.this)));
//		 if(!isShortCustExist){
//		 addShortCut();
//		 sp.edit().putBoolean(MyApplication.KEY_SP_SHORTCUT, true).commit();
//		 }else{
//			 showShortToast("��ݷ�ʽ�Ѿ�����");
//		 }

		Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
		view.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {

						if (L.GetSyntaCodeDebug) {
							openActivity(InitActivity.class);
							overridePendingTransition(R.anim.slide_left_in,
									R.anim.slide_left_out);
							finish();
						} else {
							SharedPreferences sp = getSharedPreferences(MyApplication.SP_IS_USE_APP_FIRST, 
									Context.MODE_PRIVATE);
							SharedPreferences sp2 = getSharedPreferences(MyApplication.SP_PHONE_INFO, 
									Context.MODE_PRIVATE);
							boolean is_first_use_app = sp.getBoolean(MyApplication.SP_IS_USE_APP_FIRST_KEY, true);
							
							String save_phone_info = sp2.getString(MyApplication.SP_PHONE_INFO_KEY, 
									MyApplication.DEFAULT_PHONE_INFO);
							//����û��״�ʹ�ã�������ʼ��
							//����û����ֻ���������ֻ��иı��ʱ������Ҫ��ת�������
							L.e("is_first_use_app:", ""+is_first_use_app);;
							L.e("!MyApplication.PHONE_INFO.equals(save_phone_info)",
									""+(!MyApplication.PHONE_INFO.equals(save_phone_info)));
							L.e("is_first_use_app||!MyApplication.PHONE_INFO.equals(save_phone_info)",
									""+(is_first_use_app||!MyApplication.PHONE_INFO.equals(save_phone_info)));
							L.e("MyApplication.PHONE_INFO:", ""+MyApplication.PHONE_INFO);;
							L.e("save_phone_info:", save_phone_info);
							if(is_first_use_app||!MyApplication.PHONE_INFO.equals(save_phone_info)){
								openActivity(InitActivity.class);
								overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
								finish();
//								AppManager.getAppManager().finishActivity(SplashActivity.class);
							}else{
								openActivity(LoginActivity.class);
								overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
//								finish();
//								AppManager.getAppManager().finishActivity(SplashActivity.class);
							}
						}

					}

				}, 500);
			}
		});

	}

	/**
	 * ��ӿ�ݷ�ʽ ע��AndroidManifest.xml�����Ȩ��:
	 * "com.android.permission.launcher.INSTALL_SHORTCUT"
	 * 
	 * @param activity
	 *            ����Activity
	 */
	public void addShortCut() {
		Intent shor_cut_intent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// ��ݷ�ʽ������
		shor_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));
		// �������ظ�����
		shor_cut_intent.putExtra("duplicate", false);
		// �ƶ���ǰ��ActivityΪ��ݷ�ʽ����������
		// ע�⣺ComponentName�ĵڶ�������������ϵ��(.)�������ݷ�ʽ�޷�������Ӧ����
		// ComponentName componentName = new ComponentName(getPackageName(),
		// "."+getLocalClassName());
		// shor_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new
		// Intent(Intent.ACTION_MAIN).setComponent(componentName));
		shor_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this,
				SplashActivity.class));
		// ��ݷ�ʽ��ͼ��
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				this, R.drawable.mep_app_icon);
		shor_cut_intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		sendBroadcast(shor_cut_intent);
	}

	TelephonyManager telephonyManager;

	private String getImsi() {
		String result = "";
		String imsi = telephonyManager.getSubscriberId();
		if (imsi != null && imsi.length() > 0) {
			if (imsi.length() > 15) {
				result = imsi.substring(0, 15);
			} else {
				result = imsi;
			}
		} else {
			result = MyApplication.DEFAULT_IMSI;
		}
		return result;
	}

	private String getIemi() {
		return telephonyManager.getDeviceId().substring(0, 14);
	}
}
