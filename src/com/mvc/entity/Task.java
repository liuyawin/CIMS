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
 * 任务
 * 
 * @author wanghuimin
 * @date 2016年9月8日
 */
@Entity
@Table(name = "task")
public class Task {
	private Integer task_id;// ID
	private Date task_ctime;// 任务创建时间
	private Date task_stime;// 开始时间
	private Date task_etime;// 截止时间
	private Date Task_atime;// 实际完成时间
	private String task_content;// 内容
	private Integer task_state;// 任务状态,0：未接收，1：执行中，2：已完成
	private Integer task_alarmnum;// 报警次数
	private Integer task_isdelete;// 是否删除任务,0：未删除，1：已删除
	private Integer task_type;// 任务类型, 0：普通任务，1：文书任务，2其他
	private String task_remark;// 备注
	private User creator;
	private User receiver;
	private Contract contract;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getTask_id() {
		return task_id;
	}

	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}

	public Date getTask_ctime() {
		return task_ctime;
	}

	public void setTask_ctime(Date task_ctime) {
		this.task_ctime = task_ctime;
	}

	public Date getTask_stime() {
		return task_stime;
	}

	public void setTask_stime(Date task_stime) {
		this.task_stime = task_stime;
	}

	public Date getTask_etime() {
		return task_etime;
	}

	public void setTask_etime(Date task_etime) {
		this.task_etime = task_etime;
	}

	public Date getTask_atime() {
		return Task_atime;
	}

	public void setTask_atime(Date task_atime) {
		Task_atime = task_atime;
	}

	public String getTask_content() {
		return task_content;
	}

	public void setTask_content(String task_content) {
		this.task_content = task_content;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getTask_state() {
		return task_state;
	}

	public void setTask_state(Integer task_state) {
		this.task_state = task_state;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getTask_isdelete() {
		return task_isdelete;
	}

	public void setTask_isdelete(Integer task_isdelete) {
		this.task_isdelete = task_isdelete;
	}

	public String getTask_remark() {
		return task_remark;
	}

	public void setTask_remark(String task_remark) {
		this.task_remark = task_remark;
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
	@JoinColumn(name = "receiver_id")
	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	@ManyToOne
	@JoinColumn(name = "cont_id")
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getTask_alarmnum() {
		return task_alarmnum;
	}

	public void setTask_alarmnum(Integer task_alarmnum) {
		this.task_alarmnum = task_alarmnum;
	}

	@Column(length = 1)
	public Integer getTask_type() {
		return task_type;
	}

	public void setTask_type(Integer task_type) {
		this.task_type = task_type;
	}

}
