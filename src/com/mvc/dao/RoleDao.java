package com.mvc.dao;
/**
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface RoleDao {
	// 根据角色id修改状态
	public boolean updateState(Integer id, Integer state);

}
