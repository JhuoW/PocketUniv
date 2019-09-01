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
import android.widget.AdapterView.OnItemClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.activity.PostDetailActivity;
import com.pocketuniversity.adapter.MyCollectionAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.PostModel;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;

public class MyCollectionNewsActivity extends BaseActivity{

	private List<PostModel> datas=new ArrayList<PostModel>();
	private MyCollectionAdapter adapter;
	ListView collection_list ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycollection_layout);
		initView();
		initAction();
		initData();
	}
	
	
	private void initView(){
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		collection_list  = (ListView) findViewById(R.id.collection_list);
	}
	
	private void initAction() {
		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyCollectionNewsActivity.this.finish();
			}
		});
		
		collection_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MyCollectionNewsActivity.this,
						PostDetailActivity.class);
				intent.putExtra("post", datas.get(position));
				startActivity(intent);
				MyCollectionNewsActivity.this.overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}
	
	private void initData() {
		headerLayout.showTitle("–¬Œ≈ ’≤ÿ");
		adapter=new MyCollectionAdapter(MyCollectionNewsActivity.this, datas, R.layout.lay_post_lv_item);
		collection_list.setAdapter(adapter);
		new SimpleNetTask(MyCollectionNewsActivity.this,true) {
			
			@Override
			protected void onSucceed() {
				adapter=new MyCollectionAdapter(ctx, datas, R.layout.lay_post_lv_item);
				collection_list.setAdapter(adapter);
			}
			
			@Override
			protected void doInBack() throws Exception {
				param.clear();
				param.put("userID", Utils.getID());
				String jsonstr=new WebService(C.GETMYUSERCOLLECTIONLIST, param).getReturnInfo();
				datas=GetObjectFromService.getCollectionNews(jsonstr);
			}
		}.execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}
}
