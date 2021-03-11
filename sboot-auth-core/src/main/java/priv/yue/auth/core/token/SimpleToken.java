package priv.yue.auth.core.token;

import org.apache.shiro.authc.AuthenticationToken;


public class SimpleToken implements AuthenticationToken {

    private String token;

    public SimpleToken(String token) { this.token = token; }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
