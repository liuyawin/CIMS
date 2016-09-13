package com.mvc.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.TaskDao;

/**
 * 文书任务
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
@Repository("taskDaoImpl")
public class TaskDaoImpl implements TaskDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 根据任务id修改状态
	public boolean updateState(Integer id, Integer state) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = " update task set 'task_state' = :task_state  where task_id =:task_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("task_state", state);
			query.setParameter("task_id", id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

	// 根据任务id修改状态
	public boolean delete(Integer id, Integer isdelete) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = " update task set 'task_isdelete' = :task_isdelete  where task_id =:task_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("task_isdelete", isdelete);
			query.setParameter("task_id", id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

}
