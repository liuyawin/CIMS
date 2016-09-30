package com.mvc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色
 * 
 * @author wanghuimin
 * @date 2016年9月22日
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer role_id;
	private String role_name;
	private Integer role_state;// 0表示未删除，1表示删除
	private String role_permission;// 角色权限

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	@Column(length = 32)
	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getRole_state() {
		return role_state;
	}

	public void setRole_state(Integer role_state) {
		this.role_state = role_state;
	}

	@Column(length = 128)
	public String getRole_permission() {
		return role_permission;
	}

	public void setRole_permission(String role_permission) {
		this.role_permission = role_permission;
	}
}
