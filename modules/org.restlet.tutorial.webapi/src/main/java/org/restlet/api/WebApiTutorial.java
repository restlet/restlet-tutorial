package org.restlet.api;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.api.resource.server.CompanyListServerResource;
import org.restlet.api.resource.server.CompanyServerResource;
import org.restlet.api.resource.server.ContactListServerResource;
import org.restlet.api.resource.server.ContactServerResource;
import org.restlet.api.resource.server.PingServerResource;
import org.restlet.data.ChallengeScheme;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MemoryRealm;
import org.restlet.security.Role;
import org.restlet.security.User;

public class WebApiTutorial extends Application {

    /*
     * Define role names
     */
    public static final String ROLE_ADMIN = "admin";

    public static final String ROLE_OWNER = "owner";

    public static final String ROLE_USER = "user";

    /*
     * Define route constants
     */
    public static final String ROUTE_COMPANIES = "/companies";

    public static final String ROUTE_CONTACTS = "/contacts";

    /*
     * Define SQL State code constants
     */
    public static final String SQL_STATE_23000_DUPLICATE = "23000";

    public WebApiTutorial() {
        setName("WebAPITutorial");
    }

    @Override
    public Restlet createInboundRoot() {

        /*
         * Create a base router that will link to unprotected ping to check
         * whether the api is alive or not.
         */
        Router baseRouter = new Router(getContext());
        baseRouter.setRoutingMode(Router.MODE_FIRST_MATCH);
        baseRouter.attach("/ping", PingServerResource.class);

        /*
         * Create the api router, protected by a guard
         */
        ChallengeAuthenticator apiGuard = createApiGuard();
        Router apiRouter = createApiRouter();
        apiGuard.setNext(apiRouter);

        baseRouter.attach(apiGuard);
        return baseRouter;
    }

    private ChallengeAuthenticator createApiGuard() {

        ChallengeAuthenticator apiGuard = new ChallengeAuthenticator(
                getContext(), ChallengeScheme.HTTP_BASIC, "realm");

        /*
         * Create in-memory users and roles.
         */
        MemoryRealm realm = new MemoryRealm();
        User owner = new User("owner", "owner");
        realm.getUsers().add(owner);
        realm.map(owner, Role.get(this, ROLE_OWNER));
        realm.map(owner, Role.get(this, ROLE_USER));
        User admin = new User("admin", "admin");
        realm.getUsers().add(admin);
        realm.map(admin, Role.get(this, ROLE_ADMIN));
        realm.map(admin, Role.get(this, ROLE_OWNER));
        realm.map(admin, Role.get(this, ROLE_USER));
        User user = new User("user", "user");
        realm.getUsers().add(user);
        realm.map(user, Role.get(this, ROLE_USER));

        /*
         * Attach - Verifier : to check authentication - Enroler : to check
         * authorization (roles)
         */
        apiGuard.setVerifier(realm.getVerifier());
        apiGuard.setEnroler(realm.getEnroler());

        /*
         * You can also create your own authentication/authorization system by
         * creating classes extending SecretVerifier or LocalVerifier (for
         * authentication) and Enroler (for authorization) and set these to the
         * guard.
         */

        return apiGuard;
    }

    private Router createApiRouter() {

        /*
         * Attach Server Resources to given URL. For instance,
         * CompanyListServerResource is attached to
         * http://localhost:9000/v1/companies/
         */
        Router apiRouter = new Router(getContext());
        apiRouter.attach(ROUTE_COMPANIES, CompanyListServerResource.class);
        apiRouter
                .attach(ROUTE_COMPANIES + "/", CompanyListServerResource.class);
        apiRouter
                .attach(ROUTE_COMPANIES + "/{id}",
                CompanyServerResource.class);
        apiRouter.attach(ROUTE_CONTACTS, ContactListServerResource.class);
        apiRouter.attach(ROUTE_CONTACTS + "/", ContactListServerResource.class);
        apiRouter.attach(ROUTE_CONTACTS + "/{email}",
                ContactServerResource.class);
        apiRouter.attach(ROUTE_COMPANIES + "/{id}" + ROUTE_CONTACTS,
                ContactListServerResource.class);
        apiRouter.attach(ROUTE_COMPANIES + "/{id}" + ROUTE_CONTACTS + "/",
                ContactListServerResource.class);

        return apiRouter;

    }

}
