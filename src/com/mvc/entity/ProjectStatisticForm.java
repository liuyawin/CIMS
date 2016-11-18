package com.mvc.entity;

import java.util.Date;

/**
 * 项目统计表
 * 
 * @author wangrui
 * @date 2016-11-16
 */
public class ProjectStatisticForm {

	private Integer prsf_id;// ID
	private String cont_type;// 合同类型
	private String cont_project;// 项目名称
	private String manager_name;// 项目设总
	private String province;// 所在地（省）
	private String pro_stage;// 设计阶段（项目阶段）
	private Float install_capacity;// 装机容量（MV）
	private Float cont_money;// 合同额（万元）
	private String cont_status;// 合同状态
	private Date cont_stime;// 合同签订日期
	private String remark;// 备注

	public Integer getPrsf_id() {
		return prsf_id;
	}

	public void setPrsf_id(Integer prsf_id) {
		this.prsf_id = prsf_id;
	}

	public String getCont_type() {
		return cont_type;
	}

	public void setCont_type(String cont_type) {
		this.cont_type = cont_type;
	}

	public String getCont_project() {
		return cont_project;
	}

	public void setCont_project(String cont_project) {
		this.cont_project = cont_project;
	}

	public String getManager_name() {
		return manager_name;
	}

	public void setManager_name(String manager_name) {
		this.manager_name = manager_name;
	}

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

	public Float getInstall_capacity() {
		return install_capacity;
	}

	public void setInstall_capacity(Float install_capacity) {
		this.install_capacity = install_capacity;
	}

	public Float getCont_money() {
		return cont_money;
	}

	public void setCont_money(Float cont_money) {
		this.cont_money = cont_money;
	}

	public String getCont_status() {
		return cont_status;
	}

	public void setCont_status(String cont_status) {
		this.cont_status = cont_status;
	}

	public Date getCont_stime() {
		return cont_stime;
	}

	public void setCont_stime(Date cont_stime) {
		this.cont_stime = cont_stime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
