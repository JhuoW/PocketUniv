package com.pocketuniversity.more.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dtr.zxing.activity.CaptureActivity;
import com.easemob.chat.EMChatManager;
import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.base.App;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.view.HeaderLayout;

public class MyDataActivity extends BaseActivity{

	private RelativeLayout rl_myinfo , rl_setting , rl_QR, rl_collect , rl_myQR , rl_myCampaign;
	
	public TextView tv_name ;
	public TextView tv_id ;
	
	private PUUser curUser ; 
	
	private ImageView iv_avatar;

	public static ImageLoader imageLoader = ImageLoader.getInstance();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_mydata);
		curUser = new PreferenceMap(this).getUser();
		initView();
		initAction();
		
	}
	
	private void initView(){
		rl_myinfo = (RelativeLayout) findViewById(R.id.re_myinfo);
		rl_setting = (RelativeLayout) findViewById(R.id.re_setting);
		rl_QR = (RelativeLayout) findViewById(R.id.re_QR);
		rl_myQR = (RelativeLayout) findViewById(R.id.re_myQR);
		rl_collect = (RelativeLayout) findViewById(R.id.re_collect);
		rl_myCampaign = (RelativeLayout) findViewById(R.id.re_myCampaign);
		tv_id = (TextView) findViewById(R.id.tv_id);
		tv_name = (TextView) findViewById(R.id.tv_name);
		iv_avatar =  (ImageView) findViewById(R.id.iv_avatar);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("个人主页");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyDataActivity.this.finish();
			}
		});
	}
	
	private void initAction(){
		tv_id.setText(EMChatManager.getInstance().getCurrentUser());
		tv_name.setText(curUser.getName());
		String url = curUser.getImage();
		
		imageLoader
		.displayImage(
				url,
				iv_avatar,
				PhotoUtils
						.getImageOptions(R.drawable.icon_default_avatar_selector));

		rl_myinfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyDataActivity.this, ChangeInformationActivity.class);
				startActivity(intent);
			}
		});
		
		rl_QR.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent  = new Intent(getApplicationContext(), CaptureActivity.class);
				startActivity(intent);
			}
		});
		
		rl_setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), SettingActivity.class));
			}
		});
		
		rl_myQR.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), MyQRActivity.class));
			}
		});
		rl_myCampaign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), MyCampaignActivity.class));
			}
		});
		
		rl_collect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), MyCollectionActivity.class));

			}
		});
		
	}
	
	/**
	 * 获取当前用户的信息
	 */
	private void initData(){
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		PUUser user=new PreferenceMap(App.ctx).getUser();
		tv_name.setText(user.getName());
		String url=user.getImage();
		imageLoader
				.displayImage(
						url,
						iv_avatar,
						PhotoUtils
								.getImageOptions(R.drawable.icon_default_avatar_selector));
		
	}
	
	
}
