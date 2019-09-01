package com.pocketuniversity.more.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.base.App;
import com.pocketuniversity.collection.fragment.MyCampaignCollectionFragment;
import com.pocketuniversity.collection.fragment.MyGoodsCollectionFragment;
import com.pocketuniversity.collection.fragment.MyNewsCollectionFragment;
import com.pocketuniversity.collection.fragment.MyNoticeCollectionFragment;

public class MyCollectionActivity extends BaseActivity{

	private Button[] mTabs;
	private MyCampaignCollectionFragment myCampaignCollectionFragment;
	private MyNewsCollectionFragment myNewsCollectionFragment;
	private MyGoodsCollectionFragment myGoodsCollectionFragment;
	private MyNoticeCollectionFragment myNoticeCollectionFragment;
	private Fragment[] fragments;
	private int index;
	// 当前fragment的index
	private int currentTabIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_mycollection);
		initView();
		myCampaignCollectionFragment = new MyCampaignCollectionFragment();
		myNewsCollectionFragment = new MyNewsCollectionFragment();
		myGoodsCollectionFragment = new MyGoodsCollectionFragment();
		myNoticeCollectionFragment = new MyNoticeCollectionFragment();
		fragments = new Fragment[] { myNewsCollectionFragment, myCampaignCollectionFragment ,myGoodsCollectionFragment,myNoticeCollectionFragment};
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_collection_container, myNewsCollectionFragment)
				.add(R.id.fragment_collection_container, myCampaignCollectionFragment).hide(myCampaignCollectionFragment).show(myNewsCollectionFragment)
				.commit();

	}
	
	/**
	 * 初始化组件
	 */
	private void initView() {
		mTabs = new Button[4];
		mTabs[0] = (Button) findViewById(R.id.btn_news_collection);
		mTabs[1] = (Button) findViewById(R.id.btn_campaign_collection);
		mTabs[2] = (Button) findViewById(R.id.btn_goods_collection);
		mTabs[3] = (Button) findViewById(R.id.btn_notice_collection);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);

		registerForContextMenu(mTabs[1]);
	}
	
	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_news_collection:
			index = 0;
			break;
		case R.id.btn_campaign_collection:
			index = 1;
			break;
		case R.id.btn_goods_collection:
			index = 2;
			break;
		case R.id.btn_notice_collection:
			index = 3;
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
