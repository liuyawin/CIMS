package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.ContractDao;
import com.mvc.entity.Contract;

/**
 * 合同
 * 
 * @author wangrui
 * @date 2016年9月13日
 */
@Repository("contractDaoImpl")
public class ContractDaoImpl implements ContractDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 根据合同id修改状态
	public boolean updateState(Integer cont_id, Integer cont_state) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = "update contract c set c.cont_state=:state where c.cont_id=:cont_id";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("cont_state", cont_state);
			query.setParameter("cont_id", cont_id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findAllCont(int creator_id) {
		EntityManager em = emf.createEntityManager();
		String sql = "select * from contract c where c.user_id=:creator_id and c.cont_ishistory=0";
		// 创建原生SQL查询QUERY实例,指定了返回的实体类型
		Query query = em.createNativeQuery(sql, Contract.class);
		query.setParameter("creator_id", creator_id);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findAllDebtCont(int creator_id) {
		EntityManager em = emf.createEntityManager();
		String sql = "select * from contract c where c.cont_id in (select rn.cont_id from receive_node rn "
				+ "where rn.reno_time<=now() and rn.reno_state in (0,2)) and c.user_id=:creator_id and c.cont_ishistory=0";
		Query query = em.createNativeQuery(sql, Contract.class);
		query.setParameter("creator_id", creator_id);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findAllOverdueCont(int creator_id) {
		EntityManager em = emf.createEntityManager();
		String sql = "select * from contract c where c.cont_id in (select t.cont_id from task t "
				+ "where t.task_etime<=now() and t.task_state in (0,1) and t.task_isdelete=0) and c.user_id=:creator_id and c.cont_ishistory=0";
		Query query = em.createNativeQuery(sql, Contract.class);
		query.setParameter("creator_id", creator_id);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

}
