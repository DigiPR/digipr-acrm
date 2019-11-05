/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="security.token")
public class TokenSecurityProperties {
    private static String SECRET = "";
    public static String COOKIE_TYPE = "cookie";
    public static String BEARER_TYPE = "bearer";
    public static long REMEMBER_EXPIRATION_TIME =  864_000_000; // 10 days
    public static long SESSION_EXPIRATION_TIME =  86_400_000; // 1 day
    public static long BEARER_EXPIRATION_TIME =  864_000_000; // 10 days
    public static String BEARER_TOKEN_PREFIX = "Bearer ";
    public static String HEADER_NAME = "Authorization";
    public static String COOKIE_NAME = "AUTHORISATION";

    public String getSecret() {
        return SECRET;
    }

    public void setSecret(String secret) {
        SECRET = secret;
    }

    public String getCookieType() {
        return COOKIE_TYPE;
    }

    public void setCookieType(String cookieType) {
        COOKIE_TYPE = cookieType;
    }

    public String getBearerType() {
        return BEARER_TYPE;
    }

    public void setBearerType(String bearerType) {
        BEARER_TYPE = bearerType;
    }

    public long getRememberExpirationTime() {
        return REMEMBER_EXPIRATION_TIME;
    }

    public void setRememberExpirationTime(long rememberExpirationTime) {
        REMEMBER_EXPIRATION_TIME = rememberExpirationTime;
    }

    public long getSessionExpirationTime() {
        return SESSION_EXPIRATION_TIME;
    }

    public void setSessionExpirationTime(long sessionExpirationTime) {
        SESSION_EXPIRATION_TIME = sessionExpirationTime;
    }

    public long getBearerExpirationTime() {
        return BEARER_EXPIRATION_TIME;
    }

    public void setBearerExpirationTime(long bearerExpirationTime) {
        BEARER_EXPIRATION_TIME = bearerExpirationTime;
    }

    public String getBearerTokenPrefix() {
        return BEARER_TOKEN_PREFIX;
    }

    public void setBearerTokenPrefix(String bearerTokenPrefix) {
        BEARER_TOKEN_PREFIX = bearerTokenPrefix;
    }

    public String getHeaderName() {
        return HEADER_NAME;
    }

    public void setHeaderName(String headerName) {
        HEADER_NAME = headerName;
    }

    public String getCookieName() {
        return COOKIE_NAME;
    }

    public void setCookieName(String cookieName) {
        COOKIE_NAME = cookieName;
    }
}