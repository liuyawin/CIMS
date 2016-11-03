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

	public static final String contract = "contPer";
	public static final String task = "taskPer";
	public static final String bill = "billPer";
	public static final String system = "systemPer";
	public static final String index = "alarmPer";

	// 合同所有权限
	public static final String[] contPer = { "cAdd", "cHeadEdit", "cBodyEdit", "cDel", "cStateEdit", "cPsAdd",
			"cPsEdit", "cPsDel", "cRnAdd", "cRnEdit", "cRnDel", "cTaskAdd" };

	// 任务所有权限
	public static final String[] taskPer = { "tContCollect", "tInvoAudit", "tInvoFinish", "tRemoAudit" };

	// 票据所有权限
	public static final String[] billPer = { "bInvoAdd", "bReceAdd", "bRemoAdd" };

	// 用户管理所有权限
	public static final String[] systemPer = { "uRoleAdd", "uRoleDel", "uRoleEdit", "uUserAdd",  "uUserDel", "uUserEdit",
			"uAlarmAdd", "uAlarmDel", "uAlarmDel" };

	// 首页显示所有权限
	public static final String[] indexPer = { "iAssiTask", "iEditTask", "iCommTask", "iAudiInvoTask", "iFiniInvoTask",
			"iFiniRemoTask", "iDebtAlarm", "iOverdueAlarm", "iTaskAlarm" };

}
