/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.util.WebUtils;
import rocks.process.acrm.security.token.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    private TokenService tokenService;

    public TokenAuthenticationFilter(AuthenticationManager authManager, TokenService tokenService) {
        super(authManager);
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstants.HEADER_NAME);

        if (header == null || !header.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)) {
            Cookie cookie = WebUtils.getCookie(request, SecurityConstants.COOKIE_NAME);
            if (cookie == null) {
                chain.doFilter(request, response);
                return;
            }
        }

        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (UsernameNotFoundException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                SignatureException | IllegalArgumentException e) {
            response.sendRedirect("/logout");
        }

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_NAME);
        String type = null;

        if (token == null) {
            Cookie cookie = WebUtils.getCookie(request, SecurityConstants.COOKIE_NAME);
            if (cookie != null) {
                token = cookie.getValue();
                type = SecurityConstants.COOKIE_TYPE;
            }
        } else {
            token = token.replace(SecurityConstants.BEARER_TOKEN_PREFIX, "");
            type = SecurityConstants.BEARER_TYPE;
        }

        if (token != null) {
            String user = this.tokenService.verifyAndGetSubject(token, type);
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
