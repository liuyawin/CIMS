package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.FileDao;
import com.mvc.entity.Files;

/**
 * 文件持久层实现
 * 
 * @author wangrui
 * @date 2016-10-18
 */
@Repository("fileDaoImpl")
public class FileDaoImpl implements FileDao {

	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 根据合同ID获取文件列表
	@SuppressWarnings("unchecked")
	@Override
	public List<Files> findFileByConId(Integer cont_id) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from files f where f.file_isdelete=0 and f.cont_id=:cont_id order by f.file_id");
		Query query = em.createNativeQuery(sql.toString(), Files.class);
		query.setParameter("cont_id", cont_id);
		List<Files> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据合同ID删除合同
	@Override
	public Boolean delete(Integer file_id) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		try {
			em.getTransaction().begin();
			sql.append("update files f set f.file_isdelete=1 where f.file_id=:file_id");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("file_id", file_id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

}
