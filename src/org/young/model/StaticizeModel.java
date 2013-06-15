package org.young.model;

import org.young.util.StaticConstant;

public class StaticizeModel extends YoungModel {

	private static final long serialVersionUID = 1L;

	public void setPath(String value) {
		map.put(StaticConstant.STATICIZE_PATH, value);
	}

	public String getPath() {
		Object obj = map.get(StaticConstant.STATICIZE_PATH);
		if (obj != null)
			return (String) obj;
		return null;
	}

}
