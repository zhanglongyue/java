package priv.yue.sboot.auth.token;

import org.apache.shiro.authc.AuthenticationToken;

import java.util.concurrent.TimeUnit;


public class DefaultToken implements AuthenticationToken {

    private String token;

    public DefaultToken(String token) { this.token = token; }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
