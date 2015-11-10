package com.activebeancoders.fitness;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.activebeancoders.fitness.gateway.filter.AlwaysFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

/**
 * @author Dan Barrese
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@Controller
@EnableRedisHttpSession
@EnableZuulProxy
public class GatewayApplication {

    @RequestMapping("/user")
    @ResponseBody
    public Map<String, Object> user(Principal user) {
        return Collections.<String, Object>singletonMap("name", user.getName());
    }

    @RequestMapping("/login")
    public String login() {
        return "forward:/";
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
            // @formatter:off
			auth.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER")
			.and()
				.withUser("admin").password("admin").roles("USER", "ADMIN", "READER", "WRITER")
			.and()
				.withUser("audit").password("audit").roles("USER", "ADMIN", "READER");
            // @formatter:on
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
			http
				.httpBasic()
			.and()
				.logout()
			.and()
				.authorizeRequests()
					.antMatchers("/index.html", "/login", "/").permitAll()
					.anyRequest().authenticated()
			.and()
				.csrf().csrfTokenRepository(csrfTokenRepository())
			.and()
                .addFilterBefore(new AlwaysFilter(), CsrfFilter.class)
				.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
			// @formatter:on
        }

        private Filter csrfHeaderFilter() {
            return new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request,
                                                HttpServletResponse response, FilterChain filterChain)
                        throws ServletException, IOException {
                    CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                    System.out.println(String.format("@csrf=%s", csrf));
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    System.out.println(String.format("@cookie=%s", cookie == null ? null : cookie.getValue()));
                    if (csrf != null) {
                        String token = csrf.getToken();
                        System.out.println(String.format("@token=%s", token));
                        if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                            cookie = new Cookie("XSRF-TOKEN", token);
                            cookie.setPath("/"); // TODO: set actual path
                            System.out.println("@ADDING COOKIE TO RESPONSE");
                            response.addCookie(cookie);
                        }
                    }
                    filterChain.doFilter(request, response);
                }
            };
        }

        private CsrfTokenRepository csrfTokenRepository() {
            HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
            repository.setHeaderName("X-XSRF-TOKEN");
            return repository;
        }
    }

}
