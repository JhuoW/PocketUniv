package com.pocketuniversity.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.pocketuniversity.adapter.CampaignCenterListAdp;
import com.pocketuniversity.adapter.CampaignTopVpAdp;
import com.pocketuniversity.base.App;
import com.pocketuniversity.bean.CampaignCategoryModel;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.bean.CampaignTopVpItem;
import com.pocketuniversity.db.CampaignPost;
import com.pocketuniversity.db.DBHelper;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.more.activity.CampaignCollectionActivitity;
import com.pocketuniversity.more.activity.MyPublishCampaignActivity;
import com.pocketuniversity.poptitle.ActionItem;
import com.pocketuniversity.poptitle.TitlePopup;
import com.pocketuniversity.poptitle.TitlePopup.OnItemOnClickListener;
import com.pocketuniversity.tiaozao.activity.AddTiaoZaoActivity;
import com.pocketuniversity.tiaozao.activity.MyUploadTiaoZaoActivity;
import com.pocketuniversity.tiaozao.activity.TiaoZaoActivity;
import com.pocketuniversity.utils.BaseTools;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.ColumnHorizontalScrollView;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class CampaignActivity extends BaseActivity {

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
	
	private TitlePopup titlePopup;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_campaign);
		findView();
		init();
		initData();
		initAction();
	}

	private void findView() {
		mAdapter = new CampaignCenterListAdp(newsList, 
				getLayoutInflater());
		dialog = new MyProgressDialog(ctx);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("�");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CampaignActivity.this.finish();
			}
		});
		headerLayout.showRightImageButton(R.drawable.ic_fifter, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startActivity(new Intent(CampaignActivity.this, AddCampaignActivity.class));
				titlePopup.show(v);
			}
		});
		news_listview = (ListView) findViewById(R.id.campaign_listview);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		relaCenterVpParent = (RelativeLayout) findViewById(R.id.relaCenterVpParent);
		centerViewPager = (ViewPager) findViewById(R.id.centerViewPager);
		centerRadioGroup = (RadioGroup) findViewById(R.id.centerRadioGroup);

		btn_myCollection = (Button) findViewById(R.id.MyCollectCampaign);

		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);

		w = getResources().getDrawable(R.drawable.radiobutton_normal)
				.getIntrinsicWidth();
		h = getResources().getDrawable(R.drawable.radiobutton_normal)
				.getIntrinsicHeight();
		layoutParams = new RadioGroup.LayoutParams(w, h);
		layoutParams.setMargins(0, 0, 30, 0);

		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 3;
	}

	private void init(){
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.addAction(new ActionItem(this, "�����", R.drawable.ic_func_edit));
		titlePopup.addAction(new ActionItem(this, "�ҵ��ղ�", R.drawable.ic_func_collection));
		titlePopup.addAction(new ActionItem(this, "�ҵĲ���", R.drawable.ic_func_join));
		titlePopup.addAction(new ActionItem(this, "�ҵķ���",R.drawable.ic_plugin_notification));
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if(position == 0){
					startActivity(new Intent(CampaignActivity.this, AddCampaignActivity.class));
				}else if (position == 1) {
					startActivity(new Intent(CampaignActivity.this, CampaignCollectionActivitity.class));
				}else if(position == 2){
					startActivity(new Intent(CampaignActivity.this, MyJoinCampaignActivity.class));
				}else{
					startActivity(new Intent(CampaignActivity.this, MyPublishCampaignActivity.class));
				}
			}
		});
		
	}
	
	private void initData() {

		dbHelper = new DBHelper(this);
		dbHelper.openSqLiteDatabase();

		userChannelList = (ArrayList<CampaignCategoryModel>) dbHelper
				.queryAllCampaignCategory();
		if (userChannelList.size() != 0) {
			channel_text = userChannelList.get(0).getTitle();
			channel_id = userChannelList.get(0).getId();
			initTabColumn();

		}

	}
	
	/**
	 * ��ʼ��Column��Ŀ��
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
			//����ʾ����
			showTopNews = false;
		}
		getNewsData(channel_id);
	}
	private void initAction() {
		news_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 Intent intent = new Intent(CampaignActivity.this,
				 CampaignPostDetailActivity.class);
				 intent.putExtra("post", newsList.get(position));
				//intent.putExtra("isJoin", newsList.get(position).isJoin());
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
			List<CampaignPostModel> temp = new ArrayList<CampaignPostModel>();
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
						topVpItem.relativeLayout = (RelativeLayout)
								getLayoutInflater().inflate(
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
						RadioButton radioButton = new RadioButton(CampaignActivity.this);
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
				temp = dbHelper.queryCampaignPostsByCategoryId(channel_id + "",
						currentPage * currentPageSize + "");
				newsList.clear();
				newsList.addAll(temp);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				
				news_listview.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
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
						dbHelper.deleteAllDataFromTable(CampaignPost.tableName);
						dbHelper.insertCampaignPosts(jsonarray, -1 + "");
					}
				}
				
				
				
				
				String jsonstr = new APIHelper().getPostsByCampaignCategory(channel_id
						+ "");
				Log.i("NewsFragment", "getPostsByCategory�����ķ���ֵ--->" + jsonstr);
				/**
				 * �����б�ģ������
				 */
//				String jsonNewsStr = "{\"ret\" : \"success\",\"ischanged\":\"true\",\"newsList\":"
//						+ "[{\"newsID\":\"51\",\"source\":\"�����\",\"title\":\"�����۾���10����\",\"description\":\"�۾�������������������١������У��кܶ�����������ɼ�����˺����˽���ЩΣ�������ڷ�����δȻ�����õر���˫�ۡ������Ϊ���ܽ�������10���£��������÷�����ʩ��\",\"imgUrl\":\"\",\"time\":\"2015-5-3 11:48:31\",\"viewCount\":\"213\",\"collectionCount\":\"7\",\"commentCount\":\"15\",\"collection\":false}]}";
//				
//				String jsonNewsStr2 = "{\"ret\" : \"success\",\"ischanged\":\"true\",\"newsList\":"
//						+ "[{\"newsID\":\"57\",\"source\":\"�����\",\"title\":\"������˳����۲�\",\"description\":\"�ڽ������������ӡ�����Ҳ�ܳ�Ϊһ��ɸ���ۿƼ����ķ�ʽ�������׶س��д�ѧ��һ�����о���ʾ�������ۿ�ӰƬʱ������˶���ʽ���԰�������ʶ������ۻ��ߡ������о����԰���������ϣ��Ա�ҽ���ܹ������ʶ�����������˺�����֮ǰ�������ơ������Ľ��ڷ����ڡ��ϻ��񾭿�ѧǰ�ء��ڿ��ϡ�\",\"imgUrl\":\"http://app.iriscsc.com:8080/attached/image/20150503/20150503121053_4450.jpg\",\"time\":\"2015-5-3 12:11:20\",\"viewCount\":\"94\",\"collectionCount\":\"5\",\"commentCount\":\"8\",\"collection\":false},"
//						+ "{\"newsID\":\"56\",\"source\":\"�����\",\"title\":\"�ɵ��������۾������������������Գ���\",\"description\":\" �ݡ������±����������������������һ�ֿɵ������������ӵ��۾������������ƻ�̼�����˯�ߺɶ��ɵĲ������Ӷ��ﵽ�����������ڵ�Ч���������۾���������Ϊ�����ͳ�����ʿ�ıر�Ʒ������Ϊ�����˶�Ա��ѵ�����ճ������ṩ������\",\"imgUrl\":\"http://app.iriscsc.com:8080/attached/image/20150503/20150503120745_1010.jpg\",\"time\":\"2015-5-3 12:08:26\",\"viewCount\":\"57\",\"collectionCount\":\"0\",\"commentCount\":\"2\",\"collection\":false},"
//						+ "{\"newsID\":\"55\",\"source\":\"�����\",\"title\":\"�������۾���С����\",\"description\":\"����Լ���ҩ���������ܽ������ŵ�������۾��ľ�������������ͷ�����ȳԼ���ҩ����Ȭ����ô�򵥣������ڶ���ӻ��ߵ����롣������ҽѧԺ���ļѽ���Ϊ�׵ġ������Խ��ӷ��ε��ٴ��о���Ӧ�á������Ŷ��������ڽ������������ʵ����Ȼ������·�����۶������ġ�\",\"imgUrl\":\"\",\"time\":\"2015-5-3 12:06:00\",\"viewCount\":\"28\",\"collectionCount\":\"0\",\"commentCount\":\"1\",\"collection\":false}]}";
				JSONObject json = new JSONObject(jsonstr);
				if (json.get("ret").equals("success")) {
					JSONArray jsonarray = json.getJSONArray("CampaignList");
					dbHelper.deleteAllDataFromTable(CampaignPost.tableName);
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
