package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.UserDao;
import com.mvc.entity.Department;
import com.mvc.entity.User;

/**
 * User相关Dao层接口实现
 * 
 * @author zjn
 * @date 2016年9月7日
 */
@Repository("userDaoImpl")
public class UserDaoImpl implements UserDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// /**
	// * 对用户进行增加
	// */
	// public boolean increaseByContent(Department dept ){
	// EntityManager em = emf.createEntityManager();
	// String selectSql = " insert into
	// cims(user_id,user_email,user_isdelete,user_name,user_num,user_pwd,user_role,user_sex,user_tel)
	// values('"+dept.getDept_id()+"','"+ dept.getDept_fid() +"','"+
	// dept.getDept_name() +"','"+ dept.getDept_remark() +"','"+
	// dept.getDept_state() +"') ";
	// Query query = em.createNativeQuery(selectSql);
	// query.executeUpdate();
	// em.flush();
	// em.getTransaction().commit();
	// em.close();
	// return true;
	// }
	// 根据用户id修改状态
	public boolean updateState(Integer id, Integer user_delete) {
		EntityManager em = emf.createEntityManager();
		try {
			String selectSql = " update dept set 'user_delete' = :user_delete where user_id =:user_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("user_delete", user_delete);
			query.setParameter("user_id", id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}
}
