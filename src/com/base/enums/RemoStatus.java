/**
 * 
 */
package com.base.enums;

/**
 * 到款状态
 * 
 * @author zjn
 * @date 2016年11月01日
 */
public enum RemoStatus {
	// 0：未核对；1：已核对
	waitDeal(0), finish(1);

	public int value;

	private RemoStatus(int value) {
		this.value = value;
	}
}
