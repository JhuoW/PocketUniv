package com.pocketuniversity.db;

import java.util.ArrayList;

import com.pocketuniversity.bean.Area;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class CityDB {
	private SQLiteDatabase db;
	private Context context;
	private DBManager dbm;
	
	public CityDB(Context context) {
		super();
		this.context = context;
		dbm = new DBManager(context);
	}

	public ArrayList<Area> getCity(String pcode) {
		dbm.openDatabase();
		db = dbm.getDatabase();
		ArrayList<Area> list = new ArrayList<Area>();
		
	 	try {    
	        String sql = "select * from T_City where ProvinceID='"+pcode+"'";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
	        	String code=cursor.getString(cursor.getColumnIndex("ID")); 
	        	byte bytes[]=cursor.getBlob(1); 
		        String name=new String(bytes,"utf-8");
		        Area area=new Area();
		        area.setName(name);
		        area.setCode(code);
		        area.setPcode(pcode);
		        list.add(area);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("ID")); 
	        byte bytes[]=cursor.getBlob(1); 
	        String name=new String(bytes,"utf-8");
	        Area area=new Area();
	        area.setName(name);
	        area.setCode(code);
	        area.setPcode(pcode);
	        list.add(area);
	        
	    } catch (Exception e) {  
	    	return null;
	    } 
	 	dbm.closeDatabase();
	 	db.close();	

		return list;

	}
	public ArrayList<Area> getProvince() {
		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	ArrayList<Area> list = new ArrayList<Area>();
		
	 	try {    
	        String sql = "select * from T_Province";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
	        	String code=cursor.getString(cursor.getColumnIndex("ID")); 
		        byte bytes[]=cursor.getBlob(1); 
		        String name=new String(bytes,"utf-8");
		        Area area=new Area();
		        area.setName(name);
		        area.setCode(code);
		        list.add(area);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("ID")); 
	        byte bytes[]=cursor.getBlob(1); 
	        String name=new String(bytes,"utf-8");
	        Area area=new Area();
	        area.setName(name);
	        area.setCode(code);
	        list.add(area);
	        
	    } catch (Exception e) {  
	    	return null;
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
		return list;
		
	}
	public ArrayList<Area> getDistrict(String pcode) {
		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	ArrayList<Area> list = new ArrayList<Area>();
	 	try {    
	        String sql = "select * from T_District where CityID='"+pcode+"'";  
	        Cursor cursor = db.rawQuery(sql,null);
	        if (cursor.moveToFirst()) {
				while (!cursor.isLast()) {
					String code = cursor.getString(cursor
							.getColumnIndex("ID"));
					byte bytes[] = cursor.getBlob(1);
					String name = new String(bytes, "utf-8");
					Area Area = new Area();
					Area.setCode(code);
					Area.setName(name);
					Area.setPcode(pcode);
					list.add(Area);
					cursor.moveToNext();
				}
				String code = cursor.getString(cursor.getColumnIndex("ID"));
				byte bytes[] = cursor.getBlob(1);
				String name = new String(bytes, "utf-8");
				Area Area = new Area();
				Area.setCode(code);
				Area.setName(name);
				Area.setPcode(pcode);
				list.add(Area);
			}
	        
	    } catch (Exception e) { 
	    	Log.i("wer", e.toString());
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
		return list;
		
	}
}
