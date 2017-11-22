package com.huiyi.huiyi_fanny.model;



/**
 * shipment 出货信息表
 * */

public class Shipment {

	private Integer sh_cquantity;
	private String sh_datetime;
	private Integer sh_dcode;
	private Integer sh_id;
	private String sh_kubeID;
	private String sh_xcode;
	private String sh_xorderID;
	private Integer sh_xscode;
	private Integer sh_zcode;

	public Shipment(Integer sh_cquantity, String sh_datetime, Integer sh_dcode, Integer sh_id, String sh_kubeID, String sh_xcode, String sh_xorderID, Integer sh_xscode, Integer sh_zcode) {
		this.sh_cquantity = sh_cquantity;
		this.sh_datetime = sh_datetime;
		this.sh_dcode = sh_dcode;
		this.sh_id = sh_id;
		this.sh_kubeID = sh_kubeID;
		this.sh_xcode = sh_xcode;
		this.sh_xorderID = sh_xorderID;
		this.sh_xscode = sh_xscode;
		this.sh_zcode = sh_zcode;
	}

	public Integer getSh_cquantity() {
		return sh_cquantity;
	}

	public void setSh_cquantity(Integer sh_cquantity) {
		this.sh_cquantity = sh_cquantity;
	}

	public String getSh_datetime() {
		return sh_datetime;
	}

	public void setSh_datetime(String sh_datetime) {
		this.sh_datetime = sh_datetime;
	}

	public Integer getSh_dcode() {
		return sh_dcode;
	}

	public void setSh_dcode(Integer sh_dcode) {
		this.sh_dcode = sh_dcode;
	}

	public Integer getSh_id() {
		return sh_id;
	}

	public void setSh_id(Integer sh_id) {
		this.sh_id = sh_id;
	}

	public String getSh_kubeID() {
		return sh_kubeID;
	}

	public void setSh_kubeID(String sh_kubeID) {
		this.sh_kubeID = sh_kubeID;
	}

	public String getSh_xcode() {
		return sh_xcode;
	}

	public void setSh_xcode(String sh_xcode) {
		this.sh_xcode = sh_xcode;
	}

	public String getSh_xorderID() {
		return sh_xorderID;
	}

	public void setSh_xorderID(String sh_xorderID) {
		this.sh_xorderID = sh_xorderID;
	}

	public Integer getSh_xscode() {
		return sh_xscode;
	}

	public void setSh_xscode(Integer sh_xscode) {
		this.sh_xscode = sh_xscode;
	}

	public Integer getSh_zcode() {
		return sh_zcode;
	}

	public void setSh_zcode(Integer sh_zcode) {
		this.sh_zcode = sh_zcode;
	}
}
