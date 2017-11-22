package com.huiyi.huiyi_fanny.model;

/**
 * about 参数配置表
 * */

public class Set {
	
	private Integer ID;
	private String url;

	public Set(String url) {
		super();
		this.url = url;
	}
	
	public Set(Integer iD, String url) {
		super();
		ID = iD;
		this.url = url;
	}
	
	
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}


	
	
	

}
