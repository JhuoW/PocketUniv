/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pocketuniversity.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.adapter.HotGroupAdapter;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.SearchGroup;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;

public class PublicGroupsActivity extends BaseActivity {
	private LinearLayout ll_searchResult;
	private EditText et_note;
	private TextView tv_groupName;

	private ListView hotGroup_listview;
	HotGroupAdapter adapter ;
	List<SearchGroup> list = new ArrayList<SearchGroup>();
	private TextView tv_changeGroup;
	static PreferenceMap preference  = new PreferenceMap(App.ctx);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_groups);
		initView();
		initAction();
		getHotGroup();
	}

	private void initView() {
		et_note = (EditText) findViewById(R.id.edit_note);
		tv_groupName = (TextView) findViewById(R.id.name);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("搜索公开群");
		ll_searchResult = (LinearLayout) findViewById(R.id.searchGroup);
		hotGroup_listview = (ListView) findViewById(R.id.hotGroup_listview);
		adapter = new HotGroupAdapter(list, getLayoutInflater());
		hotGroup_listview.setAdapter(adapter);
		tv_changeGroup = (TextView) findViewById(R.id.changeGroup);
	}

	
	private void getHotGroup(){
		new SimpleNetTask(ctx) {
			List<SearchGroup> temp=new ArrayList<SearchGroup>();
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if(temp==null){
					Utils.toast("net wrong");
					return;
				}
				list.clear();
				list.addAll(temp);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				String jsonStr = new WebService(C.GETHOTGROUP, param).getReturnInfo();
				temp = GetObjectFromService.getHotGroupList(jsonStr);
			}
		}.execute();
	}
	
	private void initAction() {
		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PublicGroupsActivity.this.finish();
			}
		});
		headerLayout.showRightTextButton("搜索", new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(TextUtils.isEmpty(et_note.getText().toString())){
					Utils.toast("请输入群号");
					return;
				}
				getData();
			}
		});
		
		ll_searchResult.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(PublicGroupsActivity.this, GroupSearchResultActivity.class));
			}
		});
		tv_changeGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getHotGroup();
			}
		});
		
		hotGroup_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(PublicGroupsActivity.this, GroupSearchActivity2.class).putExtra("post",list.get(position)));
			}
		});
	}

	private void getData() {
		new SimpleNetTask(ctx, false) {
			JSONObject json;
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				
				try {
					if(json.get("ret").equals("error") && json.get("errorMsg").equals("不存在这个群组！请重新输入群号！")){
						Utils.toast("不存在这个群组！请重新输入群号！");
						return;
					}
					

					String groupName = new PreferenceMap(ctx).getSearchGroup().getGroupName();
					if(TextUtils.isEmpty(groupName)){
						Utils.toast("无此群");
						return;
					}
					ll_searchResult.setVisibility(View.VISIBLE);
					tv_groupName.setText(groupName);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				String serverGroupId = et_note.getText().toString().trim();
				param.clear();
				param.put("serverGroupId", serverGroupId);
				String jsonStr = new WebService(C.SEARCHGROUP, param)
						.getReturnInfo();
				try {
					json = new JSONObject(jsonStr);
					if (json.get("ret").equals("success")) {
						JSONArray jsonarray = json.getJSONArray("groupInfo");
						for (int i = 0; i < jsonarray.length(); i++) {
							JSONObject child = jsonarray.getJSONObject(i);
							final String groupName = child.getString("groupName");
							final String serverGroupIdg = child.getString("serverGroupId");
							final String groupId = child.getString("groupId");
							final String description = child.getString("description");
							final String curNum = child.getString("curNum");
							final String maxNum = child.getString("maxNum");
							final String ownerNickName = child.getString("ownerNickName");
							final String ownerId = child.getString("ownerId");
							final String ownerHeader = child.getString("ownerHeader");
							SearchGroup searchGroup = new SearchGroup();
							searchGroup.setGroupName(groupName);
							searchGroup.setServerGroupId(serverGroupIdg);
							searchGroup.setGroupId(groupId);
							searchGroup.setDescription(description);
							searchGroup.setCurNum(curNum);
							searchGroup.setMaxNum(maxNum);
							searchGroup.setOwnerNickName(ownerNickName);
							searchGroup.setOwnerId(ownerId);
							searchGroup.setOwnerHeader(ownerHeader);
							preference.setSearchGroup(searchGroup);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.execute();
	}

	
	
}
