package com.base.enums;

/**
 * 收款节点状态
 * 
 * @author zjn
 * @date 2016年9月30日
 */
public enum RenoStatus {
	// 0：未付款；1：未付全款；2：已付款；3：多付款
	waitReceive(0), noEnough(1), finish(2), beyondActually(3);

	public int value;

	private RenoStatus(int value) {
		this.value = value;
	}
}
