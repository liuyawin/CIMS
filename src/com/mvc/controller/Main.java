package com.mvc.controller;

import net.sf.json.JSONObject;

public class Main {

	public static void main(String[] args) {
		jsonToArray();
	}

	public static void jsonToArray() {
		String per = "{\"con_per\":[1,1,1,1,1,1,1,1,1,1],\"task_per\":[1,1,1,1,1,0,0,0,0,0],\"bill_per\":[1,1,1,1,1,0,0,0,0,0],\"system_per\":[1,1,1,1,1,0,0,0,0,0],\"alarm_per\":[1,1,1,1,1,0,0,0,0,0]}";
		// String str =
		// "[[\"name\",\"专业a\"],[\"notice\",\"专业B\"],[\"purchase\",\"专业C\"]]";
		// String str = "[[1,1,1],[1,1,0],[1,2,0]]";
		JSONObject jsonObject = JSONObject.fromObject(per);

		String contPer = jsonObject.getString("con_per");
		System.out.println(contPer);
		// JSONArray a = (JSONArray) o;
		// for (int i = 0; i < a.size(); i++) {
		// Integer j = (Integer) a.get(i);
		// System.out.println(j);
		// }
		String taskPer = jsonObject.getString("task_per");
		System.out.println(taskPer);
		String billPer = jsonObject.getString("bill_per");
		System.out.println(billPer);
		String systemPer = jsonObject.getString("system_per");
		System.out.println(systemPer);
		String alarmPer = jsonObject.getString("alarm_per");
		System.out.println(alarmPer);
		// JSONArray arr = JSONArray.fromObject(str);
		// for (Object o : arr) {
		// JSONArray a = (JSONArray) o;
		// for (int i = 0; i < a.size(); i++) {
		// Integer j = (Integer) a.get(i);
		// System.out.println(j);
		// }
		// }
	}
}
