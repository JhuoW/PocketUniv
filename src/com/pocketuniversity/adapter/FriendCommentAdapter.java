package com.pocketuniversity.adapter;

import java.util.List;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.activity.UserDetailActivity;
import com.pocketuniversity.adapter.FriendsCircleCommentListAdapter.ViewHolder;
import com.pocketuniversity.bean.Comment;
import com.pocketuniversity.bean.FriendsCircle;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.view.MyProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendCommentAdapter extends BaseAdapter {

	private List<Comment> data;
	private Context context;
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	
	public FriendCommentAdapter(List<Comment> data, Context context) {
		this.data = data;
		this.context = context;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.item_friendcircle_comment, null);
			holder = new ViewHolder();
			holder.com_name = (TextView) view
					.findViewById(R.id.tv_comment_name);
			holder.com_time = (TextView) view
					.findViewById(R.id.tv_comment_time);
			holder.com_content = (TextView) view
					.findViewById(R.id.tv_comment_content);
			holder.comment_item_layout = (RelativeLayout) view.findViewById(R.id.commentclick);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final Comment comment = data.get(position);
		holder.com_name.setText(comment.getName());
		holder.com_time.setText(comment.getTime());
		holder.com_content.setText(comment.getContent());

		final String userId = comment.getCode();
		holder.comment_item_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						UserDetailActivity.class);
				intent.putExtra("userId", userId);
				context.startActivity(intent);
			}
		});
		return view;	}

	public class ViewHolder {
		TextView com_name;
		TextView com_time;
		TextView com_content;
		RelativeLayout comment_item_layout;
	}
	
}
