package com.mvc.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.ProjectStageDao;
import com.mvc.entity.Contract;
import com.mvc.entity.ProjectStage;
import com.mvc.entity.User;
import com.mvc.repository.ContractRepository;
import com.mvc.repository.ProjectStageRepository;
import com.mvc.repository.UserRepository;
import com.mvc.service.ProjectStageService;

import net.sf.json.JSONObject;

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
	@Autowired
	ContractRepository contractRepository;
	@Autowired
	UserRepository userRepository;

	// 添加工期阶段
	@Override
	public Boolean addProjectStage(JSONObject jsonObject, Integer cont_id, User user) {
		ProjectStage projectStage = new ProjectStage();
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			long time = System.currentTimeMillis();
			if (jsonObject.containsKey("prst_content")) {
				projectStage.setPrst_content(jsonObject.getString("prst_content"));// 阶段内容
			}
			if (jsonObject.containsKey("prst_etime")) {
				Date date = format.parse(jsonObject.getString("prst_etime"));// 阶段截止时间
				projectStage.setPrst_etime(date);// 阶段截止时间
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				int days = 0;
				if (jsonObject.containsKey("prst_wday")) {
					days = Integer.parseInt(jsonObject.getString("prst_wday"));// 完工提醒天数
				}
				projectStage.setPrst_wday(days);// 添加完工提醒的天数
				calendar.add(Calendar.DAY_OF_MONTH, -days);// 工作结束提醒时间=阶段截止时间-完工提醒天数
				projectStage.setPrst_wtime(calendar.getTime());// 工作结束提醒时间
			}

			projectStage.setPrst_ctime(new Date(time));// 阶段录入时间
			projectStage.setPrst_state(0);// 默认未完成
			projectStage.setUser(user);
			projectStage.setPrst_isdelete(0);// 默认未删除
			Contract contract = contractRepository.selectContById(cont_id);// 所属合同
			projectStage.setContract(contract);

			if (contract.getManager() != null) {// 先判断是否有项目经理
				User manager = userRepository.findById(contract.getManager().getUser_id());// 项目经理已经包含在合同里面
				projectStage.setManager(manager);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 写入数据库
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

	// 修改工期阶段
	@Override
	public Boolean updatePrst(JSONObject jsonObject, Integer prst_id) {
		ProjectStage projectStage = projectStageRepository.findOne(prst_id);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (jsonObject != null) {
			try {
				if (jsonObject.containsKey("prst_content")) {
					projectStage.setPrst_content(jsonObject.getString("prst_content"));// 工期阶段内容
				}
				Date date = null;
				if (jsonObject.containsKey("prst_etime")) {
					date = format.parse(jsonObject.getString("prst_etime"));// 阶段截止时间
					projectStage.setPrst_etime(date);
				} else {
					date = projectStage.getPrst_etime();
				}
				if (jsonObject.containsKey("prst_wday")) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					int days = Integer.parseInt(jsonObject.getString("prst_wday"));// 完工提醒天数
					projectStage.setPrst_wday(days);// 添加完工提醒的天数
					calendar.add(Calendar.DAY_OF_MONTH, -days);// 工作结束提醒时间=阶段截止时间-完工提醒天数
					projectStage.setPrst_wtime(calendar.getTime());// 工作结束提醒时间
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		projectStage = projectStageRepository.saveAndFlush(projectStage);
		if (projectStage.getPrst_id() != null)
			return true;
		else
			return false;
	}

}
