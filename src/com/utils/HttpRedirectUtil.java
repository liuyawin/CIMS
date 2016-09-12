/**
 * 
 */
package com.utils;

/**
 * 专门对response封装的Util
 * 
 * @author zjn
 * @date 2016年9月12日
 */
public class HttpRedirectUtil {
	public static String redirectStr(String page) {
		return "redirect:" + page;
	}
}
