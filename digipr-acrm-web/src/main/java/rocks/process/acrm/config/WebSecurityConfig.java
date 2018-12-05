/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import rocks.process.acrm.security.CSRFRequestMatcher;
import rocks.process.acrm.security.TokenAuthenticationFilter;
import rocks.process.acrm.security.TokenLoginFilter;
import rocks.process.acrm.security.TokenLogoutHandler;
import rocks.process.acrm.security.token.TokenService;
import rocks.process.acrm.security.user.UserDetailsServiceImpl;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
            .requiresChannel().requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null).requiresSecure().and() // If the X-Forwarded-Proto header is present, redirect to HTTPS (Heroku)
            .csrf()
                .requireCsrfProtectionMatcher(new CSRFRequestMatcher())
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
            .authorizeRequests()
                .antMatchers("/", "/assets/**", "/user/**", "/login/**").permitAll()
                .antMatchers(HttpMethod.GET, "/logout").permitAll()
                .anyRequest().authenticated().and()
                    .addFilter(new TokenLoginFilter(authenticationManager(),this.tokenService))
                    .addFilter(new TokenAuthenticationFilter(authenticationManager(), this.tokenService))
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .addLogoutHandler(new TokenLogoutHandler(this.tokenService));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}