package com.mvc.controller;

import com.base.constants.PermissionConstants;

import net.sf.json.JSONObject;

public class Main {

	public static void main(String[] args) {
		jsonToArray();
	}

	public static void jsonToArray() {
		String permission = "";
		String[] strArr = null;
		String per = "{\"con_per\":[1,1,1,1,1,1,1,1,1,1],\"task_per\":[1,1,1,0,0,0,0,0,0,0],\"bill_per\":[1,1,0,0,0,0,0,0,0,0],\"system_per\":[1,1,1,1,1,1,0,0,0,0],\"alarm_per\":[1,1,1,1,1,0,0,0,0,0]}";
		JSONObject jsonObject = JSONObject.fromObject(per);
		// String contPer = jsonObject.getString("con_per");
		// System.out.println(contPer);
		strArr = StringToArray(jsonObject.getString("con_per"));
		for (int i = 0; i < strArr.length; i++) {
			if (strArr[i].equals("1")) {
				permission += " " + PermissionConstants.contPer[i];
			}
		}
		System.out.println(permission);
		// String taskPer = jsonObject.getString("task_per");
		// System.out.println(taskPer);
		strArr = StringToArray(jsonObject.getString("task_per"));
		for (int i = 0; i < strArr.length; i++) {
			if (strArr[i].equals("1")) {
				permission += " " + PermissionConstants.contPer[i];
			}
		}
		System.out.println(permission);

		// String billPer = jsonObject.getString("bill_per");
		// System.out.println(billPer);
		strArr = StringToArray(jsonObject.getString("bill_per"));
		for (int i = 0; i < strArr.length; i++) {
			if (strArr[i].equals("1")) {
				permission += " " + PermissionConstants.contPer[i];
			}
		}
		System.out.println(permission);
		// String systemPer = jsonObject.getString("system_per");
		strArr = StringToArray(jsonObject.getString("system_per"));
		for (int i = 0; i < strArr.length; i++) {
			if (strArr[i].equals("1")) {
				permission += " " + PermissionConstants.contPer[i];
			}
		}
		System.out.println(permission);
//		String alarmPer = jsonObject.getString("alarm_per");
//		System.out.println(alarmPer);
		strArr = StringToArray(jsonObject.getString("alarm_per"));
		for (int i = 0; i < strArr.length; i++) {
			if (strArr[i].equals("1")) {
				permission += " " + PermissionConstants.contPer[i];
			}
		}
		System.out.println(permission + " ");
	}

	private static String[] StringToArray(String str) {
		String subStr = str.substring(1, str.length() - 1);
		String strArr[] = subStr.split(",");
		return strArr;

	}
}
