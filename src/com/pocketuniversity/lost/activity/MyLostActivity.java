package com.pocketuniversity.lost.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.LostAdapter;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Lost;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.NetAsyncTask;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class MyLostActivity extends BaseActivity {

	private ListView lost_listView;
	private LostAdapter adapter ; 
	private MyProgressDialog dialog;
	List<Lost> lostList = new ArrayList<Lost>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_lost);
		initView();
		initAction();
		getData();
	}
	
	private void initView(){
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		adapter = new LostAdapter(lostList, getLayoutInflater());
		dialog = new MyProgressDialog(ctx);
		dialog.show();
		headerLayout.showTitle("我的发布");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyLostActivity.this.finish();
			}
		});
		lost_listView = (ListView) findViewById(R.id.lost_listview);
	}
	
	private void getData(){
		new SimpleNetTask(ctx) {
			List<Lost> temp = new ArrayList<Lost>();
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
				lostList.addAll(temp);
				lost_listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				String jsonStr = new APIHelper().getMyLostListByUserId();
				temp = GetObjectFromService.getMyLost(jsonStr);
			}
		}.execute();
	}
	
	private void initAction(){
		
		lost_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyLostActivity.this,
						LostDetailActivity.class);
				intent.putExtra("post", lostList.get(position));
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		
		lost_listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE)
				.setTitleText("是否删除该物品").setConfirmText("确定")
				.setConfirmClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(final SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						
						new NetAsyncTask(ctx , false) {
							boolean result;

							@Override
							protected void onPost(Exception e) {
								// TODO Auto-generated method stub
								if(result){
									sweetAlertDialog.dismiss();
									lostList.remove(position);
									adapter.notifyDataSetChanged();
								}else {
									sweetAlertDialog.dismiss();
									Toast.makeText(getApplicationContext(), "删除物品失败", Toast.LENGTH_SHORT).show();
								}
							}
							
							@Override
							protected void doInBack() throws Exception {
								// TODO Auto-generated method stub
								param.clear();
								param.put("userID", Utils.getID());
								param.put("lostId", lostList.get(position).getLostId());
								String jsonStr = new WebService(C.DELETEMYLOST, param).getReturnInfo();
								result = GetObjectFromService.getSimplyResult(jsonStr);
							}
						}.execute();
					}
				}).setCancelText("取消").showCancelButton(true).setCancelClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						sweetAlertDialog.dismiss();

					}
				}).show();
				return true;
			}
		});
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}

