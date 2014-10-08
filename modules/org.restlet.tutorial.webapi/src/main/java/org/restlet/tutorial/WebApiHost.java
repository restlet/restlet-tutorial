package org.restlet.tutorial;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.tutorial.persistence.PersistenceService;

public class WebApiHost {

    public static final String PING = "Version: 140328 running";

    public static void main(String[] args) throws Exception {

        PersistenceService.initialize();

        /*
         * Attach application to http://localhost:9000/v1
         */
        Component c = new Component();
        c.getServers().add(Protocol.HTTP, 9000);

        /*
         * Declare logger
         */
        // Declare CLAP client connector
        c.getClients().add(Protocol.CLAP);
        // Look for the log configuration file in the current classloader
        c.getLogService().setLogPropertiesRef("clap:///logging.properties");

        c.getDefaultHost().attach("/v1", new WebApiTutorial());

        c.start();

        Context.getCurrentLogger().info("Restlet application started. URL: http://localhost:9000/v1" );

    }
}
