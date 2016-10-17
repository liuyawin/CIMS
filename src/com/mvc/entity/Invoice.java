package com.mvc.entity;

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
 * 发票
 * 
 * @author wanghuimin
 * @date 2016年9月8日
 */
@Entity
@Table(name = "invoice")
public class Invoice {
	private Integer invo_id;// ID
	private Integer invo_state;// 发票状态,0:待审核，1:待处理，2:已完成
	private Float invo_money;// 金额
	private String invo_firm;// 业主公司名称
	private Date invo_stime;// 开始时间
	private Date invo_etime;// 截止时间
	private String invo_num;// 编号（税号）
	private String invo_name;// 名称
	private Date invo_time;// 开票时间
	private Integer invo_alarmnum;// 报警次数
	private String invo_remark;// 备注
	private Date invo_ctime;// 任务创建时间
	private Date invo_atime;// 实际完成时间
	private Integer invo_isdelete;// 是否删除(1：删除,0：未删除)
	private User creator;// 创建者
	private User audit;// 审核者
	private User receiver;// 负责人
	private Contract contract;// 所属合同

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getInvo_id() {
		return invo_id;
	}

	public void setInvo_id(Integer invo_id) {
		this.invo_id = invo_id;
	}

	@Column(columnDefinition = "float(10,2) default '0.00'")
	public Float getInvo_money() {
		return invo_money;
	}

	public void setInvo_money(Float invo_money) {
		this.invo_money = invo_money;
	}

	@Column(length = 64)
	public String getInvo_firm() {
		return invo_firm;
	}

	public void setInvo_firm(String invo_firm) {
		this.invo_firm = invo_firm;
	}

	public Date getInvo_stime() {
		return invo_stime;
	}

	public void setInvo_stime(Date invo_stime) {
		this.invo_stime = invo_stime;
	}

	public Date getInvo_etime() {
		return invo_etime;
	}

	public void setInvo_etime(Date invo_etime) {
		this.invo_etime = invo_etime;
	}

	@Column(length = 32)
	public String getInvo_num() {
		return invo_num;
	}

	public void setInvo_num(String invo_num) {
		this.invo_num = invo_num;
	}

	@Column(length = 64)
	public String getInvo_name() {
		return invo_name;
	}

	public void setInvo_name(String invo_name) {
		this.invo_name = invo_name;
	}

	public Date getInvo_time() {
		return invo_time;
	}

	public void setInvo_time(Date invo_time) {
		this.invo_time = invo_time;
	}

	public String getInvo_remark() {
		return invo_remark;
	}

	public void setInvo_remark(String invo_remark) {
		this.invo_remark = invo_remark;
	}

	public Date getInvo_ctime() {
		return invo_ctime;
	}

	public void setInvo_ctime(Date invo_ctime) {
		this.invo_ctime = invo_ctime;
	}

	public Date getInvo_atime() {
		return invo_atime;
	}

	public void setInvo_atime(Date invo_atime) {
		this.invo_atime = invo_atime;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getInvo_isdelete() {
		return invo_isdelete;
	}

	public void setInvo_isdelete(Integer invo_isdelete) {
		this.invo_isdelete = invo_isdelete;
	}

	@ManyToOne
	@JoinColumn(name = "creator_id")
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@ManyToOne
	@JoinColumn(name = "audit_id")
	public User getAudit() {
		return audit;
	}

	public void setAudit(User audit) {
		this.audit = audit;
	}

	@ManyToOne
	@JoinColumn(name = "receiver_id")
	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	@ManyToOne
	@JoinColumn(name = "contract_id")
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getInvo_state() {
		return invo_state;
	}

	public void setInvo_state(Integer invo_state) {
		this.invo_state = invo_state;
	}

	public Integer getInvo_alarmnum() {
		return invo_alarmnum;
	}

	public void setInvo_alarmnum(Integer invo_alarmnum) {
		this.invo_alarmnum = invo_alarmnum;
	}

}
