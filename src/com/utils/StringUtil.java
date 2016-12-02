package com.utils;

/**
 * 字符串工具类
 * 
 * @author wangrui
 * @date 2016-11-17
 */
public class StringUtil {

	/**
	 * 判读字符串是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static Boolean strIsNotEmpty(String s) {
		Boolean flag = true;
		if (s == null || s.length() <= 0) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 4舍5入保留2位小数
	 * 
	 * @param fl
	 * @return
	 */
	public static Float save2Float(Float fl) {
		Float result = 0.0f;
		if (fl != null) {
			String str = String.format("%.2f", fl);
			result = Float.valueOf(str);
		}
		return result;
	}
}
