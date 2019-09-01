package com.pocketuniversity.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.pocketuniversity.R;
import com.example.pullrefersh.PullToRefreshView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.activity.CampaignActivity;
import com.pocketuniversity.activity.CampaignPostDetailActivity;
import com.pocketuniversity.activity.InformationActivity;
import com.pocketuniversity.activity.PostDetailActivity;
import com.pocketuniversity.adapter.TopVpAdp;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.bean.PostModel;
import com.pocketuniversity.bean.Tiaozao;
import com.pocketuniversity.bean.TopVpItem;
import com.pocketuniversity.browser.greendroid.QuickAction;
import com.pocketuniversity.browser.greendroid.QuickActionGrid;
import com.pocketuniversity.browser.greendroid.QuickActionWidget;
import com.pocketuniversity.browser.greendroid.QuickActionWidget.OnQuickActionClickListener;
import com.pocketuniversity.db.DBHelper;
import com.pocketuniversity.db.FirstThreePost;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.lost.activity.LostActivity;
import com.pocketuniversity.tiaozao.activity.TiaoZaoActivity;
import com.pocketuniversity.tiaozao.activity.TiaoZaoDetailActivity;
import com.pocketuniversity.utils.BaseTools;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.utils.StringUtils;
import com.pocketuniversity.utils.UserPreference;
import com.show.api.ShowApiRequest;

public class FindFragment extends Fragment{

	public static final int REFRESH_DELAY = 2000;

	private PullToRefreshView mPullToRefreshView;

	private LinearLayout ll_gotoCampaign;
	private LinearLayout ll_tiaozao;
	private RelativeLayout relaCenterVpParent;
	private ViewPager centerViewPager;
	private RadioGroup centerRadioGroup;
	private TopVpAdp topVpAdp;
	private List<TopVpItem> topVpItems = new ArrayList<TopVpItem>();
	private List<PostModel> topPostModels = new ArrayList<PostModel>();
	private List<CampaignPostModel> newsList = new ArrayList<CampaignPostModel>();
	private List<Tiaozao> tisozaoList = new ArrayList<Tiaozao>();

	private ScrollView sv;
	private boolean showTopNews = true;
	private RadioGroup.LayoutParams layoutParams;
	LinearLayout mRadioGroup_content;
	public DBHelper dbHelper;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	private ViewPager mViewPager;
	private int w, h;
	ImageLoader imageLoader = ImageLoader.getInstance();

	private ImageView img_firstCampaign;
	private ImageView img_secondCampaign;
	private TextView tv_firstCampaign;
	private TextView tv_secondCampaign;

	private ImageView img_tiaozao1;
	private ImageView img_tiaozao2;
	private ImageView img_tiaozao3;

	ImageView[] mImage;
	ImageView[] mCampaignImg;
	TextView[] mCampaignTxt;
	ImageView news;
	ImageView campaign;
	ImageView tiaozao;
	ImageView lost;

	String firstCampaignId;
	String secondCampaignId;

	private EditText et_trancontent;
	private Button btn_trans;
	private EditText et_tranresult;
	
	ProgressDialog dialog;

	private boolean mToolsActionGridVisible = false;
	private ImageView img_menu;
	private QuickActionGrid mToolsActionGrid;
	public static boolean isNeedLoadHide = false;// 不需要加载

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_find, container, false);
		System.out.println("view create ");
		mPullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.pull_to_refresh);
		img_menu = (ImageView) view.findViewById(R.id.content_hamburger);
		ll_gotoCampaign = (LinearLayout) view.findViewById(R.id.goToCampaign);
		ll_tiaozao = (LinearLayout) view.findViewById(R.id.goToTiaozao);
		img_firstCampaign = (ImageView) view.findViewById(R.id.img_firstCam);
		img_secondCampaign = (ImageView) view.findViewById(R.id.img_secondCam);
		mCampaignImg = new ImageView[] { img_firstCampaign, img_secondCampaign };
		img_tiaozao1 = (ImageView) view.findViewById(R.id.tiaozao1);
		img_tiaozao2 = (ImageView) view.findViewById(R.id.tiaozao2);
		img_tiaozao3 = (ImageView) view.findViewById(R.id.tiaozao3);
		mImage = new ImageView[] { img_tiaozao1, img_tiaozao2, img_tiaozao3 };
		sv = (ScrollView) view.findViewById(R.id.scrollView);
		tv_firstCampaign = (TextView) view.findViewById(R.id.tv_firstCam);
		tv_secondCampaign = (TextView) view.findViewById(R.id.tv_secondCam);
		mCampaignTxt = new TextView[] { tv_firstCampaign, tv_secondCampaign };
		relaCenterVpParent = (RelativeLayout) view
				.findViewById(R.id.relaCenterVpParent);
		news = (ImageView) view.findViewById(R.id.news);
		campaign = (ImageView) view.findViewById(R.id.campaign);
		tiaozao = (ImageView) view.findViewById(R.id.tiaozao);
		lost = (ImageView) view.findViewById(R.id.lost);
		centerViewPager = (ViewPager) view.findViewById(R.id.centerViewPager);
		centerRadioGroup = (RadioGroup) view
				.findViewById(R.id.centerRadioGroup);
		w = getResources().getDrawable(R.drawable.radiobutton_normal)
				.getIntrinsicWidth();
		h = getResources().getDrawable(R.drawable.radiobutton_normal)
				.getIntrinsicHeight();

		layoutParams = new RadioGroup.LayoutParams(w, h);
		layoutParams.setMargins(0, 0, 30, 0);
		mScreenWidth = BaseTools.getWindowsWidth(getActivity());
		mItemWidth = mScreenWidth / 4;

		et_trancontent = (EditText) view.findViewById(R.id.et_value);
		btn_trans = (Button) view.findViewById(R.id.btn_tran);
		et_tranresult = (EditText) view.findViewById(R.id.et_result);
		dialog = new ProgressDialog(getActivity());
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("翻译中......");
		
		UserPreference.ensureIntializePreference(getActivity());

		boolean isFullScreen = UserPreference.read("fullScreen", false);
		if (isFullScreen) {
			getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getActivity().getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		isNeedLoadHide = false;
		mToolsActionGrid = new QuickActionGrid(getActivity());
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initAction();
		getNewsData();
		// getFirstTwoCampaign();
		// getFirstThreeTiaozao();
		getData();
		initTrans();
	}

	private void getData() {
		new SimpleNetTask(getActivity(), false) {
			JSONArray jsonarraytz;
			JSONArray jsonarrayCam;

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				tisozaoList.clear();
				newsList.clear();

				for (int i = 0; i < jsonarraytz.length(); i++) {
					Tiaozao tiaozao = new Tiaozao();
					try {
						JSONObject tiaozaoObj = jsonarraytz.getJSONObject(i);
						String userId = tiaozaoObj.getString("studentID");
						String nickName = tiaozaoObj.getString("nickname");
						String header = tiaozaoObj.getString("picture");
						String goodsName = tiaozaoObj
								.getString("commodityName");
						String imgUrl = tiaozaoObj.getString("imgUrl");
						String price = tiaozaoObj.getString("price");
						String time = tiaozaoObj.getString("time");
						String viewCount = tiaozaoObj.getString("viewCount");
						String goodsId = tiaozaoObj.getString("commodityID");
						String phone = tiaozaoObj.getString("phone");
						String detail = tiaozaoObj.getString("detail");
						String collection = tiaozaoObj.getString("collection");
						tiaozao.setUserId(userId);
						tiaozao.setNickname(nickName);
						tiaozao.setHeader(header);
						tiaozao.setGoodsname(goodsName);
						tiaozao.setImgUrl(imgUrl);
						tiaozao.setPrice(price);
						tiaozao.setTime(time);
						tiaozao.setView_count(viewCount);
						tiaozao.setGoodsId(goodsId);
						tiaozao.setPhone(phone);
						tiaozao.setDetail(detail);
						tiaozao.setCollection(collection);
						tisozaoList.add(tiaozao);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (tisozaoList.size() == 0) {
					mImage[0].setVisibility(View.INVISIBLE);
					mImage[1].setVisibility(View.INVISIBLE);
					mImage[2].setVisibility(View.INVISIBLE);
				} else if (tisozaoList.size() == 1) {
					mImage[0].setVisibility(View.VISIBLE);
					mImage[1].setVisibility(View.INVISIBLE);
					mImage[2].setVisibility(View.INVISIBLE);
					imageLoader
							.displayImage(
									tisozaoList.get(0).getImgUrl(),
									mImage[0],
									PhotoUtils
											.getImageOptions(R.drawable.ic_default_retangle));
				} else if (tisozaoList.size() == 2) {
					mImage[0].setVisibility(View.VISIBLE);
					mImage[1].setVisibility(View.VISIBLE);
					mImage[2].setVisibility(View.INVISIBLE);
					imageLoader
							.displayImage(
									tisozaoList.get(0).getImgUrl(),
									mImage[0],
									PhotoUtils
											.getImageOptions(R.drawable.ic_default_retangle));
					imageLoader
							.displayImage(
									tisozaoList.get(1).getImgUrl(),
									mImage[1],
									PhotoUtils
											.getImageOptions(R.drawable.ic_default_retangle));
				} else {
					mImage[0].setVisibility(View.VISIBLE);
					mImage[1].setVisibility(View.VISIBLE);
					mImage[2].setVisibility(View.VISIBLE);
					for (int i = 0; i < tisozaoList.size(); i++) {
						imageLoader
								.displayImage(
										tisozaoList.get(i).getImgUrl(),
										mImage[i],
										PhotoUtils
												.getImageOptions(R.drawable.ic_default_retangle));

					}
				}

				try {

					for (int i = 0; i < jsonarrayCam.length(); i++) {
						CampaignPostModel post = new CampaignPostModel();
						JSONObject jsonObject = jsonarrayCam.getJSONObject(i);
						String id = jsonObject.getString("newsID");
						String imgUrl = jsonObject.getString("imgUrl");
						String title = jsonObject.getString("title");
						String source = jsonObject.getString("source");
						String place = jsonObject.getString("place");
						String betime = jsonObject.getString("betime");
						String time = jsonObject.getString("time");
						String commentCount = jsonObject
								.getString("commentCount");
						String viewCount = jsonObject.getString("viewCount");
						String collection = jsonObject.getString("collection");
						String isJoin = jsonObject.getString("isJoin");
						String attendCount = jsonObject
								.getString("attendCount");
						post.setImageurl(imgUrl);
						post.setSource(source);
						post.setTitle(title);
						post.setPlace(place);
						post.setContent(betime);
						post.setTime(time);
						post.setComment_count(commentCount);
						post.setView_count(viewCount);
						post.setCollection(collection);
						post.setJoin(isJoin);
						post.setHasJoin(attendCount);
						post.setId(id);
						newsList.add(post);
					}

					if (newsList.size() == 0) {
						mCampaignImg[0].setVisibility(View.INVISIBLE);
						mCampaignImg[1].setVisibility(View.INVISIBLE);
						mCampaignTxt[0].setVisibility(View.INVISIBLE);
						mCampaignTxt[1].setVisibility(View.INVISIBLE);
					} else if (newsList.size() == 1) {
						mCampaignImg[0].setVisibility(View.VISIBLE);
						mCampaignTxt[0].setVisibility(View.VISIBLE);
						mCampaignImg[1].setVisibility(View.INVISIBLE);
						mCampaignTxt[1].setVisibility(View.INVISIBLE);
						imageLoader
								.displayImage(
										newsList.get(0).getImageurl(),
										mCampaignImg[0],
										PhotoUtils
												.getImageOptions(R.drawable.ic_default_retangle));
						mCampaignTxt[0].setText(newsList.get(0).getTitle());
					} else if (newsList.size() == 2) {
						mCampaignImg[0].setVisibility(View.VISIBLE);
						mCampaignTxt[0].setVisibility(View.VISIBLE);
						mCampaignImg[1].setVisibility(View.VISIBLE);
						mCampaignTxt[1].setVisibility(View.VISIBLE);
						for (int i = 0; i < newsList.size(); i++) {
							imageLoader
									.displayImage(
											newsList.get(i).getImageurl(),
											mCampaignImg[i],
											PhotoUtils
													.getImageOptions(R.drawable.ic_default_retangle));
							mCampaignTxt[i].setText(newsList.get(i).getTitle());
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				sv.setVisibility(View.VISIBLE);
				mPullToRefreshView.setRefreshing(false);
				// mPullToRefreshView.clearAnimation();
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				String jsonTiaozao = new APIHelper().getFirstThreeTiaozao();
				JSONObject jsontz = new JSONObject(jsonTiaozao);
				if (jsontz.get("ret").equals("success")) {
					jsonarraytz = jsontz.getJSONArray("FirstThreeCommodity");
				}

				String jsonCampaign = new APIHelper().getFirstTwoCampaign();
				JSONObject json = new JSONObject(jsonCampaign);
				if (json.get("ret").equals("success")) {
					jsonarrayCam = json.getJSONArray("twoCampaign");
				}
			}
		}.execute();

	}

	private void initAction() {
		
		mToolsActionGrid.addQuickAction(new QuickAction(getActivity(),
				R.drawable.ic_btn_home, R.string.QuickAction_Home));
		mToolsActionGrid.addQuickAction(new QuickAction(getActivity(),
				R.drawable.ic_btn_share, R.string.QuickAction_Share));
		mToolsActionGrid.addQuickAction(new QuickAction(getActivity(),
				R.drawable.ic_btn_find, R.string.QuickAction_Find));
		// mToolsActionGrid.addQuickAction(new QuickAction(this,
		// R.drawable.ic_btn_select, R.string.QuickAction_SelectText));
		mToolsActionGrid.addQuickAction(new QuickAction(getActivity(),
				R.drawable.notification_start, R.string.Download));
		
		mToolsActionGrid
		.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				mToolsActionGridVisible = false;
			}
		});
		
		mToolsActionGrid.setOnQuickActionClickListener(new OnQuickActionClickListener() {
			
			@Override
			public void onQuickActionClicked(QuickActionWidget widget, int position) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					
					break;

				default:
					break;
				}
			}
		});
		
		img_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mToolsActionGridVisible = true;
				mToolsActionGrid.show(v);
			}
		});
		mPullToRefreshView
				.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
					@Override
					public void onRefresh() {
						getData();
						getNewsData();
					}
				});

		img_tiaozao1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						TiaoZaoDetailActivity.class);
				intent.putExtra("post", tisozaoList.get(0));
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		img_tiaozao2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						TiaoZaoDetailActivity.class);
				intent.putExtra("post", tisozaoList.get(1));
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		img_tiaozao3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						TiaoZaoDetailActivity.class);
				intent.putExtra("post", tisozaoList.get(2));
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		img_firstCampaign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						CampaignPostDetailActivity.class);
				intent.putExtra("post", newsList.get(0));
				// intent.putExtra("isJoin", newsList.get(position).isJoin());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		img_secondCampaign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						CampaignPostDetailActivity.class);
				intent.putExtra("post", newsList.get(1));
				// intent.putExtra("isJoin", newsList.get(position).isJoin());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		ll_gotoCampaign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), CampaignActivity.class));
			}
		});
		ll_tiaozao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), TiaoZaoActivity.class));
			}
		});
		tiaozao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), TiaoZaoActivity.class));
			}
		});

		campaign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), CampaignActivity.class));
			}
		});

		lost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), LostActivity.class));
			}
		});

		news.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(),
						InformationActivity.class));
			}
		});
		dbHelper = new DBHelper(getActivity());
		dbHelper.openSqLiteDatabase();
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

	private void initTrans() {
		btn_trans.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.show();
				String transcontent = et_trancontent.getText().toString();
				new ShowApiRequest("http://route.showapi.com/32-9", "6649",
						"acaadc0e1924440b82a13a859b4952ef")
						.setResponseHandler(resHandler)
						.addTextPara("q", transcontent).post();
			}
		});
	}

	private int currentPage = 1;
	private int currentPageSize = 40;

	private void getNewsData() {
		new SimpleNetTask(getActivity(), false) {
			@Override
			protected void onSucceed() {
				if (showTopNews) {
					relaCenterVpParent.setVisibility(View.VISIBLE);
					topPostModels.clear();
					topVpItems.clear();
					List<PostModel> models = dbHelper
							.queryFirstThreePosts(currentPage * currentPageSize
									+ "");
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

				} else {
					relaCenterVpParent.setVisibility(View.GONE);
				}
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				if (showTopNews) {
					String jsonStr = new APIHelper().getTopPosts();

					// /**
					// * 模拟数据
					// */
					// String jsonStr =
					// " { \"ret\" : \"success\",\"ischanged\":\"true\",\"advertList\":"
					// +
					// "[{\"newsID\":\"1\",\"description\":\"广告描述1\",\"title\":\"广告1\",\"imgUrl\":\"http://app.iriscsc.com:8080/attached/image/20150502/20150502152106_9695.jpg\",\"source\":\"\",\"time\":\"\",\"viewCount\":\"\",\"collectionCount\":\"\",\"commentCount\":\"\",\"collection\":\"已收藏\"},"
					// +
					// "{\"newsID\":\"2\",\"description\":\"广告描述2\",\"title\":\"广告2\",\"imgUrl\":\"http://app.iriscsc.com:8080/attached/image/20150502/20150502152858_5024.jpg\",\"source\":\"\",\"time\":\"\",\"viewCount\":\"\",\"collectionCount\":\"\",\"commentCount\":\"\",\"collection\":\"未收藏\"}]} ";

					JSONObject json = new JSONObject(jsonStr);
					if (json.get("ret").equals("success")) {
						JSONArray jsonarray = json
								.getJSONArray("firstThreeNews");
						dbHelper.deleteAllDataFromTable(FirstThreePost.tableName);
						dbHelper.insertFirstThreePosts(jsonarray);
					}
				}
			}
		}.execute();
	}

	private View.OnClickListener topImgsClickListenner = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int index = (Integer) v.getTag();// index
			Intent intent = new Intent(getActivity(), PostDetailActivity.class);
			intent.putExtra("post", topPostModels.get(index));
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_r_to_0,
					R.anim.anim_0_to_l);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		// getFirstThreeTiaozao();
		// getFirstTwoCampaign();
		getNewsData();
		getData();
	};

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("onStart");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	final AsyncHttpResponseHandler resHandler = new AsyncHttpResponseHandler() {
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable e) {
			// 做一些异常处理
			dialog.dismiss();
			e.printStackTrace();
		}

		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				dialog.dismiss();
				String jsonStr = new String(responseBody, "utf-8");
				System.out.println("translate--" + jsonStr);
				try {
					JSONObject json = new JSONObject(jsonStr);
					JSONObject jsonobj = json.getJSONObject("showapi_res_body");
					String result = jsonobj.getString("translation");
					String s1 = StringUtils.trimFirstAndLastChar(result, "[".charAt(0));
					String s2 = StringUtils.trimFirstAndLastChar(s1, "]".charAt(0));
					String s3 = StringUtils.trimFirstAndLastChar(s2, "\"".charAt(0));
					String s4 = StringUtils.trimFirstAndLastChar(s3, "\"".charAt(0));
					et_tranresult.setText(s4);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
	};
	
}
