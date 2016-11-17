package com.base.enums;

/**
 * 合同状态
 * 
 * @author wangrui
 * @date 2016-11-17
 */
public enum ContStatus {

	未立项(0), 已立项_合同未签(1), 已签订(2);

	public int value;

	private ContStatus(int value) {
		this.value = value;
	}

	public static String intToStr(int value) {
		ContStatus cont_status = ContStatus.values()[value];
		return cont_status.name();
	}
}
