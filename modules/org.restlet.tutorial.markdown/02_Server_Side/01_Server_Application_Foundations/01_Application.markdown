## Implementing application foundations ##

In this section, we will implement foundations of your Restlet server applications.

### Application class ###

TODO: 

public class TestAppApplication extends Application {
    public Restlet createInboundRoot() {
        (...)
    }
}

### Configuring and organizing routing ###

TODO: 

    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("login", LoginServerResource.class);
        router.attach("logout", LogoutServerResource.class);
        router.attach("myEntities/", NewsListServerResource.class);
        router.attach("myEntities/{id}", NewsServerResource.class);

        (...)

        return router;
    }

TODO: describe nested routing
TODO: concrete example (securing all except static content)

    public Restlet createInboundRoot() {
        this.configuration = configureFreeMarker(getContext());

        Router router = new Router(getContext());

        router.attach("/static", createStaticApplication())
                .setMatchingMode(Template.MODE_STARTS_WITH);

        router.attach("/", createGlobalApplication())
                .setMatchingMode(Template.MODE_STARTS_WITH);

        return router;
    }


### Configuring template engines ###

TODO:

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

TODO:

    public Restlet createInboundRoot() {
        this.configuration = configureFreeMarker(getContext());
        (...)
    }

### Configuring static content serving ###

TODO:

    Router router = new Router(getContext());
    (...)

    Directory directory = new Directory(getContext(),
                "clap://application/org/restlet/tutorial/server/static/");
    directory.setDeeplyAccessible(true);
    router.attach("/static", directory);

TODO: link to client connector / how to fix client connector error

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


