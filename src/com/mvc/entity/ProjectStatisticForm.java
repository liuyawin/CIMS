package com.mvc.entity;

import java.util.Date;

/**
 * 项目统计表
 * 
 * @author wangrui
 * @date 2016-11-15
 */
public class ProjectStatisticForm {

	private Integer prsf_id;// ID
	private String cont_project;// 项目名称
	private String manager_name;// 项目设总
	private String province;// 所在地（省）
	private String proStage;// 设计阶段（项目阶段）
	private Float install_capacity;// 装机容量（MV）
	private String cont_state;// 合同状态
	private Float cont_money;// 合同额（万元）
	private Date cont_stime;// 签订时间

	public Integer getPrsf_id() {
		return prsf_id;
	}

	public void setPrsf_id(Integer prsf_id) {
		this.prsf_id = prsf_id;
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

	public String getProStage() {
		return proStage;
	}

	public void setProStage(String proStage) {
		this.proStage = proStage;
	}

	public Float getInstall_capacity() {
		return install_capacity;
	}

	public void setInstall_capacity(Float install_capacity) {
		this.install_capacity = install_capacity;
	}

	public String getCont_state() {
		return cont_state;
	}

	public void setCont_state(String cont_state) {
		this.cont_state = cont_state;
	}

	public Float getCont_money() {
		return cont_money;
	}

	public void setCont_money(Float cont_money) {
		this.cont_money = cont_money;
	}

	public Date getCont_stime() {
		return cont_stime;
	}

	public void setCont_stime(Date cont_stime) {
		this.cont_stime = cont_stime;
	}

}
