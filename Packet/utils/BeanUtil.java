//package com.pocketuniversity.friend.utils;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.util.Log;
//
//public class BeanUtil {
//	private final String LOCAL_TIME = "local_time";
//	private final String LAST_ID = "last_id";
//	private static BeanUtil instance;
//
//	private BeanUtil() {
//	}
//
//	public static BeanUtil getInstance() {
//		if (instance == null) {
//			instance = new BeanUtil();
//		}
//		return instance;
//	}
//
//	/**
//	 * json数据转为friend ben对象
//	 * 
//	 * @param str
//	 * @return
//	 * @throws JSONException
//	 */
//	public Friend json2Friend(JSONObject json) throws Exception {
//		Friend friend = new Friend();
//		friend.friendId = friend.id = json.getInt("id");
//		friend.photo = json.getString("userFace");
//		friend.shareType = json.getInt("shareType");
//		friend.name = json.getString("nickName");
//		friend.linkUrl = json.getString("linkUrl");
//		friend.contentText = json.getString("content");
//		friend.userId = json.getString("userId");
//		friend.replyCount=json.getInt("replyCount");
//		friend.images=json.getString("photos");  
//		String str = json.getString("datetime");
//		String timeStr =str;// handTime(str);  
//		friend.sendDate = timeStr;
//		friend.favourName = getFavouName(json.getJSONArray("praiseItems"));
//		ArrayList<Reply> list = new ArrayList<Reply>();
//		JSONArray array = json.getJSONArray("replyItems");
//		int lenght = array.length();
//		for (int i = 0; i < lenght; i++) {
//			list.add(json2Reply(array.getJSONObject(i), friend.id));
//		}
//		friend.replyList = list;
//		// ========按时间排序缓存最后一条的发表时间
//		return friend;
//	}
//
//	private String getFavouName(JSONArray pariseArray) throws Exception {
//		StringBuilder sb = new StringBuilder();
//		int pSize = pariseArray.length();
//		JSONObject parise;
//		for (int j = 0; j < pSize; j++) {
//			parise = pariseArray.getJSONObject(j);
//			sb.append(parise.getString("nickName"));
//			if ((j + 1) != pSize)
//				sb.append(",");
//		}
//		return sb.toString();
//	}
//
//	/**
//	 * json数据转为Reply ben对象
//	 * 
//	 * @param str
//	 * @return
//	 * @throws Exception
//	 */
//	public static Reply json2Reply(JSONObject rJson, int id) throws Exception {
//		Reply reply = new Reply();
//		reply.id = rJson.getInt("id");
//		reply.content = rJson.getString("content");
//		reply.sendName = rJson.getString("nickName");
//		reply.replyName = rJson.getString("replyNickName");
//		reply.userId = rJson.getString("userId");
//		reply.friendId = id;// 对应的动态id
//		return reply;
//	}
//
//	/**
//	 * 根据json数据转换为bean集合
//	 * 
//	 * @param obj
//	 * @param db
//	 * @param shared
//	 * @return
//	 */
//	public List<Friend> getFriends(String obj) {
//		try {
//			JSONArray array = new JSONArray(obj);
//			// 缓存�?后一条的时间
//			List<Friend> list = new ArrayList<Friend>();
//			int length = array.length();
//			Friend friend;
//			JSONObject json;
//			List<Reply> replys;
//			for (int i = 0; i < length; i++) {
//				json = array.getJSONObject(i);
//				friend = json2Friend(json);
//				list.add(friend);
//				// 缓存10�?
//			}
//			return list;
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.d("getFriends", "动�?�数据转换失�?....");
//		}
//		return null;
//	}
//
//	/**
//	 * 处理时间
//	 * 
//	 * @param string
//	 * @return
//	 */
//	private String handTime(String time) {
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		if (time == null || "".equals(time.trim())) {
//			return "";
//		}
//		try {
//			Date date = format.parse(time);
//			long tm = System.currentTimeMillis();// 当前时间�?
//			long tm2 = date.getTime();// 发表动�?�的时间�?
//			long d = (tm - tm2) / 1000;// 时间差距 单位�?
//			if ((d / (60 * 60 * 24)) > 0) {
//				return d / (60 * 60 * 24) + "天前";
//			} else if ((d / (60 * 60)) > 0) {
//				return d / (60 * 60) + "小时�?";
//			} else if ((d / 60) > 0) {
//				return d / 60 + "分钟�?";
//			} else {
//				// return d + "秒前";
//				return "刚刚";
//			}
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//}
