/**
 * 
 */
package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Invoice;

/**
 * 发票任务
 * 
 * @author zjn
 * @date 2016年9月9日
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

	// 根据发票ID查询发票详情
	@Query("select i from Invoice i where invo_id=:invo_id")
	Invoice findById(@Param("invo_id") Integer invoiceId);

}
