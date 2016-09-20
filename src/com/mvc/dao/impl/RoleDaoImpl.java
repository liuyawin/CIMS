package com.mvc.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.RoleDao;
import com.mvc.repository.UserRepository;


/**
 * 角色，职位
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
@Repository("roleDaoImpl")
public class RoleDaoImpl implements RoleDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;
	@Autowired
	UserRepository userRepository;

	// 删除，修改角色状态列表
	public boolean updateState(Integer role_id) {
		EntityManager em = emf.createEntityManager();		
			em.getTransaction().begin();
			
			int count=userRepository.countRoleTotal(role_id);
			if (count<1) {
				String selectSql = " update role set `role_state` = 1  where role_id =:role_id ";
				Query query = em.createNativeQuery(selectSql);
				query.setParameter("role_id", role_id);
				query.executeUpdate();
				em.flush();
				em.getTransaction().commit();
				em.close();
				return true;
				}
			else {
				return false;
				}
				
	}

}
