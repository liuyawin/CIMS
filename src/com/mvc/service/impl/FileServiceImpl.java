package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.FileDao;
import com.mvc.entity.Files;
import com.mvc.repository.FileRepository;
import com.mvc.service.FileService;

/**
 * 文件管理业务实现
 * 
 * @author wangrui
 * @date 2016-10-14
 */
@Service("fileServiceImpl")
public class FileServiceImpl implements FileService {

	@Autowired
	FileRepository fileRepository;
	@Autowired
	FileDao fileDao;

	// 添加文件
	@Override
	public Boolean addFile(Files file) {
		Files result = fileRepository.saveAndFlush(file);
		if (result.getFile_id() != null)
			return true;
		else
			return false;
	}

	// 根据合同ID获取文件列表
	@Override
	public List<Files> findFileByConId(Integer cont_id) {
		return fileDao.findFileByConId(cont_id);
	}

	// 根据文件ID获取文件
	@Override
	public Files findFileById(Integer file_id) {
		return fileRepository.findFileById(file_id);
	}

	// 根据合同ID删除合同
	@Override
	public Boolean deleteById(Integer file_id) {
		return fileDao.delete(file_id);
	}

}
