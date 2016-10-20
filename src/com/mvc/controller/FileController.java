package com.mvc.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.base.constants.SessionKeyConstants;
import com.mvc.entity.Contract;
import com.mvc.entity.Files;
import com.mvc.entity.User;
import com.mvc.service.ContractService;
import com.mvc.service.FileService;

import net.sf.json.JSONObject;

/**
 * 文件上传
 * 
 * @author wangrui
 * @date 2016-10-14
 */
@Controller
@Scope("prototype")
@RequestMapping("/file")
public class FileController {

	// private static Logger logger = Logger.getLogger(FileController.class);

	@Autowired
	FileService fileService;
	@Autowired
	ContractService contractService;

	/**
	 * 上传文件
	 * 
	 * @param request（file,contId）
	 * @return true,false
	 * @throws IOException
	 */
	@RequestMapping("/upload.do")
	public @ResponseBody String upload(HttpServletRequest request, HttpSession session) throws IOException {
		boolean flag = true;
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {// 判断是否有文件上传
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;// 转换成多部分request
			Iterator<String> iter = multiRequest.getFileNames();// request中的所有文件名
			String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");// 上传服务器的路径
			File fileDir = new File(path);
			if (!fileDir.exists() && !fileDir.isDirectory()) {// 判断/upload目录是否存在
				fileDir.mkdir();// 创建目录
			}
			Date date = null;
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddhhmmssSSS");// 定义到毫秒
			String nowStr = "";
			Files fileBean = null;

			int contId = (int) session.getAttribute("cont_id");// 从session中取出con_id
			User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
			Contract contract = contractService.selectContById(contId);
			while (iter.hasNext()) {// 文件存储失败和存入数据库失败，都是失败
				MultipartFile file = multiRequest.getFile(iter.next());// 将要上传的文件
				fileBean = new Files();
				long time = System.currentTimeMillis();
				date = new Date();
				nowStr = dateformat.format(date);
				if (file != null) {
					try {
						String myFileName = file.getOriginalFilename();// 当前上传文件的文件名称
						String filename = myFileName.substring(0, myFileName.lastIndexOf("."));// 去掉后缀的文件名
						String suffix = myFileName.substring(myFileName.lastIndexOf(".") + 1);// 后缀
						if (myFileName.trim() != "") {// 如果名称不为"",说明该文件存在，否则说明该文件不存在
							path += "\\" + filename + nowStr + "." + suffix;// 定义上传路径
							File localFile = new File(path);
							file.transferTo(localFile);
						}
						// 将记录写入数据库
						fileBean.setFile_name(myFileName);// 文件名
						fileBean.setFile_type(suffix);// 文件类型，后缀
						fileBean.setFile_path(path);// 文件路径
						fileBean.setFile_ctime(new Date(time));// 创建时间
						fileBean.setFile_isdelete(0);// 是否删除
						fileBean.setContract(contract);// 所属合同
						fileBean.setUser(user);// 上传者
						flag = fileService.addFile(fileBean);
						if (flag == false) {
							break;
						}
					} catch (Exception e) {
						flag = false;
						e.printStackTrace();
					}
				}
			}
		}
		return String.valueOf(flag);
	}

	/**
	 * 根据合同ID获取文件列表
	 * 
	 * @param request
	 * @return 文件列表list
	 */
	@RequestMapping("/selectFileByConId.do")
	public @ResponseBody String selectFileByConId(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		int cont_id = Integer.parseInt(request.getParameter("conId"));
		List<Files> list = fileService.findFileByConId(cont_id);
		jsonObject.put("list", list);
		return jsonObject.toString();
	}

	/**
	 * 根据文件ID删除文件
	 * 
	 * @param request
	 * @return 该合同下的文件列表list
	 */
	@RequestMapping("/deleteFileById.do")
	public @ResponseBody String deleteFileById(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		int file_id = Integer.parseInt(request.getParameter("fileId"));
		Files file = fileService.findFileById(file_id);
		if (file != null) {
			int cont_id = file.getContract().getCont_id();
			// 先删除，后获取
			boolean flag = fileService.deleteById(file_id);
			if (flag) {
				List<Files> list = fileService.findFileByConId(cont_id);
				jsonObject.put("list", list);
			}
		}
		return jsonObject.toString();
	}

	/**
	 * 文件下载
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/download.do")
	public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException {
		int file_id = Integer.parseInt(request.getParameter("file_id"));
		Files fileBean = fileService.findFileById(file_id);
		String fileName = fileBean.getFile_name();
		File file = new File(fileBean.getFile_path());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", fileName);

		ResponseEntity<byte[]> byteArr = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers,
				HttpStatus.OK);
		return byteArr;
	}

}
