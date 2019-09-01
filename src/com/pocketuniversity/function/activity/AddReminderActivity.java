package com.pocketuniversity.function.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.byl.datepicker.wheelview.view.DatePickerPopWindow;
import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Reminder;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.receive.location.MyReceiver;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

@SuppressLint({ "ResourceAsColor", "SimpleDateFormat" })
public class AddReminderActivity extends BaseActivity implements
		OnClickListener {

	private EditText dailyContent;
	private LinearLayout ll_type;
	private LinearLayout ll_choosetime;
	private TextView tv_type;
	private TextView tv_remindTime;
	String sdate;
	MyProgressDialog dialog;
	String remindtime;
	String content;
	String type;
	String kind;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_remind);
		initView();
		initAction();
	}

	private void initView() {
		dialog = new MyProgressDialog(ctx);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("添加日程");
		dailyContent = (EditText) findViewById(R.id.dailyContent);
		ll_type = (LinearLayout) findViewById(R.id.ll_type);
		ll_choosetime = (LinearLayout) findViewById(R.id.ll_choosetime);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_remindTime = (TextView) findViewById(R.id.tv_remindTime);
		ll_type.setOnClickListener(this);
		ll_choosetime.setOnClickListener(this);
	}

	private void initAction() {

		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddReminderActivity.this.finish();
			}
		});
		headerLayout.showRightTextButton("添加", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addNote();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_type:
			showRemindType();
			break;

		case R.id.ll_choosetime:
			showRemindTime();
			break;
		default:
			break;
		}
	}

	public void addNote() {
		content  = dailyContent.getText().toString();
		remindtime = tv_remindTime.getText().toString();
		type = tv_type.getText().toString();
		
		if (type.equals("普通事件")) {
			kind = "1";
		} else if (type.equals("重要事件")) {
			kind = "2";
		} else if (type.equals("紧急事件")) {
			kind = "3";
		} else {
			kind = "0";
		}
		if (TextUtils.isEmpty(content)) {
			Utils.toast("请输入日程内容");
			return;
		}

		if (kind.equals("0")) {
			Utils.toast("请选择日程等级");
			return;
		}

		if (remindtime.equals("选择提醒时间")) {
			Utils.toast("请选择提醒时间");
			return;
		}

		dialog.show();
		new SimpleNetTask(ctx) {
			boolean flag;

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if (flag) {
					dialog.dismiss();
					Utils.showtoast(ctx, R.drawable.tips_success, "添加日程成功");
					setPush(true);
					AddReminderActivity.this.finish();
				} else {
					dialog.dismiss();
					Utils.showtoast(ctx, R.drawable.tips_error, "添加日程失败");
				}
			}

			@Override
			protected void doInBack() throws Exception {
				param.clear();
				param.put("userID", Utils.getID());
				param.put("content", content);
				param.put("remindtime", remindtime);
				param.put("type", kind);
				String jsonStr = new WebService(C.ADDNOTE, param)
						.getReturnInfo();
				flag = GetObjectFromService.getSimplyResult(jsonStr);
			}
		}.execute();
	}

	public void setPush(boolean b) {
		Date time = new Date();
		long s = 0;
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			time = fmt.parse(remindtime);
			s = time.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 设置本地推送
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent intent = new Intent();
		Reminder reminder = new Reminder();
		reminder.setContent(content);
		reminder.setCreateTime(Utils.getDetailTime());
		reminder.setIsFinish("false");
		reminder.setType(kind);
		reminder.setRemindTime(remindtime);
		intent.putExtra("post", reminder);
		intent.setClass(ctx, MyReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, intent, 0);
		if(b){  
            // just use current time + 10s as the Alarm time.   
            Calendar c=Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
			//可以根据项目要求修改，秒、分钟、提前、延后
            c.add(Calendar.SECOND, 10);
            // schedule an alarm  
           // am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pi); 
            am.set(AlarmManager.RTC_WAKEUP, s,pi);
        }  
        else{  
            // cancel current alarm  
            am.cancel(pi);  
        }  

	}

	private void showRemindTime() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		final DatePickerPopWindow popWindow = new DatePickerPopWindow(
				AddReminderActivity.this, df.format(date));
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.5f;
		getWindow().setAttributes(lp);
		popWindow.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
		popWindow.tv_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popWindow.dismiss();
				sdate = popWindow.birthday;
				tv_remindTime.setText(sdate);
			}
		});

		popWindow.tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popWindow.dismiss();
			}
		});
		popWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
	}

	private void showRemindType() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.alertdialog_choose_remindtype);
		TextView tv_Emergency = (TextView) window
				.findViewById(R.id.tv_content1);
		TextView tv_Importent = (TextView) window
				.findViewById(R.id.tv_content2);
		TextView tv_common = (TextView) window.findViewById(R.id.tv_content3);
		tv_Emergency.setText("紧急事件");
		tv_Importent.setText("重要事件");
		tv_common.setText("普通事件");
		tv_Emergency.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dlg.cancel();
				tv_type.setText("紧急事件");
			}
		});
		tv_Importent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dlg.cancel();
				tv_type.setText("重要事件");
			}
		});
		tv_common.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dlg.cancel();
				tv_type.setText("普通事件");
			}
		});
	}
}
