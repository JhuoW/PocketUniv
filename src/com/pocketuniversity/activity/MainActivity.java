package com.pocketuniversity.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.ab.view.slidingmenu.SlidingMenu;
import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMGroupChangeListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.chatuidemo.video.util.Utils;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.easemob.util.NetUtils;
import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.db.FriendsTable;
import com.pocketuniversity.fragment.CampaignFragment;
import com.pocketuniversity.fragment.ChatAllHistoryFragment;
import com.pocketuniversity.fragment.ContactlistFragment;
import com.pocketuniversity.fragment.FindFragment;
import com.pocketuniversity.fragment.HomeFragment;
import com.pocketuniversity.fragment.InformationFragment;
import com.pocketuniversity.fragment.MainMenuFragment;
import com.pocketuniversity.fragment.MoreFragment;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.more.activity.ScheduleActivity;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.RoundImageView;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends BaseActivity implements EMEventListener {

	private static final String TAG = "MainActivity";

	private ChatAllHistoryFragment chatHistoryFragment;
	private ContactlistFragment contactlistFragment;
	private InformationFragment informationFragment;
	private CampaignFragment campaignFragment;
	private HomeFragment homeFragment;
	private FindFragment findFragment;
	private MoreFragment moreFragment;
	// δ����Ϣtextview
	private TextView unreadLabel;
	// δ��ͨѶ¼textview
	private TextView unreadAddressLable;

	private Button btn_more;
	private PopupWindow popupWindow;
	private View view;

	private RoundImageView img_menu;

	private SlidingMenu menu;

	private TextView header_text;

	// �˺��ڱ𴦵�¼
	public boolean isConflict = false;
	// �˺ű��Ƴ�
	private boolean isCurrentAccountRemoved = false;

	private Fragment[] fragments;
	private Button[] mbuttons;

	private int index;
	// ��ǰfragment��index
	private int currentTabIndex;

	private MyConnectionListener connectionListener = null;
	private MyGroupChangeListener groupChangeListener = null;

	private PUUser curUser;

	/**
	 * ��鵱ǰ�û��Ƿ�ɾ��
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED,
						false)) {
			// ��ֹ���Ƴ���û��ȷ����ťȻ����home���������ں�̨�ֽ�app���µ�crash
			// ����fragment��ӵ��ж�ͬ��
			App.getInstance().logout(null);
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		} else if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false)) {
			// ��ֹ��T��û��ȷ����ťȻ����home���������ں�̨�ֽ�app���µ�crash
			// ����fragment��ӵ��ж�ͬ��
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		String id = Utils.getID();
		JPushInterface.setAlias(ctx, id, new TagAliasCallback() {

			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				// TODO Auto-generated method stub

			}
		});

		App.getInstance().addActivity(MainActivity.this);
		setContentView(R.layout.activity_main);
		curUser = new PreferenceMap(this).getUser();
		initView();
		initData();
		setMenu();

		// ����ѡ�ť
		btn_more = (Button) findViewById(R.id.btn_more_choose);
		btn_more.setOnClickListener(new MoreChooseClickListener());

		MobclickAgent.updateOnlineConfig(this);

		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}

		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);
		// ���fragmentֻ��ʾ���Ѻ�Ⱥ��������¼
		// chatHistoryFragment = new ChatHistoryFragment();
		// ��ʾ��������Ϣ��¼��fragment

		chatHistoryFragment = new ChatAllHistoryFragment();
		contactlistFragment = new ContactlistFragment();
		informationFragment = new InformationFragment();
		campaignFragment = new CampaignFragment();
		homeFragment = new HomeFragment();
		findFragment = new FindFragment();
		moreFragment = new MoreFragment();
		fragments = new Fragment[] { homeFragment, chatHistoryFragment,
				contactlistFragment, informationFragment, campaignFragment,
				findFragment ,moreFragment };
		// �����ʾ��һ��fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, homeFragment)
				.add(R.id.fragment_container, chatHistoryFragment)
				.hide(chatHistoryFragment).show(homeFragment).commit();

		init();
		// ֪ͨsdk��UI �Ѿ���ʼ����ϣ�ע������Ӧ��receiver��listener, ���Խ���broadcast��
		EMChat.getInstance().setAppInited();
	}

	private void init() {
		// setContactListener������ϵ�˵ı仯��
		EMContactManager.getInstance().setContactListener(
				new MyContactListener());
		// ע��һ����������״̬��listener

		connectionListener = new MyConnectionListener();
		EMChatManager.getInstance().addConnectionListener(connectionListener);

		groupChangeListener = new MyGroupChangeListener();
		// ע��Ⱥ����ص�listener
		EMGroupManager.getInstance()
				.addGroupChangeListener(groupChangeListener);

	}

	private void initData() {
		FriendsTable table = FriendsTable.getInstance();
		List<PUUser> localfriend = table.selectFriends();
		Map<String, User> usermap = new HashMap<String, User>();

		for (int i = 0; i < localfriend.size(); i++) {
			PUUser PUUser = localfriend.get(i);
			User user = new User();
			user.setAvatar(PUUser.getImage());
			user.setNick(PUUser.getName());
			user.setUsername(PUUser.getID());
			usermap.put(PUUser.getID(), user);
		}
		PUUser localuser = new PUUser();
		localuser = new PreferenceMap(ctx).getUser();
		User curuser = new User();
		curuser.setAvatar(localuser.getImage());
		curuser.setNick(localuser.getName());
		curuser.setUsername(localuser.getID());
		usermap.put(localuser.getID(), curuser);
		App.getInstance().setContactList(usermap);
		UserDao dao = new UserDao(ctx);
		List<User> users = new ArrayList<User>(usermap.values());
		dao.saveContactList(users);
	}

	/**
	 * button����¼�
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_home:
			index = 0;
			break;
		case R.id.btn_conversation:
			index = 1;
			break;
		case R.id.btn_address_list:
			index = 2;
			break;
		case R.id.btn_information:
			index = 3;
			break;
		case R.id.btn_campaign:
			index = 4;
			break;
		case R.id.btn_find_list:
			index = 5;
			break;
		case R.id.btn_more_list:
			index = 6;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mbuttons[currentTabIndex].setSelected(false);
		// �ѵ�ǰtab��Ϊѡ��״̬
		mbuttons[index].setSelected(true);
		currentTabIndex = index;
	}

	public static void goMainActivity(Activity activity) {
		Intent intent = new Intent(activity, MainActivity.class);
		activity.startActivity(intent);
	}

	public static ImageLoader imageLoader = ImageLoader.getInstance();

	void initView() {
		img_menu = (RoundImageView) findViewById(R.id.img_menu);
		String url = curUser.getImage();
		if (TextUtils.isEmpty(url)) {
			url = null;
		}
		imageLoader.displayImage(url, img_menu,
				PhotoUtils.getImageOptions(R.drawable.default_avatar));
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
		header_text = (TextView) findViewById(R.id.textView1);

		mbuttons = new Button[7];
		mbuttons[0] = (Button) findViewById(R.id.btn_home);
		mbuttons[1] = (Button) findViewById(R.id.btn_conversation);
		mbuttons[2] = (Button) findViewById(R.id.btn_address_list);
		mbuttons[3] = (Button) findViewById(R.id.btn_information);
		mbuttons[4] = (Button) findViewById(R.id.btn_campaign);
		mbuttons[5] = (Button) findViewById(R.id.btn_find_list);
		mbuttons[6] = (Button) findViewById(R.id.btn_more_list);
		mbuttons[0].setSelected(true);

	}

	static void asyncFetchGroupsFromServer() {
		HXSDKHelper.getInstance().asyncFetchGroupsFromServer(new EMCallBack() {

			@Override
			public void onSuccess() {
				HXSDKHelper.getInstance().noitifyGroupSyncListeners(true);

				if (HXSDKHelper.getInstance().isContactsSyncedWithServer()) {
					HXSDKHelper.getInstance().notifyForRecevingEvents();
				}
			}

			@Override
			public void onError(int code, String message) {
				HXSDKHelper.getInstance().noitifyGroupSyncListeners(false);
			}

			@Override
			public void onProgress(int progress, String status) {

			}

		});
	}

	static void asyncFetchContactsFromServer() {
		HXSDKHelper.getInstance().asyncFetchContactsFromServer(
				new EMValueCallBack<List<String>>() {

					@Override
					public void onSuccess(List<String> usernames) {
						Context context = HXSDKHelper.getInstance()
								.getAppContext();

						System.out.println("----------------"
								+ usernames.toString());
						EMLog.d("roster", "contacts size: " + usernames.size());
						Map<String, User> userlist = new HashMap<String, User>();
						for (String username : usernames) {
							User user = new User();
							user.setUsername(username);
							setUserHearder(username, user);
							userlist.put(username, user);
						}
						// ���user"������֪ͨ"
						User newFriends = new User();
						newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
						String strChat = context
								.getString(R.string.Application_and_notify);
						newFriends.setNick(strChat);

						userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
						// ���"Ⱥ��"
						User groupUser = new User();
						String strGroup = context
								.getString(R.string.group_chat);
						groupUser.setUsername(Constant.GROUP_USERNAME);
						groupUser.setNick(strGroup);
						groupUser.setHeader("");
						userlist.put(Constant.GROUP_USERNAME, groupUser);

						// ���"������"
						User chatRoomItem = new User();
						String strChatRoom = context
								.getString(R.string.chat_room);
						chatRoomItem.setUsername(Constant.CHAT_ROOM);
						chatRoomItem.setNick(strChatRoom);
						chatRoomItem.setHeader("");
						userlist.put(Constant.CHAT_ROOM, chatRoomItem);

						// ���"Robot"
						User robotUser = new User();
						String strRobot = context
								.getString(R.string.robot_chat);
						robotUser.setUsername(Constant.CHAT_ROBOT);
						robotUser.setNick(strRobot);
						robotUser.setHeader("");
						userlist.put(Constant.CHAT_ROBOT, robotUser);

						// �����ڴ�
						App.getInstance().setContactList(userlist);
						// ����db
						UserDao dao = new UserDao(context);
						List<User> users = new ArrayList<User>(userlist
								.values());
						dao.saveContactList(users);

						HXSDKHelper.getInstance().notifyContactsSyncListener(
								true);

						if (HXSDKHelper.getInstance()
								.isGroupsSyncedWithServer()) {
							HXSDKHelper.getInstance().notifyForRecevingEvents();
						}

					}

					@Override
					public void onError(int error, String errorMsg) {
						HXSDKHelper.getInstance().notifyContactsSyncListener(
								false);
					}

				});
	}

	static void asyncFetchBlackListFromServer() {
		HXSDKHelper.getInstance().asyncFetchBlackListFromServer(
				new EMValueCallBack<List<String>>() {

					@Override
					public void onSuccess(List<String> value) {
						EMContactManager.getInstance().saveBlackList(value);
						HXSDKHelper.getInstance().notifyBlackListSyncListener(
								true);
					}

					@Override
					public void onError(int error, String errorMsg) {
						HXSDKHelper.getInstance().notifyBlackListSyncListener(
								false);
					}

				});
	}

	/**
	 * ����hearder���ԣ�����ͨѶ�ж���ϵ�˰�header������ʾ���Լ�ͨ���Ҳ�ABCD...��ĸ�����ٶ�λ��ϵ��
	 * 
	 * @param username
	 * @param user
	 */
	private static void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	/**
	 * �����¼�
	 */
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage: // ��ͨ��Ϣ
		{
			EMMessage message = (EMMessage) event.getData();

			// ��ʾ����Ϣ
			HXSDKHelper.getInstance().getNotifier().onNewMsg(message);

			refreshUI();
			break;
		}

		case EventOfflineMessage: {
			refreshUI();
			break;
		}

		case EventConversationListChanged: {
			refreshUI();
			break;
		}

		default:
			break;
		}
	}

	private void refreshUI() {
		runOnUiThread(new Runnable() {
			public void run() {
				// ˢ��bottom bar��Ϣδ����
				updateUnreadLabel();
				if (currentTabIndex == 0) {
					// ��ǰҳ�����Ϊ������ʷҳ�棬ˢ�´�ҳ��
					if (chatHistoryFragment != null) {
						chatHistoryFragment.refresh();
					}
				}
			}
		});
	}

	@Override
	public void back(View view) {
		super.back(view);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

		if (connectionListener != null) {
			EMChatManager.getInstance().removeConnectionListener(
					connectionListener);
		}

		if (groupChangeListener != null) {
			EMGroupManager.getInstance().removeGroupChangeListener(
					groupChangeListener);
		}

		try {
			unregisterReceiver(internalDebugReceiver);
		} catch (Exception e) {
		}
	}

	/**
	 * ˢ��δ����Ϣ��
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * ˢ��������֪ͨ��Ϣ��
	 */
	public void updateUnreadAddressLable() {
		runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadAddressCountTotal();
				if (count > 0) {
					// unreadAddressLable.setText(String.valueOf(count));
					unreadAddressLable.setVisibility(View.VISIBLE);
				} else {
					unreadAddressLable.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	/**
	 * ��ȡδ��������֪ͨ��Ϣ
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (App.getInstance().getContactList()
				.get(Constant.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = App.getInstance().getContactList()
					.get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/**
	 * ��ȡδ����Ϣ��
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		for (EMConversation conversation : EMChatManager.getInstance()
				.getAllConversations().values()) {
			if (conversation.getType() == EMConversationType.ChatRoom)
				chatroomUnreadMsgCount = chatroomUnreadMsgCount
						+ conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal - chatroomUnreadMsgCount;
	}

	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;

	/***
	 * ���ѱ仯listener
	 * 
	 */
	public class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// �������ӵ���ϵ��
			Map<String, User> localUsers = App.getInstance().getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = setUserHead(username);
				// ��Ӻ���ʱ���ܻ�ص�added��������
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// ˢ��ui
			// if (currentTabIndex == 1)
			// contactlistFragment.refresh();

		}

		@Override
		public void onContactDeleted(final List<String> usernameList) {
			// ��ɾ��
			Map<String, User> localUsers = App.getInstance().getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			runOnUiThread(new Runnable() {
				public void run() {
					// �����������û�������ҳ��
					String st10 = getResources().getString(
							R.string.have_you_removed);
					if (ChatActivity.activityInstance != null
							&& usernameList
									.contains(ChatActivity.activityInstance
											.getToChatUsername())) {
						Toast.makeText(
								MainActivity.this,
								ChatActivity.activityInstance
										.getToChatUsername() + st10, 1).show();
						ChatActivity.activityInstance.finish();
					}
					updateUnreadLabel();
					// ˢ��ui
					// contactlistFragment.refresh();
					chatHistoryFragment.refresh();
				}
			});

		}

		@Override
		public void onContactInvited(String username, String reason) {

			// �ӵ��������Ϣ�����������(ͬ���ܾ�)�����ߺ󣬷��������Զ��ٷ����������Կͻ��˲���Ҫ�ظ�����
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null
						&& inviteMessage.getFrom().equals(username)) {
					inviteMessgeDao.deleteMessage(username);
				}
			}
			// �Լ���װ��javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "�������Ϊ����,reason: " + reason);
			// ������Ӧstatus
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// �Լ���װ��javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "ͬ������ĺ�������");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {

			// �ο�ͬ�⣬������ʵ�ִ˹���,demoδʵ��
			Log.d(username, username + "�ܾ�����ĺ�������");
		}

	}

	/**
	 * ���Ӽ���listener
	 * 
	 */
	public class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {
			boolean groupSynced = HXSDKHelper.getInstance()
					.isGroupsSyncedWithServer();
			boolean contactSynced = HXSDKHelper.getInstance()
					.isContactsSyncedWithServer();

			// in case group and contact were already synced, we supposed to
			// notify sdk we are ready to receive the events
			if (groupSynced && contactSynced) {
				new Thread() {
					@Override
					public void run() {
						HXSDKHelper.getInstance().notifyForRecevingEvents();
					}
				}.start();
			} else {
				if (!groupSynced) {
					asyncFetchGroupsFromServer();
				}

				if (!contactSynced) {
					asyncFetchContactsFromServer();
				}

				if (!HXSDKHelper.getInstance().isBlackListSyncedWithServer()) {
					asyncFetchBlackListFromServer();
				}
			}

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					chatHistoryFragment.errorItem.setVisibility(View.GONE);
				}

			});
		}

		@Override
		public void onDisconnected(final int error) {
			final String st1 = getResources().getString(
					R.string.can_not_connect_chat_server_connection);
			final String st2 = getResources().getString(
					R.string.the_current_network);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.USER_REMOVED) {
						// ��ʾ�ʺ��Ѿ����Ƴ�
						showAccountRemovedDialog();
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// ��ʾ�ʺ��������豸��½dialog
						showConflictDialog();
					} else {
						chatHistoryFragment.errorItem
								.setVisibility(View.VISIBLE);
						if (NetUtils.hasNetwork(MainActivity.this))
							chatHistoryFragment.errorText.setText(st1);
						else
							chatHistoryFragment.errorText.setText(st2);

					}
				}

			});
		}
	}

	/**
	 * MyGroupChangeListener
	 */
	public class MyGroupChangeListener implements EMGroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName,
				String inviter, String reason) {

			// boolean hasGroup = false;
			// for (EMGroup group : EMGroupManager.getInstance().getAllGroups())
			// {
			// if (group.getGroupId().equals(groupId)) {
			// hasGroup = true;
			// break;
			// }
			// }
			// if (!hasGroup)
			// return;
			//
			// // ������
			// String st3 = getResources().getString(
			// R.string.Invite_you_to_join_a_group_chat);
			// EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			// msg.setChatType(ChatType.GroupChat);
			// msg.setFrom(inviter);
			// msg.setTo(groupId);
			// msg.setMsgId(UUID.randomUUID().toString());
			// msg.addBody(new TextMessageBody(inviter + " " + st3));
			// // ����������Ϣ
			// EMChatManager.getInstance().saveMessage(msg);
			// // ��������Ϣ
			// HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(msg);
			//
			// runOnUiThread(new Runnable() {
			// public void run() {
			// updateUnreadLabel();
			// // ˢ��ui
			// if (currentTabIndex == 0)
			// chatHistoryFragment.refresh();
			// if (CommonUtils.getTopActivity(MainActivity.this).equals(
			// GroupsActivity.class.getName())) {
			// GroupsActivity.instance.onResume();
			// }
			// }
			// });

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter,
				String reason) {

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee,
				String reason) {

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {

			// ��ʾ�û���T�ˣ�demoʡ�Դ˲���
			// ˢ��ui
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						updateUnreadLabel();
						if (currentTabIndex == 0)
							chatHistoryFragment.refresh();
						if (CommonUtils.getTopActivity(MainActivity.this)
								.equals(GroupsActivity.class.getName())) {
							GroupsActivity.instance.onResume();
						}
					} catch (Exception e) {
						EMLog.e(TAG, "refresh exception " + e.getMessage());
					}
				}
			});
		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {

			// Ⱥ����ɢ
			// ��ʾ�û�Ⱥ����ɢ,demoʡ��
			// ˢ��ui
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					if (currentTabIndex == 0)
						chatHistoryFragment.refresh();
					if (CommonUtils.getTopActivity(MainActivity.this).equals(
							GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onApplicationReceived(String groupId, String groupName,
				String applyer, String reason) {

			// �û��������Ⱥ��
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			Log.d(TAG, applyer + " �������Ⱥ�ģ�" + groupName);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);
		}

		@Override
		public void onApplicationAccept(String groupId, String groupName,
				String accepter) {

			String st4 = getResources().getString(
					R.string.Agreed_to_your_group_chat_application);
			// ��Ⱥ���뱻ͬ��
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(accepter + " " + st4));
			// ����ͬ����Ϣ
			EMChatManager.getInstance().saveMessage(msg);
			// ��������Ϣ
			HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(msg);

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// ˢ��ui
					if (currentTabIndex == 0)
						chatHistoryFragment.refresh();
					if (CommonUtils.getTopActivity(MainActivity.this).equals(
							GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});
		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName,
				String decliner, String reason) {
			// ��Ⱥ���뱻�ܾ���demoδʵ��
		}
	}

	/**
	 * ������ʾ����Ϣ
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// ��ʾ������Ϣ
		HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(null);

		// ˢ��bottom bar��Ϣδ����
		updateUnreadAddressLable();
		// ˢ�º���ҳ��ui
		// if (currentTabIndex == 1)
		// contactlistFragment.refresh();
	}

	/**
	 * ���������msg
	 * 
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// ����msg
		inviteMessgeDao.saveMessage(msg);
		// δ������1
		User user = App.getInstance().getContactList()
				.get(Constant.NEW_FRIENDS_USERNAME);
		if (user.getUnreadMsgCount() == 0)
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * set head
	 * 
	 * @param username
	 * @return
	 */
	User setUserHead(String username) {
		User user = new User();
		user.setUsername(username);
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
		return user;
	}

	@SuppressLint("DefaultLocale")
	@Override
	protected void onResume() {
		super.onResume();

		PUUser curUser = new PreferenceMap(App.ctx).getUser();
		String imgUrl = curUser.getImage();
		if (imgUrl.equals("")) {
			imgUrl = null;
		}
		imageLoader.displayImage(imgUrl, img_menu,
				PhotoUtils.getImageOptions(R.drawable.default_avatar));

		if (!isConflict && !isCurrentAccountRemoved) {
			updateUnreadLabel();
			updateUnreadAddressLable();
			EMChatManager.getInstance().activityResumed();
		}

		// unregister this event listener when this activity enters the
		// background
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.pushActivity(this);

		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventConversationListChanged });
	}

	@Override
	protected void onStop() {
		EMChatManager.getInstance().unregisterEventListener(this);
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.popActivity(this);

		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;
	private BroadcastReceiver internalDebugReceiver;

	/**
	 * ��ʾ�ʺ��ڱ𴦵�¼dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		App.getInstance().logout(null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								conflictBuilder = null;
								finish();
								startActivity(new Intent(MainActivity.this,
										LoginActivity.class));
							}
						});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(TAG,
						"---------color conflictBuilder error" + e.getMessage());
			}

		}

	}

	/**
	 * �ʺű��Ƴ���dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		App.getInstance().logout(null);
		String st5 = getResources().getString(R.string.Remove_the_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								accountRemovedBuilder = null;
								finish();
								startActivity(new Intent(MainActivity.this,
										LoginActivity.class));
							}
						});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(TAG,
						"---------color userRemovedBuilder error"
								+ e.getMessage());
			}

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
	}

	/**
	 * �ڲ����Դ��룬�����������
	 */
	private void registerInternalDebugReceiver() {
		internalDebugReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				App.getInstance().logout(new EMCallBack() {

					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							public void run() {
								// ������ʾ��½ҳ��
								finish();
								startActivity(new Intent(MainActivity.this,
										LoginActivity.class));

							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {
					}

					@Override
					public void onError(int code, String message) {
					}
				});
			}
		};
		IntentFilter filter = new IntentFilter(getPackageName()
				+ ".em_internal_debug");
		registerReceiver(internalDebugReceiver, filter);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// getMenuInflater().inflate(R.menu.context_tab_contact, menu);
	}

	// ������Ӳ໬
	private void setMenu() {
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);

		// slidingmenu���¼�ģʽ����������п��Ի���������TOUCHMODE_MARGIN
		// �ɽ���¼���ͻ����
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		// menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

		// menu��ͼ��Fragment���
		menu.setMenu(R.layout.sliding_menu_menu);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MainMenuFragment()).commit();

		img_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (menu.isMenuShowing()) {
					menu.showContent();
				} else {
					menu.showMenu();
				}
			}
		});

	}

	class MoreChooseClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			initPopuWindow(v);
		}

	}

	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Utils.toast(R.string.Double_quit_app);
				mExitTime = System.currentTimeMillis();
			} else {
				moveTaskToBack(false);
				// finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private ImageView iv_schedule;

	// ����initPopWindow
	private void initPopuWindow(View parent) {
		if (popupWindow == null) {
			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.popuwindow, null);
			popupWindow = new PopupWindow(view,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		ColorDrawable cd = new ColorDrawable(0x000000);
		popupWindow.setBackgroundDrawable(cd);

		// ���������䰵Ч��
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.4f;
		getWindow().setAttributes(lp);

		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.showAtLocation((View) parent.getParent(), Gravity.CENTER
				| Gravity.CENTER_HORIZONTAL, 0, 0);

		popupWindow.update();
		popupWindow.setOnDismissListener(new OnDismissListener() {

			// ��dismiss�лָ�͸����
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});

		ImageView iv_schedule = (ImageView) view.findViewById(R.id.iv_schedule);
		iv_schedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						ScheduleActivity.class);
				startActivity(intent);
			}
		});

		ImageView iv_sign = (ImageView) view.findViewById(R.id.iv_sign);
		iv_sign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		ImageView iv_addCampaign = (ImageView) view
				.findViewById(R.id.iv_addCampaign);
		iv_addCampaign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,
						AddCampaignActivity.class));
			}
		});

	}

	public String getVersion() {
		PackageManager manager;
		PackageInfo info = null;
		manager = this.getPackageManager();
		try {

			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return info.versionName;
	}

	/**
	 * ����
	 */
	public void checkupdate() {

		new SimpleNetTask(ctx) {
			boolean flag;
			String jsonstr;

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if (!flag) {
					return;
				} else {
					try {
						JSONObject json = new JSONObject(jsonstr);
						String des = json.getString("description");
						final String url = json.getString("fileUrl");
						String version = json.getString("version");

						new SweetAlertDialog(ctx)
								.setTitleText("���°汾" + version + "��")
								.setContentText(des)
								.setCancelClickListener(
										new OnSweetClickListener() {
											@Override
											public void onClick(
													SweetAlertDialog sweetAlertDialog) {
												sweetAlertDialog.dismiss();
											}
										})
								.setConfirmText("��������")
								.setConfirmClickListener(
										new OnSweetClickListener() {
											@Override
											public void onClick(
													SweetAlertDialog sweetAlertDialog) {
												sweetAlertDialog.dismiss();
												startActivity(new Intent(
														Intent.ACTION_VIEW, Uri
																.parse(url)));
											}
										}).show();

					} catch (Exception e) {
					}
				}
			}

			@Override
			protected void doInBack() throws Exception {
				param.clear();
				String version = getVersion();
				param.put("apkVersion", version);
				param.put("apkType", "upgrade_Android");
				jsonstr = new WebService(C.UPDATEAPK, param).getReturnInfo();
				flag = GetObjectFromService.getSimplyResult(jsonstr);
			}
		}.execute();
	}
}
