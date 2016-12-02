package com.mvc.dao.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.base.enums.ConExecStatus;
import com.mvc.dao.ContractDao;
import com.mvc.entity.Contract;
import com.utils.Pager;

/**
 * 合同
 * 
 * @author wangrui
 * @date 2016年9月13日
 */
@Repository("contractDaoImpl")
public class ContractDaoImpl implements ContractDao {
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	// 返回欠款合同信息
	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findAllDebtCont(String contName, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select * from contract c where c.cont_state=0 and c.cont_id in (select distinct(rn.cont_id) from receive_node rn ");
		sql.append("where rn.reno_time<=now() and rn.reno_state in (0,2)) and c.cont_ishistory=0");
		if (null != contName) {
			sql.append(" and c.cont_name like '%" + contName + "%'");
		}
		sql.append(" order by cont_id desc limit :offset,:end");
		Query query = em.createNativeQuery(sql.toString(), Contract.class);
		query.setParameter("offset", offset).setParameter("end", end);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	// 返回逾期合同信息
	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findAllOverdueCont(String contName, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select * from contract c where c.cont_state=0 and c.cont_id in (select distinct(ps.cont_id) from project_stage ps where ps.prst_etime<=now()");
		sql.append(" and ps.prst_state=0) and c.cont_ishistory=0");
		if (contName != null) {
			sql.append(" and c.cont_name like '%" + contName + "%'");
		}
		sql.append(" order by cont_id desc limit :offset,:end");
		Query query = em.createNativeQuery(sql.toString(), Contract.class);
		query.setParameter("offset", offset).setParameter("end", end);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据合同名获取合同信息
	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findConByName(String contName, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from contract c where c.cont_state=0 and c.cont_ishistory=0");// 在建
		if (null != contName) {
			sql.append(" and c.cont_name like '%" + contName + "%'");
		}
		sql.append(" order by cont_id desc limit :offset,:end");
		Query query = em.createNativeQuery(sql.toString(), Contract.class);
		query.setParameter("offset", offset).setParameter("end", end);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	// 根据创建者ID和合同名查询合同总条数
	@Override
	public Long countTotal(String contName, Integer methodType) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(cont_id) from contract c where c.cont_ishistory=0 ");
		if (methodType == 1) {// 根据name查询
			sql.append(" and c.cont_state=0");
		} else if (methodType == 2) {// 查询欠款
			sql.append(
					" and c.cont_state=0 and c.cont_id in (select distinct(rn.cont_id) from receive_node rn where rn.reno_time<=now() and rn.reno_state in (0,2))");
		} else if (methodType == 3) {// 查询逾期
			sql.append(
					" and c.cont_state=0 and c.cont_id in (select distinct(t.cont_id) from task t where t.task_etime<=now() and t.task_state in (0,1) and t.task_isdelete=0)");
		} else if (methodType == 4) {// 终结合同
			sql.append(" and c.cont_state=1");// 竣工
		} else if (methodType == 5) {// 停建合同
			sql.append(" and c.cont_state=2");// 停建
		}
		if (contName != null) {
			sql.append(" and c.cont_name like '%" + contName + "%'");
		}
		Query query = em.createNativeQuery(sql.toString());
		BigInteger totalRow = (BigInteger) query.getSingleResult();// count返回值为BigInteger类型
		em.close();
		return totalRow.longValue();
	}

	// 删除合同
	@Override
	public Boolean delete(Integer cont_id) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		try {
			em.getTransaction().begin();
			sql.append("update contract c set c.cont_ishistory=1 where c.cont_id=:cont_id");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("cont_id", cont_id);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

	// 查询所有终结合同列表
	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findAllEndCont(String contName, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from contract c where c.cont_state=1 and c.cont_ishistory=0");
		if (contName != null) {
			sql.append(" and c.cont_name like '%" + contName + "%'");
		}
		sql.append(" order by cont_id desc limit :offset,:end");
		Query query = em.createNativeQuery(sql.toString(), Contract.class);
		query.setParameter("offset", offset).setParameter("end", end);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	// 修改合同基本信息
	@Override
	public Boolean updateConById(Integer cont_id, Contract contract) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(contract);
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

	// 张姣娜：根据合同id修改状态
	public Boolean updateState(Integer contId, Integer contState) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		try {
			em.getTransaction().begin();
			sql.append("update contract c set c.cont_state=:cont_state where c.cont_id=:cont_id");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("cont_state", contState);
			query.setParameter("cont_id", contId);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

	// 张姣娜：查询所有停建合同列表
	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findAllStopCont(String contName, Integer offset, Integer end) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from contract c where c.cont_state=2 and c.cont_ishistory=0");
		if (contName != null) {
			sql.append(" and c.cont_name like '%" + contName + "%'");
		}
		sql.append(" order by cont_id desc limit :offset,:end");
		Query query = em.createNativeQuery(sql.toString(), Contract.class);
		query.setParameter("offset", offset).setParameter("end", end);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	/***** 报表相关 *****/
	// 光电院项目分项统计表
	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findContByPara(Map<String, Object> map, Pager pager) {
		Integer cont_type = (Integer) map.get("cont_type");
		String pro_stage = (String) map.get("pro_stage");
		Integer managerId = (Integer) map.get("managerId");
		Integer cont_status = (Integer) map.get("cont_status");
		String province = (String) map.get("province");
		String startTime = (String) map.get("startTime");
		String endTime = (String) map.get("endTime");

		Integer offset = null;
		Integer end = null;
		if (pager != null) {
			offset = pager.getOffset();
			end = pager.getPageSize();
		}

		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from contract c where c.cont_ishistory=0");

		if (cont_type == -1) {
			sql.append(" and c.cont_type in(0,1,2,3)");
		} else {
			sql.append(" and c.cont_type=" + cont_type);
		}
		if (pro_stage != null) {
			sql.append(" and c.pro_stage like '%" + pro_stage + "%'");
		}
		if (managerId != null) {
			sql.append(" and c.manager_id=" + managerId);
		}
		if (cont_status != null) {
			switch (cont_status) {
			case 0:
				sql.append(" and c.cont_initiation=0");
				break;
			case 1:
				sql.append(" and c.cont_initiation=1 and c.cont_stime is null");
				break;
			case 2:
				sql.append(" and c.cont_initiation=1 and c.cont_stime is not null");
				break;
			default:
				break;
			}
		}
		if (province != null) {
			sql.append(" and c.province='" + province + "'");
		}
		if (startTime != null && endTime != null) {
			sql.append(" and c.cont_stime between '" + startTime + "'" + " and'" + endTime + "'");
		}
		sql.append(" order by cont_id desc");
		if (offset != null && end != null) {
			sql.append(" limit " + offset + "," + end);
		}
		Query query = em.createNativeQuery(sql.toString(), Contract.class);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	// 未返回合同统计表
	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findContByParaNoBack(Map<String, Object> map, Pager pager) {
		Integer handler = (Integer) map.get("handler");
		Integer header = (Integer) map.get("header");
		String province = (String) map.get("province");
		String startTime = (String) map.get("startTime");
		String endTime = (String) map.get("endTime");

		Integer offset = null;
		Integer end = null;
		if (pager != null) {
			offset = pager.getOffset();
			end = pager.getPageSize();
		}

		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from contract c where c.cont_ishistory=0 and c.cont_isback=" + ConExecStatus.post.value);

		if (handler != null) {
			sql.append(" and c.manager_id=" + handler);
		}
		if (header != null) {
			sql.append(" and c.creator_id=" + header);
		}
		if (province != null) {
			sql.append(" and c.province='" + province + "'");
		}
		if (startTime != null && endTime != null) {
			sql.append(" and c.cont_stime between '" + startTime + "'" + " and'" + endTime + "'");
		}
		sql.append(" order by cont_id desc");
		if (offset != null && end != null) {
			sql.append(" limit " + offset + "," + end);
		}
		Query query = em.createNativeQuery(sql.toString(), Contract.class);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	// 查询分项统计表总条数
	@Override
	public Long countTotal(Map<String, Object> map) {
		Integer cont_type = (Integer) map.get("cont_type");
		String pro_stage = (String) map.get("pro_stage");
		Integer managerId = (Integer) map.get("managerId");
		Integer cont_status = (Integer) map.get("cont_status");
		String province = (String) map.get("province");
		String startTime = (String) map.get("startTime");
		String endTime = (String) map.get("endTime");

		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from contract c where c.cont_ishistory=0 ");

		if (cont_type == -1) {
			sql.append(" and c.cont_type in(0,1,2,3)");
		} else {
			sql.append(" and c.cont_type=" + cont_type);
		}
		if (pro_stage != null) {
			sql.append(" and c.pro_stage like '%" + pro_stage + "%'");
		}
		if (managerId != null) {
			sql.append(" and c.manager_id=" + managerId);
		}
		if (cont_status != null) {
			switch (cont_status) {
			case 0:
				sql.append(" and c.cont_initiation=0");
				break;
			case 1:
				sql.append(" and c.cont_initiation=1 and c.cont_stime is null");
				break;
			case 2:
				sql.append(" and c.cont_initiation=1 and c.cont_stime is not null");
				break;
			default:
				break;
			}
		}
		if (province != null) {
			sql.append(" and c.province='" + province + "'");
		}
		if (startTime != null && endTime != null) {
			sql.append(" and c.cont_stime between '" + startTime + "'" + " and'" + endTime + "'");
		}

		Query query = em.createNativeQuery(sql.toString());
		BigInteger totalRow = (BigInteger) query.getSingleResult();
		em.close();
		return totalRow.longValue();
	}

	// 查询未返回合同统计表总条数
	@Override
	public Long countTotalNoBack(Map<String, Object> map) {
		Integer handler = (Integer) map.get("handler");
		Integer header = (Integer) map.get("header");
		String province = (String) map.get("province");
		String startTime = (String) map.get("startTime");
		String endTime = (String) map.get("endTime");

		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from contract c where c.cont_ishistory=0 and c.cont_isback="
				+ ConExecStatus.post.value);

		if (handler != null) {
			sql.append(" and c.manager_id=" + handler);
		}
		if (header != null) {
			sql.append(" and c.creator_id=" + header);
		}
		if (province != null) {
			sql.append(" and c.province='" + province + "'");
		}
		if (startTime != null && endTime != null) {
			sql.append(" and c.cont_stime between '" + startTime + "'" + " and'" + endTime + "'");
		}

		Query query = em.createNativeQuery(sql.toString());
		BigInteger totalRow = (BigInteger) query.getSingleResult();
		em.close();
		return totalRow.longValue();
	}

	// 根据日期获取合同额到款对比表
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findByOneDate(String date) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "select coalesce(sum(cont_money),0.00),coalesce(sum(remo_totalmoney),0.00),count(cont_id) from contract c where c.cont_ishistory=0 ";
		if (date != null) {
			selectSql += " and (cont_stime like '%" + date + "%') ";
		}
		Query query = em.createNativeQuery(selectSql.toString());
		List<Object> result = query.getResultList();
		em.close();
		return result;
	}

	// 根据日期获取新签合同额分析表
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findComoByDate(String dateOne, String dateTwo) {
		EntityManager em = emf.createEntityManager();
		StringBuilder selectSql = new StringBuilder();
		selectSql.append(
				"select cc.province,coalesce(aa.como_one,0.00) como_one,coalesce(bb.como_two,0.00) como_two from ");
		selectSql.append(" (select province from contract c where c.cont_ishistory=0 and (cont_stime like '%" + dateOne
				+ "%') union all select province from contract c where c.cont_ishistory=0 and (cont_stime like '%"
				+ dateTwo + "%')) as cc ");
		selectSql.append(" left join  ");
		selectSql
				.append(" (select province,coalesce(sum(cont_money),0.00) como_one from contract c where c.cont_ishistory=0 and (cont_stime like '%"
						+ dateOne + "%') group by province) as aa  ");
		selectSql.append(" on aa.province=cc.province left join ");
		selectSql
				.append(" (select province,coalesce(sum(cont_money),0.00) como_two from contract c where c.cont_ishistory=0 and (cont_stime like '%"
						+ dateTwo + "%') group by province) as bb ");
		selectSql.append(" on bb.province=cc.province ");
		selectSql.append(" group by province ");
		Query query = em.createNativeQuery(selectSql.toString());
		List<Object> result = query.getResultList();
		em.close();
		return result;
	}

	// 根据日期获取到款分析表
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findRemoByDate(String dateOne, String dateTwo) {
		EntityManager em = emf.createEntityManager();
		StringBuilder selectSql = new StringBuilder();
		selectSql.append(
				"select cc.province,coalesce(aa.remo_one,0.00) remo_one,coalesce(bb.remo_two,0.00) remo_two from ");
		selectSql.append(" (select province from contract c where c.cont_ishistory=0 and (cont_stime like '%" + dateOne
				+ "%') union all select province from contract c where c.cont_ishistory=0 and (cont_stime like '%"
				+ dateTwo + "%')) as cc ");
		selectSql.append(" left join  ");
		selectSql
				.append(" (select province,coalesce(sum(remo_totalmoney),0.00) remo_one from contract c where c.cont_ishistory=0 and (cont_stime like '%"
						+ dateOne + "%') group by province) as aa ");
		selectSql.append(" on aa.province=cc.province left join ");
		selectSql
				.append(" (select province,coalesce(sum(remo_totalmoney),0.00) remo_two from contract c where c.cont_ishistory=0 and (cont_stime like '%"
						+ dateTwo + "%') group by province) as bb ");
		selectSql.append(" on bb.province=cc.province ");
		selectSql.append(" group by province ");
		Query query = em.createNativeQuery(selectSql.toString());
		List<Object> result = query.getResultList();
		em.close();
		return result;
	}

	// 王慧敏操作，导出光伏自营项目催款计划表
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findContByParw(Map<String, Object> map, Pager pager) {
		String province = (String) map.get("province");// 行政区域
		String cont_project = (String) map.get("cont_project");// 工程名称 && 项目名称
		String cont_client = (String) map.get("cont_client");// 业主名称 && 业主公司名称
		String cont_money = (String) map.get("cont_money");// 合同金额
		String remo_totalmoney = (String) map.get("remo_totalmoney");// 2015年累计已到款
		String balance_money = (String) map.get("balance_money");// 余额
		String invo_totalmoney = (String) map.get("invo_totalmoney");// 已开发票金额
		String noinvo_totalmoney = (String) map.get("noinvo_totalmoney");// 未开发票金额
		String startTime = (String) map.get("startTime");
		String endTime = (String) map.get("endTime");

		String planTime = (String) map.get("planTime");// 计划催款时间

		Integer offset = null;
		Integer end = null;
		if (pager != null) {
			offset = pager.getOffset();
			end = pager.getPageSize();
		}
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		if (planTime != null) {
			String sql0 = " (select cont_id,coalesce(sum(remo_amoney),0.00) actual_money from receive_money r where r.remo_state=1 and r.remo_time like '%"
					+ planTime + "%' GROUP by cont_id) as bb";
			String sql00 = "(select cont_id,coalesce(sum(reno_money),0.00) plan_payment from receive_Node r where r.reno_isdelete=0 "
					+ "and r.reno_time like '%" + planTime + "%' GROUP by cont_id) as aa";
			sql.append(
					"select c.province,c.cont_project,c.cont_client,c.cont_money,c.remo_totalmoney,c.invo_totalmoney,bb.actual_money,aa.plan_payment "
							+ "from contract c left join " + sql0 + " on c.cont_id=bb.cont_id left join " + sql00
							+ " on c.cont_id=aa.cont_id where c.cont_ishistory=0 ");
			System.out.println("sql0:" + sql0);
			System.out.println("sql00:" + sql00);

		} else {
			sql.append(
					"select c.province,c.cont_project,c.cont_client,c.cont_money,c.remo_totalmoney,c.invo_totalmoney from contract c where c.cont_ishistory=0");
		}

		if (province != null) {
			sql.append(" and c.province='" + province + "'");
		}
		if (cont_project != null) {
			sql.append(" and c.cont_project='" + cont_project + "'");
		}
		if (cont_client != null) {
			sql.append(" and c.cont_client='" + cont_client + "'");
		}
		if (cont_money != null) {
			sql.append(" and c.cont_money='" + cont_money + "'");
		}
		if (remo_totalmoney != null) {
			sql.append(" and c.remo_totalmoney='" + remo_totalmoney + "'");
		}
		if (balance_money != null) {
			sql.append(" and c.balance_money='" + balance_money + "'");
		}
		if (invo_totalmoney != null) {
			sql.append(" and c.invo_totalmoney='" + invo_totalmoney + "'");
		}
		if (noinvo_totalmoney != null) {
			sql.append(" and c.noinvo_totalmoney='" + noinvo_totalmoney + "'");
		}
		if (startTime != null && endTime != null) {
			sql.append(" and c.cont_stime between '" + startTime + "'" + " and '" + endTime + "'");
		}
		sql.append(" order by c.province desc");

		if (offset != null && end != null) {
			sql.append(" limit " + offset + "," + end);
		}

		Query query = em.createNativeQuery(sql.toString());
		List<Object> list = query.getResultList();
		em.close();
		return list;
	}

	// 查询催款列表总条数
	@Override
	public Long countTotal_payment(Map<String, Object> map) {
		String province = (String) map.get("province");
		String startTime = (String) map.get("startTime");
		String endTime = (String) map.get("endTime");

		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from contract c where c.cont_ishistory=0 ");

		if (province != null) {
			sql.append(" and c.province='" + province + "'");
		}
		if (startTime != null && endTime != null) {
			sql.append(" and c.cont_stime between '" + startTime + "'" + " and'" + endTime + "'");
		}

		Query query = em.createNativeQuery(sql.toString());
		BigInteger totalRow = (BigInteger) query.getSingleResult();
		em.close();
		return totalRow.longValue();
	}

	// 张姣娜：完成文书任务后更新合同状态
	@Override
	public Boolean updateContIsback(Integer contId, Integer state) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		try {
			em.getTransaction().begin();
			sql.append("update contract c set c.cont_isback=:cont_isback where c.cont_id=:cont_id");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("cont_isback", state);
			query.setParameter("cont_id", contId);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}

	// 查询光伏项目明细表
	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findSummaryByDate(String date, Pager pager) {
		Integer offset = null;
		Integer end = null;
		if (pager != null) {
			offset = pager.getOffset();
			end = pager.getPageSize();
		}
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from contract c where c.cont_ishistory=0 ");

		if (!date.equals("")) {
			sql.append(" and c.cont_stime like '%" + date + "%' ");
		}
		sql.append(" order by c.province desc");

		if (offset != null && end != null) {
			sql.append(" limit " + offset + "," + end);
		}

		Query query = em.createNativeQuery(sql.toString(), Contract.class);
		List<Contract> list = query.getResultList();
		em.close();
		return list;
	}

	// 查询光伏项目明细表页码
	@Override
	public Long totalSummary(String date) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from contract c where c.cont_ishistory=0 ");

		if (!date.equals("")) {
			sql.append(" and c.cont_stime like '%" + date + "%' ");
		}
		Query query = em.createNativeQuery(sql.toString());
		BigInteger totalRow = (BigInteger) query.getSingleResult();
		em.close();
		return totalRow.longValue();
	}

	// 根据日期获取合同总金额
	@Override
	public Float getTotalMoney(String date) {
		EntityManager em = emf.createEntityManager();
		StringBuilder sql = new StringBuilder();
		sql.append("select coalesce(sum(cont_money),0) from contract c where c.cont_ishistory=0 ");

		if (!date.equals("")) {
			sql.append(" and c.cont_stime like '%" + date + "%' ");
		}
		Query query = em.createNativeQuery(sql.toString());
		Double totalRow = (Double) query.getSingleResult();
		em.close();
		return totalRow.floatValue();
	}
}
