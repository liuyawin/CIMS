package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.base.enums.IsDelete;
import com.mvc.dao.UserDao;
import com.mvc.entity.User;
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
	UserRepository userRepository;

	/**
	 * 删除用户
	 */
	public boolean updateState(Integer id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			String selectSql = " update user set user_isdelete =:user_isdelete where user_id =:user_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("user_id", id);
			query.setParameter("user_isdelete", IsDelete.YES.value);
			query.executeUpdate();
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
	public List<User> findUserAllByPage(String searchKey, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "select * from User where user_isdelete=0";
		// 判断查找关键字是否为空
		if (null != searchKey) {
			selectSql += " and ( user_name like '%" + searchKey + "%' or user_num like '%" + searchKey + "%')";
		}
		selectSql += " order by user_id desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, User.class);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<User> list = query.getResultList();
		em.close();
		return list;
	}

	// // 查询用户总条数
	@SuppressWarnings("unchecked")
	public Integer countTotal(String searchKey) {
		EntityManager em = emf.createEntityManager();
		String countSql = " select count(user_id) from User u where user_isdelete=0 ";
		if (null != searchKey) {
			countSql += "   and (user_name like '%" + searchKey + "%' or user_num like '%" + searchKey + "%')";
		}
		Query query = em.createNativeQuery(countSql);
		List<Object> totalRow = query.getResultList();
		em.close();
		return Integer.parseInt(totalRow.get(0).toString());
	}

}
