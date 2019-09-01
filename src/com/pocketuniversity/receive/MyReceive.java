package com.pocketuniversity.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.pocketuniversity.activity.MainActivity;
import com.pocketuniversity.bean.Notify;
import com.pocketuniversity.bean.push.CampaignNotify;
import com.pocketuniversity.bean.push.NewsNotify;
import com.pocketuniversity.bean.push.RemindNotify;
import com.pocketuniversity.fragment.ContactlistFragment;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.view.MyNotification;


public class MyReceive extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			Notify notify = GetObjectFromService.getNotificationDetail(content);
			CampaignNotify campaignNotify = GetObjectFromService.getCampaignNotificationDetail(content);
			NewsNotify newsNotify = GetObjectFromService.getNewsNotificationDetail(content);
			RemindNotify remindNotify = GetObjectFromService.getRemindNotify(content);
			if (notify.getType().equals("RequestAddFriend")) {
				MyNotification notification = new MyNotification(context,
						notify);
				ContactlistFragment.showNewFriendTips();
				notification.sendNotification();
			} 
			if (campaignNotify.getType().equals("PushCampaign")&&new PreferenceMap(context).isPush()) {
				MyNotification notification = new MyNotification(context,
						campaignNotify);
				notification.sendNotificationForCampaign();
			}
			
			if(newsNotify.getType().equals("PushNews")&&new PreferenceMap(context).isPush()){
				MyNotification notification = new MyNotification(context,
						newsNotify);
				notification.sendNotificationForNews();
			}
			if(remindNotify.getKind().equals("PushRemind") && new PreferenceMap(context).isPush()){
				MyNotification notification = new MyNotification(context,
						remindNotify);
				notification.sendNotificationForRemind();
			}

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
		} else {
		}
	}

}
