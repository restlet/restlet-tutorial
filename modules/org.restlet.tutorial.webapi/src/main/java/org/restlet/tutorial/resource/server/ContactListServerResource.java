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

import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.tutorial.WebApiTutorial;
import org.restlet.tutorial.core.exception.NotFoundException;
import org.restlet.tutorial.core.util.ResourceUtils;
import org.restlet.tutorial.persistence.ContactPersistence;
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

    private ContactPersistence contactPersistence;

    /**
     * Optional identifier of a company in order to filter the list of contacts.
     */
    private String companyId;

    /**
     * Method called at the creation of the Resource (ie : each time the
     * resource is called).
     */
    @Override
    protected void doInit() {
        getLogger().finer("Initialization of CompanyListServerResource.");

        // initialize the persistence layer.
        contactPersistence = PersistenceService.getContactPersistence();

        // indicates if we filter the list by a company's id.
        companyId = getAttribute("id");

        getLogger().finer("Initialization of CompanyListServerResource ended.");
    }

    @Override
    public ContactListRepresentation getContacts() throws NotFoundException {

        if (companyId == null) {
            getLogger().finer("Retrieve the list of contacts.");
        } else {
            getLogger()
                    .finer("Retrieve all contacts for company: " + companyId);
        }

        // Check authorization
        ResourceUtils.checkRole(this, WebApiTutorial.ROLE_USER);
        getLogger().finer("User allowed to list contacts.");

        try {
            List<Contact> contactsOut = null;

            if (companyId == null) {
                contactsOut = contactPersistence.findAll();
            } else {
                // Find company from given id and retrieve related items.
                PersistenceService<Company> companyPersistence = PersistenceService
                        .getCompanyPersistence();
                Company company = companyPersistence.findById(companyId);
                if (company == null) {
                    getLogger().info(
                            "Company with the following identifier does not exist: "
                                    + companyId);
                    throw new NotFoundException(
                            "Company with the following identifier does not exist: "
                                    + companyId);
                }

                // Retrieve the list of contacts from persistence layer.
                contactsOut = contactPersistence.findByCompany(company.getId());
            }

            List<ContactRepresentation> contacts = new ArrayList<ContactRepresentation>();

            for (Contact contact : contactsOut) {
                // Convert Contact to ContactRepresentation
                ContactRepresentation contactRef = ContactUtils
                        .toContactRepresentation(contact);

                if (contact.getCompanyId() != null) {
                    contactRef.setCompany(ResourceUtils.getCompanyUrl(contact
                            .getCompanyId()));
                }
                contactRef.setSelf(ResourceUtils.getContactUrl(contactRef
                        .getEmail()));
                contacts.add(contactRef);
            }

            ContactListRepresentation result = new ContactListRepresentation();
            result.setList(contacts);

            getLogger().fine("List of contacts successfully retrieved.");
            return result;

        } catch (SQLException ex) {
            getLogger().log(Level.WARNING, "Error when listing contacts.", ex);
            throw new ResourceException(ex);
        }
    }

    @Override
    public ContactWithCompanyListRepresentation getContactsLoad()
            throws NotFoundException {

        if (companyId == null) {
            getLogger().finer("Load the list of contacts.");
        } else {
            getLogger().finer("Load all contacts for company: " + companyId);
        }

        // Check authorization
        ResourceUtils.checkRole(this, WebApiTutorial.ROLE_USER);
        getLogger().finer("User allowed to load contacts.");

        try {
            PersistenceService<Company> companyPersistence = PersistenceService
                    .getCompanyPersistence();

            List<Contact> contactsOut = null;

            if (companyId == null) {
                // Retrieve the list of contacts from persistence layer.
                contactsOut = contactPersistence.findAll();
            } else {
                // Retrieve the company from persistence layer.
                Company company = companyPersistence.findById(companyId);
                if (company == null) {
                    getLogger().info(
                            "Company with the following identifier does not exist: "
                                    + companyId);
                    throw new NotFoundException(
                            "Company with the following identifier does not exist: "
                                    + companyId);
                }

                // Retrieve the list of contacts from persistence layer.
                contactsOut = contactPersistence.findByCompany(company.getId());
            }

            List<ContactWithCompanyRepresentation> contacts = new ArrayList<ContactWithCompanyRepresentation>();

            getLogger().finer("Strategy load");

            for (Contact contact : contactsOut) {
                // Convert Contact to ContactLoadRepresentation.
                ContactWithCompanyRepresentation contactLoad = ContactUtils
                        .toContactWithCompanyRepresentation(contact);

                Company company = companyPersistence.findById(contact
                        .getCompanyId());
                contactLoad.setCompany(CompanyUtils
                        .toCompanyRepresentation(company));
                contactLoad.setSelf(ResourceUtils.getContactUrl(contactLoad
                        .getEmail()));
                contacts.add(contactLoad);
            }

            ContactWithCompanyListRepresentation result = new ContactWithCompanyListRepresentation();
            result.setList(contacts);

            getLogger().finer("List of contacts successfully loaded.");
            return result;

        } catch (SQLException ex) {
            getLogger().log(Level.WARNING, "Error when loading contacts.", ex);
            throw new ResourceException(ex);
        }

    }
}
