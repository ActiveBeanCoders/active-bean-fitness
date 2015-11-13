security-service
================

TODO
====
* persist username passwords for log-in.
* Store session data in Redis db instead of the application heap.
* Add XSRF-TOKEN as cookie to response, for example:
    From https://spring.io/guides/tutorials/spring-security-and-angular-js/
    public class CsrfHeaderFilter extends OncePerRequestFilter {
      @Override
      protected void doFilterInternal(HttpServletRequest request,
          HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
            .getName());
        if (csrf != null) {
          Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
          String token = csrf.getToken();
          if (cookie==null || token!=null && !token.equals(cookie.getValue())) {
            cookie = new Cookie("XSRF-TOKEN", token);
            cookie.setPath("/");
            response.addCookie(cookie);
          }
        }
        filterChain.doFilter(request, response);
      }
    }
    At least we are still using the session, which makes sense because Spring Security and the Servlet container know how to do that with no effort on our part. But couldn’t we have continued to use cookies to transport the authentication token? It would have been nice, but there is a reason it wouldn’t work, and that is that the browser wouldn’t let us. You can just go poking around in the browser’s cookie store from a JavaScript client, but there are some restrictions, and for good reason. In particular you don’t have access to the cookies that were sent by the server as "HttpOnly" (which you will see is the case by default for session cookies). You also can’t set cookies in outgoing requests, so we couldn’t set a "SESSION" cookie (which is the Spring Session default cookie name), we had to use a custom "X-Session" header. Both these restrictions are for your own protection so malicious scripts cannot access your resources without proper authorization.

Thoughts
========

* What if a new tab is opened?  How can the session be retrieved?  Store session as cookie?
* What happens to authentication objects in services?  For example, resource-service calls security-service and authenticates the user.  The user is then authenticated within resource-service by a call to "SecurityContextHolder.getContext().setAuthentication(...);"  How long does this last?  How is it evicted?
* What if I'm using a token and it expires?  Will it automatically be renewed by security-service?
* Is all the service gateway stuff necessary?

