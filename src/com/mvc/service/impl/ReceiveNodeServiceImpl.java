package com.mvc.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.enums.RemoveType;
import com.base.enums.RenoStatus;
import com.mvc.dao.ReceiveNodeDao;
import com.mvc.entity.Contract;
import com.mvc.entity.ProjectStage;
import com.mvc.entity.ReceiveNode;
import com.mvc.entity.User;
import com.mvc.repository.ContractRepository;
import com.mvc.repository.ProjectStageRepository;
import com.mvc.repository.ReceiveNodeRepository;
import com.mvc.service.AlarmService;
import com.mvc.service.ReceiveNodeService;

import net.sf.json.JSONObject;

/**
 * 收款节点业务层实现
 * 
 * @author wangrui
 * @date 2016-09-20
 */
@Service("receiveNodeServiceImpl")
public class ReceiveNodeServiceImpl implements ReceiveNodeService {

	@Autowired
	ReceiveNodeRepository receiveNodeRepository;
	@Autowired
	ReceiveNodeDao receiveNodeDao;
	@Autowired
	AlarmService alarmService;
	@Autowired
	ContractRepository contractRepository;
	@Autowired
	ProjectStageRepository projectStageRepository;

	// 添加收款节点
	@Override
	public Boolean addReceiveNode(JSONObject jsonObject, Integer cont_id, User user) {
		ReceiveNode receiveNode = new ReceiveNode();
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			long time = System.currentTimeMillis();
			if (jsonObject.containsKey("reno_time")) {
				Date date = format.parse(jsonObject.getString("reno_time"));// 收款截止时间
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				receiveNode.setReno_time(date);// 节点截止时间
				int days = 0;
				if (jsonObject.containsKey("reno_wday")) {
					days = Integer.parseInt(jsonObject.getString("reno_wday"));// 收款提醒天数
				}
				receiveNode.setReno_wday(days);// 添加收款提醒的天数
				calendar.add(Calendar.DAY_OF_MONTH, -days);// 收款结束提醒时间=收款截止时间-收款提醒天数
				receiveNode.setReno_wtime(calendar.getTime());// 收款结束提醒时间
			}
			if (jsonObject.containsKey("reno_content")) {
				receiveNode.setReno_content(jsonObject.getString("reno_content"));// 节点内容
			}
			if (jsonObject.containsKey("reno_money")) {
				receiveNode.setReno_money(Float.parseFloat(jsonObject.getString("reno_money")));// 应收款金额
			}
			receiveNode.setReno_state(0);// 是否已收款，默认未收款；0未收款，1已收款，2未付全款，3提前收到款
			receiveNode.setReno_amoney((float) 0.00);// 实际收款金额
			receiveNode.setReno_ctime(new Date(time));// 节点录入时间
			receiveNode.setUser(user);
			Contract contract = contractRepository.selectContById(cont_id);// 所属合同
			receiveNode.setContract(contract);
			receiveNode.setCont_stime(contract.getCont_stime());
			receiveNode.setProvince(contract.getProvince());
			receiveNode.setReno_isdelete(0);// 默认未删除

			if (jsonObject.containsKey("projectStage")) {
				JSONObject tmp = (JSONObject) jsonObject.get("projectStage");
				Integer prst_id = Integer.valueOf(tmp.getString("prst_id"));
				ProjectStage projectStage = projectStageRepository.findOne(prst_id);// 所属阶段
				receiveNode.setProjectStage(projectStage);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 写入数据库
		receiveNode = receiveNodeRepository.saveAndFlush(receiveNode);
		if (receiveNode.getReno_id() != null)
			return true;
		else
			return false;
	}

	// 根据合同ID查找收款节点
	@Override
	public List<ReceiveNode> selectRenoByContId(Integer cont_id) {
		return receiveNodeRepository.findByContId(cont_id);
	}

	// 根据ID查询收款节点
	@Override
	public ReceiveNode findByRenoId(Integer reno_id) {
		return receiveNodeRepository.findOne(reno_id);
	}

	// 根据收款节点ID删除收款节点
	@Override
	public Boolean deleteReno(Integer reno_id) {
		return receiveNodeDao.deleteReno(reno_id);
	}

	// 更新收款节点状态和金额
	@Override
	public Boolean updateRenoStateAndMoney(Integer cont_id, Float remoAmoney) {
		List<ReceiveNode> renoList = receiveNodeRepository.findByContIdAndState(cont_id);
		float dvalue;
		ReceiveNode receiveNode = null;
		long time = System.currentTimeMillis();// 当前时间
		for (int i = 0; i < renoList.size(); i++) {
			if (remoAmoney > 0) {
				receiveNode = renoList.get(i);
				dvalue = receiveNode.getReno_money() - receiveNode.getReno_amoney();// 差值=应收款-实收款
				if (dvalue > remoAmoney) {// 若差值>本次确认金额
					float nowMoney = remoAmoney + receiveNode.getReno_amoney();
					receiveNodeDao.updateState(receiveNode.getReno_id(), RenoStatus.noEnough.value, nowMoney);
					break;
				} else {// 若差值<=本次确认金额
					if (time < receiveNode.getReno_time().getTime()) {// 提前到款
						receiveNodeDao.updateState(receiveNode.getReno_id(), RenoStatus.beyondActually.value,
								receiveNode.getReno_money());
					} else {// 已付全款
						receiveNodeDao.updateState(receiveNode.getReno_id(), RenoStatus.finish.value,
								receiveNode.getReno_money());
						alarmService.updateByIdType(receiveNode.getReno_id(), RemoveType.RenoAlarm.value);// 解除报警
					}
					if ((Math.abs(dvalue - remoAmoney) < 0.00000001)) {// 若差值=本次确认金额，跳出循环
						break;
					}
					remoAmoney -= dvalue;// 本次确认金额-差值
				}
			} else {
				break;
			}
		}
		return true;
	}

	// 修改收款节点
	@Override
	public Boolean updateReno(JSONObject jsonObject, Integer reno_id) {
		ReceiveNode receiveNode = receiveNodeRepository.findOne(reno_id);
		if (jsonObject != null) {
			try {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				if (jsonObject.containsKey("reno_content")) {
					receiveNode.setReno_content(jsonObject.getString("reno_content"));// 节点内容
				}
				if (jsonObject.containsKey("reno_money")) {
					receiveNode.setReno_money(Float.parseFloat(jsonObject.getString("reno_money")));// 应收款金额
				}
				Date date = null;
				if (jsonObject.containsKey("reno_time")) {
					date = format.parse(jsonObject.getString("reno_time"));// 节点截止时间
					receiveNode.setReno_time(date);
				} else {
					date = receiveNode.getReno_time();
				}
				if (jsonObject.containsKey("reno_wday")) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					int days = Integer.parseInt(jsonObject.getString("reno_wday"));
					receiveNode.setReno_wday(days);// 收款提醒天数
					calendar.add(Calendar.DAY_OF_MONTH, -days);// 收款提醒时间=节点截止时间-收款提醒天数
					receiveNode.setReno_wtime(calendar.getTime());// 收款提醒时间
				}
				if (jsonObject.containsKey("projectStage")) {
					JSONObject tmp = (JSONObject) jsonObject.get("projectStage");
					Integer prst_id = Integer.valueOf(tmp.getString("prst_id"));
					ProjectStage projectStage = projectStageRepository.findOne(prst_id);// 所属阶段
					receiveNode.setProjectStage(projectStage);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		receiveNode = receiveNodeRepository.saveAndFlush(receiveNode);
		if (receiveNode.getReno_id() != null)
			return true;
		else
			return false;
	}
}
