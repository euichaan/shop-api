package com.app.global.error.exception;

import com.app.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

	public EntityNotFoundException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
