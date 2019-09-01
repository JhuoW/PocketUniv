package com.pocketuniversity.bean;

import java.util.ArrayList;
import java.util.List;

public class Province {
	private String code;
	private String name;
	private String pcode;
	private List<Area> citys=new ArrayList<Area>();
	
	public List<Area> getCitys() {
		return citys;
	}
	public void setCitys(List<Area> citys) {
		this.citys = citys;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public Province() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Area [code=" + code + ", name=" + name + ", pcode=" + pcode
				+ "]";
	}
	
	
}
