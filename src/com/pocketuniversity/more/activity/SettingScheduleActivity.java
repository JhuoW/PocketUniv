package com.pocketuniversity.more.activity;

import numberpicker.NumberPicker;
import numberpicker.NumberPicker.OnValueChangeListener;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.ClassInfo;
import com.pocketuniversity.db.DBHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;

public class SettingScheduleActivity extends BaseActivity {

	private EditText et_className;
	private TextView tv_fromClassNum;
	private TextView tv_classNumLen;
	private EditText et_classRoom;
	private TextView tv_weekDay;
	private Button btn_sendSchedule;
	public ClassInfo classInfo;

	private LinearLayout ll_classNumLen;
	private LinearLayout ll_FromClassNum;
	private LinearLayout ll_weekday;
	public DBHelper dbHelper;
	NumberPicker np;
	int num;
	ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_setting_schedule);
		findView();

		initAction();
	}

	void findView() {
		dialog = new ProgressDialog(SettingScheduleActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("上传中......");
		et_className = (EditText) findViewById(R.id.et_Classname);
		tv_classNumLen = (TextView) findViewById(R.id.tv_ClassNumLen);
		tv_fromClassNum = (TextView) findViewById(R.id.tv_FromClassNum);
		et_classRoom = (EditText) findViewById(R.id.et_ClassRoom);
		tv_weekDay = (TextView) findViewById(R.id.tv_Weekday);
		btn_sendSchedule = (Button) findViewById(R.id.btn_sendSchedule);

		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("设置课程表");
		ll_classNumLen = (LinearLayout) findViewById(R.id.ll_classNumLen);
		ll_FromClassNum = (LinearLayout) findViewById(R.id.FromClassNum);
		ll_weekday = (LinearLayout) findViewById(R.id.ll_weekday);
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SettingScheduleActivity.this.finish();
			}
		});
	}

	private void initAction() {

		ll_classNumLen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showClassNumDialog();
			}
		});

		ll_FromClassNum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showFromClassNumDialog();
			}
		});		
		
		ll_weekday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showWeekdayDialog();
			}
		});
		
		// dbHelper = new DBHelper(this);
		// String className = et_className.getText().toString();
		// String classNumLen = et_classNumLen.getText().toString();
		// String fromClassNum = et_fromClassNum.getText().toString();
		// String classRoom = et_classRoom.getText().toString();
		// String weekDay = et_weekDay.getText().toString();
		//
		// classInfo = new ClassInfo();
		// classInfo.setClassname(className);
		// classInfo.setClassNumLen(classNumLen);
		// classInfo.setFromClassNum(fromClassNum);
		// classInfo.setClassRoom(classRoom);
		// classInfo.setWeekday(weekDay);
		//
		// long row = dbHelper.insertSchedule(classInfo);
		//
		// if(row == -1){
		// Toast.makeText(getApplicationContext(), "提交失败",
		// Toast.LENGTH_SHORT).show();
		// }else{
		// Toast.makeText(getApplicationContext(), "提交成功",
		// Toast.LENGTH_SHORT).show();
		//
		// }
		//
		
		btn_sendSchedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(et_className.getText().toString())){
					Utils.toast("请输入课程名称");
					return;
				}
				if(TextUtils.isEmpty(et_classRoom.getText().toString())){
					Utils.toast("请输入上课地点");
					return;
				}
				if(TextUtils.isEmpty(tv_classNumLen.getText().toString())){
					Utils.toast("请输入课程节数");
					return;
				}
				if(TextUtils.isEmpty(tv_fromClassNum.getText().toString())){
					Utils.toast("请输入从第几节课开始");
					return;
				}
				if(TextUtils.isEmpty(tv_weekDay.getText().toString())){
					Utils.toast("请输入上课时间");
					return;
				}
				dialog.show();

				final String className = et_className.getText().toString();
				final String classRoom = et_classRoom.getText().toString();
				final String classNumLen = tv_classNumLen.getText().toString();
				final String fromClassNum = tv_fromClassNum.getText().toString();
				final String weekday = tv_weekDay.getText().toString();
				
				
				new SimpleNetTask(ctx) {
					boolean flag;
					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						if(flag){
							dialog.dismiss();
							Utils.toast("上传成功");
							SettingScheduleActivity.this.finish();
						}else{
							dialog.dismiss();
							Utils.toast("上传失败");
						}
					}
					
					@Override
					protected void doInBack() throws Exception {
						// TODO Auto-generated method stub
						param.clear();
						param.put("className", className);
						param.put("classRoom", classRoom);
						param.put("classNumLen", classNumLen);
						param.put("fromClassNum", fromClassNum);
						param.put("weekday", weekday);
						param.put("userID", Utils.getID());
						String jsonStr = new WebService(C.PUBLISHSCHEDULE, param).getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonStr);
					}
				}.execute();
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void showClassNumDialog() {
		final AlertDialog dlg = new AlertDialog.Builder(
				SettingScheduleActivity.this).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.alertdialog_choosenum);
		TextView tv_title = (TextView) window.findViewById(R.id.title);
		Button btn_finish = (Button) window.findViewById(R.id.finish);
		tv_title.setText("请选择该课程节数");
		final NumberPicker np = (NumberPicker) window
				.findViewById(R.id.numberPicker);
		np.setMaxValue(5);
		np.setMinValue(1);
		np.setFocusable(true);
		np.setFocusableInTouchMode(true);
		num = 1;
		np.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				// TODO Auto-generated method stub
				num = np.getValue();
			}
		});
		btn_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_classNumLen.setText(num+"");
				dlg.cancel();
			}
		});
	}
	
	private void  showFromClassNumDialog(){
		final AlertDialog dlg = new AlertDialog.Builder(
				SettingScheduleActivity.this).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.alertdialog_choosenum);
		TextView tv_title = (TextView) window.findViewById(R.id.title);
		Button btn_finish = (Button) window.findViewById(R.id.finish);
		tv_title.setText("选择从第几节课开始");
		final NumberPicker np = (NumberPicker) window
				.findViewById(R.id.numberPicker);
		np.setMaxValue(12);
		np.setMinValue(1);
		np.setFocusable(true);
		np.setFocusableInTouchMode(true);
		num = 1;
		np.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				// TODO Auto-generated method stub
				num = np.getValue();
			}
		});
		btn_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_fromClassNum.setText(num+"");
				dlg.cancel();
			}
		});
	}
	
	private void showWeekdayDialog(){
		final AlertDialog dlg = new AlertDialog.Builder(
				SettingScheduleActivity.this).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.alertdialog_weekday);
		TextView tv_title = (TextView) window.findViewById(R.id.title);
		Button btn_finish = (Button) window.findViewById(R.id.finish);
		tv_title.setText("选择上课时间");
		final NumberPicker np = (NumberPicker) window
				.findViewById(R.id.numberPicker);
		np.setMaxValue(7);
		np.setMinValue(1);
		np.setFocusable(true);
		np.setFocusableInTouchMode(true);
		num = 1;
		np.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				// TODO Auto-generated method stub
				num = np.getValue();
			}
		});
		btn_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_weekDay.setText(num+"");
				dlg.cancel();
			}
		});
	}
}
