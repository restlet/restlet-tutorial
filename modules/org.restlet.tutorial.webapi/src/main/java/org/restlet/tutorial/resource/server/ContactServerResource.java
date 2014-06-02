package org.restlet.tutorial.resource.server;

import java.sql.SQLException;
import java.util.List;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.tutorial.WebApiTutorial;
import org.restlet.tutorial.persistence.PersistenceService;
import org.restlet.tutorial.persistence.entity.Company;
import org.restlet.tutorial.persistence.entity.Contact;
import org.restlet.tutorial.representation.ContactRepresentation;
import org.restlet.tutorial.representation.ContactWithCompanyRepresentation;
import org.restlet.tutorial.resource.ContactResource;
import org.restlet.tutorial.utils.CompanyUtils;
import org.restlet.tutorial.utils.ContactUtils;

public class ContactServerResource extends ServerResource implements
        ContactResource {

    private PersistenceService<Contact> persistenceService;

    private Contact contact;

    private String email;

    /*
     * Method called at the creation of the Resource (ie : each time the
     * resource is called)
     */
    @Override
    protected void doInit() throws ResourceException {

        getLogger().finer("Method doInit() of ContactServerResource called.");

        /*
         * Get contact related to given email
         */
        email = getAttribute("email");

        if (email == null) {
            return;
        }
        /*
         * Initialize a persistence class which will be called to do operations
         * on the database.
         */
        persistenceService = PersistenceService.getContactPersistence();

        /*
         * Retrieve Contact from Persistence layer
         */
        try {

            List<Contact> contacts = persistenceService.findBy("email", email);

            /*
             * Check if retrieved contact is not null, which would mean the
             * given email is wrong.
             */
            if (contacts.isEmpty()) {
                getLogger().info("Contact email does not exist");
                setExisting(false);
            } else {
                setExisting(true);
                contact = contacts.get(0);
            }

        } catch (SQLException ex) {
            getLogger().config("SQLException " + ex.getMessage());
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);
        }

        getLogger().finer("Method doInit() of ContactServerResource finished.");
    }

    public ContactRepresentation represent() throws ResourceException {

        getLogger()
                .finer("Method represent() of ContactServerResource called.");

        /*
         * Check authorization
         */
        if (!isInRole(WebApiTutorial.ROLE_USER)) {
            getLogger().info("Unauthorized user");
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
        }

        getLogger().finer("User allowed to call the resource.");

        try {

            getLogger().finest("Strategy reference");

            /*
             * Convert Contact to ContactReferenceRepresentation
             */
            ContactRepresentation contactRef = ContactUtils
                    .contactToContactRepresentation(contact);
            PersistenceService<Company> companyPersistence = PersistenceService
                    .getCompanyPersistence();
            Company company = companyPersistence.findById(contact
                    .getCompanyId());
            if (company != null) {
                contactRef.setCompany(WebApiTutorial.ROUTE_COMPANIES + "/"
                        + company.getId());
            }
            contactRef.setSelf(WebApiTutorial.ROUTE_CONTACTS + "/"
                    + contactRef.getEmail());

            getLogger().finer(
                    "Method represent() of ContactServerResource finished.");
            return contactRef;

        } catch (SQLException ex) {

            getLogger().config("SQLException " + ex.getMessage());
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);

        }

    }

    @Override
    public ContactWithCompanyRepresentation representLoad() {

        getLogger().finer(
                "Method representLoad() of ContactServerResource called.");

        /*
         * Check authorization
         */
        if (!isInRole(WebApiTutorial.ROLE_USER)) {
            getLogger().info("Unauthorized user");
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
        }

        getLogger().finer("User allowed to call the resource.");

        getLogger().finest("Strategy load");

        try {

            /*
             * Convert Contact to ContactLoadRepresentation
             */
            ContactWithCompanyRepresentation contactLoad = ContactUtils
                    .contactToContactWithCompanyRepresentation(contact);
            PersistenceService<Company> companyPersistence = PersistenceService
                    .getCompanyPersistence();
            Company company = companyPersistence.findById(contact.getCompanyId());
            contactLoad.setCompany(CompanyUtils
                    .companyToCompanyRepresentation(company));

            getLogger().finer(
                    "Method represent() of ContactServerResource finished.");
            return contactLoad;

        } catch (SQLException ex) {
            getLogger().config("SQLException " + ex.getMessage());
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);
        }


    }

    public void remove() throws ResourceException {

        getLogger().finer("Method remove() of ContactServerResource called.");

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
             * Delete contact in DB : return true if deleted
             */
            Boolean isDeleted = persistenceService.delete(contact.getId());

            /*
             * Check if contact is deleted : if not it means that the id must be
             * wrong
             */
            if (isDeleted == false) {
                getLogger().config("Contact id does not exist");
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
            }

            getResponse().setStatus(Status.SUCCESS_NO_CONTENT);

            getLogger().finer(
                    "Method remove() of ContactServerResource finished.");

        } catch (SQLException ex) {
            getLogger().config("SQLException " + ex.getMessage());
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);
        }
    }

    public ContactRepresentation store(ContactRepresentation contactReprIn)
            throws ResourceException {

        getLogger().finer("Method store() of ContactServerResource called.");

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
        if (contactReprIn == null) {
            getLogger().info("Wrong body");
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
                    "Wrong body");
        }

        getLogger().finest("Entity checked");

        try {

            /*
             * Convert ContactPutRepresentation to Contact : finds companyId
             * from given company id.
             */
            Contact contactIn = ContactUtils
                    .contactRepresentationToContact(contactReprIn);
            contactIn.setEmail(email);
            PersistenceService<Company> companyPersistence = PersistenceService
                    .getCompanyPersistence();
            if (contactReprIn.getCompany() != null) {
                List<Company> companies = companyPersistence.findBy("id",
                        contactReprIn.getCompany());
                if (companies.isEmpty()) {
                    getLogger().info("Company id is wrong");
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
                }
                contactIn.setCompanyId(companies.get(0).getId());
            }

            /*
             * Find contacts related to given email. If contact found : update.
             * Else create.
             */
            List<Contact> contacts = persistenceService.findBy("email", email);
            Contact contactOut;
            if (contacts.isEmpty()) {

                getLogger().finest("Create resource");

                /*
                 * Add contact in DB and retrieve the new one.
                 */
                contactOut = persistenceService.add(contactIn);

                getResponse().setStatus(Status.SUCCESS_CREATED);

            } else {

                getLogger().finest("Update resource");

                /*
                 * Update contact in DB and retrieve the new one.
                 */
                contactOut = persistenceService.update(contactIn,
                        contacts.get(0).getId());

                /*
                 * Check if retrieved contact is not null : if it is null it
                 * means that the id is wrong.
                 */
                if (contactOut == null) {
                    getLogger().info("Contact id does not exist");
                    throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
                }

            }

            /*
             * Convert Contact to ContactReferenceRepresentation
             */
            ContactRepresentation result = ContactUtils
                    .contactToContactRepresentation(contactOut);
            Company company = companyPersistence.findById(contactOut
                    .getCompanyId());
            if (company != null) {
                result.setCompany(WebApiTutorial.ROUTE_COMPANIES + "/"
                        + company.getId());
            }

            getLogger().finer(
                    "Method store() of ContactServerResource finished.");
            return result;

        } catch (SQLException ex) {
            if (WebApiTutorial.SQL_STATE_23000_DUPLICATE.equals(ex
                    .getSQLState())) {
                getLogger().info("Integrity constraint violation " + ex);
                throw new ResourceException(
                        Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, ex);
            }
            getLogger().config("SQLException " + ex.getMessage());
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);
        }
    }

}
