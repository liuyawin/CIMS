package com.mvc.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.ProjectStageDao;

/**
 * 工期阶段
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
@Repository("projectStageDaoImpl")
public class ProjectStageDaoImpl implements ProjectStageDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 根据工期阶段id修改状态
	public boolean updateState(Integer id, Integer state) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = " update project_stage set 'prst_state' = :prst_state  where prst_id =:prst_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("prst_state", state);
			query.setParameter("prst_id", id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;

	}

}
