package com.pocketuniversity.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.activity.UserDetailActivity;
import com.pocketuniversity.bean.SortUser;
import com.pocketuniversity.service.UserService;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.view.ViewHolder;

@SuppressLint("DefaultLocale")
public class UserFriendAdapter extends BaseAdapter implements SectionIndexer {
	private Context ct;
	private List<SortUser> data;
	public static ImageLoader imageLoader = ImageLoader.getInstance();

	public UserFriendAdapter(Context ct, List<SortUser> datas) {
		this.ct = ct;
		this.data = datas;
	}

	public void updateDatas(List<SortUser> list) {
		this.data = list;
		notifyDataSetChanged();
	}

	public void remove(SortUser user) {
		this.data.remove(user);
		notifyDataSetChanged();
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
		if (convertView == null) {
			convertView = LayoutInflater.from(ct).inflate(
					R.layout.item_user_friend, null);
		}
		TextView alpha = ViewHolder.findViewById(convertView, R.id.alpha);
		LinearLayout layout = ViewHolder.findViewById(convertView, R.id.layout);
		TextView nameView = ViewHolder.findViewById(convertView,
				R.id.tv_friend_name);
		ImageView avatarView = ViewHolder.findViewById(convertView,
				R.id.img_friend_avatar);

		final SortUser friend = data.get(position);
		final String name = friend.getInnerUser().getName();
		final String avatarUrl = friend.getInnerUser().getImage();

		UserService.displayAvatar(avatarUrl, avatarView);

		imageLoader.displayImage(avatarUrl, avatarView,
				PhotoUtils.getImageOptions(R.drawable.icon_default_avatar));
		nameView.setText(name);
		avatarView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ct, UserDetailActivity.class);
				intent.putExtra("userId", friend.getInnerUser().getID());
				ct.startActivity(intent);
			}
		});
		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			layout.setVisibility(View.VISIBLE);
			alpha.setText(friend.getSortLetters());
		} else {
			layout.setVisibility(View.GONE);
		}

		return convertView;
	}

	public int getSectionForPosition(int position) {
		return data.get(position).getSortLetters().charAt(0);
	}

	@SuppressLint("DefaultLocale")
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = data.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

}
