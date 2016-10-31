package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.ProjectStage;

/**
 * 工期阶段JPA
 * 
 * @author wangrui
 * @date 2016-09-20
 */
public interface ProjectStageRepository extends JpaRepository<ProjectStage, Integer> {

	// 查询该合同的工期阶段
	@Query("select ps from ProjectStage ps where cont_id=:cont_id and prst_isdelete=0")
	List<ProjectStage> selectPrstByContId(@Param("cont_id") Integer cont_id);

}
