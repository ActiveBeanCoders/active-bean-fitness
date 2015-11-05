security-service
================

Thoughts
========

* What if a new tab is opened?  How can the session be retrieved?  Store session as cookie?
* What happens to authentication objects in services?  For example, resource-service calls security-service and authenticates the user.  The user is then authenticated within resource-service by a call to "SecurityContextHolder.getContext().setAuthentication(...);"  How long does this last?  How is it evicted?
* If the service being called already has a user authenticated in the context, use it and don't bother calling out to security-service again.
* Will each service need a duplicate TokenService?  How will each service now when the user's session has expired?
* What if I'm using a token and it expires?  Will it automatically be renewed by security-service?
* Is all the service gateway stuff necessary?

