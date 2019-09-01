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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.adapter.ContactGroupAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.bean.SortUser;
import com.pocketuniversity.db.FriendsDBHelper;
import com.pocketuniversity.group.bean.Group;
import com.pocketuniversity.group.bean.GroupMember;
import com.pocketuniversity.group.db.DBGroup;
import com.pocketuniversity.group.db.GroupDBHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.CharacterParser;
import com.pocketuniversity.utils.PinyinComparator;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.EnLetterView;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class GroupPickContactsActivity extends BaseActivity {
	private ListView listView;
	/** 是否为一个新建的群组 */
	public boolean isCreatingNewGroup ;
	/** 是否为单�? */
	private boolean isSignleChecked;
	private PickContactAdapter contactAdapter;
	/** group中一�?始就有的成员 */
	private List<String> exitingMembers;
	List<SortUser> friends = new ArrayList<SortUser>();

	private EnLetterView rightLetter;
	private TextView dialog;

	List<PUUser> members ;
	
	private static CharacterParser characterParser;
	private static PinyinComparator pinyinComparator;
	
	String name;
	String description;
	String maxNum;
	String[] s;
	
	String groupId;
	String k ;
	
	private GroupDBHelper groupDBHelper;
	
	List<String> list = new ArrayList<String>();
	
	//群组中原来就有的成员
	List<GroupMember> member ;
	
	private MyProgressDialog mdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_pick_contacts);
		groupDBHelper = new GroupDBHelper(ctx);
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		description = intent.getStringExtra("description");
		maxNum = intent.getStringExtra("maxNum");
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();

		// String groupName = getIntent().getStringExtra("groupName");
		groupId = getIntent().getStringExtra("groupId");
		if (groupId == null) {// 创建群组
			isCreatingNewGroup = true;
			k = "是";
		} else {
			// 获取此群组的成员列表
			isCreatingNewGroup = false;
			k = "否";
			member = groupDBHelper.getAllmember();
			for(int i = 0;i<member.size();i++){
				String userid = member.get(i).getUserId();
				list.add(userid);
			}
		}
		if (exitingMembers == null)
			exitingMembers = new ArrayList<String>();

		// 获取好友列表
		FriendsDBHelper friendsDbHelper = new FriendsDBHelper(ctx);
		List<PUUser> alluserList = new ArrayList<PUUser>();
		alluserList = friendsDbHelper.getAllFriends();

		mdialog = new MyProgressDialog(ctx);
		
		// // 对list进行排序
		// Collections.sort(alluserList, new Comparator<PUUser>() {
		// @Override
		// public int compare(PUUser lhs, PUUser rhs) {
		// return (lhs.getName().compareTo(rhs.getName()));
		//
		// }
		// });

		List<SortUser> sortUsers = convertAVUser(alluserList);

		listView = (ListView) findViewById(R.id.list);
		contactAdapter = new PickContactAdapter(this,
				R.layout.row_contact_with_checkbox, sortUsers);
		listView.setAdapter(contactAdapter);
		// ((Sidebar) findViewById(R.id.sidebar)).setListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				checkBox.toggle();

			}
		});
		initRightLetterView();
		
		initAction();
	}

	private void initRightLetterView() {
		rightLetter = (EnLetterView) findViewById(R.id.right_letter);
		dialog = (TextView) findViewById(R.id.dialog);
		rightLetter.setTextView(dialog);
		rightLetter
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
	}

	private class LetterListViewListener implements
			EnLetterView.OnTouchingLetterChangedListener {
		@Override
		public void onTouchingLetterChanged(String s) {
			int position = contactAdapter.getPositionForSection(s.charAt(0));
			if (position != -1) {
				listView.setSelection(position);
			}
		}
	}

	
	private void initAction(){
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("请选择联系人");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GroupPickContactsActivity.this.finish();
			}
		});
		headerLayout.showRightTextButton("保存", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				save();
			}
		});
	}
	
	/**
	 * 确认选择的members
	 * 
	 * @param v
	 */
	public void save() {
		if(k.equals("否")){
			Group group = groupDBHelper.getGroup2(groupId);
			final String id = group.getId();
			mdialog.show();
			list = getToBeAddMembers();
			JSONArray json = new JSONArray();
			for(int i = 0;i<list.size();i++){
				JSONObject jo = new JSONObject();
				try {
					jo.put("id", list.get(i));
					json.put(jo);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final String newMember = "{\"chatGroupMumber\":" + json + "}";
				new SimpleNetTask(ctx , false) {
					boolean flag;
					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						if(flag){
							if (mdialog != null && mdialog.isShowing()) {
								mdialog.dismiss();
							}
							Utils.toast("添加群成员成功");
							GroupPickContactsActivity.this.finish();
						}else {
							if (mdialog != null && mdialog.isShowing()) {
								mdialog.dismiss();
							}
							Utils.toast("添加群成员失败");
							GroupPickContactsActivity.this.finish();

						}
						
					}
					
					@Override
					protected void doInBack() throws Exception {
						// TODO Auto-generated method stub
						param.clear();
						param.put("chatGroupMumber", newMember);
						param.put("groupId", groupId);
						param.put("Id", id);
						String jsonStr = new WebService(C.ADDGROUPMEMBER, param).getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonStr);
					}
				}.execute();
			}
			return;
		}
		
		
		mdialog.show();
		list = getToBeAddMembers();
		JSONArray json = new JSONArray();
        for(int i = 0 ; i<list.size();i++){
            JSONObject jo = new JSONObject();
            try {
				jo.put("id", list.get(i));
	            json.put(jo);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		System.out.println("jsonarray:"+ json);
		
		final String friends = "{\"chatGroupMumber\":" + json + "}";
		System.out.println(friends);
		new SimpleNetTask(ctx , false) {
			boolean flag;
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if(flag){
					mdialog.dismiss();
					Utils.toast("添加群组成功");
					GroupPickContactsActivity.this.finish();
				}else{
					mdialog.dismiss();
					Utils.toast("添加群组失败");
				}
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("name", name);
				param.put("description", description);
				param.put("userID", Utils.getID());
				param.put("friends", friends);
				param.put("maxNum", maxNum);
				String jsonStr = new WebService(C.CREATENEWGROUP, param).getReturnInfo();
				flag=GetObjectFromService.getSimplyResult(jsonStr);
			}
		}.execute();
	}

	/**
	 * 获取要被添加的成�?
	 * 
	 * @return
	 */
	private List<String> getToBeAddMembers() {
		List<String> members = new ArrayList<String>();
		int length = contactAdapter.isCheckedArray.length;
		for (int i = 0; i < length; i++) {
			String username = contactAdapter.getItem(i).getInnerUser()
					.getUserId();
			if (contactAdapter.isCheckedArray[i]
					&& !list.contains(username)) {
				members.add(username);
			}
		}
		return members;
	}

	/**
	 * adapter
	 */
	private class PickContactAdapter extends ContactGroupAdapter {

		private boolean[] isCheckedArray;

		public PickContactAdapter(Context context, int resource,
				List<SortUser> users) {
			super(context, resource, users);
			isCheckedArray = new boolean[users.size()];
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			// if (position > 0) {
			final String username = getItem(position).getInnerUser().getName();
			// 选择框checkbox
			final CheckBox checkBox = (CheckBox) view
					.findViewById(R.id.checkbox);
			if (list != null && list.contains(username)) {
				checkBox.setButtonDrawable(R.drawable.checkbox_bg_gray_selector);
			} else {
				checkBox.setButtonDrawable(R.drawable.checkbox_bg_selector);
			}
			if (checkBox != null) {
				// checkBox.setOnCheckedChangeListener(null);

				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 群组中原来的成员�?直设为�?�中状�??
						if (list.contains(getItem(position).getInnerUser().getUserId())) {
							isChecked = true;
							checkBox.setChecked(true);
						}
						isCheckedArray[position] = isChecked;
						// 如果是单选模�?
						if (isSignleChecked && isChecked) {
							for (int i = 0; i < isCheckedArray.length; i++) {
								if (i != position) {
									isCheckedArray[i] = false;
								}
							}
							contactAdapter.notifyDataSetChanged();
						}

					}
				});
				// 群组中原来的成员�?直设为�?�中状�??
				if (list.contains(getItem(position).getInnerUser().getUserId())) {
					checkBox.setChecked(true);
					isCheckedArray[position] = true;
				} else {
					checkBox.setChecked(isCheckedArray[position]);
				}
			}
			// }
			return view;
		}
	}

	public void back(View view) {
		finish();
	}

	private List<SortUser> convertAVUser(List<PUUser> datas) {
		List<SortUser> sortUsers = new ArrayList<SortUser>();
		int total = datas.size();
		for (int i = 0; i < total; i++) {
			PUUser avUser = datas.get(i);
			SortUser sortUser = new SortUser();
			sortUser.setInnerUser(avUser);
			String username = avUser.getName();
			if (username != null) {
				String pinyin = characterParser.getSelling(username);
				String sortString = pinyin.substring(0, 1).toUpperCase();
				if (sortString.matches("[A-Z]")) {
					sortUser.setSortLetters(sortString.toUpperCase());
				} else {
					sortUser.setSortLetters("#");
				}
			} else {
				sortUser.setSortLetters("#");
			}
			sortUsers.add(sortUser);
		}
		Collections.sort(sortUsers, pinyinComparator);
		return sortUsers;
	}
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}
	
}
