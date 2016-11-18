package com.mvc.service.impl;

import java.math.BigInteger;
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
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		List<Object> objectOne = contractDao.findByOneDate(oneDate);
		List<Object> objectTwo = contractDao.findByOneDate(twoDate);
		ComoCompareRemo comoCompareRemo = new ComoCompareRemo();
		// 获取第一年相关数据
		Object[] objOne = (Object[]) objectOne.get(0);
		if (objOne[0].equals(0.0)) {
			comoCompareRemo.setComo_one("---");
		} else {
			comoCompareRemo.setComo_one(df.format(Double.valueOf(objOne[0].toString())));
		}
		if (objOne[1].equals(0.0)) {
			comoCompareRemo.setRemo_one("---");
		} else {
			comoCompareRemo.setRemo_one(df.format(Double.valueOf(objOne[1].toString())));
		}
		comoCompareRemo.setCont_num_one(objOne[2].toString());
		// 获取第二年相关数据
		Object[] objTwo = (Object[]) objectTwo.get(0);
		if (objTwo[0].equals(0.0)) {
			comoCompareRemo.setComo_two("---");
		} else {
			comoCompareRemo.setComo_two(df.format(Double.valueOf(objTwo[0].toString())));
		}
		if (objTwo[1].equals(0.0)) {
			comoCompareRemo.setRemo_two("---");
		} else {
			comoCompareRemo.setRemo_two(df.format(Double.valueOf(objTwo[1].toString())));
		}
		comoCompareRemo.setCont_num_two(objTwo[2].toString());
		// 计算同比增长率
		if (objOne[0].equals(0.0)) {
			comoCompareRemo.setRatio_como("---");
		} else {
			Double big = Double.valueOf(objTwo[0].toString());
			Double small = Double.valueOf(objOne[0].toString());
			Double ratio_como = (big - small) / small * 100;
			String ratio = String.format("%.2f", ratio_como);
			if (ratio_como > 0) {
				comoCompareRemo.setRatio_como("同比增长" + ratio + "%");
			} else if (ratio_como < 0) {
				comoCompareRemo.setRatio_como("同比减少" + ratio + "%");
			} else {
				comoCompareRemo.setRatio_como("相等");
			}
		}
		if (objOne[1].equals(0.0)) {
			comoCompareRemo.setRatio_remo("---");
		} else {
			Double big = Double.valueOf(objTwo[1].toString());
			Double small = Double.valueOf(objOne[1].toString());
			Double ratio_remo = (big - small) / small * 100;
			String ratio = String.format("%.2f", ratio_remo);
			if (ratio_remo > 0)
				comoCompareRemo.setRatio_remo("同比增长" + ratio + "%");
			else if (ratio_remo < 0) {
				comoCompareRemo.setRatio_remo("同比减少" + ratio + "%");
			} else {
				comoCompareRemo.setRatio_remo("相等");
			}
		}
		if (objOne[2].equals(0)) {
			comoCompareRemo.setRatio_conum("---");
		} else {
			Double big = Double.valueOf(objTwo[2].toString());
			Double small = Double.valueOf(objOne[2].toString());
			Double ratio_conum = (big - small) / small * 100;
			String ratio = String.format("%.2f", ratio_conum);
			if (ratio_conum > 0)
				comoCompareRemo.setRatio_conum("同比增长" + ratio + "%");
			else if (ratio_conum < 0) {
				comoCompareRemo.setRatio_conum("同比减少" + ratio + "%");
			} else {
				comoCompareRemo.setRatio_conum("相等");
			}
		}
		return comoCompareRemo;
	}
}
