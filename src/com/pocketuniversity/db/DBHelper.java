package com.pocketuniversity.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pocketuniversity.bean.CampaignCategoryModel;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.bean.CategoryModel;
import com.pocketuniversity.bean.ClassInfo;
import com.pocketuniversity.bean.MyCampaignPostModel;
import com.pocketuniversity.bean.PostModel;

public class DBHelper extends SQLiteOpenHelper {

	private static SQLiteDatabase mSqLiteDatabase;
	private static final String DB_NAME = "schedule.db";
	private static final int VERSION = 1;
	public static final String DROP_USER_TABLE = "drop table ";

	String[] columns = { Schedule.CLASSNAME, Schedule.CLASSNUMLEN,
			Schedule.CLASSROOM, Schedule.FROMCLASSNAME, Schedule.WEEKDAY };

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
		mSqLiteDatabase = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String create_schedule = "create table if not exists "
				+ Schedule.TABLE_NAME
				+ " ( _id integer primary key autoincrement , "
				+ Schedule.CLASSNAME + " text , " 
				+ Schedule.CLASSNUMLEN + " text , " 
				+ Schedule.CLASSROOM + " text , "
				+ Schedule.FROMCLASSNAME + " text , " 
				+ Schedule.WEEKDAY + " text ) ; ";
		db.execSQL(create_schedule);
		
		String create_category = "create table if not exists " 
				+ Category.tableName 
				+  "  ( _id integer primary key autoincrement , " 
				+ Category.id + "  text , "
				+ Category.title + " text , " 
				+ Category.description + " text ) ;";
		
		db.execSQL(create_category);
		
		String post = "create table if not exists " 
				+ Post.tableName
				+ "(  _id integer primary key autoincrement , " 
				+ Post.id + " text," 
				+ Post.title + " text," 
				+ Post.content + " text," 
				+ Post.imageurl + " text,"
				+ Post.source + " text," 
				+ Post.time + " text,"
				+ Post.comment_count + " text," 
				+ Post.view_count + " text,"
				+ Post.collection_count + " text," 
				+ Post.channel_id + " text,"
				+ Post.collection + " text)";
		db.execSQL(post);
		
		String create_campaign_category = "create table if not exists " 
				+ CampaignCategory.tableName 
				+  "  ( _id integer primary key autoincrement , " 
				+ CampaignCategory.id + "  text , "
				+ CampaignCategory.title + " text , " 
				+ CampaignCategory.description + " text ) ;";
		db.execSQL(create_campaign_category);
		
		String create_campaign_post = "create table if not exists " 
				+ CampaignPost.tableName
				+ "(  _id integer primary key autoincrement , " 
				+ CampaignPost.id + " text," 
				+ CampaignPost.title + " text," 
				+ CampaignPost.content + " text,"
				+ CampaignPost.place + " text, "
				+ CampaignPost.imageurl + " text,"
				+ CampaignPost.source + " text," 
				+ CampaignPost.time + " text,"
				+ CampaignPost.comment_count + " text," 
				+ CampaignPost.view_count + " text,"
				+ CampaignPost.collection_count + " text," 
				+ CampaignPost.channel_id + " text,"
				+ CampaignPost.collection + " text,"
				+ CampaignPost.join + " text,"
				+ CampaignPost.hasjoin + " text )";
		db.execSQL(create_campaign_post);
		
		String create_my_campaign_post = "create table if not exists " 
				+ MyCampaignPost.tableName
				+ "(  _id integer primary key autoincrement , " 
				+ MyCampaignPost.id + " text," 
				+ MyCampaignPost.title + " text," 
				+ MyCampaignPost.content + " text," 
				+ MyCampaignPost.imageurl + " text,"
				+ MyCampaignPost.source + " text," 
				+ MyCampaignPost.time + " text,"
				+ MyCampaignPost.comment_count + " text," 
				+ MyCampaignPost.view_count + " text,"
				+ MyCampaignPost.collection_count + " text," 
				+ MyCampaignPost.collection + " text, "
				+ MyCampaignPost.join + " text,"
				+ MyCampaignPost.place + " text, "
				+ MyCampaignPost.hasjoin+" text )";
		db.execSQL(create_my_campaign_post);
		
		
		String create_firstThree_news = "create table if not exists " 
				+ FirstThreePost.tableName
				+ "(  _id integer primary key autoincrement , " 
				+ FirstThreePost.id + " text," 
				+ FirstThreePost.title + " text," 
				+ FirstThreePost.content + " text," 
				+ FirstThreePost.imageurl + " text,"
				+ FirstThreePost.source + " text," 
				+ FirstThreePost.time + " text,"
				+ FirstThreePost.comment_count + " text," 
				+ FirstThreePost.view_count + " text,"
				+ FirstThreePost.collection_count + " text," 
				+ FirstThreePost.channel_id + " text,"
				+ FirstThreePost.collection + " text)";
		db.execSQL(create_firstThree_news);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String deleteschTable = "DROP TABLE " + Schedule.TABLE_NAME;
		db.execSQL(deleteschTable);
		
		String deletecateTable = "DROP TABLE " + Category.tableName;
		db.execSQL(deletecateTable);
		

		String deletepostTable = "DROP TABLE " + Post.tableName;
		db.execSQL(deletepostTable);
		String create_schedule = "create table if not exists "
				+ Schedule.TABLE_NAME
				+ " ( _id integer primary key autoincrement , "
				+ Schedule.CLASSNAME + " text , " 
				+ Schedule.CLASSNUMLEN + " text , " 
				+ Schedule.CLASSROOM + " text , "
				+ Schedule.FROMCLASSNAME + " text , " 
				+ Schedule.WEEKDAY + " text ) ; ";
		db.execSQL(create_schedule);
		

		String create_category = "create table if not exists " 
				+ Category.tableName 
				+  "  ( _id Integer primary key autoincrement , " 
				+ Category.id + "  text , "
				+ Category.title + " text , " 
				+ Category.description + " text ) ;";
		
		db.execSQL(create_category);
		
		String post = "create table if not exists " 
				+ Post.tableName
				+ "(  _id integer primary key autoincrement , " 
				+ Post.id + " text," 
				+ Post.title + " text," 
				+ Post.content + " text," 
				+ Post.imageurl + " text,"
				+ Post.source + " text," 
				+ Post.time + " text,"
				+ Post.comment_count + " text," 
				+ Post.view_count + " text,"
				+ Post.collection_count + " text," 
				+ Post.channel_id + " text,"
				+ Post.collection + " text)";
		db.execSQL(post);
		
		String create_campaign_category = "create table if not exists " 
				+ CampaignCategory.tableName 
				+  "  ( _id integer primary key autoincrement , " 
				+ CampaignCategory.id + "  text , "
				+ CampaignCategory.title + " text , " 
				+ CampaignCategory.description + " text ) ;";
		db.execSQL(create_campaign_category);
		
		String create_campaign_post = "create table if not exists " 
				+ CampaignPost.tableName
				+ "(  _id integer primary key autoincrement , " 
				+ CampaignPost.id + " text," 
				+ CampaignPost.title + " text," 
				+ CampaignPost.content + " text," 
				+ CampaignPost.imageurl + " text,"
				+ CampaignPost.source + " text," 
				+ CampaignPost.time + " text,"
				+ CampaignPost.comment_count + " text," 
				+ CampaignPost.view_count + " text,"
				+ CampaignPost.collection_count + " text," 
				+ CampaignPost.channel_id + " text,"
				+ CampaignPost.collection + " text, "
				+ CampaignPost.join + " text , "
				+ CampaignPost.hasjoin+" text )";
		db.execSQL(create_campaign_post);
		
		String create_firstThree_news = "create table if not exists " 
				+ FirstThreePost.tableName
				+ "(  _id integer primary key autoincrement , " 
				+ FirstThreePost.id + " text," 
				+ FirstThreePost.title + " text," 
				+ FirstThreePost.content + " text," 
				+ FirstThreePost.imageurl + " text,"
				+ FirstThreePost.source + " text," 
				+ FirstThreePost.time + " text,"
				+ FirstThreePost.comment_count + " text," 
				+ FirstThreePost.view_count + " text,"
				+ FirstThreePost.collection_count + " text," 
				+ FirstThreePost.channel_id + " text,"
				+ FirstThreePost.collection + " text)";
		db.execSQL(create_firstThree_news);
	}

	/**
	 * 初始化数据库
	 */
	public SQLiteDatabase openSqLiteDatabase() {
		if (mSqLiteDatabase == null) {
			mSqLiteDatabase = getWritableDatabase();
		}
		return mSqLiteDatabase;
	}

	public void closeSqLiteDatabase() {
		if (mSqLiteDatabase != null && mSqLiteDatabase.isOpen()) {
			mSqLiteDatabase.close();
		}
	}

	public void releaseSqliteDatabase() {
		if (mSqLiteDatabase != null) {
			mSqLiteDatabase.close();
		}
		mSqLiteDatabase = null;
	}

	
	/**
	 * 插入Category表
	 */
	public void insertCategories(JSONArray categories) {
		mSqLiteDatabase.delete(Category.tableName, null, null);
		for (int j = 0; j < categories.length() && categories != null; j++) {
			ContentValues contentValues = new ContentValues();
			try {
				JSONObject categoryObj = categories.getJSONObject(j);
				String description = categoryObj.getString("description");
				String id = categoryObj.getString("categoryid");
				String title = categoryObj.getString("categoryName");
				contentValues.put(Category.description, description);
				contentValues.put(Category.id, id);
				contentValues.put(Category.title, title);
				
					mSqLiteDatabase.insert(Category.tableName, null,
							contentValues);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 插入CampaignCategory表
	 * @return
	 */
	public void insertCampaignCategories(JSONArray categories) {
		mSqLiteDatabase.delete(CampaignCategory.tableName, null, null);
		for (int j = 0; j < categories.length() && categories != null; j++) {
			ContentValues contentValues = new ContentValues();
			try {
				JSONObject categoryObj = categories.getJSONObject(j);
				String description = categoryObj.getString("description");
				String id = categoryObj.getString("categoryid");
				String title = categoryObj.getString("categoryName");
				contentValues.put(Category.description, description);
				contentValues.put(Category.id, id);
				contentValues.put(Category.title, title);
				
					mSqLiteDatabase.insert(CampaignCategory.tableName, null,
							contentValues);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public List<CategoryModel> queryAllCategory() {
		List<CategoryModel> list = new ArrayList<CategoryModel>();
		Cursor cursor = mSqLiteDatabase.query(Category.tableName, null, null,
				null, null, null, null);
		for (int i = 0; cursor != null && i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			CategoryModel categoryModel = new CategoryModel();
			categoryModel.setDescription(cursor.getString(3));
			categoryModel.setId(cursor.getString(1));
			categoryModel.setTitle(cursor.getString(2));
			list.add(categoryModel);
		}
		cursor.close();
		return list;
	}
	
	/**
	 * queryAllCampaignCategory
	 */
	public List<CampaignCategoryModel> queryAllCampaignCategory() {
		List<CampaignCategoryModel> list = new ArrayList<CampaignCategoryModel>();
		Cursor cursor = mSqLiteDatabase.query(CampaignCategory.tableName, null, null,
				null, null, null, null);
		for (int i = 0; cursor != null && i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			CampaignCategoryModel categoryModel = new CampaignCategoryModel();
			categoryModel.setDescription(cursor.getString(3));
			categoryModel.setId(cursor.getString(1));
			categoryModel.setTitle(cursor.getString(2));
			list.add(categoryModel);
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 获取最后一条数据
	 * @return
	 */
	public List<CategoryModel> queryLastCategory() {
		List<CategoryModel> list = new ArrayList<CategoryModel>();
		Cursor cursor = null ;
		cursor = mSqLiteDatabase.rawQuery("select * from "
				+ Category.tableName, null);
		if(cursor.moveToLast()){
			CategoryModel categoryModel = new CategoryModel();
			categoryModel.setDescription(cursor.getString(3));
			categoryModel.setId(cursor.getString(1));
			categoryModel.setTitle(cursor.getString(2));
			list.add(categoryModel);
		}
		
		cursor.close();
		return list;
	}
	
	
	/**
	 * 清空某张表内数据
	 */
	public void deleteAllDataFromTable(String Tablename){
		mSqLiteDatabase.execSQL("DELETE FROM " + Tablename);
		
	}
	
	/**
	 * 插入Schedule表
	 */
	public long insertSchedule(ClassInfo classInfo) {

		ContentValues values = new ContentValues();
		values.put(Schedule.CLASSNAME, classInfo.getClassname());
		values.put(Schedule.CLASSNUMLEN, classInfo.getClassNumLen());
		values.put(Schedule.CLASSROOM, classInfo.getClassRoom());
		values.put(Schedule.FROMCLASSNAME, classInfo.getFromClassNum());
		values.put(Schedule.WEEKDAY, classInfo.getWeekday());

		long row = mSqLiteDatabase.insert(Schedule.TABLE_NAME, " ", values);

		return row;

	}

	/**
	 * 获取表中最后一条数据
	 */
	public Cursor queryLastData() {
		Cursor cursor = null;
		cursor = mSqLiteDatabase.rawQuery("select * from "
				+ Schedule.TABLE_NAME, null);
		if (cursor.moveToLast()) {
			return cursor;
		}
		return null;
	}

	/**
	 * 获取整张表数据
	 */
	public ArrayList<ClassInfo> getAllData() {
		ArrayList<ClassInfo> classList = null;
		ClassInfo classInfo = null;
		Cursor cursor = mSqLiteDatabase.query(Schedule.TABLE_NAME, columns,
				null, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			classList = new ArrayList<ClassInfo>(cursor.getCount()); // 开辟对应的空间数
			while (cursor.moveToNext()) {
				classInfo = new ClassInfo();
				classInfo.setClassname(cursor.getString(cursor
						.getColumnIndex(Schedule.CLASSNAME)));
				classInfo.setClassNumLen(cursor.getString(cursor
						.getColumnIndex(Schedule.CLASSNUMLEN)));
				classInfo.setClassRoom(cursor.getString(cursor
						.getColumnIndex(Schedule.CLASSROOM)));
				classInfo.setFromClassNum(cursor.getString(cursor
						.getColumnIndex(Schedule.FROMCLASSNAME)));
				classInfo.setWeekday(cursor.getString(cursor
						.getColumnIndex(Schedule.WEEKDAY)));
				classList.add(classInfo);
			}

		}
		cursor.close();
		return classList;
	}
	
	public int insertPosts(JSONArray posts, String channel_id) {
		int insertCount = 0;
		try {
			for (int i = 0; i < posts.length(); i++) {
				JSONObject obj = posts.getJSONObject(i);

				String id = obj.getString("newsID");
				String source = obj.getString("source");
				String title = obj.getString("title");
				String content = obj.getString("description");
				String imgurl = obj.getString("imgUrl");
				String time = obj.getString("time");
				String comment_count = obj.getString("commentCount");
				String view_count = obj.getString("viewCount");
				//String collection_count = obj.getString("collectionCount");
				String iscollection = obj.getString("collection");
				
				ContentValues contentValues = new ContentValues();
			//	contentValues.put(Post.collection_count, collection_count);
				contentValues.put(Post.view_count, view_count);
				contentValues.put(Post.comment_count, comment_count);
				contentValues.put(Post.content, content);
				contentValues.put(Post.source, source);
				contentValues.put(Post.time, time);
				contentValues.put(Post.id, id);
				contentValues.put(Post.title, title);
				contentValues.put(Post.imageurl, imgurl);
				contentValues.put(Post.channel_id, channel_id);
				contentValues.put(Post.collection, iscollection);

				Cursor cursor = mSqLiteDatabase
						.query(Post.tableName, null, Post.id + " =? ",
								new String[] { id }, null, null, null);
				if (cursor != null && cursor.getCount() == 1) {
					PostModel model = new PostModel();
					setModelValue(model, cursor, 0);
					mSqLiteDatabase.update(Post.tableName, contentValues,
							Post.id + "=?", new String[] { id });
				} else {
					mSqLiteDatabase.insert(Post.tableName, null, contentValues);
					insertCount++;
				}
				if (cursor != null && cursor.isClosed() == false) {
					cursor.close();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return insertCount;
	}

	
	/**
	 * 插入CampaignPost表
	 * @param model
	 * @param cursor
	 * @param cursorPosition
	 */
	public int insertCampaignPosts(JSONArray posts, String channel_id) {
		int insertCount = 0;
		try {
			for (int i = 0; i < posts.length(); i++) {
				JSONObject obj = posts.getJSONObject(i);

				String id = obj.getString("newsID");
				String source = obj.getString("source");
				String title = obj.getString("title");
				//String content = obj.getString("description");
				String place = obj.getString("place");
				String content = obj.getString("betime");
				String imgurl = obj.getString("imgUrl");
				String time = obj.getString("time");
				String comment_count = obj.getString("commentCount");
				String view_count = obj.getString("viewCount");
				//String collection_count = obj.getString("collectionCount");
				String iscollection = obj.getString("collection");
				String isJoin = obj.getString("isJoin");
				String hasJoin = obj.getString("attendCount");
				System.out.println("hasJoin:" + hasJoin);
				System.out.println("isJoin:" + isJoin);
				System.out.println("place: "+ place);
				
				ContentValues contentValues = new ContentValues();
				//contentValues.put(CampaignPost.collection_count, collection_count);
				contentValues.put(CampaignPost.view_count, view_count);
				contentValues.put(CampaignPost.comment_count, comment_count);
				contentValues.put(CampaignPost.content, content);
				contentValues.put(CampaignPost.source, source);
				contentValues.put(CampaignPost.time, time);
				contentValues.put(CampaignPost.id, id);
				contentValues.put(CampaignPost.title, title);
				contentValues.put(CampaignPost.imageurl, imgurl);
				contentValues.put(CampaignPost.channel_id, channel_id);
				contentValues.put(CampaignPost.collection, iscollection);
				contentValues.put(CampaignPost.join,isJoin );
				contentValues.put(CampaignPost.hasjoin, hasJoin);
				contentValues.put(CampaignPost.place, place);
				Cursor cursor = mSqLiteDatabase
						.query(CampaignPost.tableName, null, CampaignPost.id + " =? ",
								new String[] { id }, null, null, null);
				if (cursor != null && cursor.getCount() == 1) {
					CampaignPostModel model = new CampaignPostModel();
					setModelCampaignValue(model, cursor, 0);
					mSqLiteDatabase.update(CampaignPost.tableName, contentValues,
							CampaignPost.id + "=?", new String[] { id });
				} else {
					mSqLiteDatabase.insert(CampaignPost.tableName, null, contentValues);
					insertCount++;
				}
				if (cursor != null && cursor.isClosed() == false) {
					cursor.close();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return insertCount;
	}

	
	/**
	 * 插入MyCampaignPost表
	 * @param model
	 * @param cursor
	 * @param cursorPosition
	 */
	public int insertMyCampaignPosts(JSONArray posts) {
		int insertCount = 0;
		try {
			for (int i = 0; i < posts.length(); i++) {
				JSONObject obj = posts.getJSONObject(i);

				String id = obj.getString("newsID");
				String source = obj.getString("source");
				String title = obj.getString("title");
				String content = obj.getString("description");
				String imgurl = obj.getString("imgUrl");
				String time = obj.getString("time");
				String comment_count = obj.getString("commentCount");
				String view_count = obj.getString("viewCount");
				String collection_count = obj.getString("collectionCount");
				String iscollection = obj.getString("collection");
				String isJoin = obj.getString("isJoin");
				String hasJoin = obj.getString("hasJoin");
				
				ContentValues contentValues = new ContentValues();
				contentValues.put(MyCampaignPost.collection_count, collection_count);
				contentValues.put(MyCampaignPost.view_count, view_count);
				contentValues.put(MyCampaignPost.comment_count, comment_count);
				contentValues.put(MyCampaignPost.content, content);
				contentValues.put(MyCampaignPost.source, source);
				contentValues.put(MyCampaignPost.time, time);
				contentValues.put(MyCampaignPost.id, id);
				contentValues.put(MyCampaignPost.title, title);
				contentValues.put(MyCampaignPost.imageurl, imgurl);
				contentValues.put(MyCampaignPost.collection, iscollection);
				contentValues.put(MyCampaignPost.join,isJoin );
				contentValues.put(MyCampaignPost.hasjoin, hasJoin);
				Cursor cursor = mSqLiteDatabase
						.query(MyCampaignPost.tableName, null, MyCampaignPost.id + " =? ",
								new String[] { id }, null, null, null);
				if (cursor != null && cursor.getCount() == 1) {
					MyCampaignPostModel model = new MyCampaignPostModel();
					setModelMyCampaignValue(model, cursor, 0);
					mSqLiteDatabase.update(MyCampaignPost.tableName, contentValues,
							MyCampaignPost.id + "=?", new String[] { id });
				} else {
					mSqLiteDatabase.insert(MyCampaignPost.tableName, null, contentValues);
					insertCount++;
				}
				if (cursor != null && cursor.isClosed() == false) {
					cursor.close();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return insertCount;
	}

	
	
	
	public void setModelValue(PostModel model, Cursor cursor, int cursorPosition) {
		cursor.moveToPosition(cursorPosition);
		int column = 1;
		model.setId(cursor.getString(column++));
		model.setTitle(cursor.getString(column++));
		model.setContent(cursor.getString(column++));
		model.setImageurl(cursor.getString(column++));
		model.setSource(cursor.getString(column++));
		model.setTime(cursor.getString(column++));
		model.setComment_count(cursor.getString(column++));
		model.setView_count(cursor.getString(column++));
		model.setCollection_count(cursor.getString(column++));
		model.setChannel_id(cursor.getString(column++));
		model.setCollection(cursor.getString(column++));
	}
	
	public void setModelCampaignValue(CampaignPostModel model, Cursor cursor, int cursorPosition) {
		cursor.moveToPosition(cursorPosition);
		int column = 1;
		model.setId(cursor.getString(column++));
		model.setTitle(cursor.getString(column++));
		model.setContent(cursor.getString(column++));
		model.setPlace(cursor.getString(column++));
		model.setImageurl(cursor.getString(column++));
		model.setSource(cursor.getString(column++));
		model.setTime(cursor.getString(column++));
		model.setComment_count(cursor.getString(column++));
		model.setView_count(cursor.getString(column++));
		model.setCollection_count(cursor.getString(column++));
		model.setChannel_id(cursor.getString(column++));
		model.setCollection(cursor.getString(column++));
		model.setJoin(cursor.getString(column++));
		model.setHasJoin(cursor.getString(column++));
	}
	
	public void setModelMyCampaignValue(MyCampaignPostModel model, Cursor cursor, int cursorPosition) {
		cursor.moveToPosition(cursorPosition);
		int column = 1;
		model.setId(cursor.getString(column++));
		model.setTitle(cursor.getString(column++));
		model.setContent(cursor.getString(column++));
		model.setImageurl(cursor.getString(column++));
		model.setSource(cursor.getString(column++));
		model.setTime(cursor.getString(column++));
		model.setComment_count(cursor.getString(column++));
		model.setView_count(cursor.getString(column++));
		model.setCollection_count(cursor.getString(column++));
		model.setCollection(cursor.getString(column++));
		model.setJoin(cursor.getString(column++));
		model.setHasJoin(cursor.getString(column++));
	}
	
	public List<PostModel> queryPostsByCategoryId(String categoryId,
			String limit) {
		List<PostModel> list = new ArrayList<PostModel>();

		Cursor postCursor = mSqLiteDatabase.query(Post.tableName, null,
				Post.channel_id + " =? ", new String[] { categoryId }, null,
				null, null, limit);

		for (int inner = 0; postCursor != null && inner < postCursor.getCount(); inner++) {
			PostModel model = new PostModel();
			setModelValue(model, postCursor, inner);
			list.add(model);
		}
		if (postCursor != null && postCursor.isClosed() == false) {
			postCursor.close();
		}
		if (postCursor != null && postCursor.isClosed() == false) {
			postCursor.close();
		}
		return list;
	}

	public List<CampaignPostModel> queryCampaignPostsByCategoryId(String categoryId,
			String limit) {
		List<CampaignPostModel> list = new ArrayList<CampaignPostModel>();

		Cursor postCursor = mSqLiteDatabase.query(CampaignPost.tableName, null,
				CampaignPost.channel_id + " =? ", new String[] { categoryId }, null,
				null, null, limit);

		for (int inner = 0; postCursor != null && inner < postCursor.getCount(); inner++) {
			CampaignPostModel model = new CampaignPostModel();
			setModelCampaignValue(model, postCursor, inner);
			list.add(model);
		}
		if (postCursor != null && postCursor.isClosed() == false) {
			postCursor.close();
		}
		if (postCursor != null && postCursor.isClosed() == false) {
			postCursor.close();
		}
		return list;
	}

	
	public List<MyCampaignPostModel> queryMyCampaignPostsByCategoryId() {
		List<MyCampaignPostModel> list = new ArrayList<MyCampaignPostModel>();

		Cursor postCursor = mSqLiteDatabase.query(MyCampaignPost.tableName, null,
				null, null, null,
				null, null, null);

		for (int inner = 0; postCursor != null && inner < postCursor.getCount(); inner++) {
			MyCampaignPostModel model = new MyCampaignPostModel();
			setModelMyCampaignValue(model, postCursor, inner);
			list.add(model);
		}
		if (postCursor != null && postCursor.isClosed() == false) {
			postCursor.close();
		}
		if (postCursor != null && postCursor.isClosed() == false) {
			postCursor.close();
		}
		return list;
	}
	

	public int insertFirstThreePosts(JSONArray posts) {
		int insertCount = 0;
		try {
			for (int i = 0; i < posts.length(); i++) {
				JSONObject obj = posts.getJSONObject(i);

				String id = obj.getString("newsID");
				String source = obj.getString("source");
				String title = obj.getString("title");
				String content = obj.getString("description");
				String imgurl = obj.getString("imgUrl");
				String time = obj.getString("time");
				String comment_count = obj.getString("commentCount");
				String view_count = obj.getString("viewCount");
				//String collection_count = obj.getString("collectionCount");
				String iscollection = obj.getString("collection");
				
				ContentValues contentValues = new ContentValues();
			//	contentValues.put(Post.collection_count, collection_count);
				contentValues.put(Post.view_count, view_count);
				contentValues.put(Post.comment_count, comment_count);
				contentValues.put(Post.content, content);
				contentValues.put(Post.source, source);
				contentValues.put(Post.time, time);
				contentValues.put(Post.id, id);
				contentValues.put(Post.title, title);
				contentValues.put(Post.imageurl, imgurl);
				contentValues.put(Post.collection, iscollection);

				Cursor cursor = mSqLiteDatabase
						.query(FirstThreePost.tableName, null, FirstThreePost.id + " =? ",
								new String[] { id }, null, null, null);
				if (cursor != null && cursor.getCount() == 1) {
					PostModel model = new PostModel();
					setModelValue(model, cursor, 0);
					mSqLiteDatabase.update(FirstThreePost.tableName, contentValues,
							Post.id + "=?", new String[] { id });
				} else {
					mSqLiteDatabase.insert(FirstThreePost.tableName, null, contentValues);
					insertCount++;
				}
				if (cursor != null && cursor.isClosed() == false) {
					cursor.close();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return insertCount;
	}

	public List<PostModel> queryFirstThreePosts(
			String limit) {
		List<PostModel> list = new ArrayList<PostModel>();

		Cursor postCursor = mSqLiteDatabase.query(FirstThreePost.tableName, null,
				null, null, null,
				null, null, limit);

		for (int inner = 0; postCursor != null && inner < postCursor.getCount(); inner++) {
			PostModel model = new PostModel();
			setModelValue(model, postCursor, inner);
			list.add(model);
		}
		if (postCursor != null && postCursor.isClosed() == false) {
			postCursor.close();
		}
		if (postCursor != null && postCursor.isClosed() == false) {
			postCursor.close();
		}
		return list;
	}
	
}
