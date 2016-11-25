package com.utils;

import java.math.BigDecimal;

public class DoubleFloatUtil {

	/**
	 * * 相加 *
	 * 
	 * @param s1
	 *            *
	 * @param s2
	 *            *
	 * @return String
	 */
	public static String add(String s1, String s2) {
		BigDecimal b1 = new BigDecimal(s1);
		BigDecimal b2 = new BigDecimal(s2);
		return b1.add(b2).toString();
	}

	/**
	 * * 相减 *
	 * 
	 * @param s1
	 *            *
	 * @param s2
	 *            *
	 * @return String
	 */
	public static String sub(String s1, String s2) {
		BigDecimal b1 = new BigDecimal(s1);
		BigDecimal b2 = new BigDecimal(s2);
		return b1.subtract(b2).toString();
	}

}
