/**
 * 
 */
package com.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

/**
 * 读取word模板并替换变量
 * 
 * @author zjn
 * @date 2016年11月17日
 */
public class ReplaceDoc {
	/**
	 * * 读取word模板并替换变量
	 * 
	 * @param templatePath
	 *            模板路径
	 * @param contentMap
	 *            要替换的内容
	 * @return word的Document
	 */

	public static HWPFDocument replaceDoc(String templatePath, Map<String, String> contentMap) {
		try {
			// 读取模板
			FileInputStream tempFileInputStream = new FileInputStream(new File(templatePath));
			HWPFDocument document = new HWPFDocument(tempFileInputStream);
			// 读取文本内容
			Range bodyRange = document.getRange();
			// 替换内容
			for (Map.Entry<String, String> entry : contentMap.entrySet()) {
				bodyRange.replaceText("${" + entry.getKey() + "}", entry.getValue());
			}
			return document;
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {

		Map<String, String> contentMap = new HashMap<String, String>();
		contentMap.put("name", "飞翔家族");
		contentMap.put("age", "123");
		contentMap.put("email", "1231231231@123.com");
		HWPFDocument document = replaceDoc("E:\\template.doc", contentMap);
		if (document != null) {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				document.write(byteArrayOutputStream);
				OutputStream outputStream = new FileOutputStream("E:\\wordTest.doc");
				outputStream.write(byteArrayOutputStream.toByteArray());
				outputStream.close();
			} catch (IOException e) {
			}
		}
	}
}
