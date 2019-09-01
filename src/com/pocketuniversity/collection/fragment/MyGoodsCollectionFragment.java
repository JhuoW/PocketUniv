package com.pocketuniversity.collection.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.adapter.TiaoZaoAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Tiaozao;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.tiaozao.activity.TiaoZaoDetailActivity;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class MyGoodsCollectionFragment extends Fragment {

	private GridView gridView;
	private TiaoZaoAdapter adapter;
	private MyProgressDialog dialog;
	private List<Tiaozao> tiaozaoList = new ArrayList<Tiaozao>();
	HeaderLayout headerLayout;
	Map<String, String> param;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_tiaozao, container,
				false);
		dialog = new MyProgressDialog(getActivity());
		gridView = (GridView) view.findViewById(R.id.grid_view);
		headerLayout = (HeaderLayout) view.findViewById(R.id.headerLayout);
		headerLayout.showTitle("…Ã∆∑ ’≤ÿ");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		param = new HashMap<String, String>();
		param.put("userID", Utils.getID());
		getData();
		initAction();
	}

	private void initAction() {
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						TiaoZaoDetailActivity.class);
				intent.putExtra("post", tiaozaoList.get(position));
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}

	private void getData() {
		new SimpleNetTask(getActivity(), false) {

			@Override
			protected void onSucceed() {
				adapter = new TiaoZaoAdapter(tiaozaoList, getActivity()
						.getLayoutInflater());
				gridView.setAdapter(adapter);
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				String jsonStr = new WebService(C.GETMYGOODSCOLLECTION, param)
						.getReturnInfo();
				tiaozaoList = GetObjectFromService.getMyTiaozao(jsonStr);
			}
		}.execute();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}
}
