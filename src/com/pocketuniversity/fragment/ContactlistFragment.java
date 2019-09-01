package com.pocketuniversity.fragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.AddContactActivity;
import com.pocketuniversity.activity.ChatActivity;
import com.pocketuniversity.activity.ContactActivity;
import com.pocketuniversity.activity.GroupsActivity;
import com.pocketuniversity.activity.NewFriendsMsgActivity;
import com.pocketuniversity.adapter.UserFriendAdapter;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.bean.SortUser;
import com.pocketuniversity.db.FriendsTable;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.service.UserService;
import com.pocketuniversity.ui.xlist.XListView;
import com.pocketuniversity.utils.CharacterParser;
import com.pocketuniversity.utils.NetAsyncTask;
import com.pocketuniversity.utils.PinyinComparator;
import com.pocketuniversity.view.EnLetterView;


/**
 * 联系人列表页
 * 
 */
public class ContactlistFragment extends Fragment implements
		OnItemClickListener, OnItemLongClickListener, OnClickListener,
		com.pocketuniversity.ui.xlist.XListView.IXListViewListener {

	private com.pocketuniversity.ui.xlist.XListView friendsList;
	
	
	private TextView dialog;
	private EnLetterView rightLetter;
	private UserFriendAdapter userAdapter;
	private List<SortUser> friends = new ArrayList<SortUser>();
	private List<PUUser> oldfriends;
	public static ImageView msgTipsView;
	private LinearLayout newFriendLayout, contactLayout , groupLayout;
	private static CharacterParser characterParser;
	private static PinyinComparator pinyinComparator;
	private FriendsTable friendstable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_contact_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false))
			return;
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		friendstable = FriendsTable.getInstance();
		ImageView addContactView = (ImageView) getView().findViewById(R.id.iv_new_contact);
		// 进入添加好友�?
		addContactView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), AddContactActivity.class));
			}
		});
		initListView();
		initRightLetterView();
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

	private void initListView() {
		friendsList = (XListView) getView().findViewById(R.id.list_friends);
		friendsList.setPullRefreshEnable(true);
		friendsList.setPullLoadEnable(false);
		friendsList.setXListViewListener(this);
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		RelativeLayout headView = (RelativeLayout) mInflater.inflate(
				R.layout.contact_include_new_friend, null);
		msgTipsView = (ImageView) headView.findViewById(R.id.iv_msg_tips);
		newFriendLayout = (LinearLayout) headView.findViewById(R.id.layout_new);
		contactLayout = (LinearLayout) headView
				.findViewById(R.id.layout_contact);
		groupLayout = (LinearLayout) headView.findViewById(R.id.layout_group);

		newFriendLayout.setOnClickListener(this);
		contactLayout.setOnClickListener(this);
		groupLayout.setOnClickListener(this);
		friendsList.addHeaderView(headView);
		oldfriends = friendstable.selectFriends();

		List<SortUser> sortUsers = convertAVUser(oldfriends);
		ContactlistFragment.this.friends = Collections.unmodifiableList(sortUsers);
		userAdapter = new UserFriendAdapter(getActivity(), sortUsers);
		friendsList.setAdapter(userAdapter);
		friendsList.setOnItemClickListener(this);
		friendsList.setOnItemLongClickListener(this);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	private void initRightLetterView() {
		rightLetter = (EnLetterView) getView().findViewById(R.id.right_letter);
		dialog = (TextView) getView().findViewById(R.id.dialog);
		rightLetter.setTextView(dialog);
		rightLetter
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_new:
			msgTipsView.setVisibility(View.INVISIBLE);
			Utils.goActivity(getActivity(), NewFriendsMsgActivity.class);
			break;
		case R.id.layout_contact:
			Utils.goActivity(getActivity(), ContactActivity.class);
			break;
		case R.id.layout_group :
			Utils.goActivity(getActivity(), GroupsActivity.class);
		}

	}

	@Override
	public void onRefresh() {
		new NetAsyncTask(getActivity(), false) {
			boolean haveAddRequest;
			List<PUUser> friend;
			@Override
			protected void doInBack() throws Exception {
				friend = UserService.findFriends();
				if (friend.size() != 0 && friend != null) {
					friendstable.deleteAllFriend(oldfriends);
					friendstable.insertFriends(friend);
					oldfriends = friend;
				}
				if (friendstable.selectFriends().size() == 0) {
					oldfriends.clear();
				}
				
				Map<String, User> usermap = App.getInstance().getContactList();

				for (int i = 0; i < friend.size(); i++) {
					PUUser PUUser = friend.get(i);
					User user = new User();
					user.setAvatar(PUUser.getImage());
					user.setNick(PUUser.getName());
					user.setUsername(PUUser.getID());
					usermap.put(PUUser.getID(), user);
				}
				App.getInstance().setContactList(usermap);
				UserDao dao = new UserDao(ctx);
				List<User> users = new ArrayList<User>(usermap.values());
				dao.saveContactList(users);
			}

			@Override
			protected void onPost(Exception e) {
				friendsList.stopRefresh();
				if (e != null) {
					Utils.toast(ctx, e.getMessage());
				} else {
					List<SortUser> sortUsers = convertAVUser(oldfriends);
					ContactlistFragment.this.friends = Collections
							.unmodifiableList(sortUsers);
					userAdapter.updateDatas(sortUsers);
				}
			}
		}.execute();
	}

	public static void showNewFriendTips() {
		msgTipsView.setVisibility(View.VISIBLE);
	}

	private class LetterListViewListener implements
			EnLetterView.OnTouchingLetterChangedListener {
		@Override
		public void onTouchingLetterChanged(String s) {
			int position = userAdapter.getPositionForSection(s.charAt(0));
			if (position != -1) {
				friendsList.setSelection(position);
			}
		}
	}

	private boolean hidden;

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			onRefresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			onRefresh();
		}
	}

	/**
	 * 再传入一个userid
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		final SortUser user = (SortUser) arg0.getAdapter().getItem(position);
		System.out.println("username--------->" + user.getInnerUser().getUserId());
		System.out.println("userId----------->" + user.getInnerUser().getID());  //10
		System.out.println("convastionId ------> " + user.getInnerUser().getName());
		System.out.println("imgUrl-------->" +  user.getInnerUser().getImage());
		startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(
				"userId", user.getInnerUser().getID()).putExtra("username", user.getInnerUser().getUserId())
				.putExtra("imgUrl", user.getInnerUser().getImage()));
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			int position, long arg3) {
		final SortUser user = (SortUser) arg0.getAdapter().getItem(position);

		new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
				.setTitleText("解除与" + user.getInnerUser().getName() + "的好友关系？")
				.setConfirmText("确定解除")
				.setCancelText("取消解除")
				.showCancelButton(true)
				.setCancelClickListener(
						new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								sDialog.dismiss();
							}
						})
				.setConfirmClickListener(
						new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(final SweetAlertDialog sDialog) {
								new NetAsyncTask(getActivity(), false) {
									boolean result = false;

									@Override
									protected void onPost(Exception e) {
										if (result) {
											sDialog.setTitleText("完成")
													.setContentText("已解除好友关系")
													.setConfirmText("确定")
													.showCancelButton(false)
													.setConfirmClickListener(
															null)
													.changeAlertType(
															SweetAlertDialog.SUCCESS_TYPE);
											friendstable.deleteFriend(user
													.getInnerUser().getID());
											onRefresh();

										} else {
											sDialog.setContentText("解除好友关系失败")
													.setConfirmText("确定")
													.showCancelButton(false)
													.changeAlertType(
															SweetAlertDialog.ERROR_TYPE);
										}
									}

									@Override
									protected void doInBack() throws Exception {
										Map<String, String> param = new HashMap<String, String>();
										param.put("fromUserID", Utils.getID());
										param.put("toUserID", user
												.getInnerUser().getID());
										String jsonstr = new WebService(
												C.DELETEFRIEND, param)
												.getReturnInfo();
										result = GetObjectFromService
												.getSimplyResult(jsonstr);
									}
								}.execute();

							}
						}).show();
		return true;
	}

	@Override
	public void onLoadMore() {
	}
	

}
