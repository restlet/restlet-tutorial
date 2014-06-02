package org.restlet.tutorial.resource;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.tutorial.representation.CompanyRepresentation;

public interface CompanyResource {

    @Get
    public CompanyRepresentation represent();

    @Delete
    public void remove();

    @Put
    public CompanyRepresentation store(CompanyRepresentation companyReprIn);

}
