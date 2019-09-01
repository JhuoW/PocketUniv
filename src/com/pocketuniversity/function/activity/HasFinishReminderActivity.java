package com.pocketuniversity.function.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.HasFinishReminderAdapter;
import com.pocketuniversity.adapter.ReminderAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Reminder;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class HasFinishReminderActivity extends BaseActivity implements
OnItemClickListener ,OnItemLongClickListener{
	private ListView listview;
	HasFinishReminderAdapter adapter;
	List<Reminder> list = new ArrayList<Reminder>();
	MyProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hasfinish_reminder);
		initView();
		getData();
	}
	private void initView(){
		dialog = new MyProgressDialog(ctx);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("已完成日程");
		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HasFinishReminderActivity.this.finish();
			}
		});
		listview = (ListView) findViewById(R.id.reminderList);
		adapter = new HasFinishReminderAdapter(ctx, list, getLayoutInflater());
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}
	
	private void getData(){
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
				String jsonStr = new WebService(C.GETHASFINISHNOTE, param)
						.getReturnInfo();
				temp = GetObjectFromService.getHasFinishReminderList(jsonStr);
			}
		}.execute();
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(HasFinishReminderActivity.this,
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
}
