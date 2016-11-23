package com.mvc.entity;

/**
 * 光伏自营项目催款计划表（2015年前签订项目）
 * 
 * @author wanghuimin
 * @date 2016年11月17日
 */

public class PaymentPlanListForm {
	private String province;// 行政区域
	private String cont_project;// 工程名称 && 项目名称
	private String cont_client;// 业主名称 && 业主公司名称
	private Float cont_money;// 合同金额
	private Float remo_totalmoney;// 2015年累计已到款
	private Float balance_money;// 余额
	private Float invo_totalmoney;// 已开发票金额
	private Float invo_not_totalmoney;// 未开发票金额

	private Float plan_payment;// 计划可催收额
	private Float actual_money;// 实际到款
	private Float con_clause;// 合同条款
	private String result;// 催款结果
	private String remark;// 备注

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCont_project() {
		return cont_project;
	}

	public void setCont_project(String cont_project) {
		this.cont_project = cont_project;
	}

	public String getCont_client() {
		return cont_client;
	}

	public void setCont_client(String cont_client) {
		this.cont_client = cont_client;
	}

	public Float getCont_money() {
		return cont_money;
	}

	public void setCont_money(Float cont_money) {
		this.cont_money = cont_money;
	}

	public Float getRemo_totalmoney() {
		return remo_totalmoney;
	}

	public void setRemo_totalmoney(Float remo_totalmoney) {
		this.remo_totalmoney = remo_totalmoney;
	}

	public Float getBalance_money() {
		return balance_money;
	}

	public void setBalance_money(Float balance_money) {
		this.balance_money = balance_money;
	}

	public Float getInvo_totalmoney() {
		return invo_totalmoney;
	}

	public void setInvo_totalmoney(Float invo_totalmoney) {
		this.invo_totalmoney = invo_totalmoney;
	}

	public Float getInvo_not_totalmoney() {
		return invo_not_totalmoney;
	}

	public void setInvo_not_totalmoney(Float invo_not_totalmoney) {
		this.invo_not_totalmoney = invo_not_totalmoney;
	}

	public Float getPlan_payment() {
		return plan_payment;
	}

	public void setPlan_payment(Float plan_payment) {
		this.plan_payment = plan_payment;
	}

	public Float getActual_money() {
		return actual_money;
	}

	public void setActual_money(Float actual_money) {
		this.actual_money = actual_money;
	}

	public Float getCon_clause() {
		return con_clause;
	}

	public void setCon_clause(Float con_clause) {
		this.con_clause = con_clause;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
