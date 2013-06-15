package org.young.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import org.young.exception.ActionUndefinedException;
import org.young.exception.NullModelAndViewException;
import org.young.form.Form;
import org.young.model.StaticizeModel;
import org.young.model.YoungModel;
import org.young.util.FormParser;
import org.young.util.StaticConstant;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * The core servlet of young mvc frame
 * 
 * @author haoweige@126.com
 */
@SuppressWarnings("unchecked")
public abstract class YoungController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(YoungController.class);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Form form = FormParser.parseGet(request);
		doControl(request, response, form);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Form form = null;
		try {
			form = FormParser.parsePost(request);
		} catch (FileUploadException e) {
			logger.error(e);
		}
		doControl(request, response, form);
	}

	/**
	 * invoke business method and return response page
	 * 
	 * @param request
	 * @param response
	 * @param form
	 * @throws NullModelAndViewException
	 * @throws ActionUndefinedException
	 * @throws IOException
	 */
	protected void doControl(HttpServletRequest request,
			HttpServletResponse response, Form form)
			throws NullModelAndViewException, ActionUndefinedException,
			IOException {
		ModelAndView arg = null;
		try {
			arg = doHandle(request, response, form);
		} catch (Exception e) {
			logger.error(e);
		}
		if (arg == null) {
			throw new NullModelAndViewException();
		}
		switch (arg.getAction()) {
		case ModelAndView.ACTION_TEMPLATE:
			template(request, response, arg);
			break;
		case ModelAndView.ACTION_DISPATCH:
			dispatch(request, response, arg);
			break;
		case ModelAndView.ACTION_REDIRECT:
			redirect(request, response, arg);
			break;
		case ModelAndView.ACTION_STATICIZE:
			staticize(request, response, arg);
			break;
		default:
			throw new ActionUndefinedException();
		}
	}

	/**
	 * output templete page
	 * 
	 * @param request
	 * @param response
	 * @param arg
	 * @throws IOException
	 */
	protected void template(HttpServletRequest request,
			HttpServletResponse response, ModelAndView arg) throws IOException {
		logger.info("template path=>" + arg.getView());
		String encoding = "UTF-8";
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding(encoding);
		OutputStream os = response.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os, encoding);
		BufferedWriter out = new BufferedWriter(osw);
		processTemplate(arg, out);
	}

	/**
	 * dispatch request forward view
	 * 
	 * @param request
	 * @param response
	 * @param arg
	 */
	protected void dispatch(HttpServletRequest request,
			HttpServletResponse response, ModelAndView arg) {
		String path = basepath(request) + arg.getView();
		logger.info("dispatch path=>" + path);
		RequestDispatcher dispatcher = request.getRequestDispatcher(path);
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/**
	 * redirect request forward view
	 * 
	 * @param request
	 * @param response
	 * @param arg
	 */
	protected void redirect(HttpServletRequest request,
			HttpServletResponse response, ModelAndView arg) {
		String path = basepath(request) + arg.getView();
		logger.info("redirect path=>" + path);
		try {
			response.sendRedirect(path);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/**
	 * generate static html
	 * 
	 * @param request
	 * @param response
	 * @param arg
	 * @throws IOException
	 */
	protected void staticize(HttpServletRequest request,
			HttpServletResponse response, ModelAndView arg) throws IOException {
		logger.info("staticize path=>" + StaticConstant.STATICIZE_PATH);
		if (arg.getModel() != null) {
			StaticizeModel model = (StaticizeModel) arg.getModel();
			String path = StaticConstant.WEB_ROOT + model.getPath();
			File file = new File(path);
			OutputStream os = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
			BufferedWriter out = new BufferedWriter(osw);
			processTemplate(arg, out);
		}

	}

	/**
	 * The core business method of young mvc frame
	 * 
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 */
	protected abstract ModelAndView doHandle(HttpServletRequest request,
			HttpServletResponse response, Form form);

	/**
	 * validate whether all the parameters aren't null or not
	 * 
	 * @param form
	 * @return
	 */
	protected boolean validate(Form form) {
		Set<String> names = form.getPropertyNames();
		Iterator<String> iterator = names.iterator();
		while (iterator.hasNext()) {
			String name = iterator.next();
			Object value = form.getProperty(name);
			if (value == null)
				return false;
			// if (value instanceof String && "".equals(value))
			// return false;
		}
		return true;
	}

	/**
	 * fill model into template so as to output
	 * 
	 * @param arg0
	 * @param arg1
	 */
	protected void processTemplate(ModelAndView arg0, Writer arg1) {
		ServletContext context = getServletContext();
		Configuration config = (Configuration) context
				.getAttribute(StaticConstant.TEMPLATE_CONFIG);
		try {
			String view = arg0.getView();
			Template template = config.getTemplate(view, "UTF-8");
			YoungModel model = arg0.getModel();
			if (model == null)
				model = new YoungModel();
			template.process(model.toMap(), arg1);
		} catch (IOException e) {
			logger.error(e);
		} catch (TemplateException e) {
			logger.error(e);
		}
	}

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
