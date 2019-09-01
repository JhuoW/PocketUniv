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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.adapter.HotGroupAdapter.ViewHolder;
import com.pocketuniversity.bean.PUUser;

public class RecommendFriendsAdapter extends BaseAdapter {

	private List<PUUser> list ;
	private LayoutInflater inflater;
	private DisplayImageOptions displayImageOptions;

	public RecommendFriendsAdapter(List<PUUser> list , LayoutInflater inflater){
		this.list = list;
		this.inflater = inflater;
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.default_avatar)
				.showImageOnFail(R.drawable.default_avatar).build();
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
			convertView = inflater.inflate(R.layout.item_recommend_friend, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.address = (TextView) convertView.findViewById(R.id.address);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.avatar);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PUUser user = list.get(position);
		viewHolder.name.setText(user.getName());
		viewHolder.address.setText(user.getAddress());
		String imgUrl = user.getImage();
		ImageLoader.getInstance().displayImage(imgUrl, viewHolder.img,
				displayImageOptions);
		return convertView;
	}
	
	public class ViewHolder{
		public TextView name;
		public TextView address;
		public ImageView img;
	}
}
