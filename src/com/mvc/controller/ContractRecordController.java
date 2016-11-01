package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.entity.ContractRecord;

import com.mvc.service.ContractRecordService;

import net.sf.json.JSONObject;

/**
 * 合同日志控制器
 * 
 * @author wangrui
 * @date 2016-10-25
 */
@Controller
// @Aspect
@RequestMapping("/contractRecord")
public class ContractRecordController {

	// @Pointcut("execution(* com.mvc.service.ContractService.addContract(..))")
	// public void handleContract() {
	//
	// }

	@Autowired
	ContractRecordService contractRecordService;

	/**
	 * 根据合同ID获取操作记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectContRecordByContId.do")
	public @ResponseBody String selectContRecordByContId(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		int cont_id = Integer.parseInt(request.getParameter("cont_id"));
		List<ContractRecord> list = contractRecordService.selectContRecordByContId(cont_id);
		jsonObject.put("list", list);
		return jsonObject.toString();
	}

	// /**
	// * 添加合同操作日志
	// *
	// * @param request
	// * @param session
	// * @return
	// */
	// @Before("handleContract()")
	// public void addContRecord(JoinPoint point) {
	// System.out.println("测试进入日志");
	// HttpServletRequest request = ((ServletRequestAttributes)
	// RequestContextHolder.getRequestAttributes())
	// .getRequest();
	// HttpSession session = request.getSession();
	// User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
	// // 访问目标方法的参数：
	// Object[] args = point.getArgs();
	// if (args != null && args.length > 0 && args[0].getClass() ==
	// String.class) {
	// args[0] = "改变后的参数1";
	// }
	//
	// ContractRecord contractRecord = new ContractRecord();
	// contractRecord.setUser(user);
	// // contractRecord.setContract(contract);
	//
	// Boolean flag = contractRecordService.addContRecord(contractRecord);
	//
	// }

}
