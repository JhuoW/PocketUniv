package com.pocketuniversity.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.pocketuniversity.R;
import com.pocketuniversity.adapter.CenterListAdp;
import com.pocketuniversity.adapter.TopVpAdp;
import com.pocketuniversity.bean.CategoryModel;
import com.pocketuniversity.bean.PostModel;
import com.pocketuniversity.bean.TopVpItem;
import com.pocketuniversity.collection.fragment.MyNewsCollectionFragment;
import com.pocketuniversity.db.DBHelper;
import com.pocketuniversity.db.Post;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.more.activity.CampaignCollectionActivitity;
import com.pocketuniversity.more.activity.MyCollectionNewsActivity;
import com.pocketuniversity.more.activity.MyPublishCampaignActivity;
import com.pocketuniversity.poptitle.ActionItem;
import com.pocketuniversity.poptitle.TitlePopup;
import com.pocketuniversity.poptitle.TitlePopup.OnItemOnClickListener;
import com.pocketuniversity.utils.BaseTools;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.ColumnHorizontalScrollView;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class InformationActivity extends BaseActivity {
	private static final String TAG = "InformationFragment";

	private List<CategoryModel> userChannelList = new ArrayList<CategoryModel>();
	private List<PostModel> topPostModels = new ArrayList<PostModel>();
	private List<PostModel> newsList = new ArrayList<PostModel>();
	private List<TopVpItem> topVpItems = new ArrayList<TopVpItem>();
	private ListView news_listview;
	private RelativeLayout relaCenterVpParent;
	private ViewPager centerViewPager;
	private RadioGroup centerRadioGroup;
	private TopVpAdp topVpAdp;

	public static String channel_id;
	public static String channel_text;

	private boolean showTopNews = false;

	public DBHelper dbHelper;
	private CenterListAdp mAdapter;
	private MyProgressDialog dialog;

	private int w, h;
	private RadioGroup.LayoutParams layoutParams;
	private TitlePopup titlePopup;

	LinearLayout mRadioGroup_content;
	RelativeLayout rl_column;
	private ViewPager mViewPager;
	/** 用户选择的新闻分类列表 */
	/** 当前选中的栏目 */
	private int columnSelectIndex = 0;
	/** 左阴影部分 */
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;

	private ColumnHorizontalScrollView mColumnHorizontalScrollView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information);
		findView();
		initData();
		init();
		initAction();
	}
	
	private void findView(){
		dialog = new MyProgressDialog(ctx);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("徐医新闻");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InformationActivity.this.finish();
			}
		});
		headerLayout.showRightImageButton(R.drawable.ic_fifter, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				titlePopup.show(v);
			}
		});
		news_listview = (ListView) findViewById(R.id.news_listview);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		relaCenterVpParent = (RelativeLayout) 
				findViewById(R.id.relaCenterVpParent);
		centerViewPager = (ViewPager) findViewById(R.id.centerViewPager);
		centerRadioGroup = (RadioGroup) findViewById(R.id.centerRadioGroup);

		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout)findViewById(R.id.mRadioGroup_content);
		rl_column = (RelativeLayout)findViewById(R.id.rl_column);

		w = getResources().getDrawable(R.drawable.radiobutton_normal)
				.getIntrinsicWidth();
		h = getResources().getDrawable(R.drawable.radiobutton_normal)
				.getIntrinsicHeight();
		layoutParams = new RadioGroup.LayoutParams(w, h);
		layoutParams.setMargins(0, 0, 30, 0);

		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 4;
	}
	
	
	private void init(){
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.addAction(new ActionItem(this, "我的收藏", R.drawable.ic_func_collection));
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if(position == 0){
					startActivity(new Intent(InformationActivity.this, MyCollectionNewsActivity.class));
				}
			}
		});
		
	}

	
	private void initData() {

		dbHelper = new DBHelper(this);
		dbHelper.openSqLiteDatabase();

			 userChannelList = (ArrayList<CategoryModel>) dbHelper
			 .queryAllCategory();
			if (userChannelList.size() != 0) {
				channel_text = userChannelList.get(0).getTitle();
				channel_id = userChannelList.get(0).getId();
				initTabColumn();
			}
	}
	/**
	 * 初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count = userChannelList.size();
		mColumnHorizontalScrollView.setParam(this, mScreenWidth,
				mRadioGroup_content, shade_left, shade_right, rl_column);
		for (int i = 0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					mItemWidth, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this,
					R.style.top_category_scroll_view_item_text);
			columnTextView.setBackgroundResource(R.drawable.radio_button_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(userChannelList.get(i).getTitle());
			columnTextView.setTextColor(getResources().getColorStateList(
					R.color.top_category_scroll_text_color_day));
			if (columnSelectIndex == i) {
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
						View localView = mRadioGroup_content.getChildAt(i);
						if (localView != v)
							localView.setSelected(false);
						else {
							selectTab(i);
						}

					}
				}
			});
			mRadioGroup_content.addView(columnTextView, i, params);
		}
	}
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
		}
		// 判断是否选中
		for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
		channel_text = userChannelList.get(tab_postion).getTitle();
		channel_id = userChannelList.get(tab_postion).getId();
		if (tab_postion != 0) {
			showTopNews = false;
		} else {
			showTopNews = false;
		}
		getNewsData(channel_id);
	}
	private void initAction() {
		news_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 Intent intent = new Intent(InformationActivity.this,
				 PostDetailActivity.class);
				 intent.putExtra("post", newsList.get(position));
				 startActivity(intent);
				 overridePendingTransition(R.anim.slide_in_right,
				 R.anim.slide_out_left);
			}
		});

		centerViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				for (int i = 0; i < centerRadioGroup.getChildCount(); i++) {
					if (i == arg0) {
						centerRadioGroup.getChildAt(i).setSelected(true);
					} else {
						centerRadioGroup.getChildAt(i).setSelected(false);
					}
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		centerRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						for (int i = 0; i < centerRadioGroup.getChildCount(); i++) {
							if (i == checkedId) {
								centerRadioGroup.getChildAt(i)
										.setSelected(true);
							} else {
								centerRadioGroup.getChildAt(i).setSelected(
										false);
							}
						}
						centerViewPager.setCurrentItem(checkedId);
					}
				});
	}

	private int currentPage = 1;
	private int currentPageSize = 40;

	private void getNewsData(final String channel_id) {
		dialog.show();
		new SimpleNetTask(ctx, false) {
			@Override
			protected void onSucceed() {
				if (showTopNews) {
					relaCenterVpParent.setVisibility(View.VISIBLE);
					topPostModels.clear();
					topVpItems.clear();
					List<PostModel> models = dbHelper.queryPostsByCategoryId(-1
							+ "", currentPage * currentPageSize + "");
					topPostModels.addAll(models);

					for (int i = 0; i < topPostModels.size(); i++) {
						TopVpItem topVpItem = new TopVpItem();
						topVpItem.relativeLayout = (RelativeLayout)getLayoutInflater()
								.inflate(
										R.layout.lay_center_vp_item, null);
						topVpItem.relativeLayout.setTag(i);
						topVpItem.imageView = (ImageView) topVpItem.relativeLayout
								.findViewById(R.id.ivViewPageItem);
						topVpItem.textView = (TextView) topVpItem.relativeLayout
								.findViewById(R.id.tvViewPageItem);
						topVpItem.relativeLayout
								.setOnClickListener(topImgsClickListenner);
						topVpItems.add(topVpItem);
					}

					topVpAdp = new TopVpAdp(topVpItems, topPostModels);
					centerViewPager.setAdapter(topVpAdp);
					centerRadioGroup.removeAllViews();
					for (int i = 0; i < topVpItems.size(); i++) {
						RadioButton radioButton = new RadioButton(ctx);
						radioButton.setLayoutParams(layoutParams);
						radioButton
								.setButtonDrawable(R.drawable.radiobutton_sel);
						radioButton
								.setBackgroundResource(R.drawable.radiobutton_sel);
						if (i == 0) {
							radioButton.setSelected(true);
						} else {
							radioButton.setClickable(false);
						}
						centerRadioGroup.addView(radioButton, i);
					}

				}else {
					relaCenterVpParent.setVisibility(View.GONE);
				}

				List<PostModel> newdata;
				newdata = dbHelper.queryPostsByCategoryId(channel_id + "",
						currentPage * currentPageSize + "");
				newsList.clear();
				newsList.addAll(newdata);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				mAdapter = new CenterListAdp(newsList,getLayoutInflater());
				news_listview.setAdapter(mAdapter);
				
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				if (showTopNews) {
					// String jsonStr = new APIHelper().getTopPosts();

					/**
					 * 模拟数据
					 */
					String jsonStr = " { \"ret\" : \"success\",\"ischanged\":\"true\",\"advertList\":"
							+ "[{\"newsID\":\"1\",\"description\":\"广告描述1\",\"title\":\"广告1\",\"imgUrl\":\"http://app.iriscsc.com:8080/attached/image/20150502/20150502152106_9695.jpg\",\"source\":\"\",\"time\":\"\",\"viewCount\":\"\",\"collectionCount\":\"\",\"commentCount\":\"\",\"collection\":false},"
							+ "{\"newsID\":\"2\",\"description\":\"广告描述2\",\"title\":\"广告2\",\"imgUrl\":\"http://app.iriscsc.com:8080/attached/image/20150502/20150502152858_5024.jpg\",\"source\":\"\",\"time\":\"\",\"viewCount\":\"\",\"collectionCount\":\"\",\"commentCount\":\"\",\"collection\":false}]} ";

					JSONObject json = new JSONObject(jsonStr);
					if (json.get("ret").equals("success")) {
						JSONArray jsonarray = json.getJSONArray("advertList");
						dbHelper.deleteAllDataFromTable(Post.tableName);
						dbHelper.insertPosts(jsonarray, -1 + "");
					}
					
				}
				
				
				String jsonStr = new APIHelper().getPostsByCategory(channel_id+"");
				JSONObject json = new JSONObject(jsonStr);
				if (json.get("ret").equals("success")) {
					JSONArray jsonarray = json.getJSONArray("newsList");
					dbHelper.insertPosts(jsonarray, channel_id);
				}
			}
		}.execute();
	}

	private View.OnClickListener topImgsClickListenner = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int index = (Integer) v.getTag();// index
			// Intent intent = new Intent(getActivity(),
			// PostDetaileActivity.class);
			// intent.putExtra("post", topPostModels.get(index));
			// startActivity(intent);
			// getActivity().overridePendingTransition(R.anim.anim_r_to_0,
			// R.anim.anim_0_to_l);
		}
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mAdapter = null ;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getNewsData(channel_id);
	}

}
