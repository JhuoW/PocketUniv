package com.pocketuniversity.adapter;

import java.util.List;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.adapter.TiaoZaoAdapter.ViewHolder;
import com.pocketuniversity.bean.Lost;
import com.pocketuniversity.bean.Tiaozao;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LostAdapter extends BaseAdapter{

	private List<Lost> list ;
	private LayoutInflater inflater;
	private DisplayImageOptions displayImageOptions;
	
	public LostAdapter(List<Lost> list , LayoutInflater inflater){
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
			convertView = inflater.inflate(R.layout.item_lost_list, null);
			viewHolder.img_state = (ImageView) convertView.findViewById(R.id.stateId);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.img_lost = (ImageView) convertView.findViewById(R.id.img_lost);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Lost lost = list.get(position);
		viewHolder.title.setText(lost.getTitle());
		viewHolder.time.setText(lost.getPublishTime());
		
		String thumbnail = lost.getImgUrl();
		if (thumbnail.length() > 0) {
			viewHolder.img_lost.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(thumbnail,
					viewHolder.img_lost, displayImageOptions);
		} else {
			viewHolder.img_lost.setVisibility(View.GONE);
		}
		
		String state = lost.getState();
		if(state.equals("Ê§Îï")){
			viewHolder.img_state.setImageResource(R.drawable.bg_lost_item);
		}else if (state.equals("ÕÐÁì")) {
			viewHolder.img_state.setImageResource(R.drawable.bg_found_item);
		}
		
		return convertView;
	}
	
	public class ViewHolder{
		private ImageView img_state;
		private TextView title;
		private TextView time;
		private ImageView img_lost;
		
	}

}
