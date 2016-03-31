package com.activebeancoders.fitness.security.domain;

import com.activebeancoders.fitness.security.infrastructure.UserSession;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that injects the DomainUser that invoked the method.  This works by
 * retrieving the {@link UserSession}
 * object from the ThreadLocal {@link org.springframework.security.core.context.SecurityContextHolder}.
 *  So the service in which this annotation is used must have a request filter that stores
 * that authentication information into the context, such as {@link
 * com.activebeancoders.fitness.security.infrastructure.SecuredServiceAuthenticationFilter}.
 *
 * @author Dan Barrese
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal
public @interface CurrentlyLoggedInUser {
}
