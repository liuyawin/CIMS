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
	 * 根据页数筛选部门列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getDepartmentListByPage.do")
	public @ResponseBody String getStores(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Long totalRow = departmentService.countTotal();
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(Integer.parseInt(totalRow.toString()));
		List<Department> list = departmentService.findDepartmentAllByPage(pager.getOffset(), pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 所有部门列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getAllDepartmentList.do")
	public @ResponseBody String getAllStores(HttpServletRequest request, HttpSession session) {
		List<Department> result = departmentService.findDepartmentAlls();
		return JSON.toJSONString(result);
	}

	/**
	 * 删除部门
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
	 * 添加，修改部门
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/addDepart.do")
	public @ResponseBody String addDepart(HttpServletRequest request, HttpSession session) {
		Department department = new Department();
		department.setDept_name(request.getParameter("dept_name"));
		Department pId = new Department();
		pId.setDept_id(Integer.valueOf(request.getParameter("dept_pid")));
		department.setDepartment(pId);
		department.setDept_remark(request.getParameter("dept_remark"));
		department.setDept_state(0);
		boolean result;
		if (request.getParameter("dept_id") != null) {
			department.setDept_id(Integer.valueOf(request.getParameter("dept_id")));
			result = departmentService.save(department);// 修改部门信息
		} else {
			result = departmentService.save(department);// 添加部门信息
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 根据ID查看部门详情
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectDepartmentById.do")
	public @ResponseBody String getDepartmentContentById(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer deptid = Integer.valueOf(request.getParameter("deptid"));
		Department department = departmentService.findDepartmentContentById(deptid);
		jsonObject.put("department", department);
		return jsonObject.toString();

	}

}
