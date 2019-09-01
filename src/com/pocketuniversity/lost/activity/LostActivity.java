package com.pocketuniversity.lost.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.LostAdapter;
import com.pocketuniversity.base.App;
import com.pocketuniversity.bean.Lost;
import com.pocketuniversity.db.LostDBHelper;
import com.pocketuniversity.db.LostTable;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.poptitle.ActionItem;
import com.pocketuniversity.poptitle.TitlePopup;
import com.pocketuniversity.poptitle.TitlePopup.OnItemOnClickListener;
import com.pocketuniversity.tiaozao.activity.TiaoZaoActivity;
import com.pocketuniversity.tiaozao.activity.TiaoZaoDetailActivity;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class LostActivity extends BaseActivity{
	
	private ListView lost_listView;
	LostDBHelper lostDbHelper;
	List<Lost> lostList = new ArrayList<Lost>();
	private LostAdapter adapter ; 
	private MyProgressDialog dialog;
	
	private TitlePopup titlePopup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_lost);
		dialog = new MyProgressDialog(ctx);
		dialog.show();
		init();
		initView();
		//getData();
	}
	
	private void initView(){
		lostDbHelper = new LostDBHelper(ctx);
		lostDbHelper.openSqLiteDatabase();
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("失物招领");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LostActivity.this.finish();
			}
		});
		
	headerLayout.showRightImageButton(R.drawable.ic_fifter, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				startActivity(new Intent(TiaoZaoActivity.this, AddTiaoZaoActivity.class));
				titlePopup.show(v);
			}
		});
		lost_listView = (ListView) findViewById(R.id.lost_listview);
		lost_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LostActivity.this,
						LostDetailActivity.class);
				intent.putExtra("post", lostList.get(position));
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}
	
	
	private void init(){
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.addAction(new ActionItem(this, "发布", R.drawable.ic_report_remark));
		titlePopup.addAction(new ActionItem(this, "我的发布", R.drawable.ic_func_edit));
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if(position == 1){
					startActivity(new Intent(LostActivity.this, MyLostActivity.class));
				}else {
					startActivity(new Intent(LostActivity.this, AddLostActivity.class));
				}
			}
		});
		
	}
	
	private void getData(){
		new SimpleNetTask(ctx , false) {
			
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				dialog.dismiss();
				List<Lost> list;
				list = lostDbHelper.getAllLost();
				lostList.clear();
				lostList.addAll(list);
				adapter = new LostAdapter(lostList,
						LostActivity.this.getLayoutInflater());
				lost_listView.setAdapter(adapter);
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				String jsonStr = new APIHelper().getLostList();
				JSONObject json = new JSONObject(jsonStr);
				if (json.get("ret").equals("success")) {
					JSONArray jsonarray = json.getJSONArray("lostList");
					lostDbHelper.deleteAllDataFromTable(LostTable.TABLE_NAME);
					lostDbHelper.insertLost(jsonarray);
				}
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
