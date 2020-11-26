package com.longyue.springboot_shiro_ehcache.auth.token;

import org.apache.shiro.authc.AuthenticationToken;

public class UserToken implements AuthenticationToken {

    private String token;

    public UserToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
