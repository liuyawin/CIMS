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
	private Integer total_receive_task_num;// 当前用户接收的所有任务
	private Integer wait_audit_bill_task_num;// 待审核发票任务
	private Integer assistant_task_num;// 文书任务
	private Integer manager_control_task_num;// 执行管控任务
	private Integer bill_task_num;// 发票任务
	private Integer other_task_num;// 其他任务
	private Integer debt_alarm_num;// 收款超时
	private Integer overdue_alarm_num;// 工程逾期
	private Integer task_alarm_num;// 任务超时
	private Integer remo_task_num;// 到款任务

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getAlst_id() {
		return alst_id;
	}

	public void setAlst_id(Integer alst_id) {
		this.alst_id = alst_id;
	}

	public Integer getTotal_receive_task_num() {
		return total_receive_task_num;
	}

	public void setTotal_receive_task_num(Integer total_receive_task_num) {
		this.total_receive_task_num = total_receive_task_num;
	}

	public Integer getWait_audit_bill_task_num() {
		return wait_audit_bill_task_num;
	}

	public void setWait_audit_bill_task_num(Integer wait_audit_bill_task_num) {
		this.wait_audit_bill_task_num = wait_audit_bill_task_num;
	}

	public Integer getAssistant_task_num() {
		return assistant_task_num;
	}

	public void setAssistant_task_num(Integer assistant_task_num) {
		this.assistant_task_num = assistant_task_num;
	}

	public Integer getManager_control_task_num() {
		return manager_control_task_num;
	}

	public void setManager_control_task_num(Integer manager_control_task_num) {
		this.manager_control_task_num = manager_control_task_num;
	}

	public Integer getBill_task_num() {
		return bill_task_num;
	}

	public void setBill_task_num(Integer bill_task_num) {
		this.bill_task_num = bill_task_num;
	}

	public Integer getOther_task_num() {
		return other_task_num;
	}

	public void setOther_task_num(Integer other_task_num) {
		this.other_task_num = other_task_num;
	}

	public Integer getDebt_alarm_num() {
		return debt_alarm_num;
	}

	public void setDebt_alarm_num(Integer debt_alarm_num) {
		this.debt_alarm_num = debt_alarm_num;
	}

	public Integer getOverdue_alarm_num() {
		return overdue_alarm_num;
	}

	public void setOverdue_alarm_num(Integer overdue_alarm_num) {
		this.overdue_alarm_num = overdue_alarm_num;
	}

	public Integer getTask_alarm_num() {
		return task_alarm_num;
	}

	public void setTask_alarm_num(Integer task_alarm_num) {
		this.task_alarm_num = task_alarm_num;
	}

	public Integer getRemo_task_num() {
		return remo_task_num;
	}

	public void setRemo_task_num(Integer remo_task_num) {
		this.remo_task_num = remo_task_num;
	}

}
