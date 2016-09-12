package com.mvc.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.RoleDao;

/**
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
@Repository("roleDaoImpl")
public class RoleDaoImpl implements RoleDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;
 
	@Override
	public boolean updateState(Integer id, Integer state) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = " update role set 'role_state' = :role_state  where role_id =:role_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("role_state", state);
			query.setParameter("role_id", id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

}
