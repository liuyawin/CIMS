package com.utils;

import java.io.ByteArrayInputStream;
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
import java.util.Map.Entry;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

public class WordHelper<T> {

	/**
	 * 导出2007版word（模板）
	 * 
	 * @param path
	 * @param listMap
	 * @param contentMap
	 * @param out
	 */
	@SuppressWarnings("unchecked")
	public void export2007Word(String path, Map<String, Object> listMap, Map<String, Object> contentMap,
			OutputStream out) {
		// 读取模板
		FileInputStream in = null;
		XWPFDocument doc = null;
		try {
			in = new FileInputStream(new File(path));
			doc = new XWPFDocument(in);

			// 替换模版中的变量(包含添加图片)
			generateWord(doc, contentMap);

			// 解析map中的多个list，并根据表头动态生成word表格
			Iterator<Entry<String, Object>> it = listMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = it.next();
				Integer tableOrder = Integer.valueOf(entry.getKey());
				Object val = entry.getValue();
				Collection<T> list = (Collection<T>) val;
				// 根据表头动态生成word表格(tableOrder:word模版中的第tableOrder张表格)
				dynamicWord(doc, list, tableOrder);
			}
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
	 * 根据指定的参数值、模板，生成 word 文档
	 * 
	 * @param param
	 *            需要替换的变量
	 * @param template
	 *            模板
	 */
	private XWPFDocument generateWord(XWPFDocument doc, Map<String, Object> param) {
		try {
			if (param != null && param.size() > 0) {

				// 处理段落
				List<XWPFParagraph> paragraphList = doc.getParagraphs();
				processParagraphs(paragraphList, param, doc);

				// 处理表格
				Iterator<XWPFTable> it = doc.getTablesIterator();
				while (it.hasNext()) {
					XWPFTable table = it.next();
					List<XWPFTableRow> rows = table.getRows();
					for (XWPFTableRow row : rows) {
						List<XWPFTableCell> cells = row.getTableCells();
						for (XWPFTableCell cell : cells) {
							List<XWPFParagraph> paragraphListTable = cell.getParagraphs();
							processParagraphs(paragraphListTable, param, doc);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 处理段落（多个图片）
	 * 
	 * @param paragraphList
	 */
	@SuppressWarnings("unchecked")
	private void processParagraphs(List<XWPFParagraph> paragraphList, Map<String, Object> param, XWPFDocument doc) {
		if (paragraphList != null && paragraphList.size() > 0) {
			for (int m = 0; m < paragraphList.size(); m++) {
				XWPFParagraph paragraph = paragraphList.get(m);
				List<XWPFRun> runs = paragraph.getRuns();
				for (int i = 0; i < runs.size(); i++) {
					XWPFRun run = runs.get(i);
					String text = run.getText(0);
					if (text != null) {
						boolean isSetText = false;
						for (Entry<String, Object> entry : param.entrySet()) {
							String key = entry.getKey();
							if (text.indexOf(key) != -1) {
								isSetText = true;
								Object value = entry.getValue();
								if (value instanceof String) {// 文本替换
									text = text.replace(key, value.toString());
								} else if (value instanceof Map) {// 图片替换
									text = text.replace(key, "");
									Map<String, Object> pic = (Map<String, Object>) value;
									int width = Integer.parseInt(pic.get("width").toString());
									int height = Integer.parseInt(pic.get("height").toString());
									int picType = getPictureType(pic.get("type").toString());
									byte[] byteArray = (byte[]) pic.get("content");
									ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
									try {
										String blipId = doc.addPictureData(byteInputStream, picType);
										createPicture(doc, blipId, width, height, paragraph);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
						if (isSetText) {
							run.setText(text, 0);
						}
					}
				}
			}
		}
	}

	/**
	 * 根据图片类型，取得对应的图片类型代码
	 * 
	 * @param picType
	 * @return int
	 */
	private int getPictureType(String picType) {
		int res = XWPFDocument.PICTURE_TYPE_PICT;
		if (picType != null) {
			if (picType.equalsIgnoreCase("png")) {
				res = XWPFDocument.PICTURE_TYPE_PNG;
			} else if (picType.equalsIgnoreCase("dib")) {
				res = XWPFDocument.PICTURE_TYPE_DIB;
			} else if (picType.equalsIgnoreCase("emf")) {
				res = XWPFDocument.PICTURE_TYPE_EMF;
			} else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")) {
				res = XWPFDocument.PICTURE_TYPE_JPEG;
			} else if (picType.equalsIgnoreCase("wmf")) {
				res = XWPFDocument.PICTURE_TYPE_WMF;
			}
		}
		return res;
	}

	/**
	 * word图片配置
	 * 
	 * @param doc
	 * @param blipId
	 * @param width
	 * @param height
	 * @param paragraph
	 */
	private void createPicture(XWPFDocument doc, String blipId, int width, int height, XWPFParagraph paragraph) {
		int id = doc.getAllPictures().size() - 1;
		final int EMU = 9525;
		width *= EMU;
		height *= EMU;
		CTInline inline = paragraph.createRun().getCTR().addNewDrawing().addNewInline();
		String picXml = "" + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
				+ "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
				+ "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
				+ "         <pic:nvPicPr>" + "            <pic:cNvPr id=\"" + id + "\" name=\"Generated\"/>"
				+ "            <pic:cNvPicPr/>" + "         </pic:nvPicPr>" + "         <pic:blipFill>"
				+ "            <a:blip r:embed=\"" + blipId
				+ "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
				+ "            <a:stretch>" + "               <a:fillRect/>" + "            </a:stretch>"
				+ "         </pic:blipFill>" + "         <pic:spPr>" + "            <a:xfrm>"
				+ "               <a:off x=\"0\" y=\"0\"/>" + "               <a:ext cx=\"" + width + "\" cy=\""
				+ height + "\"/>" + "            </a:xfrm>" + "            <a:prstGeom prst=\"rect\">"
				+ "               <a:avLst/>" + "            </a:prstGeom>" + "         </pic:spPr>"
				+ "      </pic:pic>" + "   </a:graphicData>" + "</a:graphic>";

		inline.addNewGraphic().addNewGraphicData();
		XmlToken xmlToken = null;
		try {
			xmlToken = XmlToken.Factory.parse(picXml);
		} catch (XmlException xe) {
			xe.printStackTrace();
		}
		inline.set(xmlToken);

		inline.setDistT(0);
		inline.setDistB(0);
		inline.setDistL(0);
		inline.setDistR(0);

		CTPositiveSize2D extent = inline.addNewExtent();
		extent.setCx(width);
		extent.setCy(height);

		CTNonVisualDrawingProps docPr = inline.addNewDocPr();
		docPr.setId(id);
		docPr.setName("图片" + id);
		docPr.setDescr("");
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
