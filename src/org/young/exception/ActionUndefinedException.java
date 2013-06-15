package org.young.exception;

import javax.servlet.ServletException;

public class ActionUndefinedException extends ServletException {

	private static final long serialVersionUID = 1L;
	private static String MESSAGE = "cause: you set a incorrect action value for ModelAndView";

	public ActionUndefinedException(String message) {
		super(message);
	}

	public ActionUndefinedException() {
		super(MESSAGE);
	}
}
