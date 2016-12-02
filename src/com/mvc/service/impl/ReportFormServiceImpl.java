package com.mvc.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.base.enums.ContStatus;
import com.base.enums.ContractType;
import com.mvc.dao.ContractDao;
import com.mvc.dao.ReceiveMoneyDao;
import com.mvc.entity.ComoCompareRemo;
import com.mvc.entity.Contract;
import com.mvc.entity.NewComoAnalyse;
import com.mvc.entity.NewRemoAnalyse;
import com.mvc.entity.NoBackContForm;
import com.mvc.entity.PaymentPlanListForm;
import com.mvc.entity.ProjectStatisticForm;
import com.mvc.entity.SummarySheet;
import com.mvc.service.ReportFormService;
import com.utils.DoubleFloatUtil;
import com.utils.ExcelHelper;
import com.utils.FileHelper;
import com.utils.Pager;
import com.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 报表业务层实现
 * 
 * @author wangrui
 * @date 2016-11-15
 */
@Service("reportFormServiceImpl")
public class ReportFormServiceImpl implements ReportFormService {

	@Autowired
	ContractDao contractDao;
	@Autowired
	ReceiveMoneyDao receiveMoneyDao;

	java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

	// 导出光电院项目分项统计表
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseEntity<byte[]> exportProjectStatistic(Map<String, Object> map, String path) {
		ResponseEntity<byte[]> byteArr = null;
		Integer cont_type = (Integer) map.get("cont_type");

		try {
			ExcelHelper<ProjectStatisticForm> ex = new ExcelHelper<ProjectStatisticForm>();
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int year = c.get(Calendar.YEAR);

			String fileName = year + "年光电院项目分项统计表.xlsx";// 2007版(2003版受限)
			path = FileHelper.transPath(fileName, path);// 解析后的上传路径
			OutputStream out = new FileOutputStream(path);

			if (cont_type == -1) {// 全部类型，导出3个sheet的Excel
				// 分布式项目
				map.put("cont_type", ContractType.分布式.value);
				List<Contract> listSource_dis = contractDao.findContByPara(map, null);
				Iterator<Contract> it_dis = listSource_dis.iterator();
				List<ProjectStatisticForm> listGoal_dis = contToProStatis(it_dis);

				// 传统光伏项目
				map.put("cont_type", ContractType.传统光伏项目.value);
				List<Contract> listSource_tra = contractDao.findContByPara(map, null);
				Iterator<Contract> it_tra = listSource_tra.iterator();
				List<ProjectStatisticForm> listGoal_tra = contToProStatis(it_tra);

				// 光热项目
				map.put("cont_type", ContractType.光热.value);
				List<Contract> listSource_pho = contractDao.findContByPara(map, null);
				Iterator<Contract> it_pho = listSource_pho.iterator();
				List<ProjectStatisticForm> listGoal_pho = contToProStatis(it_pho);

				// 其他项目
				map.put("cont_type", ContractType.其他.value);
				List<Contract> listSource_other = contractDao.findContByPara(map, null);
				Iterator<Contract> it_other = listSource_other.iterator();
				List<ProjectStatisticForm> listGoal_other = contToProStatis(it_other);

				String[] header_dis = { "序号", "合同类型hidden", "项目名称", "项目设总", "所在地", "设计阶段", "装机容量（MW）", "合同额（万元）",
						"合同状态", "签订日期hidden", "备注" };// 顺序必须和对应实体一致

				Map<Integer, String> titleMap = new HashMap<Integer, String>();// 每个sheet中内容
				titleMap.put(0, year + "年光电院分布式光伏项目统计表");
				titleMap.put(1, year + "年光电院光伏项目统计表（不含分布式）");
				titleMap.put(2, year + "年光电院光热项目统计表");
				titleMap.put(3, year + "年光电院其它类型项目统计表");

				Map<Integer, String[]> headerMap = new HashMap<Integer, String[]>();// 每个sheet的标题，暂时用统一标题
				headerMap.put(0, header_dis);
				headerMap.put(1, header_dis);
				headerMap.put(2, header_dis);
				headerMap.put(3, header_dis);

				Map<Integer, List> mapList = new HashMap<Integer, List>();// 每个sheet中内容
				mapList.put(0, listGoal_dis);
				mapList.put(1, listGoal_tra);
				mapList.put(2, listGoal_pho);
				mapList.put(3, listGoal_other);

				ex.export2007MutiExcel(titleMap, headerMap, mapList, out, "yyyy-MM-dd", -1);// -1:表示没有合并单元格的列
			} else {// 根据合同类型，只导出对应的单sheet的Excel
				List<Contract> listSource = contractDao.findContByPara(map, null);
				Iterator<Contract> it = listSource.iterator();
				List<ProjectStatisticForm> listGoal = contToProStatis(it);

				String title = String.valueOf(year);
				switch (cont_type) {
				case 0:
					title += "年光电院光伏项目统计表（不含分布式）";
					break;
				case 1:
					title += "年光电院分布式光伏项目统计表";
					break;
				case 2:
					title += "年光电院光热项目统计表";
					break;
				case 3:
					title += "年光电院其它类型项目统计表";
					break;
				default:
					break;
				}

				String[] header = { "序号", "合同类型hidden", "项目名称", "项目设总", "所在地", "设计阶段", "装机容量（MW）", "合同额（万元）", "合同状态",
						"签订日期hidden", "备注" };// 顺序必须和对应实体一致
				ex.export2007Excel(title, header, (Collection) listGoal, out, "yyyy-MM-dd", -1);
			}

			out.close();
			byteArr = FileHelper.downloadFile(fileName, path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return byteArr;
	}

	// 导出未返回合同统计表
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseEntity<byte[]> exportNoBackCont(Map<String, Object> map, String path) {
		ResponseEntity<byte[]> byteArr = null;

		try {
			ExcelHelper<NoBackContForm> ex = new ExcelHelper<NoBackContForm>();
			String fileName = "未返回合同情况一览表.xlsx";// 2007版
			path = FileHelper.transPath(fileName, path);// 解析后的上传路径
			OutputStream out = new FileOutputStream(path);

			List<Contract> listSource = contractDao.findContByParaNoBack(map, null);
			Iterator<Contract> it = listSource.iterator();
			List<NoBackContForm> listGoal = contToNoBackCont(it);

			String title = "未返回合同情况一览表";
			String[] header = { "序号", "项目名称", "业主单位", "合同额（万元）", "经手人", "负责人" };// 顺序必须和对应实体一致
			ex.export2007Excel(title, header, (Collection) listGoal, out, "yyyy-MM-dd", -1);

			out.close();
			byteArr = FileHelper.downloadFile(fileName, path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return byteArr;
	}

	/**
	 * 将contract封装成分项统计报表
	 * 
	 * @param it
	 * @return
	 */
	private List<ProjectStatisticForm> contToProStatis(Iterator<Contract> it) {
		List<ProjectStatisticForm> listGoal = new ArrayList<ProjectStatisticForm>();
		int i = 0;
		while (it.hasNext()) {// 赋值顺序和表头无关
			i++;
			Contract contract = it.next();
			ProjectStatisticForm projectStatisticForm = new ProjectStatisticForm();
			projectStatisticForm.setPrsf_id(i);// 序号
			Integer cont_type = contract.getCont_type();
			projectStatisticForm.setCont_type(ContractType.intToStr(cont_type));// 合同类型
			projectStatisticForm.setCont_project(contract.getCont_project());// 项目名称
			if (contract.getManager() != null) {
				projectStatisticForm.setManager_name(contract.getManager().getUser_name());// 项目设总
			}
			projectStatisticForm.setProvince(contract.getProvince());// 所在地（省）

			String proStageStr = contract.getPro_stage();// 设计阶段（数字类型字符串）
			proStageStr = intStrToStr(proStageStr);
			projectStatisticForm.setPro_stage(proStageStr);// 设计阶段（项目阶段）
			projectStatisticForm.setInstall_capacity(contract.getInstall_capacity());// 装机容量（MW）
			projectStatisticForm.setCont_money(contract.getCont_money());// 合同额(万元)

			String cont_status = null;
			Integer isOrNo = contract.getCont_initiation();// 是否立项
			boolean flag = false;// 是否签订合同
			if (contract.getCont_stime() != null) {
				flag = true;
			}
			if (isOrNo == 0) {// 未立项
				cont_status = ContStatus.intToStr(0);
			} else if (isOrNo == 1 && flag) {// 已签订
				cont_status = ContStatus.intToStr(2);
			} else {// 已立项_合同未签
				cont_status = ContStatus.intToStr(1);
				cont_status = cont_status.replace('_', '，');// 将_替换成，
			}
			projectStatisticForm.setCont_status(cont_status);// 合同状态
			projectStatisticForm.setCont_stime(contract.getCont_stime());// 合同签订日期

			listGoal.add(projectStatisticForm);
		}
		return listGoal;
	}

	/**
	 * 将contract封装成未返回合同统计表
	 * 
	 * @param it
	 * @return
	 */
	private List<NoBackContForm> contToNoBackCont(Iterator<Contract> it) {
		List<NoBackContForm> listGoal = new ArrayList<NoBackContForm>();
		int i = 0;
		while (it.hasNext()) {// 赋值顺序和表头无关
			i++;
			Contract contract = it.next();
			NoBackContForm noBackContForm = new NoBackContForm();
			noBackContForm.setNb_id(i);// 序号
			noBackContForm.setCont_project(contract.getCont_project());// 项目名称
			noBackContForm.setCont_client(contract.getCont_client());// 业主单位
			noBackContForm.setCont_money(contract.getCont_money());// 合同额(万元)
			if (contract.getManager() != null) {
				noBackContForm.setHandler(contract.getManager().getUser_name());// 经手人(设总)
			}
			if (contract.getCreator() != null) {
				noBackContForm.setHeader(contract.getCreator().getUser_name());// 负责人(主任)
			}

			listGoal.add(noBackContForm);
		}
		return listGoal;
	}

	// 查询光电院项目分项统计表
	@Override
	public List<ProjectStatisticForm> findProjectStatistic(Map<String, Object> map, Pager pager, String path) {
		List<Contract> listSource = contractDao.findContByPara(map, pager);
		Iterator<Contract> it = listSource.iterator();
		List<ProjectStatisticForm> listGoal = contToProStatis(it);

		return listGoal;
	}

	// 查询未返回合同统计表
	@Override
	public List<NoBackContForm> findNoBackCont(Map<String, Object> map, Pager pager, String path) {
		List<Contract> listSource = contractDao.findContByParaNoBack(map, pager);
		Iterator<Contract> it = listSource.iterator();
		List<NoBackContForm> listGoal = contToNoBackCont(it);

		return listGoal;
	}

	// 将JSONObject转成Map分项统计表
	@Override
	public Map<String, Object> JsonObjToMap(JSONObject jsonObject) {
		Integer cont_type = null;
		String pro_stage = null;
		Integer managerId = null;
		Integer cont_status = null;
		String province = null;
		String startTime = null;
		String endTime = null;
		String planTime = null;
		if (jsonObject.containsKey("contType")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("contType"))) {
				cont_type = Integer.valueOf(jsonObject.getString("contType"));// 合同类型
			}
		}
		if (jsonObject.containsKey("proStage")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("proStage"))) {
				pro_stage = jsonObject.getString("proStage");// 项目阶段
			}
		}
		if (jsonObject.containsKey("userId")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("userId"))) {
				managerId = Integer.valueOf(jsonObject.getString("userId"));// 设总
			}
		}
		if (jsonObject.containsKey("contStatus")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("contStatus"))) {
				cont_status = Integer.valueOf(jsonObject.getString("contStatus"));// 合同状态
			}
		}
		if (jsonObject.containsKey("province")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("province"))) {
				province = jsonObject.getString("province");// 省份
			}
		}
		if (jsonObject.containsKey("startDate")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("startDate"))) {
				startTime = jsonObject.getString("startDate");// 开始时间
			}
		}
		if (jsonObject.containsKey("endDate")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("endDate"))) {
				endTime = jsonObject.getString("endDate");// 结束时间
			}
		}
		if (jsonObject.containsKey("planDate")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("planDate"))) {
				planTime = jsonObject.getString("planDate");// 结束时间
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cont_type", cont_type);
		map.put("pro_stage", pro_stage);
		map.put("managerId", managerId);
		map.put("cont_status", cont_status);
		map.put("province", province);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("planTime", planTime);

		return map;
	}

	// 将JSONObject转成Map未返回合同统计表
	@Override
	public Map<String, Object> JsonObjToMapNoBack(JSONObject jsonObject) {
		Integer handler = null;
		Integer header = null;
		String province = null;
		String startTime = null;
		String endTime = null;
		if (jsonObject.containsKey("userId")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("userId"))) {
				handler = Integer.valueOf(jsonObject.getString("userId"));// 经手人（设总）
			}
		}
		if (jsonObject.containsKey("headerId")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("headerId"))) {
				header = Integer.valueOf(jsonObject.getString("headerId"));// 负责人（主任）
			}
		}
		if (jsonObject.containsKey("province")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("province"))) {
				province = jsonObject.getString("province");// 省份
			}
		}
		if (jsonObject.containsKey("startDate")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("startDate"))) {
				startTime = jsonObject.getString("startDate") + "-01";// 开始时间
			}
		}
		if (jsonObject.containsKey("endDate")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("endDate"))) {
				endTime = jsonObject.getString("endDate") + "-01";// 结束时间
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("handler", handler);
		map.put("header", header);
		map.put("province", province);
		map.put("startTime", startTime);
		map.put("endTime", endTime);

		return map;
	}

	// 查询分项统计表页码相关
	@Override
	public Pager pagerTotal(Map<String, Object> map, Integer page) {
		int totalRow = Integer.parseInt(contractDao.countTotal(map).toString());
		Pager pager = new Pager();
		pager.setPage(page);
		pager.setTotalRow(totalRow);

		return pager;
	}

	// 查询未返回合同统计表页码相关
	@Override
	public Pager pagerTotalNoBack(Map<String, Object> map, Integer page) {
		int totalRow = Integer.parseInt(contractDao.countTotalNoBack(map).toString());
		Pager pager = new Pager();
		pager.setPage(page);
		pager.setTotalRow(totalRow);

		return pager;
	}

	/**
	 * 将数字型字符串转换成字符串
	 * 
	 * @param str
	 * @return
	 */
	private String intStrToStr(String str) {
		String[] arr = { "规划", "预可研", "可研", "项目建议书", "初步设计", "发包、招标设计", "施工详图", "竣工图", "其他" };
		if (StringUtil.strIsNotEmpty(str)) {
			for (int i = 0; i < arr.length; i++) {
				if (str.contains(String.valueOf(i))) {
					str = str.replaceAll(String.valueOf(i), arr[i]);
				}
			}
			str = str.substring(0, str.length() - 1);// 去掉最后一个逗号
		}
		return str;
	}

	/************************************************ 张姣娜 **********************************/
	// 根据日期获取合同额到款对比表
	@Override
	public ComoCompareRemo findByDate(String firstDate, String secondDate) {

		List<Object> objectOne = contractDao.findByOneDate(firstDate);
		List<Object> objectTwo = contractDao.findByOneDate(secondDate);
		ComoCompareRemo comoCompareRemo = new ComoCompareRemo();
		// 获取第一年相关数据
		Object[] objOne = (Object[]) objectOne.get(0);
		if (objOne[0].equals(0.0)) {
			comoCompareRemo.setComo_one("");
		} else {
			comoCompareRemo.setComo_one(objOne[0].toString());
		}
		if (objOne[1].equals(0.0)) {
			comoCompareRemo.setRemo_one("");
		} else {
			comoCompareRemo.setRemo_one(objOne[1].toString());
		}
		comoCompareRemo.setCont_num_one(objOne[2].toString());
		// 获取第二年相关数据
		Object[] objTwo = (Object[]) objectTwo.get(0);
		if (objTwo[0].equals(0.0)) {
			comoCompareRemo.setComo_two("");
		} else {
			comoCompareRemo.setComo_two(objTwo[0].toString());
		}
		if (objTwo[1].equals(0.0)) {
			comoCompareRemo.setRemo_two("");
		} else {
			comoCompareRemo.setRemo_two(objTwo[1].toString());
		}
		comoCompareRemo.setCont_num_two(objTwo[2].toString());
		// 计算同比增长率
		if (objOne[0].equals(0.0)) {
			comoCompareRemo.setRatio_como("");
		} else {
			Double big = Double.valueOf(objTwo[0].toString());
			Double small = Double.valueOf(objOne[0].toString());
			Double ratio_como = (big - small) / small * 100;
			String ratio = String.format("%.2f", ratio_como);
			if (ratio_como > 0) {
				comoCompareRemo.setRatio_como("同比增长" + ratio + "%");
			} else if (ratio_como < 0) {
				comoCompareRemo.setRatio_como("同比减少" + ratio + "%");
			} else {
				comoCompareRemo.setRatio_como("相等");
			}
		}
		if (objOne[1].equals(0.0)) {
			comoCompareRemo.setRatio_remo("");
		} else {
			Double big = Double.valueOf(objTwo[1].toString());
			Double small = Double.valueOf(objOne[1].toString());
			Double ratio_remo = (big - small) / small * 100;
			String ratio = String.format("%.2f", ratio_remo);
			if (ratio_remo > 0)
				comoCompareRemo.setRatio_remo("同比增长" + ratio + "%");
			else if (ratio_remo < 0) {
				comoCompareRemo.setRatio_remo("同比减少" + ratio + "%");
			} else {
				comoCompareRemo.setRatio_remo("相等");
			}
		}
		if (objOne[2].equals(0)) {
			comoCompareRemo.setRatio_conum("");
		} else {
			Double big = Double.valueOf(objTwo[2].toString());
			Double small = Double.valueOf(objOne[2].toString());
			Double ratio_conum = (big - small) / small * 100;
			String ratio = String.format("%.2f", ratio_conum);
			if (ratio_conum > 0)
				comoCompareRemo.setRatio_conum("同比增长" + ratio + "%");
			else if (ratio_conum < 0) {
				comoCompareRemo.setRatio_conum("同比减少" + ratio + "%");
			} else {
				comoCompareRemo.setRatio_conum("相等");
			}
		}
		return comoCompareRemo;
	}

	// 根据日期获取新签合同额分析表
	@Override
	public List<NewComoAnalyse> findComoByDate(String firstDate, String secondDate) {
		List<Object> objects = contractDao.findComoByDate(firstDate, secondDate);
		Double totalOne = (double) 0;
		Double totalTwo = (double) 0;
		for (int i = 0; i < objects.size(); i++) {
			Object[] object = (Object[]) objects.get(i);
			totalOne += (double) object[1];
			totalTwo += (double) object[2];
		}
		List<NewComoAnalyse> newComos = new ArrayList<NewComoAnalyse>();
		for (int i = 0; i < objects.size(); i++) {
			NewComoAnalyse newComoAnalyse = new NewComoAnalyse();
			Integer orderNum = i + 1;
			newComoAnalyse.setOrder_number(orderNum.toString());
			Object[] objOne = (Object[]) objects.get(i);
			Double como_one = (double) objOne[1];
			Double como_two = (double) objOne[2];
			newComoAnalyse.setProvince(objOne[0].toString());
			if (como_one == 0) {
				newComoAnalyse.setComo_one("");
				newComoAnalyse.setRise_ratio("");
			} else {
				newComoAnalyse.setComo_one(objOne[1].toString());
				Double rise_ratio = (como_two - como_one) / como_one * 100;
				String ratio = String.format("%.2f", rise_ratio) + "%";
				newComoAnalyse.setRise_ratio(ratio);
			}
			if (como_two == 0) {
				newComoAnalyse.setComo_two("");
			} else {
				newComoAnalyse.setComo_two(objOne[2].toString());
			}
			if (totalOne == 0 || como_one == 0) {
				newComoAnalyse.setRatio_one_provi("");
			} else {
				Double ratio_one_provi = como_one / totalOne * 100;
				String ratio = String.format("%.2f", ratio_one_provi) + "%";
				newComoAnalyse.setRatio_one_provi(ratio);
			}
			if (totalTwo == 0 || como_two == 0) {
				newComoAnalyse.setRatio_two_provi("");
			} else {
				Double ratio_two_provi = como_two / totalTwo * 100;
				String ratio = String.format("%.2f", ratio_two_provi) + "%";
				newComoAnalyse.setRatio_two_provi(ratio);
			}
			newComos.add(newComoAnalyse);
		}
		NewComoAnalyse newComoAnalyse = new NewComoAnalyse();
		newComoAnalyse.setProvince("总计：");
		newComoAnalyse.setComo_one(totalOne.toString());
		newComoAnalyse.setComo_two(totalTwo.toString());
		newComos.add(newComoAnalyse);
		return newComos;
	}

	// 根据日期获取到款分析表
	@Override
	public List<NewRemoAnalyse> findRemoByDate(String firstDate, String secondDate) {
		List<Object> objects = receiveMoneyDao.findRemoByDate(firstDate, secondDate);
		List<NewRemoAnalyse> newRemos = new ArrayList<NewRemoAnalyse>();
		for (int i = 0; i < objects.size(); i++) {
			Object[] object = (Object[]) objects.get(i);
			NewRemoAnalyse newRemoAnalyse = new NewRemoAnalyse();
			Integer orderNum = i + 1;
			newRemoAnalyse.setOrder_number(orderNum.toString());
			newRemoAnalyse.setProvince(object[0].toString());
			if ((double) object[1] == 0) {
				newRemoAnalyse.setRemo_one("");
			} else {
				newRemoAnalyse.setRemo_one(object[1].toString());
			}
			if ((double) object[2] == 0) {
				newRemoAnalyse.setRemo_two("");
			} else {
				newRemoAnalyse.setRemo_two(object[2].toString());
			}
			if ((double) object[3] == 0) {
				newRemoAnalyse.setRemo_before("");
			} else {
				newRemoAnalyse.setRemo_before(object[3].toString());
			}
			if ((double) object[4] == 0) {
				newRemoAnalyse.setRemo_curr("");
			} else {
				newRemoAnalyse.setRemo_curr(object[4].toString());
			}
			if ((double) object[5] == 0) {
				newRemoAnalyse.setExp_remo_two_curr("");
			} else {
				Double exp_remo_two_curr = (double) object[5] - (double) object[2];
				newRemoAnalyse.setExp_remo_two_curr(String.format("%.2f", exp_remo_two_curr));
			}
			if ((double) object[6] == 0) {
				newRemoAnalyse.setExp_remo_two_before("");
			} else {
				Double exp_remo_two_before = (double) object[6] - (double) object[3];
				newRemoAnalyse.setExp_remo_two_before(String.format("%.2f", exp_remo_two_before));
			}
			newRemos.add(newRemoAnalyse);
		}
		// 在列表末尾追加统计信息
		Double totalRemoOne = (double) 0;
		Double totalRemoTwo = (double) 0;
		Double totalRemoBefore = (double) 0;
		Double totalRemoCurr = (double) 0;
		Double totalExpRemoTwoCurr = (double) 0;
		Double totalExpRemoTwoBefore = (double) 0;
		for (int i = 0; i < objects.size(); i++) {
			Object[] object = (Object[]) objects.get(i);
			totalRemoOne += (double) object[1];
			totalRemoTwo += (double) object[2];
			totalRemoBefore += (double) object[3];
			totalRemoCurr += (double) object[4];
			totalExpRemoTwoCurr += (double) object[5];
			totalExpRemoTwoBefore += (double) object[6];
		}
		NewRemoAnalyse newRemoAnalyse = new NewRemoAnalyse();
		newRemoAnalyse.setProvince("总计：");
		newRemoAnalyse.setRemo_one(totalRemoOne.toString());
		newRemoAnalyse.setRemo_two(totalRemoTwo.toString());
		newRemoAnalyse.setRemo_before(totalRemoBefore.toString());
		newRemoAnalyse.setRemo_curr(totalRemoCurr.toString());
		newRemoAnalyse.setExp_remo_two_curr(totalExpRemoTwoCurr.toString());
		newRemoAnalyse.setExp_remo_two_before(totalExpRemoTwoBefore.toString());
		newRemos.add(newRemoAnalyse);
		return newRemos;
	}

	// 查询光伏项目明细表
	@Override
	public List<SummarySheet> findSummaryByDate(String date, Pager pager) {
		List<Contract> listSource = contractDao.findSummaryByDate(date, pager);
		List<SummarySheet> listGoal = contToSummarySheet(listSource);
		return listGoal;
	}

	private List<SummarySheet> contToSummarySheet(List<Contract> listSource) {
		List<SummarySheet> listGoal = new ArrayList<SummarySheet>();
		String s_will = "";
		int count = 1;

		for (int i = 0; i < listSource.size(); i++) {
			Contract contract = listSource.get(i);
			SummarySheet summarySheet = new SummarySheet();

			// 处理分区域序号
			if (i == 0) {
				s_will = listSource.get(i).getProvince();// 获取第一行的数据,以便后面进行比较
			} else {
				String s_current = listSource.get(i).getProvince();
				if (s_will.equals(s_current)) {
					count++;
				} else {
					count = 1;
				}
				s_will = s_current;
			}

			summarySheet.setSub_num(String.valueOf(count));
			summarySheet.setOrder_num(String.valueOf(i + 1));
			summarySheet.setProvince(contract.getProvince());
			String proStageStr = contract.getPro_stage();// 设计阶段（数字类型字符串）
			proStageStr = intStrToStr(proStageStr);
			summarySheet.setPro_stage(proStageStr);
			summarySheet.setCont_project(contract.getCont_project());
			summarySheet.setCont_client(contract.getCont_client());
			if (contract.getInstall_capacity() != null) {
				summarySheet.setInstall_capacity(contract.getInstall_capacity().toString());
			}
			if (contract.getCont_money() == null) {
				contract.setCont_money((float) 0);
			}
			summarySheet.setCont_money(contract.getCont_money().toString());
			if (contract.getRemo_totalmoney() == 0 || contract.getCont_money() == 0
					|| contract.getRemo_totalmoney() < contract.getCont_money()) {
				summarySheet.setStatus("未结清");
			} else {
				summarySheet.setStatus("已结清");
			}

			listGoal.add(summarySheet);
		}
		return listGoal;
	}

	// 查询光伏项目明细表页码
	@Override
	public Pager pagerTotalSummary(String date, Integer page) {
		int totalRow = Integer.parseInt(contractDao.totalSummary(date).toString());
		Pager pager = new Pager();
		pager.setPage(page);
		pager.setTotalRow(totalRow);

		return pager;
	}

	// 根据日期导出当年光伏项目明细表
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ResponseEntity<byte[]> exportSummarySheet(String date, String path) {
		ResponseEntity<byte[]> byteArr = null;
		String fileName = "";
		String title = "";
		if (date.equals("")) {
			fileName = "光伏自营项目情况表.xlsx";
			title = "光伏自营项目情况表";
		} else {
			fileName = date + "年光伏自营项目情况表.xlsx";
			title = date + "年光伏自营项目情况表";
		}
		try {
			ExcelHelper<SummarySheet> ex = new ExcelHelper<SummarySheet>();
			path = FileHelper.transPath(fileName, path);// 解析后的上传路径
			OutputStream out = new FileOutputStream(path);

			List<Contract> listSource = contractDao.findSummaryByDate(date, null);
			List<SummarySheet> listGoal = contToSummarySheet(listSource);

			String[] header = { "序号", "分区域序号", "行政区域", "合同类别", "工程名称", "规模", "业主名称", "合同额（万元）", "项目状态", "备注" };// 顺序必须和对应实体一致
			ex.export2007Excel(title, header, (Collection) listGoal, out, "yyyy-MM-dd", 2);// 从0开始数，表格第二列相同数据合并单元格

			out.close();
			byteArr = FileHelper.downloadFile(fileName, path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArr;
	}

	// 根据日期导出多个年份光伏项目明细表
	@SuppressWarnings("rawtypes")
	public ResponseEntity<byte[]> exportSummarySheetList(Map<String, String> map, String path) {
		ResponseEntity<byte[]> byteArr = null;
		if (!map.get("startTime").equals("") && !map.get("endTime").equals("")) {
			Integer startTime = Integer.valueOf(map.get("startTime"));
			Integer endTime = Integer.valueOf(map.get("endTime"));
			Integer sheetNum = endTime - startTime + 1;
			try {
				ExcelHelper<ProjectStatisticForm> ex = new ExcelHelper<ProjectStatisticForm>();

				String fileName = startTime + "年至" + endTime + "年光电院项目分项统计表.xlsx";
				path = FileHelper.transPath(fileName, path);// 解析后的上传路径
				OutputStream out = new FileOutputStream(path);

				String title = " ";
				String[] header_dis = { "序号", "分区域序号", "行政区域", "合同类别", "工程名称", "规模", "业主名称", "合同额（万元）", "项目状态", "备注" };// 顺序必须和对应实体一致

				Map<Integer, String> titleMap = new HashMap<Integer, String>();// 每个sheet表名
				Map<Integer, String[]> headerMap = new HashMap<Integer, String[]>();// 每个sheet标题
				Map<Integer, List> mapList = new HashMap<Integer, List>();// 每个sheet内容
				for (int i = 0; i < sheetNum; i++) {
					title = String.valueOf(startTime + i) + "年光伏自营项目情况表 ";
					List<Contract> listSource = contractDao.findSummaryByDate(String.valueOf(startTime + i), null);
					List<SummarySheet> listGoal = contToSummarySheet(listSource);

					headerMap.put(i, header_dis);
					mapList.put(i, listGoal);
					titleMap.put(i, title);
				}
				ex.export2007MutiExcel(titleMap, headerMap, mapList, out, "yyyy-MM-dd", 2);// 从0开始数，表格第二列相同数据合并单元格

				out.close();
				byteArr = FileHelper.downloadFile(fileName, path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return byteArr;
	}

	/************************************************ 王慧敏 **********************************/
	// 导出光伏自营项目催款计划表
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseEntity<byte[]> exportProvisionPlan(Map<String, Object> map, String path) {
		ResponseEntity<byte[]> byteww = null;
		try {
			ExcelHelper<PaymentPlanListForm> ex = new ExcelHelper<PaymentPlanListForm>();
			String startTime = (String) map.get("startTime");
			String startPeriod;// 年月

			String endTime = (String) map.get("endTime");
			String endPeriod;// 年月

			String planTime = (String) map.get("planTime");

			String fileName;
			if (startTime == null || endTime == null) {
				fileName = "光伏自营项目催款计划表.xlsx";
			} else {
				startPeriod = startTime.substring(0, startTime.lastIndexOf("-"));
				endPeriod = endTime.substring(0, endTime.lastIndexOf("-"));
				fileName = startPeriod + "-" + endPeriod + "光伏自营项目催款计划表.xlsx";
			}
			path = FileHelper.transPath(fileName, path);// 解析后的上传路径
			OutputStream out = new FileOutputStream(path);
			List<Object> listSource = contractDao.findContByParw(map, null);// 筛选元数据
			List<PaymentPlanListForm> listGoal = contToProPlan(listSource);

			String title;
			if (startTime == null || endTime == null) {
				title = "光伏自营项目催款计划表签订项目";
			} else {
				startPeriod = startTime.substring(0, startTime.lastIndexOf("-"));
				endPeriod = endTime.substring(0, endTime.lastIndexOf("-"));
				title = "光伏自营项目催款计划表(" + startPeriod + "-" + endPeriod + "签订项目)";
			}
			String[] header;
			if (planTime == null) {
				String[] headers = { "序号", "行政区域", "工程名称", "业主名称", "合同金额", "累计已到款", "余额", "已开发票金额", "未开发票金额", "计划可催收款",
						"实际到款", "合同条款", "催款结果", "备注" };
				header = headers;
			} else {
				String[] headers = { "序号", "行政区域", "工程名称", "业主名称", "合同金额", "累计已到款", "余额", "已开发票金额", "未开发票金额",
						planTime + "计划可催收款", "实际到款", "合同条款", "催款结果", "备注" };
				header = headers;
			}

			ex.export2007Excel(title, header, (Collection) listGoal, out, "yyyy-MM-dd", 1);// 从0开始数，表格第一列相同数据合并单元格
			out.close();
			byteww = FileHelper.downloadFile(fileName, path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return byteww;
	}

	private List<PaymentPlanListForm> contToProPlan(List<Object> it) {
		List<PaymentPlanListForm> listGoal = new ArrayList<PaymentPlanListForm>();
		String sum_cont_money = "0.0";// 合同金额
		String sum_remo_totalmoney = "0.0";// 2015年累计已到款
		String sum_balance_money = "0.0";// 余额
		String sum_invo_totalmoney = "0.0";// 已开发票金额
		String sum_invo_not_totalmoney = "0.0";// 未开发票金额
		String sum_actual_money = "0.0";// 实际到款
		String sum_plan_payment = "0.0";// 计划到款

		List<Object> objects = it;
		for (int i = 0; i < objects.size(); i++) {
			Object[] objOne = (Object[]) objects.get(i);
			for (int j = 0; j < objOne.length; j++) {
				if (objOne[j] == null) {
					objOne[j] = "0.0";
				}
			}
			PaymentPlanListForm provisionPlanForm = new PaymentPlanListForm();
			provisionPlanForm.setOrder_num(String.valueOf(i + 1));// 序号
			provisionPlanForm.setProvince(objOne[0].toString());// 行政区域
			provisionPlanForm.setCont_project(objOne[1].toString());// 工程名称
			provisionPlanForm.setCont_client(objOne[2].toString());// 业主名称
			provisionPlanForm.setCont_money(objOne[3].toString());// 合同金额
			provisionPlanForm.setRemo_totalmoney(objOne[4].toString());// 到款金额
			String balance_money = DoubleFloatUtil.sub(objOne[3].toString(), objOne[4].toString());
			provisionPlanForm.setBalance_money(balance_money);// 余额
			provisionPlanForm.setInvo_totalmoney(objOne[5].toString());// 已开发票金额
			String invo_not_totalmoney = DoubleFloatUtil.sub(objOne[3].toString(), objOne[5].toString());
			provisionPlanForm.setInvo_not_totalmoney(invo_not_totalmoney);// 未开发票金额
			if (objOne.length > 6) {
				provisionPlanForm.setActual_money(objOne[6].toString());// 实际到款
				sum_actual_money = DoubleFloatUtil.add(sum_actual_money, objOne[6].toString());// 用于总计-实际到款

				provisionPlanForm.setPlan_payment(objOne[7].toString());// 计划到款
				sum_plan_payment = DoubleFloatUtil.add(sum_plan_payment, objOne[7].toString());// 用于总计-计划到款
			}
			sum_cont_money = DoubleFloatUtil.add(sum_cont_money, objOne[3].toString());// 用于总计-合同金额
			sum_remo_totalmoney = DoubleFloatUtil.add(sum_remo_totalmoney, objOne[4].toString());// 用于总计-累计已到款
			sum_balance_money = DoubleFloatUtil.add(sum_balance_money, balance_money);// 用于总计-余额
			sum_invo_totalmoney = DoubleFloatUtil.add(sum_invo_totalmoney, objOne[5].toString());// 用于总计-已开发票金额
			sum_invo_not_totalmoney = DoubleFloatUtil.add(sum_invo_not_totalmoney, invo_not_totalmoney);// 用于总计-未开发票金额
			listGoal.add(provisionPlanForm);
		}

		PaymentPlanListForm provisionPlanForm = new PaymentPlanListForm();
		provisionPlanForm.setProvince("总计：");
		provisionPlanForm.setCont_money(sum_cont_money);
		provisionPlanForm.setRemo_totalmoney(sum_remo_totalmoney);
		provisionPlanForm.setBalance_money(sum_balance_money);
		provisionPlanForm.setInvo_totalmoney(sum_invo_totalmoney);
		provisionPlanForm.setInvo_not_totalmoney(sum_invo_not_totalmoney);
		provisionPlanForm.setActual_money(sum_actual_money);// 实际到款
		provisionPlanForm.setPlan_payment(sum_plan_payment);// 计划到款
		listGoal.add(provisionPlanForm);

		return listGoal;
	}

	// 查询催款列表
	@Override
	public List<PaymentPlanListForm> findPaymentPlanList(Map<String, Object> map, Pager pager, String path) {
		List<Object> listSource = contractDao.findContByParw(map, pager);
		List<PaymentPlanListForm> listGoal = contToProPlan_payment(listSource);

		return listGoal;
	}

	private List<PaymentPlanListForm> contToProPlan_payment(List<Object> it) {
		List<PaymentPlanListForm> listGoal = new ArrayList<PaymentPlanListForm>();

		List<Object> objects = it;
		for (int i = 0; i < objects.size(); i++) {
			Object[] objOne = (Object[]) objects.get(i);
			for (int j = 0; j < objOne.length; j++) {
				if (objOne[j] == null) {
					objOne[j] = "0.0";
				}
			}
			PaymentPlanListForm provisionPlanForm = new PaymentPlanListForm();
			provisionPlanForm.setOrder_num(String.valueOf(i + 1));// 序号
			provisionPlanForm.setProvince(objOne[0].toString());// 行政区域
			provisionPlanForm.setCont_project(objOne[1].toString());// 工程名称
			provisionPlanForm.setCont_client(objOne[2].toString());// 业主名称
			provisionPlanForm.setCont_money(objOne[3].toString());// 合同金额
			provisionPlanForm.setRemo_totalmoney(objOne[4].toString());// 到款金额
			String balance_money = DoubleFloatUtil.sub(objOne[3].toString(), objOne[4].toString()) + "";
			provisionPlanForm.setBalance_money(balance_money);// 余额
			provisionPlanForm.setInvo_totalmoney(objOne[5].toString());// 已开发票金额
			String invo_not_totalmoney = DoubleFloatUtil.sub(objOne[3].toString(), objOne[5].toString()) + "";
			provisionPlanForm.setInvo_not_totalmoney(invo_not_totalmoney);// 未开发票金额
			if (objOne.length > 6) {
				provisionPlanForm.setActual_money(objOne[6].toString());// 实际到款
				provisionPlanForm.setPlan_payment(objOne[7].toString());// 计划到款
			}

			listGoal.add(provisionPlanForm);
		}
		return listGoal;
	}

	// 查询催款列表页码相关
	@Override
	public Pager pagerTotal_payment(Map<String, Object> map, Integer page) {
		int totalRow = Integer.parseInt(contractDao.countTotal_payment(map).toString());
		Pager pager = new Pager();
		pager.setPage(page);
		pager.setTotalRow(totalRow);

		return pager;
	}

}
