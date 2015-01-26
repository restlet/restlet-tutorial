/**
 * Copyright 2005-2015 Restlet
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or or EPL 1.0 (the "Licenses"). You can
 * select the license that you prefer but you may not use this file except in
 * compliance with one of these Licenses.
 * 
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://restlet.com/products/restlet-framework
 * 
 * Restlet is a registered trademark of Restlet S.A.S.
 */

package org.restlet.tutorial;

import java.util.logging.Logger;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.swagger.Swagger2SpecificationRestlet;
import org.restlet.ext.swagger.SwaggerSpecificationRestlet;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MemoryRealm;
import org.restlet.security.Role;
import org.restlet.security.User;
import org.restlet.tutorial.persistence.PersistenceService;
import org.restlet.tutorial.resource.server.CompanyListServerResource;
import org.restlet.tutorial.resource.server.CompanyServerResource;
import org.restlet.tutorial.resource.server.ContactListServerResource;
import org.restlet.tutorial.resource.server.ContactServerResource;
import org.restlet.tutorial.resource.server.PingServerResource;

public class WebApiTutorial extends Application {

    public static Logger logger = Engine.getLogger(WebApiTutorial.class);

    public static final String PING = "Version: 1.0.0 running";

    public static void main(String[] args) throws Exception {
        logger.info("Contacts application starting...");

        PersistenceService.initialize();

        // Attach application to http://localhost:9000/v1
        Component c = new Component();
        c.getServers().add(Protocol.HTTP, 9000);

        // Declare logger
        // Declare client connector based on the classloader
        c.getClients().add(Protocol.CLAP);
        // Look for the log configuration file in the current classloader
        c.getLogService().setLogPropertiesRef("clap:///logging.properties");

        c.getDefaultHost().attach("/v1", new WebApiTutorial());

        c.start();

        logger.info("Contacts application started on port 9000");
        logger.info("URL: http://localhost:9000/v1");
    }

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
        setDescription("Full Web API tutorial");

        getRoles().add(new Role(this, ROLE_ADMIN));
        getRoles().add(new Role(this, ROLE_OWNER));
        getRoles().add(new Role(this, ROLE_USER));
    }

    @Override
    public Restlet createInboundRoot() {

        Router publicRouter = publicResources();

        // Create the api router, protected by a guard
        ChallengeAuthenticator apiGuard = createApiGuard();
        Router apiRouter = createApiRouter();
        apiGuard.setNext(apiRouter);

        publicRouter.attachDefault(apiGuard);

        return publicRouter;
    }

    public Router publicResources() {
        Router router = new Router();

        router.attach("/ping", PingServerResource.class);

        // Attach Swagger Specifications
        attachSwaggerSpecification1(router);
        attachSwaggerSpecification2(router);
        return router;
    }

    private ChallengeAuthenticator createApiGuard() {

        ChallengeAuthenticator apiGuard = new ChallengeAuthenticator(
                getContext(), ChallengeScheme.HTTP_BASIC, "realm");

        // Create in-memory users and roles.
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

        // - Verifier : checks authentication
        // - Enroler : to check authorization (roles)
        apiGuard.setVerifier(realm.getVerifier());
        apiGuard.setEnroler(realm.getEnroler());

        // Provide your own authentication checks by extending SecretVerifier or
        // LocalVerifier classes
        // Extend the Enroler class in order to assign roles for an
        // authenticated user

        return apiGuard;
    }

    private Router createApiRouter() {

        // Attach server resources to the given URL template.
        // For instance, CompanyListServerResource is attached
        //     to http://localhost:9000/v1/companies
        // and to http://localhost:9000/v1/companies/
        Router router = new Router(getContext());
        router.attach(ROUTE_COMPANIES, CompanyListServerResource.class);
        router.attach(ROUTE_COMPANIES + "/", CompanyListServerResource.class);
        router.attach(ROUTE_COMPANIES + "/{id}", CompanyServerResource.class);
        router.attach(ROUTE_CONTACTS, ContactListServerResource.class);
        router.attach(ROUTE_CONTACTS + "/", ContactListServerResource.class);
        router.attach(ROUTE_CONTACTS + "/{email}", ContactServerResource.class);

        // List of contacts for a company
        router.attach(ROUTE_COMPANIES + "/{id}" + ROUTE_CONTACTS,
                ContactListServerResource.class);
        router.attach(ROUTE_COMPANIES + "/{id}" + ROUTE_CONTACTS + "/",
                ContactListServerResource.class);

        return router;
    }

    private void attachSwaggerSpecification1(Router router) {
        SwaggerSpecificationRestlet swaggerSpecificationRestlet = new SwaggerSpecificationRestlet(
                this);
        swaggerSpecificationRestlet.setBasePath("http://myapp.com/api/");
        swaggerSpecificationRestlet.attach(router);
    }

    private void attachSwaggerSpecification2(Router router) {
        Swagger2SpecificationRestlet swagger2SpecificationRestlet = new Swagger2SpecificationRestlet(
                this);
        swagger2SpecificationRestlet.setBasePath("http://myapp.com/api/");
        swagger2SpecificationRestlet.attach(router);
    }

}
