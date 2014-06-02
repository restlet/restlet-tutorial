package org.restlet.tutorial.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.tutorial.representation.CompanyListRepresentation;
import org.restlet.tutorial.representation.CompanyRepresentation;

public interface CompanyListResource {

    @Get
    public CompanyListRepresentation represent();

    @Post
    public CompanyRepresentation add(
CompanyRepresentation companyReprIn);

}
