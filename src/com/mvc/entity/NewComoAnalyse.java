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

	private String province;// 省份
	private Float como_one;// 第一年合同总金额
	private Float como_two;// 第二年合同总金额
	private Float ratio_one_provi;// 第一年该省合同占比
	private Float ratio_two_provi;// 第二年该省合同占比
	private Float rise_ratio;// 两年的合同增长比例
	private String remark;// 备注
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public Float getComo_one() {
		return como_one;
	}
	public void setComo_one(Float como_one) {
		this.como_one = como_one;
	}
	public Float getComo_two() {
		return como_two;
	}
	public void setComo_two(Float como_two) {
		this.como_two = como_two;
	}
	public Float getRatio_one_provi() {
		return ratio_one_provi;
	}
	public void setRatio_one_provi(Float ratio_one_provi) {
		this.ratio_one_provi = ratio_one_provi;
	}
	public Float getRatio_two_provi() {
		return ratio_two_provi;
	}
	public void setRatio_two_provi(Float ratio_two_provi) {
		this.ratio_two_provi = ratio_two_provi;
	}
	public Float getRise_ratio() {
		return rise_ratio;
	}
	public void setRise_ratio(Float rise_ratio) {
		this.rise_ratio = rise_ratio;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
