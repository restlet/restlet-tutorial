# Implementing resources #

Server resources are the elements that provide processing related to paths.

## A simple server resource ##

Restlet provides the ServerResource class to implement server resource. A server resource is simply a sub-class of the ServerResource class.
One specificity in Restlet is that a server resource instance is created for each REST request. So you can define class variables in it but
they will be available for the request.

Following code describes a simple custom server resource:

```java
public class MyServerResource extends ServerResource {
    (...)
}
```

The MyServerResource class can be attached to a path for the application, as described in the "Implementing application foundations" section.

## Adding handling methods ##

We can add now methods to handle HTTP methods for the path(s) the server resource is attached with. Restlet uses annotations for that, as listed
below:

* Get: the associated method handles an HTTP GET method
* Put: the associated method handles an HTTP PUT method
* Post: the associated method handles an HTTP POST method
* Delete: the associated method handles an HTTP DELETE method
* Head: the associated method handles an HTTP HEAD method

The following code describes how to add annotated methods to a server resource class:

```java
public class MyServerResource extends ServerResource {
    @Get
    public Representation handleGetMethod() {
        (...)
    }

    @Put
    public Representation handlePutMethod() {
        (...)
    }

    @Delete
    public void handleDeleteMethod() {
        (...)
    }
}
```

You can notice that the method signature defers according to the associated annotation. For example, we expect a GET method to send back
a representation, a PUT one to receive and send back representations, a DELETE one to exchange no representation.

## Using server resource request methods ##

When attaching server resources for a particular path, you can commonly define attributes. Accessing the request attributes
Accessing the request query

## Using server resource response methods ##

Handling redirects
