package com.pocketuniversity.activity;

import android.os.Bundle;

import com.pocketuniversity.service.LoginFinishReceiver;

public class BaseEntryActivity extends BaseActivity {

	LoginFinishReceiver  loginFinishReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		loginFinishReceiver = LoginFinishReceiver.register(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(loginFinishReceiver);
		
	}
}
