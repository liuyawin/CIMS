package com.mvc.dao.impl;

import java.math.BigInteger;
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
import com.mvc.repository.UserRepository;

/**
 * 报警
 * 
 * @author wanghuimin
 * @date 2016年9月26日
 */
@Repository("alarmDaoImpl")
public class AlarmDaoImpl implements AlarmDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;
	@Autowired
	UserRepository userRepository;

	// 查找报警信息列表
	@SuppressWarnings("unchecked")
	@Override
	public List<Alarm> findAlarmInformationList(Integer user_id, String searchKey, String alarmType, Integer offset,
			Integer end) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
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

	// 根据用户名查找报警信息
	@SuppressWarnings("unchecked")
	@Override
	public List<Alarm> findAlarmByUser(String username, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Integer userid = userRepository.findUserByUsername(username);
		String selectSql = "select * from Alarm where receiver_id=:receiver_id ";
		selectSql += "order by alar_id desc limit :offset,:end";
		Query query = em.createNamedQuery(selectSql, Alarm.class);
		query.setParameter("receiver_id", userid);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<Alarm> list = query.getResultList();
		em.close();
		return list;
	}

	// 统计报警条数
	@Override
	public Integer countAlarmTotalNum(String searchKey) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		String countSql = "";
		countSql = "select count(alar_id) from alarm  ";
		Query query = em.createNamedQuery(countSql);
		Integer reveiverid;
		if (null != searchKey) {
			reveiverid = userRepository.findUserByUsername(searchKey);
			countSql += " where receiver_id=:receiver_id  ";
			query.setParameter("receiver", reveiverid);
		}
		List<Object> totalRow = query.getResultList();
		em.close();
		return Integer.parseInt(totalRow.get(0).toString());
	}

	// 根据ID及其类型解除报警
	public Boolean updateByIdType(Integer Id, Integer IdType) {
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

	// 张姣娜添加：统计报警列表条数，alarmType:2,3
	public Integer countAlarmTotal(Integer user_id, String alarmType, String searchKey) {
		EntityManager em = emf.createEntityManager();
		String[] chars = alarmType.split(",");
		ArrayList<Integer> types = new ArrayList<Integer>();
		for (int i = 0; i < chars.length; i++) {
			types.add(Integer.valueOf(chars[i]));
		}
		System.out.println("types:" + chars.toString());
		String countSql = " select count(*) from (select count(alar_id) from alarm a where receiver_id=:receiver_id and alar_isremove=0 and alar_code in(:alar_code) ";
		// 判断查找关键字是否为空
		if (null != searchKey) {
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
}
