package com.pocketuniversity.function.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.ReminderAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Reminder;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class ReminderActivity extends BaseActivity implements
		OnItemClickListener ,OnItemLongClickListener{

	private LinearLayout isFinish;
	private ListView listview;
	ReminderAdapter adapter;
	List<Reminder> list = new ArrayList<Reminder>();
	MyProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminder);
		initView();
		getData();
	}

	private void initView() {
		dialog = new MyProgressDialog(ctx);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("日程管理");
		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ReminderActivity.this.finish();
			}
		});
		headerLayout.showRightImageButton(R.drawable.add_schedule,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ReminderActivity.this,
								AddReminderActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}
				});
		isFinish = (LinearLayout) findViewById(R.id.isFinish);
		isFinish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ReminderActivity.this, HasFinishReminderActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		listview = (ListView) findViewById(R.id.reminderList);
		adapter = new ReminderAdapter(ctx, list, getLayoutInflater());
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(this);
	}

	private void getData() {
		dialog.show();
		new SimpleNetTask(ctx) {
			List<Reminder> temp = new ArrayList<Reminder>();

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if (temp == null) {
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
				param.clear();
				param.put("userID", Utils.getID());
				String jsonStr = new WebService(C.GETREMINDERLIST, param)
						.getReturnInfo();
				temp = GetObjectFromService.getReminderList(jsonStr);
			}
		}.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(ReminderActivity.this,
				ReminderDetailActivity.class);
		intent.putExtra("post", list.get(position));
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		// TODO Auto-generated method stub
		new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE).setTitleText("是否删除该日程？")
		.setConfirmText("确定").showCancelButton(true).setCancelText("取消").setConfirmClickListener(new OnSweetClickListener() {
			
			@Override
			public void onClick(final SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				new SimpleNetTask(ctx) {
					boolean flag;

					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						sweetAlertDialog.dismiss();
						if(flag){
							list.remove(position);
							adapter.notifyDataSetChanged();
						}else {
							Utils.showtoast(ctx, R.drawable.tips_error, "删除日程失败，请重试");
						}
					}
					
					@Override
					protected void doInBack() throws Exception {
						// TODO Auto-generated method stub
						param.clear();
						param.put("userID", Utils.getID());
						param.put("id", list.get(position).getId());
						String jsonStr = new WebService(C.DELETENOTE, param).getReturnInfo();
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
		return true;
	}
}
