package com.pocketuniversity.more.activity;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pocketuniversity.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.base.App;
import com.pocketuniversity.view.HeaderLayout;

public class MyQRActivity extends BaseActivity {

	private ImageView img_qr;
	private String content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_my_qr);
		initView();
		initAction();
	}

	private void initView() {
		content = App.getInstance().getUserName();
		
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("�ҵĶ�ά��");
		headerLayout.showLeftBackButton("����", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyQRActivity.this.finish();
			}
		});

		img_qr = (ImageView) findViewById(R.id.img_qr);
		
	}

	private void initAction(){
		
		Bitmap bm = null;
		try {
			bm = qr_code(content, BarcodeFormat.QR_CODE);
			img_qr.setImageBitmap(bm);

		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "���ɶ�ά��ʧ��", Toast.LENGTH_SHORT).show();
		}

	}
	
	
	
	public Bitmap qr_code(String string, BarcodeFormat format)
			throws WriterException {
		MultiFormatWriter writer = new MultiFormatWriter();
		Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
		hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix matrix = writer.encode(string, format, 400, 400, hst);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}

			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// ͨ��������������bitmap,����ο�api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	
}
