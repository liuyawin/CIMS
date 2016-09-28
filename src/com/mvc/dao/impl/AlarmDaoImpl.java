package com.mvc.dao.impl;

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
	public List<Alarm> findAlarmInformationList(Integer user_id, Integer isremove, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		String selectSql = "select * from Alarm where receiver_id=:receiver_id and alar_isremove=:alarisremove ";
		selectSql += " order by alar_id desc limit :offset,:end";
		Query query = em.createNativeQuery(selectSql, Alarm.class);
		query.setParameter("receiver_id", user_id);
		query.setParameter("alarisremove", isremove);
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
		return list;
	}

	// 统计报警条数
	@SuppressWarnings("unchecked")
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
		List<Object> result = query.getResultList();
		em.close();
		return Integer.parseInt(result.get(0).toString());
	}

}
