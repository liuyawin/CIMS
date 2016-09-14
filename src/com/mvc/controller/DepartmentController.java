package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.entity.Department;
import com.mvc.service.DepartmentService;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 部门
 * @author wanghuimin
 * @date 2016年9月14日
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {
	@Autowired
	DepartmentService departmentService;
	
	// 筛选任务列表
		@RequestMapping(value = "/getDepartmentList.do")
		public @ResponseBody String getStores(HttpServletRequest request, HttpSession session) {
			JSONObject jsonObject = new JSONObject();
			Long totalRow = departmentService.countTotal();
			System.out.println("总数" + totalRow);
			Pager pager = new Pager();
			pager.setPage(Integer.valueOf(request.getParameter("page")));
			pager.setTotalRow(Integer.parseInt(totalRow.toString()));
			List<Department> list = departmentService.findDepartmentAlls(pager.getOffset(), pager.getLimit());
			System.out.println(list.toString());
			jsonObject.put("list", list);
			jsonObject.put("totalPage", pager.getTotalPage());
			System.out.println("返回列表:" + jsonObject.toString());
			return jsonObject.toString();
		}

}
