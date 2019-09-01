package com.pocketuniversity.adapter;

import java.util.List;
import com.example.pocketuniversity.R;
import com.pocketuniversity.bean.SortDepart;
import com.pocketuniversity.view.ViewHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class DepartmentPhoneAdapter extends BaseAdapter implements
		SectionIndexer {

	private Context ct;
	private List<SortDepart> data;

	public DepartmentPhoneAdapter(Context ct, List<SortDepart> datas) {
		this.ct = ct;
		this.data = datas;
	}

	public void updateDatas(List<SortDepart> list) {
		this.data = list;
		notifyDataSetChanged();
	}

	public void remove(SortDepart phone) {
		this.data.remove(phone);
		notifyDataSetChanged();
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(ct).inflate(
					R.layout.item_depart_phone, null);
		}
		TextView tv_name = ViewHolder
				.findViewById(convertView, R.id.Department);
		final SortDepart sortDepart = data.get(position);
		String name = sortDepart.getSortDp().getName();
		tv_name.setText(name);
		int section = getSectionForPosition(position);
		return convertView;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressLint("DefaultLocale")
	public int getPositionForSection(int sectionIndex) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = data.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == sectionIndex) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return data.get(position).getSortLetters().charAt(0);
	}

}
