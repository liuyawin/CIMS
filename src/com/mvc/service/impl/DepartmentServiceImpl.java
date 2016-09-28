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

	// 根据起始位置查找所有部门列表
	@Override
	public List<Department> findDepartmentAllByPage(Integer offset, Integer end) {
		return departmentDao.findDepartmentAllByPage(offset, end);
	}

	// 根据id删除
	@Override
	public boolean deleteState(Integer dept_id) {
		return departmentDao.delete(dept_id);
	}

	// 增加一条数据
	@Override
	public boolean save(Department department) {
		Department result = departmentrepository.saveAndFlush(department);
		if (result.getDept_id() != null)
			return true;
		else
			return false;

	}

	@Override
	public Long countTotal() {
		return departmentrepository.countTotal();
	}

	// 获取所有部门列表
	@Override
	public List<Department> findDepartmentAlls() {

		return departmentDao.findDepartmentAll();
	}

	// 根据ID查看部门详情
	@Override
	public Department findDepartmentContentById(Integer dept_id) {
		return departmentrepository.findDepartmentContentById(dept_id);
	}

}
