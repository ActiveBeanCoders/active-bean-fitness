# Active Bean Fitness App

## create issues for:
* document classes
* use constructor injection with @Autowired (and @Qualifier if needed).
* diagram all endpoints for common authentication flow
* allow http://localhost:8080/ui to work without trailing slash (see https://github.com/spring-cloud/spring-cloud-netflix/issues/454 and http://tuckey.org/urlrewrite/)
* restrict all traffic to HTTPS only
    * use secure cookies.
    * enable TomcatHttpsConfiguration @Component in security-api.
    * change all 'http' to 'https' in property files.
* utilize CSRF token on all non-GET requests.

