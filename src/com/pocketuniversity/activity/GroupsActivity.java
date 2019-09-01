package com.pocketuniversity.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.adapter.GroupAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.group.bean.Group;
import com.pocketuniversity.group.db.DBGroup;
import com.pocketuniversity.group.db.GroupDBHelper;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;

public class GroupsActivity extends BaseActivity {
	public static final String TAG = "GroupsActivity";
	private ListView groupListView;
	private GroupAdapter groupAdapter;
	private InputMethodManager inputMethodManager;
	public static GroupsActivity instance;
	GroupDBHelper groupDBHelper;
	List<Group> datas = new ArrayList<Group>();
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_groups);

		findView();

		initAction();
		
		refresh();
	}

	private void findView() {

		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);

		headerLayout.showTitle("群聊");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GroupsActivity.this.finish();
			}
		});
		instance = this;
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		groupListView = (ListView) findViewById(R.id.list);

		groupAdapter = new GroupAdapter(ctx, 1, datas);
		
		groupListView.setAdapter(groupAdapter);
		
		groupDBHelper = new GroupDBHelper(ctx);

		

	}

	
	private void initAction(){

		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					// 新建群聊
					startActivityForResult(new Intent(GroupsActivity.this,
							NewGroupActivity.class), 0);
				} else if (position == 1) {
					// 添加公开�?
					startActivityForResult(new Intent(GroupsActivity.this,
							PublicGroupsActivity.class), 0);
				} else {
					// 进入群聊
					Intent intent = new Intent(GroupsActivity.this,
							ChatActivity.class);
					// it is group chat
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
					intent.putExtra("groupId",
							groupAdapter.getItem(position - 2).getGroupId());
					String serverGroupId = groupDBHelper.getIdByGroupId(groupAdapter.getItem(position - 2).getGroupId());
                    intent.putExtra("serverGroupId", serverGroupId);

					Log.i("GroupActivity", groupAdapter.getItem(position - 2).getGroupId());
					startActivityForResult(intent, 0);
				}
			}

		});
		groupListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(
								getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});

		
	}
	
	/**
	 * 进入公开群聊列表
	 */
	public void onPublicGroups(View view) {
		startActivity(new Intent(this, PublicGroupsActivity.class));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		super.onResume();
		getGroupList();
	}


	public void refresh() {
		if (groupListView != null && groupAdapter != null) {
			getGroupList();
		}
	}

	private void getGroupList() {
		new SimpleNetTask(ctx, false) {
			List<Group> temp=new ArrayList<Group>();
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				temp = groupDBHelper.getAllGroup();
				datas.clear();
				datas.addAll(temp);

				groupAdapter.notifyDataSetChanged();

			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("userID", Utils.getID());
				String jsonStr = new WebService(C.GETMYJOINGROUPLIST, param)
						.getReturnInfo();
				JSONObject json = new JSONObject(jsonStr);
				if (json.get("ret").equals("success")) {
					JSONArray jsonarray = json.getJSONArray("groupList");
					groupDBHelper.deleteAllDataFromTable(DBGroup.TABLENAME);
					groupDBHelper.insertGroup(jsonarray);
				}
			}
		}.execute();
	}
	

}
