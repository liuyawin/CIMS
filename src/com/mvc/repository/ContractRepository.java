package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Contract;

/**
 * 合同JPA
 * 
 * @author wangrui
 * @date 2016-09-10
 */
public interface ContractRepository extends JpaRepository<Contract, Integer> {

	// 查询合同总条数
	@Query("select count(cont_id) from Contract c where creator_id=:creator_id and cont_ishistory=0")
	Long countTotal(@Param("creator_id") Integer creator_id);

	// 根据合同ID获取合同
	@Query("select c from Contract c where cont_id=:cont_id and cont_ishistory=0")
	Contract selectContById(@Param("cont_id") Integer cont_id);
}
