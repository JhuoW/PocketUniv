package com.pocketuniversity.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chatuidemo.domain.User;
import com.pocketuniversity.base.App;
import com.pocketuniversity.bean.PUUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class FriendsDBHelper extends SQLiteOpenHelper {

	private static SQLiteDatabase mSqLiteDatabase;
	private static final String DB_NAME = "friends.db";
	private static final int VERSION = 1;

	public FriendsDBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		mSqLiteDatabase = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String friendsList = "create table if not exists " + Friends.TABLE_NAME
				+ " ( _id integer primary key autoincrement , " + Friends.ID
				+ " text , " + Friends.USERID + " text , " + Friends.SEX
				+ " text , " + Friends.NICKNAME + " text , "
				+ Friends.AUTHORITY + " text , " + Friends.IMGURL + " text ,  "
				+ Friends.ADDRESS + " text ) ; ";
		db.execSQL(friendsList);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String deleteTable = "DROP TABLE " + Friends.TABLE_NAME;
		db.execSQL(deleteTable);

		String friendsList = "create table if not exists " + Friends.TABLE_NAME
				+ " ( _id integer primary key autoincrement , " + Friends.ID
				+ " text , " + Friends.USERID + " text , " + Friends.SEX
				+ " text , " + Friends.NICKNAME + " text , "
				+ Friends.AUTHORITY + " text , " + Friends.IMGURL + " text ,  "
				+ Friends.ADDRESS + " text ) ; ";
		db.execSQL(friendsList);
	}

	/**
	 * 插入Friends表
	 * 
	 * @param friends
	 */
	public void insertFriends(JSONArray friends) {
		mSqLiteDatabase.delete(Friends.TABLE_NAME, null, null);
		for (int j = 0; j < friends.length() && friends != null; j++) {
			ContentValues contentValues = new ContentValues();
			try {
				JSONObject friendsObj = friends.getJSONObject(j);
				String id = friendsObj.getString("ID");
				String userId = friendsObj.getString("userId");
				String sex = friendsObj.getString("sex");
				String nickname = friendsObj.getString("nickName");
				String authority = friendsObj.getString("authority");
				String imgUrl = friendsObj.getString("imgUrl");
				String address = friendsObj.getString("address");
				contentValues.put(Friends.ID, id);
				contentValues.put(Friends.USERID, userId);
				contentValues.put(Friends.SEX, sex);
				contentValues.put(Friends.NICKNAME, nickname);
				contentValues.put(Friends.AUTHORITY, authority);
				contentValues.put(Friends.IMGURL, imgUrl);
				contentValues.put(Friends.ADDRESS, address);

				mSqLiteDatabase.insert(Friends.TABLE_NAME, null, contentValues);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 清空某张表内数据
	 */
	public void deleteAllDataFromTable(String Tablename){
		mSqLiteDatabase.execSQL("DELETE FROM " + Tablename);
	}
	
	/**
	 * 更具username 查询user;
	 */
	public PUUser getUserByUserName(String username){
		System.out.println("username :"+ username);
		PUUser user = new PUUser();
		Cursor cursor = mSqLiteDatabase.query(Friends.TABLE_NAME, null, Friends.USERID + " = ? ", new String[]{username}, null, null, null);
		while(cursor.moveToNext()){
			user.setName(cursor.getString(cursor.getColumnIndex(Friends.NICKNAME)));
			user.setImage(cursor.getString(cursor.getColumnIndex(Friends.IMGURL)));
			user.setUserId(cursor.getString(cursor.getColumnIndex(Friends.USERID)));
		}
		cursor.close();
		return user;
	}
	
	/**
	 * 获取所有好友
	 */
	public List<PUUser> getAllFriends(){
		List<PUUser> list = new ArrayList<PUUser>();
		Cursor cursor = mSqLiteDatabase.query(Friends.TABLE_NAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			PUUser user = new PUUser();
			user.setName(cursor.getString(cursor.getColumnIndex(Friends.NICKNAME)));
			user.setImage(cursor.getString(cursor.getColumnIndex(Friends.IMGURL)));
			user.setUserId(cursor.getString(cursor.getColumnIndex(Friends.USERID)));
			user.setAuthority(cursor.getString(cursor.getColumnIndex(Friends.AUTHORITY)));
			user.setSex(cursor.getString(cursor.getColumnIndex(Friends.SEX)));
			user.setAddress(cursor.getString(cursor.getColumnIndex(Friends.ADDRESS)));
			list.add(user);
		}
		cursor.close();
		return list;
	}
}
