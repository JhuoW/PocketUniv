package com.pocketuniversity.adapter;

import java.util.List;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.adapter.CampaignCenterListAdp.Containner;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.bean.MyCampaignPostModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCampaignCenterListAdp extends BaseListAdapter<CampaignPostModel> {

	private DisplayImageOptions displayImageOptions;


	public MyCampaignCenterListAdp( Context context,  List<CampaignPostModel> list , int layoutId) {
		super(context, list, layoutId);
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.image_error)
				.showImageOnFail(R.drawable.image_error).build();
	}

	@Override
	public void conver(ViewHolder holder, int position, CampaignPostModel t) {

		holder.setText(R.id.tvTitle, t.getTitle());
		holder.setText(R.id.tvExcept, t.getContent());
		holder.setText(R.id.tvComment, t.getComment_count());
		holder.setText(R.id.tvView, t.getView_count());
		holder.setText(R.id.tvTime, t.getTime());
		holder.setText(R.id.tvJoin, t.isJoin());
		holder.setText(R.id.tvHasJoin, t.gethasJoin());
		holder.setText(R.id.tvplace, t.getPlace());
		ImageView image = holder.getView(R.id.ivRight);
		String thumbnail = t.getImageurl();
		if (thumbnail.length() > 0) {
			image.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(thumbnail, image,
					displayImageOptions);
		} else {
			image.setVisibility(View.GONE);
		}
	}
}
