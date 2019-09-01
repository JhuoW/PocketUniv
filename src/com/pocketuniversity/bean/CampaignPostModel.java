package com.pocketuniversity.bean;

import java.io.Serializable;

public class CampaignPostModel implements Serializable{
	private static final long serialVersionUID = 2763337776495941728L;

	public String id;
	public String title;
	public String content;
	public String imageurl;
	public String source;
	public String time;
	public String comment_count;
	public String view_count;
	public String collection_count;
	public String channel_id;
	public String collection;
	public String join;
	public String hasJoin;
	public String place;
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	
	public String gethasJoin(){
		return hasJoin;
	}

	public void setHasJoin(String hasJoin){
		this.hasJoin = hasJoin;
	}
	
	public String isJoin(){
		return join;
	}
	
	public void setJoin(String join){
		this.join = join ;
	}
	
	public String isCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

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

}
