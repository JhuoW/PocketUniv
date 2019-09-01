/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pocketuniversity.activity;

import numberpicker.NumberPicker;
import numberpicker.NumberPicker.OnValueChangeListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.view.HeaderLayout;

public class NewGroupActivity extends BaseActivity {
	private EditText groupNameEditText;
	private ProgressDialog progressDialog;
	private EditText introductionEditText;
	private CheckBox memberCheckbox;
	private TextView tv_max;

	private TextView et_maxNum;
	private String name;
	private String description;
	String maxNum;
	NumberPicker np;
	public boolean isPublic = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_group);
		initView();
		initAction();
	}

	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("创建群聊");
		groupNameEditText = (EditText) findViewById(R.id.edit_group_name);
		et_maxNum =  (TextView) findViewById(R.id.edit_max);
		tv_max = (TextView) findViewById(R.id.tv_max);
		introductionEditText = (EditText) findViewById(R.id.edit_group_introduction);
        np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMaxValue(300);
        np.setMinValue(2);
        np.setFocusable(true);
        np.setFocusableInTouchMode(true);
        np.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				int  num= np.getValue();
		        maxNum = num+"";
		        tv_max.setText(maxNum);
		        System.out.println("maxNum:"+maxNum);
			}
		});
        
	}

	private void initAction() {
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NewGroupActivity.this.finish();
			}
		});
		headerLayout.showRightTextButton("创建", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = groupNameEditText.getText().toString();
				description = introductionEditText.getText().toString();
				// String userId = Utils.getID();
				if(TextUtils.isEmpty(name)||TextUtils.isEmpty(description)){
					Utils.toast("群名或群简介不能为空");
					return;
				}
				
				startActivity(new Intent(NewGroupActivity.this,
						GroupPickContactsActivity.class).putExtra("name", name)
						.putExtra("description", description)
						.putExtra("maxNum", maxNum));
			}
		});
	}

}
