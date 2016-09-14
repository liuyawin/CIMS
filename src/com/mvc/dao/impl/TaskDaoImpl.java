package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.TaskDao;
import com.mvc.entity.Task;

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

	// 根据页数返回任务列表
	public List<Task> findByPage(Integer receiver_id, Integer task_state, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "select * from task where  receiver_id =:receiver_id and task_state=:task_state";
		// if (null != name) {
		// selectSql += " and (dname like '%" + name + "%' or dno like '%" +
		// name + "%' )";
		// }
		selectSql += " order by id desc	limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, Task.class);
		query.setParameter("receiver_id", receiver_id);
		query.setParameter("task_state", task_state);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<Task> list = query.getResultList();
		em.close();
		return list;
	}

}
