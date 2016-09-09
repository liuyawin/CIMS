/**
 * 
 */
package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.entity.Receipt;

/**
 * 收据
 * 
 * @author zjn
 * @date 2016年9月9日
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

}
