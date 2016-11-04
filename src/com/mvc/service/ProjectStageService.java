package com.mvc.service;

import java.util.List;

import com.mvc.entity.ProjectStage;
import com.mvc.entity.User;

import net.sf.json.JSONObject;

/**
 * 工期阶段业务层接口
 * 
 * @author wangrui
 * @date 2016-09-20
 */
public interface ProjectStageService {

	// 添加工期阶段
	Boolean addProjectStage(JSONObject jsonObject, Integer cont_id, User user);

	// 查询该合同的工期阶段
	List<ProjectStage> selectPrstByContId(Integer cont_id);

	// 根据ID查询工期阶段
	ProjectStage selectPrstById(Integer prst_id);

	// 修改成完成工期
	Boolean updatePrstState(Integer prst_id);

	// 删除工期
	Boolean deletePrstState(Integer prst_id);

	// 修改工期阶段
	Boolean updatePrst(JSONObject jsonObject, Integer prst_id);
}
