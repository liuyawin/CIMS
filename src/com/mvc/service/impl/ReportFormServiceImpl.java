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
import com.mvc.entity.ComoCompareRemo;
import com.mvc.entity.Contract;
import com.mvc.entity.NewComoAnalyse;
import com.mvc.entity.NewRemoAnalyse;
import com.mvc.entity.NoBackContForm;
import com.mvc.entity.ProjectStatisticForm;
import com.mvc.service.ReportFormService;
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

				String[] titles = { year + "年光电院分布式光伏项目统计表", year + "年光电院光伏项目统计表（不含分布式）", year + "年光电院光热项目统计表" };
				String[] header_dis = { "序号", "合同类型hidden", "项目名称", "项目设总", "所在地", "设计阶段", "装机容量（MW）", "合同额（万元）",
						"合同状态", "签订日期hidden", "备注" };// 顺序必须和对应实体一致

				Map<Integer, String[]> headerMap = new HashMap<Integer, String[]>();// 每个sheet的标题，暂时用统一标题
				headerMap.put(0, header_dis);
				headerMap.put(1, header_dis);
				headerMap.put(2, header_dis);
				headerMap.put(3, header_dis);

				Map<Integer, List> mapList = new HashMap<Integer, List>();// 每个sheet中内容
				mapList.put(0, listGoal_dis);
				mapList.put(1, listGoal_tra);
				mapList.put(2, listGoal_pho);
				mapList.put(2, listGoal_other);

				ex.export2007MutiExcel(titles, headerMap, mapList, out, "yyyy-MM-dd");
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
				default:
					break;
				}

				String[] header = { "序号", "合同类型hidden", "项目名称", "项目设总", "所在地", "设计阶段", "装机容量（MW）", "合同额（万元）", "合同状态",
						"签订日期hidden", "备注" };// 顺序必须和对应实体一致
				ex.export2007Excel(title, header, (Collection) listGoal, out, "yyyy-MM-dd");
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
			ex.export2007Excel(title, header, (Collection) listGoal, out, "yyyy-MM-dd");

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
			if (contract.getCreator() != null) {
				noBackContForm.setHandler(contract.getCreator().getUser_name());// 经手人
			}
			if (contract.getManager() != null) {
				noBackContForm.setHeader(contract.getManager().getUser_name());// 负责人
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
				startTime = jsonObject.getString("startDate") + "-01";// 开始时间
			}
		}
		if (jsonObject.containsKey("endDate")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("endDate"))) {
				endTime = jsonObject.getString("endDate") + "-01";// 结束时间
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

		return map;
	}

	// 将JSONObject转成Map未返回合同统计表
	@Override
	public Map<String, Object> JsonObjToMapNoBack(JSONObject jsonObject) {
		Integer handler = null;
		String province = null;
		String startTime = null;
		String endTime = null;
		if (jsonObject.containsKey("userId")) {
			if (StringUtil.strIsNotEmpty(jsonObject.getString("userId"))) {
				handler = Integer.valueOf(jsonObject.getString("userId"));// 经手人
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
			newComoAnalyse.setTotal_one(totalOne.toString());
			newComoAnalyse.setTotal_two(totalTwo.toString());
			newComos.add(newComoAnalyse);
		}
		return newComos;
	}

	// 根据日期获取到款分析表
	@Override
	public List<NewRemoAnalyse> findRemoByDate(String firstDate, String secondDate) {
		return null;
	}

}
