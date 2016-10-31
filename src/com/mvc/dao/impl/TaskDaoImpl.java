package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.base.enums.TaskStatus;
import com.mvc.dao.TaskDao;
import com.mvc.entity.Task;
import com.mvc.repository.SubTaskRepository;
import com.mvc.repository.TaskRepository;

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
	@Autowired
	TaskRepository taskRepository;
	@Autowired
	SubTaskRepository subTaskRepository;

	// 根据任务id修改状态
	public boolean delete(Integer id) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			String selectSql = "update task set  `task_isdelete` = 1  where task_id =:task_id";
			Query query = em.createNativeQuery(selectSql);
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
			em.getTransaction().begin();
			String selectSql = " update task set `task_state` = :task_state  where task_id =:task_id ";
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

	// 根据页数,状态，关键字返回任务列表
	@SuppressWarnings("unchecked")
	public List<Task> findByPage(Integer user_id, Integer task_state, String searchKey, Integer offset, Integer end,
			Integer sendOrReceive) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "";
		// task_state == -1表示查询所有状态任务，否则按状态查找
		if (task_state == -1) {
			// 0表示发送，1表示接收
			if (sendOrReceive == 1) {
				selectSql = "select * from task where  receiver_id =:user_id and task_isdelete=0";
			} else {
				selectSql = "select * from task where  creator_id =:user_id  and task_isdelete=0";
			}
		} else {
			// 0表示发送，1表示接收
			if (sendOrReceive == 1) {
				selectSql = "select * from task where  receiver_id =:user_id and task_state=:task_state and task_isdelete=0";
			} else {
				selectSql = "select * from task where  creator_id =:user_id and task_state=:task_state and task_isdelete=0";
			}
		}
		// 判断查找关键字是否为空
		if (null != searchKey) {
			selectSql += " and ( task_content like '%" + searchKey + "%' )";
		}
		selectSql += " order by task_id desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, Task.class);
		query.setParameter("user_id", user_id);
		if (task_state != -1) {
			query.setParameter("task_state", task_state);
		}
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<Task> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据状态，关键字查询任务总条数
	@SuppressWarnings("unchecked")
	public Integer countByParam(Integer user_id, Integer task_state, String searchKey, Integer sendOrReceive) {
		EntityManager em = emf.createEntityManager();
		String countSql = "";
		// task_state == -1表示查询所有状态任务，否则按状态查找
		if (task_state == -1) {
			// 0表示发送，1表示接收
			if (sendOrReceive == 1) {
				countSql = "select count(task_id) from task where task_isdelete=0 and receiver_id=:user_id";
			} else {
				countSql = "select count(task_id) from task where task_isdelete=0 and creator_id=:user_id";
			}
		} else {
			// 0表示发送，1表示接收
			if (sendOrReceive == 1) {
				countSql = " select count(task_id) from task where task_isdelete=0 and task_state=:task_state and receiver_id=:user_id";
			} else {
				countSql = " select count(task_id) from task where task_isdelete=0 and task_state=:task_state and creator_id=:user_id ";
			}
		}
		if (null != searchKey) {
			countSql += "   and (task_content like '%" + searchKey + "%'  )";
		}
		Query query = em.createNativeQuery(countSql);
		query.setParameter("user_id", user_id);
		if (task_state != -1) {
			query.setParameter("task_state", task_state);
		}
		List<Object> totalRow = query.getResultList();
		em.close();
		return Integer.parseInt(totalRow.get(0).toString());
	}

	// 根据合同ID和任务类型返回任务列表
	@SuppressWarnings("unchecked")
	public List<Task> findByContIdAndType(Integer user_id, Integer contId, Integer taskType) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "select * from task where  creator_id =:user_id and task_type=:task_type and cont_id=:cont_id and task_isdelete=0";
		selectSql += " order by task_id desc ";
		Query query = em.createNativeQuery(selectSql, Task.class);
		query.setParameter("user_id", user_id);
		query.setParameter("task_type", taskType);
		query.setParameter("cont_id", contId);
		List<Task> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据任务类型获取任务条数
	@SuppressWarnings("unchecked")
	public Integer countByType(Integer userId, Integer taskType) {
		EntityManager em = emf.createEntityManager();
		String countSql = " select count(task_id) from task where task_isdelete=0 and task_state=:task_state and receiver_id=:user_id and task_type=:task_type ";
		Query query = em.createNativeQuery(countSql);
		query.setParameter("task_state", TaskStatus.waitingReceipt.value);
		query.setParameter("user_id", userId);
		query.setParameter("task_type", taskType);
		List<Object> totalRow = query.getResultList();
		em.close();
		return Integer.parseInt(totalRow.get(0).toString());
	}

}
