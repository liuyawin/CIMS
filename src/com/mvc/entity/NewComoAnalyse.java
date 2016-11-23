/**
 * 
 */
package com.mvc.entity;

/**
 * 新签合同额分析表
 * 
 * @author zjn
 * @date 2016年11月17日
 */
public class NewComoAnalyse {
	private String order_number;// 序号
	private String province;// 省份
	private String como_one;// 第一年该省合同总金额
	private String como_two;// 第二年该省合同总金额
	private String ratio_one_provi;// 第一年该省合同占比
	private String ratio_two_provi;// 第二年该省合同占比
	private String rise_ratio;// 两年的合同增长比例
	private String remark;// 备注

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getComo_one() {
		return como_one;
	}

	public void setComo_one(String como_one) {
		this.como_one = como_one;
	}

	public String getComo_two() {
		return como_two;
	}

	public void setComo_two(String como_two) {
		this.como_two = como_two;
	}

	public String getRatio_one_provi() {
		return ratio_one_provi;
	}

	public void setRatio_one_provi(String ratio_one_provi) {
		this.ratio_one_provi = ratio_one_provi;
	}

	public String getRatio_two_provi() {
		return ratio_two_provi;
	}

	public void setRatio_two_provi(String ratio_two_provi) {
		this.ratio_two_provi = ratio_two_provi;
	}

	public String getRise_ratio() {
		return rise_ratio;
	}

	public void setRise_ratio(String rise_ratio) {
		this.rise_ratio = rise_ratio;
	}
	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

}
