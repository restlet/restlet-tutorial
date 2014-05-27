package org.restlet.api.resource;

import org.restlet.api.representation.ContactRepresentation;
import org.restlet.api.representation.ContactWithCompanyRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface ContactResource {

    @Get
    public ContactRepresentation represent();

    @Get("?strategy=load")
    public ContactWithCompanyRepresentation representLoad();

    @Delete
    public void remove();

    @Put
    public ContactRepresentation store(ContactRepresentation contactReprIn);
}
