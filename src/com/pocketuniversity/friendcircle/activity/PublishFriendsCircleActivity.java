package com.pocketuniversity.friendcircle.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.AddCommentActivity;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.GridAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.friendcircle.utils.Bimp;
import com.pocketuniversity.friendcircle.utils.FileUtils;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class PublishFriendsCircleActivity extends BaseActivity {

	private EditText mContent;
	private TextView mCount;
	private String method;
	private GridView noScrollgridview;
	private GridAdapter adapter;

	MyProgressDialog dialog ; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_friends_circle);
		Bimp.bmp.clear();
		method = C.PUBLISHFRIENDSCIRCLE;
		findView();
		setListener();
		initAction();
	}

	private void findView() {
		dialog = new MyProgressDialog(ctx);
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		// adapter.update();
		noScrollgridview.setAdapter(adapter);
		
		mContent = (EditText) findViewById(R.id.blogaddcomment_content);
		mCount = (TextView) findViewById(R.id.blogaddcomment_count);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
	}

	private void setListener() {
		mContent.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				int number = s.length();
				mCount.setText(String.valueOf(number));
				selectionStart = mContent.getSelectionStart();
				selectionEnd = mCount.getSelectionEnd();
				if (temp.length() > 140) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					mContent.setText(s);
					mContent.setSelection(tempSelection);
				}
			}
		});
	}

	private void initAction() {
		headerLayout.showTitle("发布动态");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PublishFriendsCircleActivity.this.finish();
			}
		});
		headerLayout.showRightImageButton(
				R.drawable.action_button_out_pressed_light,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String commentcontent = mContent.getEditableText()
								.toString();
						if (commentcontent.length() == 0) {
							new SweetAlertDialog(
									PublishFriendsCircleActivity.this,
									SweetAlertDialog.WARNING_TYPE)
									.setTitleText("状态不能为空")
									.setConfirmText("确定")
									.setCancelText("取消")
									.showCancelButton(true)
									.setCancelClickListener(
											new OnSweetClickListener() {

												@Override
												public void onClick(
														SweetAlertDialog sweetAlertDialog) {
													sweetAlertDialog.dismiss();
												}
											})
									.setConfirmClickListener(
											new OnSweetClickListener() {

												@Override
												public void onClick(
														SweetAlertDialog sweetAlertDialog) {
													sweetAlertDialog.dismiss();
												}
											}).show();
						} else {
							publishFriendsCircle(commentcontent);
						}
					}

				});

		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(PublishFriendsCircleActivity.this,
							noScrollgridview);
				} else {
					Intent intent = new Intent(
							PublishFriendsCircleActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
	}

	private void publishFriendsCircle(final String Commentcontent) {
		dialog.show();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < Bimp.drr.size(); i++) {
			String Str = Bimp.drr.get(i).substring(
					Bimp.drr.get(i).lastIndexOf("/") + 1,
					Bimp.drr.get(i).lastIndexOf("."));
			list.add(FileUtils.SDPATH + Str + ".JPEG");
		}

		List<String> picList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			String path = list.get(i);
			try {
				FileInputStream fis = new FileInputStream(path);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = fis.read(buffer)) >= 0) {
					baos.write(buffer, 0, count);
				}
				String uploadBuffer = new String(Base64.encode(baos
						.toByteArray()));
				picList.add(uploadBuffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		final String friendsPicList = "{\"friendsPicList\":" + json + "}";
		new SimpleNetTask(ctx) {
			boolean flag;
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(flag){
					//Utils.toast("发布动态成功");
					Utils.showtoast(ctx, R.drawable.tips_smile, "发布动态成功");
					finish();
				}
				else {
				//	Utils.toast("发布动态失败，请重试");
					Utils.showtoast(ctx, R.drawable.tips_smile, "发布动态失败，请重试");

				}
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("userID", Utils.getID());
				param.put("content", Commentcontent);
				param.put("picture", friendsPicList);
				String jsonStr = new WebService(method, param).getReturnInfo();
				flag = GetObjectFromService.getSimplyResult(jsonStr);
			}
		}.execute();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(0, R.anim.roll_down);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(
							PublishFriendsCircleActivity.this,
							TestPicActivity.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	 public void photo() {
	        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	        openCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

	        StringBuffer sDir = new StringBuffer();
	        if (hasSDcard()) {
	            sDir.append(Environment.getExternalStorageDirectory() + "/MyPicture/");
	        } else {
	            String dataPath = Environment.getRootDirectory().getPath();
	            sDir.append(dataPath + "/MyPicture/");
	        }

	        File fileDir = new File(sDir.toString());
	        if (!fileDir.exists()) {
	            fileDir.mkdirs();
	        }
	        File file = new File(fileDir, String.valueOf(System.currentTimeMillis()) + ".jpg");

	        path = file.getPath();
	        Uri imageUri = Uri.fromFile(file);
	        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	        startActivityForResult(openCameraIntent, TAKE_PICTURE);
	    }
	 
	 
	 public static boolean hasSDcard() {
	        String status = Environment.getExternalStorageState();
	        if (status.equals(Environment.MEDIA_MOUNTED)) {
	            return true;
	        } else {
	            return false;
	        }
	    }
	 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bimp.drr.add(path);
			}
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.notifyDataSetChanged();
		adapter.update();

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// adapter.update();
	}

}
