package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.mvc.entity.Department;
import com.mvc.service.DepartmentService;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 部门
 * 
 * @author wanghuimin
 * @date 2016年9月14日
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {
	@Autowired
	DepartmentService departmentService;

	/**
	 * 根据页数筛选任务列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getDepartmentListByPage.do")
	public @ResponseBody String getStores(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Long totalRow = departmentService.countTotal();
		System.out.println("总数" + totalRow);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(Integer.parseInt(totalRow.toString()));
		List<Department> list = departmentService.findDepartmentAllByPage(pager.getOffset(), pager.getLimit());
		System.out.println(list.toString());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		System.out.println("返回列表:" + jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * 所有任务列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getAllDepartmentList.do")
	public @ResponseBody String getAllStores(HttpServletRequest request, HttpSession session) {
		List<Department> result = departmentService.findDepartmentAlls();
		System.out.println(JSON.toJSONString(result));
		return JSON.toJSONString(result);
	}

	/**
	 * 删除任务
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deleteDepart.do")
	public @ResponseBody String deleteDepart(HttpServletRequest request, HttpSession session) {

		Integer deptId = Integer.valueOf(request.getParameter("deptId"));
		boolean result = departmentService.deleteState(deptId);
		return JSON.toJSONString(result);
	}
	
	/**
	 * 添加任务
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/addDepart.do")
	public @ResponseBody String addDepart(HttpServletRequest request, HttpSession session) {
		Department department=new Department();
//		department.setDept_name(request.getAttribute("params"));
//		department.setDepartment(department);
//		department.set(request.getAttribute("params"));
		boolean result = departmentService.save(department);
		return JSON.toJSONString(result);
	}

}
