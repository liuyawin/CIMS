package com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

import java.lang.reflect.Method;
import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

public class WordHelper<T> {

	/**
	 * 导出2007版word（模板）
	 * 
	 * @param path
	 * @param list
	 * @param out
	 */
	public void export2007Word(String path, Collection<T> list, Map<String, Object> contentMap, OutputStream out,
			Integer tableOrder) {
		// 读取模板
		FileInputStream in = null;
		XWPFDocument doc = null;
		try {
			in = new FileInputStream(new File(path));
			doc = new XWPFDocument(in);
			// 替换模版中的变量
			replaceDoc(doc, contentMap);
			// 根据表头动态生成word表格(tableOrder:word模版中的第tableOrder张表格)
			dynamicWord(doc, list, tableOrder);

			write2007Out(doc, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据表头动态生成word表格
	 * 
	 * @param doc
	 * @param list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void dynamicWord(XWPFDocument doc, Collection<T> list, Integer tableOrder) {
		try {
			List<XWPFTable> tables = doc.getTables();
			XWPFTable table = tables.get(tableOrder);// 变量******
			XWPFTableRow row = table.getRow(0);
			List<BigInteger> widthList = new ArrayList<BigInteger>(); // 记录表格标题宽度
			List<XWPFTableCell> cells = row.getTableCells();// 表头
			XWPFTableCell cell = null;
			CTTcPr cellPr = null;
			int colNum = cells.size();
			for (int i = 0; i < colNum; i++) {
				cell = cells.get(i);
				cellPr = cell.getCTTc().getTcPr();
				BigInteger width = cellPr.getTcW().getW();// 获取单元格宽度
				widthList.add(width);
			}

			Iterator<T> it = list.iterator();
			while (it.hasNext()) {
				row = table.createRow();
				T t = (T) it.next();
				Field[] fields = t.getClass().getDeclaredFields();
				cells = row.getTableCells();
				for (int i = 0; i < colNum; i++) {
					Field field = fields[i];
					String fieldName = field.getName();
					String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					cell = cells.get(i);
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					if (value != null) {
						cell.setText(String.valueOf(value));// 写入单元格内容
					}
					cellPr = cell.getCTTc().addNewTcPr();
					cellPr.addNewTcW().setW(widthList.get(i));// 设置单元格宽度
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 替换word模板2007版
	 * 
	 * @param templatePath
	 * @param contentMap
	 * @return
	 */
	private void replaceDoc(XWPFDocument doc, Map<String, Object> contentMap) {
		// 替换段落里面的变量
		this.replaceInPara(doc, contentMap);// 注意2007poi中没有range
		// 替换表格里面的变量
		this.replaceInTable(doc, contentMap);
	}

	/**
	 * 替换段落里面的变量
	 * 
	 * @param doc
	 *            要替换的文档
	 * @param params
	 *            参数
	 */
	private void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
		Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
		XWPFParagraph para;
		while (iterator.hasNext()) {
			para = iterator.next();
			this.replaceInPara(para, params);
		}
	}

	/**
	 * 替换段落里面的变量
	 * 
	 * @param para
	 *            要替换的段落
	 * @param params
	 *            参数
	 */
	private void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
		List<XWPFRun> runs;
		Matcher matcher;
		if (this.matcher(para.getParagraphText()).find()) {
			runs = para.getRuns();
			for (int i = 0; i < runs.size(); i++) {
				XWPFRun run = runs.get(i);
				String runText = run.toString();
				matcher = this.matcher(runText);
				if (matcher.find()) {
					while ((matcher = this.matcher(runText)).find()) {
						runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
					}
					// 直接调用XWPFRun的setText()方法设置文本时，在底层会重新创建一个XWPFRun，把文本附加在当前文本后面，
					// 所以我们不能直接设值，需要先删除当前run,然后再自己手动插入一个新的run。
					para.removeRun(i);
					para.insertNewRun(i).setText(runText);
				}
			}
		}
	}

	/**
	 * 替换表格里面的变量
	 * 
	 * @param doc
	 *            要替换的文档
	 * @param params
	 *            参数
	 */
	private void replaceInTable(XWPFDocument doc, Map<String, Object> params) {
		Iterator<XWPFTable> iterator = doc.getTablesIterator();
		XWPFTable table;
		List<XWPFTableRow> rows;
		List<XWPFTableCell> cells;
		List<XWPFParagraph> paras;
		while (iterator.hasNext()) {
			table = iterator.next();
			rows = table.getRows();
			for (XWPFTableRow row : rows) {
				cells = row.getTableCells();
				for (XWPFTableCell cell : cells) {
					paras = cell.getParagraphs();
					for (XWPFParagraph para : paras) {
						this.replaceInPara(para, params);
					}
				}
			}
		}
	}

	/**
	 * 正则匹配字符串
	 * 
	 * @param str
	 * @return
	 */
	private Matcher matcher(String str) {
		Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		return matcher;
	}

	/**
	 * 将输出流写入word(2007版)
	 * 
	 * @param doc
	 * @param out
	 */
	private void write2007Out(XWPFDocument doc, OutputStream out) {
		try {
			doc.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
