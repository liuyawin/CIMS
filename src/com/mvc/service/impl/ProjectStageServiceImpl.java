package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.ProjectStageDao;
import com.mvc.entity.ProjectStage;
import com.mvc.repository.ProjectStageRepository;
import com.mvc.service.ProjectStageService;

/**
 * 工期阶段业务层实现
 * 
 * @author wangrui
 * @date 2016-09-20
 */
@Service("projectStageServiceImpl")
public class ProjectStageServiceImpl implements ProjectStageService {

	@Autowired
	ProjectStageRepository projectStageRepository;
	@Autowired
	ProjectStageDao projectStageDao;

	// 添加工期阶段
	@Override
	public Boolean addProjectStage(ProjectStage projectStage) {
		ProjectStage result = projectStageRepository.saveAndFlush(projectStage);
		if (result.getPrst_id() != null)
			return true;
		else
			return false;
	}

	// 查询该合同的工期阶段
	@Override
	public List<ProjectStage> selectPrstByContId(Integer cont_id) {
		return projectStageRepository.selectPrstByContId(cont_id);
	}

	// 根据ID查询工期阶段
	@Override
	public ProjectStage selectPrstById(Integer prst_id) {
		return projectStageRepository.findOne(prst_id);
	}

	// 修改成完成工期
	@Override
	public Boolean updatePrstState(Integer prst_id) {
		return projectStageDao.updatePrstState(prst_id);
	}

	// 删除工期
	@Override
	public Boolean deletePrstState(Integer prst_id) {
		return projectStageDao.deletePrstState(prst_id);
	}

}
