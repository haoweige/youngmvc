package org.young.model;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import org.young.util.StaticConstant;

/**
 * The core model for young mvc frame
 * 
 * @author haoweige@126.com
 */
public class YoungModel implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Map<String, Object> map;

	public YoungModel() {
		map = new Hashtable<String, Object>();
	}

	/**
	 * add or update the name value into model
	 * 
	 * @param name
	 * @param value
	 */
	public void setProperty(String name, Object value) {
		map.put(name, value);
	}

	/**
	 * add or update the user into model
	 * 
	 * @param value
	 */
	public void setSessionUser(Object value) {
		setProperty(StaticConstant.SESSION_USER, value);
	}

	/**
	 * add or update basepath into model
	 * 
	 * @param value
	 */
	public void setBasePath(String value) {
		setProperty("basepath", value);
	}

	/**
	 * convert model to map
	 * 
	 * @return
	 */
	public Map<String, Object> toMap() {
		return map;
	}
}
