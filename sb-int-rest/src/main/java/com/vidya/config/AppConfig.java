package com.vidya.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author Vidya
 */
@Configuration
public class AppConfig extends WebSecurityConfigurerAdapter {

	@Value("${auth.ignore.path}")
	private String[] authIgnorePath;

	@Value("${auth.token}")
	private String authToken;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(this.authIgnorePath);
		super.configure(web);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(this.authToken);

		http.addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class).csrf().disable();

		super.configure(http);
	}
}