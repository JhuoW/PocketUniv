package com.pocketuniversity.friendcircle.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.ab.utils.AbDialogFragment.AbDialogOnLoadListener;
import com.pocketuniversity.ab.utils.AbDialogUtil;
import com.pocketuniversity.ab.utils.AbLoadDialogFragment;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.FriendAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.FriendsCircle;
import com.pocketuniversity.bean.Lost;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.friend.view.ListDialog;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;

public class FriendsCircleActivity extends AbActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener {

	private ListView listView = null;
	private PopupWindow window;// 评论window
	private PopupWindow editWindow;// 回复window
	private ListDialog dialog;
	private ImageButton back;// 返回按钮
	private ImageButton more;// 弹出更多

	private AbPullToRefreshView mAbPullToRefreshView = null;
	private AbLoadDialogFragment mDialogFragment = null;
	private int total = 50;
	private int pageSize = 15;
	private int currentPage = 1;

	private TextView discuss;// 评论
	private TextView favuor;// 赞
	private TextView favuorCancle;// 取消赞

	private View headView;// 头部view
	private ImageView userPhoto;// 头像头像
	private TextView userName;// 用户名字
	private RelativeLayout topLayout;
	ImageLoader imageLoader = ImageLoader.getInstance();

	FriendAdapter adapter ;
	HeaderLayout headerLayout;
	PUUser puUser;
	List<FriendsCircle> friendsCircleList = new ArrayList<FriendsCircle>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_group_activity);
		initView();
		setListHeader();
	}

	private void initView() {
		puUser = new PreferenceMap(FriendsCircleActivity.this).getUser();
		dialog = new ListDialog(this);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("朋友圈");
		mAbPullToRefreshView = (AbPullToRefreshView) this
				.findViewById(R.id.mPullRefreshView);
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

			}
		});
		topLayout = (RelativeLayout) findViewById(R.id.friend_circle);
		listView = (ListView) findViewById(R.id.friend_list);
		adapter = new FriendAdapter(this, friendsCircleList, getLayoutInflater());
		listView.setAdapter(adapter);
		headView = LayoutInflater.from(this).inflate(R.layout.friend_head_item,
				null);
		userPhoto = (ImageView) headView.findViewById(R.id.user_photo);
		userName = (TextView) headView.findViewById(R.id.user_name);
		listView.addHeaderView(headView);
		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		// 显示进度框
		mDialogFragment = AbDialogUtil.showLoadDialog(this, R.drawable.ic_load,
				"加载中...");
		mDialogFragment.setAbDialogOnLoadListener(new AbDialogOnLoadListener() {

			@Override
			public void onLoad() {
				// 获取数据
			}

		});
	}

	private void setListHeader() {
		imageLoader.displayImage(puUser.getImage(), userPhoto,
				PhotoUtils.getImageOptions(R.drawable.user_logo));
		userName.setText(puUser.getName());
	}

	/**
	 * 初始化popWindow
	 */
	private void initPopWindow() {
		View view = getLayoutInflater().inflate(R.layout.friend_reply, null);
		window = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		window.setAnimationStyle(R.style.reply_window_anim);
		discuss = (TextView) view.findViewById(R.id.discuss);
		favuor = (TextView) view.findViewById(R.id.favuor);
		favuorCancle = (TextView) view.findViewById(R.id.favuor_cancle);
		window.setBackgroundDrawable(getResources().getDrawable(R.color.black));
		window.setOutsideTouchable(true);

	}

	private void getData() {
		new SimpleNetTask(FriendsCircleActivity.this) {
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				Map<String, String> param = new HashMap<String, String>();
				param.clear();
				String jsonStr = new WebService(C.GETFRIENDCIRCLELIST, param).getReturnInfo();
			}
		}.execute();
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

}
