package com.pocketuniversity.tiaozao.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.activity.CampaignAddCommentActivity;
import com.pocketuniversity.activity.CampaignPostDetailActivity;
import com.pocketuniversity.activity.ChatActivity;
import com.pocketuniversity.activity.GroupDetailsActivity;
import com.pocketuniversity.activity.MainActivity;
import com.pocketuniversity.activity.PostDetailActivity;
import com.pocketuniversity.activity.UserDetailActivity;
import com.pocketuniversity.adapter.CommentListAdapter;
import com.pocketuniversity.adapter.TopPicAdp;
import com.pocketuniversity.adapter.TopVpAdp;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Comment;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.bean.PostModel;
import com.pocketuniversity.bean.Tiaozao;
import com.pocketuniversity.bean.TopPic;
import com.pocketuniversity.bean.TopVpItem;
import com.pocketuniversity.friendcircle.activity.ImagePagerActivity;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.BaseTools;
import com.pocketuniversity.utils.ImageUtils;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;
import com.pocketuniversity.view.RoundImageView;

public class TiaoZaoDetailActivity extends BaseActivity {

	private ImageView img_goods;
	private TextView tv_title;
	private TextView viewCount;
	private TextView price;
	private RoundImageView img_header;
	private TextView name;
	private TextView time;
	private TextView detail;

	private ScrollView scrollView;
	private ListView comment_list;
	private CommentListAdapter adapter;
	private List<Comment> commentdata = new ArrayList<Comment>();

	private RelativeLayout relaCenterVpParent;
	private ViewPager centerViewPager;
	private RadioGroup centerRadioGroup;
	private TopPicAdp topVpAdp;
	private List<TopVpItem> topVpItems = new ArrayList<TopVpItem>();
	private List<TopPic> topPostModels = new ArrayList<TopPic>();

	private boolean showTopNews = true;
	private RadioGroup.LayoutParams layoutParams;
	LinearLayout mRadioGroup_content;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	private ViewPager mViewPager;
	private int w, h;

	private ImageView blog_iscollection;

	boolean iscollection = false;
	MyProgressDialog dialog;


	private TextView tv_collection;

	Tiaozao curPost;
	private EditText blog_comment;

	ImageLoader imageLoader = ImageLoader.getInstance();
	ImageView imgView;

	int curCommentPage = 1;
	int curCommentCount = 40;

	Bitmap bitmap;

	private Button btn_chat;
	private Button btn_call;
	ArrayList<String> list = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_tiaozao_detail);
		initView();
		// Intent intent = getIntent();
		// if (setData(intent)) {
		// param.clear();
		// param.put("commodityID", curPost.getGoodsId());
		// param.put("page", curCommentPage + "");
		// param.put("count", curCommentCount + "");
		// initData();
		// } else {
		// startActivity(new Intent(this, MainActivity.class));
		// overridePendingTransition(R.anim.slide_in_right,
		// R.anim.slide_out_left);
		// finish();
		// }

	}

	private void initView() {
		dialog = new MyProgressDialog(ctx);

		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("宝贝详情");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TiaoZaoDetailActivity.this.finish();
			}
		});
		tv_title = (TextView) findViewById(R.id.tvTitle);
		viewCount = (TextView) findViewById(R.id.viewCount);
		price = (TextView) findViewById(R.id.price);
		tv_collection = (TextView) findViewById(R.id.tv_collection);
		img_header = (RoundImageView) findViewById(R.id.header);
		relaCenterVpParent = (RelativeLayout) findViewById(R.id.relaCenterVpParent);
		centerViewPager = (ViewPager) findViewById(R.id.centerViewPager);
		centerRadioGroup = (RadioGroup) findViewById(R.id.centerRadioGroup);
		w = getResources().getDrawable(R.drawable.radiobutton_normal)
				.getIntrinsicWidth();
		h = getResources().getDrawable(R.drawable.radiobutton_normal)
				.getIntrinsicHeight();

		layoutParams = new RadioGroup.LayoutParams(w, h);
		layoutParams.setMargins(0, 0, 30, 0);
		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 4;

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

		img_header.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						UserDetailActivity.class);
				intent.putExtra("userId", curPost.getUserId());
				startActivity(intent);
			}
		});
		name = (TextView) findViewById(R.id.name);
		detail = (TextView) findViewById(R.id.tvdetail);
		time = (TextView) findViewById(R.id.time);
		btn_chat = (Button) findViewById(R.id.chat);
		btn_chat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Utils.getID().equals(curPost.getUserId())) {
					Utils.toast("不能和自己聊天");
					return;
				}
				startActivity(new Intent(TiaoZaoDetailActivity.this,
						ChatActivity.class)
						.putExtra("userId", curPost.getUserId())
						.putExtra("username", curPost.getUserId())
						.putExtra("imgUrl", curPost.getHeader()));
			}
		});

		btn_call = (Button) findViewById(R.id.call);
		btn_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String phone = curPost.getPhone();

				new SweetAlertDialog(ctx, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
						.showCancelButton(true)
						.setCustomImage(R.drawable.ic_contacts_call)
						.setTitleText("是否确定拨打电话？")
						.setContentText("号码:" + phone).setConfirmText("确定")
						.setCancelText("取消")
						.setConfirmClickListener(new OnSweetClickListener() {

							@Override
							public void onClick(
									SweetAlertDialog sweetAlertDialog) {
								// TODO Auto-generated method stub
								sweetAlertDialog.dismiss();
								Intent intent = new Intent(Intent.ACTION_CALL,
										Uri.parse("tel:" + phone));
								startActivity(intent);
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

		comment_list = (ListView) findViewById(R.id.comment_list);
		blog_comment = (EditText) findViewById(R.id.blog_comment);
		blog_comment.requestFocus();

		blog_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TiaoZaoDetailActivity.this,
						AddTiaoZaoCommentActivity.class);
				intent.putExtra("postId", curPost.getGoodsId());

				intent.putExtra("cater", AddTiaoZaoCommentActivity.NEWS);

				TiaoZaoDetailActivity.this.startActivityForResult(intent, 0);
				overridePendingTransition(R.anim.roll_up, R.anim.roll);
			}
		});

		blog_iscollection = (ImageView) findViewById(R.id.img_collection);
		blog_iscollection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param.clear();
				String id = Utils.getID();
				param.put("userID", id);
				param.put("commodityID", curPost.getGoodsId());
				new SimpleNetTask(ctx, false) {
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
						// TODO Auto-generated method stub
						if (iscollection) {
							method = C.DELETEGOODSCOLLECTION;
						} else {
							method = C.ADDGOODSCOLLECTION;
						}
						// 收藏接口
						String jsonstr = new WebService(method, param)
								.getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonstr);
					}
				}.execute();
			}
		});

	}

	private void initData() {
		new SimpleNetTask(ctx, false) {

			@Override
			protected void onSucceed() {
				dialog.dismiss();
				adapter = new CommentListAdapter(commentdata, ctx);
				comment_list.setAdapter(adapter);
				setListViewHeightBasedOnChildren(comment_list);
			}

			@Override
			protected void doInBack() throws Exception {
				String jsonstr = "";

				jsonstr = new WebService(C.GETGOODSCOMMENTS, param)
						.getReturnInfo();
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

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			((Dialog) msg.obj).cancel();
			;
		}
	};

	
	public boolean setData(Intent intent) {
		curPost = (Tiaozao) intent.getSerializableExtra("post");
		String imgUrl = curPost.getImgUrl();
		imageLoader.displayImage(imgUrl, img_goods,
				PhotoUtils.getImageOptions(R.drawable.ic_list_empty));
		String title = curPost.getGoodsname();
		tv_title.setText(title);

		String tviewCount = curPost.getView_count();
		viewCount.setText(tviewCount);

		String tprice = curPost.getPrice();
		price.setText(tprice);

		String tname = curPost.getNickname();
		name.setText(tname);

		String ttime = curPost.getTime();
		time.setText(ttime);

		String theader = curPost.getHeader();
		imageLoader.displayImage(theader, img_header,
				PhotoUtils.getImageOptions(R.drawable.ic_default_head));

		String tdetail = curPost.getDetail();
		detail.setText(tdetail);

		if (curPost.getCollection().equals("已收藏")) {
			blog_iscollection
					.setImageResource(R.drawable.ic_collection_pressed);
			tv_collection.setText("已收藏");
			iscollection = true;
		} else {
			blog_iscollection.setImageResource(R.drawable.ic_collection_normal);
			tv_collection.setText("收藏");
			iscollection = false;
		}
		return true;
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

	
	private void initAction(){
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// initData();
		Intent intent = getIntent();
		if (setData(intent)) {
			param.clear();
			param.put("commodityID", curPost.getGoodsId());
			param.put("page", curCommentPage + "");
			param.put("count", curCommentCount + "");
			initData();
			getPicData();
		} else {
			startActivity(new Intent(this, MainActivity.class));
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			finish();
		}
	}


	private void getPicData() {
		new SimpleNetTask(ctx) {
			List<TopPic> temp = new ArrayList<TopPic>();

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if (showTopNews) {
					relaCenterVpParent.setVisibility(View.VISIBLE);
					topPostModels.clear();
					topVpItems.clear();
					topPostModels.addAll(temp);
					list.clear();
					for(int i = 0;i<topPostModels.size();i++){
						list.add(topPostModels.get(i).getImgUrl());
					}
					for (int i = 0; i < topPostModels.size(); i++) {
						TopVpItem topVpItem = new TopVpItem();
						topVpItem.relativeLayout = (RelativeLayout) getLayoutInflater()
								.inflate(R.layout.lay_center_vp_item, null);
						topVpItem.relativeLayout.setTag(i);
						topVpItem.imageView = (ImageView) topVpItem.relativeLayout
								.findViewById(R.id.ivViewPageItem);
						topVpItem.textView = (TextView) topVpItem.relativeLayout
								.findViewById(R.id.tvViewPageItem);
						topVpItem.relativeLayout
								.setOnClickListener(topImgsClickListenner);
						topVpItems.add(topVpItem);
					}
					topVpAdp = new TopPicAdp(topVpItems, topPostModels);
					centerViewPager.setAdapter(topVpAdp);
					centerRadioGroup.removeAllViews();
					for (int i = 0; i < topVpItems.size(); i++) {
						RadioButton radioButton = new RadioButton(
								TiaoZaoDetailActivity.this);
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
					param.clear();
					param.put("id", curPost.getGoodsId());
					String jsonStr = new WebService(C.GETGOODSPIC, param)
							.getReturnInfo();
					JSONObject json = new JSONObject(jsonStr);
					if (json.get("ret").equals("success")) {
						JSONArray jsonarray = json.getJSONArray("picList");
						temp = GetObjectFromService.getTopPicList(jsonarray);
					}
				}
			}
		}.execute();
	}

	private View.OnClickListener topImgsClickListenner = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int index = (Integer) v.getTag();// index
			imageBrower(index, list);
		}
	};
	
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}
}
