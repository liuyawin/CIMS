package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.ReceiveNodeDao;
import com.mvc.entity.ReceiveNode;

/**
 * 收款节点
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
@Repository("ReceiveNodeDaoIpml")
public class ReceiveNodeDaoImpl implements ReceiveNodeDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 修改收款节点状态
	public Boolean updateState(Integer id, Integer state) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = "update recive_node rn set rn.reno_state=:reno_state where rn.reno_id=:reno_id and rn.reno_isdelete=0";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("reno_state", state);
			query.setParameter("reno_id", id);
			query.executeUpdate();
			em.flush();
		} finally {
			em.close();
		}
		return true;
	}

	// 根据合同ID查找收款节点
	@SuppressWarnings("unchecked")
	@Override
	public List<ReceiveNode> selectRenoByContId(Integer cont_id) {
		EntityManager em = emf.createEntityManager();
		String sql = "select * from receive_node rn where rn.cont_id=:cont_id  and rn.reno_isdelete=0";
		Query query = em.createNativeQuery(sql, ReceiveNode.class);
		query.setParameter("cont_id", cont_id);
		List<ReceiveNode> list = query.getResultList();
		em.close();
		return list;
	}

}
