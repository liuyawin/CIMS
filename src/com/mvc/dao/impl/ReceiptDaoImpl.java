/**
 * 
 */
package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.base.enums.IsDelete;
import com.mvc.dao.ReceiptDao;
import com.mvc.entity.Receipt;

/**
 * 收据
 * 
 * @author zjn
 * @date 2016年9月15日
 */
@Repository("receiptDaoImpl")
public class ReceiptDaoImpl implements ReceiptDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 根据合同ID和页码查询该合同对应的所有收据
	@SuppressWarnings("unchecked")
	public List<Receipt> findByPage(Integer cont_id, String searchKey, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "select * from receipt where  cont_id =:cont_id and rece_isdelete=:rece_isdelete";
		if (null != searchKey) {
			selectSql += " and (rece_firm like '%" + searchKey + "%' )";
		}
		selectSql += " order by rece_id desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, Receipt.class);
		query.setParameter("cont_id", cont_id);
		query.setParameter("rece_isdelete", IsDelete.NO.value);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<Receipt> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据合同ID和搜索的关键字查询该合同对应的所有收据总条数
	@SuppressWarnings("unchecked")
	public Integer countByParam(Integer cont_id, String searchKey) {
		EntityManager em = emf.createEntityManager();
		String countSql = " select count(rece_id) from receipt where cont_id=:cont_id and rece_isdelete=:rece_isdelete";
		if (null != searchKey) {
			countSql += "   and (rece_firm like '%" + searchKey + "%'  )";
		}
		Query query = em.createNativeQuery(countSql);
		query.setParameter("cont_id", cont_id);
		query.setParameter("rece_isdelete", IsDelete.NO.value);
		List<Object> result = query.getResultList();
		em.close();
		return Integer.parseInt(result.get(0).toString());
	}

	// 根据合同ID查询收据总金额
	@SuppressWarnings("unchecked")
	public Float totalMoneyOfReceipt(Integer contId) {
		EntityManager em = emf.createEntityManager();
		String countSql = " select coalesce(sum(rece_money),0) from receipt r where cont_id=:cont_id and rece_isdelete=:rece_isdelete";
		Query query = em.createNativeQuery(countSql);
		query.setParameter("cont_id", contId);
		query.setParameter("rece_isdelete", IsDelete.NO.value);
		List<Object> result = query.getResultList();
		em.close();
		return Float.valueOf(result.get(0).toString());
	}

	// 根据收据ID删除收据
	public Boolean delete(Integer receId) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			String updateSql = " update receipt set rece_isdelete =:rece_isdelete where rece_id =:rece_id ";
			Query query = em.createNativeQuery(updateSql);
			query.setParameter("rece_id", receId);
			query.setParameter("rece_isdelete", IsDelete.YES.value);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

}
