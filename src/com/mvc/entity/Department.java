/**
 * 
 */
package com.mvc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * 部门
 * 
 * @author lwt
 * @date2016年9月8日
 *
 */
@Entity
@Table(name = "department")
public class Department {

	private Integer dept_id; // Id
	private String dept_name; // 部门名称
	private Integer dept_state;// 部门状态，0表示存在，1表示不存在
	private Department department;
	private String dept_remark;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getDept_id() {
		return dept_id;
	}

	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}

	@Column(length = 32)
	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getDept_state() {
		return dept_state;
	}

	public void setDept_state(Integer dept_state) {
		this.dept_state = dept_state;
	}

	@ManyToOne
	@JoinColumn(name="dept_pid")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getDept_remark() {
		return dept_remark;
	}

	public void setDept_remark(String dept_remark) {
		this.dept_remark = dept_remark;
	}

}
