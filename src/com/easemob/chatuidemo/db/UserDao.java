/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.domain.RobotUser;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.video.util.Utils;
import com.easemob.util.HanziToPinyin;
import com.pocketuniversity.base.C;
import com.pocketuniversity.group.db.DBGroup;
import com.pocketuniversity.group.db.GroupDBHelper;
import com.pocketuniversity.https.WebService;

public class UserDao {
	public static final String TABLE_NAME = "uers";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_NAME_NICK = "nick";
	public static final String COLUMN_NAME_AVATAR = "avatar";
	
	public static final String PREF_TABLE_NAME = "pref";
	public static final String COLUMN_NAME_DISABLED_GROUPS = "disabled_groups";
	public static final String COLUMN_NAME_DISABLED_IDS = "disabled_ids";

	public static final String ROBOT_TABLE_NAME = "robots";
	public static final String ROBOT_COLUMN_NAME_ID = "username";
	public static final String ROBOT_COLUMN_NAME_NICK = "nick";
	public static final String ROBOT_COLUMN_NAME_AVATAR = "avatar";
	
	private DbOpenHelper dbHelper;

	private GroupDBHelper groupDBHelper;
	public UserDao(Context context) {
		dbHelper = DbOpenHelper.getInstance(context);
		groupDBHelper = new GroupDBHelper(context);
	}

	/**
	 * 保存好友list
	 * 
	 * @param contactList
	 */
	public void saveContactList(List<User> contactList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, null, null);
			for (User user : contactList) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_NAME_ID, user.getUsername());
				if(user.getNick() != null)
					values.put(COLUMN_NAME_NICK, user.getNick());
				if(user.getAvatar() != null)
				    values.put(COLUMN_NAME_AVATAR, user.getAvatar());
				db.replace(TABLE_NAME, null, values);
			}
		}
	}

	/**
	 * 获取好友list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Map<String, User> users = new HashMap<String, User>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
			while (cursor.moveToNext()) {
				String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
				String avatar = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AVATAR));
				User user = new User();
				user.setUsername(username);
				user.setNick(nick);
				user.setAvatar(avatar);
				String headerName = null;
				if (!TextUtils.isEmpty(user.getNick())) {
					headerName = user.getNick();
				} else {
					headerName = user.getUsername();
				}
				
				if (username.equals(Constant.NEW_FRIENDS_USERNAME) || username.equals(Constant.GROUP_USERNAME)) {
					user.setHeader("");
				} else if (Character.isDigit(headerName.charAt(0))) {
					user.setHeader("#");
				} else {
					user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1))
							.get(0).target.substring(0, 1).toUpperCase());
					char header = user.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setHeader("#");
					}
				}
				users.put(username, user);
			}
			cursor.close();
		}
		return users;
	}
	
	/**
	 * 根据id从uers中获取某个人昵称
	 */
	public String getwhosNickName(String id){
		String nickname = null ;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor cursor =  db.rawQuery("select * from " + TABLE_NAME + " where " +  COLUMN_NAME_ID + " =? ",new String[]{id});
			if(cursor.moveToNext()){
				nickname = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
			}
			cursor.close();
		}
		return nickname;
	}
	
	/**
	 * 更具username从uers中获取User对象
	 */
	public User getUserByUserName(String username){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		User user = new User();
		if(db.isOpen()){
			Cursor cursor= db.query(TABLE_NAME,new String[]{COLUMN_NAME_AVATAR,COLUMN_NAME_NICK}, COLUMN_NAME_ID + " like ? " ,new String[]{ username }, null, null,null);
			if(cursor.moveToNext()){
				System.out.println("根据username 获取的值 ：" + cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK)));
				user.setAvatar(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AVATAR)));
				user.setNick(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK)));
			}
			cursor.close();
		}
		return user;
	}
	
//	/**
//	 * 更具username从uers中获取User对象
//	 */
//	public User getUserByUserName2(String username){
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		User user = new User();
//		if(db.isOpen()){
//			String sql = " select * from " + TABLE_NAME + " where " + COLUMN_NAME_ID  + " like ? " 
//			if(cursor.moveToNext()){
//				System.out.println("根据username 获取的值 ：" + cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK)));
//				user.setAvatar(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AVATAR)));
//				user.setNick(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK)));
//			}
//			cursor.close();
//		}
//		return user;
//	}
	
	/**
	 * 删除一个联系人
	 * @param username
	 */
	public void deleteContact(String username){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[]{username});
		}
	}
	
	/**
	 * 保存一个联系人
	 * @param user
	 */
	public void saveContact(User user){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, user.getUsername());
		if(user.getNick() != null)
			values.put(COLUMN_NAME_NICK, user.getNick());
		if(user.getAvatar() != null)
		    values.put(COLUMN_NAME_AVATAR, user.getAvatar());
		if(db.isOpen()){
			db.replace(TABLE_NAME, null, values);
		}
	}
	
	public void setDisabledGroups(List<String> groups){
        setList(COLUMN_NAME_DISABLED_GROUPS, groups);
    }
    
    public List<String>  getDisabledGroups(){       
        return getList(COLUMN_NAME_DISABLED_GROUPS);
    }
    
    public void setDisabledIds(List<String> ids){
        setList(COLUMN_NAME_DISABLED_IDS, ids);
    }
    
    public List<String> getDisabledIds(){
        return getList(COLUMN_NAME_DISABLED_IDS);
    }
    
    private void setList(String column, List<String> strList){
        StringBuilder strBuilder = new StringBuilder();
        
        for(String hxid:strList){
            strBuilder.append(hxid).append("$");
        }
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(column, strBuilder.toString());

            db.update(PREF_TABLE_NAME, values, null,null);
        }
    }
    
    private List<String> getList(String column){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + column + " from " + PREF_TABLE_NAME,null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String strVal = cursor.getString(0);
        if (strVal == null || strVal.equals("")) {
            return null;
        }
        
        cursor.close();
        
        String[] array = strVal.split("$");
        
        if(array != null && array.length > 0){
            List<String> list = new ArrayList<String>();
            for(String str:array){
                list.add(str);
            }
            
            return list;
        }
        
        return null;
    }
	
    
    public Map<String, RobotUser> getRobotUser(){
    	return DemoDBManager.getInstance().getRobotList();
    }
    
    public void saveRobotUser(List<RobotUser> robotList){
    	DemoDBManager.getInstance().saveRobotList(robotList);
    }
    
    /**
     * 获取群列表
     */
	public void getMyJoinGroup(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Map<String, String> param = new HashMap<String, String>();
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
			}
		}).start();
	}
}
