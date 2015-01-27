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

package org.restlet.tutorial.core.exception;

import java.util.List;

import org.restlet.resource.Status;
import org.restlet.tutorial.core.validation.FieldError;
import org.restlet.tutorial.core.validation.ValidationErrors;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Sent back to client when an incoming entity is invalid. It lists global
 * errors and errors detected on fields, and leads to set the HTTP response
 * status to 422 ("unprocessable entity") thanks to the {@link Status}
 * annotation.<br>
 * This sample code leverages the {@link JsonInclude} annotation in order to
 * control the serialization: only properties that are not null or non-empty are
 * written out.
 * 
 * @author Manuel Boillod
 */
@Status(422)
public class BadEntityException extends BusinessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> globalMessages;

    private List<FieldError> fieldErrors;

    public BadEntityException(String message) {
        super(422, message);
    }

    public BadEntityException(String message, ValidationErrors validationErrors) {
        this(message);
        this.globalMessages = validationErrors.getGlobalMessages();
        this.fieldErrors = validationErrors.getFieldErrors();
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<String> getGlobalMessages() {
        return globalMessages;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
