package com.mvc.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * 收款节点
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
@Repository("ReceiveNodeDaoIpml")
public class ReceiveNodeDaoImpl {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	/* 修改收款节点状态 */
	public boolean updateState(Integer id, Integer state) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = " update recive_node set 'reno_state' = :reno_state  where reno_id =:reno_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("reno_state", state);
			query.setParameter("reno_id", id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}

		return true;
	}

}
