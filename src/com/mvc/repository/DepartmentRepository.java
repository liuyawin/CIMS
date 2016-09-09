/**
 * 
 */
package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Department;

/**
 * 角色
 * 
 * @author zjn
 * @date 2016年9月7日
 */
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
	//根据ID查询部门信息
	@Query("select d from Department d where dept_id = :dept_id")
	public Department findById(@Param("dept_id") Integer dept_id);

	//筛选出所有部门列表
	@Query("select d from Department d where dept_state=0")
	public List<Department> findAlls();


	
}
