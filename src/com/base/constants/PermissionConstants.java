/**
 * 
 */
package com.base.constants;

/**
 * 权限初始化常量
 * 
 * @author zjn
 * @date 2016年10月28日
 */
public class PermissionConstants {

	// 合同所有权限
	public static final String[] contPer = { "cAdd", "cHeadEdit", "cBodyEdit", "cPsAdd", "cPsEdit", "cPsDel", "cRnAdd",
			"cRnEdit", "cRnDel", "bReceAdd", "cDel", "cTaskAdd" };

	// 任务所有权限
	public static final String[] taskPer = { "tContCollect", "bInvoAdd", "tInvoAudit", "tInvoFinish" };

	// 票据所有权限
	public static final String[] billPer = { "bInvoAdd", "bReceAdd" };

	// 用户管理所有权限
	public static final String[] systemPer = { "uRoleAdd", "uRoleDel", "uRoleEdit", "uUserAdd", "uUserDel", "uUserEdit",
			"uAlarmAdd", "uAlarmDel", "uAlarmDel" };

}
