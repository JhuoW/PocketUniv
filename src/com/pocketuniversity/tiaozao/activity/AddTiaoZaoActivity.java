package com.pocketuniversity.tiaozao.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.crypto.spec.IvParameterSpec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.bitmap.AbFileCache;
import com.ab.util.AbStrUtil;
import com.ab.view.cropimage.CropImage;
import com.ab.view.cropimage.CropImageView;
import com.byl.imageselector.a.MultiImageSelectorActivity;
import com.byl.imageselector.adapter.PubSelectedImgsAdapter;
import com.byl.imageselector.adapter.PubSelectedImgsAdapter.OnItemClickClass;
import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.ab.utils.AbImageUtil;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.activity.CropImageActivity2;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.friendcircle.utils.Bimp;
import com.pocketuniversity.friendcircle.utils.FileUtils;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.PictureUtil;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class AddTiaoZaoActivity extends BaseActivity implements
		OnClickListener {


	private ImageView iv_goods;
	private EditText et_title;
	private EditText et_price;
	private EditText phone;
	private RadioGroup radioGroup;
	private RadioButton radioButton;
	private EditText et_detail;

	private String mFileName;
	private File mCurrentPhotoFile;
	private File PHOTO_DIR = null;
	private CropImage mCrop;

	MyProgressDialog dialog;
	
	private static final int REQUEST_IMAGE = 2;

    private ArrayList<String> mSelectPath = new ArrayList<String>();
    
    GridView gridView;
	PubSelectedImgsAdapter pubSelectedImgsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_addtiaozao);
		dialog = new MyProgressDialog(ctx);
		initView();
		initAction();
		setListener();
	}

	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("发布商品");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddTiaoZaoActivity.this.finish();
			}
		});

		et_title = (EditText) findViewById(R.id.title);
		et_price = (EditText) findViewById(R.id.price);
		phone = (EditText) findViewById(R.id.phone);
		phone.setInputType(InputType.TYPE_CLASS_PHONE);
		iv_goods = (ImageView) findViewById(R.id.iv_goods);
		radioGroup = (RadioGroup) findViewById(R.id.chooseface);
		radioButton = (RadioButton) findViewById(R.id.Face);
		et_detail = (EditText) findViewById(R.id.content);
		gridView=(GridView) findViewById(R.id.gridView);
		iv_goods.setOnClickListener(this);
		radioButton.setOnClickListener(this);
	}

	
	private String getTime(){
		SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
		String    date    =    sDateFormat.format(new    java.util.Date());
		System.out.println("当前系统时间："+date);
		return date;
	}
	
	
	private void initAction(){
		headerLayout.showRightTextButton("提交", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String title = et_title.getText().toString();
				final String price = et_price.getText().toString();
				final String detail = et_detail.getText().toString();
				final String phoneNum = phone.getText().toString();  
				final String time = getTime();
				final String userId = Utils.getID();
				
				if(TextUtils.isEmpty(price)){
					Utils.toast("请添加价格");
					return;
				}
				if(TextUtils.isEmpty(title)){
					Utils.toast("请添加标题");
					return;
				}
				if(TextUtils.isEmpty(phoneNum)){
					Utils.toast("请添加号码");
					return;
				}
				if(TextUtils.isEmpty(detail)){
					Utils.toast("请添加商品详情");
					return;
				}
				if(mSelectPath.size()==0){
					Utils.toast("图片不能为空");
					dialog.dismiss();
					return;
				}
				
				//System.out.println("图片："+friendsPicList+"......");
//				if (json.length()==0) {
//					Utils.toast("图片不能为空");
//					dialog.dismiss();
//					return;
//				}
				dialog.show();
				new SimpleNetTask(ctx,false) {
					boolean flag;
					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						if(flag){
							dialog.dismiss();
							Utils.toast("添加商品成功");
//							for(int i = 0; i < mSelectPath.size(); i++){
//								PictureUtil.deleteTempFile(mSelectPath.get(i));
//							}
							AddTiaoZaoActivity.this.finish();
						}else {
							dialog.dismiss();
							Utils.toast("添加商品失败");
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
						param.put("picture", friendsPicList);
						param.put("userId", userId);
						param.put("commodityName", title);
						param.put("price", price);
						param.put("phone", phoneNum);
						param.put("time", time);
						param.put("detail", detail);
						String jsonstr = new WebService(C.ADDGOODS, param)
						.getReturnInfo();
					flag = GetObjectFromService.getSimplyResult(jsonstr);
					}
				}.execute();
				
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Face:
			et_price.setText("面议");
			break;

		case R.id.iv_goods:
			//showPhotoDialog();
			
			Intent intent = new Intent(AddTiaoZaoActivity.this, MultiImageSelectorActivity.class);
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
		}
	}
	

    @Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
	        if(requestCode == REQUEST_IMAGE){
	            if(resultCode == RESULT_OK){
	            	mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
	                pubSelectedImgsAdapter=new PubSelectedImgsAdapter(getApplicationContext(), mSelectPath, new OnItemClickClass() {
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
		
		et_price.addTextChangedListener(new TextWatcher() {
			
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
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
				if(et_price.getText().toString().equals("面议")){
					radioButton.setChecked(true);
				}else {
					radioButton.setChecked(false);
				}
			}
		});

	}
	
}
