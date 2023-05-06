package com.app.api.login.validator;

import org.springframework.stereotype.Service;

import com.app.domain.member.constant.MemberType;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;

@Service
public class OauthValidator {

	public void validateMemberType(String memberType) {
		if (!MemberType.isMemberType(memberType)) {
			throw new BusinessException(ErrorCode.INVALID_MEMBER_TYPE);
		}
	}
}
