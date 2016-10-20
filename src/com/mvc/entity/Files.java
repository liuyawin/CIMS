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
 * 文件
 * 
 * @author wangrui
 * @date 2016-10-14
 */
@Entity
@Table(name = "files")
public class Files implements Serializable {

	private static final long serialVersionUID = -6759651557758823982L;
	private Integer file_id;// ID
	private String file_name;// 文件名
	private String file_type;// 文件类型，后缀
	private String file_path;// 文件保存路径
	private Date file_ctime;// 文件创建时间
	private Contract contract;// fk外键，合同ID
	private User user; // fk外键，创建者ID
	private Integer file_isdelete;// 0：未删除，1：删除，默认未删除

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getFile_id() {
		return file_id;
	}

	public void setFile_id(Integer file_id) {
		this.file_id = file_id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	@Column(length = 20)
	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public Date getFile_ctime() {
		return file_ctime;
	}

	public void setFile_ctime(Date file_ctime) {
		this.file_ctime = file_ctime;
	}

	@ManyToOne
	@JoinColumn(name = "cont_id")
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getFile_isdelete() {
		return file_isdelete;
	}

	public void setFile_isdelete(Integer file_isdelete) {
		this.file_isdelete = file_isdelete;
	}

}
