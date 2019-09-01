package com.pocketuniversity.bean;

import java.io.Serializable;

import com.pocketuniversity.adapter.FriendCommentAdapter;

public class InnerComment implements Serializable {

	FriendCommentAdapter adapter;

	public FriendCommentAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(FriendCommentAdapter adapter) {
		this.adapter = adapter;
	}
	
}
