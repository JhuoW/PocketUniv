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
import com.pocketuniversity.bean.PostModel;

public class CenterListAdp extends BaseAdapter {

	private DisplayImageOptions displayImageOptions;
	private LayoutInflater inflater;

	private List<PostModel> list;

	public CenterListAdp(List<PostModel> list, LayoutInflater inflater) {
		this.list = list;
		this.inflater = inflater;
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.image_error)
				.showImageOnFail(R.drawable.image_error).build();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Containner containner;
		if (convertView == null) {
			containner = new Containner();
			convertView = inflater.inflate(R.layout.lay_post_lv_item, null);
			
			containner.tvTitle = (TextView) convertView
					.findViewById(R.id.tvTitle);
			containner.tvExcept = (TextView) convertView
					.findViewById(R.id.tvExcept);
			containner.ivRight = (ImageView) convertView
					.findViewById(R.id.ivRight);
			containner.tvComment = (TextView) convertView
					.findViewById(R.id.tvComment);
			containner.tvView = (TextView) convertView
					.findViewById(R.id.tvView);
			containner.tvTime = (TextView) convertView
					.findViewById(R.id.tvTime);
			convertView.setTag(containner);
		} else {
			containner = (Containner) convertView.getTag();
		}
		
		final PostModel post=list.get(position);
		containner.tvTitle.setText(post.getTitle());
		System.out.println("post.content:" + post.getContent());
		containner.tvExcept.setText(post.getContent());
		containner.tvTime.setText(post.getTime());
		containner.tvView.setText(post.getView_count());
		containner.tvComment.setText(post.getComment_count());
		// containner.ivRight
		String thumbnail = post.getImageurl();
		if (thumbnail.length() > 0) {
			containner.ivRight.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(thumbnail,
					containner.ivRight, displayImageOptions);
		} else {
			containner.ivRight.setVisibility(View.GONE);
		}
		return convertView;
	}

	public class Containner {
		private TextView tvTitle;
		private TextView tvExcept;
		private ImageView ivRight;
		private TextView tvComment;
		private TextView tvTime;
		private TextView tvView;
	}
}
