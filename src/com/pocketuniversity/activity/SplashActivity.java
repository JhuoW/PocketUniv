package com.pocketuniversity.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.pocketuniversity.base.C;
import com.pocketuniversity.db.CampaignPost;
import com.pocketuniversity.db.DBHelper;
import com.pocketuniversity.db.Post;
import com.pocketuniversity.group.bean.Group;
import com.pocketuniversity.group.db.DBGroup;
import com.pocketuniversity.group.db.GroupDBHelper;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;

/**
 * �?屏页
 *
 */
public class SplashActivity extends BaseActivity {
	private RelativeLayout rootLayout;
	private TextView versionText;
	private DBHelper dbHelper ;
	GroupDBHelper groupDBHelper;
	private static final int sleepTime = 2000;
	List<Group> datas = new ArrayList<Group>();

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_splash);
		super.onCreate(arg0);

		groupDBHelper = new GroupDBHelper(ctx);
		
		dbHelper = new DBHelper(this);
		dbHelper.openSqLiteDatabase();
		rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
		//versionText = (TextView) findViewById(R.id.tv_version);
		//versionText.setText(getVersion());
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		rootLayout.startAnimation(animation);
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		new Thread(new Runnable() {
			public void run() {
				
				String ret = new APIHelper().getCampaignCategory();
				try {
					JSONObject json = new JSONObject(ret);
					if (json.get("ret").equals("success")) {
						JSONArray jsonarray = json
								.getJSONArray("campaignCategoryList");
						dbHelper.insertCampaignCategories(jsonarray);
						dbHelper.deleteAllDataFromTable(CampaignPost.tableName);
					}
				} catch (Exception e) {
				}
				
				String ret_news = new APIHelper().getNewsCatergory();
				try {
					JSONObject json = new JSONObject(ret_news);
					if (json.get("ret").equals("success")) {
						JSONArray jsonarray = json
								.getJSONArray("newsCategoryList");
						dbHelper.insertCategories(jsonarray);
						dbHelper.deleteAllDataFromTable(Post.tableName);
					}
				} catch (Exception e) {
				}
				
				
				param.clear();
				param.put("userID", Utils.getID());
				String jsonStr = new WebService(C.GETMYJOINGROUPLIST, param)
						.getReturnInfo();
				try {
					JSONObject json = new JSONObject(jsonStr);
					if (json.get("ret").equals("success")) {
						JSONArray jsonarray = json.getJSONArray("groupList");
						groupDBHelper.deleteAllDataFromTable(DBGroup.TABLENAME);
						groupDBHelper.insertGroup(jsonarray);
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				

				if (DemoHXSDKHelper.getInstance().isLogined()) {
					// ** 免登陆情�? 加载�?有本地群和会�?
					//不是必须的，不加sdk也会自动异步去加�?(不会重复加载)�?
					//加上的话保证进了主页面会话和群组都已经load完毕
					long start = System.currentTimeMillis();
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					long costTime = System.currentTimeMillis() - start;
					//等待sleeptime时长
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					//进入主页�?
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
					finish();
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					startActivity(new Intent(SplashActivity.this, LoginActivity.class));
					finish();
				}
			}
		}).start();

	}
	
	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		String st = getResources().getString(R.string.Version_number_is_wrong);
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
			String version = packinfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return st;
		}
	}
	


	
}
