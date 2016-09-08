/**
 * 
 */
package com.mvc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	private String dept_remark; // 备注
	private Integer dept_state;// 部门状态，1表示存在，0表示不存在
	private Integer dept_pid;// 父ID

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getDept_id() {
		return dept_id;
	}

	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getDept_remark() {
		return dept_remark;
	}

	public void setDept_remark(String dept_remark) {
		this.dept_remark = dept_remark;
	}

	public Integer getDept_state() {
		return dept_state;
	}

	public void setDept_state(Integer dept_state) {
		this.dept_state = dept_state;
	}

	public Integer getDept_pid() {
		return dept_pid;
	}

	public void setDept_pid(Integer dept_pid) {
		this.dept_pid = dept_pid;
	}

}
