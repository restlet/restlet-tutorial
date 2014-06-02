package org.restlet.tutorial.resource;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.tutorial.representation.ContactRepresentation;
import org.restlet.tutorial.representation.ContactWithCompanyRepresentation;

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
