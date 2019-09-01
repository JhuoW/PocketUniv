package com.pocketuniversity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pocketuniversity.R;
import com.pocketuniversity.bean.Reminder;

public class ReminderAdapter extends BaseAdapter {

	private List<Reminder> list;
	private Context context;
	private LayoutInflater inflater;
	public ReminderAdapter(Context context,List<Reminder> list,LayoutInflater inflater){
		this.context = context;
		this.list = list;
		this.inflater = inflater;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_reminder_list, null);
			viewHolder.remindTime = (TextView) convertView.findViewById(R.id.remindTime);
			viewHolder.content = (TextView) convertView.findViewById(R.id.tv_notice_content);
			viewHolder.iv_type = (ImageView) convertView.findViewById(R.id.iv_notice);
			viewHolder.createTime = (TextView) convertView.findViewById(R.id.createTime);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Reminder reminder = list.get(position);
		viewHolder.remindTime.setText(reminder.getRemindTime());
		viewHolder.content.setText(reminder.getContent());
		viewHolder.createTime.setText(reminder.getCreateTime());
		String type = reminder.getType();
		if(type.equals("1")){
			viewHolder.iv_type.setImageResource(R.drawable.mini_green_dot);
		}else if (type.equals("2")) {
			viewHolder.iv_type.setImageResource(R.drawable.mini_orange_dot);
		}else if (type.equals("3")) {
			viewHolder.iv_type.setImageResource(R.drawable.mini_red_dot);
		}
		return convertView;
	}
	
	public class ViewHolder{
		TextView remindTime;
		TextView content;
		ImageView iv_type;
		TextView createTime;
	}
}
