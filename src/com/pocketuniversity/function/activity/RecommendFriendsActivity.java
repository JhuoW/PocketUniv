package com.pocketuniversity.function.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.activity.UserDetailActivity;
import com.pocketuniversity.adapter.RecommendFriendsAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.bean.SearchGroup;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;

public class RecommendFriendsActivity extends BaseActivity implements OnClickListener ,OnItemClickListener {

	private ListView listView;
	private RecommendFriendsAdapter adapter;
	private TextView tv_changeFriends;
	private List<PUUser> list = new ArrayList<PUUser>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend_friends);
		initView();
		getRecommendFriends();
	}
	
	private void initView(){
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("ÍÆ¼öºÃÓÑ");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RecommendFriendsActivity.this.finish();
			}
		});
		tv_changeFriends = (TextView) findViewById(R.id.changeGroup);
		tv_changeFriends.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.hotGroup_listview);
		adapter = new RecommendFriendsAdapter(list, getLayoutInflater());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	
	private void getRecommendFriends(){
		new SimpleNetTask(ctx) {
			List<PUUser> temp=new ArrayList<PUUser>();
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				if(temp==null){
					Utils.toast("net wrong");
					return;
				}
				list.clear();
				list.addAll(temp);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("userID", Utils.getID());
				param.put("num", 8+"");
				String jsonStr = new WebService(C.GETRECOMMENDFRIEND, param).getReturnInfo();
				temp = GetObjectFromService.getRecommendFriendList(jsonStr);
			}
		}.execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.changeGroup:
			getRecommendFriends();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(RecommendFriendsActivity.this, UserDetailActivity.class);
		intent.putExtra("userId", list.get(position).getID());
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}
}
