package com.pocketuniversity.tiaozao.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.TiaoZaoAdapter;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Tiaozao;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.NetAsyncTask;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class MyUploadTiaoZaoActivity extends BaseActivity{

	private GridView gridView;
	private TiaoZaoAdapter adapter;
	private MyProgressDialog dialog;
	private List<Tiaozao> tiaozaoList = new ArrayList<Tiaozao>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_tiaozao);
		initView();
		initAction();
		getData();
	}
	
	private void initView(){
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		adapter = new TiaoZaoAdapter(tiaozaoList, getLayoutInflater());
		dialog = new MyProgressDialog(this);
		dialog.show();
		headerLayout.showTitle("我的商品");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyUploadTiaoZaoActivity.this.finish();
			}
		});
		gridView = (GridView) findViewById(R.id.grid_view);
	}
	
	private void initAction(){
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyUploadTiaoZaoActivity.this,
						TiaoZaoDetailActivity.class);
				intent.putExtra("post", tiaozaoList.get(position));
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				new SweetAlertDialog(MyUploadTiaoZaoActivity.this, SweetAlertDialog.WARNING_TYPE)
				.setTitleText("是否删除商品？").setConfirmClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(final SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
					
						new NetAsyncTask(ctx , false) {
							boolean result;

							@Override
							protected void onPost(Exception e) {
								// TODO Auto-generated method stub
								if(result){
									sweetAlertDialog.dismiss();
									tiaozaoList.remove(position);
									adapter.notifyDataSetChanged();
								}else {
									sweetAlertDialog.dismiss();
									Toast.makeText(getApplicationContext(), "删除商品失败", Toast.LENGTH_SHORT).show();
								}
							}
							
							@Override
							protected void doInBack() throws Exception {
								// TODO Auto-generated method stub
								param.clear();
								param.put("userID", Utils.getID());
								param.put("commodityID",tiaozaoList.get(position).getGoodsId());
								String jsonStr = new WebService(C.DELETEMYUPLOADCOMMODITY, param).getReturnInfo();
								result = GetObjectFromService.getSimplyResult(jsonStr);
							}
						}.execute();
					}
				}).showCancelButton(true).setCancelClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						sweetAlertDialog.dismiss();
					}
				}).show();
				
				
				return true;
			}
		});
	}
	
	private void getData(){
		new SimpleNetTask(ctx) {
			List<Tiaozao> temp=new ArrayList<Tiaozao>();
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if(temp==null){
					Utils.toast("net wrong");
					return;
				}
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				tiaozaoList.addAll(temp);
				gridView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				String jsonStr = new APIHelper().getMyUploadGoodsByUserId();
				temp = GetObjectFromService.getMyUplaodTiaozao(jsonStr);
			}
		}.execute();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
