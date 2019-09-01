package com.pocketuniversity.service;

import com.pocketuniversity.bean.SearchGroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceMapForSearchGroup {

	public static final String GROUPNAME = "groupName";
	public static final String SERVERGROUPID = "serverGroupId";
	public static final String GROUPID = "groupId";
	public static final String DESCRIPTION = "description";
	public static final String 	CURNUN = "curNum";
	public static final String MAXNUM = "maxNum";
	public static final String OWNERNICKNAME = "ownerNickName";
	public static final String OWNERID = "ownerId";
	public static final String OWNERHEADER = "ownerHeader";
	Context cxt;
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	public static PreferenceMapForSearchGroup currentUserPreferenceMap;

	public PreferenceMapForSearchGroup(Context cxt) {
		this.cxt = cxt;
		pref = PreferenceManager.getDefaultSharedPreferences(cxt);
		editor = pref.edit();
	}

	public PreferenceMapForSearchGroup(Context cxt, String prefName) {
		this.cxt = cxt;
		pref = cxt.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		editor = pref.edit();
	}

	public void setSearchGroup(SearchGroup searchGroup){
		editor.putString(GROUPNAME, searchGroup.getGroupName()).commit();
		editor.putString(SERVERGROUPID, searchGroup.getServerGroupId()).commit();
		editor.putString(GROUPID, searchGroup.getGroupId()).commit();
		editor.putString(DESCRIPTION, searchGroup.getDescription()).commit();
		editor.putString(CURNUN, searchGroup.getCurNum()).commit();
		editor.putString(MAXNUM, searchGroup.getMaxNum()).commit();
		editor.putString(OWNERNICKNAME, searchGroup.getOwnerNickName()).commit();
		editor.putString(OWNERID, searchGroup.getOwnerId()).commit();
		editor.putString(OWNERHEADER, searchGroup.getOwnerHeader()).commit();
	}
	
	public SearchGroup getSearchGroup(){
		SearchGroup searchGroup = new SearchGroup();
		searchGroup.setGroupName(pref.getString(GROUPNAME, ""));
		searchGroup.setServerGroupId(pref.getString(SERVERGROUPID, ""));
		searchGroup.setGroupId(pref.getString(GROUPID, ""));
		searchGroup.setDescription(pref.getString(DESCRIPTION, ""));
		searchGroup.setCurNum(pref.getString(CURNUN, ""));
		searchGroup.setMaxNum(pref.getString(MAXNUM, ""));
		searchGroup.setOwnerNickName(pref.getString(OWNERNICKNAME, ""));
		searchGroup.setOwnerId(pref.getString(OWNERID, ""));
		searchGroup.setOwnerHeader(pref.getString(OWNERHEADER, ""));
		return searchGroup;
	}
}
