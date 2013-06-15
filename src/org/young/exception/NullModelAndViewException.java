package org.young.exception;

import javax.servlet.ServletException;

public class NullModelAndViewException extends ServletException {

	private static final long serialVersionUID = 1L;
	private static String MESSAGE = "cause maybe: 1.No ModelAndView is returned 2.Business method causes exception occurs during execution";

	public NullModelAndViewException(String message) {
		super(message);
	}

	public NullModelAndViewException() {
		super(MESSAGE);
	}

}
