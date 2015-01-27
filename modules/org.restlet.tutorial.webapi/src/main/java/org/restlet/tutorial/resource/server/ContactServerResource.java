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
import java.util.List;
import java.util.logging.Level;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.tutorial.WebApiTutorial;
import org.restlet.tutorial.core.exception.BadEntityException;
import org.restlet.tutorial.core.exception.NotFoundException;
import org.restlet.tutorial.core.util.ResourceUtils;
import org.restlet.tutorial.core.validation.ValidationErrors;
import org.restlet.tutorial.persistence.ContactPersistence;
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

    private ContactPersistence contactPersistence;

    private Contact contact;

    private String email;

    /**
     * Method called at the creation of the Resource (ie : each time the
     * resource is called).
     */
    @Override
    protected void doInit() {

        // Get contact related to given email
        email = getAttribute("email");

        getLogger().finer(
                "Initialization of ContactServerResource with contact email: "
                        + email);

        // Initialize the persistence layer.
        contactPersistence = PersistenceService.getContactPersistence();

        // Retrieve Contact from Persistence layer
        try {

            List<Contact> contacts = contactPersistence.findByEmail(email);

            // Check if retrieved contact is not null, which would mean the
            // given email is wrong.
            if (!contacts.isEmpty()) {
                contact = contacts.get(0);
                setExisting(true);
            } else {
                setExisting(false);
            }
        } catch (SQLException ex) {
            throw new ResourceException(ex);
        }

        getLogger().finer(
                "Initialization of ContactServerResource ended with contact email: "
                        + email);
    }

    public ContactRepresentation getContact() {
    	
    	getLogger().finer("Retrieve a contact");	
    	
    	if (contact == null){
            throw new NotFoundException("No contact with email: " + email);
    	}

        // Check authorization
        ResourceUtils.checkRole(this, WebApiTutorial.ROLE_USER);
        getLogger().finer("User allowed to retrieve a contact.");

        ContactRepresentation contactRef = ContactUtils
                .toContactRepresentation(contact);
        contactRef.setCompany(ResourceUtils.getCompanyUrl(contact
                .getCompanyId()));
        contactRef.setSelf(ResourceUtils.getContactUrl(contactRef.getEmail()));

        getLogger().finer("Contact successfully retrieved.");

        return contactRef;
    }

    @Override
    public ContactWithCompanyRepresentation getContactLoad() {

        getLogger().finer("Load a contact");

        if (contact == null){
            throw new NotFoundException("No contact with email: " + email);
    	}
        
        // Check authorization
        ResourceUtils.checkRole(this, WebApiTutorial.ROLE_USER);
        getLogger().finer("User allowed to load a contact.");

        try {

            ContactWithCompanyRepresentation contactLoad = ContactUtils
                    .toContactWithCompanyRepresentation(contact);
            PersistenceService<Company> companyPersistence = PersistenceService
                    .getCompanyPersistence();
            Company company = companyPersistence.findById(contact
                    .getCompanyId());
            contactLoad.setCompany(CompanyUtils
                    .toCompanyRepresentation(company));

            getLogger().finer("Contact successfully loaded");
            return contactLoad;

        } catch (SQLException ex) {
            throw new ResourceException(ex);
        }

    }

    public void remove() throws NotFoundException {

        getLogger().finer("Remove a contact.");
        
    	if (contact == null){
            throw new NotFoundException("No contact with email: " + email);
    	}

        // Check authorization
        ResourceUtils.checkRole(this, WebApiTutorial.ROLE_OWNER);
        getLogger().finer("User allowed to remove a contact.");

        try {
            // Delete company in DB: return true if deleted
            Boolean isDeleted = contactPersistence.delete(contact.getId());

            // If contact has not been deleted: if not it means that the id must
            // be wrong
            if (!isDeleted) {
                getLogger().config("Contact id does not exist");
                throw new NotFoundException(
                        "Contact with the following identifier does not exist:"
                                + contact.getId());
            }
            getLogger().finer("Contact successfully removed.");

        } catch (SQLException ex) {
            getLogger().log(Level.WARNING, "Error when removing a contact", ex);
            throw new ResourceException(ex);
        }
    }

    public ContactRepresentation store(ContactRepresentation contactReprIn)
            throws NotFoundException, BadEntityException {

        getLogger().finer("Update a contact.");

        ResourceUtils.checkRole(this, WebApiTutorial.ROLE_OWNER);
        getLogger().finer("User allowed to update a contact.");

        // Check given entity
        ResourceUtils.notNull(contactReprIn);
        getLogger().finer("Contact checked");

        try {

            // Convert ContactPutRepresentation to Contact : finds companyId
            // from given company id.
            Contact contactIn = ContactUtils.toContact(contactReprIn);
            contactIn.setEmail(email);
            PersistenceService<Company> companyPersistence = PersistenceService
                    .getCompanyPersistence();
            if (contactReprIn.getCompany() != null) {
                Company company = companyPersistence.findById(contactReprIn
                        .getCompany());
                if (company == null) {
                    getLogger().info(
                            "Company id is wrong: "
                                    + contactReprIn.getCompany());
                    ValidationErrors ve = new ValidationErrors();
                    ve.addFieldError("company", "Company id is wrong: "
                            + contactReprIn.getCompany());
                    throw new BadEntityException(
                            "Contact refers a company that does not exist.", ve);
                }
                contactIn.setCompanyId(company.getId());
            }

            // If contact related to given email found : update, else create.
            List<Contact> contacts = contactPersistence.findByEmail(email);
            Contact contactOut;
            if (contacts.isEmpty()) {
                getLogger().finer("Create resource");

                // Add contact in DB and retrieve the new one.
                contactOut = contactPersistence.add(contactIn);

                getResponse().setStatus(Status.SUCCESS_CREATED);
            } else {
                getLogger().finer("Update resource");

                // Update contact in DB and retrieve the new one.
                contactOut = contactPersistence.update(contactIn,
                        contacts.get(0).getId());

                // Check if retrieved contact is not null : if it is null it
                // means that the id is wrong.
                if (contactOut == null) {
                    getLogger().info("Contact id does not exist");
                    throw new NotFoundException("Contact id does not exist");
                }
            }

            ContactRepresentation result = ContactUtils
                    .toContactRepresentation(contactOut);
            Company company = companyPersistence.findById(contactOut
                    .getCompanyId());
            if (company != null) {
                result.setCompany(ResourceUtils.getCompanyUrl(company.getId()));
            }

            getLogger().finer("Contact successfully updated.");
            return result;

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
