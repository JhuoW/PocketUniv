package com.pocketuniversity.more.activity;

import java.util.ArrayList;
import java.util.List;

import u.aly.db;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.AddCampaignActivity;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.activity.CampaignActivity;
import com.pocketuniversity.activity.MyJoinCampaignActivity;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.ClassInfo;
import com.pocketuniversity.db.DBHelper;
import com.pocketuniversity.db.Schedule;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.poptitle.ActionItem;
import com.pocketuniversity.poptitle.TitlePopup;
import com.pocketuniversity.poptitle.TitlePopup.OnItemOnClickListener;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.ScheduleView;
import com.pocketuniversity.view.ScheduleView.OnItemClassClickListener;

public class ScheduleActivity extends BaseActivity {

	private ScheduleView scheduleView;

	public List<ClassInfo> classList;

	private ClassInfo classInfo;
	public DBHelper dbHelper;
	private TitlePopup titlePopup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_schedule);

		initView();
		initAction();
		// getClassData();
		// getAllClassData();
		// scheduleView.setClassList(classList);
		getClassDataFromServer();

	}

	void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		scheduleView = (ScheduleView) findViewById(R.id.scheduleView);
		dbHelper = new DBHelper(ctx);
		scheduleView
				.setOnItemClassClickListener(new OnItemClassClickListener() {

					@Override
					public void onClick(ClassInfo classInfo) {
						// TODO Auto-generated method stub

						Toast.makeText(ScheduleActivity.this,
								"您点击的课程是：" + classInfo.getClassname(),
								Toast.LENGTH_SHORT).show();

					}
				});

		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titlePopup.addAction(new ActionItem(this, "添加课程",
				R.drawable.ic_func_edit));
		titlePopup.addAction(new ActionItem(this, "清空课表",
				R.drawable.ic_plugin_lost));
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {

			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if (position == 0) {
					Intent intent = new Intent(getApplicationContext(),
							SettingScheduleActivity.class);
					startActivity(intent);
				} else {
					new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE).setTitleText("是否确定清空课程表？")
					.setCancelText("取消").setConfirmText("确定").setConfirmClickListener(new OnSweetClickListener() {
						
						@Override
						public void onClick(final SweetAlertDialog sweetAlertDialog) {
							// TODO Auto-generated method stub
							new SimpleNetTask(ctx) {
								boolean flag;
								@Override
								protected void onSucceed() {
									// TODO Auto-generated method stub
									if(flag){
										sweetAlertDialog.dismiss();
										scheduleView.setClassList(null);
										Utils.toast("清空成功");
									}else{
										sweetAlertDialog.dismiss();
										Utils.toast("清空失败");
									}
								}
								
								@Override
								protected void doInBack() throws Exception {
									// TODO Auto-generated method stub
									param.clear();
									param.put("userID", Utils.getID());
									String jsonStr = new WebService(C.CLEARSCHEDULE, param).getReturnInfo();
									flag = GetObjectFromService.getSimplyResult(jsonStr);
								}
							}.execute();
						}
					}).setCancelClickListener(new OnSweetClickListener() {
						
						@Override
						public void onClick(SweetAlertDialog sweetAlertDialog) {
							// TODO Auto-generated method stub
							sweetAlertDialog.dismiss();
						}
					}).show();
				}
			}
		});

	}

	void initAction() {
		headerLayout.showTitle("课程表");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScheduleActivity.this.finish();
			}
		});
		headerLayout.showRightImageButton(R.drawable.ic_fifter,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Intent intent = new Intent(getApplicationContext(),
						// SettingScheduleActivity.class);
						// startActivity(intent);
						titlePopup.show(v);

					}
				});
	}

	public void getClassData() {
		dbHelper = new DBHelper(this);
		classInfo = new ClassInfo();
		classList = new ArrayList<ClassInfo>();
		Cursor cursor = null;
		cursor = dbHelper.queryLastData();
		classInfo.setClassname(cursor.getString(cursor
				.getColumnIndex(Schedule.CLASSNAME)));
		classInfo.setClassNumLen(cursor.getString(cursor
				.getColumnIndex(Schedule.CLASSNUMLEN)));
		classInfo.setClassRoom(cursor.getString(cursor
				.getColumnIndex(Schedule.CLASSROOM)));
		classInfo.setFromClassNum(cursor.getString(cursor
				.getColumnIndex(Schedule.FROMCLASSNAME)));
		classInfo.setWeekday(cursor.getString(cursor
				.getColumnIndex(Schedule.WEEKDAY)));

		classList.add(classInfo);

	}

	public void getAllClassData() {
		dbHelper = new DBHelper(this);
		classList = dbHelper.getAllData();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// getAllClassData();
		// scheduleView.setClassList(classList);

		getClassDataFromServer();
	}

	private void getClassDataFromServer() {
		new SimpleNetTask(ctx) {

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				scheduleView.setClassList(classList);
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("userID", Utils.getID());
				String jsonStr = new WebService(C.GETSCHEDULE, param)
						.getReturnInfo();
				classList = GetObjectFromService.getClassInfo(jsonStr);
				for (int i = 0; i < classList.size(); i++) {
					dbHelper.deleteAllDataFromTable(Schedule.TABLE_NAME);
					dbHelper.insertSchedule(classList.get(i));
				}
			}
		}.execute();
	}

}
