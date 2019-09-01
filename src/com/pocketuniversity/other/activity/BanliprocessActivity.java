package com.pocketuniversity.other.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class BanliprocessActivity extends BaseActivity{

	private ListView lv_banli;
	MyProgressDialog dialog;
	SimpleAdapter simpleAdapter;
	List<Map<String,String>> data = new ArrayList<Map<String,String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_banli_process);
		initView();
		getData();
		initAction();
	}
	
	private void initView(){
		dialog = new MyProgressDialog(ctx);
		dialog.show();
		lv_banli = (ListView) findViewById(R.id.listView_banli);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("办理流程");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BanliprocessActivity.this.finish();
			}
		});
	}
	
	
	private void initAction(){
		lv_banli.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String processId = data.get(position).get("processId");
				String processName = data.get(position).get("processName");
				System.out.println(processId);
				Intent intent = new Intent(BanliprocessActivity.this, BanliprocessDetailActivity.class);
				intent.putExtra("processId", processId);
				intent.putExtra("processName", processName);
				System.out.println("processName-->"+processName);
				startActivity(intent);
			}
		});
	}
	
	private void getData(){
		new SimpleNetTask(ctx , false) {
			JSONArray jsonarray;
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				dialog.dismiss();
				for(int i = 0;i<jsonarray.length();i++){
					JSONObject obj;
					try {
						obj = jsonarray.getJSONObject(i);
						String name = obj.getString("processName");
						String id = obj.getString("processId");
						Map<String,String> item = new HashMap<String, String>();
						item.put("processName",name);
						item.put("processId",id);
						data.add(item);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
				}
				
				simpleAdapter = new SimpleAdapter(ctx, data, android.R.layout.simple_list_item_1,new String[]{"processName"},new int[]{android.R.id.text1} );
				
				lv_banli.setAdapter(simpleAdapter);
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				String jsonStr = new WebService(C.GETPROCESSLIST, param).getReturnInfo();
				JSONObject json = new JSONObject(jsonStr);
//				//模拟数据：
//				String jsonStr = "{\"ret\":\"success\",\"process\":[{\"processId\":\"1\",\"processName\":\"第一条数据\"},{\"processId\":\"2\",\"processName\":\"第二条数据\"}]}";
//				JSONObject json = new JSONObject(jsonStr);
				if(json.get("ret").equals("success")){
					jsonarray = json.getJSONArray("process");
				}
			}
		}.execute();
	}
}
