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
 * 收款节点
 * 
 * @author wangrui
 * @date 2016-09-08
 */
@Entity
@Table(name = "receiveNode")
public class ReceiveNode implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer reno_id;// ID
	private String reno_content;// 节点内容
	private Float reno_money;// 应收款金额
	private Date reno_time;// 节点截止时间
	private Date reno_wtime; // 收款提醒时间
	private Integer reno_wday; // 收款提醒天数
	private Integer reno_state;// 是否已收款，0：未收款，1未付全款，2已付全款，3提前到款
	private Float reno_amoney;// 实际收款金额
	private Date reno_ctime;// 节点录入时间
	private String reno_remark;// 备注
	private User user;// fk外键，录入人
	private Contract contract;// fk外键，所属合同
	private ProjectStage projectStage;// fk外键，关联阶段
	private Integer reno_isdelete;// 0:未删除，1:已删除

	// 以下为冗余字段，用于报表统计
	private Date cont_stime;// 合同签订日期
	private String province;// 该合同对应的区域

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getReno_id() {
		return reno_id;
	}

	public void setReno_id(Integer reno_id) {
		this.reno_id = reno_id;
	}

	@Column(length = 64)
	public String getReno_content() {
		return reno_content;
	}

	public void setReno_content(String reno_content) {
		this.reno_content = reno_content;
	}

	@Column(columnDefinition = "float(10,2) default '0.00'")
	public Float getReno_money() {
		return reno_money;
	}

	public void setReno_money(Float reno_money) {
		this.reno_money = reno_money;
	}

	public Date getReno_time() {
		return reno_time;
	}

	public void setReno_time(Date reno_time) {
		this.reno_time = reno_time;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getReno_state() {
		return reno_state;
	}

	public void setReno_state(Integer reno_state) {
		this.reno_state = reno_state;
	}

	@Column(columnDefinition = "float(10,2) default '0.00'")
	public Float getReno_amoney() {
		return reno_amoney;
	}

	public void setReno_amoney(Float reno_amoney) {
		this.reno_amoney = reno_amoney;
	}

	public Date getReno_ctime() {
		return reno_ctime;
	}

	public void setReno_ctime(Date reno_ctime) {
		this.reno_ctime = reno_ctime;
	}

	public String getReno_remark() {
		return reno_remark;
	}

	public void setReno_remark(String reno_remark) {
		this.reno_remark = reno_remark;
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
	@JoinColumn(name = "cont_id")
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	@ManyToOne
	@JoinColumn(name = "prst_id")
	public ProjectStage getProjectStage() {
		return projectStage;
	}

	public void setProjectStage(ProjectStage projectStage) {
		this.projectStage = projectStage;
	}

	public Date getReno_wtime() {
		return reno_wtime;
	}

	public void setReno_wtime(Date reno_wtime) {
		this.reno_wtime = reno_wtime;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getReno_wday() {
		return reno_wday;
	}

	public void setReno_wday(Integer reno_wday) {
		this.reno_wday = reno_wday;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getReno_isdelete() {
		return reno_isdelete;
	}

	public void setReno_isdelete(Integer reno_isdelete) {
		this.reno_isdelete = reno_isdelete;
	}

	public Date getCont_stime() {
		return cont_stime;
	}

	public void setCont_stime(Date cont_stime) {
		this.cont_stime = cont_stime;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}
