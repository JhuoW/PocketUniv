package com.pocketuniversity.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.adapter.CommentListAdapter;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Comment;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.bean.PostModel;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class PostDetailActivity extends BaseActivity{

	public static String url = "http://114.215.81.194:8187/web-news-visit-clean.aspx?nid=";
	
	private View contentView;
	private WebView webView;
	private ImageView ivComment, blog_iscollection;
	private PostModel postModel;
	private TextView tvDetailAuthor;
	private ScrollView scrollView;
	private HeaderLayout headerLayout;
	private ListView comment_list;
	private CommentListAdapter adapter;
	private List<Comment> commentdata = new ArrayList<Comment>();
	private Map<String, String> param = new HashMap<String, String>();
	private EditText blog_comment;
	String postId;
	MyProgressDialog dialog;
	PostModel curPost;
	boolean iscollection = false;
	int curCommentPage = 1;
	int curCommentCount = 40;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_news);
		initView();
		Intent intent = getIntent();
		if (setData(intent)) {
			param.clear();
			param.put("newsID", curPost.getId());
			param.put("page", curCommentPage + "");
			param.put("count", curCommentCount + "");
			initData();
			}else {
				startActivity(new Intent(this, MainActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				finish();
			}
	}
	
	private void initView(){
		dialog = new MyProgressDialog(ctx);
		dialog.show();
		blog_iscollection = (ImageView) findViewById(R.id.blog_iscollection);
		blog_comment = (EditText) findViewById(R.id.blog_comment);
		comment_list = (ListView) findViewById(R.id.comment_list);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		webView = (WebView) findViewById(R.id.webView);
		setWebSeeting(webView);
		tvDetailAuthor = (TextView) findViewById(R.id.tvDetailAuthor);
		blog_comment.requestFocus();
		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PostDetailActivity.this.finish();
			}
		});

		blog_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PostDetailActivity.this,
						AddCommentActivity.class);
				intent.putExtra("postId", curPost.getId());
				if (curPost.getChannel_id() != null
						&& curPost.getChannel_id().equals("-1")) {
					intent.putExtra("cater", AddCommentActivity.NEWS);
				} else {
					intent.putExtra("cater", AddCommentActivity.NEWS);
				}
				PostDetailActivity.this.startActivityForResult(intent, 0);
				overridePendingTransition(R.anim.roll_up, R.anim.roll);
			}
		});
		blog_iscollection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				param.clear();
				String id = Utils.getID();
				param.put("userID", id);
				param.put("newsID", curPost.getId());
				new SimpleNetTask(ctx, true) {
					boolean flag;
					String method = "";

					@Override
					protected void onSucceed() {
						if (flag) {
							if (iscollection) {
								blog_iscollection
										.setImageResource(R.drawable.icon_collection);
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
										.setImageResource(R.drawable.icon_collection_like);
								iscollection = true;
							}

						}
					}

					@Override
					protected void doInBack() throws Exception {
						if (iscollection) {   //已收藏则isCollection是false
							method = C.DELETENEWSCOLLECTION;
						} else {
							method = C.ADDNEWSCOLLECTION;
						}
						String jsonstr = new WebService(method, param)
								.getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonstr);
					}
				}.execute();
			}
		});

		WebSettings settings = webView.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setLoadWithOverviewMode(true);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
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
					jsonstr = new WebService(C.GETNEWSCOMMENT, param)
							.getReturnInfo();
				} else {
					jsonstr = new WebService(C.GETNEWSCOMMENT, param)
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
	
	public boolean setData(Intent intent) {
		curPost = (PostModel) intent.getSerializableExtra("post");
		String id = curPost.getId();
		if (curPost.getChannel_id() != null
				&& curPost.getChannel_id().equals("-1")) {
			webView.loadUrl(url + id);
		} else {
			webView.loadUrl(url + id);
		}

		headerLayout.showTitle(curPost.getTitle());
		tvDetailAuthor.setText("本文由 " + curPost.getSource() + " 发布");
		if (curPost.isCollection().equals("已收藏")) {
			blog_iscollection.setImageResource(R.drawable.icon_collection_like);
			iscollection = true;
		} else {
			blog_iscollection.setImageResource(R.drawable.icon_collection);
			iscollection = false;
		}
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setData(intent);
	}
	
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

	
	
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			((Dialog) msg.obj).cancel();
			;
		}
	};
	

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			scrollView.fullScroll(ScrollView.FOCUS_DOWN);
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearWebView(webView);
	}

	public void clearWebView(WebView webView) {
		if (webView != null) {
			webView.stopLoading();
			webView.removeAllViews();
			webView.clearView();
			webView.destroy();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}
	
}
