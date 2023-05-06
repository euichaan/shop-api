package com.app.api.login.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.api.login.dto.OauthLoginDto;
import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.external.oauth.model.OauthAttributes;
import com.app.external.oauth.service.SocialLoginApiService;
import com.app.external.oauth.service.SocialLoginApiServiceFactory;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.jwt.service.TokenManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

	private final MemberService memberService;
	private final TokenManager tokenManager;

	public OauthLoginDto.Response oauthLogin(String accessToken, MemberType memberType) {
		SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(memberType);
		OauthAttributes userInfo = socialLoginApiService.getUserInfo(accessToken);
		log.info("userInfo : {}", userInfo);

		JwtTokenDto jwtTokenDto;
		Optional<Member> optionalMember = memberService.findMemberByEmail(userInfo.getEmail());
		if (optionalMember.isEmpty()) { // 신규 회원 가입
			Member oauthMember = userInfo.toMemberEntity(memberType, Role.ADMIN);
			oauthMember = memberService.registerMember(oauthMember);

			// 토큰 생성
			jwtTokenDto = tokenManager.createJwtTokenDto(oauthMember.getMemberId(), oauthMember.getRole());
			oauthMember.updateRefreshToken(jwtTokenDto);

		} else { // 기존 회원일 경우
			Member oauthMember = optionalMember.get();

			// 토큰 생성
			jwtTokenDto = tokenManager.createJwtTokenDto(oauthMember.getMemberId(), oauthMember.getRole());
			oauthMember.updateRefreshToken(jwtTokenDto);
		}
		return OauthLoginDto.Response.of(jwtTokenDto);
	}
}
