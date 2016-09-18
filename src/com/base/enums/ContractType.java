package com.base.enums;

/**
 * 合同类型
 * 
 * @author wangrui
 * @date 2016-09-17
 *
 */
public enum ContractType {

	规划(0), 可行性(1), 施工图(2), 评估(3), 其他(4);

	public int value;

	private ContractType(int value) {
		this.value = value;
	}
}
