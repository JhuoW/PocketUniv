package com.pocketuniversity.friendcircle.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.activity.CampaignJoinerActivity;
import com.pocketuniversity.activity.UserDetailActivity;
import com.pocketuniversity.adapter.CampaignJoinerAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.CampaignJoiner;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class ZanUserActivity extends BaseActivity {
	private ListView zanListView;
	private MyProgressDialog dialog;
	private List<CampaignJoiner> zanUser = new ArrayList<CampaignJoiner>();
	CampaignJoinerAdapter adapter ; 
	String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campaign_joiner);
		Intent intent = getIntent();
		id = intent.getStringExtra("post");
		initView();
		getData();
		initAction();
	}
	
	private void initView(){
		dialog = new MyProgressDialog(ctx);
		dialog.show();
		zanListView = (ListView) findViewById(R.id.join_listview);
		adapter = new CampaignJoinerAdapter(ctx, zanUser, R.layout.item_campaign_joiner);
		zanListView.setAdapter(adapter);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("µ„‘ﬁ”√ªß");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ZanUserActivity.this.finish();
			}
		});
	}
	
	private void getData(){
		new SimpleNetTask(ctx ,false) {
			List<CampaignJoiner> temp=new ArrayList<CampaignJoiner>();

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if(temp==null){
					Utils.toast("net wrong");
					return;
				}
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				zanUser.clear();
				zanUser.addAll(temp);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("id", id);
				String jsonStr = new WebService(C.GETALLPRAISE, param).getReturnInfo();
				temp = GetObjectFromService.getZanUser(jsonStr);
			}
		}.execute();
	}
	private void initAction(){
		zanListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ZanUserActivity.this,
						UserDetailActivity.class);
				intent.putExtra("userId", zanUser.get(position).getUserId());
				startActivity(intent);
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}
}
