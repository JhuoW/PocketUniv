package com.pocketuniversity.more.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.MyAddCampaignAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.MyAddCampaign;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.NetAsyncTask;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class MyPublishCampaignActivity extends BaseActivity{
	private ListView myCompaign_listview;
	private MyProgressDialog dialog;
	private List<MyAddCampaign> list = new ArrayList<MyAddCampaign>(); 
	MyAddCampaignAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_myadd_campaign);
		initView();
		initAction();
		getData();
	}
	
	private void initView(){
		dialog = new MyProgressDialog(MyPublishCampaignActivity.this);
		dialog.show();
		myCompaign_listview = (ListView) findViewById(R.id.campaign_listview);
		adapter = new MyAddCampaignAdapter(MyPublishCampaignActivity.this, list, R.layout.item_myadd_campaign);
		myCompaign_listview.setAdapter(adapter);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("我的发布");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyPublishCampaignActivity.this.finish();
			}
		});
	}
	
	private void getData(){
		new SimpleNetTask(MyPublishCampaignActivity.this) {
			List<MyAddCampaign> temp=new ArrayList<MyAddCampaign>();

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
				list.clear();
				list.addAll(temp);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				String jsonStr = new APIHelper().getMyAddCampaign();
				temp = GetObjectFromService.getMyAddCampaign(jsonStr);
			}
		}.execute();
	}
	
	private void initAction(){
		myCompaign_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					 Intent intent = new Intent(MyPublishCampaignActivity.this,MyAddCampaignActicity.class);
					 intent.putExtra("post", list.get(position));
				//	intent.putExtra("isJoin", newsList.get(position).isJoin());
					 startActivity(intent);
					 MyPublishCampaignActivity.this.overridePendingTransition(R.anim.slide_in_right,
					 R.anim.slide_out_left);
			}
		});
		
		myCompaign_listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				new SweetAlertDialog(MyPublishCampaignActivity.this, SweetAlertDialog.WARNING_TYPE)
				.setTitleText("是否删除该活动？")
				.setConfirmClickListener(new OnSweetClickListener() {
					@Override
					public void onClick(final SweetAlertDialog sweetAlertDialog) {
							new NetAsyncTask(MyPublishCampaignActivity.this,false) {
								boolean result;
									@Override
									protected void onPost(Exception e) {
										if(result){
											sweetAlertDialog.dismiss();
											list.remove(position);
											adapter.notifyDataSetChanged();
												}else {
													sweetAlertDialog.dismiss();
													Toast.makeText(MyPublishCampaignActivity.this, "删除活动失败", Toast.LENGTH_SHORT).show();
												}
									}
									@Override
									protected void doInBack() throws Exception {
										Map<String, String> param = new HashMap<String, String>();
										param.put("userID", Utils.getID());
										param.put("CampaignID",list.get(position).getId());
										String jsonStr = new WebService(C.CANCELADDCAMPAIGN, param).getReturnInfo().toString();
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
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}
}
