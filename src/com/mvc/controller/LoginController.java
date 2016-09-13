/**
 * 
 */
package com.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zjn
 * @date 2016年9月7日
 */

@Controller
@RequestMapping("/login")
public class LoginController {
	/**
	 * 加载默认起始页
	 * 
	 * @return
	 */
	@RequestMapping("/toLoginPage.do")
	public String contractInformationPage() {
		return "contractInformation/index";
	}

	@RequestMapping("/toLoginPage1.do")
	public String contractInformationPage1() {
		return "assistant2/index";
	}
}
