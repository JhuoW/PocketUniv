package com.pocketuniversity.bean;

public class ClassInfo {
	private int fromX;
	private int fromY;
	private int toX;
	private int toY;

	private int classid;
	private String classname;
	private String fromClassNum;
	private String classNumLen;
	private String weekday;
	private String classRoom;

	public void setPoint(int fromX, int fromY, int toX, int toY) {
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}

	public int getFromX() {
		return fromX;
	}

	public void setFromX(int fromX) {
		this.fromX = fromX;
	}

	public int getFromY() {
		return fromY;
	}

	public void setFromY(int fromY) {
		this.fromY = fromY;
	}

	public int getToX() {
		return toX;
	}

	public void setToX(int toX) {
		this.toX = toX;
	}

	public int getToY() {
		return toY;
	}

	public void setToY(int toY) {
		this.toY = toY;
	}

	public int getClassid() {
		return classid;
	}

	public void setClassid(int classid) {
		this.classid = classid;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getFromClassNum() {
		return fromClassNum;
	}

	public void setFromClassNum(String fromClassNum) {
		this.fromClassNum = fromClassNum;
	}

	public String getClassNumLen() {
		return classNumLen;
	}

	public void setClassNumLen(String classNumLen) {
		this.classNumLen = classNumLen;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}

}
