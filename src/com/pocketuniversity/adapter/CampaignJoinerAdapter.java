package com.pocketuniversity.adapter;

import java.util.List;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.bean.CampaignJoiner;
import com.pocketuniversity.bean.MyAddCampaign;
import com.pocketuniversity.view.RoundImageView;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CampaignJoinerAdapter extends BaseListAdapter<CampaignJoiner> {

	private DisplayImageOptions displayImageOptions;

	public CampaignJoinerAdapter(Context context, List<CampaignJoiner> list,
			int layoutId) {
		super(context, list, layoutId);
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.ic_default_head)
				.showImageOnFail(R.drawable.ic_default_head).build();
	}

	@Override
	public void conver(ViewHolder holder, int position, CampaignJoiner t) {
		// TODO Auto-generated method stub
		holder.setText(R.id.tv_friend_name, t.getNickName());
		RoundImageView image = holder.getView(R.id.img_friend_avatar);
		String imgUrl = t.getHeader();
		ImageLoader.getInstance().displayImage(imgUrl, image,
				displayImageOptions);
	}
}
