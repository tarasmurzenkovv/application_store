package ua.dataart.configuration.security;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;


@Order(1)
public class SecurityApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

}