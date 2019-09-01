package com.pocketuniversity.bean;

import java.io.Serializable;

public class CourierDetail implements Serializable{

	private String content;
	private String time;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
