package com.mvc.entity;

/**
 * 未返回合同统计表
 * 
 * @author wangrui
 * @date 2016-11-18
 */
public class NoBackContForm {

	private Integer nb_id;// ID
	private String cont_project;// 项目名称
	private String cont_client;// 业主单位（业主公司名称）
	private Float cont_money;// 合同额（万元）
	private String handler;// 经手人（项目经理）
	private String header;// 负责人（主任）

	public Integer getNb_id() {
		return nb_id;
	}

	public void setNb_id(Integer nb_id) {
		this.nb_id = nb_id;
	}

	public String getCont_project() {
		return cont_project;
	}

	public void setCont_project(String cont_project) {
		this.cont_project = cont_project;
	}

	public String getCont_client() {
		return cont_client;
	}

	public void setCont_client(String cont_client) {
		this.cont_client = cont_client;
	}

	public Float getCont_money() {
		return cont_money;
	}

	public void setCont_money(Float cont_money) {
		this.cont_money = cont_money;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

}
