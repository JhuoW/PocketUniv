package com.pocketuniversity.adapter;

import java.util.List;

import com.example.pocketuniversity.R;
import com.pocketuniversity.bean.DpDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DpPhoneDetailAdapter extends BaseAdapter {

	private Context ctx;
	List<DpDetail> list;

	public DpPhoneDetailAdapter(Context ctx, List<DpDetail> list) {
		this.ctx = ctx;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(ctx).inflate(R.layout.item_dp_detail, null);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.departmentName);
			viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.departmentPhone);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		DpDetail dd = list.get(position);
		viewHolder.tv_name.setText(dd.getName());
		viewHolder.tv_phone.setText(dd.getPhone());
		return convertView;
	}

	public class ViewHolder {
		private TextView tv_name;
		private TextView tv_phone;
	}

}
