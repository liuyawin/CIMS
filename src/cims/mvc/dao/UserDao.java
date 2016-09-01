package cims.mvc.dao;

import cims.mvc.entity.User;

/**
 * User相关Dao层接口
 * 
 * @author zjn
 */
public interface UserDao {

	// 根据用户名查找用户信息
	public User findByUsername(String name);
}
