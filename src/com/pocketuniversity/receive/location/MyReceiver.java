package com.pocketuniversity.receive.location;



import com.example.pocketuniversity.R;
import com.pocketuniversity.bean.Reminder;
import com.pocketuniversity.function.activity.ReminderDetailPushActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class MyReceiver  extends BroadcastReceiver {
	private NotificationManager manager;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
    	Toast.makeText(context, "您的提醒事项到时了！", Toast.LENGTH_LONG).show();
    	manager = (NotificationManager)context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
    	//String id = intent.getStringExtra("id");
    	
    	Reminder reminder = (Reminder) intent.getSerializableExtra("post");
    	Intent playIntent = new Intent(context, ReminderDetailPushActivity.class);
    	
    	playIntent.putExtra("post", reminder);
    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    	NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
    	builder.setContentTitle("提醒").setContentText(reminder.getContent()).setSmallIcon(R.drawable.icon_logo_nobg).setDefaults(Notification.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true);
    	manager.notify(1, builder.build());
	}

}
