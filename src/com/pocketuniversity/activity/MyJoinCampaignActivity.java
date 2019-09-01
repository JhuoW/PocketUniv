package com.pocketuniversity.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.adapter.MyCampaignCenterListAdp;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.NetAsyncTask;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MyJoinCampaignActivity extends BaseActivity {

	private ListView myCompaign_listview;
	private MyCampaignCenterListAdp adapter;
	private List<CampaignPostModel> campaignList = new ArrayList<CampaignPostModel>();
	private MyProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_campaign);
		initView();
		initAction();
		getData();
	}

	private void initView() {
		dialog = new MyProgressDialog(ctx);
		myCompaign_listview = (ListView) findViewById(R.id.campaign_listview);
		adapter = new MyCampaignCenterListAdp(MyJoinCampaignActivity.this,
				campaignList, R.layout.lay_post_lv_campaign_item);
		myCompaign_listview.setAdapter(adapter);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("我的参与");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyJoinCampaignActivity.this.finish();
			}
		});

	}
	
	private void initAction(){
		myCompaign_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					 Intent intent = new Intent(MyJoinCampaignActivity.this,CampaignPostDetailActivity.class);
					 intent.putExtra("post", campaignList.get(position));
				//	intent.putExtra("isJoin", newsList.get(position).isJoin());
					 startActivity(intent);
					 MyJoinCampaignActivity.this.overridePendingTransition(R.anim.slide_in_right,
					 R.anim.slide_out_left);
			}
		});
		
		myCompaign_listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				new SweetAlertDialog(MyJoinCampaignActivity.this, SweetAlertDialog.WARNING_TYPE)
				.setTitleText("是否取消参加？").showCancelButton(true).setConfirmText("确定")
				.setCancelText("取消")
				.setConfirmClickListener(new OnSweetClickListener() {
					@Override
					public void onClick(final SweetAlertDialog sweetAlertDialog) {
							new NetAsyncTask(MyJoinCampaignActivity.this,false) {
								boolean result;
									@Override
									protected void onPost(Exception e) {
										if(result){
											sweetAlertDialog.dismiss();
											campaignList.remove(position);
											adapter.notifyDataSetChanged();
												}else {
													sweetAlertDialog.dismiss();
													Toast.makeText(MyJoinCampaignActivity.this, "取消参与失败", Toast.LENGTH_SHORT).show();
												}
									}
									@Override
									protected void doInBack() throws Exception {
										Map<String, String> param = new HashMap<String, String>();
										param.put("userID", Utils.getID());
										param.put("CampaignID",campaignList.get(position).getId());
										String jsonStr = new WebService(C.CANCELJOINCAMPAIGN, param).getReturnInfo().toString();
										result = GetObjectFromService.getSimplyResult(jsonStr);
									}
								}.execute();
					}
				}).setCancelClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						sweetAlertDialog.dismiss();
					}
				}).show();
				
				return true;
			}
		});
	}
	private void getData(){
		new SimpleNetTask(MyJoinCampaignActivity.this) {
			List<CampaignPostModel> temp=new ArrayList<CampaignPostModel>();
			@Override
			protected void onSucceed() {
				if(temp==null){
					Utils.toast("net wrong");
					return;
				}
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				campaignList.clear();
				campaignList.addAll(temp);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			protected void doInBack() throws Exception {
				String jsonStr = new APIHelper().getPostsByUserId();
				temp = GetObjectFromService.getMyCampaign(jsonStr);
			}
		}.execute();
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}
	
}
