package com.pocketuniversity.adapter;

import java.util.List;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.bean.PostModel;
import com.pocketuniversity.bean.TopPic;
import com.pocketuniversity.bean.TopVpItem;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class TopPicAdp extends PagerAdapter {

	private List<TopVpItem> topVpItems;
	private DisplayImageOptions displayImageOptions;
	private List<TopPic> topPostModels;

	public TopPicAdp(List<TopVpItem> topVpItems, List<TopPic> topPostModels) {
		this.topVpItems = topVpItems;
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().build();
		this.topPostModels = topPostModels;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(topVpItems.get(position).relativeLayout);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		String thumbnail = topPostModels.get(position).getImgUrl();
		topVpItems.get(position).imageView.setImageResource(R.drawable.ic_valid_loading);
		topVpItems.get(position).textView.setText("");
		if(thumbnail.length()>0){
			ImageLoader.getInstance()
			.displayImage(thumbnail, topVpItems.get(position).imageView,
					displayImageOptions);
		}

		((ViewPager) container)
				.addView(topVpItems.get(position).relativeLayout);
		return topVpItems.get(position).relativeLayout;
	}

	@Override
	public int getCount() {
		return topVpItems.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
