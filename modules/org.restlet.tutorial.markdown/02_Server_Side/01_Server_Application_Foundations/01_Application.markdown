## Implementing application foundations ##

In this section, we will implement foundations of your Restlet server applications.

### Application class ###

The central class of all Restlet applications is the class that extends the Application one from
Restlet what the chosen deployment (standalone, servlet container and so). This class mainly
defines the way to access provided resources and acts as an entry point.



public class TestAppApplication extends Application {
    public Restlet createInboundRoot() {
        (...)
    }
}

### Configuring and organizing routing ###

Routing configuration is done within the createInboundRoot method of your Restlet application class
and mainly defined using the Router class. Following code describes a simple use case of routing
configuration:

    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("login", LoginServerResource.class);
        router.attach("logout", LogoutServerResource.class);
        router.attach("myEntities/", NewsListServerResource.class);
        router.attach("myEntities/{id}", NewsServerResource.class);

        (...)

        return router;
    }

The sample above corresponds to a very simple use case. In more real and large applications, you have
to define a more structing. As a matter of fact, the processing chain can be different according to
resources. Let's take a concrete sample. You probably want to have your resources secured and it's
not perhaps the case for static content.

For this particularly use case, a good practice consists in leveraging the matching mode of Restlet
when attaching resources on routers. This allows defining inner routes for a particular paths. For
example, all routes that starts with a pattern will match and handle by the sub route.

An important point to keep in mind is that matching is done sequentially and if a route matches,
remaining routes won't be tested.

Following code describes how to define inner routes and to organize routing within your Restlet
application:

    public Restlet createInboundRoot() {
        this.configuration = configureFreeMarker(getContext());

        Router router = new Router(getContext());

        router.attach("/static", createStaticApplication())
                .setMatchingMode(Template.MODE_STARTS_WITH);

        router.attach("/", createGlobalApplication())
                .setMatchingMode(Template.MODE_STARTS_WITH);

        return router;
    }


Here are the skeletons of the createStaticApplication and createGlobalApplication methods:

    private Restlet createStaticApplication() {
        Router router = new Router(getContext());
        (...)
        return router;
    }

    private Restlet createGlobalApplication() {
        Router router = new Router(getContext());
        (...)
        return router;
    }

You can notice that these classes haven't necessary to return a Router instance. An authenticator, a
filter and so on are also possible.

### Configuring template engines ###

You can choose to use template engines, like Freemarker or Velocity, to produce your representation
content. This approach is particularly suitable when your representations are HTML pages.

In the case of Freemarker, you have to initialize the template configuration that mainly configures
the place to find out template files. This initialization has to be specified within the application
class since it must be executed once. You can access then from the application class within
resource classes.

Following code shows a method that initializes a Configuration object stores as class variable within
the application class:

    private Configuration configuration;

    public static Configuration configureFreeMarker(Context context) {
        Configuration configuration = new Configuration();
        ClassTemplateLoader loader = new ClassTemplateLoader(
                TestAppApplication.class,
                "/org/restlet/tutorial/server/templates/");
        configuration.setTemplateLoader(loader);
        // configuration.setCacheStorage(new StrongCacheStorage());
        return configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

You can notice that the configureFreeMarker method must be called from the createInboundRoot method
when the application is actually initialized, as described below:

    public Restlet createInboundRoot() {
        this.configuration = configureFreeMarker(getContext());
        (...)
    }

### Configuring static content serving ###

Restlet allows easily serving static content using the Directory class. An instance of this class
can be attached to a router for a specific path. The directory can serve all files contained in
a specific folder with any folder depth. You can define this folder using supported protocols of
Restlet like file and clap.

In the following code, you use a folder contained in our Restlet application from classpath using
the CLAP protocol:

    Router router = new Router(getContext());
    (...)

    Directory directory = new Directory(getContext(),
                "clap://application/org/restlet/tutorial/server/static/");
    directory.setDeeplyAccessible(true);
    router.attach("/static", directory);

You must be sure here to have registered the CLAP protocol. See the deployment sections to see how
to register client protocols azccording to your target environment. You can also see the troubleshooting
section if you have problems when using a client protocol.

You can notice that you can also reference static folder with an absolute path using the file protocol
of Restlet. This approach isn't convenient for deployment that uses archive with content like Web archive
(WAR) and OSGi bundle.

### Linking with application layers ###

TODO: 

public class TestAppApplication extends Application {
    (...)
    private MyEntityDao myEntityDao;
    (...)

    public TestAppApplication() {
        myEntityDao = new MyEntityDaoImpl();
        (...)
    }

    (...)

    public MyEntityDao getMyEntityDao() {
        return myEntityDao;
    }

}


