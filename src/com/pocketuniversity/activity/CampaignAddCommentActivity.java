package com.pocketuniversity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.base.C;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;

public class CampaignAddCommentActivity extends BaseActivity {

	private EditText mContent;
	private TextView mCount;
	private String postId;
	private int cater;
	public static int NEWS = 0;
	private String method;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campaign_add_comment);
		postId = getIntent().getStringExtra("postId");
		cater = getIntent().getIntExtra("cater", 0);
		method = C.ADDCAMPAIGNCOMMENT;
		findView();
		setListener();
		initAction();
	}

	private void findView() {
		mContent = (EditText) findViewById(R.id.blogaddcomment_content);
		mCount = (TextView) findViewById(R.id.blogaddcomment_count);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
	}

	private void setListener() {
		mContent.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				int number = s.length();
				mCount.setText(String.valueOf(number));
				selectionStart = mContent.getSelectionStart();
				selectionEnd = mCount.getSelectionEnd();
				if (temp.length() > 140) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					mContent.setText(s);
					mContent.setSelection(tempSelection);
				}
			}
		});
	}

	private void initAction(){
		headerLayout.showTitle("添加评论");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CampaignAddCommentActivity.this.finish();
			}
		});
		headerLayout.showRightImageButton(
				R.drawable.action_button_out_pressed_light,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String commentcontent = mContent.getEditableText()
								.toString();
						if (commentcontent.length() == 0) {
							new SweetAlertDialog(CampaignAddCommentActivity.this,SweetAlertDialog.WARNING_TYPE)
							.setTitleText("评论不能为空")
							.setConfirmText("确定")
							.setCancelText("取消")
							.showCancelButton(true)
							.setCancelClickListener(new OnSweetClickListener() {
								
								@Override
								public void onClick(SweetAlertDialog sweetAlertDialog) {
									// TODO Auto-generated method stub
									sweetAlertDialog.dismiss();
								}
							}).setConfirmClickListener(new OnSweetClickListener() {
								
								@Override
								public void onClick(SweetAlertDialog sweetAlertDialog) {
									// TODO Auto-generated method stub
									sweetAlertDialog.dismiss();
								}
							}).show();
						} else {
							publishComment(commentcontent);
						}
					}

				});
	}
	
	public void publishComment(final String commentcontent){
		new SimpleNetTask(ctx, true) {
			boolean flag;
			@Override
			protected void onSucceed() {
				if(flag){
					Utils.toast("评论成功");
					Intent intent = new Intent();
		    		intent.putExtra("commentcontent", commentcontent);
		    		setResult(RESULT_OK, intent);
					CampaignAddCommentActivity.this.finish();
				}else{
					Utils.toast("评论失败");
				}
			}
			@Override
			protected void doInBack() throws Exception {
				param.clear();
				param.put("userID", Utils.getID());
				param.put("campaignID", postId);
				param.put("content", commentcontent);
				String jsonstr=new WebService(method, param).getReturnInfo();
				flag=GetObjectFromService.getSimplyResult(jsonstr);
			}
		}.execute();
		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(0, R.anim.roll_down);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
