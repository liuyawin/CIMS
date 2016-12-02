/**
 * 
 */
package com.mvc.entity;

/**
 * 合同数量规模汇总
 * 
 * @author zjn
 * @date 2016年12月2日
 */
public class ContNumAcaleSummary {
	private String order_num;// 序号
	private String province;// 区域
	private String guangfu;// 光伏类
	private String guangdian;// 光电类
	private String fenbu;// 分布式
	private String other;// 其他

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getGuangfu() {
		return guangfu;
	}

	public void setGuangfu(String guangfu) {
		this.guangfu = guangfu;
	}

	public String getGuangdian() {
		return guangdian;
	}

	public void setGuangdian(String guangdian) {
		this.guangdian = guangdian;
	}

	public String getFenbu() {
		return fenbu;
	}

	public void setFenbu(String fenbu) {
		this.fenbu = fenbu;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

}
