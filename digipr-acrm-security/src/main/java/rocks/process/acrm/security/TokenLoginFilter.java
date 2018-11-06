/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rocks.process.acrm.data.domain.Agent;
import rocks.process.acrm.security.token.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private Agent agent = null;

    public TokenLoginFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {

        try {
            this.agent = new ObjectMapper().readValue(request.getInputStream(), Agent.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        agent.getEmail(),
                        agent.getPassword())
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        Date date = null;
        Cookie cookie = null;

        if(Boolean.parseBoolean(this.agent.getRemember())) {
            date = new Date(System.currentTimeMillis() + SecurityConstants.REMEMBER_EXPIRATION_TIME);
        }else{
            date = new Date(System.currentTimeMillis() + SecurityConstants.SESSION_EXPIRATION_TIME);
        }
        String cookieToken = this.tokenService.issueToken(this.agent.getEmail(), SecurityConstants.COOKIE_TYPE, date);
        cookie = new Cookie(SecurityConstants.COOKIE_NAME, cookieToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        if(Boolean.parseBoolean(this.agent.getRemember())) {
            cookie.setMaxAge(Math.toIntExact(SecurityConstants.REMEMBER_EXPIRATION_TIME /1000));
        }
        response.addCookie(cookie);

        date = new Date(System.currentTimeMillis() + SecurityConstants.BEARER_EXPIRATION_TIME);
        String bearerToken = this.tokenService.issueToken(this.agent.getEmail(), SecurityConstants.BEARER_TYPE, date);
        response.addHeader(SecurityConstants.HEADER_NAME, SecurityConstants.BEARER_TOKEN_PREFIX + bearerToken);
    }
}
