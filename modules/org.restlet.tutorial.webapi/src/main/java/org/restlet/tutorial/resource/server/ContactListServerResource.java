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
import org.restlet.tutorial.persistence.entity.Contact;
import org.restlet.tutorial.representation.ContactListRepresentation;
import org.restlet.tutorial.representation.ContactRepresentation;
import org.restlet.tutorial.representation.ContactWithCompanyListRepresentation;
import org.restlet.tutorial.representation.ContactWithCompanyRepresentation;
import org.restlet.tutorial.resource.ContactListResource;
import org.restlet.tutorial.utils.CompanyUtils;
import org.restlet.tutorial.utils.ContactUtils;

public class ContactListServerResource extends ServerResource implements
ContactListResource {

    private PersistenceService<Contact> persistenceService;

    /*
     * Method called at the creation of the Resource (ie : each time the
     * resource is called)
     */
    @Override
    protected void doInit() throws ResourceException {

        getLogger().finer(
                "Method doInit() of ContactListServerResource called.");

        /*
         * Initialize a persistence class which will be called to do operations
         * on the database.
         */
        persistenceService = PersistenceService.getContactPersistence();

        getLogger().finer(
                "Method doInit() of ContactListServerResource finished.");
    }

    public ContactListRepresentation represent() throws ResourceException {

        getLogger().finer(
                "Method represent() of ContactListServerResource called.");

        /*
         * Check authorization
         */
        if (!isInRole(WebApiTutorial.ROLE_USER)) {
            getLogger().info("Unauthorized user");
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
        }

        getLogger().finer("User allowed to call the resource.");

        try {

            List<Contact> contactsOut;

            /*
             * Check companyid attribute. If contactid = null : retrieve all
             * contacts, else retrieve contacts linked to the wanted company
             */
            String id = getAttribute("id");

            if (id == null) {

                getLogger().finest("id null retrieve all contacts.");
                /*
                 * Retrieve List<Contact> from persistence layer
                 */
                contactsOut = persistenceService.findAll();

            } else {

                getLogger().finest(
                        "id not null retrieve related to the company's id.");
                /*
                 * Find company id from given id and retrieve related items.
                 */
                PersistenceService<Company> companyPersistence = PersistenceService
                        .getCompanyPersistence();
                List<Company> companies = companyPersistence.findBy("id", id);
                if (companies.isEmpty()) {
                    getLogger().info("id does not exist");
                    throw new ResourceException(
                            Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY,
                            "id does not exist");
                }

                /*
                 * Retrieve List<Contact> from persistence layer
                 */
                contactsOut = persistenceService.findBy("company_id", companies
                        .get(0).getId());

            }

            List<ContactRepresentation> contacts = new ArrayList<ContactRepresentation>();

            for (Contact contact : contactsOut) {
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
                contacts.add(contactRef);
            }

            ContactListRepresentation result = new ContactListRepresentation();
            result.setList(contacts);

            getLogger()
                    .finer("Method represent() of ContactListServerResource finished.");
            return result;

        } catch (SQLException ex) {
            getLogger().warning("SQLException " + ex.getMessage());
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    }

    @Override
    public ContactWithCompanyListRepresentation representLoad() {

        getLogger().finer(
                "Method representLoad() of ContactListServerResource called.");

        /*
         * Check authorization
         */
        if (!isInRole(WebApiTutorial.ROLE_USER)) {
            getLogger().info("Unauthorized user");
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
        }

        getLogger().finer("User allowed to call the resource.");

        try {

            List<Contact> contactsOut;

            /*
             * Check companyid attribute. If contactid = null : retrieve all
             * contacts, else retrieve contacts linked to the wanted company
             */
            String id = getAttribute("id");

            if (id == null) {

                getLogger().finest("id null retrieve all contacts.");
                /*
                 * Retrieve List<Contact> from persistence layer
                 */
                contactsOut = persistenceService.findAll();

            } else {

                getLogger().finest(
                        "id not null retrieve related to the company's id.");
                /*
                 * Find company id from given id and retrieve related items.
                 */
                PersistenceService<Company> companyPersistence = PersistenceService
                        .getCompanyPersistence();
                List<Company> companies = companyPersistence.findBy("id", id);
                if (companies.isEmpty()) {
                    getLogger().info("id does not exist");
                    throw new ResourceException(
                            Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY,
                            "id does not exist");
                }

                /*
                 * Retrieve List<Contact> from persistence layer
                 */
                contactsOut = persistenceService.findBy("company_id", companies
                        .get(0).getId());

            }

            List<ContactWithCompanyRepresentation> contacts = new ArrayList<ContactWithCompanyRepresentation>();

            getLogger().finest("Strategy load");

            for (Contact contact : contactsOut) {
                /*
                 * Convert Contact to ContactLoadRepresentation.
                 */
                ContactWithCompanyRepresentation contactLoad = ContactUtils
                        .contactToContactWithCompanyRepresentation(contact);
                PersistenceService<Company> companyPersistence = PersistenceService
                        .getCompanyPersistence();
                Company company = companyPersistence.findById(contact
                        .getCompanyId());
                contactLoad.setCompany(CompanyUtils
                        .companyToCompanyRepresentation(company));
                contactLoad.setSelf(WebApiTutorial.ROUTE_CONTACTS + "/"
                        + contactLoad.getEmail());
                contacts.add(contactLoad);
            }

            ContactWithCompanyListRepresentation result = new ContactWithCompanyListRepresentation();
            result.setList(contacts);

            getLogger()
                    .finer("Method represent() of ContactListServerResource finished.");
            return result;

        } catch (SQLException ex) {
            getLogger().warning("SQLException " + ex.getMessage());
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }

    }

}
