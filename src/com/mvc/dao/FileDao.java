package com.mvc.dao;

import java.util.List;

import com.mvc.entity.Files;

/**
 * 文件持久层
 * 
 * @author wangrui
 * @date 2016-10-18
 */
public interface FileDao {

	// 根据合同ID获取文件列表
	List<Files> findFileByConId(Integer cont_id);

	// 根据合同ID删除合同
	Boolean delete(Integer file_id);
}
