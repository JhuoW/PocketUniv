package com.pocketuniversity.more.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.activity.CampaignPostDetailActivity;
import com.pocketuniversity.adapter.MyCampaignCenterListAdp;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.fragment.MyAddCampaignFragment;
import com.pocketuniversity.fragment.MyJoinFragment;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.NetAsyncTask;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class MyCampaignActivity extends BaseActivity {

	private Button[] mTabs;
	private MyJoinFragment myJoinFragment;
	private MyAddCampaignFragment myAddCampaignFragment;

	private Fragment[] fragments;
	private int index;
	// 当前fragment的index
	private int currentTabIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.fragment_mycampaign);
		findView();
		myJoinFragment = new MyJoinFragment();
		myAddCampaignFragment = new MyAddCampaignFragment();
		fragments = new Fragment[] { myJoinFragment, myAddCampaignFragment };
		// 添加显示第一个fragment
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragment_collection_container,
						myJoinFragment)
				.add(R.id.fragment_collection_container,
						myAddCampaignFragment)
				.hide(myAddCampaignFragment)
				.show(myJoinFragment).commit();
	}

	private void findView() {
		mTabs = new Button[2];
		mTabs[0] = (Button) findViewById(R.id.btn_campaign_collection);
		mTabs[1] = (Button) findViewById(R.id.btn_goods_collection);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);

		registerForContextMenu(mTabs[1]);
	}
	
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_campaign_collection:
			index = 0;
			break;
		case R.id.btn_goods_collection:
			index = 1;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_collection_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}


}
