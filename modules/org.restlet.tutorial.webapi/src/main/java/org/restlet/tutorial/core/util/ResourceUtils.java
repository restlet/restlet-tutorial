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

package org.restlet.tutorial.core.util;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.tutorial.core.exception.BadEntityException;
import org.restlet.tutorial.representation.CompanyRepresentation;

public class ResourceUtils {
    /**
     * Indicates if the authenticated client user associated to the current
     * request is in the given role name.
     * 
     * @param serverResource
     *            The current server resource.
     * @param role
     *            The role to check.
     * @throws ResourceException
     *             In case the current authenticated user has not sufficient
     *             permission.
     */
    public static void checkRole(ServerResource serverResource, String role)
            throws ResourceException {
        if (!serverResource.isInRole(role)) {
            throw new ResourceException(
                    Status.CLIENT_ERROR_FORBIDDEN.getCode(),
                    "You're not authorized to send this call.");
        }
    }

    /**
     * Returns the URL of the resource that represents a company.
     * 
     * @param id
     *            The identifier of the company.
     * @return The URL of the resource that represents a company.
     */
    public static String getCompanyUrl(String id) {
        return "/companies/" + id;
    }

    /**
     * Returns the URL of the resource that represents a contact.
     * 
     * @param email
     *            The email of the contact.
     * @return The URL of the resource that represents a contact.
     */
    public static String getContactUrl(String email) {
        return "/contacts/" + email;
    }

    /**
     * Checks that the given entity is not null.
     * 
     * @param entity
     *            The entity to check.
     * @throws BadEntityException
     *             In case the entity is null.
     */
    public static void notNull(Object entity) throws BadEntityException {
        if (entity == null) {
            throw new BadEntityException("No input entity");
        }
    }

    /**
     * Checks that the given company is valid.
     * 
     * @param companyReprIn
     * @throws BadEntityException
     */
    public static void validate(CompanyRepresentation companyReprIn)
            throws BadEntityException {
        if (companyReprIn.getDuns() == null
                || companyReprIn.getDuns().length() != 9) {
            throw new BadEntityException(
                    "'duns' field should be 9 characters long.");
        }
    }
}
