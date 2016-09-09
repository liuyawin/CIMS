/**
 * 
 */
package com.mvc.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 系统日志表
 * 
 * @author zjn
 * @date 2016年9月8日
 */
@Entity
@Table(name = "journal")
public class Journal implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer jour_id; // 系统日志ID
	private String jour_content; // 日志内容
	private Date jour_time; // 发送请求的日期
	private User user; // fk外键，操作者ID

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getJour_id() {
		return jour_id;
	}

	public void setJour_id(Integer jour_id) {
		this.jour_id = jour_id;
	}
	@Column(length = 100)
	public String getJour_content() {
		return jour_content;
	}

	public void setJour_content(String jour_content) {
		this.jour_content = jour_content;
	}

	public Date getJour_time() {
		return jour_time;
	}

	public void setJour_time(Date jour_time) {
		this.jour_time = jour_time;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
