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
			em.getTransaction().begin();
			String selectSql = " update invoice set `invo_state` = :invo_state  where invo_id =:invo_id ";
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
			em.getTransaction().begin();
			String selectSql = " update invoice set `invo _isdelete` = 1  where invo_id =:invo_id ";
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
	public List<Invoice> findByContId(Integer cont_id, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "select * from invoice where contract_id=:contract_id and invo_isdelete=0 ";
		selectSql += " order by invo_id  desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, Invoice.class);
		query.setParameter("contract_id", cont_id);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<Invoice> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据合同ID，关键字查询发票总条数
	@SuppressWarnings("unchecked")
	public Integer countByContId(Integer cont_id) {
		EntityManager em = emf.createEntityManager();
		String countSql = " select count(invo_id) from invoice where invo_isdelete=0 and contract_id=:contract_id ";
		Query query = em.createNativeQuery(countSql);
		query.setParameter("contract_id", cont_id);
		List<Object> result = query.getResultList();
		em.close();
		return Integer.parseInt(result.get(0).toString());
	}

	// 根据合同ID查询发票总金额
	@SuppressWarnings("unchecked")
	public Float totalMoneyOfInvoice(Integer contId) {
		EntityManager em = emf.createEntityManager();
		String countSql = " select sum(invo_money) from invoice i where contract_id=:contract_id ";
		Query query = em.createNativeQuery(countSql);
		query.setParameter("contract_id", contId);
		List<Object> result = query.getResultList();
		em.close();
		return Float.valueOf(result.get(0).toString());
	}

	// 根据用户id，页数返回任务列表
	@SuppressWarnings("unchecked")
	public List<Invoice> findByPage(Integer user_id, Integer ifSend, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "select * from invoice where audit_id=:audit_id and invo_isdelete=0 and invo_state=:invo_state";
		selectSql += " order by invo_id  desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, Invoice.class);
		query.setParameter("audit_id", user_id);
		query.setParameter("invo_state", ifSend);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<Invoice> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据用户ID查询任务总条数
	@SuppressWarnings("unchecked")
	public Integer countByParam(Integer user_id, Integer ifSend) {
		EntityManager em = emf.createEntityManager();
		String countSql = " select count(invo_id) from invoice where invo_isdelete=0 and audit_id=:audit_id and invo_state=:invo_state ";
		Query query = em.createNativeQuery(countSql);
		query.setParameter("audit_id", user_id);
		query.setParameter("invo_state", ifSend);
		List<Object> result = query.getResultList();
		em.close();
		return Integer.parseInt(result.get(0).toString());
	}

}
