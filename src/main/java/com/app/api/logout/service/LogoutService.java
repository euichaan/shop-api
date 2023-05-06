package com.app.api.logout.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.constant.TokenType;
import com.app.global.jwt.service.TokenManager;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService {

	private final MemberService memberService;
	private final TokenManager tokenManager;

	public void logout(final String accessToken) {

		// 1. 정상적인 토큰인지 검증
		tokenManager.validateToken(accessToken);

		// 2. 토큰 타입 확인 (access or refresh)
		Claims tokenClaims = tokenManager.getTokenClaims(accessToken);
		String tokenType = tokenClaims.getSubject();
		if (!TokenType.isAccessToken(tokenType)) {
			throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
		}

		// 3. refresh token 만료 처리
		Long memberId = Long.valueOf((Integer)tokenClaims.get("memberId"));
		Member member = memberService.findMemberByMemberId(memberId);
		member.expireRefreshToken(LocalDateTime.now());
	}
}
