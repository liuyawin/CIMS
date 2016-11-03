package com.mvc.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.base.constants.SessionKeyConstants;
import com.mvc.entity.Contract;
import com.mvc.entity.User;
import com.mvc.service.ContractService;
import com.mvc.service.UserService;
import com.utils.JSONUtil;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 合同控制器
 * 
 * @author wangrui
 * @date 2016-09-10
 */
@Controller
@RequestMapping("/contract")
public class ContractController {

	@Autowired
	ContractService contractService;
	@Autowired
	UserService userService;

	/**
	 * 返回主任合同界面
	 * 
	 * @return
	 */
	@RequestMapping("/toContractPage.do")
	public String contractPage() {
		return "contractInformation/index";
	}

	/**
	 * 返回票据管理合同界面 包 20161013
	 * 
	 * @return
	 */
	@RequestMapping("/toBillMngContractPage.do")
	public String billMngContractPage() {
		return "billInformation/index";
	}

	/**
	 * 获取指定页面的十条合同信息，总页数
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getContractList.do")
	public @ResponseBody String getContList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		String contName = request.getParameter("contName");
		int totalRow = Integer.parseInt(contractService.countTotal(contName, 1).toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);
		// 和根据名字查找共用一个方法，contName为null
		List<Contract> list = contractService.findConByName(contName, pager.getOffset(), pager.getPageSize());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据合同创建者id获取欠款的合同列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getDebtContract.do")
	public @ResponseBody String getDebtContList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		String contName = request.getParameter("contName");
		int totalRow = Integer.parseInt(contractService.countTotal(contName, 2).toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);
		List<Contract> list = contractService.findAllDebtCont(contName, pager.getOffset(), pager.getPageSize());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据合同创建者id获取逾期的合同列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getOverdueContract.do")
	public @ResponseBody String getOverdueContList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		String contName = request.getParameter("contName");
		int totalRow = Integer.parseInt(contractService.countTotal(contName, 3).toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);
		List<Contract> list = contractService.findAllOverdueCont(contName, pager.getOffset(), pager.getPageSize());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据合同名获取合同信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectConByName.do")
	public @ResponseBody String selectConByName(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		String contName = request.getParameter("contName");
		int totalRow = Integer.parseInt(contractService.countTotal(contName, 1).toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);
		List<Contract> list = contractService.findConByName(contName, pager.getOffset(), pager.getPageSize());// 合同名
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 添加合同
	 * 
	 * @param request
	 * @param session
	 * @return 合同ID
	 */
	@RequestMapping("/addContract.do")
	public @ResponseBody Integer addContract(HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		JSONObject jsonObject = new JSONObject();
		jsonObject = JSONObject.fromObject(request.getParameter("contract"));
		long time = System.currentTimeMillis();
		Contract contract = new Contract();
		contract = (Contract) JSONUtil.JSONToObj(jsonObject.toString(), Contract.class);// 将json对象转换成实体对象，注意必须和实体类型一致
		contract.setCont_initiation(1);// 已立项
		contract.setCont_ishistory(0);// 未删除
		contract.setCont_state(0);// 合同状态
		contract.setCont_ctime(new Date(time));// 合同创建时间
		contract.setCreator(user);// 合同创建者
		contract.setCur_prst("未录入工期阶段");// 当前工期阶段
		contract.setCur_reno("未录入收款节点");// 当前收款节点

		contractService.addContract(contract);
		int cont_id = contract.getCont_id();
		session.setAttribute("cont_id", cont_id);// 创建合同时将cont_id放入session
		return cont_id;
	}

	/**
	 * 根据合同ID获取合同
	 * 
	 * @param request
	 * @param session
	 * @return Contract对象
	 */
	@RequestMapping("/selectContractById.do")
	public @ResponseBody String selectContById(HttpServletRequest request, HttpSession session) {
		int cont_id = Integer.parseInt(request.getParameter("cont_id"));
		session.setAttribute("cont_id", cont_id);// 将cont_id放入session
		Contract contract = contractService.selectContById(cont_id);
		System.out.println("合同详情："+JSON.toJSONString(contract));
		return JSON.toJSONString(contract);
	}

	/**
	 * 合同信息补录
	 * 
	 * @param request
	 * @param session
	 * @return Contract对象
	 */
	@RequestMapping("/repeatAddContract.do")
	public @ResponseBody String repeatAddContract(HttpServletRequest request) {
		// 先根据con_id获取Contract,再将其他字段补齐
		Contract contract = contractService.selectContById(Integer.parseInt(request.getParameter("cont_id")));
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("contract"));
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
				if (jsonObject.containsKey("proStage")) {
					contract.setProStage(jsonObject.getString("proStage"));// 项目阶段
				}
				if (jsonObject.containsKey("cont_stime")) {
					contract.setCont_stime(format.parse(jsonObject.getString("cont_stime")));// 合同签订时间
				}
				if (jsonObject.containsKey("manager")) {
					JSONObject json = JSONObject.fromObject(jsonObject.getString("manager"));
					User manager = userService.findById(Integer.parseInt(json.getString("user_id")));// 项目设总
					contract.setManager(manager);
				}
				if (jsonObject.containsKey("cont_hasproxy")) {
					contract.setCont_hasproxy(Integer.parseInt(jsonObject.getString("cont_hasproxy")));// 是否有委托书
				}
				if (jsonObject.containsKey("assistant_manager")) {
					JSONObject json = JSONObject.fromObject(jsonObject.getString("assistant_manager"));
					User manager = userService.findById(Integer.parseInt(json.getString("user_id")));// 项目副设总
					contract.setAssistant_manager(manager);
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

				contract.setCont_state(0);// 状态,初始默认为在建 0:在建,1:竣工,2:停建
				// 存入数据库
				contractService.addContract(contract);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return JSON.toJSONString(contract);
	}

	/**
	 * 删除合同
	 * 
	 * @param request
	 *            conId
	 * @return 前十条合同信息，总页数
	 */
	@RequestMapping("/deleteContract.do")
	public @ResponseBody String deleteContract(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		boolean isdelete = contractService.deleteContract(Integer.parseInt(request.getParameter("conId")));
		if (isdelete) {// 删除成功
			String contName = request.getParameter("contName");
			String pageType = request.getParameter("pageType");// 和下面的methodType类似，debtContract：欠款，overdueContract：逾期，finishedContract：终结，ContractList：所有
			int totalRow = Integer.parseInt(contractService.countTotal(contName, 1).toString());
			Pager pager = new Pager();
			pager.setPage(1);// 返回前十条
			pager.setTotalRow(totalRow);
			List<Contract> list = null;
			switch (pageType) {
			case "contractList":
				list = contractService.findConByName(contName, pager.getOffset(), pager.getPageSize());
				break;
			case "debtContract":
				list = contractService.findAllDebtCont(contName, pager.getOffset(), pager.getPageSize());
				break;
			case "overdueContract":
				list = contractService.findAllOverdueCont(contName, pager.getOffset(), pager.getPageSize());
				break;
			case "finishedContract":
				list = contractService.findAllEndCont(contName, pager.getOffset(), pager.getPageSize());
				break;
			default:
				break;
			}
			jsonObject.put("list", list);
			jsonObject.put("totalPage", pager.getTotalPage());
		}
		return jsonObject.toString();
	}

	/**
	 * 查找合同列表
	 * 
	 * @param request
	 * @return list和总页数
	 */
	@RequestMapping("/selectContract.do")
	public @ResponseBody String selectContract(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		int methodType = Integer.parseInt(request.getParameter("findType"));// 合同方法类别：1-合同信息管理，2-欠款合同信息，3-工程逾期合同，4-终结合同信息，张姣娜增加：5-停建合同
		String contName = request.getParameter("contName");
		int totalRow = Integer.parseInt(contractService.countTotal(contName, methodType).toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);
		List<Contract> list = null;
		switch (methodType) {
		case 1:
			list = contractService.findConByName(contName, pager.getOffset(), pager.getPageSize());
			break;
		case 2:
			list = contractService.findAllDebtCont(contName, pager.getOffset(), pager.getPageSize());
			break;
		case 3:
			list = contractService.findAllOverdueCont(contName, pager.getOffset(), pager.getPageSize());
			break;
		case 4:
			list = contractService.findAllEndCont(contName, pager.getOffset(), pager.getPageSize());
			break;
		case 5:
			list = contractService.findAllStopCont(contName, pager.getOffset(), pager.getPageSize());
			break;
		default:
			break;
		}
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据合同ID修改合同基本信息
	 * 
	 * @param request
	 * @param session
	 * @return 成功返回1，失败返回0
	 */
	@RequestMapping("/updateConById.do")
	public @ResponseBody Integer updateConById(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = JSONObject.fromObject(request.getParameter("contract"));
		Contract contract = null;
		Integer cont_id = null;
		Boolean flag = null;
		if (jsonObject.containsKey("cont_id")) {
			cont_id = Integer.parseInt(jsonObject.getString("cont_id"));// 合同名称
			contract = contractService.selectContById(cont_id);
		}
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
		}
		if (cont_id != null) {
			flag = contractService.updateConById(cont_id, contract);
		}
		if (flag == true)
			return 1;
		else
			return 0;
	}

	/**
	 * 设总更新合同状态
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/modifyStatus.do")
	public @ResponseBody String updateState(HttpServletRequest request, HttpSession session) throws ParseException {
		Integer contId = Integer.valueOf(request.getParameter("contId"));
		Integer contState = Integer.valueOf(request.getParameter("contState"));
		boolean result = contractService.updateState(contId, contState);
		return JSON.toJSONString(result);
	}
}
