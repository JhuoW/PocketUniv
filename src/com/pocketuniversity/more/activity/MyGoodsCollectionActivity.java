package com.pocketuniversity.more.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.TiaoZaoAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Tiaozao;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.tiaozao.activity.TiaoZaoDetailActivity;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class MyGoodsCollectionActivity extends BaseActivity{

	private GridView gridView;
	private TiaoZaoAdapter adapter;
	private MyProgressDialog dialog;
	private List<Tiaozao> tiaozaoList = new ArrayList<Tiaozao>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tiaozao);
		initView();
		getData();
		initAction();
	}
	
	private void initView(){
		dialog = new MyProgressDialog(MyGoodsCollectionActivity.this);
		gridView = (GridView) findViewById(R.id.grid_view);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("…Ã∆∑ ’≤ÿ");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyGoodsCollectionActivity.this.finish();
			}
		});
	}
	
	private void initAction() {
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyGoodsCollectionActivity.this,
						TiaoZaoDetailActivity.class);
				intent.putExtra("post", tiaozaoList.get(position));
				startActivity(intent);
				MyGoodsCollectionActivity.this.overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}
	
	private void getData() {
		new SimpleNetTask(MyGoodsCollectionActivity.this, false) {

			@Override
			protected void onSucceed() {
				adapter = new TiaoZaoAdapter(tiaozaoList, MyGoodsCollectionActivity.this
						.getLayoutInflater());
				gridView.setAdapter(adapter);
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("userID", Utils.getID());
				String jsonStr = new WebService(C.GETMYGOODSCOLLECTION, param)
						.getReturnInfo();
				tiaozaoList = GetObjectFromService.getMyTiaozao(jsonStr);
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
