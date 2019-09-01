package com.pocketuniversity.bean;

import java.io.Serializable;

public class SearchGroup implements Serializable{

	String groupName;
	String serverGroupId;
	String groupId;
	String description;
	String curNum;
	String maxNum;
	String ownerNickName;
	String ownerId;
	String ownerHeader;
	
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getServerGroupId() {
		return serverGroupId;
	}
	public void setServerGroupId(String serverGroupId) {
		this.serverGroupId = serverGroupId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCurNum() {
		return curNum;
	}
	public void setCurNum(String curNum) {
		this.curNum = curNum;
	}
	public String getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(String maxNum) {
		this.maxNum = maxNum;
	}
	public String getOwnerNickName() {
		return ownerNickName;
	}
	public void setOwnerNickName(String ownerNickName) {
		this.ownerNickName = ownerNickName;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerHeader() {
		return ownerHeader;
	}
	public void setOwnerHeader(String ownerHeader) {
		this.ownerHeader = ownerHeader;
	}
	
	
	
	
}
