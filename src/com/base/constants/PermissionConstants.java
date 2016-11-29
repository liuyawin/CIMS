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
	public static final String index = "indexPer";
	public static final String left = "leftPer";
	
	// 合同所有权限:{新建, 主任修改 ,文书修改, 删除, 修改状态, 添加工期阶段, 修改工期阶段 ,删除工期阶段, 添加收款节点, 修改收款节点,
	// 删除收款节点, 新建任务}
	public static final String[] contPer = { "cAdd", "cHeadEdit", "cBodyEdit", "cDel", "cStateEdit", "cPsAdd",
			"cPsEdit", "cPsDel", "cRnAdd", "cRnEdit", "cRnDel", "cTaskAdd" };

	// 任务所有权限:{补录合同, 审核发票 ,完成发票, 工期阶段完工}
	public static final String[] taskPer = { "tContCollect", "tInvoAudit", "tInvoFinish", "cPsFinish" };

	// 票据所有权限:{开发票 ,开收据 ,到款, 审核到款}
	public static final String[] billPer = { "bInvoAdd", "bReceAdd", "bRemoAdd", "tRemoAudit" };

	// 用户管理所有权限:{添加角色 ,删除角色, 修改角色, 添加用户, 删除用户, 修改用户, 添加报警设置 ,删除报警设置, 修改报警设置}
	public static final String[] systemPer = { "uRoleAdd", "uRoleDel", "uRoleEdit", "uUserAdd", "uUserDel", "uUserEdit",
			"uAlarmAdd", "uAlarmDel", "uAlarmEdit" };

	// 首页显示所有权限:{文书任务, 补录合同任务, 审核发票, 完成发票, 核对到款, 收款超时 ,工期超时}
	public static final String[] indexPer = { "iAssiTask", "iEditTask", "iAudiInvoTask", "iFiniInvoTask",
			"iFiniRemoTask", "iDebtAlarm", "iOverdueAlarm" };

	// 左侧功能栏所有权限:{合同管理, 票据管理, 用户管理，发票任务, 到款任务, 收款超时 ,工期超时,报表统计}
	public static final String[] leftPer = { "contManager", "billManager", "userManager", "invoiceTask", "remoTask",
			"remoAlarm", "psAlarm" ,"reportForm"};

}
