package com.pocketuniversity.adapter;

import java.util.List;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.adapter.CenterListAdp.Containner;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.bean.PostModel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CampaignCenterListAdp extends BaseAdapter {

	private DisplayImageOptions displayImageOptions;
	private LayoutInflater inflater;

	private List<CampaignPostModel> list;

	public CampaignCenterListAdp(List<CampaignPostModel> list, LayoutInflater inflater) {
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
			convertView = inflater.inflate(R.layout.lay_post_lv_campaign_item, null);
			
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
			containner.tvJoin = (TextView) convertView
					.findViewById(R.id.tvJoin);
			containner.tvHasJoin = (TextView) convertView
					.findViewById(R.id.tvHasJoin);
			containner.tvplace = (TextView) convertView
					.findViewById(R.id.tvplace);
			convertView.setTag(containner);
		} else {
			containner = (Containner) convertView.getTag();
		}
		
		final CampaignPostModel post=list.get(position);
		containner.tvTitle.setText(post.getTitle());
		
		Log.i("CampaignCenterList", post.getTitle());
		
		containner.tvExcept.setText(post.getContent());
		containner.tvTime.setText(post.getTime());
		containner.tvView.setText(post.getView_count());
		containner.tvComment.setText(post.getComment_count());
		Log.i("CampaignCenterList--comment", post.getComment_count());

		containner.tvJoin.setText(post.isJoin());
		Log.i("CampaignCenterList--isJoin", post.isJoin());

		containner.tvHasJoin.setText(post.gethasJoin());
		Log.i("CampaignCenterList--hasJoin", post.gethasJoin());

		containner.tvplace.setText(post.getPlace());
		Log.i("CampaignCenterList--place", post.getPlace());

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
		private TextView tvJoin;
		private TextView tvHasJoin;
		private TextView tvplace ;
	}
}
