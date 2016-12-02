package com.mvc.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.enums.ContractState;
import com.base.enums.MethodType;
import com.mvc.dao.ContractDao;
import com.mvc.entity.Contract;
import com.mvc.entity.ContractRecord;
import com.mvc.entity.User;
import com.mvc.repository.ContractRecordRepository;
import com.mvc.repository.ContractRepository;
import com.mvc.repository.UserRepository;
import com.mvc.service.ContractService;
import com.utils.JSONUtil;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 合同业务实现
 * 
 * @author wangrui
 * @date 2016-09-10
 */
@Service("contractServiceImpl")
public class ContractServiceImpl implements ContractService {

	@Autowired
	ContractDao contractDao;
	@Autowired
	ContractRepository contractRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ContractRecordRepository contractRecordRepository;

	// 查询所有欠款合同
	@Override
	public List<Contract> findAllDebtCont(String contName, Integer offset, Integer end) {
		return contractDao.findAllDebtCont(contName, offset, end);
	}

	// 查询所有逾期合同
	@Override
	public List<Contract> findAllOverdueCont(String contName, Integer offset, Integer end) {
		return contractDao.findAllOverdueCont(contName, offset, end);
	}

	// 查询合同总条数
	@Override
	public Long countTotal(String contName, Integer methodType) {
		return contractDao.countTotal(contName, methodType);
	}

	// 根据合同名获取合同信息
	@Override
	public List<Contract> findConByName(String contName, Integer offset, Integer end) {
		return contractDao.findConByName(contName, offset, end);
	}

	// 添加合同
	@Override
	public Contract addContract(User user, JSONObject jsonObject) {
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		Contract contract = new Contract();
		contract = (Contract) JSONUtil.JSONToObj(jsonObject.toString(), Contract.class);// 将json对象转换成实体对象，注意必须和实体类型一致
		contract.setCont_initiation(1);// 已立项
		contract.setCont_ishistory(0);// 未删除
		contract.setCont_state(0);// 项目状态
		contract.setCont_rank(1);// 合同等级
		contract.setCont_isback(0);// 是否返回合同
		contract.setCompany_type("0");// 公司类型
		contract.setCont_ctime(date);// 合同创建时间
		contract.setCreator(user);// 合同创建者
		contract.setCur_prst("未录入工期阶段");// 当前工期阶段
		contract.setCur_reno("未录入收款节点");// 当前收款节点
		contract.setCont_money((float) 0);
		contract.setRece_total(0);
		contract.setRece_totalmoney((float) 0);
		contract.setRemo_count(0);
		contract.setRemo_totalmoney((float) 0);
		contract.setInvo_total(0);
		contract.setInvo_totalmoney((float) 0);
		contract = contractRepository.saveAndFlush(contract);

		// 合同日志
		ContractRecord contractRecord = new ContractRecord();
		contractRecord.setConre_content(user.getUser_name() + "---新建合同---" + contract.getCont_name());
		contractRecord.setConre_time(date);
		contractRecord.setContract(contract);
		contractRecord.setUser(user);
		contractRecordRepository.saveAndFlush(contractRecord);

		return contract;
	}

	// 根据合同ID获取合同
	@Override
	public Contract selectContById(Integer cont_id) {
		return contractRepository.selectContById(cont_id);
	}

	// 根据合同ID删除合同
	@Override
	public List<Contract> deleteContract(Integer cont_id, String contName, String methodType, Pager pager, User user) {
		List<Contract> list = null;
		Contract contract = contractRepository.selectContById(cont_id);
		boolean isdelete = contractDao.delete(cont_id);
		if (isdelete) {// 删除成功
			int methodTypeInt = MethodType.valueOf(methodType).value;
			switch (methodTypeInt) {
			case 1:
				list = contractDao.findConByName(contName, pager.getOffset(), pager.getPageSize());
				break;
			case 2:
				list = contractDao.findAllDebtCont(contName, pager.getOffset(), pager.getPageSize());
				break;
			case 3:
				list = contractDao.findAllOverdueCont(contName, pager.getOffset(), pager.getPageSize());
				break;
			case 4:
				list = contractDao.findAllEndCont(contName, pager.getOffset(), pager.getPageSize());
				break;
			case 5:
				list = contractDao.findAllStopCont(contName, pager.getOffset(), pager.getPageSize());
				break;
			default:
				break;
			}

			// 合同日志
			ContractRecord contractRecord = new ContractRecord();
			long time = System.currentTimeMillis();
			Date date = new Date(time);
			contractRecord.setConre_content(user.getUser_name() + "---删除合同---" + contract.getCont_name());
			contractRecord.setConre_time(date);
			contractRecord.setContract(contract);
			contractRecord.setUser(user);
			contractRecordRepository.saveAndFlush(contractRecord);
		}
		return list;
	}

	// 查询所有终结合同列表
	@Override
	public List<Contract> findAllEndCont(String contName, Integer offset, Integer end) {
		return contractDao.findAllEndCont(contName, offset, end);
	}

	// 修改合同基本信息
	@Override
	public Boolean updateContBase(Integer cont_id, JSONObject jsonObject, User user) {
		Contract contract = contractRepository.selectContById(cont_id);
		if (contract != null) {
			if (jsonObject.containsKey("cont_name")) {
				contract.setCont_name(jsonObject.getString("cont_name"));// 合同名称
			}
			if (jsonObject.containsKey("cont_project")) {
				contract.setCont_project(jsonObject.getString("cont_project"));// 项目名称
			}
			if (jsonObject.containsKey("cont_type")) {
				contract.setCont_type(Integer.parseInt(jsonObject.getString("cont_type")));// 合同类型
			}
			if (jsonObject.containsKey("cont_cheader")) {
				contract.setCont_cheader(jsonObject.getString("cont_cheader"));// 业主联系人
			}
			if (jsonObject.containsKey("cont_ctel")) {
				contract.setCont_ctel(jsonObject.getString("cont_ctel"));// 业主联系方式
			}
			if (jsonObject.containsKey("cont_cdept")) {
				contract.setCont_cdept(jsonObject.getString("cont_cdept"));// 业主联系部门
			}
			if (jsonObject.containsKey("cont_rank")) {
				contract.setCont_rank(jsonObject.getInt("cont_rank"));// 等级
			}
			if (jsonObject.containsKey("cont_client")) {
				contract.setCont_client(jsonObject.getString("cont_client"));// 业主公司
			}
			if (jsonObject.containsKey("province")) {
				contract.setProvince(jsonObject.getString("province"));// 省
			}
			if (jsonObject.containsKey("city")) {
				contract.setCity(jsonObject.getString("city"));// 市
			}
		}

		// return contractDao.updateConById(cont_id, contract);
		contract = contractRepository.saveAndFlush(contract);

		// 合同日志
		ContractRecord contractRecord = new ContractRecord();
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		contractRecord.setConre_content(user.getUser_name() + "---修改合同基本信息---" + contract.getCont_name());
		contractRecord.setConre_time(date);
		contractRecord.setContract(contract);
		contractRecord.setUser(user);
		contractRecordRepository.saveAndFlush(contractRecord);

		if (contract.getCont_id() != null)
			return true;
		else
			return false;
	}

	// 张姣娜：根据合同id修改状态
	@Override
	public Boolean updateState(Integer contId, Integer contState, User user) {
		Contract contract = contractRepository.selectContById(contId);
		boolean flag = contractDao.updateState(contId, contState);
		if (flag) {
			// 合同日志
			ContractRecord contractRecord = new ContractRecord();
			long time = System.currentTimeMillis();
			Date date = new Date(time);

			String before = ContractState.intToStr(contract.getCont_state());
			String after = ContractState.intToStr(contState);
			contractRecord.setConre_content(
					user.getUser_name() + "---项目状态：" + before + ">>" + after + "---" + contract.getCont_name());
			contractRecord.setConre_time(date);
			contractRecord.setContract(contract);
			contractRecord.setUser(user);
			contractRecordRepository.saveAndFlush(contractRecord);
		}
		return flag;
	}

	// 张姣娜：查询所有停建合同列表
	@Override
	public List<Contract> findAllStopCont(String contName, Integer offset, Integer end) {
		return contractDao.findAllStopCont(contName, offset, end);
	}

	// 合同信息补录
	@Override
	public Contract updateContract(Integer cont_id, JSONObject jsonObject, User user) {
		Contract contract = contractRepository.selectContById(cont_id);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		boolean flag_shezong = false;
		String shezong = null;
		if (jsonObject != null) {
			try {
				if (jsonObject.containsKey("cont_money")) {
					contract.setCont_money(Float.parseFloat(jsonObject.getString("cont_money")));// 合同金额
				}
				if (jsonObject.containsKey("cont_pnum")) {
					contract.setCont_pnum(jsonObject.getString("cont_pnum"));// 项目编码
				}
				if (jsonObject.containsKey("cont_onum")) {
					contract.setCont_onum(jsonObject.getString("cont_onum"));// 本公司合同编号
				}
				if (jsonObject.containsKey("cont_cnum")) {
					contract.setCont_cnum(jsonObject.getString("cont_cnum"));// （甲方）业主方编号
				}
				if (jsonObject.containsKey("install_capacity")) {
					contract.setInstall_capacity(Float.parseFloat(jsonObject.getString("install_capacity")));// 装机容量
				}
				if (jsonObject.containsKey("proStage")) {
					contract.setPro_stage(jsonObject.getString("proStage"));// 项目阶段
				}
				if (jsonObject.containsKey("cont_stime")) {
					contract.setCont_stime(format.parse(jsonObject.getString("cont_stime")));// 合同签订时间
				}
				if (jsonObject.containsKey("manager")) {
					JSONObject json = JSONObject.fromObject(jsonObject.getString("manager"));
					User manager = userRepository.findById(Integer.valueOf(json.getString("user_id")));// 项目设总
					if (contract.getManager() != null && contract.getManager() != manager) {// 修改设总
						flag_shezong = true;
						shezong = contract.getManager().getUser_name();
					}
					contract.setManager(manager);
				}
				if (jsonObject.containsKey("cont_hasproxy")) {
					contract.setCont_hasproxy(Integer.parseInt(jsonObject.getString("cont_hasproxy")));// 是否有委托书
				}
				if (jsonObject.containsKey("assistant_manager")) {
					JSONObject json = JSONObject.fromObject(jsonObject.getString("assistant_manager"));
					if (!json.isNullObject() && json != null) {// JSONObject为空判断
						User assistant_manager = userRepository.findById(Integer.valueOf(json.getString("user_id")));// 项目副设总
						contract.setAssistant_manager(assistant_manager);
					}
				}
				if (jsonObject.containsKey("cont_initiation")) {
					contract.setCont_initiation(jsonObject.getInt("cont_initiation"));// 是否立项
				}
				if (jsonObject.containsKey("cont_orgcodenum")) {
					contract.setCont_orgcodenum(jsonObject.getString("cont_orgcodenum"));// 组织机构代码证号
				}
				if (jsonObject.containsKey("company_type")) {
					contract.setCompany_type(jsonObject.getString("company_type"));// 企业性质
				}
				if (jsonObject.containsKey("cont_caddress")) {
					contract.setCont_caddress(jsonObject.getString("cont_caddress"));// 业主通讯地址
				}
				if (jsonObject.containsKey("cont_czipcode")) {
					contract.setCont_czipcode(jsonObject.getString("cont_czipcode"));// 业主邮编
				}
				if (jsonObject.containsKey("cont_cfax")) {
					contract.setCont_cfax(jsonObject.getString("cont_cfax"));// 业主传真
				}
				// 联系人1
				if (jsonObject.containsKey("landline_tel")) {
					contract.setLandline_tel(jsonObject.getString("landline_tel"));// 固定电话
				}
				if (jsonObject.containsKey("post")) {
					contract.setPost(jsonObject.getString("post"));// 职务
				}
				if (jsonObject.containsKey("cont_cdept")) {
					contract.setCont_cdept(jsonObject.getString("cont_cdept"));// 所在部门
				}
				if (jsonObject.containsKey("cont_cemail")) {
					contract.setCont_cemail(jsonObject.getString("cont_cemail"));// 电子邮箱
				}
				// 联系人2
				if (jsonObject.containsKey("cont_cheader2")) {
					contract.setCont_cheader2(jsonObject.getString("cont_cheader2"));// 姓名2
				}
				if (jsonObject.containsKey("cont_ctel2")) {
					contract.setCont_ctel2(jsonObject.getString("cont_ctel2"));// 联系方式
				}
				if (jsonObject.containsKey("landline_tel2")) {
					contract.setLandline_tel2(jsonObject.getString("landline_tel2"));// 固定电话
				}
				if (jsonObject.containsKey("post2")) {
					contract.setPost2(jsonObject.getString("post2"));// 职务
				}
				if (jsonObject.containsKey("cont_cdept2")) {
					contract.setCont_cdept2(jsonObject.getString("cont_cdept2"));// 所在部门
				}
				if (jsonObject.containsKey("cont_cemail2")) {
					contract.setCont_cemail2(jsonObject.getString("cont_cemail2"));// 电子邮箱
				}
				if (jsonObject.containsKey("cont_avetaxpayer")) {
					contract.setCont_avetaxpayer(jsonObject.getInt("cont_avetaxpayer"));// 纳税人类型
				}
				if (jsonObject.containsKey("invoice_type")) {
					contract.setInvoice_type(Integer.valueOf(jsonObject.getString("invoice_type")));// 发票类型
				}
				if (jsonObject.containsKey("cont_taxidennum")) {
					contract.setCont_taxidennum(jsonObject.getString("cont_taxidennum"));// 纳税人识别号
				}
				if (jsonObject.containsKey("tel")) {
					contract.setTel(jsonObject.getString("tel"));// 电话
				}
				if (jsonObject.containsKey("cont_bank")) {
					contract.setCont_bank(jsonObject.getString("cont_bank"));// 开户行
				}
				if (jsonObject.containsKey("cont_account")) {
					contract.setCont_account(jsonObject.getString("cont_account"));// 银行账户
				}
				if (jsonObject.containsKey("com_signaddress")) {
					contract.setCom_signaddress(jsonObject.getString("com_signaddress"));// 公司注册地址
				}
				if (jsonObject.containsKey("cont_remark")) {
					contract.setCont_remark(jsonObject.getString("cont_remark"));// 备注
				}

				contract.setCont_state(0);// 项目状态,初始默认为在建 0:在建,1:竣工,2:停建
				// 存入数据库
				contract = contractRepository.saveAndFlush(contract);

				// 合同日志
				ContractRecord contractRecord = new ContractRecord();
				long time = System.currentTimeMillis();
				Date date = new Date(time);
				contractRecord.setConre_content(user.getUser_name() + "---补录合同---" + contract.getCont_name());
				contractRecord.setConre_time(date);
				contractRecord.setContract(contract);
				contractRecord.setUser(user);
				contractRecordRepository.saveAndFlush(contractRecord);

				if (flag_shezong) {// 修改设总
					// 合同日志
					contractRecord = new ContractRecord();
					contractRecord.setConre_content(
							user.getUser_name() + "---修改设总---" + shezong + ">>" + contract.getManager().getUser_name());
					contractRecord.setConre_time(date);
					contractRecord.setContract(contract);
					contractRecord.setUser(user);
					contractRecordRepository.saveAndFlush(contractRecord);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return contract;
	}

	// 根据合同名和方法类别获取合同列表
	@Override
	public List<Contract> findConByNameAndMType(String contName, Integer methodType, Pager pager) {
		List<Contract> list = null;
		switch (methodType) {
		case 1:
			list = contractDao.findConByName(contName, pager.getOffset(), pager.getPageSize());
			break;
		case 2:
			list = contractDao.findAllDebtCont(contName, pager.getOffset(), pager.getPageSize());
			break;
		case 3:
			list = contractDao.findAllOverdueCont(contName, pager.getOffset(), pager.getPageSize());
			break;
		case 4:
			list = contractDao.findAllEndCont(contName, pager.getOffset(), pager.getPageSize());
			break;
		case 5:
			list = contractDao.findAllStopCont(contName, pager.getOffset(), pager.getPageSize());
			break;
		default:
			break;
		}
		return list;
	}

	// 张姣娜：完成文书任务后更新合同状态
	@Override
	public Boolean updateContIsback(Integer contId, Integer state) {
		return contractDao.updateContIsback(contId, state);
	}

	// 根据日期获取合同总金额
	@Override
	public Float getTotalMoney(String date) {
		return contractDao.getTotalMoney(date);
	}

}
