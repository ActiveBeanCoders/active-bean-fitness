package com.activebeancoders.fitness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

/**
 * @author Dan Barrese
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@Controller
@EnableZuulProxy
public class GatewayApplication {

//    @RequestMapping("/user")
//    @ResponseBody
//    public Map<String, Object> user(Principal user) {
//        return Collections.<String, Object>singletonMap("name", user.getName());
//    }

//    @RequestMapping("/login")
//    public String login() {
//        return "forward:/";
//    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

//    @Configuration
//    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//        @Autowired
//        public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
//            // @formatter:off
//			auth.inMemoryAuthentication()
//				.withUser("user").password("password").roles("USER")
//			.and()
//				.withUser("admin").password("admin").roles("USER", "ADMIN", "READER", "WRITER")
//			.and()
//				.withUser("audit").password("audit").roles("USER", "ADMIN", "READER");
//            // @formatter:on
//        }
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            // @formatter:off
//			http
//				.httpBasic()
//			.and()
//				.logout()
//			.and()
//				.authorizeRequests()
//					.antMatchers("/index.html", "/login", "/").permitAll()
//					.anyRequest().authenticated()
//			.and()
//				.csrf().csrfTokenRepository(csrfTokenRepository())
//			.and()
//                .addFilterBefore(new AlwaysFilter(), CsrfFilter.class)
//				.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
//			// @formatter:on
//        }
//
//        private Filter csrfHeaderFilter() {
//            return new OncePerRequestFilter() {
//                @Override
//                protected void doFilterInternal(HttpServletRequest request,
//                                                HttpServletResponse response, FilterChain filterChain)
//                        throws ServletException, IOException {
//                    CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//                    System.out.println(String.format("@csrf=%s", csrf));
//                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
//                    System.out.println(String.format("@cookie=%s", cookie == null ? null : cookie.getValue()));
//                    if (csrf != null) {
//                        String token = csrf.getToken();
//                        System.out.println(String.format("@token=%s", token));
//                        if (cookie == null || token != null && !token.equals(cookie.getValue())) {
//                            cookie = new Cookie("XSRF-TOKEN", token);
//                            cookie.setPath("/"); // TODO: set actual path
//                            System.out.println("@ADDING COOKIE TO RESPONSE");
//                            response.addCookie(cookie);
//                        }
//                    }
//                    filterChain.doFilter(request, response);
//                }
//            };
//        }
//
//        private CsrfTokenRepository csrfTokenRepository() {
//            HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//            repository.setHeaderName("X-XSRF-TOKEN");
//            return repository;
//        }
//    }

}
