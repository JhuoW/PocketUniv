package com.pocketuniversity.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.service.UserService;
import com.pocketuniversity.view.MyProgressDialog;

public class LoginActivity extends BaseEntryActivity implements OnClickListener {
	private EditText usernameEdit, passwordEdit;
	private Button loginBtn;
	private TextView registerBtn, login_forgetpassword;
	private ImageView image_password, login_remenber_password_image;
	private View login_remenber_password;
	private String account, password;
	private PreferenceMap accountPre;

	MyProgressDialog dialog;

	private boolean autoLogin = false;

	private final String TAG = "LoginActivity";

	private boolean canseePassword = false;
	private boolean remenberAccount = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// 如果用户名密码都有，直接进入主页面
		if (DemoHXSDKHelper.getInstance().isLogined()) {
			autoLogin = true;
			startActivity(new Intent(LoginActivity.this, MainActivity.class));

			return;
		}
		accountPre = new PreferenceMap(ctx);
		initView();
		initData();
		initAction();
	}

	private void initView() {
		usernameEdit = (EditText) findViewById(R.id.et_username);
		passwordEdit = (EditText) findViewById(R.id.et_password);
		loginBtn = (Button) findViewById(R.id.btn_login);
		registerBtn = (TextView) findViewById(R.id.btn_register);
		registerBtn.setVisibility(View.GONE);
		login_forgetpassword = (TextView) findViewById(R.id.login_forgetpassword);
		image_password = (ImageView) findViewById(R.id.image_password);
		login_remenber_password_image = (ImageView) findViewById(R.id.login_remenber_password_image);
		login_remenber_password = findViewById(R.id.login_remenber_password);
		dialog = new MyProgressDialog(ctx);

		image_password.setOnClickListener(this);
		login_remenber_password.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		login_forgetpassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == registerBtn) {
			Utils.goActivity(ctx, RegistActivity.class);
		} else if (v == image_password) {
			if (canseePassword) {
				image_password
						.setImageResource(R.drawable.ic_login_input_password_false);
				passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD); // 设置输入密码可见
				canseePassword = false;
			} else {
				image_password
						.setImageResource(R.drawable.ic_login_input_password_true);
				passwordEdit // 设置输入密码不可见
						.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				canseePassword = true;
			}
			Utils.setEditTextLastPosition(passwordEdit); // 设置光标更随
		} else if (v == login_remenber_password) {
			if (remenberAccount) {
				login_remenber_password_image
						.setImageResource(R.drawable.ic_register_agree_false);
				remenberAccount = false;
			} else {
				login_remenber_password_image
						.setImageResource(R.drawable.ic_register_agree_true);
				remenberAccount = true;
			}
		} else if (v == login_forgetpassword) {
			Utils.goActivity(LoginActivity.this, ForgetPasswordActivity.class);
		} else {
			login();
		}
	}

	private void login() {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Utils.toast("网络不可用");
			return;
		}

		account = usernameEdit.getText().toString().trim();
		password = passwordEdit.getText().toString().trim();

		if (TextUtils.isEmpty(account)) {
			Utils.toast("用户名不能为空");
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Utils.toast("密码不能为空");
			return;
		}

		accountPre.setIsRemenberAccount(remenberAccount);

		if (remenberAccount) {
			accountPre.setAccount(account);
			accountPre.setPassword(password);
		} else {
			accountPre.setAccount("");
			accountPre.setPassword("");
		}
		// WebService Map
		param.clear();
		param.put("username", account);
		param.put("password", password);
		dialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {

				
				//String result = "success";

				String jsonStr = new WebService(C.LOGIN, param).getReturnInfo(); // 返回值应为个人信息
				
				Log.i(TAG, "返回的个人信息jsonStr ------ > " + jsonStr);
				/*
				 *返回值：{"ret":"success","ID":1,"sex":"男","nickName":"1311440140","authority":"学生","imgUrl":"1","address":"13本物联网班"}
				 */
				// String result = JsonUtils2.getValue(jsonStr,
				// "ret").toString();

				Map<String, Boolean> result = GetObjectFromService
						.getLoginResult(jsonStr);

				Log.i(TAG, "result ------ > " + result);

				try {
					List<PUUser> friend = UserService.findFriends();
					Map<String, User> usermap = App.getInstance()
							.getContactList();
					for (int i = 0; i < friend.size(); i++) {
						PUUser puUser = friend.get(i);
						User user = new User();
						user.setAvatar(puUser.getImage());
						user.setNick(puUser.getName());
						user.setUsername(puUser.getID());   //9
						System.out.println("setUsername--->+getUsername--->" + puUser.getID()+" "+user.getUsername()+" "+user.getNick());
						usermap.put(puUser.getID(), user);   
					}

					App.getInstance().setContactList(usermap);
					UserDao dao = new UserDao(ctx);
					List<User> users = new ArrayList<User>(usermap.values());
					dao.saveContactList(users);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Message msg = handler.obtainMessage();
				msg.obj = result;
				handler.sendMessage(msg);

			}
		}).start();

		// new LoginAsyncTask().execute();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			Map<String , Boolean> result = (Map<String, Boolean>) msg.obj;
			
			if (result.get("result")) {
				// final String passwordstr = MD5.getMD5(password.getBytes());
				// // 获取密码s
				// System.out.println("passwordstr --- >" + passwordstr);
				System.out.println("username --- >" + account);

				EMChatManager.getInstance().login(account, password,
						new EMCallBack() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub

								// 登陆成功，保存用户名密码
								App.getInstance().setUserName(account);
								App.getInstance().setPassword(password);

								try {
									EMGroupManager.getInstance()
											.loadAllGroups();
									EMChatManager.getInstance()
											.loadAllConversations();
									initializeContacts();
								} catch (Exception e) {
									e.printStackTrace();
								}
								// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
								EMChatManager.getInstance()
										.updateCurrentUserNick(
												accountPre.getUser().getUserId());
								if (dialog != null && dialog.isShowing()) {
									dialog.dismiss();
									MainActivity
											.goMainActivity(LoginActivity.this);
									finish();
									// Looper.prepare();
									// Utils.toast("登录成功");
									// Looper.loop();
								}

							}

							@Override
							public void onProgress(int arg0, String arg1) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onError(int arg0, String arg1) {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(), "登录失败",
										Toast.LENGTH_SHORT).show();
							}
						});
			} else {
				Utils.toast("登录失败");
				dialog.dismiss();
			}

		};
	};

	public void initAction() {

		// 如果用户名改变，清空密码
		usernameEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				passwordEdit.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	public void initData() {
		if (accountPre.isRemenberAccount()) {
			usernameEdit.setText(accountPre.getAccount());
			passwordEdit.setText(accountPre.getPassword());
			login_remenber_password_image
					.setImageResource(R.drawable.ic_register_agree_true);
			remenberAccount = true;
			Utils.setEditTextLastPosition(usernameEdit);
		}
	}

	private void initializeContacts() {
		Map<String, User> userlist = new HashMap<String, User>();
		// 添加user"申请与通知"
		User newFriends = new User();
		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
		String strChat = getResources().getString(
				R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
		// 添加"群聊"
		User groupUser = new User();
		String strGroup = getResources().getString(R.string.group_chat);
		groupUser.setUsername(Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(Constant.GROUP_USERNAME, groupUser);

		// 添加"Robot"
		User robotUser = new User();
		String strRobot = getResources().getString(R.string.robot_chat);
		robotUser.setUsername(Constant.CHAT_ROBOT);
		robotUser.setNick(strRobot);
		robotUser.setHeader("");
		userlist.put(Constant.CHAT_ROBOT, robotUser);

		// 存入内存
		App.getInstance().setContactList(userlist);
		// 存入db
		UserDao dao = new UserDao(LoginActivity.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (autoLogin) {
			return;
		}
	}

}
