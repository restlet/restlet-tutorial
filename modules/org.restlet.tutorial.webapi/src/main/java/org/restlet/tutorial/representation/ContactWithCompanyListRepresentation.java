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

package org.restlet.tutorial.representation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Acts as container for list of contacts.<br>
 * It is mainly used in order to gain fine control on the way we generate XML
 * document via Jackson.<br>
 * 
 * This sample code leverages several annotations in order to control the
 * serialization:
 * <ul>
 * <li>the {@link JacksonXmlRootElement} annotation : the root element of the
 * XML representation will be called "contacts".</li>
 * <li>the {@link JacksonXmlElementWrapper} annotation: in the XML
 * representation, the "contacts" attribute won't be wrapped inside a "list"
 * element.</li>
 * <li>the {@link JacksonXmlProperty} annotation: in the XML representation, any
 * "Tag" element is marked as "contact" instead of "contacts".</li>
 * </ul>
 * <br>
 * 
 * @see https://github.com/FasterXML/jackson-dataformat-xml#known-limitations
 * 
 * @author Manuel Boillod
 * @author Guillaume Blondeau
 */
@JacksonXmlRootElement(localName = "contacts")
public class ContactWithCompanyListRepresentation {

    private List<ContactWithCompanyRepresentation> list;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "contact")
    public List<ContactWithCompanyRepresentation> getList() {
        if (list == null) {
            list = new ArrayList<ContactWithCompanyRepresentation>();
        }
        return list;
    }

    public void setList(List<ContactWithCompanyRepresentation> list) {
        this.list = list;
    }

}
