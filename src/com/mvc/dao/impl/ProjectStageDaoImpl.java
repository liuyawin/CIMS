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
//	public Boolean updateState(Integer id, Integer state) {
//		EntityManager em = emf.createEntityManager();
//		try {
//			em.getTransaction().begin();
//			String selectSql = "update project_stage ps set ps.prst_state=:prst_state where ps.prst_id =:prst_id and ps.prst_isdelete=0";
//			Query query = em.createNativeQuery(selectSql);
//			query.setParameter("prst_state", state);
//			query.setParameter("prst_id", id);
//			query.executeUpdate();
//			em.flush();
//			em.getTransaction().commit();
//		} finally {
//			em.close();
//		}
//		return true;
//	}

	// 修改成完成工期
	@Override
	public Boolean updatePrstState(Integer prst_id) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			String sql = "update project_stage ps set ps.prst_state=1,prst_atime=now() where ps.prst_id=:prst_id and ps.prst_isdelete=0";
			Query query = em.createNativeQuery(sql);
			query.setParameter("prst_id", prst_id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

	// 删除工期
	@Override
	public Boolean deletePrstState(Integer prst_id) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			String sql = "update project_stage ps set ps.prst_isdelete=1 where ps.prst_id=:prst_id";
			Query query = em.createNativeQuery(sql);
			query.setParameter("prst_id", prst_id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

}
