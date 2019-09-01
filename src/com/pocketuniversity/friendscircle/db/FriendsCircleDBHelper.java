package com.pocketuniversity.friendscircle.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pocketuniversity.bean.Comment;
import com.pocketuniversity.bean.FriendsCircle;
import com.pocketuniversity.bean.MyDynamic;
import com.pocketuniversity.db.Friends;
import com.pocketuniversity.db.Schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FriendsCircleDBHelper extends SQLiteOpenHelper{
	
	private static SQLiteDatabase sqLiteDatabase;
	private static final String DBNAME = "friendscircle.db";
	private static final int VERSION = 1;

	public SQLiteDatabase openSqLiteDatabase() {
		if (sqLiteDatabase == null) {
			sqLiteDatabase = getWritableDatabase();
		}
		return sqLiteDatabase;
	}
	
	public FriendsCircleDBHelper(Context context) {
		super(context, DBNAME, null, VERSION);
		openSqLiteDatabase();
	}
	
	public void closeSqLiteDatabase() {
		if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
			sqLiteDatabase.close();
		}
	}

	public void releaseSqliteDatabase() {
		if (sqLiteDatabase != null) {
			sqLiteDatabase.close();
		}
		sqLiteDatabase = null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String friendscircle = "create table if not exists "
				+ FriendsCircleTable.TABLE_NAME
				+ " ( _id integer primary key autoincrement , "
				+ FriendsCircleTable.NAME + " text , " 
				+ FriendsCircleTable.HEADER + " text , " 
				+ FriendsCircleTable.IMGURL + " text , "
				+ FriendsCircleTable.TIME + " text , " 
				+ FriendsCircleTable.CONTENT + " text , " 
				+ FriendsCircleTable.COMMENTCOUNT + " text , " 
				+ FriendsCircleTable.ZANCOUNT + " text , " 
				+ FriendsCircleTable.ID + " text , " 
				+ FriendsCircleTable.USERID + " text , " 
				+ FriendsCircleTable.ISZAN + " text ) ; ";
		db.execSQL(friendscircle);
		
		String myDynamic = "create table if not exists "
				+ MyDynamicTable.TABLE_NAME
				+ " ( _id integer primary key autoincrement , "
				+ MyDynamicTable.NAME + " text , " 
				+ MyDynamicTable.HEADER + " text , " 
				+ MyDynamicTable.IMGURL + " text , "
				+ MyDynamicTable.TIME + " text , " 
				+ MyDynamicTable.CONTENT + " text , " 
				+ MyDynamicTable.COMMENTCOUNT + " text , " 
				+ MyDynamicTable.ZANCOUNT + " text , " 
				+ MyDynamicTable.ID + " text , " 
				+ MyDynamicTable.USERID + " text , " 
				+ MyDynamicTable.ISZAN + " text , "
				+ MyDynamicTable.DAY + " text , "
				+ MyDynamicTable.MONTH + " text ) ; ";
		db.execSQL(myDynamic);
		
		String comment = "create table if not exists "
				+ FriendsCommentDB.TABLE_NAME
				+ " ( _id integer primary key autoincrement , "
				+ FriendsCommentDB.NAME + " text , " 
				+ FriendsCommentDB.IMGURL + " text , "
				+ FriendsCommentDB.TIME + " text , " 
				+ FriendsCommentDB.CONTENT + " text , " 
				+ FriendsCommentDB.ID + " text , " 
				+ FriendsCommentDB.AUTHORITY + " text , "
				+ FriendsCommentDB.COMMENTID + " text ) ; ";
		db.execSQL(comment);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String deleteTable = "DROP TABLE " + FriendsCircleTable.TABLE_NAME;
		db.execSQL(deleteTable);
		String deletemyDynamicTable = "DROP TABLE " + MyDynamicTable.TABLE_NAME;
		db.execSQL(deletemyDynamicTable);
		String deletecommentDynamicTable = "DROP TABLE " + FriendsCommentDB.TABLE_NAME;
		db.execSQL(deletecommentDynamicTable);
		String friendscircle = "create table if not exists "
				+ FriendsCircleTable.TABLE_NAME
				+ " ( _id integer primary key autoincrement , "
				+ FriendsCircleTable.NAME + " text , " 
				+ FriendsCircleTable.HEADER + " text , " 
				+ FriendsCircleTable.IMGURL + " text , "
				+ FriendsCircleTable.TIME + " text , " 
				+ FriendsCircleTable.CONTENT + " text , " 
				+ FriendsCircleTable.COMMENTCOUNT + " text , " 
				+ FriendsCircleTable.ZANCOUNT + " text , " 
				+ FriendsCircleTable.ID + " text , " 
				+ FriendsCircleTable.USERID + " text , " 
				+ FriendsCircleTable.ISZAN + " text ) ; ";
		db.execSQL(friendscircle);
		
		String myDynamic = "create table if not exists "
				+ MyDynamicTable.TABLE_NAME
				+ " ( _id integer primary key autoincrement , "
				+ MyDynamicTable.NAME + " text , " 
				+ MyDynamicTable.HEADER + " text , " 
				+ MyDynamicTable.IMGURL + " text , "
				+ MyDynamicTable.TIME + " text , " 
				+ MyDynamicTable.CONTENT + " text , " 
				+ MyDynamicTable.COMMENTCOUNT + " text , " 
				+ MyDynamicTable.ZANCOUNT + " text , " 
				+ MyDynamicTable.ID + " text , " 
				+ MyDynamicTable.USERID + " text , " 
				+ MyDynamicTable.ISZAN + " text , "
				+ MyDynamicTable.DAY + " text , "
				+ MyDynamicTable.MONTH + " text ) ; ";
		db.execSQL(myDynamic);
		String comment = "create table if not exists "
				+ FriendsCommentDB.TABLE_NAME
				+ " ( _id integer primary key autoincrement , "
				+ FriendsCommentDB.NAME + " text , " 
				+ FriendsCommentDB.IMGURL + " text , "
				+ FriendsCommentDB.TIME + " text , " 
				+ FriendsCommentDB.CONTENT + " text , " 
				+ FriendsCommentDB.ID + " text , " 
				+ FriendsCommentDB.AUTHORITY + " text ) ; ";
		db.execSQL(comment);
	}
	
	public void insertPosts(JSONArray posts) {
		//sqLiteDatabase.delete(FriendsCircleTable.TABLE_NAME, null, null);
		int insertCount = 0;
		try {
			for (int i = 0; i < posts.length(); i++) {
			
				JSONObject obj = posts.getJSONObject(i);
				String commentCount = obj.getString("commentCount");
				String content =obj.getString("content");
				String header =obj.getString("header");
				String imgUrl =obj.getString("imgUrl");
				String name =obj.getString("name");
				String time =obj.getString("time");
				String zanCount =obj.getString("zanCount");
				String isZan =obj.getString("isZan");
				String id = obj.getString("id");
				String userId = obj.getString("userId");
				ContentValues contentValues = new ContentValues();
				contentValues.put(FriendsCircleTable.COMMENTCOUNT, commentCount);
				contentValues.put(FriendsCircleTable.CONTENT, content);
				contentValues.put(FriendsCircleTable.HEADER, header);
				contentValues.put(FriendsCircleTable.IMGURL, imgUrl);
				contentValues.put(FriendsCircleTable.NAME, name);
				contentValues.put(FriendsCircleTable.TIME, time);
				contentValues.put(FriendsCircleTable.ZANCOUNT, zanCount);
				contentValues.put(FriendsCircleTable.ISZAN, isZan);
				contentValues.put(FriendsCircleTable.ID, id);
				contentValues.put(FriendsCircleTable.USERID, userId);
				
				Cursor cursor = sqLiteDatabase.query(FriendsCircleTable.TABLE_NAME, null, FriendsCircleTable.ID + " =? ",
								new String[] { id }, null, null, null);
				if (cursor != null && cursor.getCount() == 1) {
					FriendsCircle model = new FriendsCircle();
					setModelValue(model, cursor, 0);
					sqLiteDatabase.update(FriendsCircleTable.TABLE_NAME, contentValues,
							FriendsCircleTable.ID + "=?", new String[] { id });
				} else {
					sqLiteDatabase.insert(FriendsCircleTable.TABLE_NAME, null, contentValues);
					insertCount++;
				}
				if (cursor != null && cursor.isClosed() == false) {
					cursor.close();
				}
			}
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	
	public void insertDynamicPosts(JSONArray posts) {
		//sqLiteDatabase.delete(FriendsCircleTable.TABLE_NAME, null, null);
		int insertCount = 0;
		try {
			for (int i = 0; i < posts.length(); i++) {
			
				JSONObject obj = posts.getJSONObject(i);
				String commentCount = obj.getString("commentCount");
				String content =obj.getString("content");
				String header =obj.getString("header");
				String imgUrl =obj.getString("imgUrl");
				String name =obj.getString("name");
				String time =obj.getString("time");
				String zanCount =obj.getString("zanCount");
				String isZan =obj.getString("isZan");
				String id = obj.getString("id");
				String userId = obj.getString("userId");
				String day = obj.getString("day");
				String month = obj.getString("month");
				ContentValues contentValues = new ContentValues();
				contentValues.put(MyDynamicTable.COMMENTCOUNT, commentCount);
				contentValues.put(MyDynamicTable.CONTENT, content);
				contentValues.put(MyDynamicTable.HEADER, header);
				contentValues.put(MyDynamicTable.IMGURL, imgUrl);
				contentValues.put(MyDynamicTable.NAME, name);
				contentValues.put(MyDynamicTable.TIME, time);
				contentValues.put(MyDynamicTable.ZANCOUNT, zanCount);
				contentValues.put(MyDynamicTable.ISZAN, isZan);
				contentValues.put(MyDynamicTable.ID, id);
				contentValues.put(MyDynamicTable.USERID, userId);
				contentValues.put(MyDynamicTable.DAY, day);
				contentValues.put(MyDynamicTable.MONTH, month);

				
				Cursor cursor = sqLiteDatabase.query(MyDynamicTable.TABLE_NAME, null, MyDynamicTable.ID + " =? ",
								new String[] { id }, null, null, null);
				if (cursor != null && cursor.getCount() == 1) {
					MyDynamic model = new MyDynamic();
					setmyDynamicModelValue(model, cursor, 0);
					sqLiteDatabase.update(MyDynamicTable.TABLE_NAME, contentValues,
							MyDynamicTable.ID + "=?", new String[] { id });
				} else {
					sqLiteDatabase.insert(MyDynamicTable.TABLE_NAME, null, contentValues);
					insertCount++;
				}
				if (cursor != null && cursor.isClosed() == false) {
					cursor.close();
				}
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} 
	
	
	public void insertComment(JSONArray posts){
		int insertCount = 0;
		try {
			for (int i = 0; i < posts.length(); i++) {
				System.out.println("posts.length:"+posts.length());
				JSONObject obj = posts.getJSONObject(i);
				String content = obj.getString("content");
				String picture = obj.getString("picture");
				String nickName = obj.getString("nickName");
				String time = obj.getString("time");
				String authority = obj.getString("authority");
				String userID = obj.getString("userID");
				String id = obj.getString("id");
				ContentValues contentValues = new ContentValues();
				contentValues.put(FriendsCommentDB.ID, userID);
				contentValues.put(FriendsCommentDB.AUTHORITY, authority);
				contentValues.put(FriendsCommentDB.IMGURL, picture);
				contentValues.put(FriendsCommentDB.NAME, nickName);
				contentValues.put(FriendsCommentDB.TIME, time);
				contentValues.put(FriendsCommentDB.CONTENT, content);
				contentValues.put(FriendsCommentDB.COMMENTID, id);
				Cursor cursor = sqLiteDatabase.query(FriendsCommentDB.TABLE_NAME, null, FriendsCommentDB.COMMENTID + " =? ",
								new String[] { id }, null, null, null);
				if (cursor != null && cursor.getCount() == 1) {
					Comment model = new Comment();
					setCommentModelValue(model, cursor, 0);
					sqLiteDatabase.update(FriendsCommentDB.TABLE_NAME, contentValues,
							FriendsCommentDB.COMMENTID + "=?", new String[] { id });
				} else {
					sqLiteDatabase.insert(FriendsCommentDB.TABLE_NAME, null, contentValues);
					insertCount++;
				}
				if (cursor != null && cursor.isClosed() == false) {
					cursor.close();
				}
			}
			System.out.println("insertCount:"+ insertCount); //1
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
	public List<FriendsCircle> queryFriendsCircle(String limit){
		List<FriendsCircle> list = new ArrayList<FriendsCircle>();
		Cursor cursor = sqLiteDatabase.query(FriendsCircleTable.TABLE_NAME, null,
				null, null, null,
				null, null, limit);
		
//		while(cursor.moveToNext()){
//			
//			FriendsCircle fc = new FriendsCircle();
//			fc.setCommentCount(cursor.getString(cursor.getColumnIndex(FriendsCircleTable.COMMENTCOUNT)));
//			fc.setContent(cursor.getString(cursor.getColumnIndex(FriendsCircleTable.CONTENT)));
//			fc.setHeader(cursor.getString(cursor.getColumnIndex(FriendsCircleTable.HEADER)));
//			fc.setId(cursor.getString(cursor.getColumnIndex(FriendsCircleTable.ID)));
//			fc.setImgUrl(cursor.getString(cursor.getColumnIndex(FriendsCircleTable.IMGURL)));
//			fc.setIsZan(cursor.getString(cursor.getColumnIndex(FriendsCircleTable.ISZAN)));
//			fc.setName(cursor.getString(cursor.getColumnIndex(FriendsCircleTable.NAME)));
//			fc.setTime(cursor.getString(cursor.getColumnIndex(FriendsCircleTable.TIME)));
//			fc.setUserId(cursor.getString(cursor.getColumnIndex(FriendsCircleTable.USERID)));
//			fc.setZanCount(cursor.getString(cursor.getColumnIndex(FriendsCircleTable.ZANCOUNT)));
//			list.add(fc);
//		}
		
		for (int inner = 0; cursor != null && inner < cursor.getCount(); inner++) {
			FriendsCircle model = new FriendsCircle();
			setModelValue(model, cursor, inner);
			list.add(model);
		}
		if (cursor != null && cursor.isClosed() == false) {
			cursor.close();
		}
		if (cursor != null && cursor.isClosed() == false) {
			cursor.close();
		}
		return list;
	}
	
	
	public List<MyDynamic> queryMyDynamic(String limit){
		List<MyDynamic> list = new ArrayList<MyDynamic>();
		Cursor cursor = sqLiteDatabase.query(MyDynamicTable.TABLE_NAME, null,
				null, null, null,
				null, null, limit);
		
		for (int inner = 0; cursor != null && inner < cursor.getCount(); inner++) {
			MyDynamic model = new MyDynamic();
			setmyDynamicModelValue(model, cursor, inner);
			list.add(model);
		}
		if (cursor != null && cursor.isClosed() == false) {
			cursor.close();
		}
		if (cursor != null && cursor.isClosed() == false) {
			cursor.close();
		}
		return list;
	}
	
	public List<Comment> queryCommentDynamic(String limit){
		List<Comment> list = new ArrayList<Comment>();
		Cursor cursor = sqLiteDatabase.query(FriendsCommentDB.TABLE_NAME, null,
				null, null, null,
				null, null, limit);
		
		for (int inner = 0; cursor != null && inner < cursor.getCount(); inner++) {
			Comment model = new Comment();
			setCommentModelValue(model, cursor, inner);
			list.add(model);
		}
		if (cursor != null && cursor.isClosed() == false) {
			cursor.close();
		}
		if (cursor != null && cursor.isClosed() == false) {
			cursor.close();
		}
		return list;
	}
	
	
	public void setmyDynamicModelValue(MyDynamic model, Cursor cursor, int cursorPosition) {
		cursor.moveToPosition(cursorPosition);
		int column = 1;
		model.setName(cursor.getString(column++));
		model.setHeader(cursor.getString(column++));
		model.setImgUrl(cursor.getString(column++));
		model.setTime(cursor.getString(column++));
		model.setContent(cursor.getString(column++));
		model.setCommentCount(cursor.getString(column++));
		model.setZanCount(cursor.getString(column++));
		model.setId(cursor.getString(column++));
		model.setUserId(cursor.getString(column++));
		model.setIsZan(cursor.getString(column++));
		model.setDay(cursor.getString(column++));
		model.setMonth(cursor.getString(column++));
	}
	
	public void setCommentModelValue(Comment model, Cursor cursor, int cursorPosition) {
		cursor.moveToPosition(cursorPosition);
		int column = 1;
		model.setName(cursor.getString(column++));
		model.setImage_url(cursor.getString(column++));
		model.setTime(cursor.getString(column++));
		model.setContent(cursor.getString(column++));
		model.setCode(cursor.getString(column++));
		model.setAuthority(cursor.getString(column++));
		model.setId(cursor.getString(column++));
	}
	
	public void setModelValue(FriendsCircle model, Cursor cursor, int cursorPosition) {
		cursor.moveToPosition(cursorPosition);
		int column = 1;
		model.setName(cursor.getString(column++));
		model.setHeader(cursor.getString(column++));
		model.setImgUrl(cursor.getString(column++));
		model.setTime(cursor.getString(column++));
		model.setContent(cursor.getString(column++));
		model.setCommentCount(cursor.getString(column++));
		model.setZanCount(cursor.getString(column++));
		model.setId(cursor.getString(column++));
		model.setUserId(cursor.getString(column++));
		model.setIsZan(cursor.getString(column++));
	}
	
	/**
	 * 清空某张表内数据
	 */
	public void deleteAllDataFromTable(String Tablename){
		sqLiteDatabase.execSQL("DELETE FROM " + Tablename);
		
	}
}
