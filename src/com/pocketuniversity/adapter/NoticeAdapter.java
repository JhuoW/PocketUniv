package com.pocketuniversity.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.pocketuniversity.adapter.LostAdapter.ViewHolder;
import com.pocketuniversity.bean.Notice;

public class NoticeAdapter extends BaseAdapter{

	private List<Notice> list ;
	private LayoutInflater inflater;
	private DisplayImageOptions displayImageOptions;
	
	public NoticeAdapter(List<Notice> list , LayoutInflater inflater){
		this.list = list;
		this.inflater = inflater;
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.image_error)
				.showImageOnFail(R.drawable.image_error).build();
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
			convertView = inflater.inflate(R.layout.item_notice_list, null);
			viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			viewHolder.source = (TextView) convertView.findViewById(R.id.source);
			viewHolder.viewCount = (TextView) convertView.findViewById(R.id.viewCount);
			viewHolder.collection = (TextView) convertView.findViewById(R.id.tvCollection);
			viewHolder.time = (TextView) convertView.findViewById(R.id.tvTime);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Notice notice = list.get(position);
		
		viewHolder.title.setText(notice.getTitle());
		viewHolder.source.setText(notice.getSource());
		viewHolder.viewCount.setText(notice.getViewCount());
		viewHolder.collection.setText(notice.getCollection());
		viewHolder.time.setText(notice.getTime());
		
		return convertView;
	}
	
	private class ViewHolder{
		private TextView title ;
		private TextView source;
		private TextView viewCount;
		private TextView collection;
		private TextView time;
	}

}
