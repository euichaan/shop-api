package com.app.global.config.web;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.app.global.interceptor.AdminAuthorizationInterceptor;
import com.app.global.interceptor.AuthenticationInterceptor;
import com.app.global.resolver.memberinfo.MemberInfoArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthenticationInterceptor authenticationInterceptor;
	private final MemberInfoArgumentResolver memberInfoArgumentResolver;
	private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;
	private final ObjectMapper objectMapper;

	@Override
	public void addCorsMappings(final CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*") // 모든 Origin 허용
			.allowedMethods(
				HttpMethod.GET.name(),
				HttpMethod.POST.name(),
				HttpMethod.PUT.name(),
				HttpMethod.PATCH.name(),
				HttpMethod.DELETE.name(),
				HttpMethod.OPTIONS.name()
			);
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor)
			.order(1)
			.addPathPatterns("/api/**")
			.excludePathPatterns("/api/oauth/login",
				"/api/access-token/issue",
				"api/logout",
				"/api/health");
		registry.addInterceptor(adminAuthorizationInterceptor)
			.order(2)
			.addPathPatterns("/api/admin/**");
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(memberInfoArgumentResolver);
	}

	@Bean
	public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean() {
		FilterRegistrationBean<XssEscapeServletFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(new XssEscapeServletFilter());
		filterRegistration.setOrder(1);
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}

	@Override
	public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
		converters.add(jsonEscapeConverter());
	}

	@Bean
	public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
		ObjectMapper copy = objectMapper.copy();
		copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
		return new MappingJackson2HttpMessageConverter(copy);
	}
}
