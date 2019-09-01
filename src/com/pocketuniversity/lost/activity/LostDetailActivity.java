package com.pocketuniversity.lost.activity;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.pocketuniversity.activity.ChatActivity;
import com.pocketuniversity.activity.MainActivity;
import com.pocketuniversity.activity.UserDetailActivity;
import com.pocketuniversity.adapter.TopPicAdp;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Lost;
import com.pocketuniversity.bean.Tiaozao;
import com.pocketuniversity.bean.TopPic;
import com.pocketuniversity.bean.TopVpItem;
import com.pocketuniversity.friendcircle.activity.ImagePagerActivity;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.tiaozao.activity.TiaoZaoDetailActivity;
import com.pocketuniversity.utils.BaseTools;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;
import com.pocketuniversity.view.RoundImageView;

public class LostDetailActivity extends BaseActivity {

	private ImageView img_state;
	private TextView tv_title;
	private TextView tv_publishTime ;
	private TextView tvtime;
	private TextView time ;
	private TextView tvplace;
	private TextView place ;
	private TextView detail;
	private RoundImageView img_header;
	private TextView major ;
	private TextView name ;
	
	
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
	
	
	ArrayList<String> list = new ArrayList<String>();

	Bitmap bitmap;
	
	private Effectstype effect;
	
	private ScrollView scrollView;
	
	Lost curPost ;
	ImageLoader imageLoader = ImageLoader.getInstance();
	private Button btn_chat;
	private Button btn_call;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_lost_detail);
		initView();
	}
	
	private void initView(){
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("详情");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LostDetailActivity.this.finish();
			}
		});
		img_state = (ImageView) findViewById(R.id.stateId);
		tv_title = (TextView) findViewById(R.id.tvTitle);
		tv_publishTime = (TextView) findViewById(R.id.publishtime);
		tvtime = (TextView) findViewById(R.id.tv_time);
		time = (TextView) findViewById(R.id.time);
		tvplace = (TextView) findViewById(R.id.tv_place);
		place = (TextView) findViewById(R.id.place);
		detail = (TextView) findViewById(R.id.tvdetail);
		img_header = (RoundImageView) findViewById(R.id.header);
		name = (TextView) findViewById(R.id.name);
		major = (TextView) findViewById(R.id.major);
		
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
		
		btn_chat = (Button) findViewById(R.id.chat);
		btn_chat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Utils.getID().equals(curPost.getUserId())){
					Utils.toast("不能和自己聊天");
					return;
				}
				startActivity(new Intent(LostDetailActivity.this,
						ChatActivity.class).putExtra("userId",
						curPost.getUserId()).putExtra("username", curPost.getUserId()).putExtra("imgUrl",
						curPost.getHeader()));
				
			}
		});
		
		btn_call = (Button) findViewById(R.id.call);
		btn_call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String phone = curPost.getPhone();
				
				new SweetAlertDialog(ctx, SweetAlertDialog.CUSTOM_IMAGE_TYPE).showCancelButton(true)
				.setCustomImage(R.drawable.ic_contacts_call)
				.setTitleText("是否确定拨打电话？").setContentText("号码:"+phone).setConfirmText("确定")
			     .setCancelText("取消").setConfirmClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						sweetAlertDialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_CALL,
								Uri.parse("tel:" + phone));
						startActivity(intent);
					}
				}).setCancelClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						sweetAlertDialog.dismiss();
						
					}
				}).show();
				
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
		
		
	}
	
	public boolean getData(Intent intent){
		curPost =  (Lost) intent.getSerializableExtra("post");
		String imgUrl = curPost.getImgUrl();
		
		String title = curPost.getTitle();
		tv_title.setText(title);
		
		String publishtime = curPost.getPublishTime();
		tv_publishTime.setText(publishtime);
		
		//获取当前状态：
		String state = curPost.getState();
		if(state.equals("失物")){
			img_state.setImageResource(R.drawable.bg_lost_item);
			tvtime.setText("丢失时间：");
			tvplace.setText("丢失地点：");
		}else {
			img_state.setImageResource(R.drawable.bg_found_item);
			tvtime.setText("拾取时间：");
			tvplace.setText("拾取地点：");
		}
		
		String ttime = curPost.getLostTime();
		time.setText(ttime);
		
		String tplace = curPost.getPlace();
		place.setText(tplace);
		
		String tname = curPost.getNickname();
		name.setText(tname);
		
		String tmajor = curPost.getMajor();
		major.setText(tmajor);
		
		String theader = curPost.getHeader();
		imageLoader.displayImage(theader, img_header,
				PhotoUtils.getImageOptions(R.drawable.ic_default_head));
		
		String tdetail = curPost.getDetail();
		detail.setText(tdetail);
		return true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent = getIntent();
		if (getData(intent)){
			System.out.println("获取数据");
			getPicData();
		}else {
			startActivity(new Intent(this, MainActivity.class));
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			finish();
		}
	}
	
	private void getPicData(){
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
					if(topPostModels.size()==0){
						relaCenterVpParent.setVisibility(View.GONE);
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
								LostDetailActivity.this);
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

			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				if (showTopNews) {
					param.clear();
					param.put("id", curPost.getLostId());
					String jsonStr = new WebService(C.GETLOSTPIC, param)
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
