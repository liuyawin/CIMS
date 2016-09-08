package com.mvc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 权限表
 * 
 * @author wanghuimin
 * @date 2016年9月8日
 */
@Entity
@Table(name = "permission")
public class Permission {
	private Integer perm_id;// ID
	private Integer perm_rank;// 权限等级,0:无权限，1查看，2添加，3修改，4删除
	private User user;// 用户外键
	private Module module;// 模块外键

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getPerm_id() {
		return perm_id;
	}

	public void setPerm_id(Integer perm_id) {
		this.perm_id = perm_id;
	}

	public Integer getPerm_rank() {
		return perm_rank;
	}

	public void setPerm_rank(Integer perm_rank) {
		this.perm_rank = perm_rank;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name = "modu_id")
	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

}
