package org.young.model;

import org.young.util.StaticConstant;

/**
 * @see org.young.model.YoungModel
 * @author haoweige@126.com
 */
public class ErrorModel extends YoungModel {

	private static final long serialVersionUID = 1L;

	public void setError(Object value) {
		map.put(StaticConstant.PAGE_ERROR, value);
	}

}
