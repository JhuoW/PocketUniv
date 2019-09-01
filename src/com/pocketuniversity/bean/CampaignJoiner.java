package com.pocketuniversity.bean;

import java.io.Serializable;

public class CampaignJoiner implements Serializable{

	private String userId;
	private String nickName;
	private String Header;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getHeader() {
		return Header;
	}
	public void setHeader(String header) {
		Header = header;
	}
	
	
	
}
