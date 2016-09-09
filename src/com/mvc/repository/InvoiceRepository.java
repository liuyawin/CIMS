/**
 * 
 */
package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.entity.Invoice;

/**
 * 发票任务
 * 
 * @author zjn
 * @date 2016年9月9日
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
