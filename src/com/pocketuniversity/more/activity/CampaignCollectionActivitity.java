package com.pocketuniversity.more.activity;

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
import com.pocketuniversity.activity.CampaignPostDetailActivity;
import com.pocketuniversity.adapter.MyCampaignCenterListAdp;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.bean.MyCampaignPostModel;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class CampaignCollectionActivitity extends BaseActivity {


	private ListView myCompaign_listview;
	private MyCampaignCenterListAdp adapter ;
	private List<CampaignPostModel> campaignList = new ArrayList<CampaignPostModel>();

	private MyProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_my_campaign);
		dialog = new MyProgressDialog(this);
		param.clear();
		param.put("userID", Utils.getID());
		findView();
		getData();
		initAction();
	}
	
	private void findView(){
		myCompaign_listview = (ListView) findViewById(R.id.campaign_listview);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("ªÓ∂Ø ’≤ÿ");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CampaignCollectionActivitity.this.finish();
			}
		});
	}
	
	private void getData(){
		new SimpleNetTask(CampaignCollectionActivitity.this) {
			
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub

				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				adapter=new MyCampaignCenterListAdp(CampaignCollectionActivitity.this,campaignList ,R.layout.lay_post_lv_campaign_item);				
				myCompaign_listview.setAdapter(adapter);
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				String jsonStr = new WebService(C.GETMYCAMPAIGNCOLLECTIONLIST, param).getReturnInfo().toString();

				campaignList = GetObjectFromService.getMyCampaign(jsonStr);
			}
		}.execute();
	}
	
	private void initAction(){
		myCompaign_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					 Intent intent = new Intent(CampaignCollectionActivitity.this,CampaignPostDetailActivity.class);
					intent.putExtra("post", campaignList.get(position));
				//	intent.putExtra("isJoin", newsList.get(position).isJoin());
					 startActivity(intent);
					 CampaignCollectionActivitity.this.overridePendingTransition(R.anim.slide_in_right,
					 R.anim.slide_out_left);
			}
		});
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}

}
