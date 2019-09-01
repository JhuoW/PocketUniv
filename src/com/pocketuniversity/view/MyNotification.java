package com.pocketuniversity.view;

import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.AddContactActivity;
import com.pocketuniversity.activity.CampaignPostDetailActivity;
import com.pocketuniversity.activity.NewFriendsMsgActivity;
import com.pocketuniversity.activity.PostDetailActivity;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.bean.Notify;
import com.pocketuniversity.bean.PostModel;
import com.pocketuniversity.bean.Reminder;
import com.pocketuniversity.bean.push.CampaignNotify;
import com.pocketuniversity.bean.push.NewsNotify;
import com.pocketuniversity.bean.push.RemindNotify;
import com.pocketuniversity.function.activity.ReminderDetailActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.content.Context;
import android.content.Intent;

public class MyNotification {
	Context context;
	String jsonstr;//
	Notify notify;//
	CampaignNotify campaignNotify;
	NewsNotify newsNotify;
	RemindNotify remindNotify;
	String title;// ¢ò
	String image;//
	int type;//

	NotificationManager manager;
	int notificationID;

	public MyNotification(Context context, Notify notify) {
		super();
		this.context = context;
		this.notify = notify;
		manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public MyNotification(Context context, CampaignNotify campaignNotify) {
		super();
		this.context = context;
		this.campaignNotify = campaignNotify;
		manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public MyNotification(Context context, NewsNotify newsNotify) {
		super();
		this.context = context;
		this.newsNotify = newsNotify;
		manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public MyNotification(Context context, RemindNotify remindNotify) {
		super();
		this.context = context;
		this.remindNotify = remindNotify;
		manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@SuppressLint("NewApi")
	public void sendNotification() {
		Intent intent = new Intent();
		if (notify.getType().equals("RequestAddFriend")) {
			intent.setClass(context, NewFriendsMsgActivity.class);
		}
		// else if (notify.getType() == 1) {
		// intent.setClass(context, NewsDetailActivity.class);
		// } else if (notify.getType() == 2) {
		// intent.setClass(context, NewsDetailActivity.class);
		// }
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent,PendingIntent.FLAG_UPDATE_CURRENT);

		Builder builder = new Notification.Builder(context);

		builder.setSmallIcon(R.drawable.icon_logo_nobg);
		builder.setTicker(notify.getTitle());
		builder.setWhen(System.currentTimeMillis());
		builder.setContentTitle(notify.getTitle());
		builder.setContentText(notify.getContent());
		builder.setContentIntent(pendingIntent);

		builder.setDefaults(Notification.DEFAULT_SOUND);
		Notification notification = builder.build();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		manager.notify(notificationID, notification);
	}

	@SuppressLint("NewApi")
	public void sendNotificationForCampaign() {
		Intent intent = new Intent();
		if (campaignNotify.getType().equals("PushCampaign")) {
			CampaignPostModel post = new CampaignPostModel();
			post.setCollection(campaignNotify.getCollection());
			post.setContent(campaignNotify.getContent());
			post.setHasJoin(campaignNotify.getHasJoin());
			post.setId(campaignNotify.getId());
			post.setImageurl(campaignNotify.getImageurl());
			post.setJoin(campaignNotify.getJoin());
			post.setPlace(campaignNotify.getPlace());
			post.setSource(campaignNotify.getSource());
			post.setTime(campaignNotify.getTime());
			post.setTitle(campaignNotify.getTitle());
			post.setComment_count(campaignNotify.getComment_count());
			post.setView_count(campaignNotify.getView_count());
			intent.putExtra("post", post);
			intent.setClass(context, CampaignPostDetailActivity.class);
		}
		// else if (notify.getType() == 1) {
		// intent.setClass(context, NewsDetailActivity.class);
		// } else if (notify.getType() == 2) {
		// intent.setClass(context, NewsDetailActivity.class);
		// }
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Builder builder = new Notification.Builder(context);

		builder.setSmallIcon(R.drawable.icon_logo_nobg);
		builder.setTicker(campaignNotify.getTitle());
		builder.setWhen(System.currentTimeMillis());
		builder.setContentTitle(campaignNotify.getTitle());
		builder.setContentText(campaignNotify.getTitle());
		builder.setContentIntent(pendingIntent);

		builder.setDefaults(Notification.DEFAULT_SOUND);
		Notification notification = builder.build();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		manager.notify(notificationID, notification);
	}
	
	@SuppressLint("NewApi")
	public void sendNotificationForNews() {
		Intent intent = new Intent();
		if (newsNotify.getType().equals("PushNews")) {
			PostModel post = new PostModel();
			post.setTitle(newsNotify.getTitle());
			post.setSource(newsNotify.getSource());
			post.setId(newsNotify.getId());
			post.setCollection(newsNotify.getCollection());
			intent.putExtra("post", post);
			intent.setClass(context, PostDetailActivity.class);
		}
		// else if (notify.getType() == 1) {
		// intent.setClass(context, NewsDetailActivity.class);
		// } else if (notify.getType() == 2) {
		// intent.setClass(context, NewsDetailActivity.class);
		// }
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Builder builder = new Notification.Builder(context);

		builder.setSmallIcon(R.drawable.icon_logo_nobg);
		builder.setTicker(newsNotify.getTitle());
		builder.setWhen(System.currentTimeMillis());
		builder.setContentTitle(newsNotify.getTitle());
		builder.setContentText(newsNotify.getTitle());
		builder.setContentIntent(pendingIntent);

		builder.setDefaults(Notification.DEFAULT_SOUND);
		Notification notification = builder.build();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		manager.notify(notificationID, notification);
	}

	
	@SuppressLint("NewApi")
	public void sendNotificationForRemind() {
		Intent intent = new Intent();
		if (remindNotify.getKind().equals("PushRemind")) {
			Reminder post = new Reminder();
			post.setContent(remindNotify.getContent());
			post.setCreateTime(remindNotify.getCreateTime());
			post.setId(remindNotify.getId());
			post.setRemindTime(remindNotify.getRemindTime());
			post.setType(remindNotify.getType());
			intent.putExtra("post", post);
			intent.setClass(context, ReminderDetailActivity.class);
		}
		// else if (notify.getType() == 1) {
		// intent.setClass(context, NewsDetailActivity.class);
		// } else if (notify.getType() == 2) {
		// intent.setClass(context, NewsDetailActivity.class);
		// }
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Builder builder = new Notification.Builder(context);

		builder.setSmallIcon(R.drawable.icon_logo_nobg);
		builder.setTicker(remindNotify.getContent());
		builder.setWhen(System.currentTimeMillis());
		builder.setContentTitle("Ã·–—");
		builder.setContentText(remindNotify.getContent());
		builder.setContentIntent(pendingIntent);

		builder.setDefaults(Notification.DEFAULT_SOUND);
		Notification notification = builder.build();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		manager.notify(notificationID, notification);
	}

}
