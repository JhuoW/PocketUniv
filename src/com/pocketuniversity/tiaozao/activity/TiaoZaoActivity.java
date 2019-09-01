package com.pocketuniversity.tiaozao.activity;

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
import android.widget.GridView;

import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.TiaoZaoAdapter;
import com.pocketuniversity.base.App;
import com.pocketuniversity.bean.Tiaozao;
import com.pocketuniversity.db.TiaoZao;
import com.pocketuniversity.db.TiaoZaoDBHelper;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.more.activity.MyGoodsCollectionActivity;
import com.pocketuniversity.poptitle.ActionItem;
import com.pocketuniversity.poptitle.TitlePopup;
import com.pocketuniversity.poptitle.TitlePopup.OnItemOnClickListener;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class TiaoZaoActivity extends BaseActivity {

	private GridView gridView;
	TiaoZaoDBHelper tiaozaoDbHelper;
	private List<Tiaozao> newsList = new ArrayList<Tiaozao>();
	private TiaoZaoAdapter adapter;
	private MyProgressDialog dialog;
	
	private TitlePopup titlePopup;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_tiaozao);
		dialog = new MyProgressDialog(ctx);
		dialog.show();
		init();
		findView();
		getData();
	}

	private void findView() {
		tiaozaoDbHelper = new TiaoZaoDBHelper(ctx);
		tiaozaoDbHelper.openSqLiteDatabase();
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("跳蚤");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TiaoZaoActivity.this.finish();
			}
		});
		
		headerLayout.showRightImageButton(R.drawable.ic_fifter, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				startActivity(new Intent(TiaoZaoActivity.this, AddTiaoZaoActivity.class));
				titlePopup.show(v);
			}
		});
		gridView = (GridView) findViewById(R.id.grid_view);
		// gridView.setHorizontalSpacing(horizontalSpacing);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TiaoZaoActivity.this,
						TiaoZaoDetailActivity.class);
				intent.putExtra("post", newsList.get(position));
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}

	
	private void init(){
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.addAction(new ActionItem(this, "我的商品", R.drawable.ic_report_remark));
		titlePopup.addAction(new ActionItem(this, "上传商品", R.drawable.ic_func_edit));
		titlePopup.addAction(new ActionItem(this, "我的收藏", R.drawable.ic_func_collection));
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if(position == 1){
					startActivity(new Intent(TiaoZaoActivity.this, AddTiaoZaoActivity.class));
				}else if (position == 0) {
					startActivity(new Intent(TiaoZaoActivity.this, MyUploadTiaoZaoActivity.class));
				}else{
					startActivity(new Intent(TiaoZaoActivity.this, MyGoodsCollectionActivity.class));
				}
			}
		});
		
	}
	
	private void getData() {
		new SimpleNetTask(ctx, false) {

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				dialog.dismiss();
				List<Tiaozao> list;
				list = tiaozaoDbHelper.getAllTiaozao();
				newsList.clear();
				newsList.addAll(list);
				adapter = new TiaoZaoAdapter(newsList,
						TiaoZaoActivity.this.getLayoutInflater());
				gridView.setAdapter(adapter);
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				String jsonStr = new APIHelper().getTiaozaoList();
				JSONObject json = new JSONObject(jsonStr);
				if (json.get("ret").equals("success")) {
					JSONArray jsonarray = json.getJSONArray("tiaozaoList");
					tiaozaoDbHelper.deleteAllDataFromTable(TiaoZao.TABLE_NAME);
					tiaozaoDbHelper.insertTiaoZao(jsonarray);
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
