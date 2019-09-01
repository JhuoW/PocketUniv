package com.pocketuniversity.function.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
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
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.byl.datepicker.wheelview.view.DatePickerPopWindow;
import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Reminder;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class ReminderDetailPushActivity extends BaseActivity implements
OnClickListener  {


	private EditText remindDetail;
	private LinearLayout ll_type;
	private LinearLayout ll_choosetime;
	private TextView tv_type;
	private TextView tv_remindTime;
	private TextView tv_createTime;
	String sdate;
	Reminder reminder;

	String content;
	String type;
	String remindTime;
	String createTime;
	//String id;
	
	String isFinish;

	MyProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminderdetail_push);
		Intent intent = getIntent();
		initView();
		if (setData(intent)) {
			System.out.println("");
			initAction();
		} else {
			startActivity(new Intent(this, ReminderActivity.class));
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			finish();
		}
	}

	private void initView() {
		dialog = new MyProgressDialog(ctx);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		remindDetail = (EditText) findViewById(R.id.dailyContent);
		setEditTextEditable(remindDetail, false);
		ll_type = (LinearLayout) findViewById(R.id.ll_type);
		ll_choosetime = (LinearLayout) findViewById(R.id.ll_choosetime);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_createTime = (TextView) findViewById(R.id.tv_createTime);
		tv_remindTime = (TextView) findViewById(R.id.tv_remindTime);
		remindDetail.setOnClickListener(this);
	}

	private boolean setData(Intent intent) {
		reminder = (Reminder) intent.getSerializableExtra("post");
		content = reminder.getContent();
		type = reminder.getType();
		
		remindTime = reminder.getRemindTime();
		createTime = reminder.getCreateTime();
		//id = reminder.getId();
		remindDetail.setText(content);
		if (type.equals("1")) {
			tv_type.setText("普通事件");
		} else if (type.equals("2")) {
			tv_type.setText("重要事件");
		} else if (type.equals("3")) {
			tv_type.setText("紧急事件");
		}
		tv_remindTime.setText(remindTime);
		tv_createTime.setText(createTime);
		return true;
	}

	private void initAction() {
		headerLayout.showTitle("日程详情");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ReminderDetailPushActivity.this.finish();
			}
		});
	}

	private void modifyNote() {
		content = remindDetail.getText().toString();
		remindTime = tv_remindTime.getText().toString();
		final String kind;
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
		dialog.show();
		new SimpleNetTask(ctx) {
			boolean flag;

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (flag) {
					Utils.showtoast(ctx, R.drawable.tips_success, "修改日程成功");
					remindDetail.setEnabled(false);
				} else {
					Utils.showtoast(ctx, R.drawable.tips_success, "修改日程失败,请重试");
				}
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("userID", Utils.getID());
				param.put("id", reminder.getId());
				param.put("content", content);
				param.put("remindTime", remindTime);
				param.put("type", kind);
				String jsonStr = new WebService(C.MODIFYNOTE, param)
						.getReturnInfo();
				flag = GetObjectFromService.getSimplyResult(jsonStr);
			}
		}.execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dailyContent:
			setEditTextEditable(remindDetail, false);
			break;
		case R.id.ll_type:
			//showRemindType();
			break;
		case R.id.ll_choosetime:
			//showRemindTime();
			break;
		default:
			break;
		}
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

	private void showRemindTime() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		final DatePickerPopWindow popWindow = new DatePickerPopWindow(
				ReminderDetailPushActivity.this, df.format(date));
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

	
	/**
	 * 设置TextView是否可编辑
	 * @param editText
	 * @param value
	 */
	private void setEditTextEditable(EditText editText, boolean value) {
		if (value) {
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
		} else {
			editText.setFocusableInTouchMode(false);
			editText.clearFocus();
		}
	}


}
