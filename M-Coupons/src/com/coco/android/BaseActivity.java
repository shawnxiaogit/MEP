package com.coco.android;

import java.lang.reflect.Field;


import com.coco.android.util.L;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @Descriptio Activity���࣬��װһЩ�����Ĳ���
 * @author Shawn
 * @Time 2013-8-14  ����1:22:58
 */
public class BaseActivity extends Activity {
	
	private static final String TAG = "BaseActivity";
	protected AlertDialog mAlertDialog;
	protected AsyncTask mRunningTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		L.d(TAG, this.getClass().getSimpleName()+"onCreated() invoked!!");
		
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
	}
	/**
	 * @Description ���ñ���
	 * @author Shawn
	 * @Time 2013-8-23  ����2:22:59
	 * @param title �����ַ���
	 */
	public void setTitle(String title){
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.selft_title_bar);
		TextView  tv_title = (TextView)findViewById(R.id.tv_title);
		tv_title.setText(title);
	}
	/**
	 * @Description ���ñ���
	 * @author Shawn
	 * @Time 2013-8-23  ����2:22:59
	 * @param res ������Դ
	 */
	public void setTitle(int res){
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.selft_title_bar);
		TextView  tv_title = (TextView)findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(res));
	}
	
	@Override
	protected void onStart() {
		L.d(TAG, this.getClass().getSimpleName()+"onStart() invoked!!");
		super.onStart();
	}
	
	@Override
	protected void onRestart() {
		L.d(TAG, this.getClass().getSimpleName()+"onRestart() invoked!!");
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		L.d(TAG, this.getClass().getSimpleName()+"onResume() invoked!!");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		L.d(TAG, this.getClass().getSimpleName()+"onPause() invoked!!");
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		L.d(TAG, this.getClass().getSimpleName()+"onStop() invoked!!");
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		L.d(TAG, this.getClass().getSimpleName()+"onDestroy() invoked!!");
		super.onDestroy();
		
		if(mRunningTask != null && mRunningTask.isCancelled() == false){
			mRunningTask.cancel(false);
			mRunningTask = null;
		}
		
		if(mAlertDialog != null){
			mAlertDialog.dismiss();
			mAlertDialog = null;
		}
		AppManager.getAppManager().finishActivity(this);
	}
	
	
	

	protected void showShortToast(int pResId) {
		showShortToast(getString(pResId));
	}

	protected void showLongToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
	}
	protected void showLongToast(int pResId) {
		showLongToast(getString(pResId));
	}

	protected void showShortToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
	}

	protected boolean hasExtra(String pExtraKey) {
		if (getIntent() != null) {
			return getIntent().hasExtra(pExtraKey);
		}
		return false;
	}

	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}
	
	
	
	/**
	 * ͨ�����������öԻ����Ƿ�Ҫ�رգ��ڱ�У��ʱ�ܹ��ã� ��Ϊ���û���д����ʱ��ȷ��ʱĬ��Dialog����ʧ�� ���Դﲻ��У���Ч��
	 * ��mShowing�ֶξ������������Ƿ�Ҫ��ʧ�ģ�������Dialog����˽�б����� ����ֻ��ͨ������ȥ���������
	 * 
	 * @param pDialog
	 * @param pIsClose
	 */
	public void setAlertDialogIsClose(DialogInterface pDialog, Boolean pIsClose) {
		try {
			Field field = pDialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(pDialog, pIsClose);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected AlertDialog showAlertDialog(String TitleID, String Message) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(TitleID)
				.setMessage(Message).show();
		return mAlertDialog;
	}

	protected AlertDialog showAlertDialog(int pTitelResID, String pMessage,
			DialogInterface.OnClickListener pOkClickListener) {
		String title = getResources().getString(pTitelResID);
		return showAlertDialog(title, pMessage, pOkClickListener, null, null);
	}

	protected AlertDialog showAlertDialog(String pTitle, String pMessage,
			DialogInterface.OnClickListener pOkClickListener,
			DialogInterface.OnClickListener pCancelClickListener,
			DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this)
				.setTitle(pTitle)
				.setMessage(pMessage)
				.setPositiveButton(android.R.string.ok, pOkClickListener)
				.setNegativeButton(android.R.string.cancel,
						pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	protected AlertDialog showAlertDialog(String pTitle, String pMessage,
			String pPositiveButtonLabel, String pNegativeButtonLabel,
			DialogInterface.OnClickListener pOkClickListener,
			DialogInterface.OnClickListener pCancelClickListener,
			DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(pTitle)
				.setMessage(pMessage)
				.setPositiveButton(pPositiveButtonLabel, pOkClickListener)
				.setNegativeButton(pNegativeButtonLabel, pCancelClickListener)
				.show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	protected ProgressDialog showProgressDialog(int pTitelResID,
			String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		String title = getResources().getString(pTitelResID);
		return showProgressDialog(title, pMessage, pCancelClickListener);
	}
	
	
	protected ProgressDialog showProgressDialog(String pTitle, String pMessage) {
		return showProgressDialog(pTitle,pMessage,null);
	}
	protected ProgressDialog showProgressDialog(String pMessage) {
		return showProgressDialog("",pMessage,null);
	}
	protected ProgressDialog showProgressDialog(int pMsgResId) {
		return showProgressDialog("",getString(pMsgResId),null);
	}

	protected ProgressDialog showProgressDialog(String pTitle, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		mAlertDialog = ProgressDialog.show(this, pTitle, pMessage, true, true);
		mAlertDialog.setOnCancelListener(pCancelClickListener);
		return (ProgressDialog) mAlertDialog;
	}

	protected void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	protected void handleOutmemoryError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "�ڴ�ռ䲻�㣡", Toast.LENGTH_SHORT)
						.show();
				// finish();
			}
		});
	}

	private int network_err_count = 0;

	protected void handleNetworkError() {
		network_err_count++;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (network_err_count < 3) {
					Toast.makeText(BaseActivity.this, "���ٺ�����ô��������",
							Toast.LENGTH_SHORT).show();
				} else if (network_err_count < 5) {
					Toast.makeText(BaseActivity.this, "������Ĳ�������",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(BaseActivity.this, "���������������ô��ô���",
							Toast.LENGTH_SHORT).show();
				}
				// finish();
			}
		});
	}

	protected void handleMalformError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "���ݸ�ʽ����", Toast.LENGTH_SHORT)
						.show();
				// finish();
			}
		});
	}

	protected void handleFatalError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "������һ�����⣬������ֹ��",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
	
	public void finish()
	{
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}
	
	public void defaultFinish()
	{
		finish();
	}

}
