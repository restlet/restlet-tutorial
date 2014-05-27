package org.restlet.api.resource;

import org.restlet.api.representation.CompanyListRepresentation;
import org.restlet.api.representation.CompanyRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface CompanyListResource {

    @Get
    public CompanyListRepresentation represent();

    @Post
    public CompanyRepresentation add(
CompanyRepresentation companyReprIn);

}
