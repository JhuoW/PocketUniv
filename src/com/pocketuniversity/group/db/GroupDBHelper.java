package com.pocketuniversity.group.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.bean.Tiaozao;
import com.pocketuniversity.db.CampaignPost;
import com.pocketuniversity.db.Category;
import com.pocketuniversity.db.Friends;
import com.pocketuniversity.db.Schedule;
import com.pocketuniversity.db.TiaoZao;
import com.pocketuniversity.group.bean.Group;
import com.pocketuniversity.group.bean.GroupMember;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupDBHelper extends SQLiteOpenHelper{
	private static SQLiteDatabase mSqLiteDatabase;
	private static final String DB_NAME = "group.db";
	private static final int VERSION = 1;
	public static final String DROP_USER_TABLE = "drop table ";
	
	public GroupDBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
		mSqLiteDatabase = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String create_group = "create table if not exists " 
				+ DBGroup.TABLENAME 
				+  "  ( _id integer primary key autoincrement , " 
				+ DBGroup.GROUPID + "  text , "
				+ DBGroup.ID + " text ,  "
				+ DBGroup.GROUPNAME + " text , " 
				+ DBGroup.DESCRIPTION + " text , "
				+ DBGroup.GROUPTYPE + " text ) ;";
		db.execSQL(create_group);
		
		String groupmember = "create table if not exists " 
				+ DBMember.TABLENAME 
				+  "  ( _id integer primary key autoincrement , " 
				+ DBMember.USERID + "  text , "
				+ DBMember.NAME + " text ,  "
				+ DBMember.IMGURL + " text ); ";
		db.execSQL(groupmember);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String deleteschTable = "DROP TABLE " + DBGroup.TABLENAME;
		db.execSQL(deleteschTable);
		String deletegroupTable = "DROP TABLE " + DBMember.TABLENAME;
		db.execSQL(deletegroupTable);
		String create_group = "create table if not exists " 
				+ DBGroup.TABLENAME
				+  "  ( _id integer primary key autoincrement , " 
				+ DBGroup.GROUPID + "  text , "
				+ DBGroup.ID + " text ,  "
				+ DBGroup.GROUPNAME + " text , " 
				+ DBGroup.DESCRIPTION + " text , "
				+ DBGroup.GROUPTYPE + " text ) ;";
		db.execSQL(create_group);
		
		String groupmember = "create table if not exists " 
				+ DBMember.TABLENAME 
				+  "  ( _id integer primary key autoincrement , " 
				+ DBMember.USERID + "  text , "
				+ DBMember.NAME + " text ,  "
				+ DBMember.IMGURL + " text ); ";
		db.execSQL(groupmember);
	}
	
	public void insertGroup(JSONArray group) {
		try {
			for (int i = 0; i < group.length(); i++) {
				JSONObject obj = group.getJSONObject(i);

				String groupId = obj.getString("groupId");
				String groupName = obj.getString("groupName");
				String groupType = obj.getString("groupType");
				String description = obj.getString("description");
				String id = obj.getString("id");
				ContentValues contentValues = new ContentValues();
				contentValues.put(DBGroup.GROUPID, groupId);
				contentValues.put(DBGroup.ID, id);
				contentValues.put(DBGroup.GROUPNAME, groupName);
				contentValues.put(DBGroup.GROUPTYPE, groupType);
				contentValues.put(DBGroup.DESCRIPTION, description);
				mSqLiteDatabase.insert(DBGroup.TABLENAME, null, contentValues);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清空某张表内数据
	 */
	public void deleteAllDataFromTable(String Tablename){
		mSqLiteDatabase.execSQL("DELETE FROM " + Tablename);
		
	}
	
	public List<Group> getAllGroup(){
		List<Group> list = new ArrayList<Group>();
		Cursor cursor = mSqLiteDatabase.query(DBGroup.TABLENAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			Group group = new Group();
			group.setGroupId(cursor.getString(cursor.getColumnIndex(DBGroup.GROUPID)));
			group.setGroupName(cursor.getString(cursor.getColumnIndex(DBGroup.GROUPNAME)));
			group.setGroupType(cursor.getString(cursor.getColumnIndex(DBGroup.GROUPTYPE)));
			group.setId(cursor.getString(cursor.getColumnIndex(DBGroup.ID)));
			group.setDescription(cursor.getString(cursor.getColumnIndex(DBGroup.DESCRIPTION)));
			list.add(group);
		}
		cursor.close();
		return list;
	}
	
	
	/**
	 * 插入groupmember表
	 */
	public void insertGroupMember(JSONArray member){
		try {
			for (int i = 0; i < member.length(); i++) {
				JSONObject obj = member.getJSONObject(i);

				String userId = obj.getString("userID");
				String name = obj.getString("name");
				String  imgUrl = obj.getString("imgUrl");
				ContentValues contentValues = new ContentValues();
				contentValues.put(DBMember.USERID, userId);
				contentValues.put(DBMember.NAME, name);
				contentValues.put(DBMember.IMGURL, imgUrl);
				mSqLiteDatabase.insert(DBMember.TABLENAME, null, contentValues);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据群Id找自增Id
	 * @param groupId
	 * @return
	 */
	public String getIdByGroupId(String groupId){
		String id = null;
		Cursor cursor  = mSqLiteDatabase.query(DBGroup.TABLENAME, null, DBGroup.GROUPID + " = ?", new String[]{groupId}, null, null, null);
		while(cursor.moveToNext()){
			id = cursor.getString(cursor.getColumnIndex(DBGroup.ID));
		}
		cursor.close();
		return id;
	}

	/**
	 * 根据消息发送者id找到昵称
	 */
	public String getNicknameByid(String id){
		String nickname = null;		
		Cursor cursor = mSqLiteDatabase.query(DBMember.TABLENAME, null, DBMember.USERID + " = ? ", new String[]{id}, null, null, null);
		while(cursor.moveToNext()){
			nickname = cursor.getString(cursor.getColumnIndex(DBMember.NAME));
		}
		cursor.close();
		return nickname;
	}
	
	/**
	 * 获取所有群成员
	 */
	public List<GroupMember> getAllmember(){
		List<GroupMember> list = new ArrayList<GroupMember>();
		Cursor cursor = mSqLiteDatabase.query(DBMember.TABLENAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			GroupMember gm = new GroupMember();
			gm.setUserId(cursor.getString(cursor.getColumnIndex(DBMember.USERID)));
			gm.setName(cursor.getString(cursor.getColumnIndex(DBMember.NAME)));
			gm.setImgUrl(cursor.getString(cursor.getColumnIndex(DBMember.IMGURL)));
			list.add(gm);
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 更具userId获取成员
	 */
	public GroupMember getMember(String userId){
		GroupMember gm = new GroupMember();
		Cursor cursor = mSqLiteDatabase.query(DBMember.TABLENAME, null, DBMember.USERID + " = ? ", new String[]{userId}, null, null, null);
		while(cursor.moveToNext()){
			gm.setUserId(cursor.getString(cursor.getColumnIndex(DBMember.USERID)));
			gm.setName(cursor.getString(cursor.getColumnIndex(DBMember.NAME)));
			gm.setImgUrl(cursor.getString(cursor.getColumnIndex(DBMember.IMGURL)));
		}
		cursor.close();
		return gm;
	}
	
	/**
	 * 根据群id获取群对象
	 */
	public Group getGroup(String id){
		Group group = new Group();
		Cursor cursor = mSqLiteDatabase.query(DBGroup.TABLENAME, null, DBGroup.ID + " = ? ", new String[]{id}, null, null, null);
		while(cursor.moveToNext()){
			group.setGroupId(cursor.getString(cursor.getColumnIndex(DBGroup.GROUPID)));
			group.setGroupName(cursor.getString(cursor.getColumnIndex(DBGroup.GROUPNAME)));
			group.setGroupType(cursor.getString(cursor.getColumnIndex(DBGroup.GROUPTYPE)));
			group.setId(cursor.getString(cursor.getColumnIndex(DBGroup.ID)));
			group.setDescription(cursor.getString(cursor.getColumnIndex(DBGroup.DESCRIPTION)));
		}
		cursor.close();
		return group;
	}
	
	/**
	 * 根据groupid 获取对象
	 */
	
	public Group getGroup2(String groupid){
		Group group = new Group();
		Cursor cursor = mSqLiteDatabase.query(DBGroup.TABLENAME, null, DBGroup.GROUPID + " = ? ", new String[]{groupid}, null, null, null);
		while(cursor.moveToNext()){
			group.setGroupId(cursor.getString(cursor.getColumnIndex(DBGroup.GROUPID)));
			group.setGroupName(cursor.getString(cursor.getColumnIndex(DBGroup.GROUPNAME)));
			group.setGroupType(cursor.getString(cursor.getColumnIndex(DBGroup.GROUPTYPE)));
			group.setId(cursor.getString(cursor.getColumnIndex(DBGroup.ID)));
			group.setDescription(cursor.getString(cursor.getColumnIndex(DBGroup.DESCRIPTION)));
		}
		cursor.close();
		return group;
	}
	
	public Group selectGroups(String serverGroupId){
		Group group = new Group();
		Cursor cursor = mSqLiteDatabase.query(DBGroup.TABLENAME, null, DBGroup.ID + " = ? ", new String[]{serverGroupId}, null, null, null);
		while(cursor.moveToNext()){
			group.setGroupId(cursor.getString(cursor.getColumnIndex(DBGroup.GROUPID)));
			group.setGroupName(cursor.getString(cursor.getColumnIndex(DBGroup.GROUPNAME)));
			group.setGroupType(cursor.getString(cursor.getColumnIndex(DBGroup.GROUPTYPE)));
			group.setId(cursor.getString(cursor.getColumnIndex(DBGroup.ID)));
			group.setDescription(cursor.getString(cursor.getColumnIndex(DBGroup.DESCRIPTION)));
		}
		cursor.close();
		return group;
	}
	
}
