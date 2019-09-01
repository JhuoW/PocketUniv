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
import android.widget.ListView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.CampaignPostDetailActivity;
import com.pocketuniversity.adapter.NoticeAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Notice;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.other.activity.NoticeDetailActivity;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class MyNoticeCollectionFragment extends Fragment {

	private ListView lv_notice ;
	List<Notice> noticeList = new ArrayList<Notice>();
	HeaderLayout headerLayout;
	private MyProgressDialog dialog;
	Map<String, String> param ;
	private NoticeAdapter adapter ; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_notice, container,false);
		dialog = new MyProgressDialog(getActivity());
		lv_notice = (ListView) view.findViewById(R.id.notice_listview);
		headerLayout = (HeaderLayout) view.findViewById(R.id.headerLayout);
		headerLayout.showTitle("Í¨ÖªÊÕ²Ø");
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
	
	private void getData(){
		new SimpleNetTask(getActivity(),false) {
			
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				adapter = new NoticeAdapter(noticeList, getActivity().getLayoutInflater());
				lv_notice.setAdapter(adapter);
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				String jsonStr = new WebService(C.GETMYNOTICECOLLECTION, param).getReturnInfo();
				noticeList = GetObjectFromService.getMyNotice(jsonStr);
			}
		}.execute();
	}
	
	private void initAction(){
		lv_notice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				 Intent intent = new Intent(getActivity(),NoticeDetailActivity.class);
				intent.putExtra("post", noticeList.get(position));
			//	intent.putExtra("isJoin", newsList.get(position).isJoin());
				 startActivity(intent);
				 getActivity().overridePendingTransition(R.anim.slide_in_right,
				 R.anim.slide_out_left);
			}
		});
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}
}
