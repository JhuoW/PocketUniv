package com.pocketuniversity.collection.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.CampaignPostDetailActivity;
import com.pocketuniversity.adapter.MyCampaignCenterListAdp;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

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

public class MyCampaignCollectionFragment extends Fragment{

	private ListView myCompaign_listview;
	private MyCampaignCenterListAdp adapter ;
	private List<CampaignPostModel> campaignList = new ArrayList<CampaignPostModel>();
	HeaderLayout headerLayout;
	private MyProgressDialog dialog;
	Map<String, String> param ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view  = inflater.inflate(R.layout.activity_my_campaign, container,false);
		dialog = new MyProgressDialog(getActivity());
		myCompaign_listview = (ListView) view.findViewById(R.id.campaign_listview);
		headerLayout = (HeaderLayout) view.findViewById(R.id.headerLayout);
		headerLayout.showTitle("ªÓ∂Ø ’≤ÿ");
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
		new SimpleNetTask(getActivity()) {
			
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub

				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				adapter=new MyCampaignCenterListAdp(getActivity(),campaignList ,R.layout.lay_post_lv_campaign_item);				
				myCompaign_listview.setAdapter(adapter);
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				String jsonStr = new WebService(C.GETMYCAMPAIGNCOLLECTIONLIST, param).getReturnInfo().toString();

				campaignList = GetObjectFromService.getMyCampaign(jsonStr);
			}
		}.execute();
	}
	
	private void initAction(){
		myCompaign_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					 Intent intent = new Intent(getActivity(),CampaignPostDetailActivity.class);
					intent.putExtra("post", campaignList.get(position));
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
