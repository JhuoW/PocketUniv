package com.pocketuniversity.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;
import org.kobjects.base64.Base64;

import u.aly.bt;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbStrUtil;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.UserUtils;
import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.geniuseoe.spiner.demo.widget.AbstractSpinerAdapter;
import com.geniuseoe.spiner.demo.widget.SpinerPopWindow;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.CampaignCategoryModel;
import com.pocketuniversity.db.DBHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

/**
 * 设置图片，设置内容
 * 
 * @author Administrator
 * 
 */
public class AddCampaignActivity extends BaseActivity implements
		OnClickListener, AbstractSpinerAdapter.IOnItemSelectListener {

	private static final int CAMERA_WITH_DATA = 3023;
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	private static final int CAMERA_CROP_DATA = 3022;

	private ImageButton img_chooseCategory;
	private TextView tvcategory;

	private ImageView iv_campaign;
	private List<String> campaignCategory = new ArrayList<String>();
	private List<CampaignCategoryModel> userChannelList = new ArrayList<CampaignCategoryModel>();
	public DBHelper dbHelper;
	List<String> list;

	private RelativeLayout ll_lowwarn;
	
	private EditText et_title;
	private EditText et_place;

	// 开始时间
	private ImageButton img_btime;
	private TextView tv_btime;

	// 结束时间
	private ImageButton img_etime;
	private TextView tv_etime;

	private EditText et_content;
	
	private String mFileName;
	private File mCurrentPhotoFile;
	private File PHOTO_DIR = null;
	String uploadBuffer;
	
	private String categoryNum;

	MyProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_add_campaign);
		initView();
		initAction();
		setListener();
	}

	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("添加活动");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddCampaignActivity.this.finish();
			}
		});

		ll_lowwarn = (RelativeLayout) findViewById(R.id.ll_lowwarn);
		et_title = (EditText) findViewById(R.id.title);
		et_place = (EditText) findViewById(R.id.place);
		iv_campaign = (ImageView) findViewById(R.id.iv_campaign);
		img_chooseCategory = (ImageButton) findViewById(R.id.bt_dropdown);
		tvcategory = (TextView) findViewById(R.id.tv_value);
		et_content = (EditText) findViewById(R.id.content);
		dialog = new MyProgressDialog(this);
		tv_btime = (TextView) findViewById(R.id.btime);
		tv_etime = (TextView) findViewById(R.id.etime);
		img_btime = (ImageButton) findViewById(R.id.bt_dropdownBtime);
		img_etime = (ImageButton) findViewById(R.id.bt_dropdownEtime);

		iv_campaign.setOnClickListener(this);
		img_chooseCategory.setOnClickListener(this);
		img_btime.setOnClickListener(this);
		img_etime.setOnClickListener(this);
		ll_lowwarn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_campaign:
			showPhotoDialog();
			break;
		case R.id.ll_lowwarn:
			showSpinWindow();
			break;
		case R.id.bt_dropdown:
			showSpinWindow();
			break;
		case R.id.bt_dropdownBtime:
			showTimeDialog();
			break;
		case R.id.bt_dropdownEtime:
			showTimeDialog2();
			break;
		}
	}

	private SpinerPopWindow mSpinerPopWindow;

	private void showSpinWindow() {
		Log.e("", "showSpinWindow");
		mSpinerPopWindow.setWidth(tvcategory.getWidth());
		mSpinerPopWindow.showAsDropDown(tvcategory);
	}

	private String getTime(){
		SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
		String    date    =    sDateFormat.format(new    java.util.Date());
		return date;
	}
	
	
	private void initAction() {
		headerLayout.showRightTextButton("提交", new OnClickListener() {

			@Override
			public void onClick(View v) {
				String category = tvcategory.getText().toString();
				String title = et_title.getText().toString();
				String place = et_place.getText().toString();
				String btime = tv_btime.getText().toString();
				String etime = tv_etime.getText().toString();
				String content = et_content.getText().toString();
				String curTime = getTime();
				
				if (TextUtils.isEmpty(uploadBuffer)) {
					Utils.toast("图片不能为空");
					return;
				}
				if(TextUtils.isEmpty(category)){
					Utils.toast("请添加分类");
					return;
				}
				if(TextUtils.isEmpty(title)){
					Utils.toast("请添加标题");
					return;
				}
				if(TextUtils.isEmpty(place)){
					Utils.toast("请添加地点");
					return;
				}
				if(TextUtils.isEmpty(btime)){
					Utils.toast("请添加活动开始时间");
					return;
				}
				if(TextUtils.isEmpty(etime)){
					Utils.toast("请添加活动结束时间");
					return;
				}
				if(TextUtils.isEmpty(content)){
					Utils.toast("请添加活动详情");
					return;
				}
				
				
				if(category.equals("团委活动")){
					categoryNum = "1";
				}else if (category.equals("院系活动")) {
					categoryNum = "2";
				}else if (category.equals("社团活动")) {
					categoryNum = "3";
				}else if (category.equals("科研活动")) {
					categoryNum = "4";
				}else if (category.equals("其他活动")) {
					categoryNum = "5";
				}
				dialog.show();

				param.put("Picture", uploadBuffer);
				param.put("category", categoryNum);
				param.put("title", title);
				param.put("place", place);
				param.put("btime", btime);
				param.put("etime", etime);
				param.put("content", content);
				param.put("nickName", new PreferenceMap(ctx).getUser().getName());
				param.put("curTime", curTime);
				param.put("userId", Utils.getID());
				System.out.println("userId---->" + new PreferenceMap(ctx).getUser().getName());
				new SimpleNetTask(ctx,false) {
					boolean flag;
					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						if(flag){
							dialog.dismiss();
							Utils.toast("添加活动成功,请等待审核");
							AddCampaignActivity.this.finish();
						}else {
							dialog.dismiss();
							Utils.toast("添加活动失败");
						}
						
					}
					
					@Override
					protected void doInBack() throws Exception {
						// TODO Auto-generated method stub
						String jsonstr = new WebService(C.SETCAMPAIGNPIC, param)
							.getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonstr);
					}
				}.execute();
				
			}
		});
		dbHelper = new DBHelper(this);
		dbHelper.openSqLiteDatabase();
		userChannelList = (ArrayList<CampaignCategoryModel>) dbHelper
				.queryAllCampaignCategory();
		list = new ArrayList<String>();
		for (int i = 0; i < userChannelList.size(); i++) {
			list.add(userChannelList.get(i).getTitle());
		}
		mSpinerPopWindow = new SpinerPopWindow(this);
		mSpinerPopWindow.refreshData(list, 0);
		mSpinerPopWindow.setItemListener(AddCampaignActivity.this);

	}

	private void showPhotoDialog() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		Window window = dlg.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.alertdialog_choose_photo);
		// 为确认按钮添加事件,执行退出应用操作
		TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content2);
		tv_paizhao.setText("取消");
		tv_paizhao.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("SdCardPath")
			public void onClick(View v) {

				dlg.cancel();

			}
		});
		TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content1);
		tv_xiangce.setText("相册");
		tv_xiangce.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
					intent.setType("image/*");
					startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
				} catch (ActivityNotFoundException e) {

					Toast.makeText(getApplicationContext(), "没有找到照片",
							Toast.LENGTH_SHORT).show();
				}
				dlg.cancel();
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent mIntent) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			Uri uri = mIntent.getData();
			String currentFilePath = getPath(uri);
			if (!AbStrUtil.isEmpty(currentFilePath)) {
				Intent intent1 = new Intent(this, CropImageActivity2.class);
				intent1.putExtra("PATH", currentFilePath);
				startActivityForResult(intent1, CAMERA_CROP_DATA);
			} else {
				Toast.makeText(getApplicationContext(), "未在存储卡中找到这个文件",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case CAMERA_WITH_DATA:
			String currentFilePath2 = mCurrentPhotoFile.getPath();
			Intent intent2 = new Intent(this, CropImageActivity2.class);
			intent2.putExtra("PATH", currentFilePath2);
			startActivityForResult(intent2, CAMERA_CROP_DATA);
			break;

		case CAMERA_CROP_DATA:
			final String path = mIntent.getStringExtra("PATH");
			iv_campaign.setImageBitmap(BitmapFactory.decodeFile(path));

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(path);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count = 0;
			try {
				while ((count = fis.read(buffer)) >= 0) {
					baos.write(buffer, 0, count);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			uploadBuffer = new String(Base64.encode(baos.toByteArray())); // 图片转Base64编码
			break;
		}
	}

	public String getPath(Uri uri) {
		if (AbStrUtil.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		CursorLoader cursorloder = new CursorLoader(ctx, uri, projection, null,
				null, null);
		Cursor cursor = cursorloder.loadInBackground();
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}

	@Override
	public void onItemClick(int pos) {
		// TODO Auto-generated method stub
		setHero(pos);
	}

	private void setHero(int pos) {
		if (pos >= 0 && pos <= list.size()) {
			String value = list.get(pos);
			tvcategory.setText(value);
		}
	}

	/**
	 * 设置输入框
	 */
	private void setListener() {
		et_title.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int number = s.length();
				selectionStart = et_title.getSelectionStart();
				selectionEnd = et_title.getSelectionEnd();
				if (temp.length() > 20) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					et_title.setText(s);
					et_title.setSelection(tempSelection);
				}
			}
		});

		et_place.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int number = s.length();
				selectionStart = et_place.getSelectionStart();
				selectionEnd = et_place.getSelectionEnd();
				if (temp.length() > 30) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					et_place.setText(s);
					et_place.setSelection(tempSelection);
				}
			}
		});

	}

	// 时间
	private void showTimeDialog() {
		DatePickerFragment datePicker = new DatePickerFragment();
		datePicker.show(getFragmentManager(), "datePicker");
	}

	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			Log.d("OnDateSet", "select year:" + year + ";month:" + month
					+ ";day:" + day);
			int month2 = month + 1;
			tv_btime.setText(year + "-" + month2 + "-" + day);
			tv_btime.setKeyListener(null);
		}

	}
	
	
	// 结束时间
	private void showTimeDialog2() {
		DatePickerFragment2 datePicker = new DatePickerFragment2();
		datePicker.show(getFragmentManager(), "datePicker");
	}

	public class DatePickerFragment2 extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			Log.d("OnDateSet", "select year:" + year + ";month:" + month
					+ ";day:" + day);
			int month2 = month + 1;
			tv_etime.setText(year + "-" + month2 + "-" + day);
			tv_etime.setKeyListener(null);
		}

	}

}
