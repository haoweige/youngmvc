package org.young.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

public class UrlRewriteSpecFilter extends UrlRewriteFilter {

	private String[] excludeURIArr;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String URI = request.getRequestURI();
		if (contains(URI)) {
			chain.doFilter(request, response);
		} else {
			super.doFilter(request, response, chain);
		}
	}

	private boolean contains(String path) {
		if (excludeURIArr != null && excludeURIArr.length > 0) {
			for (int i = 0; i < excludeURIArr.length; i++) {
				if (path.contains(excludeURIArr[i])) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		String excludeURI = config.getInitParameter("excludeURI");
		if (excludeURI != null)
			excludeURIArr = excludeURI.split(",");
	}

}
