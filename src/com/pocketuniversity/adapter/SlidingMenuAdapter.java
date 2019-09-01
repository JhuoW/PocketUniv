package com.pocketuniversity.adapter;

import java.util.List;

import com.example.pocketuniversity.R;
import com.pocketuniversity.adapter.CommentListAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SlidingMenuAdapter extends BaseAdapter{

	private List<String> titleList;
	private List<Integer> imgList;
	private Context context;

	public SlidingMenuAdapter(List<String> titleList , List<Integer> imgList, Context context) {
		this.titleList = titleList;
		this.imgList = imgList;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titleList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return titleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_sliding_menu,null);
			holder.item_menu_img = (ImageView) convertView.findViewById(R.id.menu_item_img);
			holder.item_menu_tv = (TextView) convertView.findViewById(R.id.menu_item_tv);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item_menu_img.setImageResource(imgList.get(position));
		holder.item_menu_tv.setText(titleList.get(position));
		return convertView;
	}
	
	
	public class ViewHolder{
		ImageView item_menu_img;
		TextView item_menu_tv;
	}

}
