package com.mvc.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.AlarmLevelDao;

/**
 * 报警等级
 * @author wanghuimin
 * @date 2016年9月22日
 */
@Repository("alarmLevelDaoImpl")
public class AlarmLevelDaoImpl implements AlarmLevelDao{
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 根据id删除报警等级
	@Override
	public boolean deleteAlarmLevelById(Integer alleid) {
		EntityManager em=emf.createEntityManager();
		try{
		em.getTransaction().begin();
		String selectSql="delete from alarm_level where alle_id=:alle_id ";
		Query query=em.createNativeQuery(selectSql);
		query.setParameter("alle_id", alleid);
		
		query.executeUpdate();
		em.flush();
		em.getTransaction().commit();
		}
		finally{
			em.close();
		}	
		return true;
	}

}
