package priv.yue.auth.core.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SessionFilter extends DefaultFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request, response, mappedValue);
    }
}
