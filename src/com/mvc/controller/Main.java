package com.mvc.controller;

import com.base.constants.PermissionConstants;

import net.sf.json.JSONObject;

public class Main {

	public static void main(String[] args) {
		jsonToArray();
	}

	public static void jsonToArray() {
		String per = "{\"con_per\":[1,1,1,1,1,1,1,1,1,1],\"task_per\":[1,1,1,0,0,0,0,0,0,0],\"bill_per\":[1,1,0,0,0,0,0,0,0,0],\"system_per\":[1,1,1,1,1,1,0,0,0,0],\"alarm_per\":[1,1,1,1,1,0,0,0,0,0]}";

		JSONObject jsonObject = JSONObject.fromObject(per);

		String contPer = jsonObject.getString("con_per");
		System.out.println(contPer);
		String contPer11 = contPer.substring(1, contPer.length() - 1);
		String con_per[] = contPer11.split(",");
		for (int i = 0; i < con_per.length; i++) {
			if (con_per[i].equals("1")) {
				con_per[i] = PermissionConstants.contPer[i];
				System.out.println(con_per[i]);
			}
		}

		String taskPer = jsonObject.getString("task_per");
		System.out.println(taskPer);
		String billPer = jsonObject.getString("bill_per");
		System.out.println(billPer);
		String systemPer = jsonObject.getString("system_per");
		System.out.println(systemPer);
		String alarmPer = jsonObject.getString("alarm_per");
		System.out.println(alarmPer);
	}
}
