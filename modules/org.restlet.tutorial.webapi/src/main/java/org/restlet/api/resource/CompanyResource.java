package org.restlet.api.resource;

import org.restlet.api.representation.CompanyRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface CompanyResource {

    @Get
    public CompanyRepresentation represent();

    @Delete
    public void remove();

    @Put
    public CompanyRepresentation store(CompanyRepresentation companyReprIn);

}
