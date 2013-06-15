package org.young.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.young.util.HTMLProcessor;

/**
 * HTMLInputFilter is used to protect web application from xss attack
 * 
 * @author haoweige@126.com
 */
public class HTMLInputFilter implements Filter {

	private HTMLProcessor processor;

	@Override
	public void destroy() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		Map map = request.getParameterMap();
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			String key = String.valueOf(iterator.next());
			String value = String.valueOf(map.get(key));
			value = processor.process(value);
			map.put(key, value);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		processor = new HTMLProcessor();
	}

}
