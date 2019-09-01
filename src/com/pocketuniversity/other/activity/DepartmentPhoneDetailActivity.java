package com.pocketuniversity.other.activity;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.DpPhoneDetailAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.DpDetail;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class DepartmentPhoneDetailActivity extends BaseActivity {

	private ListView lv;
	private DpPhoneDetailAdapter adapter;

	MyProgressDialog dialog;

	String id;
	String name;
	public List<DpDetail> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_depart_phone);
		Intent intent = getIntent();
		id = intent.getStringExtra("ID");
		name = intent.getStringExtra("Name");
		initView();
		getData();
		initAction();
	}

	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle(name);
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DepartmentPhoneDetailActivity.this.finish();
			}
		});
		lv = (ListView) findViewById(R.id.phone_listview);
		dialog = new MyProgressDialog(ctx);
		dialog.show();

	}

	private void getData() {
		new SimpleNetTask(ctx, false) {

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}

				adapter = new DpPhoneDetailAdapter(ctx, list);
				lv.setAdapter(adapter);
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("ID", id);
				String jsonStr = new WebService(C.GETDEPARTPHONE, param)
						.getReturnInfo();
				list = GetObjectFromService.getDpDetail(jsonStr);
			}
		}.execute();

	}

	private void initAction() {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				final String phone = list.get(position).getPhone();
				String name = list.get(position).getName();
				new SweetAlertDialog(ctx)
						.setTitleText(name + ":" + phone)
						.setConfirmText("²¦´ò")
						.showCancelButton(true)
						.setConfirmClickListener(new OnSweetClickListener() {

							@Override
							public void onClick(
									SweetAlertDialog sweetAlertDialog) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(Intent.ACTION_CALL,
										Uri.parse("tel:" + phone));
								startActivity(intent);
							}
						}).setCancelText("È¡Ïû")
						.setCancelClickListener(new OnSweetClickListener() {

							@Override
							public void onClick(
									SweetAlertDialog sweetAlertDialog) {
								// TODO Auto-generated method stub
								sweetAlertDialog.dismiss();
							}
						}).show();

			}
		});

	}

}
