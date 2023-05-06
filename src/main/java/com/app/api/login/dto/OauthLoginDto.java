package com.app.api.login.dto;

import java.util.Date;

import com.app.global.jwt.dto.JwtTokenDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OauthLoginDto {

	@Getter @Setter
	public static class Request {

		@Schema(description = "소셜 로그인 회원 타입", example = "KAKAO", required = true)
		private String memberType;
	}

	@Getter @Setter
	@Builder @NoArgsConstructor @AllArgsConstructor
	public static class Response {

		@Schema(description = "grantType", example = "Bearer", required = true)
		private String grantType;

		@Schema(description = "accessToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2ODMzNDQzNTEsImV4cCI6MTY4MzM0NTI1MSwibWVtYmVySWQiOjEsInJvbGUiOiJBRE1JTiJ9.38ohc5ZQnHWcF7De9qhQS5dXNCmJDkeJJDwiiLc2M1aJpDKQP4775vTSyeSwpXIrMSw6s70E8Qv4xOKJPjPLgw", required = true)
		private String accessToken;

		@Schema(description = "access Token 만료시간", example = "2023-05-06 12:54:11", required = true)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
		private Date accessTokenExpireTime;

		@Schema(description = "refreshToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjgzMzQ0MzUxLCJleHAiOjE2ODQ1NTM5NTEsIm1lbWJlcklkIjoxfQ.7xTwwRIXRGS3lW6_3Nak9DylzedJt6ivwbtSMzoK2WOCvIA4PzjyoY-dZh8PlcEqqJ9wNQHhY0LE1Tqs97txyA", required = true)
		private String refreshToken;

		@Schema(description = "refresh Token 만료시간", example = "2023-05-20 12:39:11", required = true)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
		private Date refreshTokenExpireTime;

		public static Response of(JwtTokenDto jwtTokenDto) {
			return Response.builder()
				.grantType(jwtTokenDto.getGrantType())
				.accessToken(jwtTokenDto.getAccessToken())
				.accessTokenExpireTime(jwtTokenDto.getAccessTokenExpireTime())
				.refreshToken(jwtTokenDto.getRefreshToken())
				.refreshTokenExpireTime(jwtTokenDto.getRefreshTokenExpireTime())
				.build();
		}
	}
}
