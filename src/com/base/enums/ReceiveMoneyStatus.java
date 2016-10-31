/**
 * 
 */
package com.base.enums;

/**
 * 收款状态
 * 
 * @author zjn
 * @date 2016年10月28日
 */
public enum ReceiveMoneyStatus {

	// -1：全部；0：未核对；1：已核对
	all(-1), waitAudit(0), finish(1);

	public int value;

	private ReceiveMoneyStatus(int value) {
		this.value = value;
	}
}
