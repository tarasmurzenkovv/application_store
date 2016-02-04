package ua.dataart.configuration.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAuthorizationPoint extends LoginUrlAuthenticationEntryPoint {

    private final static String LOGIN_FORM_URL = "/ajax_login";

    public AjaxAuthorizationPoint() {
        super(LOGIN_FORM_URL);
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print("Invalid login/password");
        response.getWriter().flush();
    }
}
