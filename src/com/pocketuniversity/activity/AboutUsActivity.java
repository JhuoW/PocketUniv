package com.pocketuniversity.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.pocketuniversity.R;
import com.pocketuniversity.view.HeaderLayout;

public class AboutUsActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		initView();
	}
	
	private void initView(){
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("关于我们");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AboutUsActivity.this.finish();
			}
		});
	}
}
