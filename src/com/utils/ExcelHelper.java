package com.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mvc.entity.ProjectStatisticForm;

/**
 * Excel操作类
 * 
 * @author wangrui
 * @date 2016-11-15
 */
@SuppressWarnings("hiding")
public class ExcelHelper<T> {

	/**
	 * 导出2003版单sheet的Excel
	 * 
	 * @param headers
	 * @param list
	 * @param out
	 */
	public void export2003Excel(String title, String[] headers, Collection<T> list, OutputStream out, String pattern) {
		HSSFWorkbook workbook = new HSSFWorkbook();// 声明一个工作薄
		export2003Excel(workbook, title, headers, list, pattern);
		write2003Out(workbook, out);
	}

	/**
	 * 导出2003版多sheet的Excel
	 * 
	 * @param headers
	 * @param list
	 * @param out
	 * @param pattern
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void export2003MutiExcel(Map<Integer, String> titlesMap, Map<Integer, String[]> headerMap,
			Map<Integer, List> map, OutputStream out, String pattern) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		for (int i = 0; i < map.size(); i++) {
			Collection<T> list = map.get(i);
			String[] headers = headerMap.get(i);
			String title = titlesMap.get(i);
			export2003Excel(workbook, title, headers, list, pattern);
		}
		write2003Out(workbook, out);
	}

	/**
	 * 导出2007版单sheet的Excel
	 * 
	 * @param title
	 * @param headers
	 * @param list
	 * @param out
	 * @param pattern
	 */
	public void export2007Excel(String title, String[] headers, Collection<T> list, OutputStream out, String pattern,
			Integer mergeColumn) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		export2007Excel(workbook, title, headers, list, pattern, mergeColumn);
		write2007Out(workbook, out);
	}

	/**
	 * 导出2007版多sheet的Excel
	 * 
	 * @param titles
	 * @param headers
	 * @param map
	 * @param out
	 * @param pattern
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void export2007MutiExcel(Map<Integer, String> titlesMap, Map<Integer, String[]> headerMap,
			Map<Integer, List> map, OutputStream out, String pattern, Integer mergeColumn) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		for (int i = 0; i < map.size(); i++) {
			Collection<T> list = map.get(i);
			String[] headers = headerMap.get(i);
			String title = titlesMap.get(i);
			export2007Excel(workbook, title, headers, list, pattern, mergeColumn);
		}
		write2007Out(workbook, out);
	}

	/**
	 * 导出2003版Excel
	 * 
	 * @param title
	 *            * 表格标题名 *
	 * @param headers
	 *            表格属性列名数组
	 * @param list
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	private void export2003Excel(HSSFWorkbook workbook, String title, String[] headerSource, Collection<T> list,
			String pattern) {
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		Boolean flag = false;
		String[] headers = hidHeader(headerSource);
		if (headerSource.length - headers.length > 0) {
			flag = true;
		}
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 遍历集合数据，产生数据行
		Iterator<T> it = list.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fieldSource = t.getClass().getDeclaredFields();
			Field[] fields = null;
			if (flag) {
				fields = hidField(fieldSource, t.getClass());
			} else {
				fields = fieldSource;
			}
			for (short i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value instanceof Integer) {
						int intValue = (Integer) value;
						cell.setCellValue(intValue);
					} else if (value instanceof Float) {
						float fValue = (Float) value;
						cell.setCellValue(fValue);
					} else if (value instanceof Double) {
						double dValue = (Double) value;
						cell.setCellValue(dValue);
					} else if (value instanceof Long) {
						long longValue = (Long) value;
						cell.setCellValue(longValue);
					}
					if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value != null) {
						// 其它数据类型都当作字符串简单处理
						textValue = value.toString();
					} else if (value == null) {
						textValue = "";
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^//d+(//.//d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							HSSFFont font3 = workbook.createFont();
							font3.setColor(HSSFColor.BLUE.index);
							richString.applyFont(font3);
							cell.setCellValue(richString);
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}
		}
	}

	/**
	 * 导出2007版Excel
	 * 
	 * @param title
	 *            * 表格标题名 *
	 * @param headers
	 *            表格属性列名数组
	 * @param list
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void export2007Excel(XSSFWorkbook workbook, String title, String[] headerSource, Collection<T> list,
			String pattern, Integer mergeColumn) {
		XSSFSheet sheet = workbook.createSheet(title);
		// 生成样式
		XSSFCellStyle titleStyle = getTitleStyle(workbook);
		XSSFCellStyle headerStyle = getStyle(workbook, "header");
		XSSFCellStyle contentStyle = getStyle(workbook, "content");

		// 合并单元格，设置表名样式
		Row titleRow = sheet.createRow(0); // 创建一个行
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellStyle(titleStyle);
		titleCell.setCellValue(title);
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, headerSource.length-1));// 起始行
																					// ，结束行，起始列，结束列
		// 产生表格标题行
		XSSFRow row = sheet.createRow(3);// 从第三行开始生成表格
		row.setHeight((short) 500);// 行高设置成25px

		Boolean flag = false;
		String[] headers = hidHeader(headerSource);
		if (headerSource.length - headers.length > 0) {
			flag = true;
		}
		for (short i = 0; i < headers.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(headerStyle);
			XSSFRichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 遍历集合数据，产生数据行
		Iterator<T> it = list.iterator();
		int index = 3;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			row.setHeight((short) 400);// 行高设置成25px
			T t = (T) it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fieldSource = t.getClass().getDeclaredFields();
			Field[] fields = null;
			if (flag) {
				fields = hidField(fieldSource, t.getClass());
			} else {
				fields = fieldSource;
			}
			for (short i = 0; i < fields.length; i++) {
				XSSFCell cell = row.createCell(i);
				cell.setCellStyle(contentStyle);
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value instanceof Integer) {
						int intValue = (Integer) value;
						cell.setCellValue(intValue);
					} else if (value instanceof Float) {
						float fValue = (Float) value;
						cell.setCellValue(fValue);
					} else if (value instanceof Double) {
						double dValue = (Double) value;
						cell.setCellValue(dValue);
					} else if (value instanceof Long) {
						long longValue = (Long) value;
						cell.setCellValue(longValue);
					}
					if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value != null) {
						// 其它数据类型都当作字符串简单处理
						textValue = value.toString();
					} else if (value == null) {
						textValue = "";
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^//d+(//.//d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							XSSFRichTextString richString = new XSSFRichTextString(textValue);
							cell.setCellValue(richString);
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
				}
				sheet.autoSizeColumn((short) i);// 设置自动调整列宽
			}
		}
		// 第mergeColumn列相同数据合并单元格
		if (mergeColumn != -1) {
			addMergedRegion(sheet, mergeColumn, 4, sheet.getLastRowNum(), workbook);// 就是合并第一列的所有相同单元格
		}
	}

	/**
	 * 将输出流写入工作薄(2003版)
	 * 
	 * @param workbook
	 * @param out
	 */
	private void write2003Out(HSSFWorkbook workbook, OutputStream out) {
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将输出流写入工作薄(2007版)
	 * 
	 * @param workbook
	 * @param out
	 */
	private void write2007Out(XSSFWorkbook workbook, OutputStream out) {
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 去除需要隐藏的表头
	 * 
	 * @param headers
	 * @return
	 */
	private String[] hidHeader(String[] headers) {
		List<String> list = new ArrayList<String>();
		for (short i = 0; i < headers.length; i++) {
			if (!headers[i].contains("hidden")) {
				list.add(headers[i]);
			}
		}
		String[] arr = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = (String) list.get(i);
		}
		return arr;
	}

	/**
	 * 去除需要隐藏的字段（属性）
	 * 
	 * @param fieldSource
	 * @param cla
	 * @return
	 */
	private Field[] hidField(Field[] fields, Class<? extends Object> cla) {
		List<Field> list = new ArrayList<Field>();
		if (cla == ProjectStatisticForm.class) {// 需要隐藏的字段，并列加到这里
			String str = null;
			for (short i = 0; i < fields.length; i++) {
				str = fields[i].getName();
				if (!str.equals("cont_type") && !str.equals("cont_stime")) {
					list.add(fields[i]);
				}
			}
		}
		Field[] arr = null;
		if (list.size() > 0) {
			arr = new Field[list.size()];
			for (int i = 0; i < list.size(); i++) {
				arr[i] = (Field) list.get(i);
			}
		}
		return arr;
	}

	// 设置标题样式
	private XSSFCellStyle getTitleStyle(XSSFWorkbook workbook) {

		XSSFCellStyle style = workbook.createCellStyle();

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直对齐居中

		// 生成另一个字体
		XSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12); // 设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 字体加粗
		// 把字体应用到当前的样式
		style.setFont(font);

		return style;
	}

	// 设置表格样式
	private XSSFCellStyle getStyle(XSSFWorkbook workbook, String type) {

		XSSFCellStyle style = workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN); // 底部边框
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 底部边框颜色

		style.setBorderLeft(CellStyle.BORDER_THIN); // 左边边框
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边边框颜色

		style.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
		style.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 右边边框颜色

		style.setBorderTop(CellStyle.BORDER_THIN); // 上边边框
		style.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上边边框颜色

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直对齐居中

		// 生成另一个字体
		XSSFFont font = workbook.createFont();
		font.setFontName("宋体");

		switch (type) {
		case "header":
			font.setFontHeightInPoints((short) 10); // 设置字体大小
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 字体加粗
			break;
		case "content":
			font.setFontHeightInPoints((short) 10); // 设置字体大小
			break;
		default:
			break;
		}
		// 把字体应用到当前的样式
		style.setFont(font);
		return style;
	}

	/**
	 * 合并单元格
	 * 
	 * @param sheet
	 *            要合并单元格的excel 的sheet
	 * @param cellLine
	 *            要合并的列
	 * @param startRow
	 *            要合并列的开始行
	 * @param endRow
	 *            要合并列的结束行
	 */
	private static void addMergedRegion(XSSFSheet sheet, int cellLine, int startRow, int endRow,
			XSSFWorkbook workBook) {

		XSSFCellStyle style = workBook.createCellStyle(); // 样式对象

		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		// 获取第一行的数据,以便后面进行比较
		String s_will = sheet.getRow(startRow).getCell(cellLine).getStringCellValue();

		int count = 0;
		boolean flag = false;
		int merge_start_row = startRow;
		for (int i = startRow + 1; i <= endRow; i++) {
			String s_current = sheet.getRow(i).getCell(cellLine).getStringCellValue();
			if (s_will.equals(s_current)) {
				flag = true;
				count++;
			} else {
				if (flag) {
					CellRangeAddress cra = new CellRangeAddress(merge_start_row, merge_start_row + count, cellLine,
							cellLine);
					System.out.println("起始行=" + merge_start_row + "----终结行=" + (merge_start_row + count));
					sheet.addMergedRegion(cra);
				}
				flag = false;
				count = 0;
				merge_start_row = i;
			}
			s_will = s_current;

			// 由于上面循环中合并的单元放在有下一次相同单元格的时候做的，所以最后如果几行有相同单元格则要运行下面的合并单元格。
			if (i == endRow && count > 0) {
				CellRangeAddress cra = new CellRangeAddress(endRow - count, endRow, cellLine, cellLine);
				sheet.addMergedRegion(cra);
			}
		}
	}
}
