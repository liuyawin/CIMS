package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {

	// 查询合同总条数
	@Query("select count(cont_id) from contract c where c.creator_id=:creator_id and c.cont_ishistory=0")
	int countTotal(@Param("creator_id") Integer creator_id);

	// 根据合同名获取合同信息
	@Query("select * from contract c where c.creator_id=:creator_id and c.cont_name like '%:contName%'")
	List<Contract> findConByName(@Param("creator_id") Integer creator_id, String contName);
}
