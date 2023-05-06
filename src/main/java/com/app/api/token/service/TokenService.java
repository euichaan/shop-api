package com.app.api.token.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.api.token.dto.AccessTokenResponseDto;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.global.jwt.constant.GrantType;
import com.app.global.jwt.service.TokenManager;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

	private final MemberService memberService;
	private final TokenManager tokenManager;

	public AccessTokenResponseDto createAccessTokenByRefreshToken(final String refreshToken) {
		Member member = memberService.findMemberByRefreshToken(refreshToken);

		Date accessTokenExpireTime = tokenManager.createAccessTokenExpireTime();
		String accessToken = tokenManager.createAccessToken(member.getMemberId(), member.getRole(), accessTokenExpireTime);

		return AccessTokenResponseDto.builder()
			.grantType(GrantType.BEARER.getType())
			.accessToken(accessToken)
			.accessTokenExpireTime(accessTokenExpireTime)
			.build();
	}
}
