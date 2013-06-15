package org.young.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.young.exception.AuthParamUnsetException;
import org.young.form.Form;
import org.young.util.FormParser;
import org.young.util.StaticConstant;

public class AuthorizeFilter implements Filter {

	private Logger logger = Logger.getLogger(AuthorizeFilter.class);

	private String loginURI;
	private Set<Pattern> authPatternSet;

	@Override
	public void destroy() {
		authPatternSet = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String URI = request.getRequestURI();
		if (isLoggedNeeded(URI) && isNotLogged(request)) {
			StringBuffer buf = new StringBuffer();
			buf.append(request.getContextPath());
			buf.append("/").append(loginURI);
			response.sendRedirect(buf.toString());
			cacheRequest(request);
		} else {
			chain.doFilter(request, response);
		}
	}

	private static final Pattern REQUEST_PATH_PATTERN = Pattern
			.compile("/[0-9a-zA-Z]+/(.*)");

	/**
	 * request that contains params is unsupported
	 * 
	 * @param request
	 */
	private void cacheRequest(HttpServletRequest request) {
		HttpSession cache = request.getSession();
		String URI = request.getRequestURI();
		Matcher matcher = REQUEST_PATH_PATTERN.matcher(URI);
		if (matcher.find()) {
			/* cache URI */
			String path = matcher.group(1);
			cache.setAttribute(StaticConstant.REQUEST_PATH, path);
			/* cache Form */
			String method = request.getMethod();
			Form form = null;
			if ("GET".equals(method)) {
				form = FormParser.parseGet(request);
			} else if ("POST".equals(method)) {
				try {
					form = FormParser.parsePost(request);
				} catch (FileUploadException e) {
					logger.error(e);
				} catch (IOException e) {
					logger.error(e);
				}
			}
			cache.setAttribute(StaticConstant.REQUEST_FORM, form);
		}
	}

	private boolean isLoggedNeeded(String URI) {
		if (authPatternSet == null)
			return false;
		Iterator<Pattern> iterator = authPatternSet.iterator();
		while (iterator.hasNext()) {
			Pattern pattern = iterator.next();
			Matcher matcher = pattern.matcher(URI);
			if (matcher.find()) {
				return true;
			}
		}
		return false;
	}

	private boolean isNotLogged(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object user = session.getAttribute(StaticConstant.SESSION_USER);
		if (user == null)
			return true;
		return false;
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		loginURI = config.getInitParameter("loginURI");
		if (StringUtils.isEmpty(loginURI))
			throw new AuthParamUnsetException();
		authPatternSet = new HashSet<Pattern>();
		String authURI = config.getInitParameter("authURI");
		if (authURI != null) {
			String[] authURIArr = authURI.split(",");
			for (int i = 0; i < authURIArr.length; i++) {
				Pattern pattern = Pattern.compile(authURIArr[i]);
				authPatternSet.add(pattern);
			}
		}

	}

}
