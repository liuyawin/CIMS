package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.UserDao;
import com.mvc.entity.User;
import com.mvc.entity.UserDeptRelation;
import com.mvc.repository.DepartmentRepository;
import com.mvc.repository.UserRepository;

/**
 * User相关Dao层接口实现
 * 
 * @author wanghuimin
 * @date 2016年9月7日
 */
@Repository("userDaoImpl")
public class UserDaoImpl implements UserDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;
	@Autowired
	DepartmentRepository departmentRepository;
	@Autowired
	UserRepository userRepository;

	/**
	 * 删除用户
	 */
	public boolean updateState(Integer id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			String selectSql = " update user set `user_isdelete` = 1 where user_id =:user_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("user_id", id);
			query.executeUpdate();
			String selectSql1 = " update user_dept_relation set `user_dept_relation_state`=1 where user_id=:user_id ";
			Query query1 = em.createNativeQuery(selectSql1);
			query1.setParameter("user_id", id);
			query1.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;

	}

	// 根据页数筛选全部用户列表
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUserAllByPage(Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		String selectSql = "select * from User where user_isdelete=0";
		selectSql += " order by user_id desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, User.class);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<User> list = query.getResultList();
		em.close();
		return list;
	}

	// 只要设计部人员列表
	@SuppressWarnings("unchecked")
	@Override
	public List<UserDeptRelation> findUserFromDesign() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		int deptid = departmentRepository.findOnlyUserDesign();
		String selectSql = "select * from user_dept_relation where dept_id=" + deptid
				+ " and re_state=0";
		Query query = em.createNativeQuery(selectSql, UserDeptRelation.class);
		List<UserDeptRelation> list = query.getResultList();
		em.close();
		return list;
	}
}
