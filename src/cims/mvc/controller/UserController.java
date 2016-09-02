package cims.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cims.mvc.entity.User;
import cims.mvc.service.UserService;

/**
 * 用户管理
 * 
 * @author zjn
 */
@Controller
public class UserController {
	@Autowired
	UserService userService;

	// 默认初始页面
	@RequestMapping("")
	public String Create(Model model) {
		return "create";
	}

	// 创建新用户后保存
	@RequestMapping("/save")
	public String Save(@ModelAttribute("form") User user, Model model) { // user:视图层传给控制层的表单对象；model：控制层返回给视图层的对象
		User user2 = userService.save(user);
		model.addAttribute("user", user2);
		return "detail";
	}
}
