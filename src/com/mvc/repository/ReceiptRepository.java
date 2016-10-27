/**
 * 
 */
package com.mvc.repository;

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

	// 根据收据Id查询该条数据的详情
	@Query("select r from Receipt r where rece_id=:rece_id")
	Receipt findByReceiptId(@Param("rece_id") Integer rece_id);

}
