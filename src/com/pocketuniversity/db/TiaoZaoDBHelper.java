package com.pocketuniversity.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.bean.Tiaozao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TiaoZaoDBHelper extends SQLiteOpenHelper {

	private static SQLiteDatabase mSqLiteDatabase;
	private static final String DB_NAME = "tiaozao.db";
	private static final int VERSION = 1;
	public static final String DROP_USER_TABLE = "drop table ";

	public TiaoZaoDBHelper(Context context) {
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
		String create_Tiaozao = "create table if not exists "
				+ TiaoZao.TABLE_NAME
				+ "(  _id integer primary key autoincrement , "
				+ TiaoZao.USERID + " text," 
				+ TiaoZao.NICKNAME + " text,"
				+ TiaoZao.HEADER + " text," 
				+ TiaoZao.GOODSNAME + " text,"
				+ TiaoZao.VIEWCOUNT + " text," 
				+ TiaoZao.TIME + " text,"
				+ TiaoZao.PRICE + " text," 
				+ TiaoZao.GOODSID + " text, "
				+ TiaoZao.PHONE + " text, "
				+ TiaoZao.DETAIL + " text, "
				+ TiaoZao.COLLECTION + " text, "
				+ TiaoZao.IMGURL + " text )";
		db.execSQL(create_Tiaozao);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String deleteTable = "DROP TABLE " + TiaoZao.TABLE_NAME;
		db.execSQL(deleteTable);
		String create_Tiaozao = "create table if not exists "
				+ TiaoZao.TABLE_NAME
				+ "(  _id integer primary key autoincrement , "
				+ TiaoZao.USERID + " text," 
				+ TiaoZao.NICKNAME + " text,"
				+ TiaoZao.HEADER + " text," 
				+ TiaoZao.GOODSNAME + " text,"
				+ TiaoZao.VIEWCOUNT + " text," 
				+ TiaoZao.TIME + " text,"
				+ TiaoZao.PRICE + " text,"
				+ TiaoZao.GOODSID + " text, "
				+ TiaoZao.PHONE + " text, "
				+ TiaoZao.DETAIL + " text, "
				+ TiaoZao.COLLECTION + " text, "
				+ TiaoZao.IMGURL + " text )";
		db.execSQL(create_Tiaozao);
	}

	/**
	 * 插入跳蚤表
	 */
	public void insertTiaoZao(JSONArray tiaozao) {
		mSqLiteDatabase.delete(TiaoZao.TABLE_NAME, null, null);
		for (int j = 0; j < tiaozao.length() && tiaozao != null; j++) {
			ContentValues contentValues = new ContentValues();
			try {
				JSONObject tiaozaoObj = tiaozao.getJSONObject(j);
				String userId = tiaozaoObj.getString("studentID");
				String nickName = tiaozaoObj.getString("nickname");
				String header = tiaozaoObj.getString("picture");
				String goodsName = tiaozaoObj.getString("commodityName");
				String imgUrl = tiaozaoObj.getString("imgUrl");
				String price = tiaozaoObj.getString("price");
				String time = tiaozaoObj.getString("time");
				String viewCount = tiaozaoObj.getString("viewCount");
				String goodsId = tiaozaoObj.getString("commodityID");
				String phone = tiaozaoObj.getString("phone");
				String detail = tiaozaoObj.getString("detail");
				String collection = tiaozaoObj.getString("collection");
				contentValues.put(TiaoZao.USERID, userId);
				contentValues.put(TiaoZao.NICKNAME, nickName);
				contentValues.put(TiaoZao.HEADER, header);
				contentValues.put(TiaoZao.GOODSNAME, goodsName);
				contentValues.put(TiaoZao.IMGURL, imgUrl);
				contentValues.put(TiaoZao.PRICE, price);
				contentValues.put(TiaoZao.TIME, time);
				contentValues.put(TiaoZao.GOODSID, goodsId);
				contentValues.put(TiaoZao.VIEWCOUNT,viewCount);
				contentValues.put(TiaoZao.PHONE, phone);
				contentValues.put(TiaoZao.DETAIL, detail);
				contentValues.put(TiaoZao.COLLECTION, collection);
				mSqLiteDatabase.insert(TiaoZao.TABLE_NAME, null, contentValues);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public List<Tiaozao> getAllTiaozao(){
		List<Tiaozao> list = new ArrayList<Tiaozao>();
		Cursor cursor = mSqLiteDatabase.query(TiaoZao.TABLE_NAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			Tiaozao tiaozao = new Tiaozao();
			tiaozao.setGoodsname(cursor.getString(cursor.getColumnIndex(TiaoZao.GOODSNAME)));
			tiaozao.setHeader(cursor.getString(cursor.getColumnIndex(TiaoZao.HEADER)));
			tiaozao.setImgUrl(cursor.getString(cursor.getColumnIndex(TiaoZao.IMGURL)));
			tiaozao.setNickname(cursor.getString(cursor.getColumnIndex(TiaoZao.NICKNAME)));
			tiaozao.setPrice(cursor.getString(cursor.getColumnIndex(TiaoZao.PRICE)));
			tiaozao.setTime(cursor.getString(cursor.getColumnIndex(TiaoZao.TIME)));
			tiaozao.setUserId(cursor.getString(cursor.getColumnIndex(TiaoZao.USERID)));
			tiaozao.setView_count(cursor.getString(cursor.getColumnIndex(TiaoZao.VIEWCOUNT)));
			tiaozao.setPhone(cursor.getString(cursor.getColumnIndex(TiaoZao.PHONE)));
			tiaozao.setGoodsId(cursor.getString(cursor.getColumnIndex(TiaoZao.GOODSID)));
			tiaozao.setDetail(cursor.getString(cursor.getColumnIndex(TiaoZao.DETAIL)));
			tiaozao.setCollection(cursor.getString(cursor.getColumnIndex(TiaoZao.COLLECTION)));
			list.add(tiaozao);
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
	
}
