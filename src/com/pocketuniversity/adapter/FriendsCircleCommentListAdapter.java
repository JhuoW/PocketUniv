package com.pocketuniversity.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.activity.UserDetailActivity;
import com.pocketuniversity.bean.Comment;
import com.pocketuniversity.friendcircle.activity.ZanUserActivity;
import com.pocketuniversity.utils.PhotoUtils;

public class FriendsCircleCommentListAdapter extends BaseAdapter {

	private List<Comment> data;
	private Context context;
	public static ImageLoader imageLoader = ImageLoader.getInstance();

	public FriendsCircleCommentListAdapter(List<Comment> data, Context context) {
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.item_newscommentlist, null);
			holder = new ViewHolder();
			holder.com_imageview = (ImageView) view
					.findViewById(R.id.iv_comment_image);
			holder.com_name = (TextView) view
					.findViewById(R.id.tv_comment_name);
			holder.com_time = (TextView) view
					.findViewById(R.id.tv_comment_time);
			holder.com_content = (TextView) view
					.findViewById(R.id.tv_comment_content);
			holder.comment_item_layout = (LinearLayout) view
					.findViewById(R.id.comment_item_layout);
			holder.com_authority = (TextView) view
					.findViewById(R.id.tv_comment_authority);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final Comment comment = data.get(position);
		holder.com_name.setText(comment.getName());
		holder.com_time.setText(comment.getTime());
		holder.com_content.setText(comment.getContent());
		holder.com_authority.setText(comment.getAuthority());
		imageLoader.displayImage(comment.getImage_url(), holder.com_imageview,
				PhotoUtils.getImageOptions(R.drawable.icon_default_avatar));

		final String userId = comment.getCode();
		holder.com_imageview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						UserDetailActivity.class);
				intent.putExtra("userId", userId);
				context.startActivity(intent);
			}
		});
		return view;
	}

	class ViewHolder {
		ImageView com_imageview;
		TextView com_name;
		TextView com_time;
		TextView com_content;
		TextView com_authority;
		LinearLayout comment_item_layout;
		
	}

}
