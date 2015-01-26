/**
 * Copyright 2005-2015 Restlet
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
import java.util.logging.Level;

import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.tutorial.WebApiTutorial;
import org.restlet.tutorial.core.exception.BadEntityException;
import org.restlet.tutorial.core.exception.NotFoundException;
import org.restlet.tutorial.core.util.ResourceUtils;
import org.restlet.tutorial.persistence.CompanyPersistence;
import org.restlet.tutorial.persistence.PersistenceService;
import org.restlet.tutorial.persistence.entity.Company;
import org.restlet.tutorial.representation.CompanyRepresentation;
import org.restlet.tutorial.resource.CompanyResource;
import org.restlet.tutorial.utils.CompanyUtils;

public class CompanyServerResource extends ServerResource implements
        CompanyResource {

    private CompanyPersistence companyPersistence;

    private Company company;

    private String id;

    /**
     * Method called at the creation of the Resource (ie : each time the
     * resource is called).
     */
    @Override
    protected void doInit() {

        // Get company related to given id
        id = getAttribute("id");

        getLogger().finer(
                "Initialization of CompanyServerResource with company id: "
                        + id);

        // Initialize the persistence layer.
        companyPersistence = PersistenceService.getCompanyPersistence();

        try {
            company = companyPersistence.findById(id);

            // Check if retrieved company is not null. If it is null it means
            // that the given email is wrong.
            setExisting(company != null);
            if (!isExisting()) {
                getLogger().config("Company id does not exist:" + id);
                setExisting(false);
            }

        } catch (SQLException ex) {
            throw new ResourceException(ex);
        }

        getLogger().finer(
                "Initialization of CompanyServerResource ended with company id: "
                        + id);
    }

    public CompanyRepresentation getCompany() {

        getLogger().finer("Retrieve a company");

        // Check authorization
        ResourceUtils.checkRole(this, WebApiTutorial.ROLE_USER);
        getLogger().finer("User allowed to retrieve a company.");

        CompanyRepresentation result = CompanyUtils
                .toCompanyRepresentation(company);
        result.setSelf(ResourceUtils.getCompanyUrl(company.getId()));

        getLogger().finer("Company successfully retrieved");

        return result;
    }

    public void remove() throws NotFoundException {

        getLogger().finer("Removal of company");

        ResourceUtils.checkRole(this, WebApiTutorial.ROLE_USER);
        getLogger().finer("User allowed to remove a company.");

        try {

            // Delete company in DB: return true if deleted
            Boolean isDeleted = companyPersistence.delete(company.getId());

            // If contact has not been deleted: if not it means that the id must
            // be wrong
            if (!isDeleted) {
                getLogger().config("Company id does not exist");
                throw new NotFoundException(
                        "Company with the following identifier does not exist:"
                                + company.getId());
            }
            getLogger().finer("Company successfully removed.");

        } catch (SQLException ex) {
            getLogger().log(Level.WARNING, "Error when removing a company", ex);
            throw new ResourceException(ex);
        }

    }

    public CompanyRepresentation store(CompanyRepresentation companyReprIn)
            throws BadEntityException, NotFoundException {
        getLogger().finer("Update a company.");

        ResourceUtils.checkRole(this, WebApiTutorial.ROLE_OWNER);
        getLogger().finer("User allowed to update a company.");

        // Check given entity
        ResourceUtils.notNull(companyReprIn);
        ResourceUtils.validate(companyReprIn);
        getLogger().finer("Company checked");

        try {

            // Convert CompanyRepresentation to Company
            Company companyIn = CompanyUtils.toCompany(companyReprIn);
            companyIn.setId(id);

            Company companyOut;

            // If company exists, we update it.
            if (isExisting()) {
                getLogger().finer("Update company.");

                // Update company in DB and retrieve the new one.
                companyOut = companyPersistence.update(companyIn,
                        company.getId());

                // Check if retrieved company is not null : if it is null it
                // means that the id is wrong.
                if (companyOut == null) {
                    getLogger().finer("Company does not exist.");
                    throw new NotFoundException(
                            "Company with the following id does not exist: "
                                    + id);
                }
            } else {
                getLogger().finer("Resource does not exist.");
                throw new NotFoundException(
                        "Company with the following id does not exist: " + id);
            }

            getLogger().finer("Company successfully updated.");
            return CompanyUtils.toCompanyRepresentation(companyOut);

        } catch (SQLException ex) {
            getLogger().log(Level.WARNING, "Error when updating a company", ex);
            if (WebApiTutorial.SQL_STATE_23000_DUPLICATE.equals(ex
                    .getSQLState())) {
                throw new BadEntityException(
                        "Can't update a company due to integrity constraint violation.");
            }
            throw new ResourceException(ex);
        }
    }

}
