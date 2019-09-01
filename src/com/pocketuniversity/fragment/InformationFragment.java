package com.pocketuniversity.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.PostDetailActivity;
import com.pocketuniversity.adapter.CenterListAdp;
import com.pocketuniversity.adapter.TopVpAdp;
import com.pocketuniversity.bean.CategoryModel;
import com.pocketuniversity.bean.PostModel;
import com.pocketuniversity.bean.TopVpItem;
import com.pocketuniversity.db.Category;
import com.pocketuniversity.db.DBHelper;
import com.pocketuniversity.db.Post;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.utils.BaseTools;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.ColumnHorizontalScrollView;
import com.pocketuniversity.view.MyProgressDialog;

public class InformationFragment extends Fragment {
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

	private boolean showTopNews = true;

	public DBHelper dbHelper;
	private CenterListAdp mAdapter;
	private MyProgressDialog dialog;

	private int w, h;
	private RadioGroup.LayoutParams layoutParams;

	LinearLayout mRadioGroup_content;
	RelativeLayout rl_column;
	private ViewPager mViewPager;
	/** �û�ѡ������ŷ����б� */
	/** ��ǰѡ�е���Ŀ */
	private int columnSelectIndex = 0;
	/** ����Ӱ���� */
	public ImageView shade_left;
	/** ����Ӱ���� */
	public ImageView shade_right;
	/** ��Ļ��� */
	private int mScreenWidth = 0;
	/** Item��� */
	private int mItemWidth = 0;

	private ColumnHorizontalScrollView mColumnHorizontalScrollView;

//	/**
//	 * ģ��Json����
//	 */
//	String jsonArrayStr = "[{\"count\":0,\"categoryid\":\"5\",\"categoryName\":\"��������\",\"description\":\"��������\"},"
//			+ "{\"count\":0,\"categoryid\":\"4\",\"categoryName\":\"�й�����\",\"description\":\"�й�����\"},"
//			+ "{\"count\":0,\"categoryid\":\"1\",\"categoryName\":\"��ҽ����\",\"description\":\"��ҽ����\"},"
//			+ "{\"count\":0,\"categoryid\":\"2\",\"categoryName\":\"ʵ��������\",\"description\":\"ʵ��������\"},"
//			+ "{\"count\":0,\"categoryid\":\"3\",\"categoryName\":\"��������\",\"description\":\"��������\"}]";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_information_layout,
				container, false);
		news_listview = (ListView) view.findViewById(R.id.news_listview);
		shade_left = (ImageView) view.findViewById(R.id.shade_left);
		shade_right = (ImageView) view.findViewById(R.id.shade_right);
		relaCenterVpParent = (RelativeLayout) view
				.findViewById(R.id.relaCenterVpParent);
		centerViewPager = (ViewPager) view.findViewById(R.id.centerViewPager);
		centerRadioGroup = (RadioGroup) view
				.findViewById(R.id.centerRadioGroup);

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

	private void initData() {

		dbHelper = new DBHelper(getActivity());
		dbHelper.openSqLiteDatabase();
//		dbHelper.deleteAllDataFromTable(Category.tableName);
//		try {
//			JSONArray jsonArray = new JSONArray(jsonArrayStr);
//
//			System.out.println("jsonArray----->" + jsonArray);
//			dbHelper.insertCategories(jsonArray);
//			userChannelList = (ArrayList<CategoryModel>) dbHelper
//					.queryAllCategory();
			 userChannelList = (ArrayList<CategoryModel>) dbHelper
			 .queryAllCategory();
			if (userChannelList.size() != 0) {
				channel_text = userChannelList.get(0).getTitle();
				channel_id = userChannelList.get(0).getId();
				initTabColumn();
			}

//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	/**
	 * ��ʼ��Column��Ŀ��
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
		// �ж��Ƿ�ѡ��
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
			showTopNews = true;
		}
		getNewsData(channel_id);
	}

	private void initAction() {
		news_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 Intent intent = new Intent(getActivity(),
				 PostDetailActivity.class);
				 intent.putExtra("post", newsList.get(position));
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
					List<PostModel> models = dbHelper.queryPostsByCategoryId(-1
							+ "", currentPage * currentPageSize + "");
					topPostModels.addAll(models);

					for (int i = 0; i < topPostModels.size(); i++) {
						TopVpItem topVpItem = new TopVpItem();
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

					topVpAdp = new TopVpAdp(topVpItems, topPostModels);
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

				List<PostModel> newdata;
				newdata = dbHelper.queryPostsByCategoryId(channel_id + "",
						currentPage * currentPageSize + "");
				newsList.clear();
				newsList.addAll(newdata);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				mAdapter = new CenterListAdp(newsList, getActivity()
						.getLayoutInflater());
				news_listview.setAdapter(mAdapter);
				
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				if (showTopNews) {
					// String jsonStr = new APIHelper().getTopPosts();

					/**
					 * ģ������
					 */
					String jsonStr = " { \"ret\" : \"success\",\"ischanged\":\"true\",\"advertList\":"
							+ "[{\"newsID\":\"1\",\"description\":\"�������1\",\"title\":\"���1\",\"imgUrl\":\"http://app.iriscsc.com:8080/attached/image/20150502/20150502152106_9695.jpg\",\"source\":\"\",\"time\":\"\",\"viewCount\":\"\",\"collectionCount\":\"\",\"commentCount\":\"\",\"collection\":false},"
							+ "{\"newsID\":\"2\",\"description\":\"�������2\",\"title\":\"���2\",\"imgUrl\":\"http://app.iriscsc.com:8080/attached/image/20150502/20150502152858_5024.jpg\",\"source\":\"\",\"time\":\"\",\"viewCount\":\"\",\"collectionCount\":\"\",\"commentCount\":\"\",\"collection\":false}]} ";

					JSONObject json = new JSONObject(jsonStr);
					if (json.get("ret").equals("success")) {
						JSONArray jsonarray = json.getJSONArray("advertList");
						dbHelper.deleteAllDataFromTable(Post.tableName);
						dbHelper.insertPosts(jsonarray, -1 + "");
					}
					
				}
				
				
				String jsonStr = new APIHelper().getPostsByCategory(channel_id+"");
//				/**
//				 * �����б�ģ������
//				 */
//				String jsonNewsStr = "{\"ret\" : \"success\",\"ischanged\":\"true\",\"newsList\":"
//						+ "[{\"newsID\":\"51\",\"source\":\"�����\",\"title\":\"�����۾���10����\",\"description\":\"�۾�������������������١������У��кܶ�����������ɼ�����˺����˽���ЩΣ�������ڷ�����δȻ�����õر���˫�ۡ������Ϊ���ܽ�������10���£��������÷�����ʩ��\",\"imgUrl\":\"\",\"time\":\"2015-5-3 11:48:31\",\"viewCount\":\"213\",\"collectionCount\":\"7\",\"commentCount\":\"15\",\"collection\":false}]}";
//				
//				String jsonNewsStr2 = "{\"ret\" : \"success\",\"ischanged\":\"true\",\"newsList\":"
//						+ "[{\"newsID\":\"57\",\"source\":\"�����\",\"title\":\"������˳����۲�\",\"description\":\"�ڽ������������ӡ�����Ҳ�ܳ�Ϊһ��ɸ���ۿƼ����ķ�ʽ�������׶س��д�ѧ��һ�����о���ʾ�������ۿ�ӰƬʱ������˶���ʽ���԰�������ʶ������ۻ��ߡ������о����԰���������ϣ��Ա�ҽ���ܹ������ʶ�����������˺�����֮ǰ�������ơ������Ľ��ڷ����ڡ��ϻ��񾭿�ѧǰ�ء��ڿ��ϡ�\",\"imgUrl\":\"http://app.iriscsc.com:8080/attached/image/20150503/20150503121053_4450.jpg\",\"time\":\"2015-5-3 12:11:20\",\"viewCount\":\"94\",\"collectionCount\":\"5\",\"commentCount\":\"8\",\"collection\":false},"
//						+ "{\"newsID\":\"56\",\"source\":\"�����\",\"title\":\"�ɵ��������۾������������������Գ���\",\"description\":\" �ݡ������±����������������������һ�ֿɵ������������ӵ��۾������������ƻ�̼�����˯�ߺɶ��ɵĲ������Ӷ��ﵽ�����������ڵ�Ч���������۾���������Ϊ�����ͳ�����ʿ�ıر�Ʒ������Ϊ�����˶�Ա��ѵ�����ճ������ṩ������\",\"imgUrl\":\"http://app.iriscsc.com:8080/attached/image/20150503/20150503120745_1010.jpg\",\"time\":\"2015-5-3 12:08:26\",\"viewCount\":\"57\",\"collectionCount\":\"0\",\"commentCount\":\"2\",\"collection\":false},"
//						+ "{\"newsID\":\"55\",\"source\":\"�����\",\"title\":\"�������۾���С����\",\"description\":\"����Լ���ҩ���������ܽ������ŵ�������۾��ľ�������������ͷ�����ȳԼ���ҩ����Ȭ����ô�򵥣������ڶ���ӻ��ߵ����롣������ҽѧԺ���ļѽ���Ϊ�׵ġ������Խ��ӷ��ε��ٴ��о���Ӧ�á������Ŷ��������ڽ������������ʵ����Ȼ������·�����۶������ġ�\",\"imgUrl\":\"\",\"time\":\"2015-5-3 12:06:00\",\"viewCount\":\"28\",\"collectionCount\":\"0\",\"commentCount\":\"1\",\"collection\":false}]}";
				
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
