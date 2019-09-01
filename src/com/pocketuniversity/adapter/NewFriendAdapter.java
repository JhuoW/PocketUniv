package com.pocketuniversity.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.activity.NewFriendsMsgActivity;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.RequestUser;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.view.ViewHolder;

public class NewFriendAdapter extends BaseAdapter {
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	private List<RequestUser> data;
	private Context context;

	public NewFriendAdapter(Context context, List<RequestUser> data) {
		this.data = data;
		this.context = context;
	}

	@Override
	public View getView(int position, View conView, ViewGroup parent) {
		if (conView == null) {
			conView = LayoutInflater.from(context).inflate(
					R.layout.item_request_user, null);
		}
		final RequestUser addRequest = data.get(position);

		TextView nameView = ViewHolder.findViewById(conView,
				R.id.tv_request_name);
		final TextView statueView = ViewHolder.findViewById(conView,
				R.id.tv_request_operate);
		TextView remarkView = ViewHolder.findViewById(conView,
				R.id.tv_request_remark);
		ImageView avatarView = ViewHolder.findViewById(conView,
				R.id.iv_request_avatar);
		final Button addBtn = ViewHolder.findViewById(conView,
				R.id.btn_request_operate);
		imageLoader.displayImage(addRequest.getImage(), avatarView,
				PhotoUtils.getImageOptions(R.drawable.icon_default_avatar));

		nameView.setText(addRequest.getName());
		remarkView.setText(addRequest.getRemark());

		
		String status = addRequest.getStatus();

		if (status.equals("New")) {
			addBtn.setVisibility(View.VISIBLE);
			statueView.setVisibility(View.GONE);
			addBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					final Handler mHandler = new Handler() {
						public void handleMessage(Message msg) {
							if ((Boolean) msg.obj) {
								addBtn.setVisibility(View.GONE);
								statueView.setVisibility(View.VISIBLE);
								statueView.setText("已同意");
							}
						}
					};
					new Thread() {
						public void run() {
							Map<String, String> param = new HashMap<String, String>();
							param.put("fromUserID", addRequest.getID());
							param.put("toUserID", Utils.getID());
							param.put("commend", "Agree");
							String jsonstr = new WebService(C.NEWAFRIENDAPPLY,
									param).getReturnInfo();
							Log.i("NewFriendAdapter", "new friend apply --->" + jsonstr);
							
							Boolean flag = GetObjectFromService
									.getSimplyResult(jsonstr);
							Message msg = mHandler.obtainMessage();
							msg.obj = flag;
							mHandler.sendMessage(msg);
						};
					}.start();
				}
			});
		} else if (status.equals("Agree")) {
			addBtn.setVisibility(View.GONE);
			statueView.setVisibility(View.VISIBLE);
			statueView.setText("已同意");
		} else if (status.equals("Refuse")) {
			addBtn.setVisibility(View.GONE);
			statueView.setVisibility(View.VISIBLE);
			statueView.setText("已拒绝");
		}
		return conView;
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

}
