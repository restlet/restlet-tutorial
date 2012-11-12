## Deploying applications as standalone server ##

Now you have a Restlet applications implemented based on previous described use cases:

* Implementing a simple Web API
* Implementing a Web application

it's time to see deploy and execute them within a server. Restlet provides several approaches
for this issue and we will focus here on the one based on a standalone server.

### Choosing the underlying server ###

Restlet integrates a smart autodetect processing of available extensions. You simply have to put
them in the classpath to make them available. This also applies for server connectors usable
for a standalone Restlet server:

* Internal NIO server
* Simple framework

By default, meaning no additional extension provided a server connector is available, the Restlet
internal NIO server is used.

If you put the simple extension, the Simple Framework server will automatically use as underlying
HTTP server. In this case, you will see messages like that in the traces:

    1 nov. 2012 11:35:05 org.restlet.ext.simple.SimpleServerHelper start
    INFO: Starting the Simple [HTTP/1.1] server on port 8182

### Creating the launch processing ###

There are two approaches when implementing the launch processing:

* At application level
* At component level

The first one is the simplest but the most restricted. It simply allows attaching a Restlet application
to a server to execute it. But you can't set more configurations here. Following code describes how
to implemente this approach:

    Server server = new Server(Protocol.HTTP, 8182);
    server.setNext(new MyApplication());

The second on is the recommended one since it provides flexility when configuring application configuration
for execution. Following lists gives a non-exhaustive list of supported elements for configuration:

* Server connectors
* Client connectors
* Virtual hosts
* Internal routing
* Restlet services

You first need to create a component and eventually set some hints about it as described below:

    Component component = new Component();
    component.setName("My component");
    component.setDescription("My component description");
    component.setOwner("Restlet");
    component.setAuthor("The Restlet Team");

The second important point to configure now is connectors for both server(s) and client(s). Since we want
to make available a server application, a server protocol is mandatory, an HTTP one in our case. If you
want to access other resources using Restlet, you need to add client protocols like `FILE` or `HTTP`. Following
code describes how to configure such aspects:

    getServers().add(Protocol.HTTP, 8182);
    getClients().add(Protocol.FILE);
    getClients().add(Protocol.HTTP);
    getClients().add(Protocol.HTTPS);

The final thing to do to configure our component is to define how to make available our application
through the component. Several possibilities are supported within Restlet:

* Using the default host:

        component.getDefaultHost().attach("/app", new MyApplication());

* Using the internal router to only make available the application within a same process using the RIAP protocol:

        component.getInternalRouter().attach("/app", new MyApplication());

* Using a virtual host to precisely control the domain to access the application:

        VirtualHost virtualHost = new VirtualHost();
        virtualHost.setHostDomain("www.myapp.org");
        virtualHost.setHostPort("80|8182");
        component.getHosts().add(virtualHost);

Let's finally see how startup and shutdown you server.

### Launching the server ###

For both approaches, `start` and `stop` methods are available to start and stop the server

    // Application level approach    
    Server server = (...)
    server.start();

    // Component level approach
    Component component = (...)
    component.start();

You can notice that the `start` method isn't blocking so you can consider using an stdin
read line to detect when stopping the server, as described below:

    (...)
    System.out.println("Press a key to stop");
    System.in.read();

    // Application level approach    
    if (server!=null) {
        server.stop();
    }
    // Component level approach
    if (component!=null) {
        component.stop();
    }

