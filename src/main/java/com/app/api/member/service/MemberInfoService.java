package com.app.api.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.api.member.dto.MemberInfoResponseDto;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

	private final MemberService memberService;

	@Transactional(readOnly = true)
	public MemberInfoResponseDto getMemberInfo(final Long memberId) {
		Member member = memberService.findMemberByMemberId(memberId);
		return MemberInfoResponseDto.of(member);
	}
}
