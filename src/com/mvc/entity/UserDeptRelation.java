/**
 * 
 */
package com.mvc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 部门员工关系
 * 
 * @author lwt
 * @date2016年9月8日
 */
@Entity
@Table(name = "user_dept_relation")
public class UserDeptRelation {

	private Integer udre_id;// 关系ID
	private User user;// 用户
	private Department department;// 部门

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getUdre_id() {
		return udre_id;
	}

	public void setUdre_id(Integer udre_id) {
		this.udre_id = udre_id;
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
	@JoinColumn(name = "dept_id")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

}
