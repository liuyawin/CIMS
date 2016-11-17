package com.utils;

/**
 * 字符串工具类
 * 
 * @author wangrui
 * @date 2016-11-17
 */
public class StringUtil {

	public static Boolean strIsNotEmpty(String s) {
		Boolean flag = true;
		if (s == null || s.length() <= 0) {
			flag = false;
		}
		return flag;
	}
}
