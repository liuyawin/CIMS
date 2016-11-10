package com.mvc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 报警级别设置
 * 
 * @author wangrui
 * @date 2016-09-08
 */
@Entity
@Table(name = "alarm_level")
public class AlarmLevel implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer alle_id;// ID
	private Integer alle_rank;// 报警等级
	private Role role;// 角色，0-文书，1-主任，2-副院长，3-院长
	private Integer alle_days;// 报警间隔
	private Integer alle_isdelete;// 0：未删除，1：已删除

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getAlle_id() {
		return alle_id;
	}

	public void setAlle_id(Integer alle_id) {
		this.alle_id = alle_id;
	}

	public Integer getAlle_rank() {
		return alle_rank;
	}

	public void setAlle_rank(Integer alle_rank) {
		this.alle_rank = alle_rank;
	}

	public Integer getAlle_days() {
		return alle_days;
	}

	public void setAlle_days(Integer alle_days) {
		this.alle_days = alle_days;
	}

	@ManyToOne
	@JoinColumn(name = "role_id")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getAlle_isdelete() {
		return alle_isdelete;
	}

	public void setAlle_isdelete(Integer alle_isdelete) {
		this.alle_isdelete = alle_isdelete;
	}
}
