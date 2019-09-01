package com.pocketuniversity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.base.App;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.db.Friends;
import com.pocketuniversity.db.FriendsDBHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.listener.ICallBack;
import com.pocketuniversity.utils.PhotoUtils;

public class UserService {

	public static final int ORDER_UPDATED_AT = 1;
	public static final int ORDER_DISTANCE = 0;
	public static ImageLoader imageLoader = ImageLoader.getInstance();

	public static FriendsDBHelper friendsDBHelper; 
	
	/**
	 * �ӷ�������ȡ��������
	 * @return
	 * @throws Exception
	 */
	public static List<PUUser> findFriends() throws Exception {
		friendsDBHelper = new FriendsDBHelper(App.ctx);
		PUUser curUser = new PreferenceMap(App.ctx).getUser();
		Map<String, String> param = new HashMap<String, String>();
		param.put("userID", curUser.getID());
		String jsonstr = new WebService(C.GETFRIEND, param).getReturnInfo();
		Log.i("LoginActivity", "��ȡ�ĺ���(findFriend()����)-->" + jsonstr);
		/*��ȡ�ĺ���(findFriend()����)
		 * { "ret":"success","ischanged":"true","userFriendList":
		 * [{"ID":"100000071"
		 * ,"imgUrl":"","nickName":"kingsize","sex":"��","address"
		 * :"����ʡ������","authority":"manager"}, {"ID":"100000063","imgUrl":
		 * "http://app.iriscsc.com:8080/UserFile/100000063/Image/Picture/1000000632015511220506.jpg"
		 * ,
		 * "nickName":"Mort","sex":"��","address":"����ʡ������","authority":"default"}
		 * , {"ID":"100000059","imgUrl":
		 * "http://app.iriscsc.com:8080/UserFile/100000059/Image/Picture/1000000592015511220442.jpg"
		 * ,"nickName":"zhouxiaoming","sex":"Ů","address":"����ʡ�γ���","authority":
		 * "default"}, {"ID":"100000045","imgUrl":
		 * "http://app.iriscsc.com:8080/UserFile/100000045/Image/Picture/1000000452015511122407.jpg"
		 * ,
		 * "nickName":"��ˮ��","sex":"��","address":"����ʡ����������������·","authority":"manager"
		 * }, {"ID":"100000061","imgUrl":
		 * "http://app.iriscsc.com:8080/UserFile/100000061/Image/Picture/1000000612015511220438.jpg"
		 * ,"nickName":"����","sex":"Ů","address":"����ʡ������","authority":"default"},
		 * {"ID":"100000065","imgUrl":
		 * "http://app.iriscsc.com:8080/UserFile/100000065/Image/Picture/1000000652015518101046.jpg"
		 * ,
		 * "nickName":"����","sex":"��","address":"����ʡ�������캣·��·","authority":"manager"
		 * }, {"ID":"100000052","imgUrl":
		 * "http://app.iriscsc.com:8080/UserFile/100000052/Image/Picture/1000000522015522224321.jpg"
		 * ,
		 * "nickName":"��","sex":"��","address":"ɽ��ʡ������ۥ����","authority":"manager"
		 * }]}
		 */
		
		/**
		 * �������б�������ݿ�
		 */
		JSONObject json = new JSONObject(jsonstr);
		if(json.get("ret").equals("success")){
			JSONArray jsonarray = json
					.getJSONArray("newFriendList");
			friendsDBHelper.deleteAllDataFromTable(Friends.TABLE_NAME);
			friendsDBHelper.insertFriends(jsonarray);
		}
		
		List<PUUser> friend = GetObjectFromService.getFriend(jsonstr);
		final List<PUUser> friends = new ArrayList<PUUser>();
		friends.addAll(friend);
		return friends;
	}

	public static void displayAvatar(String imageUrl, ImageView avatarView) {
		imageLoader.displayImage(imageUrl, avatarView,
				PhotoUtils.getImageOptions(R.drawable.icon_default_avatar));
	}

	
	
	/**
	 * ��Ӻ���
	 * @param friendId
	 * @param saveCallback
	 */
	public static void addFriend(String friendId, final ICallBack saveCallback) {
		PUUser user = new PreferenceMap(App.ctx).getUser();
		Map<String, String> param = new HashMap<String, String>();
		param.put("Remarks", "��ã�����" + user.getName());
		param.put("fromUserID", user.getID());
		param.put("toUserID", friendId);
		String jsonstr = new WebService(C.REQUESTFRIEND, param).getReturnInfo();
		Boolean flag = GetObjectFromService.getSimplyResult(jsonstr);
		saveCallback.onBackMessage(flag);
	}
}
