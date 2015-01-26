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

import org.restlet.resource.Status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Parent class of all {@link Exception} written back to the client in case of
 * business errors. The {@link Status} annotation allows the framework to
 * serialize the thrown exception and set the status of the HTTP response.<br>
 * This sample code leverages the {@link JsonIgnoreProperties} annotation in
 * order to control the serialization: some properties are simply hidden. By
 * default, only the exception message and the status property are serialized.
 * any subclass is allowed to add some specific properties.
 * 
 * @author Manuel Boillod
 */
@JsonIgnoreProperties({ "cause", "localizedMessage", "stackTrace", "suppressed" })
public abstract class BusinessException extends RuntimeException {

    private int status;

    public BusinessException(int status, String message) {
        super(message);
        this.status = status;
    }

    public BusinessException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}