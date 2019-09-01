package com.pocketuniversity.bean;



import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class DepartmentPhone implements Serializable{

	private String id ; 
	private String name ;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
