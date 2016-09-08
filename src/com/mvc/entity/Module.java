package com.mvc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模块
 * 
 * @author wanghuimin
 * @date 2016年9月8日
 */
@Entity
@Table(name = "module")
public class Module {
	private Integer modu_id;// ID
	private String modu_name;// 模块名称名称
	private Integer modu_pid;// 父ID

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getModu_id() {
		return modu_id;
	}

	public void setModu_id(Integer modu_id) {
		this.modu_id = modu_id;
	}

	public String getModu_name() {
		return modu_name;
	}

	public void setModu_name(String modu_name) {
		this.modu_name = modu_name;
	}

	public Integer getModu_pid() {
		return modu_pid;
	}

	public void setModu_pid(Integer modu_pid) {
		this.modu_pid = modu_pid;
	}

}
