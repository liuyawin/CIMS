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
 * 合同
 * 
 * @author lwt
 * @date2016年9月8日
 */
@Entity
@Table(name = "contract")
public class Contract {
	private Integer cont_id;// 合同ID
	private String cont_onum;// 本公司编号
	private String cont_cnum;// 业主方编号
	private String cont_name;// 合同名称
	private Integer cont_initiation;// 是否立项，0:未立项，1:已立项(默认已立项)
	private String cont_project;// 项目名称
	private String cont_pnum;// 项目编码
	private Integer cont_type;// 合同的类型，0:规划，1:可行性，2:施工图，3:评估，4:其他
	private Date cont_stime;// 合同签订时间
	private String cont_saddress;// 合同签订地点
	private Float cont_money;// 合同金额
	private Integer cont_hasproxy;// 是否有委托书，0表示没有，1表示有
	private String cont_client;// 业主公司名称
	private String cont_caddress;// 业主地址
	private String cont_cheader;// 业主联系人
	private String cont_cdept;// 业主联系部门
	private String cont_ctel;// 业主联系方式
	private String cont_cemail;// 业主邮箱
	private String cont_cfax;// 业主传真
	private String cont_czipcode;// 业主邮编
	private Date cont_ctime;// 合同创建时间
	private String cont_bank;// 开户行
	private String cont_account;// 银行账户
	private String cont_taxidennum;// 纳税人识别号
	private String cont_orgcodenum;// 组织机构代码证号
	private Integer cont_avetaxpayer;// 增税人一般纳税人,0:否，1:是
	private Integer cont_proalanum;// 工程阶段报警次数
	private Integer cont_payalanum;// 收款节点报警次数
	private Integer cont_state;// 状态,0:在建,1:竣工,2:停建
	private Integer cont_rank;// 等级,用于报警类别的区分,0: 重要，1：一般
	private String cont_remark;// 备注
	private User creator;// 合同创建者
	private User manager;// 项目经理
	private Integer cont_ishistory;// 0：最新，1历史

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getCont_id() {
		return cont_id;
	}

	public void setCont_id(Integer cont_id) {
		this.cont_id = cont_id;
	}

	@Column(length = 32)
	public String getCont_onum() {
		return cont_onum;
	}

	public void setCont_onum(String cont_onum) {
		this.cont_onum = cont_onum;
	}

	@Column(length = 32)
	public String getCont_cnum() {
		return cont_cnum;
	}

	public void setCont_cnum(String cont_cnum) {
		this.cont_cnum = cont_cnum;
	}

	@Column(length = 64)
	public String getCont_name() {
		return cont_name;
	}

	public void setCont_name(String cont_name) {
		this.cont_name = cont_name;
	}

	public Integer getCont_initiation() {
		return cont_initiation;
	}

	public void setCont_initiation(Integer cont_initiation) {
		this.cont_initiation = cont_initiation;
	}

	@Column(length = 64)
	public String getCont_project() {
		return cont_project;
	}

	public void setCont_project(String cont_project) {
		this.cont_project = cont_project;
	}

	@Column(length = 32)
	public String getCont_pnum() {
		return cont_pnum;
	}

	public void setCont_pnum(String cont_pnum) {
		this.cont_pnum = cont_pnum;
	}

	public Integer getCont_type() {
		return cont_type;
	}

	public void setCont_type(Integer cont_type) {
		this.cont_type = cont_type;
	}

	public Date getCont_stime() {
		return cont_stime;
	}

	public void setCont_stime(Date cont_stime) {
		this.cont_stime = cont_stime;
	}

	@Column(length = 64)
	public String getCont_saddress() {
		return cont_saddress;
	}

	public void setCont_saddress(String cont_saddress) {
		this.cont_saddress = cont_saddress;
	}

	public Float getCont_money() {
		return cont_money;
	}

	public void setCont_money(Float cont_money) {
		this.cont_money = cont_money;
	}

	public Integer getCont_hasproxy() {
		return cont_hasproxy;
	}

	public void setCont_hasproxy(Integer cont_hasproxy) {
		this.cont_hasproxy = cont_hasproxy;
	}

	@Column(length = 64)
	public String getCont_client() {
		return cont_client;
	}

	public void setCont_client(String cont_client) {
		this.cont_client = cont_client;
	}

	@Column(length = 64)
	public String getCont_caddress() {
		return cont_caddress;
	}

	public void setCont_caddress(String cont_caddress) {
		this.cont_caddress = cont_caddress;
	}

	@Column(length = 16)
	public String getCont_cheader() {
		return cont_cheader;
	}

	public void setCont_cheader(String cont_cheader) {
		this.cont_cheader = cont_cheader;
	}

	@Column(length = 32)
	public String getCont_cdept() {
		return cont_cdept;
	}

	public void setCont_cdept(String cont_cdept) {
		this.cont_cdept = cont_cdept;
	}

	@Column(length = 32)
	public String getCont_ctel() {
		return cont_ctel;
	}

	public void setCont_ctel(String cont_ctel) {
		this.cont_ctel = cont_ctel;
	}

	@Column(length = 32)
	public String getCont_cemail() {
		return cont_cemail;
	}

	public void setCont_cemail(String cont_cemail) {
		this.cont_cemail = cont_cemail;
	}

	@Column(length = 16)
	public String getCont_cfax() {
		return cont_cfax;
	}

	public void setCont_cfax(String cont_cfax) {
		this.cont_cfax = cont_cfax;
	}

	@Column(length = 16)
	public String getCont_czipcode() {
		return cont_czipcode;
	}

	public void setCont_czipcode(String cont_czipcode) {
		this.cont_czipcode = cont_czipcode;
	}

	public Date getCont_ctime() {
		return cont_ctime;
	}

	public void setCont_ctime(Date cont_ctime) {
		this.cont_ctime = cont_ctime;
	}

	@Column(length = 32)
	public String getCont_bank() {
		return cont_bank;
	}

	public void setCont_bank(String cont_bank) {
		this.cont_bank = cont_bank;
	}

	@Column(length = 32)
	public String getCont_account() {
		return cont_account;
	}

	public void setCont_account(String cont_account) {
		this.cont_account = cont_account;
	}

	@Column(length = 64)
	public String getCont_taxidennum() {
		return cont_taxidennum;
	}

	public void setCont_taxidennum(String cont_taxidennum) {
		this.cont_taxidennum = cont_taxidennum;
	}

	@Column(length = 64)
	public String getCont_orgcodenum() {
		return cont_orgcodenum;
	}

	public void setCont_orgcodenum(String cont_orgcodenum) {
		this.cont_orgcodenum = cont_orgcodenum;
	}

	public Integer getCont_avetaxpayer() {
		return cont_avetaxpayer;
	}

	public void setCont_avetaxpayer(Integer cont_avetaxpayer) {
		this.cont_avetaxpayer = cont_avetaxpayer;
	}

	public Integer getCont_proalanum() {
		return cont_proalanum;
	}

	public void setCont_proalanum(Integer cont_proalanum) {
		this.cont_proalanum = cont_proalanum;
	}

	public Integer getCont_payalanum() {
		return cont_payalanum;
	}

	public void setCont_payalanum(Integer cont_payalanum) {
		this.cont_payalanum = cont_payalanum;
	}

	public Integer getCont_state() {
		return cont_state;
	}

	public void setCont_state(Integer cont_state) {
		this.cont_state = cont_state;
	}

	public Integer getCont_rank() {
		return cont_rank;
	}

	public void setCont_rank(Integer cont_rank) {
		this.cont_rank = cont_rank;
	}

	public String getCont_remark() {
		return cont_remark;
	}

	public void setCont_remark(String cont_remark) {
		this.cont_remark = cont_remark;
	}

	@ManyToOne
	@JoinColumn(name = "creator_id")
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@ManyToOne
	@JoinColumn(name = "manager_id")
	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	@Column(columnDefinition = "INT default 0")
	public Integer getCont_ishistory() {
		return cont_ishistory;
	}

	public void setCont_ishistory(Integer cont_ishistory) {
		this.cont_ishistory = cont_ishistory;
	}

}
