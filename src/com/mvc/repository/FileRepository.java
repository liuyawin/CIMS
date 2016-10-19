package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Files;

/**
 * 文件JPA
 * 
 * @author wangrui
 * @date 2016-10-14
 */
public interface FileRepository extends JpaRepository<Files, Integer> {

	// 根据文件ID获取文件
	@Query("select f from Files f where f.file_id=:file_id and f.file_isdelete=0")
	Files findFileById(@Param("file_id") Integer file_id);

}
