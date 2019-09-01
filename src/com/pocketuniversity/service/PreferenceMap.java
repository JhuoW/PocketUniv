package com.pocketuniversity.service;

import com.example.pocketuniversity.R;
import com.pocketuniversity.base.App;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.bean.SearchGroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceMap {

	public static final String NOTIFY_WHEN_NEWS = "notifyWhenNews";
	public static final String VOICE_NOTIFY = "voiceNotify";
	public static final String VIBRATE_NOTIFY = "vibrateNotify";
	public static final String ISREMENBER = "isremenber";
	public static final String ISPUSH = "ispush";
	public static final String ACCOUNT = "account";
	public static final String PASSWORD = "password";

	public static final String NAME = "curusername";
	public static final String SEX = "curusersex";
	public static final String ID = "curuserid";
	public static final String IMAGE = "curuserimage";
	public static final String ADDRESS = "curuseraddress";
	public static final String AUTHORITY = "curuserauthority";
	
	
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
	public static PreferenceMap currentUserPreferenceMap;

	public PreferenceMap(Context cxt) {
		this.cxt = cxt;
		pref = PreferenceManager.getDefaultSharedPreferences(cxt);
		editor = pref.edit();
	}

	public PreferenceMap(Context cxt, String prefName) {
		this.cxt = cxt;
		pref = cxt.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		editor = pref.edit();
	}

	// public static PreferenceMap getCurUserPrefDao(Context ctx) {
	// if (currentUserPreferenceMap == null) {
	// currentUserPreferenceMap = new PreferenceMap(ctx,
	// User.getCurrentUserId());
	// }
	// return currentUserPreferenceMap;
	// }

	public boolean isRemenberAccount() {
		return pref.getBoolean(ISREMENBER, false);
	}

	public void setIsRemenberAccount(Boolean flag) {
		editor.putBoolean(ISREMENBER, flag).commit();
	}

	public boolean isPush() {
		return pref.getBoolean(ISPUSH, true);
	}

	public void setIsPush(Boolean flag) {
		editor.putBoolean(ISPUSH, flag).commit();
	}

	public String getAccount() {
		return pref.getString(ACCOUNT, "");
	}

	public void setAccount(String account) {
		editor.putString(ACCOUNT, account).commit();
	}

	public String getPassword() {
		return pref.getString(PASSWORD, "");
	}

	public void setPassword(String password) {
		editor.putString(PASSWORD, password).commit();
	}

	public boolean isNotifyWhenNews() {
		return pref.getBoolean(NOTIFY_WHEN_NEWS, App.ctx.getResources()
				.getBoolean(R.bool.defaultNotifyWhenNews));
	}

	public void setNotifyWhenNews(boolean notifyWhenNews) {
		editor.putBoolean(NOTIFY_WHEN_NEWS, notifyWhenNews).commit();
	}

	boolean getBooleanByResId(int resId) {
		return App.ctx.getResources().getBoolean(resId);
	}

	public boolean isVoiceNotify() {
		return pref.getBoolean(VOICE_NOTIFY,
				getBooleanByResId(R.bool.defaultVoiceNotify));
	}

	public void setVoiceNotify(boolean voiceNotify) {
		editor.putBoolean(VOICE_NOTIFY, voiceNotify).commit();
	}

	public boolean isVibrateNotify() {
		return pref.getBoolean(VIBRATE_NOTIFY,
				getBooleanByResId(R.bool.defaultVibrateNotify));
	}

	public void setVibrateNotify(boolean vibrateNotify) {
		editor.putBoolean(VIBRATE_NOTIFY, vibrateNotify);
	}

	public void setUser(PUUser user) {
		editor.putString(NAME, user.getName()).commit();
		editor.putString(ID, user.getID()).commit();
		editor.putString(SEX, user.getSex()).commit();
		editor.putString(IMAGE, user.getImage()).commit();
		editor.putString(ADDRESS, user.getAddress()).commit();
		editor.putString(AUTHORITY, user.getAuthority()).commit();
	}

	public PUUser getUser() {
		PUUser user = new PUUser();
		user.setID(pref.getString(ID, ""));
		user.setImage(pref.getString(IMAGE, ""));
		user.setName(pref.getString(NAME, ""));
		user.setSex(pref.getString(SEX, ""));
		user.setAuthority(pref.getString(AUTHORITY, ""));
		user.setAddress(pref.getString(ADDRESS, ""));
		return user;
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
