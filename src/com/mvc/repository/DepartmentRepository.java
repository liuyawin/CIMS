/**
 * 
 */
package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.entity.Department;

/**
 * @author zjn
 * @date 2016年9月7日
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
