package com.mvc.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.InvoiceDao;

/**
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
@Repository("invoiceDaoImpl")
public class InvoiceDaoImpl implements InvoiceDao{
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	@Override
	public boolean updateState(Integer id, Integer state) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = " update invoice set 'invo_state' = :invo_state  where invo_id =:invo_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("invo_state", state);
			query.setParameter("invo_id", id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

}
