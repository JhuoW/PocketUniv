package com.pocketuniversity.more.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.Code;
import com.pocketuniversity.view.HeaderLayout;

public class ChangePasswordActivity extends BaseActivity implements OnClickListener  {

	private EditText password, password2, code;
	private ImageView codeimage;
	private Button btn_change;

	private String realCode;
	private String oldpassword;
	private String newpassword;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_change_password);
		
		initView();
		initData();
		initAction();
	}
	
	
	private void initAction() {
		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChangePasswordActivity.this.finish();
			}
		});
	}
	
	void initView(){
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		password = (EditText) findViewById(R.id.et_password);
		password2 = (EditText) findViewById(R.id.et_password2);
		code = (EditText) findViewById(R.id.et_code);
		codeimage = (ImageView) findViewById(R.id.iv_showCode);
		btn_change = (Button) findViewById(R.id.btn_change);
		btn_change.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_showCode:
			codeimage.setImageBitmap(Code.getInstance().createBitmap());
			realCode = Code.getInstance().getCode();
			break;
		case R.id.btn_change:
			oldpassword = password.getEditableText().toString();
			newpassword = password2.getEditableText().toString();
			if (!code.getEditableText().toString().equals(realCode)) {
				Utils.toast("验证码不正确");
				return;
			} else if (oldpassword.length() == 0) {
				Utils.toast("初始密码不能为空");
				return;
			} else if (newpassword.length() == 0) {
				Utils.toast("新密码不能为空");
				return;
			} else if (newpassword.length() < 5) {
				Utils.toast("密码至少为六位数");
				return;
			} else {
				new SimpleNetTask(ctx, true) {
					boolean flag;
					@Override
					protected void onSucceed() {
						if (flag) {
//							try {
//								PUUsr.getCurrentUser().updatePassword(oldpassword, newpassword);
//							} catch (AVException e) {
//								e.printStackTrace();
//							}
							Utils.toast("修改成功");
							ChangePasswordActivity.this.finish();
						} else {
							Utils.toast("修改失败");
						}
					}
					@Override
					protected void doInBack() throws Exception {
						Map<String, String> param = new HashMap<String, String>();
						param.put("userID",
								App.getInstance().getUserName());
						param.put("Password",
								oldpassword);
						param.put("newPassword",
								newpassword);
						String jsonstr = new WebService(C.MODIFYUSERPASSWORD,
								param).getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonstr);
					}
				}.execute();
			}
			break;
		}
	}
	
	
	private void initData() {
		codeimage.setImageBitmap(Code.getInstance().createBitmap());
		realCode = Code.getInstance().getCode();
		codeimage.setOnClickListener(this);
		btn_change.setOnClickListener(this);
		headerLayout.showTitle("修改密码");
	}

	
}
