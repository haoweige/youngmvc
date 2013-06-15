package org.young.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * @author haoweige@126.com
 */
public class CookieUtil {

	private final static Integer MAX_AGE = 60 * 60;
	private final static String PATH = "/";

	public static Cookie[] getCookieArray(HttpServletRequest request) {
		Cookie[] cookieArray = request.getCookies();
		return cookieArray;
	}

	public static Map<String, Cookie> getCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = null;
		Cookie[] cookieArray = getCookieArray(request);
		if (cookieArray != null) {
			cookieMap = new HashMap<String, Cookie>();
			for (Cookie cookie : cookieArray) {
				String cookieName = cookie.getName();
				cookieMap.put(cookieName, cookie);
			}
		}
		return cookieMap;
	}

	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie cookie = null;
		Map<String, Cookie> cookieMap = getCookieMap(request);
		if (cookieMap != null && cookieMap.containsKey(cookieName))
			cookie = cookieMap.get(cookieName);
		return cookie;
	}

	public static boolean addCookie(String cookieName, String cookieValue,
			HttpServletResponse response) {
		if (StringUtils.isNotEmpty(cookieName)
				&& StringUtils.isNotEmpty(cookieValue)) {
			Cookie cookie = new Cookie(cookieName, cookieValue);
			cookie.setMaxAge(MAX_AGE);
			cookie.setPath(PATH);
			response.addCookie(cookie);
			return true;
		}
		return false;
	}

	public static boolean removeCookie(String cookieName,
			HttpServletResponse response) {
		if (StringUtils.isNotEmpty(cookieName)) {
			Cookie cookie = new Cookie(cookieName, null);
			cookie.setMaxAge(0);
			cookie.setPath(PATH);
			response.addCookie(cookie);
			return true;
		}
		return false;
	}

}
