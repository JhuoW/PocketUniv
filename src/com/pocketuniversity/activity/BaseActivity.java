package com.pocketuniversity.activity;

import java.util.HashMap;
import java.util.Map;

import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chatuidemo.video.util.Utils;
import com.pocketuniversity.view.HeaderLayout;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;


public abstract class BaseActivity extends FragmentActivity {
	protected Context ctx;
	protected HeaderLayout headerLayout;
	protected Map<String, String> param = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
	}
	
	

	protected void hideSoftInputView() {
		Utils.hideSoftInputView(this);
	}

	protected void setSoftInputMode() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
    /**
     * ����
     * 
     * @param view
     */
    public void back(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // onresumeʱ��ȡ��notification��ʾ
        HXSDKHelper.getInstance().getNotifier().reset();
        
        // umeng
        MobclickAgent.onResume(this);
    }
    
}
