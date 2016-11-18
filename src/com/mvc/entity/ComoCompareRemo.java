/**
 * 
 */
package com.mvc.entity;

/**
 * 合同额到款对比表
 * 
 * @author zjn
 * @date 2016年11月16日
 */
public class ComoCompareRemo {

	private String como_one;// 第一年合同总金额
	private String remo_one;// 第一年到款总金额
	private String cont_num_one;// 第一年合同总数

	private String como_two;// 第二年合同总金额
	private String remo_two;// 第二年到款总金额
	private String cont_num_two;// 第二年合同总数

	private String ratio_como;// 备注：合同额两年增长比率
	private String ratio_remo;// 备注：到款两年同比增长比率
	private String ratio_conum;// 备注：合同数两年同比增长比率

	public String getRatio_como() {
		return ratio_como;
	}

	public void setRatio_como(String ratio_como) {
		this.ratio_como = ratio_como;
	}

	public String getRatio_remo() {
		return ratio_remo;
	}

	public void setRatio_remo(String ratio_remo) {
		this.ratio_remo = ratio_remo;
	}

	public String getRatio_conum() {
		return ratio_conum;
	}

	public void setRatio_conum(String ratio_conum) {
		this.ratio_conum = ratio_conum;
	}

	public String getComo_one() {
		return como_one;
	}

	public void setComo_one(String como_one) {
		this.como_one = como_one;
	}

	public String getCont_num_one() {
		return cont_num_one;
	}

	public void setCont_num_one(String cont_num_one) {
		this.cont_num_one = cont_num_one;
	}

	public String getRemo_one() {
		return remo_one;
	}

	public void setRemo_one(String remo_one) {
		this.remo_one = remo_one;
	}

	public String getComo_two() {
		return como_two;
	}

	public void setComo_two(String como_two) {
		this.como_two = como_two;
	}

	public String getRemo_two() {
		return remo_two;
	}

	public void setRemo_two(String remo_two) {
		this.remo_two = remo_two;
	}

	public String getCont_num_two() {
		return cont_num_two;
	}

	public void setCont_num_two(String cont_num_two) {
		this.cont_num_two = cont_num_two;
	}

}
