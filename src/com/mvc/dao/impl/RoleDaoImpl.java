package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.RoleDao;
import com.mvc.entity.Role;
import com.mvc.repository.UserRepository;

/**
 * 角色，职位
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
@Repository("roleDaoImpl")
public class RoleDaoImpl implements RoleDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;
	@Autowired
	UserRepository userRepository;

	// 删除，修改角色状态列表
	public boolean updateState(Integer role_id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Long count = userRepository.countUserByroleid(role_id);
		String count1=count.toString();
		int count2=Integer.parseInt(count1);
		if (count2 < 1) {
			String selectSql = " update role set `role_state` = 1  where role_id =:role_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("role_id", role_id);
			query.executeUpdate();
			em.flush(); 
			em.getTransaction().commit();
			em.close();
			return true;
		} else {
			return false;
		}

	}

	// 根据页数筛选用户列表
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> findRoleAllByPage(Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		String selectSql="select * from role where role_state=0";
		selectSql +=" order by role_id desc limit :offset,:end ";
		Query query=em.createNativeQuery(selectSql,Role.class);
		query.setParameter("offset",offset);
		query.setParameter("end", end);		
		List<Role> list=query.getResultList();
		em.close();
		return list;
	}

}
