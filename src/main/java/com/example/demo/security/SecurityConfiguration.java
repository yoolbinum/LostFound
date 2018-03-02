package com.example.demo.security;

import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SSUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    private static final String[] PUBLIC_MATCHERS = {
            "/register",
            "/h2-console/**",
            "/**",
            "/items/lost",
            "/items/found"
    };

    private static final String[] USER_MATCHERS = {
    };

    private static final String[] ADMIN_MATCHERS = {
    };

    private static final String[] USER_ADMIN_MATCHERS = {
            "/items/add"
    };


    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception{
        return new SSUserDetailsService(userRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                    .antMatchers(PUBLIC_MATCHERS).permitAll()
                    .antMatchers(USER_ADMIN_MATCHERS).hasAnyAuthority("ADMIN", "USER")
                        .anyRequest().authenticated()
                        .and()
                    .formLogin()
                        .loginPage("/login").permitAll()
                        .and()
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .and()
                    .httpBasic();
        http
                .csrf().disable();
        http
                .headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(userDetailsServiceBean());
    }
}
