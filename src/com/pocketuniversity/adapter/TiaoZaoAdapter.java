package com.pocketuniversity.adapter;

import java.util.List;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.bean.Tiaozao;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TiaoZaoAdapter extends BaseAdapter {

	private List<Tiaozao> list ;
	private LayoutInflater inflater;
	private DisplayImageOptions displayImageOptions;
	
	public TiaoZaoAdapter(List<Tiaozao> list , LayoutInflater inflater ){
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
			convertView = inflater.inflate(R.layout.item_tiaozao, null);
			viewHolder.iv_goods = (ImageView) convertView.findViewById(R.id.goodsImg);
			viewHolder.goodsName = (TextView) convertView.findViewById(R.id.goodsName);
			viewHolder.viewCount = (TextView) convertView.findViewById(R.id.viewCount);
			viewHolder.price = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Tiaozao tiaozao = list.get(position);
		viewHolder.goodsName.setText(tiaozao.getGoodsname());
		viewHolder.viewCount.setText(tiaozao.getView_count());
		viewHolder.price.setText(tiaozao.getPrice());
		
		String thumbnail = tiaozao.getImgUrl();
		if (thumbnail.length() > 0) {
			viewHolder.iv_goods.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(thumbnail,
					viewHolder.iv_goods, displayImageOptions);
		} else {
			viewHolder.iv_goods.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	public class ViewHolder{
		private ImageView iv_goods;
		private TextView goodsName;
		private TextView viewCount;
		private TextView price;
	}
	
}
