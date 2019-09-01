package com.pocketuniversity.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.friendcircle.activity.ImagePagerActivity;

public class NoScrollGridAdapter extends BaseAdapter {

	/** 上下�? */
	private Context ctx;
	/** 图片Url集合 */
	private ArrayList<String> imageUrls;

	public NoScrollGridAdapter(Context ctx, ArrayList<String> urls) {
		this.ctx = ctx;
		this.imageUrls = urls;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrls == null ? 0 : imageUrls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imageUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ViewHolder") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		  ViewHolder holder = null;
		  if (convertView == null) {
			   holder = new ViewHolder();
	            convertView = LayoutInflater.from
	                    (this.ctx).inflate(R.layout.item_gridview, null, false);
	            holder.content = (RelativeLayout)convertView.findViewById(R.id.content);
	            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_image);
	            convertView.setTag(holder);
		  }else{
			  holder = (ViewHolder) convertView.getTag();
		  }
		  
		  
		  DisplayImageOptions options = new DisplayImageOptions.Builder()//
			.cacheInMemory(true)//
			.cacheOnDisc(true)//
			.bitmapConfig(Config.RGB_565)//
			.build();
		  ImageLoader.getInstance().displayImage(imageUrls.get(position), holder.imageView, options);
		  
		  holder.content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imageBrower(position, imageUrls);
			}
		});
		  
//		View view = View.inflate(ctx, R.layout.item_gridview, null);
//		ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
//		DisplayImageOptions options = new DisplayImageOptions.Builder()//
//				.cacheInMemory(true)//
//				.cacheOnDisc(true)//
//				.bitmapConfig(Config.RGB_565)//
//				.build();
//		ImageLoader.getInstance().displayImage(imageUrls.get(position), imageView, options);
		return convertView;
	}
	
	private class ViewHolder{
		RelativeLayout content;
		ImageView imageView;
	}
	
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(ctx, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		ctx.startActivity(intent);
	}

}
