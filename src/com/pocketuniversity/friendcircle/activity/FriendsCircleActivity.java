package com.pocketuniversity.friendcircle.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import u.aly.db;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.activity.UserDetailActivity;
import com.pocketuniversity.adapter.FriendAdapter;
import com.pocketuniversity.adapter.FriendCommentAdapter;
import com.pocketuniversity.adapter.NoScrollGridAdapter;
import com.pocketuniversity.adapter.FriendAdapter.ViewHolder;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Comment;
import com.pocketuniversity.bean.FriendsCircle;
import com.pocketuniversity.bean.InnerComment;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.friendscircle.db.FriendsCircleDBHelper;
import com.pocketuniversity.friendscircle.db.FriendsCircleTable;
import com.pocketuniversity.friendscircle.db.FriendsCommentDB;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.ui.xlist.XListView;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.utils.StringUtils;
import com.pocketuniversity.view.HeaderLayout;

public class FriendsCircleActivity extends BaseActivity implements
		OnItemClickListener, OnItemLongClickListener, OnClickListener,
		XListView.IXListViewListener {

	private XListView listView;
	private int pageSize = 10;
	private int currentPage = 1;

	// private View headView;// 头部view
	private ImageView userPhoto;// 头像头像
	private TextView userName;// 用户名字
	private LinearLayout topLayout;
	private LinearLayout myDynamic;
	ImageLoader imageLoader = ImageLoader.getInstance();

	RelativeLayout headView;

	FriendAdapter adapter;
	HeaderLayout headerLayout;
	PUUser puUser;
	List<FriendsCircle> friendsCircleList = new ArrayList<FriendsCircle>();
	FriendsCircleDBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_group_activity);
		dbHelper = new FriendsCircleDBHelper(ctx);
		initView();
		setListHeader();
		onRefresh();
		// getData(false);
	}

	private void initView() {
		puUser = new PreferenceMap(FriendsCircleActivity.this).getUser();
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("朋友圈");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FriendsCircleActivity.this.finish();
			}
		});
		headerLayout.showRightTextButton("发动态", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.goActivity(FriendsCircleActivity.this,
						PublishFriendsCircleActivity.class);
			}
		});
		topLayout = (LinearLayout) findViewById(R.id.friend_circle);
		listView = (XListView) findViewById(R.id.friend_list);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
		adapter = new FriendAdapter(this, friendsCircleList,
				getLayoutInflater());
		listView.setAdapter(adapter);
		headView = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.friend_head_item, null);
		headView.setOnClickListener(this);
		userPhoto = (ImageView) headView.findViewById(R.id.user_photo);
		userName = (TextView) headView.findViewById(R.id.user_name);
		myDynamic = (LinearLayout) headView.findViewById(R.id.top_bg);

		userPhoto.setOnClickListener(this);
		userName.setOnClickListener(this);
		myDynamic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.goActivity(FriendsCircleActivity.this,
						MyDynamicActivity.class);
			}
		});
		listView.addHeaderView(headView);
		listView.setHeaderDividersEnabled(false);
		listView.setFooterDividersEnabled(false);
	}

	private void setListHeader() {
		imageLoader.displayImage(puUser.getImage(), userPhoto,
				PhotoUtils.getImageOptions(R.drawable.user_logo));
		userName.setText(puUser.getName());
	}

	public void getData(boolean isLoad) {
		new SimpleNetTask(FriendsCircleActivity.this) {
			List<FriendsCircle> temp = new ArrayList<FriendsCircle>();

			@Override
			protected void onSucceed() {
				listView.stopRefresh();
				listView.stopLoadMore();
				if (temp == null) {
					Utils.toast("net wrong");
					return;
				}
				// friendsCircleList.clear();
				// friendsCircleList.addAll(temp);
				List<FriendsCircle> list;
				list = dbHelper.queryFriendsCircle(currentPage * pageSize + "");
				if (list != null) {
					friendsCircleList.clear();
					friendsCircleList.addAll(list);
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
				String jsonStr = new WebService(C.GETFRIENDCIRCLELIST, param)
						.getReturnInfo();
				// temp = GetObjectFromService.getFriendsCircleList(jsonStr);
				JSONObject json = new JSONObject(jsonStr);
				if (json.get("ret").equals("success")) {
					JSONArray jsonarray = json
							.getJSONArray("friendsCircleList");
					dbHelper.insertPosts(jsonarray);
				}
			}
		}.execute();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// onRefresh();
	}

	@Override
	public void onRefresh() {
		currentPage = 1;
		System.out.println("刷新");
		dbHelper.deleteAllDataFromTable(FriendsCircleTable.TABLE_NAME);
		getData(false);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		currentPage += 1;
		System.out.println("刷新");
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
		Intent intent = new Intent(FriendsCircleActivity.this,
				FriendsCircleDetailActivity.class);
		intent.putExtra("post", friendsCircleList.get(position - 2));
		intent.putExtra("position", position + "");
		startActivityForResult(intent,1000);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent datas) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, datas);
//		if (resultCode != 1001||requestCode != 1000) {
//			System.out.println("回调未调用");
//			return;
//		}
		if (resultCode != 1001||requestCode != 1000) {
			System.out.println("回调未调用");
			return;
		}
		System.out.println("回调调用");
		String position = datas.getExtras().getString("position");
		int itemIndex = Integer.parseInt(position);
		String id = datas.getExtras().getString("id");
		System.out.println("itemIndex:"+itemIndex + ",id:"+id);
		updateView(itemIndex, id);
	}

	public void updateView(final int itemIndex, final String id) {
		// 得到第一个可显示控件的位置，
		int firstVisiblePosition = listView.getFirstVisiblePosition();
		int lastVisiblePosition = listView.getLastVisiblePosition();
		// 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
		if (itemIndex >= firstVisiblePosition
				&& itemIndex <= lastVisiblePosition) {
			// 得到要更新的item的view
			View view = listView.getChildAt(itemIndex - firstVisiblePosition);
			// 从view中取得holder
			if (view.getTag() instanceof ViewHolder) {
				final ViewHolder holder = (ViewHolder) view.getTag();
				new SimpleNetTask(ctx) {
					JSONArray jsonarray = null;

					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						try {
							for (int k = 0; k < jsonarray.length(); k++) {

								JSONObject obj = jsonarray.getJSONObject(k);
								String commentCount = obj
										.getString("commentCount");
								String content = obj.getString("content");
								String header = obj.getString("header");
								String imgUrl = obj.getString("imgUrl");
								String name = obj.getString("name");
								String time = obj.getString("time");
								String zanCount = obj.getString("zanCount");
								String isZan = obj.getString("isZan");
								final String id = obj.getString("id");
								String userId = obj.getString("userId");
								holder.name.setText(name);
								holder.time.setText(time);
								holder.content.setText(content);
								holder.commentCount.setText(commentCount);
								holder.zancount.setText(zanCount);
								ImageLoader
										.getInstance()
										.displayImage(
												header,
												holder.header,
												new DisplayImageOptions.Builder()
														.cacheInMemory()
														.cacheOnDisc()
														.showImageForEmptyUri(
																R.drawable.ic_default_head)
														.showImageOnFail(
																R.drawable.ic_default_head)
														.build());
								friendsCircleList.get(itemIndex-2).setCommentCount(commentCount);
								friendsCircleList.get(itemIndex-2).setContent(content);
								friendsCircleList.get(itemIndex-2).setHeader(header);
								friendsCircleList.get(itemIndex-2).setId(id);
								friendsCircleList.get(itemIndex-2).setImgUrl(imgUrl);
								friendsCircleList.get(itemIndex-2).setIsZan(isZan);
								friendsCircleList.get(itemIndex-2).setName(name);
								friendsCircleList.get(itemIndex-2).setTime(time);
								friendsCircleList.get(itemIndex-2).setUserId(userId);
								friendsCircleList.get(itemIndex-2).setZanCount(zanCount);

								String s1 = StringUtils.trimFirstAndLastChar(
										imgUrl, "[".charAt(0));
								String s2 = StringUtils.trimFirstAndLastChar(
										s1, "]".charAt(0));
								String s4;
								final ArrayList<String> list = new ArrayList<String>();
								String[] stringArr = s2.split(",");
								if (imgUrl.equals("[\"\"]")) {

								} else {
									for (int i = 0; i < stringArr.length; i++) {
										String s = stringArr[i];
										String s3 = StringUtils
												.trimFirstAndLastChar(s,
														"\"".charAt(0));
										s4 = StringUtils.trimFirstAndLastChar(
												s3, "\"".charAt(0));
										System.out.println("s4:" + s4);
										String s5 = s4.replace("\\", "");
										list.add(s5);
									}
								}

								if (TextUtils.isEmpty(imgUrl)) {
									holder.gridview.setVisibility(View.GONE);
								} else {
									holder.gridview
											.setAdapter(new NoScrollGridAdapter(
													FriendsCircleActivity.this,
													list));
								}
								// 点击回帖九宫格，查看大图
								holder.gridview
										.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(
													AdapterView<?> parent,
													View view, int position,
													long id) {
												// TODO Auto-generated method
												// stub
												imageBrower(position, list);
											}
										});

								if (friendsCircleList.get(itemIndex-2).getIsZan().equals("true")) {
									holder.img_thumb
											.setImageResource(R.drawable.ic_thumb_checked);
									friendsCircleList.get(itemIndex-2).setIsZan("true");
								} else {
									holder.img_thumb
											.setImageResource(R.drawable.ic_thumb_uncheck);
									friendsCircleList.get(itemIndex-2).setIsZan("false");
								}

								System.out.println("id:" + id);
								holder.ll_thumb
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												new SimpleNetTask(
														FriendsCircleActivity.this) {
													boolean temp;
													String jsonStr;

													@Override
													protected void onSucceed() {
														if (temp) {
															if (friendsCircleList.get(itemIndex-2)
																	.getIsZan()
																	.equals("true")) {
																holder.img_thumb
																		.setImageResource(R.drawable.ic_thumb_uncheck);
																int zanCount = Integer
																		.parseInt(holder.zancount
																				.getText()
																				.toString());
																int newZanCount = zanCount - 1;
																holder.zancount
																		.setText(newZanCount
																				+ "");
																friendsCircleList.get(itemIndex-2)
																		.setIsZan("false");
																
															} else {
																holder.img_thumb
																		.setImageResource(R.drawable.ic_thumb_checked);
																int zanCount = Integer
																		.parseInt(holder.zancount
																				.getText()
																				.toString());
																int newZanCount = zanCount + 1;
																holder.zancount
																		.setText(newZanCount
																				+ "");
																friendsCircleList.get(itemIndex-2)
																		.setIsZan("true");
															}
														}else {
															Utils.showtoast(FriendsCircleActivity.this, R.drawable.tips_error, "点赞失败，请重试");
														}
													}

													@Override
													protected void doInBack()
															throws Exception {
														// TODO Auto-generated
														// method stub
														if (friendsCircleList.get(itemIndex-2)
																.getIsZan()
																.equals("true")) {
															jsonStr = new APIHelper()
																	.CancelZan(id);

														} else {
															jsonStr = new APIHelper()
																	.AddZan(id);
														}
														temp = GetObjectFromService
																.getSimplyResult(jsonStr);
													}
												}.execute();
											}
										});

								holder.header
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub
												Intent intent = new Intent(
														FriendsCircleActivity.this,
														UserDetailActivity.class);
												intent.putExtra("userId",
														friendsCircleList.get(itemIndex-2)
																.getUserId());
												startActivity(intent);
											}
										});

								final InnerComment innerComment = new InnerComment();
								FriendCommentAdapter adapter;
								final List<Comment> commentdata = new ArrayList<Comment>();
								adapter = new FriendCommentAdapter(commentdata,
										FriendsCircleActivity.this);
								innerComment.setAdapter(adapter);
								holder.commentList.setAdapter(innerComment
										.getAdapter());
								final int pageSize = 5;
								final int currentPage = 1;

								new SimpleNetTask(FriendsCircleActivity.this) {
									List<Comment> temp = new ArrayList<Comment>();

									@Override
									protected void onSucceed() {
										temp = dbHelper
												.queryCommentDynamic(currentPage
														* pageSize + "");
										commentdata.clear();
										commentdata.addAll(temp);
										// adapter.notifyDataSetChanged();
										FriendCommentAdapter adapter = new FriendCommentAdapter(
												commentdata,
												FriendsCircleActivity.this);
										innerComment.setAdapter(adapter);
										holder.commentList
												.setAdapter(innerComment
														.getAdapter());
										System.out.println("长度：" + temp.size());
										// 判断是否有回复内容
										if (listIsEmpty(commentdata)) {
											holder.replyLayout
													.setVisibility(View.GONE);
										} else {
											holder.replyLayout
													.setVisibility(View.VISIBLE);
										}
									}

									@Override
									protected void doInBack() throws Exception {
										// TODO Auto-generated method stub
										Map<String, String> params = new HashMap<String, String>();
										params.put("id", id);
										params.put("currentPage", currentPage
												+ "");
										params.put("currentPageSize", pageSize
												+ "");
										String jsonStr = new WebService(
												C.GETFRIENDCIRCLECOMMENT,
												params).getReturnInfo();
										JSONObject json = new JSONObject(
												jsonStr);
										if (json.get("ret").equals("success")) {
											JSONArray jsonarray = json
													.getJSONArray("comments");
											dbHelper.deleteAllDataFromTable(FriendsCommentDB.TABLE_NAME);
											dbHelper.insertComment(jsonarray);
										}
									}
								}.execute();
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}

					@Override
					protected void doInBack() throws Exception {
						// TODO Auto-generated method stub
						param.clear();
						param.put("id", id);
						param.put("userID", Utils.getID());
						String jsonStr = new WebService(
								C.UPDATEFRIENDCIRCLEITEM, param)
								.getReturnInfo();
						JSONObject json = new JSONObject(jsonStr);
						if (json.get("ret").equals("success")) {
							jsonarray = json.getJSONArray("friendsCircleList");
						}
					}
				}.execute();
			}
		}
	}

	
	private boolean listIsEmpty(List list) {
		if (list != null && list.size() > 0)
			return false;
		return true;

	}
	
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}
	
}
