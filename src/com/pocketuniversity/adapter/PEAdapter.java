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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.adapter.LostAdapter.ViewHolder;
import com.pocketuniversity.bean.PE;

public class PEAdapter extends BaseAdapter {

	private List<PE> pe;
	private Context context;
	private LayoutInflater inflater;
	private DisplayImageOptions displayImageOptions;
	public PEAdapter(Context context,List<PE> pe,LayoutInflater inflater){
		this.context = context;
		this.pe = pe;
		this.inflater = inflater;
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.image_error)
				.showImageOnFail(R.drawable.image_error).build();
	}
	
	public void addpe(List<PE> newNewss){
		pe.addAll(newNewss);
	}

	public List<PE> getNewss() {
		return pe;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pe.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pe.get(position);
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
			convertView = inflater.inflate(R.layout.item_pe_listview, null);
			viewHolder.tv_title = (TextView) convertView.findViewById(R.id.fragment_news_listview_title);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.fragment_news_listview_photo);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.fragment_news_listview_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PE p = pe.get(position); 
		viewHolder.tv_title.setText(p.getTitle());
		viewHolder.tv_time.setText(p.getTime());
		String thumbnail = p.getPicUrl();
		if (thumbnail.length() > 0) {
			viewHolder.img.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(thumbnail,
					viewHolder.img, displayImageOptions);
		} else {
			viewHolder.img.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	public class ViewHolder{
		private TextView tv_title;
		private ImageView img;
		private TextView tv_time;
	}
	
}
