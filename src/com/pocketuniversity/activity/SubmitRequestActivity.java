package com.pocketuniversity.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class SubmitRequestActivity extends BaseActivity{
	private EditText requestcontent;
	private ImageView clearbutton;
	private String remark;
	private String fromUserId;
	private String toUserId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_submitrequest_layout);
		toUserId=getIntent().getExtras().getString("toUserId");
		fromUserId=Utils.getID();
		initView();
	}
	
	
	private void initView(){
		requestcontent = (EditText) findViewById(R.id.et_sendrequest);
		clearbutton = (ImageView) findViewById(R.id.iv_clear);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("请求验证");
		headerLayout.showLeftBackButton("返回", new OnClickListener() {
			@Override
			public void onClick(View v) {
				SubmitRequestActivity.this.finish();
			}
		});
		headerLayout.showRightTextButton("发送", new OnClickListener() {
			@Override
			public void onClick(View v) {
				param.clear();
				remark=requestcontent.getEditableText().toString();
				param.put("Remarks",remark );
				param.put("fromUserID", fromUserId);
				param.put("toUserID", toUserId);
				new SendRequestAsynTask().execute();
			}
		});
		clearbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestcontent.setText("");
			}
		});
	}
	
	
	class SendRequestAsynTask extends AsyncTask<Void, Void, Object> {
		MyProgressDialog dialog = new MyProgressDialog(ctx);

		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected Object doInBackground(Void... params) {
			String jsonstr = new WebService(C.REQUESTFRIEND, param).getReturnInfo();
			Boolean flag = GetObjectFromService.getSimplyResult(jsonstr);
			return flag;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			if((Boolean)result){
				Utils.toast("发送好友请求成功");
				SubmitRequestActivity.this.finish();
			}else{
				Utils.toast("您已发送申请，请等待对方同意！");
			}
		}
	}

}
