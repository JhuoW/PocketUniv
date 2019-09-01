package com.pocketuniversity.function.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.StatusExpandAdapter;
import com.pocketuniversity.bean.CourierDetail;
import com.pocketuniversity.entity.ChildStatusEntity;
import com.pocketuniversity.entity.GroupStatusEntity;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;
import com.show.api.ShowApiRequest;

public class CourierDetailActivity extends BaseActivity {

	private String name = "";
	private String phone = "";
	private String url = "";
	private String imgUrl = "";
	private String simpleName = "";
	
	TextView tv_name;
	TextView tv_phone ;
	TextView tv_url ;
	ImageView img;
	private DisplayImageOptions displayImageOptions;
	
	private EditText search_edit;
	private ImageView iv_search;
	MyProgressDialog mdialog;

	private ExpandableListView expandlistView;
	private StatusExpandAdapter statusAdapter;
	private List<CourierDetail> list = new ArrayList<CourierDetail>();
	List<GroupStatusEntity> gs = new ArrayList<GroupStatusEntity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courier_detail);
		initView();
		Intent intent = getIntent();
		setdata(intent);
		initAction();
	}

	private void initView(){
		mdialog = new MyProgressDialog(ctx);
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.image_error)
				.showImageOnFail(R.drawable.image_error).build();
		tv_name = (TextView) findViewById(R.id.tvTitle);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_url = (TextView) findViewById(R.id.tv_url);
		img = (ImageView) findViewById(R.id.ivRight);
		search_edit = (EditText) findViewById(R.id.search_edit);
		iv_search = (ImageView) findViewById(R.id.search_btn);
		expandlistView = (ExpandableListView) findViewById(R.id.expandlist);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("查询");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CourierDetailActivity.this.finish();
			}
		});
	}
	
	/**
	 * 初始化可拓展列表
	 */
	private void initExpandListView(List<GroupStatusEntity> gs) {
		statusAdapter = new StatusExpandAdapter(ctx, gs);
		expandlistView.setAdapter(statusAdapter);
		expandlistView.setGroupIndicator(null); // 去掉默认带的箭头
		expandlistView.setSelection(0);// 设置默认选中项

		// 遍历所有group,将所有项设置成默认展开
		int groupCount = expandlistView.getCount();
		for (int i = 0; i < groupCount; i++) {
			expandlistView.expandGroup(i);
		}

		expandlistView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}
	
	private void setdata(Intent intent) {
		name = intent.getStringExtra("name");
		phone = intent.getStringExtra("phone");
		url = intent.getStringExtra("url");
		imgUrl = intent.getStringExtra("imgUrl");
		simpleName = intent.getStringExtra("simpleName");
		tv_name.setText(name);
		tv_phone.setText(phone);
		tv_url.setText(url);
		if (imgUrl.length() > 0) {
			img.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(imgUrl,
					img, displayImageOptions);
		} else {
			img.setVisibility(View.GONE);
		}
	}
	
	private void initAction(){
		iv_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mdialog.show();
				String nu = search_edit.getText().toString();
				new ShowApiRequest( "http://route.showapi.com/64-19", "6649", "acaadc0e1924440b82a13a859b4952ef")
	             .setResponseHandler(resHandler)
	             .addTextPara("com", simpleName)
	             .addTextPara("nu", nu)
	             .post();
			}
		});
	}
	
	 final AsyncHttpResponseHandler resHandler=new AsyncHttpResponseHandler(){
	        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
	            //做一些异常处理
	        	if (mdialog != null && mdialog.isShowing()) {
					mdialog.dismiss();
				}
	        	Utils.showtoast(ctx, R.drawable.tips_error, "查询失败");
	            e.printStackTrace();
	        }
	        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
	            try {
	            	if (mdialog != null && mdialog.isShowing()) {
						mdialog.dismiss();
					}
	            	String jsonStr = new String(responseBody,"utf-8");
	            	JSONObject	json = new JSONObject(jsonStr);
					JSONObject jsonobj = json.getJSONObject("showapi_res_body");
					System.out.println(jsonobj);
					JSONArray jsonarray = jsonobj.getJSONArray("data");
					list = GetObjectFromService.getCourierDetailList(jsonarray);
					gs = getListData(list);
					initExpandListView(gs);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	             
	    }};
	    
	    public List<GroupStatusEntity> getListData(List<CourierDetail> list){
	    	List<GroupStatusEntity> groupList = new ArrayList<GroupStatusEntity>();
	    	for(int i = 0;i<list.size();i++){
	    		List<ChildStatusEntity> childList = new ArrayList<ChildStatusEntity>();
	    		GroupStatusEntity groupStatusEntity = new GroupStatusEntity();
	    		groupStatusEntity.setGroupName(list.get(i).getTime());
	    		ChildStatusEntity childStatusEntity = new ChildStatusEntity();
	    		childStatusEntity.setCompleteTime(list.get(i).getContent());
	    		childStatusEntity.setIsfinished(true);
	    		childList.add(childStatusEntity);
	    		groupStatusEntity.setChildList(childList);
	    		groupList.add(groupStatusEntity);
	    	}
	    	return groupList;
	    }
}
