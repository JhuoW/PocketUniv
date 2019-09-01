package com.pocketuniversity.more.activity;

import android.os.Bundle;

import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.base.App;

public class QuestionaireListActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
	}
}
