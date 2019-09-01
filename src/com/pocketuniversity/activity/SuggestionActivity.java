package com.pocketuniversity.activity;

import com.example.pocketuniversity.R;
import com.pocketuniversity.view.HeaderLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SuggestionActivity extends BaseActivity implements OnClickListener{
	private ImageView net,account,message,product,more;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestion);
		initView();
		initAction();
	}
	
	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		net=(ImageView) findViewById(R.id.iv_net);
		account=(ImageView) findViewById(R.id.iv_account);
		message=(ImageView) findViewById(R.id.iv_message);
		product=(ImageView) findViewById(R.id.iv_product);
		more=(ImageView) findViewById(R.id.iv_more);
		net.setOnClickListener(this);
		account.setOnClickListener(this);
		message.setOnClickListener(this);
		product.setOnClickListener(this);
		more.setOnClickListener(this);
	}

	private void initAction() {
		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SuggestionActivity.this.finish();
			}
		});
		
		headerLayout.showTitle("�������");
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent it=new Intent();
		it.setClass(SuggestionActivity.this, SubmitSuggestionActivity.class);
		switch(v.getId()){
		case R.id.iv_net:
			it.putExtra("suggestionType", "��������");
			break;
		case R.id.iv_account:
			it.putExtra("suggestionType", "�ʺŵ�¼");
			break;
		case R.id.iv_message:
			it.putExtra("suggestionType", "��ʱͨ��");
			break;
		case R.id.iv_product:
			it.putExtra("suggestionType", "��Ʒ����");
			break;
		case R.id.iv_more:
			it.putExtra("suggestionType", "��������");
			break;
		}
		SuggestionActivity.this.startActivity(it);
		SuggestionActivity.this.finish();
	}
}
