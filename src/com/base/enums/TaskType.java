/**
 * 
 */
package com.base.enums;

/**
 * 任务类型
 * 
 * @author zjn
 * @date 2016年9月30日
 */
public enum TaskType {

	// 0:普通任务；1文书任务；2：执行管控任务；3：其他任务
	normal(0), assistants(1), monitor(2), other(3);

	public int value;

	private TaskType(int value) {
		this.value = value;
	}
}
