package com.pocketuniversity.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.adapter.CampaignJoinerAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.CampaignJoiner;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class CampaignJoinerActivity extends BaseActivity{

	private ListView joinerListView;
	private MyProgressDialog dialog;
	private List<CampaignJoiner> joiner = new ArrayList<CampaignJoiner>();
	CampaignJoinerAdapter adapter ; 
	String campaignId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campaign_joiner);
		Intent intent = getIntent();
		campaignId = intent.getStringExtra("post");
		initView();
		getData();
		initAction();
	}
	
	private void initView(){
		dialog = new MyProgressDialog(ctx);
		dialog.show();
		joinerListView = (ListView) findViewById(R.id.join_listview);
		adapter = new CampaignJoinerAdapter(ctx, joiner, R.layout.item_campaign_joiner);
		joinerListView.setAdapter(adapter);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("≤Œ”Î’ﬂ");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CampaignJoinerActivity.this.finish();
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
				joiner.clear();
				joiner.addAll(temp);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("campaignId", campaignId);
				String jsonStr = new WebService(C.GETCAMPAIGNJOINER, param)
						.getReturnInfo();
				temp = GetObjectFromService.getCampaignJoiner(jsonStr);
			}
		}.execute();
	}
	
	private void initAction(){
		joinerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CampaignJoinerActivity.this,
						UserDetailActivity.class);
				intent.putExtra("userId", joiner.get(position).getUserId());
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
