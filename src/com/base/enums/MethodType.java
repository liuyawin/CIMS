package com.base.enums;

/**
 * 合同方法类别
 * 
 * @author wangrui
 * @date 2016-11-03
 *
 */
public enum MethodType {
	// 1-合同信息管理，2-欠款合同信息，3-工程逾期合同，4-终结合同信息，5-停建合同
	contractList(1), debtContract(2), overdueContract(3), finishedContract(4), stopContract(5);

	public int value;

	private MethodType(int value) {
		this.value = value;
	}
}
