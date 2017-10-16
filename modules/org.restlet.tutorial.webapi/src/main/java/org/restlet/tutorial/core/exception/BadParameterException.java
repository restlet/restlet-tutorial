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

package org.restlet.tutorial.core.exception;

import org.restlet.resource.Status;

/**
 * Sent back to client when an incoming entity is invalid. It defines a
 * "message" property and leads to set the HTTP response status to 400
 * ("bad request") thanks to the {@link Status} annotation.
 * 
 * @author Manuel Boillod
 */
@Status(400)
public class BadParameterException extends BusinessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadParameterException(String message) {
        super(400, message);
    }
}
