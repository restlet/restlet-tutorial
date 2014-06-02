package org.restlet.tutorial.resource;

import org.restlet.resource.Get;
import org.restlet.tutorial.representation.ContactListRepresentation;
import org.restlet.tutorial.representation.ContactWithCompanyListRepresentation;

public interface ContactListResource {

    @Get
    public ContactListRepresentation represent();

    @Get("?strategy=load")
    public ContactWithCompanyListRepresentation representLoad();

}
