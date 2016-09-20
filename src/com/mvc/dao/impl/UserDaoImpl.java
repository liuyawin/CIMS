package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.UserDao;
import com.mvc.entity.User;
import com.mvc.entity.UserDeptRelation;
import com.mvc.repository.DepartmentRepository;
import com.mvc.repository.UserRepository;

/**
 * User相关Dao层接口实现
 * 
 * @author wanghuimin
 * @date 2016年9月7日
 */
@Repository("userDaoImpl")
public class UserDaoImpl implements UserDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;
	@Autowired
	DepartmentRepository departmentRepository;
	@Autowired
	UserRepository userRepository;


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

	/**
	 * 删除用户
	 */
	public boolean updateState(Integer id) {
		EntityManager em = emf.createEntityManager();		
			em.getTransaction().begin();
			int count=userRepository.countUserByroleid(id);
			if(count<1){
				try {
					String selectSql = " update user set `user_isdelete` = 1 where user_id =:user_id ";
					Query query = em.createNativeQuery(selectSql);
					query.setParameter("user_id", id);
					query.executeUpdate();
					String selectSql1=" update user_dept_relation set `user_dept_relation_state`=1 where user_id=:user_id ";
					Query query1=em.createNativeQuery(selectSql1);
					query1.setParameter("user_id", id);
					query1.executeUpdate();			
					em.flush();
					em.getTransaction().commit();
				} finally {
					em.close();
				}
				return true;
				}
			else
				return false;
		
	}
	//根据页数筛选全部用户列表
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUserAllByPage(Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		String selectSql = "select * from User where user_isdelete=0";		
		selectSql += " order by user_id desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, User.class);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<User> list = query.getResultList();
		em.close();
		return list;
	}
	//只要设计部人员列表	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserDeptRelation> findUserFromDesign() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();		
		int deptid=departmentRepository.findOnlyUserDesign();		
		String selectSql = "select * from user_dept_relation where dept_id="+ deptid +" and user_dept_relation_state=0";	
		Query query = em.createNativeQuery(selectSql, UserDeptRelation.class);
		List<UserDeptRelation> list = query.getResultList();
		em.close();
		return list;
	}
}
