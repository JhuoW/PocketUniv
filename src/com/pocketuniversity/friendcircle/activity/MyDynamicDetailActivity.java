package com.pocketuniversity.friendcircle.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.activity.MainActivity;
import com.pocketuniversity.adapter.FriendsCircleCommentListAdapter;
import com.pocketuniversity.adapter.NoScrollGridAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.CampaignJoiner;
import com.pocketuniversity.bean.Comment;
import com.pocketuniversity.bean.FriendsCircle;
import com.pocketuniversity.bean.MyDynamic;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.image.NoScrollGridView;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.utils.StringUtils;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;
import com.pocketuniversity.view.RoundImageView;

public class MyDynamicDetailActivity extends BaseActivity {


	private TextView name;
	private TextView time;
	private TextView content;
	private TextView zancount;
	private TextView commentCount;
	private RoundImageView header;
	private NoScrollGridView gridview;
	private ImageView img_thumb;
	private LinearLayout ll_thumb;
	private TextView zanUser01;
	private TextView zanUser02;
	private TextView zanNum;
	private LinearLayout ll_zan;
	ImageLoader imageLoader = ImageLoader.getInstance();
	private List<CampaignJoiner> zanUserList;
	private List<Comment> commentdata = new ArrayList<Comment>();
	private FriendsCircleCommentListAdapter adapter;
	MyDynamic curPost;
	private ListView comment_list;

	MyProgressDialog dialog;
	PUUser user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_circle_detail);
		user = new PreferenceMap(ctx).getUser();
		initView();
	}

	
    private void initData(){
    	dialog.show();
    	new SimpleNetTask(ctx, false) {
			@Override
			protected void onSucceed() {
				dialog.dismiss();
				adapter = new FriendsCircleCommentListAdapter(commentdata, ctx);
				comment_list.setAdapter(adapter);
				setListViewHeightBasedOnChildren(comment_list);
				commentCount.setText(commentdata.size()+"");
			}

			@Override
			protected void doInBack() throws Exception {
				String jsonstr = "";
				jsonstr = new WebService(C.GETFRIENDCIRCLECOMMENT, param)
						.getReturnInfo();
				commentdata = GetObjectFromService.getFriendsCircleCommentList(jsonstr);
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

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			((Dialog) msg.obj).cancel();
			;
		}
	};

    
	private void initView() {
		dialog = new MyProgressDialog(ctx);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("详情");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyDynamicDetailActivity.this.finish();
			}
		});
		
		headerLayout.showRightImageButton(R.drawable.ic_campus_comment_send2, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =  new Intent();
				intent.setClass(MyDynamicDetailActivity.this, FriendsCircleCommentActivity.class);
				intent.putExtra("postId", curPost.getId());
				MyDynamicDetailActivity.this.startActivityForResult(intent, 0);
				overridePendingTransition(R.anim.roll_up, R.anim.roll);
			}
		});
		
		ll_zan = (LinearLayout) findViewById(R.id.ll_zan);
		zanUser01 = (TextView) findViewById(R.id.user01);
		zanUser02 = (TextView) findViewById(R.id.user02);
		zanNum = (TextView) findViewById(R.id.zanNum);
		name = (TextView) findViewById(R.id.name);
		time = (TextView) findViewById(R.id.date);
		ll_thumb = (LinearLayout) findViewById(R.id.ll_thumb);
		content = (TextView) findViewById(R.id.content_text);
		header = (RoundImageView) findViewById(R.id.photo);
		commentCount = (TextView) findViewById(R.id.tv_comment);
		zancount = (TextView) findViewById(R.id.tv_thumb);
		gridview = (NoScrollGridView) findViewById(R.id.gridview);
		img_thumb = (ImageView) findViewById(R.id.img_thumb);
		comment_list = (ListView) findViewById(R.id.comment_list);
		ll_zan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String id = curPost.getId();
				startActivity(new Intent(MyDynamicDetailActivity.this, ZanUserActivity.class).putExtra("post", id));

			}
		});
		
	}

	public boolean setData(Intent intent) {
		curPost = (MyDynamic) intent.getSerializableExtra("post");
		name.setText(curPost.getName());
		time.setText(curPost.getTime());
		zancount.setText(curPost.getZanCount());
		commentCount.setText(curPost.getCommentCount());
		content.setText(curPost.getContent());
		String headerUrl = curPost.getHeader();
		imageLoader.displayImage(headerUrl, header,
				PhotoUtils.getImageOptions(R.drawable.ic_default_head));
		String imgUrl = curPost.getImgUrl();

		String s1 = StringUtils.trimFirstAndLastChar(imgUrl, "[".charAt(0));
		String s2 = StringUtils.trimFirstAndLastChar(s1, "]".charAt(0));
		String s4;
		final ArrayList<String> list = new ArrayList<String>();
		String[] stringArr = s2.split(",");
		if (imgUrl.equals("[\"\"]")) {
		} else {
			for (int i = 0; i < stringArr.length; i++) {
				String s = stringArr[i];
				String s3 = StringUtils.trimFirstAndLastChar(s, "\"".charAt(0));
				s4 = StringUtils.trimFirstAndLastChar(s3, "\"".charAt(0));
				System.out.println("s4:" + s4);
				String s5 = s4.replace("\\", "");
				list.add(s5);
			}
		}

		if (TextUtils.isEmpty(imgUrl)) {
			gridview.setVisibility(View.GONE);
		} else {
			gridview.setAdapter(new NoScrollGridAdapter(ctx, list));
		}

		// 点击回帖九宫格，查看大图
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				imageBrower(position, list);
			}
		});
		
		String isZan = curPost.getIsZan();
		
		if (isZan.equals("true")) {
			img_thumb.setImageResource(R.drawable.ic_thumb_checked);
			curPost.setIsZan("true");
		} else {
			img_thumb.setImageResource(R.drawable.ic_thumb_uncheck);
			curPost.setIsZan("false");
		}
		
		final String id = curPost.getId();
		
		ll_thumb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new SimpleNetTask(ctx) {
					boolean temp;
					String jsonStr ;
					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						if (temp) {
							if (curPost.getIsZan().equals("true")) {
								img_thumb
										.setImageResource(R.drawable.ic_thumb_uncheck);
								int zanCount = Integer
										.parseInt(zancount.getText()
												.toString());
								int newZanCount = zanCount - 1;
								zancount.setText(newZanCount + "");
								zanNum.setText(newZanCount + "");
								getZanUser();
								curPost.setIsZan("false");
							} else {
								img_thumb
										.setImageResource(R.drawable.ic_thumb_checked);
								int zanCount = Integer
										.parseInt(zancount.getText()
												.toString());
								int newZanCount = zanCount + 1;
								zancount.setText(newZanCount + "");
								zanNum.setText(newZanCount + "");
								getZanUser();
								curPost.setIsZan("true");
							}
						}
					}

					@Override
					protected void doInBack() throws Exception {
						// TODO Auto-generated method stub
						if (curPost.getIsZan().equals("true")) {
							jsonStr = new APIHelper().CancelZan(id);
							
						} else {
							jsonStr = new APIHelper().AddZan(id);
						}
						temp = GetObjectFromService
								.getSimplyResult(jsonStr);
					}
				}.execute();
			}
		});
		return true;
	}

	
	//获取点赞人
	private void getZanUser(){
		new SimpleNetTask(ctx) {
			
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if(zanUserList.size() == 0){
					zanUser01.setVisibility(View.GONE);
					zanUser02.setVisibility(View.GONE);
					zanNum.setText(zanUserList.size()+"");
				}else if(zanUserList.size() == 1){
					zanUser01.setVisibility(View.VISIBLE);
					zanUser02.setVisibility(View.GONE);
					zanUser01.setText(zanUserList.get(0).getNickName());
					zanNum.setText(zanUserList.size()+"");
				}else {
					for(int i = 0;i<zanUserList.size();i++){
						zanUser01.setVisibility(View.VISIBLE);
						zanUser02.setVisibility(View.VISIBLE);
						zanUser01.setText(zanUserList.get(0).getNickName());
						zanUser02.setText(zanUserList.get(1).getNickName());
						zanNum.setText(zanUserList.size()+"");
					}
				}
				
				zancount.setText(zanUserList.size()+"");
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("id", curPost.getId());
				String jsonStr = new WebService(C.GETFIRSTTWOZAN, param).getReturnInfo();
				zanUserList = GetObjectFromService.getZanUser(jsonStr);
			}
		}.execute();
	}
	
	
	
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(ctx, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
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
		Intent intent = getIntent();
		if(setData(intent)){
			//获取点赞人
			getZanUser();
			param.clear();
			param.put("id", curPost.getId());
			initData();
		}else {
			startActivity(new Intent(this, MainActivity.class));
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			finish();
		}
	}

}
