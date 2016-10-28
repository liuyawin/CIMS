package com.mvc.dao.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.base.enums.InvoiceStatus;
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
	public boolean invoiceFinish(Integer id, Date invoTime) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			String selectSql = " update invoice set invo_state = :invo_state ,invo_time=:invo_time where invo_id =:invo_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("invo_state", InvoiceStatus.finish.value);
			query.setParameter("invo_id", id);
			query.setParameter("invo_time", invoTime);
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
	// 根据合同ID，页数，关键字返回发票列表
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
	public Integer countByContId(Integer cont_id) {
		EntityManager em = emf.createEntityManager();
		String countSql = " select count(invo_id) from invoice where invo_isdelete=0 and contract_id=:contract_id ";
		Query query = em.createNativeQuery(countSql);
		query.setParameter("contract_id", cont_id);
		BigInteger totalRow = (BigInteger) query.getSingleResult();// count返回值为BigInteger类型
		em.close();
		return totalRow.intValue();
	}

	// 根据合同ID查询发票总金额
	@SuppressWarnings("unchecked")
	public Float totalMoneyOfInvoice(Integer contId) {
		EntityManager em = emf.createEntityManager();
		String countSql = " select sum(invo_money) from invoice i where contract_id=:contract_id ";
		Query query = em.createNativeQuery(countSql);
		query.setParameter("contract_id", contId);
		List<Object> totalRow = query.getResultList();
		em.close();
		return Float.valueOf(totalRow.get(0).toString());
	}

	// 根据用户id，页数返回发票列表
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

	// 根据用户ID查询发票总条数
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

	// 按发票状态获取列表
	@SuppressWarnings("unchecked")
	public Integer WaitingDealCountByParam(Integer user_id, Integer invoiceState) {
		EntityManager em = emf.createEntityManager();
		String countSql = " select count(invo_id) from invoice where invo_isdelete=0 and receiver_id=:receiver_id and invo_state=:invo_state ";
		Query query = em.createNativeQuery(countSql);
		query.setParameter("receiver_id", user_id);
		query.setParameter("invo_state", invoiceState);
		List<Object> totalRow = query.getResultList();
		em.close();
		return Integer.parseInt(totalRow.get(0).toString());
	}

	// 根据用户id，页数返回发票列表
	@SuppressWarnings("unchecked")
	public List<Invoice> WaitingDealFindByPage(Integer user_id, Integer invoiceState, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "select * from invoice where receiver_id=:receiver_id and invo_isdelete=0 and invo_state=:invo_state";
		selectSql += " order by invo_id  desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, Invoice.class);
		query.setParameter("receiver_id", user_id);
		query.setParameter("invo_state", invoiceState);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<Invoice> list = query.getResultList();
		em.close();
		return list;
	}

	// 主任转发发票
	public boolean transmitInvoice(Integer invoiceId, Date invoEtime, Integer receiverId) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			String selectSql = " update invoice set `invo_state` = :invo_state ,`receiver_id` = :receiver_id ,`invo_etime` = :invo_etime where invo_id =:invo_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("invo_state", InvoiceStatus.waitdealing.value);
			query.setParameter("invo_id", invoiceId);
			query.setParameter("invo_etime", invoEtime);
			query.setParameter("receiver_id", receiverId);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

	// 根据发票状态查找发票
	@SuppressWarnings("unchecked")
	@Override
	public List<Invoice> findByStateAndPerm(Integer invoState, String permission, Integer user_id) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from invoice where invo_isdelete=0");
		if (permission.contains("tInvoAdd")) {// 开发票权限（主任、设总）
			if (!permission.contains("tInvoAudit")) {// 无审核发票权限（设总）
				sql.append(" and creator_id=:user_id");
			}
			if (invoState == -1) {// -1：全部，0：待审核，1：待处理，2：已完成
				sql.append(" and invo_state in(0,1,2)");
			} else {
				sql.append(" and invo_state=：invoState");
			}
		} else {// 执行人(文书)
			sql.append(" and receiver_id=:user_id");
			if (invoState == -1) {// -1：全部，0：待审核，1：待处理，2：已完成
				sql.append(" and invo_state in(1,2)");
			} else {
				sql.append(" and invo_state=：invoState");
			}
		}
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter("user_id", user_id).setParameter("invoState", invoState);
		List<Invoice> list = query.getResultList();
		em.close();
		return list;
	}

}
