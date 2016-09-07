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

/**
 * User相关Dao层接口实现
 * 
 * @author zjn
 * @date 2016年9月7日
 */
@Repository("userDaoImpl")
public class UserDaoImpl implements UserDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	/**
	 * 根据用户名查找用户信息
	 */
	public User findByUsername(String name) {
		EntityManager em = emf.createEntityManager();
		String selectSql = " select * from  user u where  u.name = :name";
		Query query = em.createNativeQuery(selectSql, User.class);
		query.setParameter("name", name);
		List<User> list = query.getResultList();
		em.close();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 查找所有用户
	 */
	public List<User> findAllUser(String name) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "select * from user where name like '%" + name + "%' ";

		Query query = em.createNativeQuery(selectSql, User.class);
		List<User> list = query.getResultList();
		em.close();
		return list;
	}
}
