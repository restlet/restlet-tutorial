/*
 * Copyright 2005-2017 Restlet
 *
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or or EPL 1.0 (the "Licenses"). You can
 * select the license that you prefer but you may not use this file except in
 * compliance with one of these Licenses.
 *
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 *
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 *
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 *
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://restlet.com/products/restlet-framework
 *
 * Restlet is a registered trademark of Restlet S.A.S.
 */

package org.restlet.tutorial.resource.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.tutorial.WebApiTutorial;
import org.restlet.tutorial.core.exception.BadEntityException;
import org.restlet.tutorial.core.util.ResourceUtils;
import org.restlet.tutorial.persistence.CompanyPersistence;
import org.restlet.tutorial.persistence.PersistenceService;
import org.restlet.tutorial.persistence.entity.Company;
import org.restlet.tutorial.representation.CompanyListRepresentation;
import org.restlet.tutorial.representation.CompanyRepresentation;
import org.restlet.tutorial.resource.CompanyListResource;
import org.restlet.tutorial.utils.CompanyUtils;

public class CompanyListServerResource extends ServerResource implements
        CompanyListResource {

    private CompanyPersistence companyPersistence;

    /**
     * Method called at the creation of the Resource (ie : each time the
     * resource is called).
     */
    @Override
    protected void doInit() {
        getLogger().finer("Initialization of CompanyListServerResource.");

        // Initialize the persistence layer.
        companyPersistence = PersistenceService.getCompanyPersistence();

        getLogger().finer("Initialization of CompanyListServerResource ended.");
    }

    public CompanyListRepresentation getCompanies() {
        getLogger().finer("Retrieve the list of companies.");

        // Check authorization
        ResourceUtils.checkRole(this, WebApiTutorial.ROLE_USER);
        getLogger().finer("User allowed to list companies.");

        try {
            // Retrieve the companies from persistence layer
            List<Company> companies = companyPersistence.findAll();

            // Create companyListRepresentaion
            List<CompanyRepresentation> companyReprs = new ArrayList<CompanyRepresentation>();
            for (Company company : companies) {
                CompanyRepresentation companyRepr = CompanyUtils
                        .toCompanyRepresentation(company);
                companyRepr
                        .setSelf(ResourceUtils.getCompanyUrl(company.getId()));
                companyReprs.add(companyRepr);
            }
            CompanyListRepresentation result = new CompanyListRepresentation();
            result.setList(companyReprs);

            getLogger().finer("List of companies successfully retrieved.");

            return result;
        } catch (SQLException ex) {
            getLogger().log(Level.WARNING, "Error when listing companies", ex);
            throw new ResourceException(ex);
        }
    }

    @Override
    public CompanyRepresentation add(CompanyRepresentation companyReprIn)
            throws BadEntityException {
        getLogger().finer("Add a new company.");

        // Check authorization
        ResourceUtils.checkRole(this, WebApiTutorial.ROLE_USER);
        getLogger().finer("User allowed to add a company.");

        // Check entity
        ResourceUtils.notNull(companyReprIn);
        ResourceUtils.validate(companyReprIn);
        getLogger().finer("Company checked");

        try {

            // Convert CompanyRepresentation to Company
            Company companyIn = CompanyUtils.toCompany(companyReprIn);

            // Add new company in DB and retrieve created company
            Company companyOut = companyPersistence.add(companyIn);

            // Convert Company to CompanyRepresentation
            CompanyRepresentation result = CompanyUtils
                    .toCompanyRepresentation(companyOut);

            // Set location of created resource and status to created (201)
            getResponse().setLocationRef(
                    ResourceUtils.getCompanyUrl(companyOut.getId()));
            getResponse().setStatus(Status.SUCCESS_CREATED);

            getLogger().finer("Company successfully added.");

            return result;
        } catch (SQLException ex) {
            getLogger().log(Level.WARNING, "Error when adding a company", ex);
            if (WebApiTutorial.SQL_STATE_23000_DUPLICATE.equals(ex
                    .getSQLState())) {
                throw new BadEntityException(
                        "Can't add a company due to integrity constraint violation.");
            }
            throw new ResourceException(ex);
        }
    }

}
