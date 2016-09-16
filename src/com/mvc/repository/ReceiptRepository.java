/**
 * 
 */
package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Receipt;

/**
 * 收据
 * 
 * @author zjn
 * @date 2016年9月9日
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

	// 根据合同ID查询该合同对应的所有收据
	@Query("select r from Receipt r where cont_id = :cont_id ")
	List<Receipt> findAllByContId(@Param("cont_id") Integer cont_id);

	// 根据合同ID查询该合同对应的所有收据总条数
	@Query("select count(rece_id) from Receipt R where cont_id = :cont_id")
	Long countTotal(@Param("cont_id") Integer cont_id);

	// 根据收据Id查询该条数据的详情
	@Query("select r from Receipt r where rece_id=:rece_id")
	Receipt findByReceiptId(@Param("rece_id") Integer rece_id);

}
