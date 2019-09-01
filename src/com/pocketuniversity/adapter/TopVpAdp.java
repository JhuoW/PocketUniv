package com.pocketuniversity.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.bean.PostModel;
import com.pocketuniversity.bean.TopVpItem;

public class TopVpAdp extends PagerAdapter {

	private List<TopVpItem> topVpItems;
	private DisplayImageOptions displayImageOptions;
	private List<PostModel> topPostModels;

	public TopVpAdp(List<TopVpItem> topVpItems, List<PostModel> topPostModels) {
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
		topVpItems.get(position).textView.setText(topPostModels.get(position)
				.getTitle());
		String thumbnail = topPostModels.get(position).getImageurl();
		topVpItems.get(position).imageView.setImageResource(R.drawable.ic_valid_loading);
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
