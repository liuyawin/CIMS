package com.mvc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.enums.ContractState;
import com.mvc.dao.ContractDao;
import com.mvc.entity.ComoCompareRemo;
import com.mvc.entity.Contract;
import com.mvc.entity.PlanProjectForm;
import com.mvc.service.ReportFormService;

/**
 * 报表业务层实现
 * 
 * @author wangrui
 * @date 2016-11-15
 */
@Service("reportFormServiceImpl")
public class ReportFormServiceImpl implements ReportFormService {

	@Autowired
	ContractDao contractDao;

	// 光电院承担规划项目表
	@Override
	public List<PlanProjectForm> findPlanProject(Integer cont_state, Date startTime, Date endTime) {
		List<Contract> listSource = contractDao.findContByState(cont_state, startTime, endTime);
		List<PlanProjectForm> listGoal = new ArrayList<PlanProjectForm>();
		Iterator<Contract> it = listSource.iterator();
		int i = 0;
		while (it.hasNext()) {
			i++;
			Contract contract = it.next();
			PlanProjectForm planProjectForm = new PlanProjectForm();
			planProjectForm.setPlpr_id(i);// 序号
			planProjectForm.setCont_project(contract.getCont_project());// 项目名称
			if (contract.getManager() != null) {
				planProjectForm.setManager(contract.getManager().getUser_name());// 项目设总
			}
			// planProjectForm.setInstall_capacity();//装机容量（MW）
			String cont_stateStr = ContractState.intToStr(contract.getCont_state());
			planProjectForm.setCont_state(cont_stateStr);// 合同状态
			planProjectForm.setCont_money(contract.getCont_money());// 合同额(万元)
			planProjectForm.setCont_stime(contract.getCont_stime());// 签订时间
			listGoal.add(planProjectForm);
		}
		return listGoal;
	}

	// 根据日期获取合同额到款对比表
	@Override
	public ComoCompareRemo findByDate(Date oneDate, Date twoDate) {

		Object objectOne = contractDao.findByOneDate(oneDate);
		Object objectTwo = contractDao.findByOneDate(twoDate);

		ComoCompareRemo comoCompareRemo = new ComoCompareRemo();
		Object[] objOne = (Object[]) objectOne;
		comoCompareRemo.setComo_one((Float) objOne[0]);
		comoCompareRemo.setRemo_one((Float) objOne[1]);
		comoCompareRemo.setCont_num_one((Integer) objOne[2]);

		Object[] objTwo = (Object[]) objectTwo;
		comoCompareRemo.setComo_two((Float) objTwo[0]);
		comoCompareRemo.setRemo_two((Float) objTwo[1]);
		comoCompareRemo.setCont_num_two((Integer) objTwo[2]);

		Float ratio_como = (Float) (((Float) objTwo[0] - (Float) objOne[0]) / ((Float) objOne[0]) * 100);
		Float ratio_remo = (Float) (((Float) objTwo[1] - (Float) objOne[1]) / ((Float) objOne[1]) * 100);
		Float ratio_conum = (Float) (((Float) objTwo[2] - (Float) objOne[2]) / ((Float) objOne[2]) * 100);

		comoCompareRemo.setRatio_como(ratio_como + "%");
		comoCompareRemo.setRatio_remo(ratio_remo + "%");
		comoCompareRemo.setRatio_conum(ratio_conum + "%");
		return comoCompareRemo;
	}

}
