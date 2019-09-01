package com.pocketuniversity.base;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import cn.jpush.android.api.JPushInterface;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.domain.User;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pocketuniversity.utils.PhotoUtils;

public class App extends Application{
	public static boolean debug = true;
	public static App ctx;
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();
	private List<Activity> list = new ArrayList<Activity>();
	public static Context context ; 
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this ;
		ctx = this;
		initImageLoader(ctx);
		JPushInterface.setDebugMode(false);
		JPushInterface.init(this);
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// ���app������Զ�̵�service����application:onCreate�ᱻ����2��
		// Ϊ�˷�ֹ����SDK����ʼ��2�Σ��Ӵ��жϻᱣ֤SDK����ʼ��1��
		// Ĭ�ϵ�app�����԰���ΪĬ�ϵ�process name�����У�����鵽��process name����app��process name����������

		if (processAppName == null ||!processAppName.equalsIgnoreCase("com.example.pocketuniversity")) {
		    //"com.easemob.chatuidemo"Ϊdemo�İ����������Լ���Ŀ��Ҫ�ĳ��Լ�����
		    // ���application::onCreate �Ǳ�service ���õģ�ֱ�ӷ���
		    return;
		}else{
			EMChat.getInstance().init(ctx);
			hxSDKHelper.onInit(ctx);
			EMChat.getInstance().setDebugMode(true);
		}
	}
	
	
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
	}
	@SuppressLint("NewApi")
	public void openStrictMode() {
		if (App.debug) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().build());
		}
	}

	/**
	 * ��ȡ�ڴ��к���user list
	 * @return
	 */
	public Map<String, User> getContactList() {
		return hxSDKHelper.getContactList();
	}

	/**
	 * �˳���¼,�������
	 */
	public void logout(final EMCallBack emCallBack) {
		// �ȵ���sdk logout��������app���Լ�������
		hxSDKHelper.logout(emCallBack);
	}
	/**
	 * ��ȡ��ǰ��½�û���
	 *
	 * @return
	 */
	public String getUserName() {
	    return hxSDKHelper.getHXId();
	}
	/**
	 * ���ú���user list���ڴ���
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		hxSDKHelper.setContactList(contactList);
	}

	public static App getInstance() {

		return ctx;
	}

	public void addActivity(Activity activity) {
		list.add(activity);
	}

	public void exit() {
		for (Activity activity : list) {
			activity.finish();
		}
		System.exit(0);
	}

	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"pocketuniversity/Cache");
		ImageLoaderConfiguration config = PhotoUtils.getImageLoaderConfig(
				context, cacheDir);
		ImageLoader.getInstance().init(config);
	}

	/**
	 * �����û���
	 *
	 * @param user
	 */
	public void setUserName(String username) {
	    hxSDKHelper.setHXId(username);
	}
	
	/**
	 * �������� �����ʵ������ ֻ��demo��ʵ�ʵ�Ӧ������Ҫ��password ���ܺ���� preference ����sdk
	 * �ڲ����Զ���¼��Ҫ�����룬�Ѿ����ܴ洢��
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
	    hxSDKHelper.setPassword(pwd);
	}
	

}
