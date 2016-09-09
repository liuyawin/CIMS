/**
 * 
 */
package com.mvc.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 合同日志表
 * 
 * @author zjn
 * @date 2016年9月8日
 */
@Entity
@Table(name = "contract_record")
public class ContractRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer conre_id; // 合同记录表ID
	private String conre_content; // 合同日志内容
	private Date conre_time; // 操作时间
	private Contract contract;// fk外键，合同ID
	private User user; // fk外键，操作者ID

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getConre_id() {
		return conre_id;
	}

	public void setConre_id(Integer conre_id) {
		this.conre_id = conre_id;
	}
	@Column(length = 64)
	public String getConre_content() {
		return conre_content;
	}

	public void setConre_content(String conre_content) {
		this.conre_content = conre_content;
	}

	public Date getConre_time() {
		return conre_time;
	}

	public void setConre_time(Date conre_time) {
		this.conre_time = conre_time;
	}

	@ManyToOne
	@JoinColumn(name = "cont_id")
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
