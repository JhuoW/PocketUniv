package com.pocketuniversity.collection.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.MyCollectionActivity;
import com.pocketuniversity.activity.PostDetailActivity;
import com.pocketuniversity.adapter.MyCollectionAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.PostModel;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MyNewsCollectionFragment extends Fragment {

	private List<PostModel> datas=new ArrayList<PostModel>();
	private MyCollectionAdapter adapter;
	HeaderLayout headerLayout;
	ListView collection_list ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view  = inflater.inflate(R.layout.activity_mycollection_layout, container,false);
		headerLayout = (HeaderLayout) view.findViewById(R.id.headerLayout);
		collection_list  = (ListView) view.findViewById(R.id.collection_list);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initAction();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initData();
	}
	private void initAction() {
		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		
		collection_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						PostDetailActivity.class);
				intent.putExtra("post", datas.get(position));
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}

	private void initData() {
		headerLayout.showTitle("–¬Œ≈ ’≤ÿ");
		adapter=new MyCollectionAdapter(getActivity(), datas, R.layout.lay_post_lv_item);
		collection_list.setAdapter(adapter);
		
		final Map<String, String> param = new HashMap<String, String>();
		param.put("userID", Utils.getID());
		
		
		new SimpleNetTask(getActivity(),true) {
			
			@Override
			protected void onSucceed() {
				adapter=new MyCollectionAdapter(ctx, datas, R.layout.lay_post_lv_item);
				collection_list.setAdapter(adapter);
			}
			
			@Override
			protected void doInBack() throws Exception {
				String jsonstr=new WebService(C.GETMYUSERCOLLECTIONLIST, param).getReturnInfo();
				datas=GetObjectFromService.getCollectionNews(jsonstr);
			}
		}.execute();
	}
	
}
