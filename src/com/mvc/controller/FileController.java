package com.mvc.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.mvc.entity.Files;
import com.mvc.service.FileService;

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

	/**
	 * 上传文件
	 * 
	 * @param request
	 * @return true,false
	 * @throws IOException
	 */
	@RequestMapping("/upload.do")
	public @ResponseBody String upload(HttpServletRequest request) throws IOException {
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

	// @RequestMapping("/download.do")
	// public @ResponseBody String download(HttpServletRequest request) throws
	// IOException {
	// String filename = request.getParameter("filename");// 接收前台传过来的文件名
	//
	// }
}
