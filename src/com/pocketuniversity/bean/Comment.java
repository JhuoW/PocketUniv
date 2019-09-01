package com.pocketuniversity.bean;

import java.io.Serializable;

public class Comment implements Serializable{
	private String code;
	private String image_url;
	private String name;
	private String time;
	private String content;
	private String authority;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "Comment [code=" + code + ", image_url=" + image_url + ", name="
				+ name + ", time=" + time + ", content=" + content
				+ ", authority=" + authority + "]";
	}


	
}
