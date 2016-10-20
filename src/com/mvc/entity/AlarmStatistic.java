package com.mvc.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 报警统计
 * 
 * @author wangrui
 * @date 2016-10-20
 */
@Entity
@Table(name = "alarm_statistic")
public class AlarmStatistic implements Serializable {

	private static final long serialVersionUID = -1705098726238981603L;

	private Integer alst_id;// ID
	private Integer totalReceiveTaskNum;// 当前用户接收的所有任务
	private Integer waitAuditBillTaskNum;// 待审核发票任务
	private Integer assistantTaskNum;// 文书任务
	private Integer managerControlTaskNum;// 执行管控任务
	private Integer billTaskNum;// 发票任务
	private Integer otherTaskNum;// 其他任务
	private Integer debtAlarmNum;// 收款超时
	private Integer overdueAlarmNum;// 工程逾期
	private Integer taskAlarmNum;// 任务超时

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getAlst_id() {
		return alst_id;
	}

	public void setAlst_id(Integer alst_id) {
		this.alst_id = alst_id;
	}

	public Integer getTotalReceiveTaskNum() {
		return totalReceiveTaskNum;
	}

	public void setTotalReceiveTaskNum(Integer totalReceiveTaskNum) {
		this.totalReceiveTaskNum = totalReceiveTaskNum;
	}

	public Integer getWaitAuditBillTaskNum() {
		return waitAuditBillTaskNum;
	}

	public void setWaitAuditBillTaskNum(Integer waitAuditBillTaskNum) {
		this.waitAuditBillTaskNum = waitAuditBillTaskNum;
	}

	public Integer getAssistantTaskNum() {
		return assistantTaskNum;
	}

	public void setAssistantTaskNum(Integer assistantTaskNum) {
		this.assistantTaskNum = assistantTaskNum;
	}

	public Integer getManagerControlTaskNum() {
		return managerControlTaskNum;
	}

	public void setManagerControlTaskNum(Integer managerControlTaskNum) {
		this.managerControlTaskNum = managerControlTaskNum;
	}

	public Integer getBillTaskNum() {
		return billTaskNum;
	}

	public void setBillTaskNum(Integer billTaskNum) {
		this.billTaskNum = billTaskNum;
	}

	public Integer getOtherTaskNum() {
		return otherTaskNum;
	}

	public void setOtherTaskNum(Integer otherTaskNum) {
		this.otherTaskNum = otherTaskNum;
	}

	public Integer getDebtAlarmNum() {
		return debtAlarmNum;
	}

	public void setDebtAlarmNum(Integer debtAlarmNum) {
		this.debtAlarmNum = debtAlarmNum;
	}

	public Integer getOverdueAlarmNum() {
		return overdueAlarmNum;
	}

	public void setOverdueAlarmNum(Integer overdueAlarmNum) {
		this.overdueAlarmNum = overdueAlarmNum;
	}

	public Integer getTaskAlarmNum() {
		return taskAlarmNum;
	}

	public void setTaskAlarmNum(Integer taskAlarmNum) {
		this.taskAlarmNum = taskAlarmNum;
	}

}
