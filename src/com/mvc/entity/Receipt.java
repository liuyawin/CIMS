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
 * 收据
 * 
 * @author wanghuimin
 * @date 2016年9月8日
 */
@Entity
@Table(name = "receipt")
public class Receipt {
	private Integer rece_id;// Id
	private String rece_firm;// 交款业主公司
	private Float rece_money;// 收款金额
	private Date rece_time;// 应收款时间
	private Date rece_atime;// 实际收款时间
	private String rece_remark;// 备注
	private User user;// 开收据者
	private Contract contract;// 所属合同
	private ReceiveNode receiveNode;// 收款节点
	private Integer rece_isdelete;// 是否删除，0表示：未删除，1表示已删除

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getRece_id() {
		return rece_id;
	}

	@Column(length = 64)
	public void setRece_id(Integer rece_id) {
		this.rece_id = rece_id;
	}

	public String getRece_firm() {
		return rece_firm;
	}

	public void setRece_firm(String rece_firm) {
		this.rece_firm = rece_firm;
	}

	@Column(columnDefinition = "float(10,2) default '0.00'")
	public Float getRece_money() {
		return rece_money;
	}

	public void setRece_money(Float rece_money) {
		this.rece_money = rece_money;
	}

	public Date getRece_time() {
		return rece_time;
	}

	public void setRece_time(Date rece_time) {
		this.rece_time = rece_time;
	}

	public String getRece_remark() {
		return rece_remark;
	}

	public void setRece_remark(String rece_remark) {
		this.rece_remark = rece_remark;
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
	@JoinColumn(name = "reno_id")
	public ReceiveNode getReceiveNode() {
		return receiveNode;
	}

	public void setReceiveNode(ReceiveNode receiveNode) {
		this.receiveNode = receiveNode;
	}

	public Date getRece_atime() {
		return rece_atime;
	}

	public void setRece_atime(Date rece_atime) {
		this.rece_atime = rece_atime;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getRece_isdelete() {
		return rece_isdelete;
	}

	public void setRece_isdelete(Integer rece_isdelete) {
		this.rece_isdelete = rece_isdelete;
	}

}
