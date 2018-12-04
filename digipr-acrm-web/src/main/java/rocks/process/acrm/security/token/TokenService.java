/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rocks.process.acrm.security.user.UserDetailsServiceImpl;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    private TokenBlacklistRepository tokenBlacklistRepository;
    private UserDetailsServiceImpl userDetailsService;
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private Key signingKey;

    @Autowired
    public TokenService(TokenBlacklistRepository tokenBlacklistRepository, UserDetailsServiceImpl userDetailsService, @Value("${token.secret}") String secret) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
        this.userDetailsService = userDetailsService;
        this.signingKey = new SecretKeySpec(secret.getBytes(), SIGNATURE_ALGORITHM.getJcaName());
    }

    public String issueToken(String subject, String type, Date expirationTime){
        return Jwts.builder()
                .setSubject(subject)
                .claim("type", type)
                .setExpiration(expirationTime)
                .signWith(SIGNATURE_ALGORITHM, signingKey)
                .compact();
    }

    public String verifyAndGetSubject(String token, String type){
        if(this.isTokenBlacklisted(token)){
            return null;
        }

        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();

        if(claims!=null){
            if (type!=null && !((String) claims.get("type")).equals(type))
            {
                return null;
            }
            String username = claims.getSubject();
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
