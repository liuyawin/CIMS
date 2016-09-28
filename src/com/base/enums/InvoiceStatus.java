package com.base.enums;

/**
 * 发票状态
 * 
 * @author zjn
 * @date 2016年9月27日
 */
public enum InvoiceStatus {

	// 发票状态,0:待审核，1:待处理，2:已完成
	waitAudit(0), waitdealing(1), finish(2);

	public int value;

	private InvoiceStatus(int value) {
		this.value = value;
	}
}
