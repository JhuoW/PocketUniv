package com.pocketuniversity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.pocketuniversity.R;
import com.pocketuniversity.function.activity.CarActivity;
import com.pocketuniversity.function.activity.CourierActivity;
import com.pocketuniversity.function.activity.HistoryActivity;
import com.pocketuniversity.function.activity.PEActivity;
import com.pocketuniversity.function.activity.RecommendFriendsActivity;
import com.pocketuniversity.function.activity.ReminderActivity;
import com.pocketuniversity.function.activity.WeatherActivity;
import com.pocketuniversity.tiaozao.activity.TiaoZaoDetailActivity;

public class MoreFragment extends Fragment implements OnClickListener {

	RelativeLayout rl_kuaidi;
	RelativeLayout rl_PC;
	RelativeLayout rl_history;
	RelativeLayout rl_car;
	RelativeLayout rl_weather;
	RelativeLayout rl_notice;
	RelativeLayout rl_Friends;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_more,
				container, false);
		rl_kuaidi = (RelativeLayout) view.findViewById(R.id.rl_kuaidi);
		rl_PC = (RelativeLayout) view.findViewById(R.id.rl_PC);
		rl_history = (RelativeLayout) view.findViewById(R.id.rl_history);
		rl_car = (RelativeLayout) view.findViewById(R.id.rl_car);
		rl_weather = (RelativeLayout) view.findViewById(R.id.rl_weather);
		rl_notice = (RelativeLayout) view.findViewById(R.id.rl_Notice);
		rl_Friends = (RelativeLayout) view.findViewById(R.id.rl_Friend);
		rl_kuaidi.setOnClickListener(this);
		rl_PC.setOnClickListener(this);
		rl_history.setOnClickListener(this);
		rl_car.setOnClickListener(this);
		rl_weather.setOnClickListener(this);
		rl_notice.setOnClickListener(this);
		rl_Friends.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_kuaidi:
			Intent intent = new Intent(getActivity(),
					CourierActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;

		case R.id.rl_PC:
			Intent intent2 = new Intent(getActivity(),
					PEActivity.class);
			startActivity(intent2);
			getActivity().overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.rl_history:
			Intent intent3 = new Intent(getActivity(),
					HistoryActivity.class);
			startActivity(intent3);
			getActivity().overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.rl_weather:
			Intent intent4 = new Intent(getActivity(),
					WeatherActivity.class);
			startActivity(intent4);
			getActivity().overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.rl_car:
			Intent intent5 = new Intent(getActivity(),
					CarActivity.class);
			startActivity(intent5);
			getActivity().overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.rl_Notice:
			Intent intent6 = new Intent(getActivity(),
					ReminderActivity.class);
			startActivity(intent6);
			getActivity().overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.rl_Friend:
			Intent intent7 = new Intent(getActivity(), RecommendFriendsActivity.class);
			startActivity(intent7);
			getActivity().overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		default:
			break;
		}
	}
}
