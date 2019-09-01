package com.pocketuniversity.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.base.C;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.MD5;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class RegistActivity extends BaseActivity {

	private EditText et_inputusername , et_inputpassword , et_inputAgainpassword;
	private CheckBox cb_male,cb_female ; 
	private Button btn_register;
	String username , password , repassword , sex ;
	private final String TAG = "RegistActivity" ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		findView();
	}
	
	void findView(){
		et_inputAgainpassword = (EditText) findViewById(R.id.et_input2password);
		et_inputpassword  = (EditText) findViewById(R.id.et_inputpassword);
		et_inputusername = (EditText) findViewById(R.id.et_inputusername);
		cb_male = (CheckBox) findViewById(R.id.cb_male);
		cb_female = (CheckBox) findViewById(R.id.cb_female);
		btn_register = (Button) findViewById(R.id.btn_register);
		headerLayout = (HeaderLayout) this.findViewById(R.id.headerLayout);
		headerLayout.showTitle("用户注册");
		headerLayout.showLeftBackButton("返回",new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				quitRegist();
			}
		});
		
		cb_male.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					cb_female.setChecked(false);
				}
			}
		});
		
		cb_female.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
					if(isChecked){
						cb_male.setChecked(false);
					}
			}
		});
		
	}
	
	private void quitRegist() {
		new SweetAlertDialog(ctx).setTitleText("退出注册")
				.setContentText("确定退出本次注册？").setConfirmText("确定")
				.setConfirmClickListener(new OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						sweetAlertDialog.dismiss();
						RegistActivity.this.finish();
					}
				}).show();
	}
	
	/**
	 * 注册
	 */
	private void register(){
		username = et_inputusername.getText().toString();
		password = et_inputpassword.getText().toString();
		repassword = et_inputAgainpassword.getText().toString();
		if(cb_male.isChecked()){
			sex = "1" ;
		}else {
			sex = "0" ; 
		}
		
		if (TextUtils.isEmpty(username)) {
			Utils.toast("用户名不能为空");
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Utils.toast("密码不能为空");
			return;
		}
		if (!repassword.equals(password)) {
			Utils.toast(R.string.password_not_consistent);
			return;
		}
		MyProgressDialog dialog = new MyProgressDialog(ctx);
		dialog.show();
		param.clear();
		param.put("nickname", username);
		param.put("password", MD5.getMD5(password.getBytes()));
		param.put("sex", sex);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String jsonStr  = new WebService(C.REGISTER, param).getReturnInfo();
				Log.i(TAG, "jsonStr ----- >" + jsonStr);
				
			}
		}).start();
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
		};
	};
	
}
