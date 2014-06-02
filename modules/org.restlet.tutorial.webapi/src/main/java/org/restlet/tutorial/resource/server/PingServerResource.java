package org.restlet.tutorial.resource.server;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.tutorial.WebApiHost;

public class PingServerResource extends ServerResource {

    @Get("txt")
    public String represent() {
        return WebApiHost.PING;
    }

}
