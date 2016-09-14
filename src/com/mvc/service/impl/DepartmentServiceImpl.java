package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.DepartmentDao;
import com.mvc.entity.Department;
import com.mvc.repository.DepartmentRepository;
import com.mvc.service.DepartmentService;

/**
 * 部门
 * 
 * @author wanghuimin
 * @date 2016年9月13日
 */
@Service("departmentServiceImpl")
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	DepartmentRepository departmentrepository;
	@Autowired
	DepartmentDao departmentDao;

	// 根据部门id和部门名称筛选列表
	public List<Department> findDepartmentByName(Integer dept_id, String dept_name) {
		return departmentrepository.findByName(dept_id, dept_name);
	}

	// 查找所有部门列表
	@Override
	public List<Department> findDepartmentAlls(Integer offset, Integer end) {
		return departmentDao.findDepartmentAll(offset, end);
	}

	// 根据id删除
	@Override
	public boolean deleteState(Integer dept_id) {
		departmentrepository.deleteById(dept_id);
		return true;
	}

	// 增加一条数据
	@Override
	public Department save(Department department) {
		return departmentrepository.saveAndFlush(department);
	}

	@Override
	public Long countTotal() {
		return departmentrepository.countTotal();
	}

}
