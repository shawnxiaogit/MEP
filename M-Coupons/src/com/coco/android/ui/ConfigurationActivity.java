package com.coco.android.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coco.android.BaseActivity;
import com.coco.android.MyApplication;
import com.coco.android.R;
import com.coco.android.bean.ModifyPwdNode;
import com.coco.android.bean.RequestUtil;
import com.coco.android.socket.SocketClient;
import com.coco.android.util.L;


/**
 * 
 * @Descriptio ����ҳ��
 * 1)�����˵��Ĳ˵�������
 * 2)�����޸ĵ�½����
 * 3)�������½��빦������ҳ
 * 4)���Կ����̻��š��ն˺ŵ���Ϣ�еĻ�����ǩ���ɹ���
 * @author Shawn
 * @Time 2013-10-9  ����2:17:53
 */
public class ConfigurationActivity 	extends BaseActivity{
	private static final String TAG = "ConfigurationActivity";
	/**
	 * ��ť�޸�����
	 */
//	private Button btn_modi_pwd;
	/**
	 * �����ն˺š��̻���
	 */
	private LinearLayout ll_pos_info;
	/**
	 * �ı���ͼ �ն˺�
	 */
	private TextView tv_term_num ;
	/**
	 * �ı���ͼ �̻���
	 */
	private TextView tv_comm_num;
	
	/**
	 * ���������ذ�ť
	 */
	private Button btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.e(TAG, "onCreate()");
		setContentView(R.layout.activity_configuration);
		((TextView)findViewById(R.id.tv_title)).setText(R.string.confi_name);
		//��ʼ���ؼ�
		initView();
		//Ϊ�ؼ����ü�����
		setUpViewListener();
	}
	/**
	 * 
	 * @Description ��ʼ���ؼ�
	 * @author Shawn
	 * @Time 2013-10-9  ����3:00:31
	 */
	private void initView(){
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
//		btn_modi_pwd = (Button) findViewById(R.id.btn_modi_pwd);
//		btn_help = (Button) findViewById(R.id.btn_help);
		ll_pos_info = (LinearLayout) findViewById(R.id.ll_pos_info);
		tv_term_num = (TextView) findViewById(R.id.tv_term_num);
		tv_comm_num = (TextView) findViewById(R.id.tv_comm_num);
		if(MyApplication.TERM_NUM!=null&&MyApplication.TERM_NUM.length()>0){
			tv_term_num.setText(MyApplication.TERM_NUM);
		}
		if(MyApplication.COMM_NUM!=null&&MyApplication.COMM_NUM.length()>0){
			tv_comm_num.setText(MyApplication.COMM_NUM);
		}
	}
	/**
	 * 
	 * @Description Ϊ�ؼ����ü�����
	 * @author Shawn
	 * @Time 2013-10-9  ����3:01:40
	 */
	private void setUpViewListener(){
//		btn_modi_pwd.setOnClickListener(new BtnModiPwdListener());
//		btn_help.setOnClickListener(new BtnHelpListener());
		btn_back.setOnClickListener(new BackListener());
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
			defaultFinish();
		}
		
	}
	/**
	 * 
	 * @Descriptio �޸����������
	 * @author Shawn
	 * @Time 2013-10-9  ����3:02:12
	 */
	private class BtnModiPwdListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			L.d("BtnChangePwdListener", "onClick()");
			//�����Ի����޸�����
			showDialogModifyPwd(ConfigurationActivity.this);
		}
		
	}
	/**
	 * 
	 * @Description �����Ի����޸�����
	 * @author Shawn
	 * @Time 2013-10-9  ����3:52:28
	 */
	private void showDialogModifyPwd(Context context){
		L.e("showDialogModifyPwd", "showDialogModifyPwd()");
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_modify_pwd, null);
		/**
		 * �༭�������
		 */
		final EditText et_old_pwd = (EditText) view.findViewById(R.id.et_old_pwd);
		final EditText et_new_pwd = (EditText) view.findViewById(R.id.et_pwd_new);
		final EditText et_again_new_pwd = (EditText) view.findViewById(R.id.et_again_new_pwd);
		
		AlertDialog.Builder build = new Builder(context);
		build.setTitle(R.string.title_modify_pwd);
		build.setView(view);
		build.setPositiveButton(R.string.dia_btn_sure_txt, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String oldPwd = et_old_pwd.getText().toString();
				String newPwd = et_new_pwd.getText().toString();
				String newAgainPwd = et_again_new_pwd.getText().toString();
				//�����ж��Ƿ�Ϊ��
				if(!allPwdNotNull(oldPwd,newPwd,newAgainPwd)){
					if(newPwd.equals(newAgainPwd)){
						String val_11 = MyApplication.getLineNum(getApplicationContext());
						String value_60 = MyApplication.getSixtyValue2(MyApplication.PHONE_NUM,MyApplication.IMSI,MyApplication.IEMI);
						//��֯�޸����뱨��
						byte[] request = ModifyPwdNode.buildMsg(val_11, oldPwd, value_60, newPwd);
						mRunningTask = new ModifyPwdTask(request);
						mRunningTask.execute();
					}else{
						showShortToast(R.string.pwd_not_the_same);
					}
					
					//1���жϾ������Ƿ���ȷ
						//a)��ȡ����ľ�����
//					SharedPreferences sp = getSharedPreferences(MyApplication.SP_USER_NAM_PWD, Context.MODE_PRIVATE);	
//					String savedPwd = sp.getString(MyApplication.TMP_USER_NAME, MyApplication.PWD_DEFAULT);
//					if(oldPwd.equals(savedPwd)){
//						//2���ж������������Ƿ�һ��
//						if(newPwd.equals(newAgainPwd)){
//							//3����ok���޸�����(��sp����)
//							sp.edit().putString(MyApplication.TMP_USER_NAME, newPwd).commit();
//							dialog.dismiss();
//							showShortToast(R.string.modify_pwd_success);
//						}else{
//							showShortToast(R.string.pwd_not_the_same);
//						}
//						
//					}else{
//						showShortToast(R.string.old_pwd_incorr);
//						return;
//					}
				}
				
			}
		});
		build.setNegativeButton(R.string.dia_btn_canc_txt, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		build.create().show();
		
	}
	
	/**
	 * �޸���������
	 * @author ShawnXiao
	 *
	 */
	private class ModifyPwdTask extends AsyncTask{
		private byte[] mRequest;
		public ModifyPwdTask(byte[] request){
			mRequest = request;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(R.string.consume_processing);
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			SocketClient client = new SocketClient(mRequest,modifyPwdHandler);
			client.start();
		}
		
	}
	
	Handler modifyPwdHandler= new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case RequestUtil.REQUEST_SUCCESS:{
				String hex_response = (String) msg.obj;
				if(hex_response!=null&&hex_response.length()>0){
					ModifyPwdNode node = ModifyPwdNode.parseMsg(hex_response);
					String code = getResources().getStringArray(R.array.return_code)[0];
					L.e("code:", code);
					L.e("node.get(39):", ""+node.get(39));
					if(node.get(39).equals(code)){
						showShortToast(R.string.modify_login_pwd_success);
					}else{
						String err_msg = (String)node.get(63);
						if(err_msg!=null&&err_msg.length()>0){
							showShortToast(err_msg);
						}else{
							showShortToast(R.string.modify_login_pwd_faield);
						}
						
					}
				}else{
					showShortToast(R.string.modify_login_pwd_faield);
				}
				if(mAlertDialog!=null&&mAlertDialog.isShowing()){
					mAlertDialog.dismiss();
				}
			}break;
			case RequestUtil.REQUEST_FAIELD:{
				showShortToast(R.string.modify_login_pwd_faield);
				if(mAlertDialog!=null&&mAlertDialog.isShowing()){
					mAlertDialog.dismiss();
				}
			}break;
			}
			
		}
	};
	
	/**
	 * 
	 * @Description �ж��������붼��Ϊ��
	 * @author Shawn
	 * @Time 2013-10-9  ����4:14:58
	 * @param oldPwd ԭʼ����
	 * @param newPwd ������
	 * @param newAgainPwd �ٴ������������
	 * @return boolean
	 */
	private boolean allPwdNotNull(String oldPwd,String newPwd,String newAgainPwd){
		boolean result = true;
		if(TextUtils.isEmpty(oldPwd)){
			result = true;
			showShortToast(R.string.old_pwd_not_null);
		}else{
			if(TextUtils.isEmpty(newPwd)){
				result = true;
				showShortToast(R.string.new_pwd_not_null);
			}else{
				if(TextUtils.isEmpty(newAgainPwd)){
					result = true;
					showShortToast(R.string.new_pwd_sure_not_null);
				}else{
					result = false;
				}
			}
		}
		
		return result;
	}
	/**
	 * 
	 * @Descriptio ����������
	 * @author Shawn
	 * @Time 2013-10-9  ����3:03:00
	 */
	private class BtnHelpListener implements OnClickListener{

		@Override
		public void onClick(View v) {
//			showDialogModifyPwd(ConfigurationActivity.this);
			SharedPreferences sp = getSharedPreferences(MyApplication.SP_JUM_FOM_CON, Context.MODE_PRIVATE);
			sp.edit().putBoolean(MyApplication.SP_JUM_FOM_CON_KEY, true).commit();
			//���빦������ҳ
			openActivity(GuidActivity.class);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
		
	}
}
