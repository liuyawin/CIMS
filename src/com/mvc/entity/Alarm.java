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
 * 报警
 * 
 * @author wangrui
 * @date 2016-09-08
 */
@Entity
@Table(name = "alarm")
public class Alarm implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer alar_id;// ID
	private Date alar_time;// 报警时间
	private String alar_content; // 报警内容
	private String alar_code;// 报警类型，1任务报警，2收款提醒，3收款超时，4工程逾期，4工程超时；
	private Integer alar_isremove;// 报警解除标志，0-未解除，1-解除
	private User user;// fk外键，报警接收人
	private Contract contract;// fk外键，所属合同
	private Task task;// 任务Id
	private ReceiveNode receiveNode;// 收款节点
	private ProjectStage projectStage;// 工期阶段
	private Integer num;// 去重后的报警次数

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getAlar_id() {
		return alar_id;
	}

	public void setAlar_id(Integer alar_id) {
		this.alar_id = alar_id;
	}

	public Date getAlar_time() {
		return alar_time;
	}

	public void setAlar_time(Date alar_time) {
		this.alar_time = alar_time;
	}

	public String getAlar_content() {
		return alar_content;
	}

	public void setAlar_content(String alar_content) {
		this.alar_content = alar_content;
	}

	@Column(length = 32)
	public String getAlar_code() {
		return alar_code;
	}

	public void setAlar_code(String alar_code) {
		this.alar_code = alar_code;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getAlar_isremove() {
		return alar_isremove;
	}

	public void setAlar_isremove(Integer alar_isremove) {
		this.alar_isremove = alar_isremove;
	}

	@ManyToOne
	@JoinColumn(name = "receiver_id")
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
	@JoinColumn(name = "task_id")
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@ManyToOne
	@JoinColumn(name = "reno_id")
	public ReceiveNode getReceiveNode() {
		return receiveNode;
	}

	public void setReceiveNode(ReceiveNode receiveNode) {
		this.receiveNode = receiveNode;
	}

	@ManyToOne
	@JoinColumn(name = "prst_id")
	public ProjectStage getProjectStage() {
		return projectStage;
	}

	public void setProjectStage(ProjectStage projectStage) {
		this.projectStage = projectStage;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}
