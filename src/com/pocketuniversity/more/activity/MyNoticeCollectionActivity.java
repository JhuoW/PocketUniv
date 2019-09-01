package com.pocketuniversity.more.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.pocketuniversity.adapter.NoticeAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Notice;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.other.activity.NoticeDetailActivity;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class MyNoticeCollectionActivity extends BaseActivity{
	
	private ListView lv_notice ;
	List<Notice> noticeList = new ArrayList<Notice>();
	private MyProgressDialog dialog;
	private NoticeAdapter adapter ; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		initView();
		getData();
		initAction();
	}
	
	private void initView(){
		dialog = new MyProgressDialog(MyNoticeCollectionActivity.this);
		lv_notice = (ListView) findViewById(R.id.notice_listview);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("Í¨ÖªÊÕ²Ø");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyNoticeCollectionActivity.this.finish();
			}
		});
	}
	
	private void getData(){
		new SimpleNetTask(MyNoticeCollectionActivity.this,false) {
			
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				adapter = new NoticeAdapter(noticeList, MyNoticeCollectionActivity.this.getLayoutInflater());
				lv_notice.setAdapter(adapter);
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("userID", Utils.getID());
				String jsonStr = new WebService(C.GETMYNOTICECOLLECTION, param).getReturnInfo();
				noticeList = GetObjectFromService.getMyNotice(jsonStr);
			}
		}.execute();
	}
	
	private void initAction(){
		lv_notice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				 Intent intent = new Intent(MyNoticeCollectionActivity.this,NoticeDetailActivity.class);
				intent.putExtra("post", noticeList.get(position));
			//	intent.putExtra("isJoin", newsList.get(position).isJoin());
				 startActivity(intent);
				 MyNoticeCollectionActivity.this.overridePendingTransition(R.anim.slide_in_right,
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
