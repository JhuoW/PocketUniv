package com.pocketuniversity.more.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.base.App;
import com.pocketuniversity.view.HeaderLayout;

public class SettingActivity extends BaseActivity implements OnClickListener {

	/**
	 * ��������Ϣ֪ͨ����
	 */
	private RelativeLayout rl_switch_notification;
	/**
	 * ������������
	 */
	private RelativeLayout rl_switch_sound;
	/**
	 * �����𶯲���
	 */
	private RelativeLayout rl_switch_vibrate;
	/**
	 * ��������������
	 */
	private RelativeLayout rl_switch_speaker;

	/**
	 * �������Ͳ���
	 */
	private RelativeLayout rl_switch_push;

	/**
	 * ������Ϣ֪ͨimageView
	 */
	private ImageView iv_switch_open_notification;
	/**
	 * �ر�����Ϣ֪ͨimageview
	 */
	private ImageView iv_switch_close_notification;
	/**
	 * ��������ʾimageview
	 */
	private ImageView iv_switch_open_sound;
	/**
	 * �ر�������ʾimageview
	 */
	private ImageView iv_switch_close_sound;
	/**
	 * ����Ϣ����ʾ
	 */
	private ImageView iv_switch_open_vibrate;
	/**
	 * �ر���Ϣ����ʾ
	 */
	private ImageView iv_switch_close_vibrate;
	/**
	 * ����������������
	 */
	private ImageView iv_switch_open_speaker;
	/**
	 * �ر���������������
	 */
	private ImageView iv_switch_close_speaker;

	/**
	 * ������
	 */
	private ImageView iv_switch_open_push;

	/**
	 * �ر�����
	 */
	private ImageView iv_switch_close_push;

	private EMChatOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_setting);
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false))
			return;
		initView();
		initAction();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_switch_notification:
			if (iv_switch_open_notification.getVisibility() == View.VISIBLE) {
				iv_switch_open_notification.setVisibility(View.INVISIBLE);
				iv_switch_close_notification.setVisibility(View.VISIBLE);
				rl_switch_sound.setVisibility(View.GONE);
				rl_switch_vibrate.setVisibility(View.GONE);

				options.setNotificationEnable(false);
				EMChatManager.getInstance().setChatOptions(options);

				HXSDKHelper.getInstance().getModel()
						.setSettingMsgNotification(false);
			} else {
				iv_switch_open_notification.setVisibility(View.VISIBLE);
				iv_switch_close_notification.setVisibility(View.INVISIBLE);
				rl_switch_sound.setVisibility(View.VISIBLE);
				rl_switch_vibrate.setVisibility(View.VISIBLE);

				options.setNotificationEnable(true);
				EMChatManager.getInstance().setChatOptions(options);
				HXSDKHelper.getInstance().getModel()
						.setSettingMsgNotification(true);
			}
			break;
		case R.id.rl_switch_sound:
			if (iv_switch_open_sound.getVisibility() == View.VISIBLE) {
				iv_switch_open_sound.setVisibility(View.INVISIBLE);
				iv_switch_close_sound.setVisibility(View.VISIBLE);
				options.setNoticeBySound(false);
				EMChatManager.getInstance().setChatOptions(options);
				HXSDKHelper.getInstance().getModel().setSettingMsgSound(false);
			} else {
				iv_switch_open_sound.setVisibility(View.VISIBLE);
				iv_switch_close_sound.setVisibility(View.INVISIBLE);
				options.setNoticeBySound(true);
				EMChatManager.getInstance().setChatOptions(options);
				HXSDKHelper.getInstance().getModel().setSettingMsgSound(true);
			}
			break;
		case R.id.rl_switch_vibrate:
			if (iv_switch_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
				iv_switch_close_vibrate.setVisibility(View.VISIBLE);
				options.setNoticedByVibrate(false);
				EMChatManager.getInstance().setChatOptions(options);
				HXSDKHelper.getInstance().getModel()
						.setSettingMsgVibrate(false);
			} else {
				iv_switch_open_vibrate.setVisibility(View.VISIBLE);
				iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
				options.setNoticedByVibrate(true);
				EMChatManager.getInstance().setChatOptions(options);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(true);
			}
			break;
		case R.id.rl_switch_speaker:
			if (iv_switch_open_speaker.getVisibility() == View.VISIBLE) {
				iv_switch_open_speaker.setVisibility(View.INVISIBLE);
				iv_switch_close_speaker.setVisibility(View.VISIBLE);
				options.setUseSpeaker(false);
				EMChatManager.getInstance().setChatOptions(options);
				HXSDKHelper.getInstance().getModel()
						.setSettingMsgSpeaker(false);
			} else {
				iv_switch_open_speaker.setVisibility(View.VISIBLE);
				iv_switch_close_speaker.setVisibility(View.INVISIBLE);
				options.setUseSpeaker(true);
				EMChatManager.getInstance().setChatOptions(options);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(true);
			}
			break;
		case R.id.rl_switch_push:
			if (iv_switch_open_push.getVisibility() == View.VISIBLE) {
				iv_switch_open_push.setVisibility(View.INVISIBLE);
				iv_switch_close_push.setVisibility(View.VISIBLE);
			} else {
				iv_switch_open_push.setVisibility(View.VISIBLE);
				iv_switch_close_push.setVisibility(View.INVISIBLE);
			}
			break;
		}
	}

	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);

		rl_switch_notification = (RelativeLayout) this
				.findViewById(R.id.rl_switch_notification);
		rl_switch_sound = (RelativeLayout) this
				.findViewById(R.id.rl_switch_sound);
		rl_switch_vibrate = (RelativeLayout) this
				.findViewById(R.id.rl_switch_vibrate);
		rl_switch_speaker = (RelativeLayout) this
				.findViewById(R.id.rl_switch_speaker);
		rl_switch_push = (RelativeLayout) this
				.findViewById(R.id.rl_switch_push);

		iv_switch_open_notification = (ImageView) this
				.findViewById(R.id.iv_switch_open_notification);
		iv_switch_close_notification = (ImageView) this
				.findViewById(R.id.iv_switch_close_notification);
		iv_switch_open_sound = (ImageView) this
				.findViewById(R.id.iv_switch_open_sound);
		iv_switch_close_sound = (ImageView) this
				.findViewById(R.id.iv_switch_close_sound);
		iv_switch_open_vibrate = (ImageView) this
				.findViewById(R.id.iv_switch_open_vibrate);
		iv_switch_close_vibrate = (ImageView) this
				.findViewById(R.id.iv_switch_close_vibrate);
		iv_switch_open_speaker = (ImageView) this
				.findViewById(R.id.iv_switch_open_speaker);
		iv_switch_close_speaker = (ImageView) this
				.findViewById(R.id.iv_switch_close_speaker);
		iv_switch_open_push = (ImageView) this
				.findViewById(R.id.iv_switch_open_push);
		iv_switch_close_push = (ImageView) this
				.findViewById(R.id.iv_switch_close_push);

		rl_switch_notification.setOnClickListener(this);
		rl_switch_sound.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		rl_switch_speaker.setOnClickListener(this);
		rl_switch_push.setOnClickListener(this);

	}

	private void initAction() {
		headerLayout.showTitle("����ҳ");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SettingActivity.this.finish();
			}
		});
		options = EMChatManager.getInstance().getChatOptions();

		if (options.getNotificationEnable()) {
			iv_switch_open_notification.setVisibility(View.VISIBLE);
			iv_switch_close_notification.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_notification.setVisibility(View.INVISIBLE);
			iv_switch_close_notification.setVisibility(View.VISIBLE);
		}
		if (options.getNoticedBySound()) {
			iv_switch_open_sound.setVisibility(View.VISIBLE);
			iv_switch_close_sound.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_sound.setVisibility(View.INVISIBLE);
			iv_switch_close_sound.setVisibility(View.VISIBLE);
		}
		if (options.getNoticedByVibrate()) {
			iv_switch_open_vibrate.setVisibility(View.VISIBLE);
			iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
			iv_switch_close_vibrate.setVisibility(View.VISIBLE);
		}

		if (options.getUseSpeaker()) {
			iv_switch_open_speaker.setVisibility(View.VISIBLE);
			iv_switch_close_speaker.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_speaker.setVisibility(View.INVISIBLE);
			iv_switch_close_speaker.setVisibility(View.VISIBLE);
		}

	}
	
    @Override
    public void onSaveInstanceState(Bundle outState) {
        
        super.onSaveInstanceState(outState);
        
    }
    
    

}
