package com.pocketuniversity.entity;

import java.util.List;

/**
 * �?级Item实体�?
 * 
 * @author zihao
 * 
 */
public class GroupStatusEntity {
	private String groupName;
	/** 二级Item数据列表 **/
	private List<ChildStatusEntity> childList;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<ChildStatusEntity> getChildList() {
		return childList;
	}

	public void setChildList(List<ChildStatusEntity> childList) {
		this.childList = childList;
	}

}