package com.pocketuniversity.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pocketuniversity.bean.Lost;
import com.pocketuniversity.bean.Tiaozao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LostDBHelper extends SQLiteOpenHelper{
	private static SQLiteDatabase mSqLiteDatabase;
	private static final String DB_NAME = "lost.db";
	private static final int VERSION = 1;
	public static final String DROP_USER_TABLE = "drop table ";
	
	public LostDBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		mSqLiteDatabase = getWritableDatabase();

	}
	
	
	public SQLiteDatabase openSqLiteDatabase() {
		if (mSqLiteDatabase == null) {
			mSqLiteDatabase = getWritableDatabase();
		}
		return mSqLiteDatabase;
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String create_lost = "create table if not exists "
				+ LostTable.TABLE_NAME
				+ "(  _id integer primary key autoincrement , "
				+ LostTable.USERID + " text, " 
				+ LostTable.LOSTID + " text, "
				+ LostTable.NICKNAME + " text,"
				+ LostTable.PHONE + " text," 
				+ LostTable.HEADER + " text,"
				+ LostTable.MAJOR + " text," 
				+ LostTable.TITLE + " text,"
				+ LostTable.PUBLISHTIME + " text," 
				+ LostTable.STATE + " text, "
				+ LostTable.LOSTTIME + " text, "
				+ LostTable.PLACE + " text, "
				+ LostTable.DETAIL + " text, "
				+ LostTable.IMGURL + " text )";
		db.execSQL(create_lost);
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String deleteTable = "DROP TABLE " + LostTable.TABLE_NAME;
		db.execSQL(deleteTable);
		String create_lost = "create table if not exists "
				+ LostTable.TABLE_NAME
				+ "(  _id integer primary key autoincrement , "
				+ LostTable.USERID + " text," 
				+ LostTable.LOSTID + " text, "
				+ LostTable.NICKNAME + " text,"
				+ LostTable.PHONE + " text," 
				+ LostTable.HEADER + " text,"
				+ LostTable.MAJOR + " text," 
				+ LostTable.TITLE + " text,"
				+ LostTable.PUBLISHTIME + " text," 
				+ LostTable.STATE + " text, "
				+ LostTable.LOSTTIME + " text, "
				+ LostTable.PLACE + " text, "
				+ LostTable.DETAIL + " text, "
				+ LostTable.IMGURL + " text )";
		db.execSQL(create_lost);
	}
	
	
	/**
	 * 插入失物招领表
	 */
	public void insertLost(JSONArray lost) {
		mSqLiteDatabase.delete(LostTable.TABLE_NAME, null, null);
		for (int j = 0; j < lost.length() && lost != null; j++) {
			ContentValues contentValues = new ContentValues();
			try {
				JSONObject lostObj = lost.getJSONObject(j);
				String userId = lostObj.getString("userId");
				String lostId = lostObj.getString("lostId");
				String nickName = lostObj.getString("nickName");
				String phone = lostObj.getString("phone");
				String header = lostObj.getString("header");
				String major = lostObj.getString("major");
				String title = lostObj.getString("title");
				String publishTime = lostObj.getString("publishTime");
				String state = lostObj.getString("state");
				String lostTime = lostObj.getString("lostTime");
				String place = lostObj.getString("place");
				String detail = lostObj.getString("detail");
				String imgUrl = lostObj.getString("imgUrl");
				contentValues.put(LostTable.USERID, userId);
				contentValues.put(LostTable.LOSTID, lostId);
				contentValues.put(LostTable.NICKNAME, nickName);
				contentValues.put(LostTable.PHONE, phone);
				contentValues.put(LostTable.HEADER, header);
				contentValues.put(LostTable.MAJOR, major);
				contentValues.put(LostTable.TITLE, title);
				contentValues.put(LostTable.PUBLISHTIME, publishTime);
				contentValues.put(LostTable.STATE, state);
				contentValues.put(LostTable.LOSTTIME,lostTime);
				contentValues.put(LostTable.PLACE, place);
				contentValues.put(LostTable.DETAIL, detail);
				contentValues.put(LostTable.IMGURL, imgUrl);
				mSqLiteDatabase.insert(LostTable.TABLE_NAME, null, contentValues);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public List<Lost> getAllLost(){
		List<Lost> list = new ArrayList<Lost>();
		Cursor cursor = mSqLiteDatabase.query(LostTable.TABLE_NAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			Lost lost = new Lost();
			lost.setUserId(cursor.getString(cursor.getColumnIndex(LostTable.USERID)));
			lost.setLostId(cursor.getString(cursor.getColumnIndex(LostTable.LOSTID)));
			lost.setNickname(cursor.getString(cursor.getColumnIndex(LostTable.NICKNAME)));
			lost.setPhone(cursor.getString(cursor.getColumnIndex(LostTable.PHONE)));
			lost.setHeader(cursor.getString(cursor.getColumnIndex(LostTable.HEADER)));
			lost.setMajor(cursor.getString(cursor.getColumnIndex(LostTable.MAJOR)));
			lost.setTitle(cursor.getString(cursor.getColumnIndex(LostTable.TITLE)));
			lost.setPublishTime(cursor.getString(cursor.getColumnIndex(LostTable.PUBLISHTIME)));
			lost.setState(cursor.getString(cursor.getColumnIndex(LostTable.STATE)));
			lost.setLostTime(cursor.getString(cursor.getColumnIndex(LostTable.LOSTTIME)));
			lost.setPlace(cursor.getString(cursor.getColumnIndex(LostTable.PLACE)));
			lost.setDetail(cursor.getString(cursor.getColumnIndex(LostTable.DETAIL)));
			lost.setImgUrl(cursor.getString(cursor.getColumnIndex(LostTable.IMGURL)));
			list.add(lost);
		}
		return list;
	}
	
	
	/**
	 * 清空某张表内数据
	 */
	public void deleteAllDataFromTable(String Tablename){
		mSqLiteDatabase.execSQL("DELETE FROM " + Tablename);
		
	}
	
}
