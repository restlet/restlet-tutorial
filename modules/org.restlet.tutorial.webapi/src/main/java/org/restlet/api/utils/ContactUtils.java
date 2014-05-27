package org.restlet.api.utils;

import org.restlet.api.persistence.entity.Contact;
import org.restlet.api.representation.ContactRepresentation;
import org.restlet.api.representation.ContactWithCompanyRepresentation;

public class ContactUtils {

    public static ContactRepresentation contactToContactRepresentation(
            Contact contact) {
        ContactRepresentation cRepr = new ContactRepresentation();
        if (contact != null) {
            cRepr.setFirstName(contact.getFirstName());
            cRepr.setEmail(contact.getEmail());
            cRepr.setName(contact.getName());
            cRepr.setAge(contact.getAge());
            return cRepr;
        }
        return null;
    }

    public static ContactWithCompanyRepresentation contactToContactWithCompanyRepresentation(
            Contact contact) {
        ContactWithCompanyRepresentation cRepr = new ContactWithCompanyRepresentation();
        if (contact != null) {
            cRepr.setFirstName(contact.getFirstName());
            cRepr.setEmail(contact.getEmail());
            cRepr.setName(contact.getName());
            cRepr.setAge(contact.getAge());
            return cRepr;
        }
        return null;
    }

    public static Contact contactRepresentationToContact(
            ContactRepresentation cRepr) {
        if (cRepr != null) {
            Contact contact = new Contact();
            contact.setFirstName(cRepr.getFirstName());
            contact.setName(cRepr.getName());
            contact.setAge(cRepr.getAge());
            return contact;
        }
        return null;
    }

}
