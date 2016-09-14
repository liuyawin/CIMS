package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Contract;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

	// 查询合同总条数
	@Query("select count(cont_id) from Contract c where creator_id=:creator_id and cont_ishistory=0")
	Long countTotal(@Param("creator_id") Integer creator_id);

}
