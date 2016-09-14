package com.mvc.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.SubTaskDao;

/**
 * 文书子任务
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
@Repository("subTaskDaoImpl")
public class SubTaskDaoImpl implements SubTaskDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 根据子任务id修改状态
	public boolean updateState(Integer id, Integer state) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = " update sub_task set 'suta_state' = :suta_state  where suta_id =:suta_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("suta_state", state);
			query.setParameter("suta_id", id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;

	}

}
