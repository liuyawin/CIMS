/**
 * 
 */
package com.base.enums;

/**
 * 根据不同Id解除报警
 * 
 * @author zjn
 * @date 2016年9月30日
 */
public enum RemoveType {

	// 0:解除工期阶段报警，1:解除任务报警，2:解除收款节点报警
	PrstAlarm(0), TaskAlarm(1), RenoAlarm(2);

	public int value;

	private RemoveType(int value) {
		this.value = value;
	}
}
