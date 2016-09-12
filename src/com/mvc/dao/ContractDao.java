package com.mvc.dao;
/**
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface ContractDao {
	// 根据合同id修改状态
		public boolean updateState(Integer id, Integer state);

}
