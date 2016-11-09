package com.mvc.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.enums.InvoiceStatus;
import com.base.enums.IsDelete;
import com.mvc.controller.LoginController;
import com.mvc.dao.InvoiceDao;
import com.mvc.entity.Contract;
import com.mvc.entity.ContractRecord;
import com.mvc.entity.Invoice;
import com.mvc.entity.User;
import com.mvc.repository.ContractRecordRepository;
import com.mvc.repository.ContractRepository;
import com.mvc.repository.InvoiceRepository;
import com.mvc.service.InvoiceService;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 发票
 * 
 * @author zjn
 * @date 2016年9月16日
 */
@Service("invoiceServiceImpl")
public class InvoiceServiceImpl implements InvoiceService {
	@Autowired
	InvoiceRepository invoiceRepository;
	@Autowired
	InvoiceDao invoiceDao;
	@Autowired
	ContractRepository contractRepository;
	@Autowired
	ContractRecordRepository contractRecordRepository;

	// 根据发票ID查询发票详情
	public Invoice findById(Integer invoiceId) {
		return invoiceRepository.findById(invoiceId);
	}

	// 根据发票id删除发票
	public Boolean delete(Integer invoiceId, User user) {
		Invoice invoice = invoiceRepository.findById(invoiceId);
		Contract contract = invoice.getContract();

		Boolean flag = invoiceDao.delete(invoiceId);
		if (flag) {
			// 合同日志
			ContractRecord contractRecord = new ContractRecord();
			contractRecord.setConre_content(
					user.getUser_name() + "---删除发票，金额：" + invoice.getInvo_money() + "万元---" + contract.getCont_name());
			long time = System.currentTimeMillis();
			Date date = new Date(time);
			contractRecord.setConre_time(date);
			contractRecord.setContract(contract);
			contractRecord.setUser(user);
			contractRecordRepository.saveAndFlush(contractRecord);
		}
		return invoiceDao.delete(invoiceId);
	}

	// 根据合同id，页数返回发票列表
	public List<Invoice> findByContId(Integer cont_id, Integer offset, Integer end) {
		return invoiceDao.findByContId(cont_id, offset, end);
	}

	// 根据合同ID查询发票总条数
	public Integer countByContId(Integer cont_id) {
		return invoiceDao.countByContId(cont_id);
	}

	// 根据合同ID查询发票总金额
	public Float totalMoneyOfInvoice(Integer contId) {
		return invoiceDao.totalMoneyOfInvoice(contId);
	}

	// 创建发票
	public Boolean save(JSONObject jsonObject, Integer cont_id, User user) {
		String permission = user.getRole().getRole_permission();// 权限
		String permissionStr = LoginController.numToPermissionStr(permission);
		Contract contract = contractRepository.selectContById(cont_id);

		Invoice invoice = new Invoice();
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			invoice.setContract(contract);
			invoice.setInvo_money(Float.valueOf(jsonObject.getString("invo_money")));
			invoice.setInvo_firm(jsonObject.getString("invo_firm"));
			Date sTime = format.parse(jsonObject.getString("invo_stime"));
			invoice.setInvo_stime(sTime);
			Date eTime = format.parse(jsonObject.getString("invo_etime"));
			invoice.setInvo_etime(eTime);
			if (jsonObject.containsKey("invo_remark")) {
				invoice.setInvo_remark(jsonObject.getString("invo_remark"));
			}
			User creator = new User();
			creator.setUser_id(user.getUser_id());
			invoice.setCreator(creator);
			invoice.setInvo_isdelete(IsDelete.NO.value);
			invoice.setInvo_ctime(date);
			if (permissionStr.contains("tInvoAudit")) {// 审核发票权限（主任）
				invoice.setInvo_state(InvoiceStatus.waitdealing.value);// 待处理
				if (jsonObject.containsKey("receiver")) {
					JSONObject receiverObject = JSONObject.fromObject(jsonObject.getString("receiver"));
					User receiver = new User();
					receiver.setUser_id(Integer.valueOf(receiverObject.getString("user_id")));
					invoice.setReceiver(receiver);
				}
				invoice.setAudit(user);
			} else {
				if (jsonObject.containsKey("audit")) {
					JSONObject auditObject = JSONObject.fromObject(jsonObject.getString("audit"));
					User audit = new User();
					audit.setUser_id(Integer.valueOf(auditObject.getString("user_id")));
					invoice.setAudit(audit);
				}
				invoice.setInvo_state(InvoiceStatus.waitAudit.value);// 待审核
			}
			if (jsonObject.containsKey("invo_id")) {// 修改发票
				invoice.setInvo_id(Integer.valueOf(jsonObject.getString("invo_id")));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Invoice invoiceResult = invoiceRepository.saveAndFlush(invoice);

		// 合同日志
		ContractRecord contractRecord = new ContractRecord();
		contractRecord.setConre_content(
				user.getUser_name() + "---开发票，金额：" + invoice.getInvo_money() + "万元---" + contract.getCont_name());
		contractRecord.setConre_time(date);
		contractRecord.setContract(contract);
		contractRecord.setUser(user);
		contractRecordRepository.saveAndFlush(contractRecord);

		if (invoiceResult.getInvo_id() != null) {
			return true;
		} else
			return false;
	}

	// 根据用户id，页数返回发票列表
	public List<Invoice> findByPage(Integer user_id, Integer ifSend, Integer offset, Integer end) {
		return invoiceDao.findByPage(user_id, ifSend, offset, end);
	}

	// 根据用户ID查询发票总条数
	public Integer countByParam(Integer user_id, Integer ifSend) {
		return invoiceDao.countByParam(user_id, ifSend);
	}

	// 按发票状态获取列表
	public Integer WaitingDealCountByParam(Integer user_id, Integer invoiceState) {
		return invoiceDao.WaitingDealCountByParam(user_id, invoiceState);
	}

	// 根据用户id，页数返回发票列表
	public List<Invoice> WaitingDealFindByPage(Integer user_id, Integer invoiceState, Integer offset, Integer end) {
		return invoiceDao.WaitingDealFindByPage(user_id, invoiceState, offset, end);
	}

	// 点击完成发票任务
	public Boolean invoiceFinish(Integer invoiceId, Date invoTime, User user) {
		Boolean flag = invoiceDao.invoiceFinish(invoiceId, invoTime);
		Invoice invoice = invoiceRepository.findById(invoiceId);
		if (flag) {
			// 合同日志
			ContractRecord contractRecord = new ContractRecord();
			Contract contract = invoice.getContract();
			contractRecord.setConre_content(
					user.getUser_name() + "---发票完成，金额：" + invoice.getInvo_money() + "万元---" + contract.getCont_name());
			long time = System.currentTimeMillis();
			Date date = new Date(time);
			contractRecord.setConre_time(date);
			contractRecord.setContract(contract);
			contractRecord.setUser(user);
			contractRecordRepository.saveAndFlush(contractRecord);
		}
		return flag;
	}

	// 主任转发发票
	public boolean transmitInvoice(Integer invoiceId, Date invoEtime, Integer receiverId) {
		return invoiceDao.transmitInvoice(invoiceId, invoEtime, receiverId);
	}

	// 根据权限，状态，页码 查找发票
	@Override
	public List<Invoice> selectInvoByStateAndPerm(Integer invoState, String permission, Integer user_id, Pager pager) {
		List<Invoice> list;
		permission = LoginController.numToPermissionStr(permission);
		if (invoState == -1) {// -1：全部，0：待审核，1：待处理，2：已完成
			list = invoiceDao.findByAllAndPerm(permission, user_id, pager);
		} else {
			list = invoiceDao.findByStateAndPerm(invoState, permission, user_id, pager);
		}
		return list;
	}

	// 根据权限，状态 查询发票总条数
	@Override
	public Integer countByStateAndPerm(Integer invoState, String permission, Integer user_id) {
		Integer totalRow;
		permission = LoginController.numToPermissionStr(permission);
		if (invoState == -1) {// -1：全部，0：待审核，1：待处理，2：已完成
			totalRow = invoiceDao.countByAllAndPerm(permission, user_id);
		} else {
			totalRow = invoiceDao.countByStateAndPerm(invoState, permission, user_id);
		}
		return totalRow;
	}

	// 根据合同ID，状态，页码 查找发票
	@Override
	public List<Invoice> selectInvoByStateAndContId(Integer invoState, Integer cont_id, Pager pager) {
		List<Invoice> list;
		if (invoState == -1) {// -1：全部，0：待审核，1：待处理，2：已完成
			list = invoiceDao.findByAllAndContId(cont_id, pager);
		} else {
			list = invoiceDao.findByStateAndContId(invoState, cont_id, pager);
		}
		return list;
	}

	// 根据合同ID，状态 查询发票总条数
	@Override
	public Integer countByStateAndContId(Integer invoState, Integer cont_id) {
		Integer totalRow;
		if (invoState == -1) {// -1：全部，0：待审核，1：待处理，2：已完成
			totalRow = invoiceDao.countByAllAndContId(cont_id);
		} else {
			totalRow = invoiceDao.countByStateAndContId(invoState, cont_id);
		}
		return totalRow;
	}

	// 根据合同ID获取已完成发票总条数
	@Override
	public Integer countTotalRow(Integer cont_id) {
		return invoiceDao.countTotalRow(cont_id);
	}

}
