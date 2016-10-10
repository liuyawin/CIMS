package com.base.enums;

/**
 * 任务状态
 * 
 * @author zjn
 * @date 2016年9月13日
 */
public enum TaskStatus {
	// 0:待接收；1：处理中；2：已完成
	waitingReceipt(0), dealing(1), finish(2);

	public int value;

	private TaskStatus(int value) {
		this.value = value;
	}

}
