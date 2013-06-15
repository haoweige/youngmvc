package org.young.handler;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.young.controller.ModelAndView;
import org.young.form.Form;
import org.young.util.StaticConstant;

public abstract class YoungHandler {

	/**
	 * The core business method of young mvc frame
	 * 
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	public abstract ModelAndView doHandle(HttpServletRequest request,
			HttpServletResponse response, Form form);

	/**
	 * get the basepath of web application
	 * 
	 * @param request
	 * @return
	 */
	protected String basepath(HttpServletRequest request) {
		StringBuffer basepath = new StringBuffer();
		basepath.append(request.getScheme());
		basepath.append("://");
		basepath.append(request.getServerName());
		basepath.append(":");
		basepath.append(request.getServerPort());
		basepath.append(request.getContextPath());
		basepath.append("/");
		// logger.info("basepath=>" + basepath.toString());
		return basepath.toString();
	}

	/**
	 * fill value into request error attribute so as to dispatch
	 * 
	 * @param request
	 * @param value
	 */
	protected void setError(HttpServletRequest request, String value) {
		setPageAttribute(request, StaticConstant.PAGE_ERROR, value);
	}

	/**
	 * get the error attribute of request
	 * 
	 * @param request
	 * @return
	 */
	protected String getError(HttpServletRequest request) {
		Object obj = getPageAttribute(request, StaticConstant.PAGE_ERROR);
		if (obj != null)
			return (String) obj;
		return null;
	}

	/**
	 * fill value into request attribute so as to dispatch
	 * 
	 * @param request
	 * @param name
	 * @param value
	 */
	protected void setPageAttribute(HttpServletRequest request, String name,
			Object value) {
		request.setAttribute(name, value);
	}

	/**
	 * get the attribute of request
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	protected Object getPageAttribute(HttpServletRequest request, String name) {
		return request.getAttribute(name);
	}

	/**
	 * fill value into session attribute
	 * 
	 * @param request
	 * @param name
	 * @param value
	 */
	protected void setSessionAttribute(HttpServletRequest request, String name,
			Object value) {
		HttpSession session = getSession(request);
		session.setAttribute(name, value);
	}

	/**
	 * get the attribute of session
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	protected Object getSessionAttribute(HttpServletRequest request, String name) {
		HttpSession session = getSession(request);
		return session.getAttribute(name);
	}

	/**
	 * remove all the attributes of session
	 * 
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	protected void clearSessionAttribute(HttpServletRequest request) {
		HttpSession session = getSession(request);
		Enumeration names = session.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			session.setAttribute(name, null);
		}
	}

	protected HttpSession getSession(HttpServletRequest request) {
		return request.getSession();
	}

	protected ServletContext getServletContext(HttpServletRequest request) {
		return getSession(request).getServletContext();
	}

	/**
	 * get the attribute value of servlet context
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	protected Object getContextAttribute(HttpServletRequest request, String name) {
		return getServletContext(request).getAttribute(name);
	}

	/**
	 * set the attribute of servlet context
	 * 
	 * @param request
	 * @param name
	 * @param value
	 */
	protected void setContextAttribute(HttpServletRequest request, String name,
			String value) {
		getServletContext(request).setAttribute(name, value);
	}

}
