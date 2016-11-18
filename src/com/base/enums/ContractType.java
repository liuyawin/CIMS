package com.base.enums;

/**
 * 合同类型
 * 
 * @author wangrui
 * @date 2016-09-17
 *
 */
public enum ContractType {

	传统光伏项目(0), 分布式(1), 光热(2), 其他(3);

	public int value;

	private ContractType(int value) {
		this.value = value;
	}
	
	public static String intToStr(int value) {
		ContractType cont_type = ContractType.values()[value];
		return cont_type.name();
	}
}
