package com.pocketuniversity.more.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.json.JSONObject;
import org.kobjects.base64.Base64;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.UserUtils;
import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.activity.CropImageActivity;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class ChangeInformationActivity extends BaseActivity implements
		OnClickListener {

	private static final int CAMERA_WITH_DATA = 3023;
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	private static final int CAMERA_CROP_DATA = 3022;

	private ImageView iv_avatar;
	private Button btn_save;
	private EditText et_name;
	private CheckBox male, female;
	private String imageName;
	private View layout_name, layout_sex;
	private String name_str;
	private String sex_str;
	
	private PUUser curuser ;
	
	private String mFileName;
	private File mCurrentPhotoFile;
	private File PHOTO_DIR = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_change_information);
		curuser = new PreferenceMap(this).getUser();
		initView();
		initData();
		initAction();
	}
	
	
	
	private void initData() {
		headerLayout.showTitle("个人信息修改");

		et_name.setText(curuser.getName());
		Utils.setEditTextLastPosition(et_name);
		if(curuser.getSex().equals("男")){
			male.setChecked(true);
			female.setChecked(false);
		}else{
			female.setChecked(true);
			male.setChecked(false);
		}
		String photo_dir = AbFileUtil.getFullImageDownPathDir();
		if (AbStrUtil.isEmpty(photo_dir)) {
			Toast.makeText(getApplicationContext(), "存储卡不存在", Toast.LENGTH_SHORT).show();
		} else {
			PHOTO_DIR = new File(photo_dir);
		}
	}

	
	
	public void initAction(){
		
		headerLayout.showLeftBackButton("返回", new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeInformationActivity.this.finish();
			}
		});

		
		male.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					female.setChecked(false);
				} else {
					female.setChecked(true);
				}
			}
		});
		
		female.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					male.setChecked(false);
				}else{
					male.setChecked(true);
				}
			}
		});
		
		
	}
	
	
	
	

	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		et_name = (EditText) findViewById(R.id.et_name);
		male = (CheckBox) findViewById(R.id.cb_male);
		female = (CheckBox) findViewById(R.id.cb_female);
		layout_name = findViewById(R.id.layout_name);
		layout_sex = findViewById(R.id.layout_sex);
		btn_save = (Button) findViewById(R.id.btn_savechange);
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);

		iv_avatar.setOnClickListener(this);
		layout_name.setOnClickListener(this);
		layout_sex.setOnClickListener(this);
		btn_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		 case R.id.iv_avatar:
	
			 showPhotoDialog();
		 break;

		 case R.id.btn_savechange :
			 name_str = et_name.getText().toString();
			 sex_str = "女";
			 if (male.isChecked()) {
				sex_str = "男";
			}
			 if( TextUtils.isEmpty(name_str)){
				 Utils.toast("用户名不能为空");
				 return;
			 }
			 
				param.clear();
				param.put("userID", curuser.getID());
				param.put("nickname", name_str);
				param.put("sex", sex_str);
				new SimpleNetTask(ctx, true) {
					boolean flag;
					@Override
					protected void onSucceed() {
						if (flag) {
							curuser.setName(name_str);
							curuser.setSex(((male.isChecked() ? "男" : "女")));
							new PreferenceMap(App.ctx).setUser(curuser);
							Utils.toast("修改信息成功");
							ChangeInformationActivity.this.finish();
						}
					}

					@Override
					protected void doInBack() throws Exception {
						String jsonstr = new WebService(C.MODIFYINFOR, param)
								.getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonstr);
					}
				}.execute();
			 
			 break ;
		}

	}

	private void showPhotoDialog() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		Window window = dlg.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.alertdialog_choose_photo);
		// 为确认按钮添加事件,执行退出应用操作
		TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
		tv_paizhao.setText("拍照");
		tv_paizhao.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("SdCardPath")
			public void onClick(View v) {

				doPickPhotoAction();
				dlg.cancel();
				
			}
		});
		TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
		tv_xiangce.setText("相册");
		tv_xiangce.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
					intent.setType("image/*");
					startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
				} catch (ActivityNotFoundException e) {
					
					Toast.makeText(getApplicationContext(), "没有找到照片", Toast.LENGTH_SHORT).show();
				}
				dlg.cancel();
			}
		});

	}

	
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			Toast.makeText(getApplicationContext(), "没有可用的存储卡", Toast.LENGTH_SHORT).show();
		}
	}
	protected void doTakePhoto() {
		try {
			mFileName = System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "未找到系统相机程序", Toast.LENGTH_SHORT).show();
				
		}
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
				Intent intent1 = new Intent(this, CropImageActivity.class);
				intent1.putExtra("PATH", currentFilePath);
				startActivityForResult(intent1, CAMERA_CROP_DATA);
			} else {
				Toast.makeText(getApplicationContext(), "未在存储卡中找到这个文件", Toast.LENGTH_SHORT).show();
			}
			break;
		case CAMERA_WITH_DATA:
			String currentFilePath2 = mCurrentPhotoFile.getPath();
			Intent intent2 = new Intent(this, CropImageActivity.class);
			intent2.putExtra("PATH", currentFilePath2);
			startActivityForResult(intent2, CAMERA_CROP_DATA);
			break;

		case CAMERA_CROP_DATA:
			final String path = mIntent.getStringExtra("PATH");
			iv_avatar.setImageBitmap(BitmapFactory.decodeFile(path));
			
			
			new SimpleNetTask(ctx, true) {
				boolean flag = true;
				MyProgressDialog dialog = new MyProgressDialog(ctx);

				@Override
				protected void onSucceed() {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();

					}
					if (flag) {
						Utils.toast("更换头像成功");
					} else {
						Utils.toast("更换头像失败");
					}
				}

				@Override
				protected void doInBack() throws Exception {
					FileInputStream fis = new FileInputStream(path);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int count = 0;
					while ((count = fis.read(buffer)) >= 0) {
						baos.write(buffer, 0, count);
					}
					String uploadBuffer = new String(Base64.encode(baos
							.toByteArray()));      //图片转Base64编码
					Log.i("ChangeInformation", uploadBuffer);
					param.clear();
					param.put("userID", curuser.getID());
					param.put("Picture", uploadBuffer);
					String jsonstr = new WebService(C.MODIFYAVATAR, param)
							.getReturnInfo();
					try {
						JSONObject json = new JSONObject(jsonstr);
						if (json.get("ret").equals("success")) {
							String imageurl = json.getString("pictureUrl");
							curuser.setImage(imageurl);
							new PreferenceMap(ctx).setUser(curuser);
							User user=UserUtils.getUserInfo(Utils.getID());
							user.setAvatar(imageurl);
							App.getInstance().getContactList().put(Utils.getID(), user);
							flag = true;
							fis.close();
						} else {
							flag = false;
						}
					} catch (Exception e) {
						flag = false;
					}
				}

			}.execute();   
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
		System.out.println("头像路径："+path);
		return path;
	}

	
}
