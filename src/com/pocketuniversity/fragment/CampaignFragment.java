package com.pocketuniversity.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
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

import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.CampaignPostDetailActivity;
import com.pocketuniversity.adapter.CampaignCenterListAdp;
import com.pocketuniversity.adapter.CampaignTopVpAdp;
import com.pocketuniversity.bean.CampaignCategoryModel;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.bean.CampaignTopVpItem;
import com.pocketuniversity.db.CampaignPost;
import com.pocketuniversity.db.Category;
import com.pocketuniversity.db.DBHelper;
import com.pocketuniversity.db.Post;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.more.activity.CampaignCollectionActivitity;
import com.pocketuniversity.utils.BaseTools;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.ColumnHorizontalScrollView;
import com.pocketuniversity.view.MyProgressDialog;


public class CampaignFragment extends Fragment {

	private List<CampaignCategoryModel> userChannelList = new ArrayList<CampaignCategoryModel>();
	private List<CampaignPostModel> topPostModels = new ArrayList<CampaignPostModel>();
	private List<CampaignPostModel> newsList = new ArrayList<CampaignPostModel>();
	private List<CampaignTopVpItem> topVpItems = new ArrayList<CampaignTopVpItem>();
	
	private ListView news_listview;
	private RelativeLayout relaCenterVpParent;
	private ViewPager centerViewPager;
	private RadioGroup centerRadioGroup;
	private CampaignTopVpAdp topVpAdp;
	private Button btn_myCollection;

	public static String channel_id;
	public static String channel_text;

	private boolean showTopNews = false;
	
	public DBHelper dbHelper;
	private CampaignCenterListAdp mAdapter;
	private MyProgressDialog dialog;
	
	private int w, h;
	private RadioGroup.LayoutParams layoutParams;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_campaign,
				container, false);
		news_listview = (ListView) view.findViewById(R.id.campaign_listview);
		shade_left = (ImageView) view.findViewById(R.id.shade_left);
		shade_right = (ImageView) view.findViewById(R.id.shade_right);
		relaCenterVpParent = (RelativeLayout) view
				.findViewById(R.id.relaCenterVpParent);
		centerViewPager = (ViewPager) view.findViewById(R.id.centerViewPager);
		centerRadioGroup = (RadioGroup) view
				.findViewById(R.id.centerRadioGroup);
		
		btn_myCollection = (Button) view.findViewById(R.id.MyCollectCampaign);

		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) view
				.findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) view
				.findViewById(R.id.mRadioGroup_content);
		rl_column = (RelativeLayout) view.findViewById(R.id.rl_column);

		w = getResources().getDrawable(R.drawable.radiobutton_normal)
				.getIntrinsicWidth();
		h = getResources().getDrawable(R.drawable.radiobutton_normal)
				.getIntrinsicHeight();
		layoutParams = new RadioGroup.LayoutParams(w, h);
		layoutParams.setMargins(0, 0, 30, 0);

		mScreenWidth = BaseTools.getWindowsWidth(getActivity());
		mItemWidth = mScreenWidth / 4;

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
		initAction();
	}

	
//	/**
//	 * 模拟Json数组
//	 */
//	String jsonArrayStr = "[{\"count\":0,\"categoryid\":\"5\",\"categoryName\":\"社团活动\",\"description\":\"社团活动\"},"
//			+ "{\"count\":0,\"categoryid\":\"4\",\"categoryName\":\"团委活动\",\"description\":\"团委活动\"},"
//			+ "{\"count\":0,\"categoryid\":\"1\",\"categoryName\":\"院系活动\",\"description\":\"院系活动\"},"
//			+ "{\"count\":0,\"categoryid\":\"2\",\"categoryName\":\"科研活动\",\"description\":\"科研活动\"},"
//			+ "{\"count\":0,\"categoryid\":\"3\",\"categoryName\":\"友情链接\",\"description\":\"友情链接\"}]";

	
	private void initData() {

		dbHelper = new DBHelper(getActivity());
		dbHelper.openSqLiteDatabase();
		//dbHelper.deleteAllDataFromTable(CampaignCategory.tableName);
	//	try {
//			JSONArray jsonArray = new JSONArray(jsonArrayStr);
//
//			System.out.println("jsonArray----->" + jsonArray);
//			dbHelper.insertCampaignCategories(jsonArray);
//			userChannelList = (ArrayList<CampaignCategoryModel>) dbHelper
//					.queryAllCampaignCategory();
			 userChannelList = (ArrayList<CampaignCategoryModel>) dbHelper
			 .queryAllCampaignCategory();
			if (userChannelList.size() != 0) {
				channel_text = userChannelList.get(0).getTitle();
				channel_id = userChannelList.get(0).getId();
				initTabColumn();
	//		}

		} 

	}

	/**
	 * 初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count = userChannelList.size();
		mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth,
				mRadioGroup_content, shade_left, shade_right, rl_column);
		for (int i = 0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					mItemWidth, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			TextView columnTextView = new TextView(getActivity());
			columnTextView.setTextAppearance(getActivity(),
					R.style.top_category_scroll_view_item_text);
			columnTextView.setBackgroundResource(R.drawable.radio_button_bg_campaign);
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
			//不显示广告框
			showTopNews = false;
		}
		getNewsData(channel_id);
	}

	private void initAction() {
		news_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 Intent intent = new Intent(getActivity(),
				 CampaignPostDetailActivity.class);
				 intent.putExtra("post", newsList.get(position));
				//intent.putExtra("isJoin", newsList.get(position).isJoin());
				 startActivity(intent);
				 getActivity().overridePendingTransition(R.anim.slide_in_right,
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
		
		
		btn_myCollection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), CampaignCollectionActivitity.class));
			}
		});
	}

	private int currentPage = 1;
	private int currentPageSize = 40;

	private void getNewsData(final String channel_id) {
		new SimpleNetTask(getActivity(), false) {
			@Override
			protected void onSucceed() {
				if (showTopNews) {
					relaCenterVpParent.setVisibility(View.VISIBLE);
					topPostModels.clear();
					topVpItems.clear();
					List<CampaignPostModel> models = dbHelper.queryCampaignPostsByCategoryId(-1
							+ "", currentPage * currentPageSize + "");
					topPostModels.addAll(models);

					for (int i = 0; i < topPostModels.size(); i++) {
						CampaignTopVpItem topVpItem = new CampaignTopVpItem();
						topVpItem.relativeLayout = (RelativeLayout) getActivity()
								.getLayoutInflater().inflate(
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

					topVpAdp = new CampaignTopVpAdp(topVpItems, topPostModels);
					centerViewPager.setAdapter(topVpAdp);
					centerRadioGroup.removeAllViews();
					for (int i = 0; i < topVpItems.size(); i++) {
						RadioButton radioButton = new RadioButton(getActivity());
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

				List<CampaignPostModel> newdata;
				newdata = dbHelper.queryCampaignPostsByCategoryId(channel_id + "",
						currentPage * currentPageSize + "");
				newsList.clear();
				newsList.addAll(newdata);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				mAdapter = new CampaignCenterListAdp(newsList, getActivity()
						.getLayoutInflater());
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
						dbHelper.deleteAllDataFromTable(CampaignPost.tableName);
						dbHelper.insertCampaignPosts(jsonarray, -1 + "");
					}
				}
				
				
				
				
				String jsonstr = new APIHelper().getPostsByCampaignCategory(channel_id
						+ "");
				Log.i("NewsFragment", "getPostsByCategory方法的返回值--->" + jsonstr);
				/**
				 * 新闻列表模拟数据
				 */
//				String jsonNewsStr = "{\"ret\" : \"success\",\"ischanged\":\"true\",\"newsList\":"
//						+ "[{\"newsID\":\"51\",\"source\":\"爱睿视\",\"title\":\"最伤眼睛的10件事\",\"description\":\"眼睛是人身上最脆弱的器官。生活中，有很多事情会对它造成极大的伤害。了解这些危险有助于防患于未然，更好地保护双眼。爱睿视为你总结出最毁眼10件事，助你做好防护措施。\",\"imgUrl\":\"\",\"time\":\"2015-5-3 11:48:31\",\"viewCount\":\"213\",\"collectionCount\":\"7\",\"commentCount\":\"15\",\"collection\":false}]}";
//				
//				String jsonNewsStr2 = "{\"ret\" : \"success\",\"ischanged\":\"true\",\"newsList\":"
//						+ "[{\"newsID\":\"57\",\"source\":\"爱睿视\",\"title\":\"看电视顺便测眼病\",\"description\":\"在将来，“看电视”或许也能成为一种筛查眼科疾病的方式。来自伦敦城市大学的一项新研究显示，分析观看影片时眼球的运动方式可以帮助人们识别青光眼患者。此项研究可以帮助早期诊断，以便医生能够更早地识别并在永久性伤害出现之前着手治疗。该论文近期发表在《老化神经科学前沿》期刊上。\",\"imgUrl\":\"http://app.iriscsc.com:8080/attached/image/20150503/20150503121053_4450.jpg\",\"time\":\"2015-5-3 12:11:20\",\"viewCount\":\"94\",\"collectionCount\":\"5\",\"commentCount\":\"8\",\"collection\":false},"
//						+ "{\"newsID\":\"56\",\"source\":\"爱睿视\",\"title\":\"可调生物钟眼镜问世：亮点是它来自朝鲜\",\"description\":\" 据《朝鲜新报》报道，朝鲜最近发明了一种可调节人类生物钟的眼镜，它可以抑制或刺激人体睡眠荷尔蒙的产生，从而达到调节身体周期的效果。这种眼镜将有望成为出国和出差人士的必备品，还可为体育运动员的训练和日常生活提供便利。\",\"imgUrl\":\"http://app.iriscsc.com:8080/attached/image/20150503/20150503120745_1010.jpg\",\"time\":\"2015-5-3 12:08:26\",\"viewCount\":\"57\",\"collectionCount\":\"0\",\"commentCount\":\"2\",\"collection\":false},"
//						+ "{\"newsID\":\"55\",\"source\":\"爱睿视\",\"title\":\"戴近视眼镜的小老鼠\",\"description\":\"如果吃几颗药就能让深受近视困扰的你摆脱眼镜的纠缠，就像碰到头疼脑热吃几颗药就能痊愈那么简单，这是众多近视患者的梦想。而温州医学院以瞿佳教授为首的“单纯性近视防治的临床研究及应用”科研团队正致力于将这个梦想变成现实，虽然这条道路是曲折而漫长的。\",\"imgUrl\":\"\",\"time\":\"2015-5-3 12:06:00\",\"viewCount\":\"28\",\"collectionCount\":\"0\",\"commentCount\":\"1\",\"collection\":false}]}";
				JSONObject json = new JSONObject(jsonstr);
				if (json.get("ret").equals("success")) {
					JSONArray jsonarray = json.getJSONArray("CampaignList");
					dbHelper.insertCampaignPosts(jsonarray, channel_id);
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
	public void onDestroyView() {
		super.onDestroyView();
		mAdapter = null ;
	};
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		onDestroyView();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getNewsData(channel_id);
	}

}
