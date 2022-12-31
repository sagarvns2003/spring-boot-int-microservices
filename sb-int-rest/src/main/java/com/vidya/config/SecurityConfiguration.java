package com.vidya.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.vidya.config.filter.TokenAuthenticationFilter;

/**
 * @author Vidya
 */
@Configuration
public class SecurityConfiguration {

	@Value("${auth.ignore.path}")
	private String[] authIgnorePath;

	@Value("${auth.token}")
	private String authToken;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.debug(false).ignoring().requestMatchers(this.authIgnorePath);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(this.authToken);
		http.csrf().disable().addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class);
		return http.build();
	}
}