package com.pocketuniversity.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.bean.MyAddCampaign;

public class MyAddCampaignAdapter extends BaseListAdapter<MyAddCampaign>{

	private DisplayImageOptions displayImageOptions;

	
	public MyAddCampaignAdapter( Context context,  List<MyAddCampaign> list , int layoutId) {
		super(context, list, layoutId);
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.image_error)
				.showImageOnFail(R.drawable.image_error).build();
	}

	@Override
	public void conver(ViewHolder holder, int position, MyAddCampaign t) {
		// TODO Auto-generated method stub
		holder.setText(R.id.tvTitle, t.getTitle());
		holder.setText(R.id.tvExcept, t.getContent());
		holder.setText(R.id.tvComment, t.getComment_count());
		holder.setText(R.id.tvView, t.getView_count());
		holder.setText(R.id.tvTime, t.getTime());
		holder.setText(R.id.tvJoin, t.getJoin());
		holder.setText(R.id.tvHasJoin, t.getHasJoin());
		holder.setText(R.id.tvplace, t.getPlace());
		ImageView image = holder.getView(R.id.ivRight);
		ImageView img_state = holder.getView(R.id.img_state);
		String thumbnail = t.getImageurl();
		if (thumbnail.length() > 0) {
			image.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(thumbnail, image,
					displayImageOptions);
		} else {
			image.setVisibility(View.GONE);
		}
		String state = t.getState();
		if(state.equals("´ýÉóºË")){
			img_state.setImageResource(R.drawable.ic_report_wait);
		}else if (state.equals("ÒÑÍ¨¹ý")) {
			img_state.setImageResource(R.drawable.ic_report_pass);
		}else {
			img_state.setImageResource(R.drawable.ic_report_unpass);

		}
	}

}
