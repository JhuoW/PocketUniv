package com.pocketuniversity.bean;

import java.io.Serializable;

public class CarRoute implements Serializable{

	private String info;
	private String name;
	private String stats;
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStats() {
		return stats;
	}
	public void setStats(String stats) {
		this.stats = stats;
	}
	
}
