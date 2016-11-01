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

import com.base.enums.ReceiveMoneyStatus;
import com.base.enums.RemoStatus;
import com.mvc.dao.ReceiveMoneyDao;
import com.mvc.entity.ReceiveMoney;

/**
 * 到款
 * 
 * @author zjn
 * @date 2016年10月27日
 */
@Repository("receiveMoneyDaoImpl")
public class ReceiveMoneyDaoImpl implements ReceiveMoneyDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 根据合同ID获取已到款钱数
	@SuppressWarnings("unchecked")
	@Override
	public Float receiveMoneyByContId(Integer contId) {
		EntityManager em = emf.createEntityManager();
<<<<<<< HEAD
		String countSql = " select coalesce(sum(remo_amoney),0) from receive_money r where cont_id=:cont_id and remo_state=:remo_state";
=======
		String countSql = " select coalesce(sum(remo_amoney),0) from receive_money r where cont_id=:cont_id ";
>>>>>>> 3efde56aed096a1c39cfbb65dbfc594a83871e71
		Query query = em.createNativeQuery(countSql);
		query.setParameter("cont_id", contId);
		query.setParameter("remo_state", RemoStatus.finish.value);
		List<Object> result = query.getResultList();
		em.close();
		return Float.valueOf(result.get(0).toString());
	}

	// 根据参数获取该合同的所有到款记录
	@SuppressWarnings("unchecked")
	@Override
	public List<ReceiveMoney> findListByParam(Integer contId, Integer remoState, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "";
		if (remoState != ReceiveMoneyStatus.all.value) {
			selectSql += "select * from receive_money where  cont_id =:cont_id and remo_state=:remo_state ";
		} else {
			selectSql += "select * from receive_money where  cont_id =:cont_id  ";
		}
		selectSql += " order by remo_id desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, ReceiveMoney.class);
		query.setParameter("cont_id", contId);
		if (remoState != ReceiveMoneyStatus.all.value) {
			query.setParameter("remo_state", remoState);
		}
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<ReceiveMoney> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据参数获取该合同的所有到款记录总条数
	@SuppressWarnings("unchecked")
	@Override
	public Integer countByParam(Integer contId, Integer remoState) {
		EntityManager em = emf.createEntityManager();
		String countSql = "";
		if (remoState != ReceiveMoneyStatus.all.value) {
			countSql += "select count(*) from receive_money where  cont_id =:cont_id and remo_state=:remo_state ";
		} else {
			countSql += "select count(*)  from receive_money where  cont_id =:cont_id  ";
		}
		Query query = em.createNativeQuery(countSql);
		query.setParameter("cont_id", contId);
		if (remoState != ReceiveMoneyStatus.all.value) {
			query.setParameter("remo_state", remoState);
		}
		List<Object> result = query.getResultList();
		em.close();
		return Integer.parseInt(result.get(0).toString());
	}

	// 审核到款记录
	@Override
	public Boolean updateRemoStateById(Integer remoId, Float remoAmoney) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			String selectSql = " update receive_money set  remo_amoney=:remo_amoney,remo_state=:remo_state  where remo_id =:remo_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("remo_id", remoId);
			query.setParameter("remo_amoney", remoAmoney);
			query.setParameter("remo_state", ReceiveMoneyStatus.finish.value);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

	// 根据状态查询到款记录
	@SuppressWarnings("unchecked")
	@Override
	public List<ReceiveMoney> findListByState(Integer userId, Integer remoState, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "";
		if (remoState != ReceiveMoneyStatus.all.value) {
			selectSql += "select * from receive_money where  (operater_id =:operater_id or creater_id=:creater_id) and remo_state=:remo_state ";
		} else {
			selectSql += "select * from receive_money where  operater_id =:operater_id or creater_id=:creater_id  ";
		}
		selectSql += " order by remo_id desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, ReceiveMoney.class);
		query.setParameter("operater_id", userId);
		query.setParameter("creater_id", userId);
		if (remoState != ReceiveMoneyStatus.all.value) {
			query.setParameter("remo_state", remoState);
		}
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<ReceiveMoney> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据状态查询到款记录总条数
	@SuppressWarnings("unchecked")
	@Override
	public Integer countByState(Integer userId, Integer remoState) {
		EntityManager em = emf.createEntityManager();
		String countSql = "";
		if (remoState != ReceiveMoneyStatus.all.value) {
			countSql += "select count(*) from receive_money where  (operater_id =:operater_id or creater_id=:creater_id) and remo_state=:remo_state ";
		} else {
			countSql += "select count(*)  from receive_money where operater_id =:operater_id or creater_id=:creater_id ";
		}
		Query query = em.createNativeQuery(countSql);
		query.setParameter("operater_id", userId);
		query.setParameter("creater_id", userId);
		if (remoState != ReceiveMoneyStatus.all.value) {
			query.setParameter("remo_state", remoState);
		}
		List<Object> result = query.getResultList();
		em.close();
		return Integer.parseInt(result.get(0).toString());
	}
}
