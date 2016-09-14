package com.mvc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 子任务
 * 
 * @author wanghuimin
 * @date 2016年9月8日
 */
@Entity
@Table(name = "sub_task")
public class SubTask {
	private Integer suta_id;// ID
	private String suta_content;// 任务创建内容
	private Integer suta_state;// 任务状态,0:未完成，1已完成
	private String suta_remark;// 备注
	private Task task;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getSuta_id() {
		return suta_id;
	}

	public void setSuta_id(Integer suta_id) {
		this.suta_id = suta_id;
	}

	public String getSuta_content() {
		return suta_content;
	}

	public void setSuta_content(String suta_content) {
		this.suta_content = suta_content;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getSuta_state() {
		return suta_state;
	}

	public void setSuta_state(Integer suta_state) {
		this.suta_state = suta_state;
	}

	public String getSuta_remark() {
		return suta_remark;
	}

	public void setSuta_remark(String suta_remark) {
		this.suta_remark = suta_remark;
	}

	@ManyToOne
	@JoinColumn(name = "task_id")
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
