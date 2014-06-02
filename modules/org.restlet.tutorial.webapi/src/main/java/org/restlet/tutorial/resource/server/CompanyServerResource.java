package org.restlet.tutorial.resource.server;

import java.sql.SQLException;
import java.util.List;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.tutorial.WebApiTutorial;
import org.restlet.tutorial.persistence.PersistenceService;
import org.restlet.tutorial.persistence.entity.Company;
import org.restlet.tutorial.representation.CompanyRepresentation;
import org.restlet.tutorial.resource.CompanyResource;
import org.restlet.tutorial.utils.CompanyUtils;

public class CompanyServerResource extends ServerResource implements
        CompanyResource {

    private PersistenceService<Company> persistenceService;

    private Company company;

    private String id;

    /*
     * Method called at the creation of the Resource (ie : each time the
     * resource is called)
     */
    @Override
    protected void doInit() throws ResourceException {

        getLogger().finer("Method doInit() of CompanyServerResource called.");

        /*
         * Initialize a persistence class which will be called to do operations
         * on the database.
         */
        persistenceService = PersistenceService.getCompanyPersistence();

        /*
         * Get company related to given id
         */
        id = getAttribute("id");

        if (id == null) {
            return;
        }

        try {

            List<Company> companies = persistenceService.findBy("id", id);

            /*
             * Check if retrieved company is not null. If it is null it means
             * that the given email is wrong.
             */
            if (companies.isEmpty()) {
                getLogger().config("Company id does not exist");
                setExisting(false);
            } else {
                company = companies.get(0);
                setExisting(true);
            }

        } catch (SQLException ex) {
            getLogger().warning("SQLException " + ex.getMessage());
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }

        getLogger().finer("Method doInit() of CompanyServerResource finished.");
    }

    public CompanyRepresentation represent() throws ResourceException {

        getLogger()
                .finer("Method represent() of CompanyServerResource called.");

        /*
         * Check authorization
         */
        if (!isInRole(WebApiTutorial.ROLE_USER)) {
            getLogger().info("Unauthorized user");
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
        }

        getLogger().finer("User allowed to call the resource.");

        getLogger().finer(
                "Method represent() of CompanyServerResource finished.");
        
        CompanyRepresentation result = CompanyUtils
                .companyToCompanyRepresentation(company);
        result.setSelf(WebApiTutorial.ROUTE_COMPANIES + "/" + company.getId());

        return result;

    }

    public void remove() throws ResourceException {

        getLogger().finer("Method remove() of CompanyServerResource called.");

        /*
         * Check authorization
         */
        if (!isInRole(WebApiTutorial.ROLE_OWNER)) {
            getLogger().info("Unauthorized user");
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
        }

        getLogger().finer("User allowed to call the resource.");

        try {

            /*
             * Delete company in DB : return true if deleted
             */
            Boolean isDeleted = persistenceService.delete(company.getId());

            /*
             * Check if contact is deleted : if not it means that the id must be
             * wrong
             */
            if (isDeleted == false) {
                getLogger().config("Company id does not exist");
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
            }

            getResponse().setStatus(Status.SUCCESS_NO_CONTENT);

            getLogger().finer(
                    "Method remove() of CompanyServerResource finished.");

        } catch (SQLException ex) {
            if (WebApiTutorial.SQL_STATE_23000_DUPLICATE.equals(ex
                    .getSQLState())) {
                getLogger().info("Integrity constraint violation " + ex);
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
                        ex.getMessage(), ex);
            }
            getLogger().warning("SQLException " + ex.getMessage());
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);
        }

    }

    public CompanyRepresentation store(CompanyRepresentation companyReprIn)
            throws ResourceException {

        getLogger().finer("Method store() of CompanyServerResource called.");

        /*
         * Check authorization
         */
        if (!isInRole(WebApiTutorial.ROLE_OWNER)) {
            getLogger().info("Unauthorized user");
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
        }

        getLogger().finer("User allowed to call the resource.");

        /*
         * Check given entity
         */
        if (companyReprIn == null) {
            getLogger().info("Wrong body");
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
                    "Wrong body");
        }
        String isWellFormed = isWellFormed(companyReprIn);
        if (!isWellFormed.isEmpty()) {
            getLogger().info("Body : wrong arguments " + isWellFormed);
            throw new ResourceException(
                    Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY,
                    "Wrong arguments : " + isWellFormed);
        }
        getLogger().finest("Entity checked");

        try {

            /*
             * Convert CompanyRepresentation to Company
             */
            Company companyIn = CompanyUtils
                    .companyRepresentationToCompany(companyReprIn);
            companyIn.setId(id);

            Company companyOut;

            /*
             * If company does not exist yet : create company. Else, update
             * company.
             */
            if (!isExisting()) {

                getLogger().finer("Resource does not exist.");

                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,
                        "id does not exist");

            } else {

                getLogger().finest("Update resource.");

                /*
                 * Update company in DB and retrieve the new one.
                 */
                companyOut = persistenceService.update(companyIn,
                        company.getId());

                /*
                 * Check if retrieved company is not null : if it is null it
                 * means that the id is wrong.
                 */
                if (companyOut == null) {
                    getLogger().config("Contact id does not exist");
                    throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
                }

            }

            getLogger().finer(
                    "Method store() of CompanyServerResource finished.");
            return CompanyUtils.companyToCompanyRepresentation(companyOut);

        } catch (SQLException ex) {
            if (WebApiTutorial.SQL_STATE_23000_DUPLICATE.equals(ex
                    .getSQLState())) {
                getLogger().info("Integrity constraint violation " + ex);
                throw new ResourceException(
                        Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY,
                        ex.getMessage(), ex);
            }
            getLogger().warning("SQLException " + ex.getMessage());
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    }

    private String isWellFormed(CompanyRepresentation companyReprIn) {
        if (companyReprIn.getDuns() != null
                && companyReprIn.getDuns().length() != 9) {
            return "Company DUNS should have a size of 9";
        }
        return "";
    }

}
