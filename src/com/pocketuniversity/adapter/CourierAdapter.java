package com.pocketuniversity.adapter;

import java.util.List;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.bean.SortCourier;
import com.pocketuniversity.bean.SortDepart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class CourierAdapter extends BaseAdapter implements SectionIndexer {
	private Context ct;
	private List<SortCourier> data;
	private DisplayImageOptions displayImageOptions;
	public CourierAdapter(Context ct, List<SortCourier> datas) {
		this.ct = ct;
		this.data = datas;
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.image_error)
				.showImageOnFail(R.drawable.image_error).build();
	}
	
	public void updateDatas(List<SortCourier> list) {
		this.data = list;
		notifyDataSetChanged();
	}

	public void remove(SortCourier phone) {
		this.data.remove(phone);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(ct).inflate(
					R.layout.item_courier_list, null);
		}
		TextView tv_name = (TextView) convertView.findViewById(R.id.tvTitle);
		TextView tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
		TextView tv_url = (TextView) convertView.findViewById(R.id.tv_url);
		ImageView img = (ImageView) convertView.findViewById(R.id.ivRight);
		final SortCourier sortCourier = data.get(position);
		tv_name.setText(sortCourier.getCourier().getName());
		tv_phone.setText(sortCourier.getCourier().getPhone());
		tv_url.setText(sortCourier.getCourier().getUrl());
		String thumbnail = sortCourier.getCourier().getImgUrl();
		if (thumbnail.length() > 0) {
			img.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(thumbnail,
					img, displayImageOptions);
		} else {
			img.setVisibility(View.GONE);
		}
		int section = getSectionForPosition(position);
		return convertView;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = data.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == sectionIndex) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return data.get(position).getSortLetters().charAt(0);
		}

}
