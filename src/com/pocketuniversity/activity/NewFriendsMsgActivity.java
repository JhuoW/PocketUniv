package com.pocketuniversity.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.adapter.NewFriendAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.RequestUser;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.BaseListView;
import com.pocketuniversity.view.MyProgressDialog;

/**
 * 申请与�?�知
 * 
 */
public class NewFriendsMsgActivity extends BaseActivity {
	private BaseListView<RequestUser> listView;
	private List<RequestUser> data = new ArrayList<RequestUser>();
	private NewFriendAdapter adapter;
	RelativeLayout rl_clear ; 
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friends_msg);

		initView();

		initData();

	}

	private void initData() {
		param.clear();
		param.put("userID", Utils.getID());
		new MyAsynTask().executeOnExecutor(Executors.newCachedThreadPool());
	}

	class MyAsynTask extends AsyncTask<Void, Void, Object> {
		MyProgressDialog dialog = new MyProgressDialog(ctx);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected Object doInBackground(Void... params) {
			String jsonstr = new WebService(C.NEWFRIENDREQUEST, param)
					.getReturnInfo();

			Log.i("NewFriendsMsgActivity", "new friend request --->" + jsonstr);

			List<RequestUser> data = GetObjectFromService
					.getRequestUser(jsonstr);
			return data;
		}

		@Override
		protected void onPostExecute(final Object result) {
			super.onPostExecute(result);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			data = (List<RequestUser>) result;
			adapter = new NewFriendAdapter(ctx, data);
			listView.setAdapter(adapter);
			setListViewHeightBasedOnChildren(listView);
		}
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount()));
		params.height += 5;// if without this statement,the listview will be a
							// little short
		listView.setLayoutParams(params);
	}

	private void initView() {
		listView = (BaseListView<RequestUser>) findViewById(R.id.newfriendList);
		rl_clear = (RelativeLayout) findViewById(R.id.container_remove);
		
		rl_clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE)
				.showCancelButton(true).setTitleText("是否清空好友申请？").setConfirmText("确定")
				.setCancelText("取消").setConfirmClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(final SweetAlertDialog sweetAlertDialog) {

						dialog = new ProgressDialog(NewFriendsMsgActivity.this);
						dialog.setCanceledOnTouchOutside(false);
						dialog.setMessage("清空中......");
						dialog.show();
						
						new SimpleNetTask(ctx) {
							boolean flag;
							@Override
							protected void onSucceed() {
								// TODO Auto-generated method stub
								if(flag){
									dialog.dismiss();
									sweetAlertDialog.dismiss();
									listView.setAdapter(null);
									Utils.toast("请空成功");
								}else {
									dialog.dismiss();
									sweetAlertDialog.dismiss();

									Utils.toast("请空失败");
								}
							}
							
							@Override
							protected void doInBack() throws Exception {
								// TODO Auto-generated method stub
								param.clear();
								param.put("userID", Utils.getID());
								String jsonStr = new WebService(C.DELETEFRIENDAPLIC, param).getReturnInfo();
								flag = GetObjectFromService.getSimplyResult(jsonStr);
							}
						}.execute();
					}
				}).setCancelClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						sweetAlertDialog.dismiss();
					}
				}).show();
				
			}
		});
		
		listView.requestFocus();
		

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i("NewFriendsMsgActivity", "点击");
				Intent intent = new Intent(getApplicationContext(), UserDetailInApplyActivity.class);
				intent.putExtra("userId", data.get(position-1).getID());
				startActivity(intent);
			}
		});
		

	}


	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}
}
