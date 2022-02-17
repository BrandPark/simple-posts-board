package com.brandpark.simplepostsboard.infra.config;

import com.brandpark.simplepostsboard.infra.auth.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtRequestFilter jwtRequestFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .mvcMatchers("/", "/accounts/sign-up").permitAll()
                .mvcMatchers(HttpMethod.GET, "/posts/create").authenticated()
                .mvcMatchers(HttpMethod.GET, "/posts/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/accounts/*/blocks").authenticated()
                .mvcMatchers(HttpMethod.GET, "/api/v1/blocks").authenticated()
                .mvcMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/authenticate").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().logoutSuccessUrl("/").deleteCookies("JSESSIONID");

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

        http.csrf().ignoringAntMatchers("/api/v1/**", "/authenticate");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/bootstrap/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * @Component 로 등록된 JwtRequestFilter 가 일반 서블릿 필터로 중복 등록되는것을 방지
     */
    @Bean
    public FilterRegistrationBean registrationBean(JwtRequestFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        registrationBean.addUrlPatterns("/api/v1/*");
        return registrationBean;
    }
}
