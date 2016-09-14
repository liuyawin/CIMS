package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.DepartmentDao;
import com.mvc.entity.Department;

/**
 * Department相关Dao层接口实现
 * 
 * @author wanghuimin
 * @date 2016年9月8日
 */
@Repository("departmentDaoImpl")
public class DepartmentDaoImpl implements DepartmentDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;
//根据id修改部门状态
	public boolean delete(Integer id, Integer state) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = " update dept set 'dept_state' = :dept_state  where dept_id =:dept_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("dept_state", state);
			query.setParameter("dept_id", id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}
//筛选部门列表
	@Override
	public List<Department> findDepartmentAll(Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "select * from department where dept_state=0";		
		selectSql += " order by dept_id desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, Department.class);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<Department> list = query.getResultList();
		em.close();
		return list;
	}

}
