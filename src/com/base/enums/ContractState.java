package com.base.enums;

/**
 * 合同状态
 * 
 * @author wangrui
 * @date 2016-11-07
 */
public enum ContractState {
	在建(0), 竣工(1), 停建(2);

	public int value;

	private ContractState(int value) {
		this.value = value;
	}

	public static String intToStr(int value) {
		ContractState cont_state = ContractState.values()[value];
		return cont_state.name();
	}
}
