package com.dtr.zxing.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.dtr.zxing.decode.DecodeThread;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.AddContactActivity;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.base.App;
import com.pocketuniversity.view.HeaderLayout;

public class ResultActivity extends BaseActivity {

	private ImageView mResultImage;
	private TextView mResultText;
	private Button btn_search_friend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_result);

		

		initView();
		initAction();
		initData();

	}

	
	
	
	private void initView() {

		mResultImage = (ImageView) findViewById(R.id.result_image);
		mResultText = (TextView) findViewById(R.id.result_text);
		btn_search_friend = (Button) findViewById(R.id.btn_search_friend);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
	}

	private void initAction() {

		headerLayout.showTitle("扫描结果");
		headerLayout.showLeftBackButton("返回", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ResultActivity.this.finish();
			}
		});

	}

	private void initData(){
		Bundle extras = getIntent().getExtras();
		if (null != extras) {
			int width = extras.getInt("width");
			int height = extras.getInt("height");

			LayoutParams lps = new LayoutParams(width, height);
			lps.topMargin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 30, getResources()
							.getDisplayMetrics());
			lps.leftMargin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
							.getDisplayMetrics());
			lps.rightMargin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
							.getDisplayMetrics());

			mResultImage.setLayoutParams(lps);

			final String result = extras.getString("result");
			mResultText.setText(result);
			btn_search_friend.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ResultActivity.this, AddContactActivity.class);
					intent.putExtra("QRFriendCode", result);
					startActivity(intent);
				}
			});
			Bitmap barcode = null;
			byte[] compressedBitmap = extras
					.getByteArray(DecodeThread.BARCODE_BITMAP);
			if (compressedBitmap != null) {
				barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0,
						compressedBitmap.length, null);
				// Mutable copy:
				barcode = barcode.copy(Bitmap.Config.RGB_565, true);
			}

			mResultImage.setImageBitmap(barcode);
		}
		
	}
	
}
