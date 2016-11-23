/**
 * 
 */
package com.mvc.entity;

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
 * 到款
 * 
 * @author zjn
 * @date 2016年10月27日
 */
@Entity
@Table(name = "receive_money")
public class ReceiveMoney {
	private Integer remo_id;// Id
	private Float remo_money;// 本次应收款金额
	private Float remo_amoney;// 本次实收款金额
	private Date remo_time;// 收款时间
	private Integer remo_state;// 状态：0表示未核对，1表示已核对
	private User creater;// 收款发起人
	private User operater;// 操作人
	private Contract contract;// 所属合同
	private String remo_remark;// 备注
	private Integer remo_isdelete;// 是否删除，0表示：未删除，1表示已删除

	// 以下为冗余字段，用于报表统计
	private String province;// 该合同对应的区域
	private Date cont_stime;// 合同创建时间

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getRemo_id() {
		return remo_id;
	}

	public void setRemo_id(Integer remo_id) {
		this.remo_id = remo_id;
	}

	@Column(columnDefinition = "float(10,2) default '0.00'")
	public Float getRemo_money() {
		return remo_money;
	}

	public void setRemo_money(Float remo_money) {
		this.remo_money = remo_money;
	}

	@Column(columnDefinition = "float(10,2) default '0.00'")
	public Float getRemo_amoney() {
		return remo_amoney;
	}

	public void setRemo_amoney(Float remo_amoney) {
		this.remo_amoney = remo_amoney;
	}

	public Date getRemo_time() {
		return remo_time;
	}

	public void setRemo_time(Date remo_time) {
		this.remo_time = remo_time;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getRemo_state() {
		return remo_state;
	}

	public void setRemo_state(Integer remo_state) {
		this.remo_state = remo_state;
	}

	@ManyToOne
	@JoinColumn(name = "creater_id")
	public User getCreater() {
		return creater;
	}

	public void setCreater(User creater) {
		this.creater = creater;
	}

	@ManyToOne
	@JoinColumn(name = "operater_id")
	public User getOperater() {
		return operater;
	}

	public void setOperater(User operater) {
		this.operater = operater;
	}

	@ManyToOne
	@JoinColumn(name = "cont_id")
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public String getRemo_remark() {
		return remo_remark;
	}

	public void setRemo_remark(String remo_remark) {
		this.remo_remark = remo_remark;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getRemo_isdelete() {
		return remo_isdelete;
	}

	public void setRemo_isdelete(Integer remo_isdelete) {
		this.remo_isdelete = remo_isdelete;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Date getCont_stime() {
		return cont_stime;
	}

	public void setCont_stime(Date cont_stime) {
		this.cont_stime = cont_stime;
	}

}
