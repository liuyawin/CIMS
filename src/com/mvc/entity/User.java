package com.mvc.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户表
 * 
 * @author wanghuimin
 * @date 2016年9月8日
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer uesr_id; // id
	private String user_num; // 账号
	private String user_pwd; // 密码
	private String user_name;// 姓名
	private Integer user_sex;// 性别，1：女，0：男
	private String user_tel;// 电话
	private String user_email;// 邮箱
	private String user_role;// 职位
	private Integer user_isdelete;// 员工状态，1：已删除，0：未删除

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getUesr_id() {
		return uesr_id;
	}

	public void setUesr_id(Integer uesr_id) {
		this.uesr_id = uesr_id;
	}

	public String getUser_num() {
		return user_num;
	}

	public void setUser_num(String user_num) {
		this.user_num = user_num;
	}

	public String getUser_pwd() {
		return user_pwd;
	}

	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Integer isUser_sex() {
		return user_sex;
	}

	public void setUser_sex(Integer user_sex) {
		this.user_sex = user_sex;
	}

	public String getUser_tel() {
		return user_tel;
	}

	public void setUser_tel(String user_tel) {
		this.user_tel = user_tel;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_role() {
		return user_role;
	}

	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}

	public Integer getUser_isdelete() {
		return user_isdelete;
	}

	public void setUser_isdelete(Integer user_isdelete) {
		this.user_isdelete = user_isdelete;
	}

}