package com.pocketuniversity.friendcircle.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.FriendAdapter;
import com.pocketuniversity.adapter.MyDynamicAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.FriendsCircle;
import com.pocketuniversity.bean.MyDynamic;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.friendscircle.db.FriendsCircleDBHelper;
import com.pocketuniversity.friendscircle.db.FriendsCircleTable;
import com.pocketuniversity.friendscircle.db.MyDynamicTable;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.ui.xlist.XListView;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;
import com.pocketuniversity.view.SquareImageView;

public class MyDynamicActivity extends BaseActivity implements
		OnItemClickListener, OnItemLongClickListener, OnClickListener,
		XListView.IXListViewListener {

	private XListView listView;

	private int pageSize = 10;
	private int currentPage = 1;
	RelativeLayout headView;
	private SquareImageView header;
	private LinearLayout topLayout;
	private TextView tv_name;
	ImageLoader imageLoader = ImageLoader.getInstance();
	MyDynamicAdapter adapter;
	List<MyDynamic> myDynamicList = new ArrayList<MyDynamic>();
	MyProgressDialog dialog;
	PUUser user;
	FriendsCircleDBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydynamic);
		user = new PreferenceMap(ctx).getUser();
		findView();
		initAction();
		onRefresh();
	}

	private void findView() {
		dialog = new MyProgressDialog(ctx);
		dbHelper = new FriendsCircleDBHelper(ctx);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("ÎÒµÄ¶¯Ì¬");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyDynamicActivity.this.finish();
			}
		});
		topLayout = (LinearLayout) findViewById(R.id.friend_circle);

		listView = (XListView) findViewById(R.id.friend_list);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
		adapter = new MyDynamicAdapter(ctx, myDynamicList, getLayoutInflater());
		listView.setAdapter(adapter);
		headView = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.friends_circle_head, null);
		headView.setOnClickListener(this);
		header = (SquareImageView) headView.findViewById(R.id.siv_img);
		tv_name = (TextView) headView.findViewById(R.id.tv_mtopname);
		listView.addHeaderView(headView);

	}

	private void initAction() {
		imageLoader.displayImage(user.getImage(), header,
				PhotoUtils.getImageOptions(R.drawable.default_avatar));
		tv_name.setText(user.getName());
	}

	
	
	public void getData(boolean isLoad) {
		dialog.show();
		new SimpleNetTask(MyDynamicActivity.this) {
			List<MyDynamic> temp = new ArrayList<MyDynamic>();

			@Override
			protected void onSucceed() {
				listView.stopRefresh();
				listView.stopLoadMore();
				if (temp == null) {
					dialog.dismiss();
					Utils.toast("net wrong");
					return;
				}
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				// friendsCircleList.clear();
				// friendsCircleList.addAll(temp);
				List<MyDynamic> list;
				list = dbHelper.queryMyDynamic(currentPage * pageSize + "");
				if (list != null) {
					myDynamicList.clear();
					myDynamicList.addAll(list);
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				Map<String, String> param = new HashMap<String, String>();
				param.clear();
				param.put("userID", Utils.getID());
				param.put("currentPage", currentPage + "");
				param.put("currentPageSize", pageSize + "");
				String jsonStr = new WebService(C.GETMYDYNAMIC, param)
						.getReturnInfo();
				// temp = GetObjectFromService.getFriendsCircleList(jsonStr);
				JSONObject json = new JSONObject(jsonStr);
				if (json.get("ret").equals("success")) {
					JSONArray jsonarray = json
							.getJSONArray("myDynamicList");
					dbHelper.insertDynamicPosts(jsonarray);
				}
			}
		}.execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		currentPage = 1;
		dbHelper.deleteAllDataFromTable(MyDynamicTable.TABLE_NAME);
		getData(false);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		currentPage += 1;
		getData(true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

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
		Intent intent = new Intent(MyDynamicActivity.this,
				MyDynamicDetailActivity.class);
		intent.putExtra("post", myDynamicList.get(position-2));
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
}
