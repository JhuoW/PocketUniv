package com.pocketuniversity.bean;

import java.io.Serializable;

public class MyCampaignPostModel implements Serializable {

	
	private static final long serialVersionUID = 1111111111111111111L;

	private String id;
	private String title;
	private String content;
	private String imageurl;
	private String source;
	private String time;
	private String comment_count;
	private String view_count;
	private String collection_count;
	public String collection;
	public String join;
	public String hasJoin;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getComment_count() {
		return comment_count;
	}
	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}
	public String getView_count() {
		return view_count;
	}
	public void setView_count(String view_count) {
		this.view_count = view_count;
	}
	public String getCollection_count() {
		return collection_count;
	}
	public void setCollection_count(String collection_count) {
		this.collection_count = collection_count;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getJoin() {
		return join;
	}
	public void setJoin(String join) {
		this.join = join;
	}
	public String getHasJoin() {
		return hasJoin;
	}
	public void setHasJoin(String hasJoin) {
		this.hasJoin = hasJoin;
	}
	
	
	
}
