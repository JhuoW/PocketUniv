package com.pocketuniversity.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pocketuniversity.bean.Lost;
import com.pocketuniversity.bean.Notice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OtherDBHelper extends SQLiteOpenHelper {
	private static SQLiteDatabase mSqLiteDatabase;
	private static final String DB_NAME = "other.db";
	private static final int VERSION = 1;
	public static final String DROP_USER_TABLE = "drop table ";
	
	public OtherDBHelper(Context context) {
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
		String create_notice =  "create table if not exists "
				+ NoticeTable.TABLE_NAME
				+ "(  _id integer primary key autoincrement , "
				+ NoticeTable.ID + " text, " 
				+ NoticeTable.VIEWCOUNT + " text, "
				+ NoticeTable.TIME + " text,"
				+ NoticeTable.COLLECTION + " text," 
				+ NoticeTable.TITLE + " text,"
				+ NoticeTable.SOURCE + " text )";

		db.execSQL(create_notice);
		
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String deleteTable = "DROP TABLE " + NoticeTable.TABLE_NAME;
		db.execSQL(deleteTable);
		String create_notice =  "create table if not exists "
				+ NoticeTable.TABLE_NAME
				+ "(  _id integer primary key autoincrement , "
				+ NoticeTable.ID + " text, " 
				+ NoticeTable.VIEWCOUNT + " text, "
				+ NoticeTable.TIME + " text,"
				+ NoticeTable.COLLECTION + " text," 
				+ NoticeTable.TITLE + " text,"
				+ NoticeTable.SOURCE + " text )";

		db.execSQL(create_notice);
	}
	
	/**
	 * 插入通知表
	 */
	public void insertNotice(JSONArray notice) {
		mSqLiteDatabase.delete(NoticeTable.TABLE_NAME, null, null);
		for (int j = 0; j < notice.length() && notice != null; j++) {
			ContentValues contentValues = new ContentValues();
			try {
				JSONObject noticeObj = notice.getJSONObject(j);
				String id = noticeObj.getString("id");
				String viewCount = noticeObj.getString("viewCount");
				String time = noticeObj.getString("time");
				String collection = noticeObj.getString("collection");
				String title = noticeObj.getString("title");
				String source = noticeObj.getString("source");
				contentValues.put(NoticeTable.ID, id);
				contentValues.put(NoticeTable.VIEWCOUNT, viewCount);
				contentValues.put(NoticeTable.TIME, time);
				contentValues.put(NoticeTable.COLLECTION, collection);
				contentValues.put(NoticeTable.TITLE, title);
				contentValues.put(NoticeTable.SOURCE, source);
				mSqLiteDatabase.insert(NoticeTable.TABLE_NAME, null, contentValues);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public List<Notice> getAllNotice(){
		List<Notice> list = new ArrayList<Notice>();
		Cursor cursor = mSqLiteDatabase.query(NoticeTable.TABLE_NAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			Notice notice = new Notice();
			notice.setId(cursor.getString(cursor.getColumnIndex(NoticeTable.ID)));
			notice.setViewCount(cursor.getString(cursor.getColumnIndex(NoticeTable.VIEWCOUNT)));
			notice.setTime(cursor.getString(cursor.getColumnIndex(NoticeTable.TIME)));
			notice.setCollection(cursor.getString(cursor.getColumnIndex(NoticeTable.COLLECTION)));
			notice.setTitle(cursor.getString(cursor.getColumnIndex(NoticeTable.TITLE)));
			notice.setSource(cursor.getString(cursor.getColumnIndex(NoticeTable.SOURCE)));
			list.add(notice);
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
