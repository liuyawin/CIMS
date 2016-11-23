/**
 * 
 */
package com.mvc.entity;

/**
 * 项目到款分析表
 * 
 * @author zjn
 * @date 2016年11月17日
 */
public class NewRemoAnalyse {
	private String order_number;// 序号
	private String province;// 省份
	private String remo_one;// 第一年该省合同到款总金额
	private String remo_two;// 第二年该省合同到款总金额
	private String remo_before;// 第二年之前签订合同第二年中到款总金额
	private String remo_curr;// 第二年新签合同第二年到款总金额
	private String exp_remo_two_curr;// 第二年签订合同下一年预计到款额
	private String exp_remo_two_before;// 第二年以前签订合同下一年预计到款额
	private String remark;// 备注

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRemo_one() {
		return remo_one;
	}

	public void setRemo_one(String remo_one) {
		this.remo_one = remo_one;
	}

	public String getRemo_two() {
		return remo_two;
	}

	public void setRemo_two(String remo_two) {
		this.remo_two = remo_two;
	}

	public String getRemo_before() {
		return remo_before;
	}

	public void setRemo_before(String remo_before) {
		this.remo_before = remo_before;
	}

	public String getRemo_curr() {
		return remo_curr;
	}

	public void setRemo_curr(String remo_curr) {
		this.remo_curr = remo_curr;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExp_remo_two_curr() {
		return exp_remo_two_curr;
	}

	public void setExp_remo_two_curr(String exp_remo_two_curr) {
		this.exp_remo_two_curr = exp_remo_two_curr;
	}

	public String getExp_remo_two_before() {
		return exp_remo_two_before;
	}

	public void setExp_remo_two_before(String exp_remo_two_before) {
		this.exp_remo_two_before = exp_remo_two_before;
	}
}
