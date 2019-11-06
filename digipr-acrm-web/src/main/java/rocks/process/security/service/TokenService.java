/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import rocks.process.security.config.TokenSecurityProperties;
import rocks.process.security.model.Token;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    private JpaRepository<Token, String> tokenBlacklistRepository;
    private UserDetailsService userDetailsService;
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private Key signingKey;

    @Autowired
    public TokenService(JpaRepository<Token, String> tokenBlacklistRepository, UserDetailsService userDetailsServiceImpl, TokenSecurityProperties tokenSecurityProperties) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
        this.userDetailsService = userDetailsServiceImpl;
        this.signingKey = new SecretKeySpec(tokenSecurityProperties.getSecret().getBytes(), SIGNATURE_ALGORITHM.getJcaName());
    }

    public String issueToken(String subject, String type, Date expirationTime){
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expirationTime)
                .signWith(SIGNATURE_ALGORITHM, signingKey)
                .setHeaderParam("typ", type)
                .compact();
    }

    public String verifyAndGetSubject(String token, String type){
        if(this.isTokenBlacklisted(token)){
            return null;
        }

        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token);

        if(claims!=null){
            if (type!=null && !((String) claims.getHeader().get("typ")).equals(type))
            {
                return null;
            }
            String username = claims.getBody().getSubject();
            if(this.userDetailsService.loadUserByUsername(username)!=null){
                return username;
            }
        }
        return null;
    }

    private boolean isTokenBlacklisted(String token){
        return tokenBlacklistRepository.existsById(token);
    }

    public void blacklistToken(String token){
        tokenBlacklistRepository.save(new Token(token));
    }

}
