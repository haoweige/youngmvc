package org.young.exception;

import javax.servlet.ServletException;

public class AuthParamUnsetException extends ServletException {

	private static final long serialVersionUID = 1L;
	private static String MESSAGE = "cause: you unset the loginURI or the authURI";

	public AuthParamUnsetException(String message) {
		super(message);
	}

	public AuthParamUnsetException() {
		super(MESSAGE);
	}
}
