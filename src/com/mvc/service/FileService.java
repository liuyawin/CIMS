package com.mvc.service;

import java.util.List;

import com.mvc.entity.Files;

/**
 * 文件管理业务
 * 
 * @author wangrui
 * @date 2016-10-14
 */
public interface FileService {

	// 添加文件
	Boolean addFile(Files file);

	// 根据合同ID获取文件列表
	List<Files> findFileByConId(Integer cont_id);

	// 根据文件ID获取文件
	Files findFileById(Integer file_id);

	// 根据合同ID删除合同
	Boolean deleteById(Integer file_id);
}
