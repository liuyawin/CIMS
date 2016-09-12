package com.mvc.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.ContractDao;

/**
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
@Repository("contractDaoImpl")
public class ContractDaoImpl implements ContractDao{
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	@Override
	public boolean updateState(Integer id, Integer state) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = " update contract set 'cont_state' = :cont_state  where cont_id =:cont_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("cont_state", state);
			query.setParameter("cont_id", id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

}
