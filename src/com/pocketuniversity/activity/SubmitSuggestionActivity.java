package com.pocketuniversity.activity;
import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.base.C;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class SubmitSuggestionActivity extends BaseActivity {
	private EditText et_problemsubmit;
	private TextView problemcontent_count;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submitsuggestion_layout);

		initView();
		initData();
		initAction();
		
	}
	
	
	private void initAction() {
		et_problemsubmit.addTextChangedListener(new TextWatcher() {
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
				problemcontent_count.setText(String.valueOf(number));
				selectionStart = et_problemsubmit.getSelectionStart();
				selectionEnd = problemcontent_count.getSelectionEnd();
				if (temp.length() > 140) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					et_problemsubmit.setText(s);
					et_problemsubmit.setSelection(tempSelection);
				}
			}
		});

		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SubmitSuggestionActivity.this.finish();
			}
		});
		headerLayout.showRightTextButton(
			 "提交",
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String suggestion = et_problemsubmit.getEditableText()
								.toString();
						if (suggestion.length() == 0) {
							Utils.toast("给点意见吧");
							return;
						} else {
							param.clear();
							param.put("userID", Utils.getID());
							param.put("content", suggestion);
						}
						new SimpleNetTask(ctx, true) {
							boolean flag;

							@Override
							protected void onSucceed() {
								if (flag) {
									Utils.toast("感谢您的宝贵意见");
									SubmitSuggestionActivity.this.finish();
								}else{
									et_problemsubmit.setText("");
								}
							}

							@Override
							protected void doInBack() throws Exception {
								String jsonstr = new WebService(C.PUBLISHSUGGESTION,
										param).getReturnInfo();
								flag = GetObjectFromService
										.getSimplyResult(jsonstr);
							}
						}.execute();
					}
				});
	}
	
	
	private void initData() {
		String problem = getIntent().getStringExtra("suggestionType");
		headerLayout.showTitle(problem);
	}
	
	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		et_problemsubmit = (EditText) findViewById(R.id.et_problemsubmit);
		problemcontent_count = (TextView) findViewById(R.id.problemcontent_count);
	}

}
