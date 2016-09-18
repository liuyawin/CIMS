package com.mvc.dao.impl;

import java.math.BigInteger;
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
		StringBuilder sql = new StringBuilder();
		try {
			em.getTransaction().begin();
			sql.append("update contract c set c.cont_state=:cont_state where c.cont_id=:cont_id");
			Query query = em.createNativeQuery(sql.toString());
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

	// 返回欠款合同信息
	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findAllDebtCont(int creator_id, String contName, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from contract c where c.cont_id in (select distinct(rn.cont_id) from receive_node rn ");
		sql.append(
				"where rn.reno_time<=now() and rn.reno_state in (0,2)) and c.creator_id=:creator_id and c.cont_ishistory=0");
		if (null != contName) {
			sql.append(" and c.cont_name like '%" + contName + "%'");
		}
		sql.append(" order by cont_id limit :offset,:end");
		Query query = em.createNativeQuery(sql.toString(), Contract.class);
		query.setParameter("creator_id", creator_id).setParameter("offset", offset).setParameter("end", end);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	// 返回逾期合同信息
	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findAllOverdueCont(int creator_id, String contName, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select * from contract c where c.cont_id in (select distinct(t.cont_id) from task t where t.task_etime<=now()");
		sql.append(
				" and t.task_state in (0,1) and t.task_isdelete=0) and c.creator_id=:creator_id and c.cont_ishistory=0");
		if (null != contName) {
			sql.append(" and c.cont_name like '%" + contName + "%'");
		}
		sql.append(" order by cont_id limit :offset,:end");
		Query query = em.createNativeQuery(sql.toString(), Contract.class);
		query.setParameter("creator_id", creator_id).setParameter("offset", offset).setParameter("end", end);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据合同名获取合同信息
	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findConByName(int creator_id, String contName, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from contract c where c.creator_id=:creator_id and c.cont_ishistory=0");
		if (null != contName) {
			sql.append(" and c.cont_name like '%" + contName + "%'");
		}
		sql.append(" order by cont_id limit :offset,:end");
		Query query = em.createNativeQuery(sql.toString(), Contract.class);
		query.setParameter("creator_id", creator_id).setParameter("offset", offset).setParameter("end", end);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据创建者ID和合同名查询合同总条数
	@Override
	public Long countTotal(int creator_id, String contName, String methodType) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(cont_id) from contract c where c.creator_id=:creator_id and c.cont_ishistory=0 ");
		if (methodType.equals("name")) {// 根据name查询

		} else if (methodType.equals("Debt")) {// 查询欠款
			sql.append(
					" and c.cont_id in (select distinct(rn.cont_id) from receive_node rn where rn.reno_time<=now() and rn.reno_state in (0,2))");
		} else if (methodType.equals("Overdue")) {// 查询逾期
			sql.append(
					" and c.cont_id in (select distinct(t.cont_id) from task t where t.task_etime<=now() and t.task_state in (0,1) and t.task_isdelete=0)");
		}
		if (null != contName) {
			sql.append(" and c.cont_name like '%" + contName + "%'");
		}
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter("creator_id", creator_id);
		BigInteger totalRow = (BigInteger) query.getSingleResult();// count返回值为BigInteger类型
		em.close();
		return totalRow.longValue();
	}

	// 删除合同
	@Override
	public boolean delete(Integer cont_id) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		try {
			em.getTransaction().begin();
			sql.append("update contract c set c.cont_ishistory=1 where c.cont_id=:cont_id");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("cont_id", cont_id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}
}
