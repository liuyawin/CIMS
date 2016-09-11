package com.mvc.dao;
/**
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface TaskDao {
	    // 根据任务id修改状态
		public boolean updateState(Integer id, Integer state);
		// 根据任务id修改删除状态,相当于删除
		public boolean delete(Integer id, Integer isdelete);

}
