package com.base.enums;

/**
 * 合同类型
 * 
 * @author wangrui
 * @date 2016-09-17
 *
 */
public enum IsDelete {

	NO(0), YES(1);

	public int value;

	private IsDelete(int value) {
		this.value = value;
	}
}
