package com.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 */
@Component
public class SharedRenderVariableInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

	// 系统启动并初始化一次的变量
	private Map<String, Object> globalRenderVariables = new HashMap<String, Object>();

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView == null) {
			return;
		}

		String viewName = modelAndView.getViewName();
		if (viewName != null && !viewName.startsWith("redirect:")) {
			modelAndView.addAllObjects(globalRenderVariables);
			modelAndView.addAllObjects(perRequest(request, response));
		}
	}

	protected Map<String, Object> perRequest(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> model = new HashMap<String, Object>();

		model.put("ctx", request.getContextPath());
		return model;
	}

	// 用于初始化 sharedRenderVariables, 全局共享变量请尽量用global前缀
	private void initSharedRenderVariables() {

	}

	// 在系统启动时会执行
	public void afterPropertiesSet() throws Exception {
		initSharedRenderVariables();
	}
}
