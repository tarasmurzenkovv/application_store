package ua.dataart.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityApplicationContextConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImplementation userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AjaxAuthorizationPoint ajaxAuthorizationPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.sessionManagement().disable();

        http.authorizeRequests()
                .antMatchers(
                        "/ajax_login","/pages/index.html", "/pages/registration.html",
                        "/pages/**", "/resources/css/**", "/resources/img/**",
                        "/resources/js/**","/img/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/register").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/customer_types").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/ajax_login").permitAll();

        http.httpBasic().authenticationEntryPoint(ajaxAuthorizationPoint);

        http.authorizeRequests().anyRequest().permitAll();
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/pages/index.html");

        http.exceptionHandling().accessDeniedHandler(new AjaxAccessDeniedHandler());
    }
}
