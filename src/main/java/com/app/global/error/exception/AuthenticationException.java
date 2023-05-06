package com.app.global.error.exception;

import com.app.global.error.ErrorCode;

public class AuthenticationException extends BusinessException {

	public AuthenticationException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
