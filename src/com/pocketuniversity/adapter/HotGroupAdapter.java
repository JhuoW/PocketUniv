package com.pocketuniversity.adapter;

import java.util.List;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.pocketuniversity.adapter.LostAdapter.ViewHolder;
import com.pocketuniversity.bean.Lost;
import com.pocketuniversity.bean.SearchGroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HotGroupAdapter extends BaseAdapter{

	private List<SearchGroup> list ;
	private LayoutInflater inflater;
	
	public HotGroupAdapter(List<SearchGroup> list , LayoutInflater inflater){
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
			convertView = inflater.inflate(R.layout.row_group, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		SearchGroup searchGroup = list.get(position);
		viewHolder.name.setText(searchGroup.getGroupName());
		
		return convertView;
	}
	
	public class ViewHolder{
		private TextView name;
	}

}
