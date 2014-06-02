package org.restlet.tutorial.resource.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.tutorial.WebApiTutorial;
import org.restlet.tutorial.persistence.PersistenceService;
import org.restlet.tutorial.persistence.entity.Company;
import org.restlet.tutorial.representation.CompanyListRepresentation;
import org.restlet.tutorial.representation.CompanyRepresentation;
import org.restlet.tutorial.resource.CompanyListResource;
import org.restlet.tutorial.utils.CompanyUtils;

public class CompanyListServerResource extends ServerResource implements
        CompanyListResource {

    private PersistenceService<Company> persistenceService;

    /*
     * Method called at the creation of the Resource (ie : each time the
     * resource is called)
     */
    @Override
    protected void doInit() throws ResourceException {

        getLogger().finer(
                "Method doInit() of CompanyListServerResource called.");

        /*
         * Initialize a persistence class which will be called to do operations
         * on the database.
         */
        persistenceService = PersistenceService.getCompanyPersistence();

        getLogger().finer(
                "Method doInit() of CompanyListServerResource finished.");
    }

    public CompanyListRepresentation represent() throws ResourceException {

        getLogger().finer(
                "Method represent() of CompanyListServerResource called.");

        /*
         * Check authorization
         */
        if (!isInRole(WebApiTutorial.ROLE_USER)) {
            getLogger().info("Unauthorized user");
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
        }

        getLogger().finer("User allowed to call the resource.");

        try {

            /*
             * Retrieve List<Company> from persistence layer
             */
            List<Company> companies = persistenceService.findAll();

            /*
             * Create companyListRepresentaion
             */
            List<CompanyRepresentation> companyReprs = new ArrayList<CompanyRepresentation>();
            for (Company company : companies) {
                CompanyRepresentation companyRepr = CompanyUtils
                        .companyToCompanyRepresentation(company);
                companyRepr.setSelf(WebApiTutorial.ROUTE_COMPANIES + "/"
                        + company.getId());
                companyReprs.add(companyRepr);
            }
            CompanyListRepresentation result = new CompanyListRepresentation();
            result.setList(companyReprs);

            getLogger()
                    .finer("Method represent() of CompanyListServerResource finished.");
            return result;

        } catch (SQLException ex) {
            System.out.println(ex.getSQLState());
            getLogger().warning("SQLException " + ex.getMessage());
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    }

    @Override
    public CompanyRepresentation add(
CompanyRepresentation companyReprIn) {
        getLogger().finer("Method add() of CompanyListServerResource called.");

        /*
         * Check authorization
         */
        if (!isInRole(WebApiTutorial.ROLE_USER)) {
            getLogger().info("Unauthorized user");
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
        }

        getLogger().finer("User allowed to call the resource.");

        /*
         * Check entity
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

            /*
             * Add new company in DB and retrieve created company
             */
            Company companyOut = persistenceService.add(companyIn);

            /*
             * Convert Company to CompanyRepresentation
             */
            CompanyRepresentation result = CompanyUtils
                    .companyToCompanyRepresentation(companyOut);

            /*
             * Set location of created resource and status to created (201)
             */
            getResponse().setLocationRef(
                    getRequest().getResourceRef()
                            .addSegment(companyOut.getId()));
            getResponse().setStatus(Status.SUCCESS_CREATED);

            getLogger().finer(
                    "Method add() of CompanyListServerResource finished.");
            return result;

        } catch (SQLException ex) {
            getLogger().warning("SQLException " + ex.getMessage());
            if (WebApiTutorial.SQL_STATE_23000_DUPLICATE.equals(ex
                    .getSQLState())) {
                getLogger().info("Integrity constraint violation " + ex);
                throw new ResourceException(
                        Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY,
                        ex.getMessage(), ex);
            }
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
