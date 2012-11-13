## Troubleshooting ##

This section describes how to solve common client problems with Restlet.

### I have problems when trying to access resources hosted on GAE ###

**Problem**

When using the Client or ClientResource API to access remote resources hosted on GAE, the following
exception occurs on server side:

    org.restlet.resource.ServerResource doCatch: Exception or error caught in server resource
    Internal Server Error (500) - The server encountered an unexpected condition which prevented it from fulfilling the request
     at org.restlet.resource.ServerResource.doHandle(ServerResource.java:508)
     at org.restlet.resource.ServerResource.put(ServerResource.java:1225)
     at org.restlet.resource.ServerResource.doHandle(ServerResource.java:574)
     at org.restlet.resource.ServerResource.doNegotiatedHandle(ServerResource.java:640)
     at org.restlet.resource.ServerResource.doConditionalHandle(ServerResource.java:339)
     at org.restlet.resource.ServerResource.handle(ServerResource.java:902)
     at org.restlet.resource.Finder.handle(Finder.java:246)
     at org.restlet.routing.Filter.doHandle(Filter.java:159)
     at org.restlet.routing.Filter.handle(Filter.java:206)

**Solution**

Some platforms like GAE don't support chunked encoding for requests, which is by default enabled
in Restlet. To make requests work, you need to disable chunked encoding using the `setRequestEntityBuffering`
method of client resource, as described below:

    String url = (...)
    ClientResource clientResource = new ClientResource(url);
    clientResource.setRequestEntityBuffering(true);
    clientResource.get();


