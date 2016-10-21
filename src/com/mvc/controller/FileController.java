package com.mvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
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
			createDir(path);

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
	 * 单个文件下载（前台读取）
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/downloadSingle.do")
	public ResponseEntity<byte[]> downloadSingle(HttpServletRequest request) throws IOException {
		int file_id = Integer.parseInt(request.getParameter("file_id"));
		Files fileBean = fileService.findFileById(file_id);
		String fileName = fileBean.getFile_name();
		ResponseEntity<byte[]> byteArr = SingleDownloadFile(fileName, fileBean.getFile_path());
		return byteArr;
	}

	/**
	 * 文件下载（多个文件:格式为压缩文件，单个文件：不压缩。前台读取）
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/download.do")
	public ResponseEntity<byte[]> downloadFiles(HttpServletRequest request, HttpSession session) throws IOException {
		ResponseEntity<byte[]> byteArr = null;
		int cont_id = (int) session.getAttribute("cont_id");// 从session中获取cont_id
		List<Files> list = fileService.findFileByConId(cont_id);
		// 在服务器端创建打包下载的临时文件
		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/download");
		int file_num = list.size();
		if (file_num == 1) {// 单个文件下载
			Files fileBean = list.get(0);
			byteArr = SingleDownloadFile(fileBean.getFile_name(), fileBean.getFile_path());
		} else if (file_num > 1) {// 多文件压缩下载
			byteArr = mutiDownloadFile(list, path);
		}
		return byteArr;
	}

	/**
	 * 根据路径确定目录，没有目录，则创建目录
	 * 
	 * @param path
	 */
	public void createDir(String path) {
		File fileDir = new File(path);
		if (!fileDir.exists() && !fileDir.isDirectory()) {// 判断/download目录是否存在
			fileDir.mkdir();// 创建目录
		}
	}

	/**
	 * 单个文件下载（后台调用）
	 * 
	 * @param fileName
	 * @param zipPath
	 * @return
	 */
	public ResponseEntity<byte[]> SingleDownloadFile(String fileName, String zipPath) {
		try {
			fileName = new String(fileName.getBytes("GB2312"), "ISO_8859_1");// 避免文件名中文不显示
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		File file = new File(zipPath);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", fileName);

		ResponseEntity<byte[]> byteArr = null;
		try {
			byteArr = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArr;
	}

	/**
	 * 多文件文件下载（后台调用）
	 * 
	 * @param list
	 * @param path
	 * @return
	 */
	public ResponseEntity<byte[]> mutiDownloadFile(List<Files> list, String path) {
		ResponseEntity<byte[]> byteArr = null;
		List<File> files = new ArrayList<File>();// 文件list
		Iterator<Files> it = list.iterator();
		int file_id = 0;
		boolean flag = true;
		String cont_name = "";// 合同名用作压缩文件的名称
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddhhmmss");// 定义到秒
		String nowStr = dateformat.format(date);

		while (it.hasNext()) {
			file_id = it.next().getFile_id();
			Files fileBean = fileService.findFileById(file_id);
			if (flag) {// 合同名只需获取一次
				cont_name = fileBean.getContract().getCont_name();
				flag = false;
			}
			File file = new File(fileBean.getFile_path());
			files.add(file);
		}

		String fileName = cont_name + nowStr + ".zip";// 压缩文件名格式：合同名+日期+.zip
		createDir(path);
		String zipPath = path + "\\" + fileName;// 压缩文件路径
		File file = new File(zipPath);
		byte[] buffer = new byte[1024];
		try {
			ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(file));// 压缩流

			for (int i = 0; i < files.size(); i++) {
				FileInputStream fis = new FileInputStream(files.get(i));
				zipStream.putNextEntry(new ZipEntry(files.get(i).getName()));
				zipStream.setEncoding("GBK"); // 设置压缩文件内的字符编码，不然会变成乱码
				int len;
				while ((len = fis.read(buffer)) > 0) {// 读入需要下载的文件的内容，打包到zip文件
					zipStream.write(buffer, 0, len);
				}
				zipStream.closeEntry();
				fis.close();
			}
			zipStream.close();

			byteArr = SingleDownloadFile(fileName, zipPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteArr;
	}

}
