package com.vidya.config;

import com.vidya.config.filter.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author Vidya Sagar Gupta
 */
@Configuration
public class SecurityConfiguration {

  @Value("${auth.ignore.path}")
  private String[] authIgnorePath;

  @Value("${auth.token}")
  private String authToken;

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.debug(false).ignoring().requestMatchers(this.authIgnorePath);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        /*.csrf(
            csrfConfigurer ->
                csrfConfigurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))*/
        .addFilterBefore(
            new TokenAuthenticationFilter(this.authToken), BasicAuthenticationFilter.class);
    return http.build();
  }
}
