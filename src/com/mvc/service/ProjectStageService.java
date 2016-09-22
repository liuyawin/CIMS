package com.mvc.service;

import java.util.List;

import com.mvc.entity.ProjectStage;

/**
 * 工期阶段业务层接口
 * 
 * @author wangrui
 * @date 2016-09-20
 */
public interface ProjectStageService {

	// 添加工期阶段
	Boolean addProjectStage(ProjectStage projectStage);

	// 查询该合同的工期阶段
	List<ProjectStage> selectPrstByContId(Integer cont_id);

	// 根据ID查询工期阶段
	ProjectStage selectPrstById(Integer prst_id);
}
