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
	public ComoCompareRemo findByDate(String oneDate, String twoDate) {

		List<Object> objectOne = contractDao.findByOneDate(oneDate);
		List<Object> objectTwo = contractDao.findByOneDate(twoDate);

		ComoCompareRemo comoCompareRemo = new ComoCompareRemo();
		Object[] objOne = (Object[]) objectOne.get(0);
		System.out.println("jieguo:" + objOne[2]);
		comoCompareRemo.setComo_one(objOne[0].toString());
		comoCompareRemo.setRemo_one(objOne[1].toString());
		comoCompareRemo.setCont_num_one(objOne[2].toString());

		Object[] objTwo = (Object[]) objectTwo.get(0);
		comoCompareRemo.setComo_two(objTwo[0].toString());
		comoCompareRemo.setRemo_two(objTwo[1].toString());
		comoCompareRemo.setCont_num_two(objTwo[2].toString());
		if (objOne[0].equals(0.0)) {
			comoCompareRemo.setRatio_como("---");
		} else {
			Double ratio_como = (Double) (((Double) objTwo[0] - (Double) objOne[0]) / (Double) objOne[0] * 100);
			if (ratio_como > 0)
				comoCompareRemo.setRatio_como("同比增长" + ratio_como + "%");
			else if (ratio_como < 0) {
				comoCompareRemo.setRatio_como("同比减少" + ratio_como + "%");
			} else {
				comoCompareRemo.setRatio_como("相等");
			}
		}
		if (objOne[1].equals(0.0)) {
			comoCompareRemo.setRatio_remo("---");
		} else {
			Double ratio_remo = (Double) (((Double) objTwo[1] - (Double) objOne[1]) / (Double) objOne[1] * 100);
			if (ratio_remo > 0)
				comoCompareRemo.setRatio_remo("同比增长" + ratio_remo + "%");
			else if (ratio_remo < 0) {
				comoCompareRemo.setRatio_remo("同比减少" + ratio_remo + "%");
			} else {
				comoCompareRemo.setRatio_remo("相等");
			}
		}
		if (objOne[2].toString().equals("0")) {
			comoCompareRemo.setRatio_conum("---");
		} else {
			Double ratio_conum = (Double) (((Double) objTwo[2] - (Double) objOne[2]) / (Double) objOne[2] * 100);
			if (ratio_conum > 0)
				comoCompareRemo.setRatio_conum("同比增长" + ratio_conum + "%");
			else if (ratio_conum < 0) {
				comoCompareRemo.setRatio_conum("同比减少" + ratio_conum + "%");
			} else {
				comoCompareRemo.setRatio_conum("相等");
			}
		}
		return comoCompareRemo;
	}

}
