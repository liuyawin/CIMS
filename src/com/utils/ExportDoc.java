/**
 * 
 */
package com.utils;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * @author zjn
 * @date 2016年11月17日
 */
public class ExportDoc {
	/**
	 * * 导出Word文件
	 * 
	 * @param destFile
	 *            目标文件路径
	 * 
	 * @param fileContent
	 *            要导出的文件内容
	 * 
	 */
	public static int exportDoc(String destFile, String fileContent) {
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));
			POIFSFileSystem fileSystem = new POIFSFileSystem();
			DirectoryEntry directory = fileSystem.getRoot();
			directory.createDocument("WordDocument", byteArrayInputStream);
			FileOutputStream fileOutputStream = new FileOutputStream(destFile);
			fileSystem.writeFilesystem(fileOutputStream);
			byteArrayInputStream.close();
			fileOutputStream.close();
			return 1;
		} catch (IOException e) {
			return 0;
		}
	}

	public static void main(String[] args) {
		exportDoc("E:\\wordTest.doc", "exportDoc导出Word");
	}
}
