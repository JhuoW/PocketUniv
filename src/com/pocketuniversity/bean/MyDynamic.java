package com.pocketuniversity.bean;

import java.io.Serializable;

public class MyDynamic implements Serializable{
	private static final long serialVersionUID = 1111111111111111111L;


	private String name;
	private String header ; 
	private String imgUrl;
	private String time;
	private String content;
	private String commentCount;
	private String zanCount;
	private String isZan;
	private String userId;
	private String id;
	private String day;
	private String month;
	
	
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsZan() {
		return isZan;
	}
	public void setIsZan(String isZan) {
		this.isZan = isZan;
	}
	public String getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	public String getZanCount() {
		return zanCount;
	}
	public void setZanCount(String zanCount) {
		this.zanCount = zanCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
