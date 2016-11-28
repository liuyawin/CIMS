package com.mvc.entity;

/**
 * 光伏自营项目汇总表
 * 
 * @author zjn
 * @date 2016年11月28日
 */
public class SummarySheet {
	private String order_num;
	private String sub_num;
	private String province;
	private String pro_stage;
	private String cont_project;
	private String install_capacity;
	private String cont_client;
	private String cont_money;
	private String status;
	private String remark;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPro_stage() {
		return pro_stage;
	}

	public void setPro_stage(String pro_stage) {
		this.pro_stage = pro_stage;
	}

	public String getCont_project() {
		return cont_project;
	}

	public void setCont_project(String cont_project) {
		this.cont_project = cont_project;
	}

	public String getInstall_capacity() {
		return install_capacity;
	}

	public void setInstall_capacity(String install_capacity) {
		this.install_capacity = install_capacity;
	}

	public String getCont_client() {
		return cont_client;
	}

	public void setCont_client(String cont_client) {
		this.cont_client = cont_client;
	}

	public String getCont_money() {
		return cont_money;
	}

	public void setCont_money(String cont_money) {
		this.cont_money = cont_money;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	public String getSub_num() {
		return sub_num;
	}

	public void setSub_num(String sub_num) {
		this.sub_num = sub_num;
	}

}
