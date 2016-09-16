package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.InvoiceDao;
import com.mvc.entity.Invoice;

/**
 * 发票
 * 
 * @author zjn
 * @date 2016年9月16日
 */
@Repository("invoiceDaoImpl")
public class InvoiceDaoImpl implements InvoiceDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 根据发票id修改状态
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

	// 根据发票id删除发票
	public boolean delete(Integer invoiceId) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = " update invoice set 'invo _isdelete' = 1  where invo_id =:invo_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("invo_id", invoiceId);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	// 根据合同ID，页数，关键字返回任务列表
	public List<Invoice> findByPage(Integer cont_id, String searchKey, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "select * from invoice where cont_id=:cont_id and invo _isdelete=0";
		if (null != searchKey) {
			selectSql += " and ( invo_firm like '%" + searchKey + "%' )";
		}
		selectSql += " order by invo_id  desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, Invoice.class);
		query.setParameter("cont_id", cont_id);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<Invoice> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据合同ID，关键字查询任务总条数
	@SuppressWarnings("unchecked")
	public Integer countByParam(Integer cont_id, String searchKey) {
		EntityManager em = emf.createEntityManager();
		String countSql = " select count(invoice_id) from invoice where invo _isdelete=0 and cont_id=:cont_id ";
		if (null != searchKey) {
			countSql += "   and (invo_firm like '%" + searchKey + "%'  )";
		}
		Query query = em.createNativeQuery(countSql);
		query.setParameter("cont_id", cont_id);
		List<Object> result = query.getResultList();
		em.close();
		return Integer.parseInt(result.get(0).toString());
	}

}
