/**
 * 
 */
package com.mvc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.AlarmDao;
import com.mvc.entity.Alarm;

/**
 * 报警
 * 
 * @author zjn
 * @date 2016年10月25日
 */
@Repository("alarmDaoImpl")
public class AlarmDaoImpl implements AlarmDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 根据参数统计报警列表条数，alarmType是数组类型:[2,3]
	@SuppressWarnings("unchecked")
	@Override
	public Integer countByParam(Integer user_id, String alarmType, String searchKey) {
		EntityManager em = emf.createEntityManager();
		String[] chars = alarmType.split(",");
		ArrayList<Integer> types = new ArrayList<Integer>();
		for (int i = 0; i < chars.length; i++) {
			types.add(Integer.valueOf(chars[i]));
		}
		String countSql = " select count(*) from (select count(alar_id) from alarm a where receiver_id=:receiver_id and alar_isremove=0 and alar_code in(:alar_code) ";
		// 判断查找关键字是否为空
		if (null != searchKey && searchKey != " ") {
			countSql += " and ( alar_content like '%" + searchKey + "%' )";
		}
		countSql += " group by task_id,reno_id,prst_id) as tmp  ";
		Query query = em.createNativeQuery(countSql);
		query.setParameter("receiver_id", user_id);
		query.setParameter("alar_code", types);
		List<Object> totalRow = query.getResultList();
		em.close();
		return Integer.parseInt(totalRow.get(0).toString());
	}

	// 查找报警信息列表
	@SuppressWarnings("unchecked")
	@Override
	public List<Alarm> findAlarmList(Integer user_id, String searchKey, String alarmType, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		String[] chars = alarmType.split(",");
		ArrayList<Integer> types = new ArrayList<Integer>();
		for (int i = 0; i < chars.length; i++) {
			types.add(Integer.valueOf(chars[i]));
		}
		String selectSql = "select count(1)num , alar_id,alar_time,alar_content,alar_code,alar_isremove,receiver_id,cont_id,task_id,reno_id,prst_id from Alarm where receiver_id=:receiver_id and alar_isremove=0 and alar_code in(:alar_code) ";
		// 判断查找关键字是否为空
		if (null != searchKey) {
			selectSql += " and ( alar_content like '%" + searchKey + "%' )";
		}
		selectSql += " group by task_id,reno_id,prst_id  ";
		selectSql += " order by alar_id desc limit :offset,:end ";
		Query query = em.createNativeQuery(selectSql, Alarm.class);
		query.setParameter("receiver_id", user_id);
		query.setParameter("alar_code", types);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<Alarm> list = query.getResultList();
		em.close();
		return list;
	}

	// 王睿：根据ID及其类型解除报警
	@Override
	public boolean updateByIdType(Integer Id, Integer IdType) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			String selectSql = "";
			Query query = null;
			if (IdType == 0) {
				selectSql = "update alarm set  `alar_isremove` = 1  where alar_id in (select alar_id from (select * from alarm where prst_id =:prst_id) a)";
				query = em.createNativeQuery(selectSql);
				query.setParameter("prst_id", Id);
			} else if (IdType == 1) {
				selectSql = "update alarm set  `alar_isremove` = 1  where alar_id in (select alar_id from (select * from alarm where task_id =:task_id) a)";
				query = em.createNativeQuery(selectSql);
				query.setParameter("task_id", Id);
			} else if (IdType == 2) {
				selectSql = "update alarm set  `alar_isremove` = 1  where alar_id in (select alar_id from (select * from alarm where reno_id =:reno_id) a)";
				query = em.createNativeQuery(selectSql);
				query.setParameter("reno_id", Id);
			}
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

}
