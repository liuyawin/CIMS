package com.mvc.entity;

/**
 * 光伏自营项目催款计划表（2015年前签订项目）
 * 
 * @author wanghuimin
 * @date 2016年11月17日
 */

public class PaymentPlanListForm {

	private String order_num;// 序号
	private String province;// 行政区域
	private String cont_project;// 工程名称 && 项目名称
	private String cont_client;// 业主名称 && 业主公司名称
	private String cont_money;// 合同金额
	private String remo_totalmoney;// 2015年累计已到款
	private String balance_money;// 余额
	private String invo_totalmoney;// 已开发票金额
	private String invo_not_totalmoney;// 未开发票金额

	private String plan_payment;// 计划可催收额
	private String actual_money;// 实际到款
	private String con_clause;// 合同条款
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

	public String getCont_money() {
		return cont_money;
	}

	public void setCont_money(String cont_money) {
		this.cont_money = cont_money;
	}

	public String getRemo_totalmoney() {
		return remo_totalmoney;
	}

	public void setRemo_totalmoney(String remo_totalmoney) {
		this.remo_totalmoney = remo_totalmoney;
	}

	public String getBalance_money() {
		return balance_money;
	}

	public void setBalance_money(String balance_money) {
		this.balance_money = balance_money;
	}

	public String getInvo_totalmoney() {
		return invo_totalmoney;
	}

	public void setInvo_totalmoney(String invo_totalmoney) {
		this.invo_totalmoney = invo_totalmoney;
	}

	public String getInvo_not_totalmoney() {
		return invo_not_totalmoney;
	}

	public void setInvo_not_totalmoney(String invo_not_totalmoney) {
		this.invo_not_totalmoney = invo_not_totalmoney;
	}

	public String getPlan_payment() {
		return plan_payment;
	}

	public void setPlan_payment(String plan_payment) {
		this.plan_payment = plan_payment;
	}

	public String getActual_money() {
		return actual_money;
	}

	public void setActual_money(String actual_money) {
		this.actual_money = actual_money;
	}

	public String getCon_clause() {
		return con_clause;
	}

	public void setCon_clause(String con_clause) {
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

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

}
