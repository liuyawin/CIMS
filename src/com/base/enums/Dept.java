/**
 * 
 */
package com.base.enums;

/**
 * 部门
 * 
 * @author zjn
 * @date 2016年11月8日
 */
public enum Dept {
	zonghebu(0), shejibu(1);

	public int value;

	private Dept(int value) {
		this.value = value;
	}
}
