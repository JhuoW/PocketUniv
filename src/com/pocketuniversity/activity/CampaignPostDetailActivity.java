package com.pocketuniversity.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.adapter.CommentListAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.CampaignJoiner;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.bean.Comment;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.JsonUtils2;
import com.pocketuniversity.utils.NetAsyncTask;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;
import com.pocketuniversity.view.RoundImageView;

/**
 * 接口：1.获取评论 2.收藏 3.取消收藏 4.参与活动 5.取消参与活动 6.获取活动详情
 * 
 * @author Administrator
 * 
 */
public class CampaignPostDetailActivity extends BaseActivity {

	// private static String campaignUrl =
	// "http://192.168.1.118:8087/web-activity-visit-clean.aspx?nid=";

	private Button btn_meJoin;
	private View contentView;
	// private WebView webView;
	private ImageView ivComment, blog_iscollection;
	private CampaignPostModel postModel;
	private ScrollView scrollView;
	private HeaderLayout headerLayout;
	private ListView comment_list;
	private CommentListAdapter adapter;
	private List<Comment> commentdata = new ArrayList<Comment>();
	private Map<String, String> param = new HashMap<String, String>();
	private EditText blog_comment;
	private EditText et_phone;
	private TextView nowJoinNum;
	private TextView tvDetail;
	String compaignDetail;

	List<String> list;
	private String[] headers;

	private TextView tv_collection;
	private TextView title;
	private TextView time;
	private TextView place;
	private ImageView img_campaign;
	ImageLoader imageLoader = ImageLoader.getInstance();

	private List<CampaignJoiner> joinerList;
	String[] headerlist;

	private RoundImageView header;
	private TextView tv_name;
	private TextView tv_time;
	
	String authorheader;
	
	String postId;
	MyProgressDialog dialog;
	CampaignPostModel curPost;
	boolean iscollection = false;
	int curCommentPage = 1;
	int curCommentCount = 40;

	private RoundImageView header1;
	private RoundImageView header2;
	private RoundImageView header3;
	private RoundImageView header4;
	private RoundImageView header5;
	private RoundImageView header6;

	
	private RoundImageView more;
	private RoundImageView[] mImageView;
	PUUser curUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("CampaignPostDetailActivity", "oncreat");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_campaign_post_detail_act);
		curUser = new PreferenceMap(ctx).getUser();
		initView();
		Intent intent = getIntent();
		if (setData(intent)) {
			dialog.show();
			getCampaignDetail();
			// 获取这条id为CampaignID的新闻的所有评论
			param.clear();
			param.put("CampaignID", curPost.getId());
			param.put("page", curCommentPage + "");
			param.put("count", curCommentCount + "");
			initData();
			getCampaignJoiner();
		} else {
			startActivity(new Intent(this, MainActivity.class));
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			finish();
		}
	}

	private void getCampaignDetail() {
		new SimpleNetTask(ctx, false) {

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				dialog.dismiss();
				String html = Html.fromHtml(compaignDetail).toString();
				tvDetail.setText(html);
				imageLoader.displayImage(authorheader,
						header, PhotoUtils
								.getImageOptions(R.drawable.ic_default_head));
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				dialog.show();
				Map<String, String> param = new HashMap<String, String>();
				param.put("campaignID", curPost.getId());
				String jsonStr = new WebService(C.GETCAMPAIGNDETAIL, param)
						.getReturnInfo().toString();
				compaignDetail = JsonUtils2.getValue(jsonStr, "campaignDetail")
						.toString();
				authorheader = JsonUtils2.getValue(jsonStr, "imgUrl").toString();
			}
		}.execute();

	}

	private void initData() {
		new SimpleNetTask(ctx, false) {

			@Override
			protected void onSucceed() {
				adapter = new CommentListAdapter(commentdata, ctx);
				comment_list.setAdapter(adapter);
				setListViewHeightBasedOnChildren(comment_list);
			}

			@Override
			protected void doInBack() throws Exception {
				String jsonstr = "";
				if (curPost.getChannel_id() != null
						&& curPost.getChannel_id().equals("-1")) {
					// 获取评论
					jsonstr = new WebService(C.GETCAMPAIGNCOMMENT, param)
							.getReturnInfo();
				} else {
					jsonstr = new WebService(C.GETCAMPAIGNCOMMENT, param)
							.getReturnInfo();
				}
				commentdata = GetObjectFromService.getCommentList(jsonstr);
			}
		}.execute();
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int singleHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			singleHeight = listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount()));
		params.height += singleHeight;
		listView.setLayoutParams(params);
	}

	private void initView() {

		header1 = (RoundImageView) findViewById(R.id.header1);
		header2 = (RoundImageView) findViewById(R.id.header2);
		header3 = (RoundImageView) findViewById(R.id.header3);
		header4 = (RoundImageView) findViewById(R.id.header4);
		header5 = (RoundImageView) findViewById(R.id.header5);
		header6 = (RoundImageView) findViewById(R.id.header6);
		mImageView = new RoundImageView[] { header1, header2, header3, header4,
				header5, header6 };
		more = (RoundImageView) findViewById(R.id.header7);
		header = (RoundImageView) findViewById(R.id.header);
		tv_name = (TextView) findViewById(R.id.name);
		tv_time = (TextView) findViewById(R.id.time);
		tv_collection = (TextView) findViewById(R.id.tv_collection);
		dialog = new MyProgressDialog(ctx);
		btn_meJoin = (Button) findViewById(R.id.btn_meJoin);
		blog_iscollection = (ImageView) findViewById(R.id.blog_iscollection);
		blog_comment = (EditText) findViewById(R.id.blog_comment);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_phone.setInputType(InputType.TYPE_CLASS_PHONE);// 电话
		comment_list = (ListView) findViewById(R.id.comment_list);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		title = (TextView) findViewById(R.id.tvTitle);
		time = (TextView) findViewById(R.id.tvExcept);
		place = (TextView) findViewById(R.id.tvplace);
		img_campaign = (ImageView) findViewById(R.id.img_campaign);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		nowJoinNum = (TextView) findViewById(R.id.nowJoinNum);
		tvDetail = (TextView) findViewById(R.id.tvdetail);
		// webView = (WebView) findViewById(R.id.webView);
		// setWebSeeting(webView);
		blog_comment.requestFocus();

		btn_meJoin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(et_phone.getText().toString())) {
					Toast.makeText(getApplicationContext(), "手机号不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}

				/*
				 * 点击参与活动
				 */
				new SweetAlertDialog(CampaignPostDetailActivity.this,
						SweetAlertDialog.NORMAL_TYPE)
						.setTitleText("是否确定参加该活动？")
						.setConfirmClickListener(new OnSweetClickListener() {

							@Override
							public void onClick(
									final SweetAlertDialog sweetAlertDialog) {
								// TODO Auto-generated method stub

								new NetAsyncTask(
										CampaignPostDetailActivity.this, false) {
									boolean result = false;

									@Override
									protected void onPost(Exception e) {
										// TODO Auto-generated method stub
										if (result) {
											Log.i("CampaignPostDetail", "点击2");
											sweetAlertDialog.dismiss();
											new SweetAlertDialog(
													CampaignPostDetailActivity.this , SweetAlertDialog.SUCCESS_TYPE)
													.setTitleText("完成")
													.setContentText("已参加此活动")
													.setConfirmText("确定")
													.showCancelButton(false)
													.setConfirmClickListener(
															new OnSweetClickListener() {
																
																@Override
																public void onClick(SweetAlertDialog sweetAlertDialog) {
																	// TODO Auto-generated method stub
																	sweetAlertDialog.dismiss();
																}
															}).show();
												
											Log.i("CampaignPostDetail", "点击3");

											btn_meJoin.setText("已参加");
											btn_meJoin
													.setBackgroundColor(Color.GRAY);
											btn_meJoin.setClickable(false);
											et_phone.setText("");
										
											sweetAlertDialog.dismiss();
											String joinNum = curPost.gethasJoin();
											int i = Integer.parseInt(joinNum);
											int k = i+1;
											nowJoinNum.setText(k+"");
											if(list.size()<6){
											mImageView[list.size()].setVisibility(View.VISIBLE);
											list.add(0,curUser.getImage());
											System.out.println("数量："+list.size());
											for(int m = 0 ;m<list.size() ; m++){
												String imgUrl = list.get(m);
												imageLoader.displayImage(imgUrl,
														mImageView[m], PhotoUtils
																.getImageOptions(R.drawable.ic_default_head));
											}
											for(int n = list.size();n<6;n++){
												mImageView[n].setVisibility(View.INVISIBLE);
											}
											}else {
												mImageView[list.size()-1].setVisibility(View.VISIBLE);
												list.add(0,curUser.getImage());
												list.remove(list.size()-1);
												for(int m = 0 ;m<list.size() ; m++){
													String imgUrl = list.get(m);
													imageLoader.displayImage(imgUrl,
															mImageView[m], PhotoUtils
																	.getImageOptions(R.drawable.ic_default_head));
												}
												for(int n = list.size();n<6;n++){
													mImageView[n].setVisibility(View.INVISIBLE);
												}
											}
										} else {
											sweetAlertDialog.dismiss();
											new SweetAlertDialog(
													CampaignPostDetailActivity.this , SweetAlertDialog.ERROR_TYPE)
													.setContentText("参加活动失败")
													.setConfirmText("确定")
													.showCancelButton(false).setConfirmClickListener(new OnSweetClickListener() {
														
														@Override
														public void onClick(SweetAlertDialog sweetAlertDialog) {
															// TODO Auto-generated method stub
															sweetAlertDialog.dismiss();
														}
													}).show();
													
										}
									}

									@Override
									protected void doInBack() throws Exception {
										/**
										 * 参加活动
										 */
										Map<String, String> param = new HashMap<String, String>();
										param.put("userID", Utils.getID());
										param.put("CampaignID", curPost.getId());
										param.put("phone", et_phone.getText()
												.toString());
										System.out.println(Utils.getID()
												+ "   " + curPost.getId());
										String jsonStr = new WebService(
												C.JOINCAMPAIGN, param)
												.getReturnInfo().toString();
										System.out.println(jsonStr);
										Log.i("CampaignPostDetail", "点击");
										result = GetObjectFromService
												.getSimplyResult(jsonStr);
										System.out.println("result--->"
												+ result);
									}
								}.execute();

							}
						}).setCancelClickListener(new OnSweetClickListener() {

							@Override
							public void onClick(
									SweetAlertDialog sweetAlertDialog) {
								// TODO Auto-generated method stub
								sweetAlertDialog.dismiss();
							}
						}).show();

			}
		});
		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CampaignPostDetailActivity.this.finish();
			}
		});

		blog_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(CampaignPostDetailActivity.this,
						CampaignAddCommentActivity.class);
				intent.putExtra("postId", curPost.getId());
				if (curPost.getChannel_id() != null
						&& curPost.getChannel_id().equals("-1")) {
					intent.putExtra("cater", CampaignAddCommentActivity.NEWS);
				} else {
					intent.putExtra("cater", CampaignAddCommentActivity.NEWS);
				}
				CampaignPostDetailActivity.this.startActivityForResult(intent,
						0);
				overridePendingTransition(R.anim.roll_up, R.anim.roll);
			}
		});

		blog_iscollection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				param.clear();
				String id = Utils.getID();
				param.put("userID", id);
				param.put("CampaignID", curPost.getId());
				new SimpleNetTask(ctx, true) {
					boolean flag;
					String method = "";

					@Override
					protected void onSucceed() {
						if (flag) {
							if (iscollection) {
								System.out.println("取消收藏");
								blog_iscollection
										.setImageResource(R.drawable.ic_collection_normal);
								tv_collection.setText("收藏");
								iscollection = false;
							} else {
								final Dialog dialog = new Dialog(ctx,
										R.style.like_toast_dialog_style);
								View view = LayoutInflater.from(ctx).inflate(
										R.layout.record_layout, null);
								dialog.setContentView(view, new LayoutParams(
										ViewGroup.LayoutParams.WRAP_CONTENT,
										ViewGroup.LayoutParams.WRAP_CONTENT));
								WindowManager.LayoutParams lp = dialog
										.getWindow().getAttributes();
								lp.alpha = 0.9f;
								lp.gravity = Gravity.CENTER;
								dialog.show();

								new Thread(new Runnable() {
									@Override
									public void run() {
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										Message msg = mHandler.obtainMessage();
										msg.obj = dialog;
										mHandler.sendMessage(msg);
									}
								}).start();

								blog_iscollection
										.setImageResource(R.drawable.ic_collection_pressed);
								tv_collection.setText("已收藏");
								iscollection = true;
							}

						}
					}

					@Override
					protected void doInBack() throws Exception {
						if (iscollection) {
							method = C.DELETECOLLECTION;
						} else {
							method = C.ADDCOLLECTION;
						}
						// 收藏接口
						String jsonstr = new WebService(method, param)
								.getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonstr);
					}
				}.execute();
			}
		});
		// WebSettings settings = webView.getSettings();
		// settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// settings.setLoadWithOverviewMode(true);
		
		
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String campaignId = curPost.getId();
				startActivity(new Intent(CampaignPostDetailActivity.this, CampaignJoinerActivity.class).putExtra("post", campaignId));
			}
		});
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			((Dialog) msg.obj).cancel();
			;
		}
	};

	public void setWebSeeting(final WebView webView) {
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
	}

	public boolean setData(Intent intent) {
		curPost = (CampaignPostModel) intent.getSerializableExtra("post");
		String JoinNum = curPost.gethasJoin();
		String ImgUrl = curPost.getImageurl();
		tv_name.setText(curPost.getSource());
		tv_time.setText(curPost.getTime());
		imageLoader.displayImage(ImgUrl, img_campaign,
				PhotoUtils.getImageOptions(R.drawable.ic_list_empty));
		title.setText(curPost.getTitle());
		time.setText(curPost.getContent());
		place.setText(curPost.getPlace());

		nowJoinNum.setText(JoinNum);
		String id = curPost.getId();
		if (curPost.getChannel_id() != null
				&& curPost.getChannel_id().equals("-1")) {
			// webView.loadUrl(campaignUrl + id);
		} else {
			// webView.loadUrl(campaignUrl + id);
		}

		headerLayout.showTitle(curPost.getTitle());
		if (curPost.isJoin().equals("已参与")) {
			btn_meJoin.setText("已参与");
			btn_meJoin.setBackgroundColor(Color.GRAY);
			btn_meJoin.setClickable(false);
		} else {

		}

		if (curPost.isCollection().equals("已收藏")) {
			blog_iscollection.setImageResource(R.drawable.ic_collection_pressed);
			tv_collection.setText("已收藏");
			iscollection = true;
		} else {
			blog_iscollection.setImageResource(R.drawable.ic_collection_normal);
			tv_collection.setText("收藏");
			iscollection = false;
		}
		return true;
	}

	/**
	 * 获取活动参与者
	 */
	private void getCampaignJoiner() {
		new SimpleNetTask(ctx, false) {

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
			     list = new ArrayList<String>();  
					for(int i = 0;i<joinerList.size();i++){
						list.add(0,joinerList.get(i).getHeader());
					}
					for(int i = 0 ;i<list.size() ; i++){
						String imgUrl = list.get(i);
						imageLoader.displayImage(imgUrl,
								mImageView[i], PhotoUtils
										.getImageOptions(R.drawable.ic_default_head));
					}
					for(int j = list.size(); j<6;j++){
						mImageView[j].setVisibility(View.INVISIBLE);
					}
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				Map<String, String> param = new HashMap<String, String>();
				param.put("campaignId", curPost.getId());
				String jsonStr = new WebService(C.GETFIRSTSIXCAMPAIGNJOINER, param)
						.getReturnInfo();
				joinerList = GetObjectFromService.getCampaignJoiner(jsonStr);
			}
		}.execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode != RESULT_OK) {
			return;
		}
		Comment comment = new Comment();
		PUUser user = new PreferenceMap(ctx).getUser();
		comment.setContent(intent.getExtras().getString("commentcontent"));
		comment.setImage_url(user.getImage());
		comment.setName(user.getName());
		comment.setTime("刚刚");
		commentdata.add(0, comment);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
		getCampaignJoiner();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setData(intent);
	}
}
