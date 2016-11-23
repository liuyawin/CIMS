/**
 * 
 */
package com.base.enums;

/**
 * @author zjn
 * @date 2016年11月23日
 */
public enum ConExecStatus {
	// 子任务类型,0:待办，1:已印刷，待签字，2:已签字，待盖章，3:已盖章，待邮寄，4：已邮寄，待返回，5：已签
	waitdeal(0), print(1), sign(2), seal(3), post(4), file(5);

	public int value;

	private ConExecStatus(int value) {
		this.value = value;
	}

}
