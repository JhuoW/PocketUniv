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
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.utils.UserUtils;
import com.easemob.chatuidemo.video.util.Utils;
import com.easemob.chatuidemo.widget.ExpandGridView;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.NetUtils;
import com.example.pocketuniversity.R;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import com.pocketuniversity.base.C;
import com.pocketuniversity.group.bean.Group;
import com.pocketuniversity.group.bean.GroupMember;
import com.pocketuniversity.group.db.DBMember;
import com.pocketuniversity.group.db.GroupDBHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class GroupDetailsActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = "GroupDetailsActivity";
	private static final int REQUEST_CODE_ADD_USER = 0;
	private static final int REQUEST_CODE_EXIT = 1;
	private static final int REQUEST_CODE_EXIT_DELETE = 2;
	private static final int REQUEST_CODE_CLEAR_ALL_HISTORY = 3;
	private static final int REQUEST_CODE_ADD_TO_BALCKLIST = 4;
	private static final int REQUEST_CODE_EDIT_GROUPNAME = 5;

	String longClickUsername = null;

	private GroupDBHelper groupDBHelper;

	private ExpandGridView userGridview;
	private String groupId;
	private ProgressBar loadingPB;
	private Button exitBtn;
	private Button deleteBtn;
	private EMGroup group;
	private GridAdapter adapter;
	private int referenceWidth;
	private int referenceHeight;
	private ProgressDialog progressDialog;

	private Effectstype effect;

	private MyProgressDialog dialog;
	private RelativeLayout rl_switch_block_groupmsg;
	
	/**
	 * 屏蔽群消息imageView
	 */
	private ImageView iv_switch_block_groupmsg;
	/**
	 * 关闭屏蔽群消息imageview
	 */
	private ImageView iv_switch_unblock_groupmsg;

	public static GroupDetailsActivity instance;

	String st = "";
	// 清空所有有聊天记录
	private RelativeLayout clearAllHistory;
	private RelativeLayout changeGroupNameLayout;
	private RelativeLayout idLayout;
	private RelativeLayout rl_clearHistory;
	private RelativeLayout rl_groupDes;
	private TextView idText;
	private TextView tv_groupname;
	private Group thisgroup;

	List<GroupMember> member = new ArrayList<GroupMember>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 获取传过来的groupid
		groupId = getIntent().getStringExtra("groupId");
		group = EMGroupManager.getInstance().getGroup(groupId);
		// we are not supposed to show the group if we don't find the group
		if (group == null) {
			finish();
			return;
		}
		setContentView(R.layout.activity_group_details);
		instance = this;
		groupDBHelper = new GroupDBHelper(ctx);

		findView();

		List<String> members = new ArrayList<String>();
		member = groupDBHelper.getAllmember();
		// members.addAll(group.getMembers());

		adapter = new GridAdapter(this, R.layout.grid, member);
		userGridview.setAdapter(adapter);
		headerLayout.showTitle("聊天信息" + "(" + member.size() + "人)");

		// 保证每次进详情看到的都是�?新的group
		// updateGroup();

		// 设置OnTouchListener
		userGridview.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (adapter.isInDeleteMode) {
						adapter.isInDeleteMode = false;
						adapter.notifyDataSetChanged();
						return true;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});

		clearAllHistory.setOnClickListener(this);
		changeGroupNameLayout.setOnClickListener(this);

	}

	private void findView() {
		dialog = new MyProgressDialog(ctx);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GroupDetailsActivity.this.finish();
			}
		});
		thisgroup = groupDBHelper.getGroup2(groupId);
		st = getResources().getString(R.string.people);
		clearAllHistory = (RelativeLayout) findViewById(R.id.re_clear);
		userGridview = (ExpandGridView) findViewById(R.id.gridview);
		exitBtn = (Button) findViewById(R.id.btn_exit_grp);
		deleteBtn = (Button) findViewById(R.id.btn_exitdel_grp);
		changeGroupNameLayout = (RelativeLayout) findViewById(R.id.re_change_groupname);
		tv_groupname = (TextView) findViewById(R.id.tv_groupname);
		rl_groupDes = (RelativeLayout) findViewById(R.id.re_change_groupdescription);

		idLayout = (RelativeLayout) findViewById(R.id.re_groupid);
		idLayout.setVisibility(View.VISIBLE);
		idText = (TextView) findViewById(R.id.tv_groupid);

		rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);

		iv_switch_block_groupmsg = (ImageView) findViewById(R.id.iv_switch_block_groupmsg);
		iv_switch_unblock_groupmsg = (ImageView) findViewById(R.id.iv_switch_unblock_groupmsg);

		rl_switch_block_groupmsg.setOnClickListener(this);
		rl_groupDes.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);
		Drawable referenceDrawable = getResources().getDrawable(
				R.drawable.smiley_add_btn);
		referenceWidth = referenceDrawable.getIntrinsicWidth();
		referenceHeight = referenceDrawable.getIntrinsicHeight();

		idText.setText(thisgroup.getId());

		tv_groupname.setText(thisgroup.getGroupName());

		if (group.getOwner() == null || "".equals(group.getOwner())
				|| !group.getOwner().equals(Utils.getID())) {
			exitBtn.setVisibility(View.VISIBLE);
			deleteBtn.setVisibility(View.GONE);
			changeGroupNameLayout.setVisibility(View.VISIBLE);
		}

		// 如果自己是群主，显示解散按钮
		if (EMChatManager.getInstance().getCurrentUser()
				.equals(group.getOwner())) {
			exitBtn.setVisibility(View.GONE);
			deleteBtn.setVisibility(View.VISIBLE);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String st1 = getResources().getString(R.string.being_added);
		String st2 = getResources().getString(R.string.is_quit_the_group_chat);
		String st3 = getResources().getString(R.string.chatting_is_dissolution);
		String st4 = getResources().getString(R.string.are_empty_group_of_news);
		String st5 = getResources()
				.getString(R.string.is_modify_the_group_name);
		final String st6 = getResources().getString(
				R.string.Modify_the_group_name_successful);
		final String st7 = getResources().getString(
				R.string.change_the_group_name_failed_please);
		String st8 = getResources().getString(R.string.Are_moving_to_blacklist);
		final String st9 = getResources().getString(
				R.string.failed_to_move_into);

		final String stsuccess = getResources().getString(
				R.string.Move_into_blacklist_success);
		if (resultCode == RESULT_OK) {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(GroupDetailsActivity.this);
				progressDialog.setMessage(st1);
				progressDialog.setCanceledOnTouchOutside(false);
			}
			switch (requestCode) {
			case REQUEST_CODE_ADD_USER:// 添加群成�?
				final String[] newmembers = data
						.getStringArrayExtra("newmembers");
				progressDialog.setMessage(st1);
				progressDialog.show();
				addMembersToGroup(newmembers);
				break;
			case REQUEST_CODE_EXIT: // �?出群
				progressDialog.setMessage(st2);
				progressDialog.show();
				exitGrop();
				break;
			case REQUEST_CODE_EXIT_DELETE: // 解散�?
				progressDialog.setMessage(st3);
				progressDialog.show();
				deleteGrop();
				break;
			case REQUEST_CODE_CLEAR_ALL_HISTORY:
				// 清空此群聊的聊天记录
				progressDialog.setMessage(st4);
				progressDialog.show();
				clearGroupHistory();
				break;

			case REQUEST_CODE_EDIT_GROUPNAME: // 修改群名�?
				final String returnData = data.getStringExtra("data");
				if (!TextUtils.isEmpty(returnData)) {
					progressDialog.setMessage(st5);
					progressDialog.show();

					new Thread(new Runnable() {
						public void run() {
							try {
								EMGroupManager.getInstance().changeGroupName(
										groupId, returnData);
								runOnUiThread(new Runnable() {
									public void run() {
										// ((TextView)
										// findViewById(R.id.group_name)).setText(returnData
										// + "(" + group.getAffiliationsCount()
										// + st);
										progressDialog.dismiss();
										Toast.makeText(getApplicationContext(),
												st6, 0).show();
									}
								});

							} catch (EaseMobException e) {
								e.printStackTrace();
								runOnUiThread(new Runnable() {
									public void run() {
										progressDialog.dismiss();
										Toast.makeText(getApplicationContext(),
												st7, 0).show();
									}
								});
							}
						}
					}).start();
				}
				break;
			case REQUEST_CODE_ADD_TO_BALCKLIST:
				progressDialog.setMessage(st8);
				progressDialog.show();
				new Thread(new Runnable() {
					public void run() {
						try {
							EMGroupManager.getInstance().blockUser(groupId,
									longClickUsername);
							runOnUiThread(new Runnable() {
								public void run() {
									refreshMembers();
									progressDialog.dismiss();
									Toast.makeText(getApplicationContext(),
											stsuccess, 0).show();
								}
							});
						} catch (EaseMobException e) {
							runOnUiThread(new Runnable() {
								public void run() {
									progressDialog.dismiss();
									Toast.makeText(getApplicationContext(),
											st9, 0).show();
								}
							});
						}
					}
				}).start();

				break;
			default:
				break;
			}
		}
	}

	private void refreshMembers() {
		adapter.clear();
		//
		List<String> members = new ArrayList<String>();
		// members.addAll(group.getMembers());
		// adapter.addAll(members);
		List<GroupMember> member = new ArrayList<GroupMember>();
		member.addAll(groupDBHelper.getAllmember());
		userGridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 点击解散群组按钮
	 * 
	 * @param view
	 */
	public void exitDeleteGroup(View view) {
		startActivityForResult(
				new Intent(this, ExitGroupDialog.class).putExtra("deleteToast",
						getString(R.string.dissolution_group_hint)),
				REQUEST_CODE_EXIT_DELETE);

	}

	/**
	 * 清空群聊天记�?
	 */
	public void clearGroupHistory() {

		EMChatManager.getInstance().clearConversation(group.getGroupId());
		// progressDialog.dismiss();
		// adapter.refresh(EMChatManager.getInstance().getConversation(toChatUsername));

	}

	/**
	 * �?出群�?
	 * 
	 * @param groupId
	 */
	private void exitGrop() {
		String st1 = getResources().getString(
				R.string.Exit_the_group_chat_failure);
		new Thread(new Runnable() {
			public void run() {
				try {
					EMGroupManager.getInstance().exitFromGroup(groupId);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							setResult(RESULT_OK);
							finish();
							if (ChatActivity.activityInstance != null)
								ChatActivity.activityInstance.finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(
									getApplicationContext(),
									getResources()
											.getString(
													R.string.Exit_the_group_chat_failure)
											+ " " + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 解散群组
	 * 
	 * @param groupId
	 */
	private void deleteGrop() {
		final String st5 = getResources().getString(
				R.string.Dissolve_group_chat_tofail);
		new Thread(new Runnable() {
			public void run() {
				try {
					EMGroupManager.getInstance().exitAndDeleteGroup(groupId);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							setResult(RESULT_OK);
							finish();
							if (ChatActivity.activityInstance != null)
								ChatActivity.activityInstance.finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(),
									st5 + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 增加群成�?
	 * 
	 * @param newmembers
	 */
	private void addMembersToGroup(final String[] newmembers) {
		final String st6 = getResources().getString(
				R.string.Add_group_members_fail);
		new Thread(new Runnable() {

			public void run() {
				try {
					// 创建者调用add方法
					if (EMChatManager.getInstance().getCurrentUser()
							.equals(group.getOwner())) {
						EMGroupManager.getInstance().addUsersToGroup(groupId,
								newmembers);
					} else {
						// �?般成员调用invite方法
						EMGroupManager.getInstance().inviteUser(groupId,
								newmembers, null);
					}
					runOnUiThread(new Runnable() {
						public void run() {
							refreshMembers();
							// ((TextView)
							// findViewById(R.id.group_name)).setText(group.getGroupName()
							// + "(" + group.getAffiliationsCount()
							// + st);
							progressDialog.dismiss();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(),
									st6 + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		String st6 = getResources().getString(R.string.Is_unblock);
		final String st7 = getResources().getString(R.string.remove_group_of);
		switch (v.getId()) {
		case R.id.rl_switch_block_groupmsg: // 屏蔽群组
			if (iv_switch_block_groupmsg.getVisibility() == View.VISIBLE) {
				System.out.println("change to unblock group msg");
				try {
					EMGroupManager.getInstance().unblockGroupMessage(groupId);
					iv_switch_block_groupmsg.setVisibility(View.INVISIBLE);
					iv_switch_unblock_groupmsg.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
					// todo: 显示错误给用户
					Utils.toast("取消屏蔽失败");
				}
			} else {
				System.out.println("change to block group msg");
				try {
					EMGroupManager.getInstance().blockGroupMessage(groupId);
					iv_switch_block_groupmsg.setVisibility(View.VISIBLE);
					iv_switch_unblock_groupmsg.setVisibility(View.INVISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
					// todo: 显示错误给用户
					if (group.getOwner().equals(Utils.getID())) {
						Utils.toast("群主无法屏蔽群消息");
						return;
					}
					Utils.toast("屏蔽群组失败");
				}
			}
			break;

		case R.id.re_clear: // 清空聊天记录
			new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE)
					.setTitleText("是否确定清空聊天记录？").showCancelButton(true)
					.setConfirmText("确定").setCancelText("取消")
					.setConfirmClickListener(new OnSweetClickListener() {

						@Override
						public void onClick(
								final SweetAlertDialog sweetAlertDialog) {
							// TODO Auto-generated method stub
							// clearGroupHistory();
							new SimpleNetTask(ctx) {

								@Override
								protected void onSucceed() {
									// TODO Auto-generated method stub
									sweetAlertDialog.dismiss();
									new SweetAlertDialog(ctx,
											SweetAlertDialog.SUCCESS_TYPE)
											.showCancelButton(false)
											.setTitleText("清空群消息成功")
											.setConfirmText("确定")
											.setConfirmClickListener(
													new OnSweetClickListener() {

														@Override
														public void onClick(
																SweetAlertDialog sweetAlertDialog) {
															// TODO
															// Auto-generated
															// method stub
															sweetAlertDialog
																	.dismiss();
														}
													}).show();
								}

								@Override
								protected void doInBack() throws Exception {
									// TODO Auto-generated method stub
									clearGroupHistory();
								}
							}.execute();

						}
					}).setCancelClickListener(new OnSweetClickListener() {

						@Override
						public void onClick(SweetAlertDialog sweetAlertDialog) {
							// TODO Auto-generated method stub
							sweetAlertDialog.dismiss();
						}
					}).show();
			break;

		case R.id.re_change_groupname:
			// startActivityForResult(new Intent(this,
			// EditActivity.class).putExtra("data", group.getGroupName()),
			// REQUEST_CODE_EDIT_GROUPNAME);
			if (!group.getOwner().equals(Utils.getID())) {
				return;
			}
			showNameAlert();
			break;

		case R.id.re_change_groupdescription:
			showDesAlert();
			break;
			
		case R.id.btn_exit_grp:
			Log.i("GroupDetailsActivity", "退出");
			exitGroup();
			break;
		case R.id.btn_exitdel_grp:
			Log.i("GroupDetailsActivity", "解散");
			delGroup();
			break;
		}

	}

	/**
	 * 删除群组
	 */
	private void delGroup(){
		new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE).setTitleText("是否解散群聊？")
		.showCancelButton(true).setConfirmText("确定").setCancelText("取消").setConfirmClickListener(new OnSweetClickListener() {
			
			@Override
			public void onClick(final SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				new SimpleNetTask(ctx , false) {
					boolean flag;
					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						if(flag){
							sweetAlertDialog.dismiss();
							Utils.toast("解散群组成功");
							setResult(RESULT_OK);
							finish();
							if(ChatActivity.activityInstance != null)
							    ChatActivity.activityInstance.finish();
						}else {
							sweetAlertDialog.dismiss();
							Utils.toast("解散群组失败");
						}
					}
					
					@Override
					protected void doInBack() throws Exception {
						// TODO Auto-generated method stub
						param.clear();
						param.put("userID", Utils.getID());
						param.put("groupId", thisgroup.getGroupId());
						param.put("serverGroupId", thisgroup.getId());
						String jsonStr = new WebService(C.DELGROUP, param).getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonStr);
					}
				}.execute();
			}
		}).setCancelClickListener(new OnSweetClickListener() {
			
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		}).show();
	}
	
	/**
	 * 退出群组
	 */
	private void exitGroup(){
		new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE).setTitleText("是否退出群聊？")
		.showCancelButton(true).setConfirmText("确定").setCancelText("取消").setCancelClickListener(new OnSweetClickListener() {
			
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		}).setConfirmClickListener(new OnSweetClickListener() {
			
			@Override
			public void onClick(final SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				new SimpleNetTask(ctx , false) {
					boolean flag;
					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						if(flag){
							sweetAlertDialog.dismiss();
							Utils.toast("退出群组成功");
							setResult(RESULT_OK);
							finish();
							if(ChatActivity.activityInstance != null)
							    ChatActivity.activityInstance.finish();
						}else {
							sweetAlertDialog.dismiss();
							Utils.toast("退出群组失败");
						}
					}
					
					@Override
					protected void doInBack() throws Exception {
						// TODO Auto-generated method stub
						param.clear();
						param.put("userID", Utils.getID());
						param.put("groupId", thisgroup.getGroupId());
						param.put("serverGroupId", thisgroup.getId());
						String jsonStr = new WebService(C.EXITGROUP, param).getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonStr);
					}
				}.execute();
			}
		}).show();
	}
	
	private void showDesAlert() {
		// TODO Auto-generated method stub
		
		new SweetAlertDialog(ctx, SweetAlertDialog.CUSTOM_IMAGE_TYPE).showCancelButton(false)
		.setCustomImage(R.drawable.ic_msg_comment)
		.setTitleText("群简介").setContentText(thisgroup.getDescription()).setConfirmText("确定")
	     .setCancelText("取消").setConfirmClickListener(new OnSweetClickListener() {
			
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		}).show();
		
		
//		final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
//				.getInstance(GroupDetailsActivity.this);
//		effect = Effectstype.RotateBottom;
//		dialogBuilder
//				.withTitle("群简介")
//				.withTitleColor("#FFFFFF")
//				.withDividerColor("#11000000")
//				.withMessage(thisgroup.getDescription())
//				.withMessageColor("#FFFFFF")
//				.withIcon(
//						getResources().getDrawable(
//								R.drawable.to_group_details_normal))
//				.isCancelableOnTouchOutside(true) // def | isCancelable(true)
//				.withDuration(700) // def
//				.withEffect(effect) // def Effectstype.Slidetop
//				.withButton1Text("确定") // def gone
//				.setButton1Click(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						dialogBuilder.dismiss();
//					}
//				}).show();

	}

	/**
	 * 修改群名称AlertDialog
	 */
	private void showNameAlert() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		Window window = dlg.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.social_alertdialog);
		// 设置能弹出输入法
		dlg.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		// 为确认按钮添加事件,执行退出应用操作
		Button ok = (Button) window.findViewById(R.id.btn_ok);
		final EditText ed_name = (EditText) window.findViewById(R.id.ed_name);

		ok.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("ShowToast")
			public void onClick(View v) {
				final String newName = ed_name.getText().toString().trim();
				dialog.show();
				if (TextUtils.isEmpty(newName)) {
					return;
				}
				new SimpleNetTask(ctx) {
					boolean flag;

					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						if (flag) {
							dialog.dismiss();
							Utils.toast("修改群名称成功");
							dlg.cancel();
							tv_groupname.setText(newName);
							thisgroup.setGroupName(newName);
						} else {
							dialog.dismiss();
							Utils.toast("修改群名称失败");
						}
					}

					@Override
					protected void doInBack() throws Exception {
						// TODO Auto-generated method stub
						param.clear();
						param.put("Newgroupname", newName);
						param.put("groupid", groupId);
						param.put("id", thisgroup.getId());
						Log.i("GroupDetailsActivity",
								groupId + "," + thisgroup.getId());
						String jsonStr = new WebService(C.CHANGEGROUPNAME,
								param).getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonStr);
					}
				}.execute();

				dlg.cancel();
			}
		});
		// 关闭alert对话框架
		Button cancel = (Button) window.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});

	}

	/**
	 * 获取群成员列表
	 */
	private void getGroupMember() {
		new SimpleNetTask(ctx, false) {
			List<GroupMember> temp = new ArrayList<GroupMember>();
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				System.out.println("获取群成员成功");
				temp = groupDBHelper.getAllmember();
				member.clear();
				member.addAll(temp);
				adapter.notifyDataSetChanged();
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("serverGroupId", thisgroup.getId());
				String jsonStr = new WebService(C.GETGROUPMEMBERS, param)
						.getReturnInfo();
				JSONObject json = new JSONObject(jsonStr);
				if (json.get("ret").equals("success")) {
					JSONArray jsonarray = json.getJSONArray("memberList");
					groupDBHelper.deleteAllDataFromTable(DBMember.TABLENAME);
					groupDBHelper.insertGroupMember(jsonarray);
				}
			}
		}.execute();

	}

	/**
	 * 群组成员gridadapter
	 * 
	 * @author admin_new
	 * 
	 */
	private class GridAdapter extends ArrayAdapter<GroupMember> {

		private int res;
		public boolean isInDeleteMode;
		private List<GroupMember> objects;

		public GridAdapter(Context context, int textViewResourceId,
				List<GroupMember> objects) {
			super(context, textViewResourceId, objects);
			this.objects = objects;
			res = textViewResourceId;
			isInDeleteMode = false;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(res,
						null);
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.iv_avatar);
				holder.textView = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.badgeDeleteView = (ImageView) convertView
						.findViewById(R.id.badge_delete);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final LinearLayout button = (LinearLayout) convertView
					.findViewById(R.id.button_avatar);
			// �?后一个item，减人按�?
			if (position == getCount() - 1) {
				holder.textView.setText("");
				// 设置成删除按�?
				holder.imageView.setImageResource(R.drawable.smiley_minus_btn);
				// button.setCompoundDrawablesWithIntrinsicBounds(0,
				// R.drawable.smiley_minus_btn, 0, 0);
				// 如果不是创建者或者没有相应权限，不提供加减人按钮
				if (!group.getOwner().equals(
						EMChatManager.getInstance().getCurrentUser())) {
					// if current user is not group admin, hide add/remove btn
					convertView.setVisibility(View.INVISIBLE);
				} else { // 显示删除按钮
					if (isInDeleteMode) {
						// 正处于删除模式下，隐藏删除按�?
						convertView.setVisibility(View.INVISIBLE);
					} else {
						// 正常模式
						convertView.setVisibility(View.VISIBLE);
						convertView.findViewById(R.id.badge_delete)
								.setVisibility(View.INVISIBLE);
					}
					final String st10 = getResources().getString(
							R.string.The_delete_button_is_clicked);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							isInDeleteMode = true;
							notifyDataSetChanged();
						}
					});
				}
			} else if (position == getCount() - 2) { // 添加群组成员按钮
				holder.textView.setText("");
				holder.imageView.setImageResource(R.drawable.smiley_add_btn);
				// button.setCompoundDrawablesWithIntrinsicBounds(0,
				// R.drawable.smiley_add_btn, 0, 0);
				// 如果不是创建者或者没有相应权�?
				if (!group.isAllowInvites()
						&& !group.getOwner().equals(
								EMChatManager.getInstance().getCurrentUser())) {
					// if current user is not group admin, hide add/remove btn
					convertView.setVisibility(View.VISIBLE);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 进入选人页面
							startActivityForResult((new Intent(
									GroupDetailsActivity.this,
									GroupPickContactsActivity.class).putExtra(
									"groupId", groupId)), REQUEST_CODE_ADD_USER);
						}
					});
				} else {
					// 正处于删除模式下,隐藏添加按钮
					if (isInDeleteMode) {
						convertView.setVisibility(View.INVISIBLE);
					} else {
						convertView.setVisibility(View.VISIBLE);
						convertView.findViewById(R.id.badge_delete)
								.setVisibility(View.INVISIBLE);
					}
					final String st11 = getResources().getString(
							R.string.Add_a_button_was_clicked);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							EMLog.d(TAG, st11);
							// 进入选人页面
							startActivityForResult((new Intent(
									GroupDetailsActivity.this,
									GroupPickContactsActivity.class).putExtra(
									"groupId", groupId)), REQUEST_CODE_ADD_USER);
						}
					});
				}
			} else { // 普�?�item，显示群组成�?
				GroupMember gm = getItem(position);
				final String userId = gm.getUserId();
				final String myid = Utils.getID();
				// final String username = getItem(position);
				// String nickname =
				// groupDBHelper.getNicknameByid(gm.getName());
				convertView.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);
				// Drawable avatar =
				// getResources().getDrawable(R.drawable.default_avatar);
				// avatar.setBounds(0, 0, referenceWidth, referenceHeight);
				// button.setCompoundDrawables(null, avatar, null, null);
				holder.textView.setText(gm.getName());
				UserUtils.setUserAvatar6(getContext(), gm.getUserId(),
						holder.imageView);
				// demo群组成员的头像都用默认头像，�?由开发�?�自己去设置头像
				if (isInDeleteMode) {
					// 如果是删除模式下，显示减人图�?
					convertView.findViewById(R.id.badge_delete).setVisibility(
							View.VISIBLE);
				} else {
					convertView.findViewById(R.id.badge_delete).setVisibility(
							View.INVISIBLE);
				}

				final String st12 = getResources().getString(
						R.string.not_delete_myself);
				final String st13 = getResources().getString(
						R.string.Are_removed);
				final String st14 = getResources().getString(
						R.string.Delete_failed);
				final String st15 = getResources().getString(
						R.string.confirm_the_members);
				final SweetAlertDialog dialog = new SweetAlertDialog(ctx,
						SweetAlertDialog.ERROR_TYPE).setTitleText("不能删除自己")
						.setConfirmText("确定")
						.setConfirmClickListener(new OnSweetClickListener() {

							@Override
							public void onClick(
									SweetAlertDialog sweetAlertDialog) {
								// TODO Auto-generated method stub
								sweetAlertDialog.dismiss();
							}
						});
				convertView.findViewById(R.id.badge_delete).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isInDeleteMode) {
							// 如果是删除自己，return
							if (myid.equals(userId)) {
								// startActivity(new
								// Intent(GroupDetailsActivity.this,
								// AlertDialog.class).putExtra("msg", st12));
								dialog.show();
								return;
							}
							if (!NetUtils.hasNetwork(getApplicationContext())) {
								Toast.makeText(
										getApplicationContext(),
										getString(R.string.network_unavailable),
										0).show();
								return;
							}
							deleteMembersFromGroup(userId);
						} else {
							// 正常情况下点击user，可以进入用户详情或者聊天页面等�?
							// startActivity(new
							// Intent(GroupDetailsActivity.this,
							// ChatActivity.class).putExtra("userId",
							// user.getUsername()));

						}
					}

					/**
					 * 删除群成员
					 * 
					 * @param username
					 */
					protected void deleteMembersFromGroup(final String username) {
						new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE)
								.setTitleText("是否确定删除该成员？")
								.setConfirmText("确定")
								.setCancelText("取消")
								.showCancelButton(true)
								.setConfirmClickListener(
										new OnSweetClickListener() {

											@Override
											public void onClick(
													final SweetAlertDialog sweetAlertDialog) {
												// TODO Auto-generated method
												// stub
												new SimpleNetTask(ctx, false) {
													boolean flag;

													@Override
													protected void onSucceed() {
														// TODO Auto-generated
														// method stub
														if (flag) {
															Utils.toast("移除成员成功");
															sweetAlertDialog.dismiss();
															objects.remove(position);
															adapter.notifyDataSetChanged();
															headerLayout.showTitle("聊天信息" + "(" + member.size() + "人)");
														} else {
															Utils.toast("移除群成员失败");
															sweetAlertDialog.dismiss();
														}
													}

													@Override
													protected void doInBack()
															throws Exception {
														// TODO Auto-generated
														// method stub
														param.clear();
														param.put("userID",username);
														param.put("groupId", thisgroup.getGroupId());
														param.put("serverGroupId",thisgroup.getId());
														String jsonStr = new WebService(
																C.DELETEMEMBER,
																param)
																.getReturnInfo();
														flag = GetObjectFromService
																.getSimplyResult(jsonStr);
													}
												}.execute();

											}
										})
								.setCancelClickListener(
										new OnSweetClickListener() {

											@Override
											public void onClick(
													SweetAlertDialog sweetAlertDialog) {
												// TODO Auto-generated method
												// stub
												sweetAlertDialog.dismiss();
											}
										}).show();

					}
				});

				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(GroupDetailsActivity.this,
								UserDetailActivity.class);
						intent.putExtra("userId", getItem(position).getUserId());
						startActivity(intent);
					}
				});

			}
			return convertView;
		}

		@Override
		public int getCount() {
			return super.getCount() + 2;
		}
	}

	protected void updateGroup() {
		new Thread(new Runnable() {
			public void run() {
				try {
					final EMGroup returnGroup = EMGroupManager.getInstance()
							.getGroupFromServer(groupId);
					// 更新本地数据
					EMGroupManager.getInstance().createOrUpdateLocalGroup(
							returnGroup);

					runOnUiThread(new Runnable() {
						public void run() {
							// ((TextView)
							// findViewById(R.id.group_name)).setText(group.getGroupName()
							// + "(" + group.getAffiliationsCount()
							// + ")");
							loadingPB.setVisibility(View.INVISIBLE);
							refreshMembers();
							if (EMChatManager.getInstance().getCurrentUser()
									.equals(group.getOwner())) {
								// 显示解散按钮
								exitBtn.setVisibility(View.GONE);
								deleteBtn.setVisibility(View.VISIBLE);
							} else {
								// 显示�?出按�?
								exitBtn.setVisibility(View.VISIBLE);
								deleteBtn.setVisibility(View.GONE);
							}

							// update block
							EMLog.d(TAG,
									"group msg is blocked:"
											+ group.getMsgBlocked());
							if (group.isMsgBlocked()) {
								iv_switch_block_groupmsg
										.setVisibility(View.VISIBLE);
								iv_switch_unblock_groupmsg
										.setVisibility(View.INVISIBLE);
							} else {
								iv_switch_block_groupmsg
										.setVisibility(View.INVISIBLE);
								iv_switch_unblock_groupmsg
										.setVisibility(View.VISIBLE);
							}
						}
					});

				} catch (Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							loadingPB.setVisibility(View.INVISIBLE);
						}
					});
				}
			}
		}).start();
	}

	public void back(View view) {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	private static class ViewHolder {
		ImageView imageView;
		TextView textView;
		ImageView badgeDeleteView;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getGroupMember();
		headerLayout.showTitle("聊天信息" + "(" + member.size() + "人)");
	}

}
