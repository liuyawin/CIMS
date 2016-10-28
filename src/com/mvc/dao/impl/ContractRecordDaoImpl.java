package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.ContractRecordDao;
import com.mvc.entity.ContractRecord;

/**
 * 合同日志持久层实现
 * 
 * @author wangrui
 * @date 2016-10-25
 */
@Repository("contractRecordDaoImpl")
public class ContractRecordDaoImpl implements ContractRecordDao {

	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 根据合同ID获取合同操作日志
	@SuppressWarnings("unchecked")
	@Override
	public List<ContractRecord> selectContRecordByContId(Integer cont_id) {
		EntityManager em = emf.createEntityManager();
		String sql = "select * from contract_record cr where cr.cont_id=:cont_id";
		Query query = em.createNativeQuery(sql, ContractRecord.class);
		query.setParameter("cont_id", cont_id);
		List<ContractRecord> list = query.getResultList();
		em.close();
		return list;
	}

}
