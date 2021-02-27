package priv.yue.sboot.token;

import org.apache.shiro.authc.AuthenticationToken;


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
