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

package org.restlet.tutorial.core.validation;

import java.util.ArrayList;
import java.util.List;

import org.restlet.tutorial.core.exception.BadEntityException;

/**
 * @author Manuel Boillod
 */
public class ValidationErrors {

    private List<FieldError> fieldErrors = new ArrayList<>();

    private List<String> globalMessages = new ArrayList<>();

    public void addFieldError(FieldError fieldError) {
        fieldErrors.add(fieldError);
    }

    public void addFieldError(String field, String message) {
        addFieldError(new FieldError(field, message));
    }

    public void addGlobalMessage(String globalMessage) {
        globalMessages.add(globalMessage);
    }

    /**
     * Checks whether the list of registered messages or field errors are empty.
     * 
     * @param message
     *            The error message in case an error has been listed.
     * @throws BadEntityException
     *             In case an error has been listed.
     */
    public void checkErrors(String message) throws BadEntityException {
        if (!globalMessages.isEmpty() || !fieldErrors.isEmpty()) {
            throw new BadEntityException(message, this);
        }
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public List<String> getGlobalMessages() {
        return globalMessages;
    }
}
