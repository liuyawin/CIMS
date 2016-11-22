/**
 * 
 */
package com.base.constants;

/**
 * Session
 * 
 * @author zjn
 * @date 2016年9月9日
 */
public class SessionKeyConstants {

	/**
	 * 获取登陆后放入的用户模型的key
	 */
	public static String LOGIN = "user_login";

	/**
	 * 获取核销员登录后放入的用户模型的key
	 */
	public static String CHECKERLOGIN = "checker_login";

	/**
	 * 用于报表统计，查询时将年份存入session，导出时直接调用
	 */
	public static String BEGIN_YEAR = "begin_year";
	public static String END_YEAR = "end_year";
}
