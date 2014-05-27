package org.restlet.api.resource.server;

import org.restlet.api.WebApiHost;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class PingServerResource extends ServerResource {

    @Get("txt")
    public String represent() {
        return WebApiHost.PING;
    }

}
