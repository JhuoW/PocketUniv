package com.pocketuniversity.activity;

import java.util.List;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.db.FriendsTable;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.NetAsyncTask;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.view.HeaderLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserDetailInApplyActivity extends BaseActivity {

	private ImageView avatar;
	private TextView username, usernumber, useraddress;
	private String userId;
	boolean isFriend = false;
	static ImageLoader imageLoader = ImageLoader.getInstance();
	private PUUser user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_user_detail_apply);
		userId = (String) getIntent().getExtras().get("userId");//查询到的好友Id
		System.out.println("userId--->" + userId);
		isFriend = checkUser(userId);
		initData();
	}
	
	
	private void initView(){
		avatar = (ImageView) findViewById(R.id.iv_infor_avatar);
		username = (TextView) findViewById(R.id.tv_infor_name);
		usernumber = (TextView) findViewById(R.id.tv_infor_number);
		useraddress = (TextView) findViewById(R.id.tv_infor_address);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("个人信息");
		headerLayout.showLeftBackButton("", new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserDetailInApplyActivity.this.finish();
			}
		});
		if (user == null) {
			return;
		}
	}
	
	
	
	private void initData(){
		
		param.clear();
		param.put("userID", userId);
		new NetAsyncTask(ctx, true) {
			@Override
			protected void onPost(Exception e) {
				if (user != null) {
					initView();
					username.setText(user.getName());
					usernumber.setText("学号：" + user.getUserId());
					useraddress.setText(user.getAddress());
					imageLoader
							.displayImage(
									user.getImage(),
									avatar,
									PhotoUtils
											.getImageOptions(R.drawable.icon_default_avatar));
				} else {
					return;
				}

			}

			@Override
			protected void doInBack() throws Exception {
				String jsonstr = new WebService(C.GETFRIENDINFOR, param)
						.getReturnInfo();
				user = GetObjectFromService.getPUUser(jsonstr);
			}
		}.execute();
	}
	
	
	
	private boolean checkUser(String id) {
		List<PUUser> friends = FriendsTable.getInstance().selectFriends();
		for (PUUser user : friends) {
			if (user.getID().equals(id)) {
				return true;
			}
		}
		return false;
	}


}
