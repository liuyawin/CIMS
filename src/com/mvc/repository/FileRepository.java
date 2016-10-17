package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.entity.Files;

/**
 * 文件JPA
 * 
 * @author wangrui
 * @date 2016-10-14
 */
public interface FileRepository extends JpaRepository<Files, Integer> {

}
