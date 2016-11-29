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
 * 合同
 * 
 * @author lwt
 * @date2016年9月8日
 */
@Entity
@Table(name = "contract")
public class Contract implements Serializable {

	private static final long serialVersionUID = 1L;
	// 合同项目相关
	private Integer cont_id;// 合同ID
	private String cont_name;// 合同名称
	private Integer cont_type;// 合同类型，0：传统光伏项目，1：分布式, 2：光热, 3：其他;
	private Integer cont_rank;// 等级,用于报警类别的区分,0: 重要，1：一般
	private Float cont_money;// 合同金额
	private String cont_pnum;// 项目编码
	private String cont_onum;// 本公司合同编号
	private String cont_cnum;// （甲方）业主方编号
	private String cont_project;// 项目名称
	private String province;// 项目所在省
	private String city;// 项目所在市
	private String pro_stage;// 项目阶段(0=规划,1=预可研,2=可研,3=项目建议书,4=初步设计,5=发包、招标设计,6=施工详图,7=竣工图,8=其他)
	private Integer cont_state;// 项目状态,0:在建,1:竣工,2:停建
	private Date cont_stime;// 合同签订日期
	private User manager;// 项目设总（项目经理）
	private User assistant_manager;// 项目副设总（项目副经理），可为空
	private Integer cont_hasproxy;// 是否有委托书，0表示没有，1表示有
	private Integer cont_initiation;// 是否立项，0:未立项，1:已立项(默认已立项)
	private Float install_capacity;// 装机容量（MV）
	private Integer cont_isback;// 新增： 是否返回合同（归档）..............................

	// 业主信息相关
	private String cont_client;// 业主公司名称
	private String cont_orgcodenum;// 组织机构代码证号
	private String company_type;// 企业性质(0=国有企业，1=事业单位，2=民营企业，3=国外企业，4=政府机关，5=其他)
	private String cont_caddress;// 业主通讯地址
	private String cont_czipcode;// 业主邮编编码
	private String cont_cfax;// 业主传真
	private Integer cont_avetaxpayer;// 增税人一般纳税人,(0=一般纳税人，1=小规模纳税人)
	private Integer invoice_type;// 发票类型(0=增值税专用发票，1=增值税普通发票)
	private String cont_taxidennum;// 纳税人识别号
	private String tel;// 固定电话
	private String cont_bank;// 开户行
	private String cont_account;// 账号
	private String com_signaddress;// 公司注册地址
	private String cont_remark;// 备注（其他需要说明的情况）

	// 业主联系人1相关信息
	private String cont_cheader;// 联系人姓名
	private String cont_ctel;// 联系方式
	private String landline_tel;// 固定电话
	private String post;// 职务
	private String cont_cdept;// 所在部门
	private String cont_cemail;// 电子邮箱

	// 业主联系人2相关信息
	private String cont_cheader2;// 联系人姓名
	private String cont_ctel2;// 联系方式
	private String landline_tel2;// 新增：固定电话
	private String post2;// 职务
	private String cont_cdept2;// 所在部门
	private String cont_cemail2;// 电子邮箱

	// 该字段暂时未用到
	private String cont_saddress;// 合同签订地点

	// 以下字段为统计报表所设计
	private Date cont_ctime;// 合同创建时间
	private User creator;// 合同创建者
	private Integer cont_ishistory;// 0：最新，1历史
	private Integer cont_proalanum;// 工程阶段报警次数
	private Integer cont_payalanum;// 收款节点报警次数
	private String cur_prst;// 当前工期阶段
	private String cur_reno;// 当前收款节点
	private Integer invo_total;// 发票总数
	private Float invo_totalmoney;// 发票总金额
	private Integer rece_total;// 收据总数
	private Float rece_totalmoney;// 收据总金额
	private Integer remo_count;// 到款次数
	private Float remo_totalmoney;// 到款总金额

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

	@Column(columnDefinition = "INT not null default 0")
	public Integer getCont_initiation() {
		return cont_initiation;
	}

	public void setCont_initiation(Integer cont_initiation) {
		this.cont_initiation = cont_initiation;
	}

	@Column(columnDefinition = "float(10,2) not null default '0.00'")
	public Float getInstall_capacity() {
		return install_capacity;
	}

	public void setInstall_capacity(Float install_capacity) {
		this.install_capacity = install_capacity;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getCont_isback() {
		return cont_isback;
	}

	public void setCont_isback(Integer cont_isback) {
		this.cont_isback = cont_isback;
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

	@Column(columnDefinition = "float(10,2) default '0.00'")
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

	@Column(columnDefinition = "INT not null default 0")
	public Integer getCont_proalanum() {
		return cont_proalanum;
	}

	public void setCont_proalanum(Integer cont_proalanum) {
		this.cont_proalanum = cont_proalanum;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getCont_payalanum() {
		return cont_payalanum;
	}

	public void setCont_payalanum(Integer cont_payalanum) {
		this.cont_payalanum = cont_payalanum;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getCont_state() {
		return cont_state;
	}

	public void setCont_state(Integer cont_state) {
		this.cont_state = cont_state;
	}

	@Column(columnDefinition = "INT not null default 1")
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

	@Column(columnDefinition = "INT not null default 0")
	public Integer getInvo_total() {
		return invo_total;
	}

	public void setInvo_total(Integer invo_total) {
		this.invo_total = invo_total;
	}

	@Column(columnDefinition = "float(10,2) not null default '0.00'")
	public Float getInvo_totalmoney() {
		return invo_totalmoney;
	}

	public void setInvo_totalmoney(Float invo_totalmoney) {
		this.invo_totalmoney = invo_totalmoney;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getRece_total() {
		return rece_total;
	}

	public void setRece_total(Integer rece_total) {
		this.rece_total = rece_total;
	}

	@Column(columnDefinition = "float(10,2) not null default '0.00'")
	public Float getRece_totalmoney() {
		return rece_totalmoney;
	}

	public void setRece_totalmoney(Float rece_totalMoney) {
		this.rece_totalmoney = rece_totalMoney;
	}

	@Column(columnDefinition = "varchar(100) not null default '未录入工期阶段'")
	public String getCur_prst() {
		return cur_prst;
	}

	public void setCur_prst(String cur_prst) {
		this.cur_prst = cur_prst;
	}

	@Column(columnDefinition = "varchar(100) not null default '未录入收款节点'")
	public String getCur_reno() {
		return cur_reno;
	}

	public void setCur_reno(String cur_reno) {
		this.cur_reno = cur_reno;
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

	@Column(columnDefinition = "INT not null default 0")
	public Integer getCont_ishistory() {
		return cont_ishistory;
	}

	public void setCont_ishistory(Integer cont_ishistory) {
		this.cont_ishistory = cont_ishistory;
	}

	@Column(columnDefinition = "INT not null default 0")
	public Integer getRemo_count() {
		return remo_count;
	}

	public void setRemo_count(Integer remo_count) {
		this.remo_count = remo_count;
	}

	@Column(columnDefinition = "float(10,2) not null default '0.00'")
	public Float getRemo_totalmoney() {
		return remo_totalmoney;
	}

	public void setRemo_totalmoney(Float remo_totalmoney) {
		this.remo_totalmoney = remo_totalmoney;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPro_stage() {
		return pro_stage;
	}

	public void setPro_stage(String pro_stage) {
		this.pro_stage = pro_stage;
	}

	@ManyToOne
	@JoinColumn(name = "assmana_id")
	public User getAssistant_manager() {
		return assistant_manager;
	}

	public void setAssistant_manager(User assistant_manager) {
		this.assistant_manager = assistant_manager;
	}

	@Column(columnDefinition = "INT not null default 0")
	public String getCompany_type() {
		return company_type;
	}

	public void setCompany_type(String company_type) {
		this.company_type = company_type;
	}

	public Integer getInvoice_type() {
		return invoice_type;
	}

	public void setInvoice_type(Integer invoice_type) {
		this.invoice_type = invoice_type;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCom_signaddress() {
		return com_signaddress;
	}

	public void setCom_signaddress(String com_signaddress) {
		this.com_signaddress = com_signaddress;
	}

	public String getLandline_tel() {
		return landline_tel;
	}

	public void setLandline_tel(String landline_tel) {
		this.landline_tel = landline_tel;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getCont_cheader2() {
		return cont_cheader2;
	}

	public void setCont_cheader2(String cont_cheader2) {
		this.cont_cheader2 = cont_cheader2;
	}

	public String getCont_ctel2() {
		return cont_ctel2;
	}

	public void setCont_ctel2(String cont_ctel2) {
		this.cont_ctel2 = cont_ctel2;
	}

	public String getLandline_tel2() {
		return landline_tel2;
	}

	public void setLandline_tel2(String landline_tel2) {
		this.landline_tel2 = landline_tel2;
	}

	public String getPost2() {
		return post2;
	}

	public void setPost2(String post2) {
		this.post2 = post2;
	}

	public String getCont_cdept2() {
		return cont_cdept2;
	}

	public void setCont_cdept2(String cont_cdept2) {
		this.cont_cdept2 = cont_cdept2;
	}

	public String getCont_cemail2() {
		return cont_cemail2;
	}

	public void setCont_cemail2(String cont_cemail2) {
		this.cont_cemail2 = cont_cemail2;
	}

}
