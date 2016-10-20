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
 * 工期阶段表
 * 
 * @author zjn
 * @date 2016年9月8日
 */
@Entity
@Table(name = "project_stage")
public class ProjectStage implements Serializable {

	private static final long serialVersionUID = 5422736653915596798L;

	private Integer prst_id; // 工期阶段ID
	private String prst_content; // 工期阶段内容
	private Date prst_etime; // 阶段截止时间
	private Integer prst_state; // 阶段状态,0:未完成，1已完成
	private Date prst_wtime; // 工作结束提醒时间
	private Integer prst_wday; // 工作提醒天数
	private Date prst_atime; // 实际完成时间
	private Date prst_ctime; // 阶段录入时间
	private String prst_remark; // 备注
	private Contract contract;// fk外键， 所属合同
	private User user; // fk外键，录入人
	private User manager; // fk外键，项目经理
	private Integer prst_isdelete;// 0:未删除，1:已删除

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getPrst_id() {
		return prst_id;
	}

	public void setPrst_id(Integer prst_id) {
		this.prst_id = prst_id;
	}

	@Column(length = 64)
	public String getPrst_content() {
		return prst_content;
	}

	public void setPrst_content(String prst_content) {
		this.prst_content = prst_content;
	}

	public Date getPrst_etime() {
		return prst_etime;
	}

	public void setPrst_etime(Date prst_etime) {
		this.prst_etime = prst_etime;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getPrst_state() {
		return prst_state;
	}

	public void setPrst_state(Integer prst_state) {
		this.prst_state = prst_state;
	}

	public Date getPrst_wtime() {
		return prst_wtime;
	}

	public void setPrst_wtime(Date prst_wtime) {
		this.prst_wtime = prst_wtime;
	}

	public Date getPrst_atime() {
		return prst_atime;
	}

	public void setPrst_atime(Date prst_atime) {
		this.prst_atime = prst_atime;
	}

	public Date getPrst_ctime() {
		return prst_ctime;
	}

	public void setPrst_ctime(Date prst_ctime) {
		this.prst_ctime = prst_ctime;
	}

	public String getPrst_remark() {
		return prst_remark;
	}

	public void setPrst_remark(String prst_remark) {
		this.prst_remark = prst_remark;
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

	@ManyToOne
	@JoinColumn(name = "manager_id")
	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getPrst_wday() {
		return prst_wday;
	}

	public void setPrst_wday(Integer prst_wday) {
		this.prst_wday = prst_wday;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getPrst_isdelete() {
		return prst_isdelete;
	}

	public void setPrst_isdelete(Integer prst_isdelete) {
		this.prst_isdelete = prst_isdelete;
	}
}
