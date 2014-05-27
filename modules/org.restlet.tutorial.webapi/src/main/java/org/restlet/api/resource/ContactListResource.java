package org.restlet.api.resource;

import org.restlet.api.representation.ContactListRepresentation;
import org.restlet.api.representation.ContactWithCompanyListRepresentation;
import org.restlet.resource.Get;

public interface ContactListResource {

    @Get
    public ContactListRepresentation represent();

    @Get("?strategy=load")
    public ContactWithCompanyListRepresentation representLoad();

}
