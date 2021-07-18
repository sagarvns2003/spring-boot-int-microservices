/**
 * 
 */
package com.vidya.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author Vidya
 *
 */
public class TokenAuthenticationFilter extends GenericFilterBean {

	public static final String HEADER_X_AUTH_TOKEN = "X-AUTH-TOKEN";

	private final String authToken;

	/**
	 * @param authToken
	 */
	public TokenAuthenticationFilter(String authToken) {
		super();
		this.authToken = authToken;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

		String userToken = httpServletRequest.getHeader(HEADER_X_AUTH_TOKEN);
		if (StringUtils.equals(userToken, this.authToken)) {
			PreAuthenticatedAuthenticationToken authenticatedAuthenticationToken = new PreAuthenticatedAuthenticationToken(
					"aPrincipal", null);
			authenticatedAuthenticationToken.setAuthenticated(Boolean.TRUE);
			SecurityContextHolder.getContext().setAuthentication(authenticatedAuthenticationToken);

			httpServletResponse.setStatus(HttpStatus.OK.value());
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} else {
			System.out.println("Request forbidden, Invalid authentication token !!!");
			httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
		}
	}
}