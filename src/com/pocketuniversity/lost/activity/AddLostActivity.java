package com.pocketuniversity.lost.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbStrUtil;
import com.byl.imageselector.a.MultiImageSelectorActivity;
import com.byl.imageselector.adapter.PubSelectedImgsAdapter;
import com.byl.imageselector.adapter.PubSelectedImgsAdapter.OnItemClickClass;
import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.activity.CropImageActivity;
import com.pocketuniversity.activity.AddCampaignActivity.DatePickerFragment;
import com.pocketuniversity.activity.CropImageActivity2;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.tiaozao.activity.AddTiaoZaoActivity;
import com.pocketuniversity.utils.PictureUtil;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class AddLostActivity extends BaseActivity implements OnClickListener {

	private ImageView img_lost;
	private EditText et_title;
	private RadioGroup radioGroup;
	private RadioButton get;
	private RadioButton lost;
	private EditText et_place;
	private ImageButton img_time;
	private TextView tv_time;
	private EditText phone;
	private EditText detail;

	private RelativeLayout rl_btime;
	String uploadBuffer;
	
	private static final int REQUEST_IMAGE = 2;

	private ArrayList<String> mSelectPath = new ArrayList<String>();

	String state;

	MyProgressDialog dialog;
	GridView gridView;
	PubSelectedImgsAdapter pubSelectedImgsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_lost_add);
		dialog = new MyProgressDialog(ctx);
		initView();
		initAction();
		setListener();

	}

	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("发布");
		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddLostActivity.this.finish();
			}
		});
		img_lost = (ImageView) findViewById(R.id.iv_goods);
		gridView=(GridView) findViewById(R.id.gridView);
		et_title = (EditText) findViewById(R.id.title);
		radioGroup = (RadioGroup) findViewById(R.id.chooseface);
		get = (RadioButton) findViewById(R.id.get);
		lost = (RadioButton) findViewById(R.id.lost);
		et_place = (EditText) findViewById(R.id.place);
		img_time = (ImageButton) findViewById(R.id.bt_dropdownBtime);
		tv_time = (TextView) findViewById(R.id.btime);
		phone = (EditText) findViewById(R.id.phone);
		detail = (EditText) findViewById(R.id.content);
		rl_btime = (RelativeLayout) findViewById(R.id.rl_btime);
		img_lost.setOnClickListener(this);
		get.setOnClickListener(this);
		lost.setOnClickListener(this);
		//img_time.setOnClickListener(this);
		rl_btime.setOnClickListener(this);
	}

	private String getTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	private void initAction() {
		headerLayout.showRightTextButton("发布", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String title = et_title.getText().toString();
				final String place = et_place.getText().toString();
				final String time = tv_time.getText().toString();
				final String tphone = phone.getText().toString();
				final String tdetail = detail.getText().toString();
				final String publishTime = getTime();

				if (TextUtils.isEmpty(title)) {
					Utils.toast("标题不能为空");
					return;
				}
				if (TextUtils.isEmpty(place)) {
					Utils.toast("地点不能为空");
					return;
				}
				if (TextUtils.isEmpty(state)) {
					Utils.toast("物品状态不能为空");
					return;
				}
				if (TextUtils.isEmpty(time)) {
					Utils.toast("时间不能为空");
					return;
				}
				if (TextUtils.isEmpty(tphone)) {
					Utils.toast("号码不能为空");
					return;
				}
				if (TextUtils.isEmpty(tdetail)) {
					Utils.toast("物品详情不能为空");
					return;
				}

				if (TextUtils.isEmpty(uploadBuffer)) {
					uploadBuffer = "";
				}

				// Log.i("picture", uploadBuffer);
				Log.i("AddLostActivity", Utils.getID() + "," + title + ","
						+ "," + state + "," + place + "," + publishTime + ","
						+ time + "," + tphone + "," + tdetail);
				dialog.show();
				new SimpleNetTask(ctx, false) {
					boolean flag;

					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						if (flag) {
							dialog.dismiss();
							Utils.toast("发布物品成功");
							AddLostActivity.this.finish();
						} else {
							dialog.dismiss();
							Utils.toast("发布物品失败");
						}
					}

					@Override
					protected void doInBack() throws Exception {

						List<String> picList = new ArrayList<String>();
						for (int i = 0; i < mSelectPath.size(); i++) {
							String path = mSelectPath.get(i);
							//PictureUtil.getSmallBitmap(path);
							System.out.println("图片："+path);
							String uploadBuffer = PictureUtil.bitmapToString(path);
							picList.add(uploadBuffer);
							PictureUtil.deleteTempFile(mSelectPath.get(i));
						}
						JSONArray json = new JSONArray();
						for(int i = 0;i<picList.size();i++){
							JSONObject jo = new JSONObject();
							String pic = picList.get(i);
							try {
								jo.put("id", pic);
								json.put(jo);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						String friendsPicList = "{\"friendsPicList\":" + json + "}";
						
						param.clear();
						param.put("userID", Utils.getID());
						param.put("title", title);
						param.put("picture", friendsPicList);
						param.put("state", state);
						param.put("place", place);
						param.put("publishTime", publishTime);
						param.put("time", time);
						param.put("phone", tphone);
						param.put("detail", tdetail);
						
						String jsonStr = new WebService(C.ADDLOST, param)
								.getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonStr);
					}
				}.execute();
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_goods:
			Intent intent = new Intent(AddLostActivity.this, MultiImageSelectorActivity.class);
            // 是否显示拍摄图片
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
            // 最大可选择图片数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 3);
            // 选择模式
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
            // 默认选择
            if(mSelectPath != null && mSelectPath.size()>0){
                intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
            }
            startActivityForResult(intent, REQUEST_IMAGE);
			break;
		case R.id.get:
			state = "招领";
			break;
		case R.id.lost:
			state = "失物";
			break;
		case R.id.rl_btime:
			showTimeDialog();
			break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE) {
			if (resultCode == RESULT_OK) {
				mSelectPath = data
						.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				pubSelectedImgsAdapter = new PubSelectedImgsAdapter(
						getApplicationContext(), mSelectPath,
						new OnItemClickClass() {
							@Override
							public void OnItemClick(View v, String filepath) {
								mSelectPath.remove(filepath);
								pubSelectedImgsAdapter.notifyDataSetChanged();
							}
						});
			}
			gridView.setAdapter(pubSelectedImgsAdapter);
		}

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
			tv_time.setText(year + "-" + month2 + "-" + day);
			tv_time.setKeyListener(null);
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

		phone.addTextChangedListener(new TextWatcher() {
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
				selectionStart = phone.getSelectionStart();
				selectionEnd = phone.getSelectionEnd();
				if (temp.length() > 11) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					phone.setText(s);
					phone.setSelection(tempSelection);
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
				selectionStart = phone.getSelectionStart();
				selectionEnd = phone.getSelectionEnd();
				if (temp.length() > 30) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					et_place.setText(s);
					et_place.setSelection(tempSelection);
				}
			}
		});

	}

}
