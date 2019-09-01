package com.pocketuniversity.other.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.AddCampaignActivity;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.activity.CampaignActivity;
import com.pocketuniversity.activity.MyJoinCampaignActivity;
import com.pocketuniversity.adapter.NoticeAdapter;
import com.pocketuniversity.base.App;
import com.pocketuniversity.bean.Notice;
import com.pocketuniversity.db.NoticeTable;
import com.pocketuniversity.db.OtherDBHelper;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.lost.activity.LostActivity;
import com.pocketuniversity.lost.activity.LostDetailActivity;
import com.pocketuniversity.more.activity.CampaignCollectionActivitity;
import com.pocketuniversity.more.activity.MyNoticeCollectionActivity;
import com.pocketuniversity.more.activity.MyPublishCampaignActivity;
import com.pocketuniversity.poptitle.ActionItem;
import com.pocketuniversity.poptitle.TitlePopup;
import com.pocketuniversity.poptitle.TitlePopup.OnItemOnClickListener;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class NoticeActivity extends BaseActivity {

	private ListView listView;
	OtherDBHelper otherDBHelper;
	List<Notice> noticeList = new ArrayList<Notice>();
	private NoticeAdapter adapter ; 
	private MyProgressDialog dialog;
	private TitlePopup titlePopup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_notice);
		dialog = new MyProgressDialog(ctx);
		dialog.show();
		init();
		initView();
	}
	
	private void initView(){
		otherDBHelper = new OtherDBHelper(ctx);
		otherDBHelper.openSqLiteDatabase();
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("通知公告");
		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NoticeActivity.this.finish();
			}
		});
		headerLayout.showRightImageButton(R.drawable.ic_fifter, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				titlePopup.show(v);
			}
		});
		listView = (ListView) findViewById(R.id.notice_listview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NoticeActivity.this,
						NoticeDetailActivity.class);
				intent.putExtra("post", noticeList.get(position));
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}
	
	private void init(){
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.addAction(new ActionItem(this, "我的收藏", R.drawable.ic_func_collection));
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if(position == 0){
					startActivity(new Intent(NoticeActivity.this, MyNoticeCollectionActivity.class));
				}else {
					
				}
			}
		});
		
	}
	
	private void getData(){
		new SimpleNetTask(ctx,false) {
			
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				dialog.dismiss();
				List<Notice> list;
				list = otherDBHelper.getAllNotice();
				noticeList.clear();
				noticeList.addAll(list);
				adapter = new NoticeAdapter(noticeList,
						NoticeActivity.this.getLayoutInflater());
				listView.setAdapter(adapter);
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				String jsonStr = new APIHelper().getNoticeListByUserId();
				JSONObject json = new JSONObject(jsonStr);
				if (json.get("ret").equals("success")) {
					JSONArray jsonarray = json.getJSONArray("noticeList");
					otherDBHelper.deleteAllDataFromTable(NoticeTable.TABLE_NAME);
					otherDBHelper.insertNotice(jsonarray);
				}
			}
		}.execute();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}
}
