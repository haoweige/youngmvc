package org.young.controller;

import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.young.form.Form;

public class InvokerController extends YoungController {

	private static final long serialVersionUID = 1L;

	protected static Logger logger = Logger.getLogger(InvokerController.class);

	@Override
	protected ModelAndView doHandle(HttpServletRequest request,
			HttpServletResponse response, Form form) {
		return invokeMethod(request, response, form);
	}

	protected Method[] methods = null;

	protected Method findMethod(String name) {
		for (Method method : methods) {
			if (method.getName().equals(name))
				return method;
		}
		return null;
	}

	protected ModelAndView invokeMethod(HttpServletRequest request,
			HttpServletResponse response, Form form) {
		String URL = request.getRequestURL().toString();
		int index = URL.lastIndexOf("/") + 1;
		String name = URL.substring(index).trim();
		logger.info("invoke method=>" + name);
		Method method = findMethod(name);
		try {
			if (method != null) {
				Object obj = this.getClass().newInstance();
				Object[] args = new Object[] { request, response, form };
				/* be careful : the obj cann't be 'this' */
				return (ModelAndView) method.invoke(obj, args);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		logger.error(name + " method doesn't exist");
		return null;
	}

	@Override
	public void init() throws ServletException {
		super.init();
		methods = this.getClass().getMethods();
	}

}
